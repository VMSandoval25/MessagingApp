package app.getsizzle.shared

import app.getsizzle.shared.data.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

expect val endpoint: String

val jsonClient: HttpClient
    get() = HttpClient () {
        install(ContentNegotiation) {
            json()
        }
        install(Logging)
        {
            logger=object: Logger {
                override fun log(message: String) {
                    co.touchlab.kermit.Logger.d(message)
                }
            }
            level= LogLevel.ALL
        }
        install(WebSockets)
        defaultRequest {
            url {
                protocol = URLProtocol.HTTP
                host = endpoint
                port = 9090
                contentType(type = ContentType.Application.Json)
            }
        }

    }

class WebSocketClient(){
    data class EmittableValue<T>(
        val value: T,
        val counter:Int,
        val shouldEmit: Boolean = true
    )
    var counter:Int=0
    private val _chatMessage = MutableStateFlow(EmittableValue<String>("",counter,false))
    val chatMessage = _chatMessage.asStateFlow()
        .filter { it.shouldEmit }
        .map { it.value }


    private var _alert = MutableStateFlow("")
    val alert: StateFlow<String> = _alert



    val _job by lazy {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                connect()
                startChat()
            } catch (e: Exception) {

            }

        }

    }
    private var _session: WebSocketSession? = null

    private suspend fun connect() {
        _session = jsonClient.webSocketSession(
            method = HttpMethod.Get,
            host = endpoint,
            port = 9090,
            path = "/chat"
        )
    }
    private suspend fun startChat() {
        try {
            receive()

        } catch (e: Exception) {
            if (e is ClosedReceiveChannelException) {
                _alert.emit("Disconnected. ${e.message}.")
            } else if (e is WebSocketException) {
                _alert.emit("Unable to connect.")
            }
//            withTimeout(5000) {
//                CoroutineScope(Dispatchers.Default).launch { startChat() }
//            }
        }
    }

    fun send(message: String) {
        CoroutineScope(Dispatchers.Default).launch {
            _session?.send(Frame.Text(message))
        }
    }


    private suspend fun receive() {
        while (true) {
            val frame = _session?.incoming?.receive()
            if (frame is Frame.Text) {
                extractChatMessage(frame.readText())
            }
            //_chatMessage.emit("") // Emptied message. Fixes bug part of issue #110 GITHUB
        }
    }

    private suspend fun extractChatMessage(it: String) {
        println("extract: $it")
        try{
            // update db+
            counter+=1
            if(it=="")
            {
                _chatMessage.emit(EmittableValue(it,counter,false))
            }
            else
                _chatMessage.emit(EmittableValue(it,counter,true))

            //println("hello: $chatMessage")

            _alert.emit("")
        }catch (th: Throwable){
            _alert.emit(it)
        }
    }
    suspend fun close(){
        _chatMessage.emit(EmittableValue("",counter,false))
        //_session?.close(CloseReason(CloseReason.Codes.NORMAL, "Closing WebSocket"))
    }



}

val socketClient= WebSocketClient()

object Api {
    suspend fun findDietitian(): Int {
        return jsonClient.get(Dietitian.path).body()
    }

    suspend fun getDietitian(groupId: Int): String {
        return jsonClient.get(Dietitian.path + "/userId/${groupId}").body()
    }
    suspend fun getDietitianObject(groupId: Int): Dietitian {
        return jsonClient.get(Dietitian.path + "/Dietitian/${groupId}").body()
    }

    suspend fun dummyChatRoom() {
        return jsonClient.post(ChatRoom.path + "/dummy").body()
    }

    suspend fun findChatRoomId(dietitianId: String, patientId: String): Int {
        return jsonClient.get(ChatRoom.path + "/${dietitianId}/${patientId}").body()
    }

    suspend fun registerPatient(patientObject: Patient): String {
        return jsonClient.post(Patient.path + "/register") {
            contentType(ContentType.Application.Json)
            setBody(patientObject)
        }.body()
    }

    suspend fun loginPatient(credential: String, password: String): String {
        return jsonClient.post(Patient.path + "/login") {
            contentType(ContentType.Application.Json)
            setBody(LogCredentials(credential, password))
        }.body()
    }

