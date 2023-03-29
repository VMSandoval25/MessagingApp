package app.getsizzle.web

import app.getsizzle.shared.data.model.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.util.*
import io.ktor.websocket.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue
import java.io.File
import java.time.Duration
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

val client = KMongo.createClient().coroutine
val database = client.getDatabase("messagingDatabase")
val patientCollection = database.getCollection<Patient>()
val dietitianCollection = database.getCollection<Dietitian>()
val messageCollection = database.getCollection<Message>()
val chatRoomCollection = database.getCollection<ChatRoom>()

data class UserSession(
    val userId:String,
    val groupId: Int
)

class Connection2(val session: DefaultWebSocketSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val name = "${lastId.getAndIncrement()}"
}
object AuthConfig{
    const val sessionAuth = "auth_session"
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 9090
    embeddedServer(Netty, 9090) {
        install(Sessions) {
            val secretSignKey = hex("6819b57a326945c1968f45236589")
            cookie<UserSession>("user_session", directorySessionStorage(File("build/.sessions"), cached = true)){
                cookie.path = "/"
                transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
            }
        }
        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(15)
            timeout = Duration.ofSeconds(15)
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Delete)
            allowMethod(HttpMethod.Put)
            anyHost()
        }
        install(Compression) {
            gzip()
        }
        routing {
            get("/") {
                call.respondText(
                    this::class.java.classLoader.getResource("logIn.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            get("/signUp") {
                call.respondText(
                    this::class.java.classLoader.getResource("signUp.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            get("/chatpage") {
                call.respondText(
                    this::class.java.classLoader.getResource("chatPage.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            get("/home") {
                call.respondText(
                    this::class.java.classLoader.getResource("home.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            get("/chatroom") {
                call.respondText(
                    this::class.java.classLoader.getResource("patientsList.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            get("/patientprofile") {
                call.respondText(
                    this::class.java.classLoader.getResource("patientProfile.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            static("/") {
                resources("")
            }

            route(Patient.path){
                get("/userId/email/{email}") {
                    val email:String = call.parameters["email"]!!
                    call.respond(dietitianCollection.findOne(Dietitian::email eq email)?.groupId!!)
                }
                post("/register") {
                    val temp: Patient = call.receive<Patient>()
                    if (patientCollection.findOne(Patient::userId eq temp.userId) == null) {
                        if (patientCollection.findOne(Patient::email eq temp.email) == null) {
                            patientCollection.insertOne(temp)
                            call.respondText("Successfully registered", status = HttpStatusCode.OK)
                        } else {
                            call.respondText("Email already used")
                        }
                    } else {
                        call.respondText("Username already used")
                    }
                }
                post("/login") {
                    val params: LogCredentials = call.receive<LogCredentials>()
                    var pt = patientCollection.findOne(Patient::userId eq params.credential)
                    if (pt == null)
                        pt = patientCollection.findOne(Patient::email eq params.credential)
                    if (pt == null) {
                        call.respondText("email / username not registered")
                    } else {
                        if (pt.password == params.password) {
                            //call.sessions.set(UserSession(temp.userId, groupId = temp.groupId))
                            call.respondText("Log in successful")
                        } else call.respondText("Incorrect password")
                    }
                }
                get("/userId/{userId}") {
                    val userId: String = call.parameters["userId"].toString()
                    val record = patientCollection.findOne(Patient::userId eq userId)
                    if (record != null) {
                        call.respond(record)
                    } else{
                        call.respond(0)
                    }
                }
                get("/{groupId}") {
                    val groupId = call.parameters["groupId"]?.toInt()
                    call.respond(patientCollection.find(Patient::groupId eq groupId).toList())
                }
                get("/specific/{patientId}") {
                    val patientId = call.parameters["patientId"]
                    call.respond(patientCollection.find(Patient::userId eq patientId).toList())
                }
                patch("/update/age/{patientId}/{newAge}") {
                    val patientId = call.parameters["patientId"].toString()
                    val newAge:Int? = call.parameters["newAge"]?.toInt()

                    patientCollection.updateOne(Patient::userId eq patientId, setValue(Patient::age, newAge))
                }
                patch("/update/height/{patientId}/{newHeight}") {
                    val patientId = call.parameters["patientId"].toString()
                    val newHeight:Int? = call.parameters["newHeight"]?.toInt()

                    patientCollection.updateOne(Patient::userId eq patientId, setValue(Patient::height, newHeight))
                }
                patch("/update/weight/{patientId}/{newWeight}") {
                    val patientId = call.parameters["patientId"].toString()
                    val newWeight = call.parameters["newWeight"]?.toDouble()

                    patientCollection.updateOne(Patient::userId eq patientId, setValue(Patient::weight, newWeight))
                }

                patch("/update/pressure/{patientId}/{newPressure}") {
                    val patientId = call.parameters["patientId"].toString()
                    val newPressure = call.parameters["newPressure"].toString()

                    patientCollection.updateOne(Patient::userId eq patientId, setValue(Patient::bloodPressure, newPressure))
                }

                patch("/update/allergens/{patientId}/{newAllergens}"){
                    val patientId = call.parameters["patientId"].toString()
                    val newAllergens = call.parameters["newAllergens"].toString()

                    patientCollection.updateOne(Patient::userId eq patientId, setValue(Patient::allergens, newAllergens))
                }
                patch("/update/gender/{patientId}/{newGender}"){
                    val patientId = call.parameters["patientId"].toString()
                    val newGender = call.parameters["newGender"].toString().uppercase()

                    patientCollection.updateOne(Patient::userId eq patientId, setValue(Patient::gender, Gender.valueOf(newGender)))
                }
                patch("/update/recent/visit/{patientId}/{lastVisit}"){
                    val patientId = call.parameters["patientId"].toString()
                    val newVisit = call.parameters["lastVisit"].toString()
                    patientCollection.updateOne(Patient::userId eq patientId, setValue(Patient::lastVisit, newVisit))
                    call.respondText("hello world")
                }

                patch("/update/prescriptions/{patientId}/{currentPrescriptions}"){
                    val patientId = call.parameters["patientId"].toString()
                    val newPrescriptions = call.parameters["currentPrescriptions"].toString()

                    patientCollection.updateOne(Patient::userId eq patientId, setValue(Patient::currentPrescriptions, newPrescriptions))
                }

                patch("/update/note/{patientId}/{notes}"){
                    val patientId = call.parameters["patientId"].toString()
                    val newNote = call.parameters["notes"].toString()

                    patientCollection.updateOne(Patient::userId eq patientId, setValue(Patient::notes, newNote))
                }


            }
            route(Dietitian.path) {

                post("/register") {
                    val temp: Dietitian = call.receive<Dietitian>()
                    if (dietitianCollection.findOne(Dietitian::userId eq temp.userId) == null) {
                        if (dietitianCollection.findOne(Dietitian::email eq temp.email) == null) {
                            dietitianCollection.insertOne(temp)
                            call.respondText("Successfully registered", status = HttpStatusCode.OK)
                        } else {
                            call.respondText("Email already used")
                        }
                    } else {
                        call.respondText("Username already used")
                    }
                }
                post("/login") {
                    val params: LogCredentials = call.receive<LogCredentials>()
                    call.sessions.clear<UserSession>()
                    var message:String = ""
                    var temp = dietitianCollection.findOne(Dietitian::userId eq params.credential)
                    if (temp == null){
                        message="username not registered"
                        temp = dietitianCollection.findOne(Dietitian::email eq params.credential)
                    }
                    if (temp == null) {
                        //call.respondText("email / username not registered")
                        message="email or username not registered"
                    }
                    if(message==""){
                        if (temp != null) {
                            if (temp.password == params.password) {
                                call.sessions.set(UserSession(temp.userId, groupId = temp.groupId))
                                call.respondText("Log in successful")
                            } else call.respondText("Incorrect password")
                        }
                    }
                    call.respondText(message)
                }
                post("/logout"){
                    call.sessions.clear<UserSession>()
                    val sesh = call.sessions.get<UserSession>()
                    if(sesh==null)
                    {
                        call.respondText("Successful log out")
                    }
                    else{
                        call.respondText("Not logged out")
                    }
                }
                get {
                    val record = call.sessions.get<UserSession>()
                    if (record != null) {
                        call.respond(record.groupId)
                    } else {
                        call.respond(0)
                    }
                }
                get ("/chatroomId") {
                    val record = call.sessions.get<UserSession>()
                    if (record != null) {
                        call.respond(record.groupId)
                    } else {
                        call.respond(0)
                    }
                }
                get("/sender"){
                    val record = call.sessions.get<UserSession>()
                    if (record != null) {
                        call.respondText(record.userId)
                    } else {
                        call.respondText("0")
                    }
                }
                get("/userId/{groupId}") {
                    val groupId:Int = call.parameters["groupId"]?.toInt()!!
                    call.respond(dietitianCollection.findOne(Dietitian::groupId eq groupId)?.userId!!)

                }
                get("/Dietitian/{groupId}") {
                    val groupId:Int = call.parameters["groupId"]?.toInt()!!
                    call.respond(dietitianCollection.findOne(Dietitian::groupId eq groupId)!!)
                }

                get("/{userId}/{email}") {
                    val id: String = call.parameters["userId"].toString()
                    val email: String = call.parameters["email"].toString()
                    call.respond(
                        dietitianCollection.findOne(Dietitian::userId eq id) == null &&
                                dietitianCollection.findOne(Dietitian::email eq email) == null
                    )
                }
                options {
                    call.respond(dietitianCollection.find().toList())
                }
            }
            route(Message.path) {
                post{
                    val msg: Message = call.receive<Message>()
                    messageCollection.insertOne(msg)
                    // updating lastSentAt on chatRoomCollection
                    val chatRoomId = messageCollection.findOne(Message::messageId eq msg.messageId)?.chatRoomId
                    val patientId = chatRoomCollection.findOne(ChatRoom::chatRoomId eq chatRoomId)?.patientId
                    patientCollection.updateOne(Patient::userId eq patientId, setValue(Patient::lastSentAt, msg.sentTime))

                    call.respond(HttpStatusCode.OK)
                }
                get("/{chatroomId}"){
                    val chatroomId = call.parameters["chatroomId"]?.toInt()
                    call.respond(messageCollection.find(Message::chatRoomId eq chatroomId).toList())
                }

                get("/unread/{id}"){
                    val patientId = call.parameters["id"].toString()
                    val record = messageCollection.findOne(Message::senderID eq patientId, Message::isRead eq false)
                    call.respond(record != null)
                }

                patch{
                    messageCollection.updateOne(Message::messageId eq call.receive<Message>().messageId, setValue(Message::isRead, true))
                }

            }
            route(ChatRoom.path) {
                post("/dummy") {
                    val dummy = ChatRoom(1, "DietitianH", "PatientY")
                    val dummyChatRoom: ChatRoom = dummy
                    chatRoomCollection.insertOne(dummyChatRoom)
                }
                post("/addChatRoom") {
                    val patient: Patient = call.receive<Patient>()
                    val dietitian = dietitianCollection.findOne(Dietitian::groupId eq  patient.groupId)
//                    if (patientCollection.findOne(Patient::groupId eq patient.groupId) != null) {
                        if (dietitian != null) {
                            chatRoomCollection.insertOne(
                                ChatRoom(
                                    chatRoomId = patient.userId.hashCode(),
                                    dietitianId = dietitian.userId, patientId = patient.userId
                                )
                            )
                            call.respondText("Successfully added chatRoom", status = HttpStatusCode.OK)
                        }
                        else{
                            call.respondText("Dietitian does not exist")
                        }
//                    }else {
//                        call.respondText("GroupId does not exist")
//                    }
                }
                get("/{dietitianId}/{patientId}") {
                    val dietitianId: String = call.parameters["dietitianId"].toString()
                    val patientId: String = call.parameters["patientId"].toString()
                    val userSession = call.sessions.get<UserSession>()

                    val record = chatRoomCollection.findOne(ChatRoom::dietitianId eq dietitianId, ChatRoom::patientId eq patientId)
                    if (record != null) {
                        call.respond(record.chatRoomId)
                    } else{
                        call.respond(0)
                    }
                }
            }
        }
        routing {
            val connections = Collections.synchronizedSet<Connection2?>(LinkedHashSet())

            webSocket("/chat") {
                val thisConnection = Connection2(this)
                connections += thisConnection
                try {
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        val textWithUsername = "[${thisConnection.name}]: $receivedText"
                        val text="$receivedText"
                        connections.minus(thisConnection).forEach {
                            it.session.send(receivedText)
                        }
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)

                }
                finally {
                    println("Removing $thisConnection!")
                    connections -= thisConnection
                }
            }
        }

    }.start(wait = true)
}