    suspend fun addDummyPatient() {
        jsonClient.post(Patient.path) {
            contentType(ContentType.Application.Json)
            setBody("")
        }

    }

    suspend fun getPatientList(groupId: Int): List<Patient> {
        return jsonClient.get(Patient.path + "/${groupId}").body()
    }

    suspend fun getSpecificPatient(patientId: String): List<Patient>{
        return jsonClient.get(Patient.path + "/specific/${patientId}").body()
    }

    suspend fun addDummyDietitian(): List<Dietitian> {
        return jsonClient.post(Dietitian.path).body()
    }

    suspend fun getGroupID(email: String): Int {
        return jsonClient.get(Patient.path + "/userId/email/${email}").body()
    }
    //--------------------------------------------
    suspend fun updatePatientAge(patientId: String, newAge:Int){
        jsonClient.patch(Patient.path+"/update/age/${patientId}/${newAge}")
    }
    suspend fun updatePatientHeight(patientId: String, newHeight:Int){
        jsonClient.patch(Patient.path+"/update/height/${patientId}/${newHeight}")
    }

    suspend fun updatePatientWeight(patientId: String, newWeight:Double){
        jsonClient.patch(Patient.path+"/update/weight/${patientId}/${newWeight}")
    }

    suspend fun updatePatientBloodPressure(patientId: String, newPressure:String){

        jsonClient.patch(Patient.path+"/update/pressure/${patientId}/${convertSlashToStar(newPressure)}")
    }

    suspend fun updatePatientAllergens(patientId: String, newAllergens:String){
        jsonClient.patch(Patient.path+"/update/allergens/${patientId}/${convertSlashToStar(newAllergens)}")
    }

    suspend fun updatePatientGender(patientId: String, newGender: String){
        jsonClient.patch(Patient.path+"/update/gender/${patientId}/${newGender}")
    }

    suspend fun updatePatientLastVisit(patientId: String, lastVisit:String): String{
        return jsonClient.patch(Patient.path+"/update/recent/visit/${patientId}/${convertSlashToStar(lastVisit)}").body()
    }
    suspend fun updatePatientPrescriptions(patientId: String, currentPrescriptions:String){
        jsonClient.patch(Patient.path+"/update/prescriptions/${patientId}/${convertSlashToStar(currentPrescriptions)}")
    }
    suspend fun updatePatientNotes(patientId: String, notes:String){
        jsonClient.patch(Patient.path+"/update/note/${patientId}/${convertSlashToStar(notes)}")
    }
    //------------------------------------------
    suspend fun registerDietitian(dietitianObject: Dietitian): String {
        return jsonClient.post(Dietitian.path + "/register") {
            contentType(ContentType.Application.Json)
            setBody(dietitianObject)
        }.body()

    }

    suspend fun loginDietitian(credential: String, password: String): String {
        return jsonClient.post(Dietitian.path + "/login") {
            contentType(ContentType.Application.Json)
            setBody(LogCredentials(credential, password))
        }.body()
    }
    suspend fun logoutDietitian(): String {
        return jsonClient.post(Dietitian.path + "/logout") {
            contentType(ContentType.Application.Json)
            setBody("")
        }.body()
    }

    suspend fun addMessage(message: Message) {
        jsonClient.post(Message.path) {
            contentType(ContentType.Application.Json)
            setBody(message)
        }
    }
    suspend fun updateMessageRead(message: Message) {
        jsonClient.patch(Message.path) {
            contentType(ContentType.Application.Json)
            setBody(message)
        }
    }

    suspend fun findUnreadMessage(id: String): Boolean {
        return jsonClient.get(Message.path + "/unread/${id}").body()
    }

    suspend fun getMessage(chatroomId: Int): List<Message> {
        //http://10.0.2.2:9090/
        return jsonClient.get(Message.path + "/${chatroomId}").body()
    }

    suspend fun findSenderID(): String {
        return jsonClient.get(Dietitian.path + "/sender").body()
    }

    suspend fun getPatient(userId: String): Patient {
        return jsonClient.get(Patient.path + "/userId/${userId}").body()
    }

    suspend fun addChatRoom(patient: Patient): String {
        return jsonClient.post(ChatRoom.path + "/addChatRoom") {
            contentType(ContentType.Application.Json)
            setBody(patient)
        }.body()
    }


}
