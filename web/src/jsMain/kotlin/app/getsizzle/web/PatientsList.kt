package app.getsizzle.web

import app.getsizzle.shared.Api.findDietitian
import app.getsizzle.shared.Api.findSenderID
import app.getsizzle.shared.Api.findUnreadMessage
import app.getsizzle.shared.Api.getPatientList
import app.getsizzle.shared.Api.logoutDietitian
import app.getsizzle.shared.data.model.Patient
import app.getsizzle.shared.socketClient
import csstype.*
import csstype.Position.Companion.absolute
import emotion.react.css
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.hr
import react.dom.html.ReactHTML.i
import react.dom.html.ReactHTML.iframe
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.ul
import react.router.useNavigate
import react.useEffectOnce
import react.useState


private val scope = MainScope()

val PatientsList = FC<Props> {
    var patientList by useState(emptyList<Patient>())
    var fullPatientList by useState(emptyList<Patient>())
    val navigate = useNavigate()
    val patientMapUnread by useState(mutableMapOf<String, Boolean>())
    var activeUser by useState("")
    var activePatient by useState("")
    var activePatientFullName by useState("")
    var activePatientFullNameForPrf by useState("")
    var navigateIFrameMsg by useState("")
    var navigateIFramePrf by useState("")
    var counterMsg by useState(0)
    var counterKids = 0
    var counterAdults = 0
    var counterSeniors = 0

    val elementMessage = document.getElementById("message")
    val elementPatientsList = document.getElementById("patients-list")
    val elementIFrameMsg = document.getElementById("iframe-msg")
    val elementIFramePrf = document.getElementById("iframe-prf")
    val elementAptNum = document.getElementById("apt-num")
    val elementProfile = document.getElementById("profile")
    val elementCross = document.getElementById("cross")
    val elementNav = document.getElementById("nav")
    val rectMessage = elementMessage?.getBoundingClientRect()
    val rectPatientsList = elementPatientsList?.getBoundingClientRect()
    val rectAptNum = elementAptNum?.getBoundingClientRect()
    val rectProfile = elementProfile?.getBoundingClientRect()

    var arrayMonthsApts by useState(mutableListOf<Int>())
    var arrayDatesApts by useState(mutableListOf<Int>())
    var arrayTimeApts by useState(mutableListOf<Int>())
    var arrayPatientApts by useState(mutableListOf<Int>())

    val elementScrollBottom = document.getElementById("chat-container")
    var emptyPatientAnnoucement by useState(false)

    useEffectOnce {
        scope.launch {
            if(!socketClient._job.isActive){
                socketClient._job.start()
            }
        }
        scope.launch{
            if(findDietitian() == 0)
                navigate("/")
            activeUser = findSenderID()
            if(getPatientList(findDietitian()).isNotEmpty()) {
                val randomNum = (0..(getPatientList(findDietitian()).size)).random()
                if(randomNum != 0)
                {
                    for (i in 1..randomNum) {
                        val newPatient = (0 until getPatientList(findDietitian()).size).random()
                        if (!arrayPatientApts.contains(newPatient))
                        {
                            arrayPatientApts.add(newPatient)
                            arrayMonthsApts.add((1..12).random())
                            arrayDatesApts.add((1..28).random())
                            arrayTimeApts.add((1..6).random())
                        }
                    }
                }
            }
            getPatientList(findDietitian()).forEach { patient->
                patientMapUnread.put(patient.userId, findUnreadMessage(patient.userId))
            }
            patientList = getPatientList(findDietitian())
            fullPatientList = getPatientList(findDietitian())

            if(getPatientList(findDietitian()).isEmpty()) emptyPatientAnnoucement = true

            socketClient.chatMessage.collect {
                getPatientList(findDietitian()).forEach { patient ->
                    patientMapUnread.put(patient.userId, findUnreadMessage(patient.userId))
                }
                patientList = getPatientList(findDietitian())
                fullPatientList = getPatientList(findDietitian())
            }
        }
    }
    div{
        className = ClassName("navbar")
        id = "nav"
        div{
            className = ClassName("S-logo-container")
            ReactHTML.img {
                src = "/pics/sizzle_s.png"
                alt = "Sizzle S"
                className = ClassName("S-logo")
            }
            SearchComponent {
                onSubmit = {search ->
                    patientList = fullPatientList.filter { it.fullName.uppercase().contains(search.uppercase()) }
                }
            }
        }
        div{
            className = ClassName("item-container")
            p{
                className = ClassName("item-nav")
                +"Welcome, $activeUser"
            }
            input{
                type = InputType.button
                value = "Log Out"
                className = ClassName("logout-button")
                onClick = {
                    scope.launch {
                        logoutDietitian()
                        navigate("/")
                    }
                }
            }
        }
    }

    hr{css {height = 1.px; backgroundColor = Color("#C6C6C6"); border = None.none; margin = 0.px;}}

    div {
        className = ClassName("container")
        ul {
            className = ClassName("patients-list")
            id = "patients-list"
            // Instead of using lastRD on ChatRoom, we used lastSentAt on Patient to sort
            // A patient cannot have multiple dietitians with current design
            if(fullPatientList.isNotEmpty() && patientList.isEmpty()){
                div{
                    className = ClassName("empty-message")
                    p{
                        +"No results found..."
                    }
                }
            }
            if(emptyPatientAnnoucement){
                div{
                    className = ClassName("empty-message")
                    p{
                        +"You have no current patients..."
                    }
                    p{
                        +"Want to connect with your clients?"
                    }
                    p{
                        +"Tell them about Sizzle!"
                    }
                }
            }


            fullPatientList.forEach { patient->
                if (patient.age?.compareTo(18) == -1 || patient.age?.compareTo(18) == 0) {
                    // if less than equal to 18, then kids
                    counterKids += 1
                }else if (patient.age?.compareTo(64) == -1 || patient.age?.compareTo(64) == 0) {
                    // if less than equal to 64, then adults
                    counterAdults += 1
                } else if (patient.age?.compareTo(64) == 1){
                    // if greater than 64
                    counterSeniors += 1
                }
            }
            patientList.sortedByDescending(Patient::lastSentAt).forEach { patient ->
                div {
                    className = ClassName("patient-list")
                    p {
                        className = ClassName("list-text")
                        +patient.fullName
                    }
                    if(patientMapUnread[patient.userId] == true){
                        p{
                            className = ClassName("unread-message")
                            +"●"
                        }
                    }
                    div {
                        p {
                            className = ClassName("list-info")
                            +"${patient.gender} ・ ${patient.age} Years ・ ${patient.height?.div(12)}' ${patient.height?.mod(12)}\""
                        }
                    }
                    button {
                        className = ClassName("msg")
                        +"message"
                        onClick = {
                            // We need to encode patient's user ID as Dietitian can see their user ID
                            activePatient = patient.userId
                            activePatientFullName = patient.fullName
                            navigateIFrameMsg = "http://0.0.0.0:9090/chatpage?${patient.userId}"
                            elementIFrameMsg?.removeAttribute("style")
                            elementMessage?.setAttribute("style",
                                    "background: #FFC5AB; position: absolute; top:"+rectPatientsList?.top?.minus(window.innerWidth/100)+
                                    "px;left:"+rectMessage?.left?.minus(window.innerWidth/100)+
                                    "px;width:calc(30vw - 30px);height:calc(10vh - 2vw - 30px); visibility: visible;")
                            counterMsg = 0
                        }
                    }
                    button {
                        className = ClassName("view")
                        +"view"
                        onClick = {
                            activePatientFullNameForPrf = patient.fullName
                            navigateIFramePrf = "http://0.0.0.0:9090/patientprofile?${patient.userId}"
                            elementIFramePrf?.removeAttribute("style")
                            elementProfile?.setAttribute("class","profile-on")
                            elementCross?.setAttribute("class","cross")
                        }
                    }
                }
            }
        }

        iframe {
            // iframe for message
            css {position = absolute; top = rectMessage?.bottom?.px; left = rectMessage?.left?.px;width = 30.vw; height = 0.px;
                visibility = Visibility.hidden;}
            if (navigateIFrameMsg != "") {
                css {position = absolute; top = rectMessage?.bottom?.px; left = rectMessage?.left?.px; width = 30.vw;
                    height = rectAptNum?.bottom?.minus(rectPatientsList!!.top + 10*window.innerHeight/100 - 2*window.innerWidth/100)?.px;
                    border = 2.px; visibility = Visibility.visible;}
            }
            id = "iframe-msg"
            src = navigateIFrameMsg
        }
        iframe {
            // iframe for profile
            css {position = absolute; top = rectProfile?.bottom?.px; left = rectAptNum?.right?.px;width = 0.vw;
                height = rectAptNum?.bottom?.minus(rectPatientsList!!.top + 10*window.innerHeight/100 - 3*window.innerWidth/100 - 30)?.px;
                visibility = Visibility.hidden; transition = 0.7.s;}
            if (navigateIFramePrf != "") {
                css {position = absolute; top = rectProfile?.bottom?.px; left = rectAptNum?.left?.minus(window.innerWidth/100)?.px; width = 25.vw;
                    height = rectAptNum?.bottom?.minus(rectPatientsList!!.top + 10*window.innerHeight/100 - 3*window.innerWidth/100 - 30)?.px;
                    border = 2.px; visibility = Visibility.visible; transition = 0.7.s;}
            }
            id = "iframe-prf"
            src = navigateIFramePrf
        }
        div {
            className = ClassName("patient-num")
            p {
                className = ClassName("sub-title")
                +"Your Patients"
                div {
                    className = ClassName("frame-count")
                    h1 {
                        className = ClassName("count")
                        +fullPatientList.size.toString()
                    }
                    ul {
                        className = ClassName("dynamic-list")
                        li {
                            +"$counterKids kids"
                        }
                        li {
                            +"$counterAdults adults"
                        }
                        li {
                            +"$counterSeniors seniors"
                        }
                    }
                }
            }
        }
        div {
            className = ClassName("task-num")
            p {
                className = ClassName("sub-title")
                +"Active Tasks"
            }
            div {
                className = ClassName("frame-count")
                h1 {
                    className = ClassName("count")
                    +"4"
                }
                ul {
                    className = ClassName("dynamic-list")
                    li {
                        +"2 urgent"
                    }
                    li {
                        +"2 required"
                    }
                }
            }
        }
        div {
            className = ClassName("apt-num")
            id = "apt-num"
            p {
                className = ClassName("sub-title")
                +"Upcoming Appointments"
            }
            div {
                className = ClassName("frame-count-reverse")
                ul {
                    className = ClassName("dynamic-list")
                    if(arrayPatientApts.size > 0 && fullPatientList.isNotEmpty()){
                        for(i in 0 until arrayPatientApts.size){
                            li{
                                +"${arrayMonthsApts[i]} / ${arrayDatesApts[i]} @ ${arrayTimeApts[i]} pm with "
                                +fullPatientList[arrayPatientApts[i]].fullName
                                console.log("This is first patient 0: ${fullPatientList[0]}")
                            }
                            console.log("Value of i: $i")
                        }
                    }
                    else{
                        li{
                            +"No Appointments"
                        }
                    }
                }
                h1 {
                    className = ClassName("count")
                    +"${arrayPatientApts.size}"
                }
            }
        }
        div {
            className = ClassName("message")
            id = "message"
            if (activePatient != "" && counterMsg!=0){
                // Expandable
                p {
                    className = ClassName("p-patient-name")
                    +activePatientFullName
                }
                i {
                    className = ClassName("arrow up")
                    onClick = {
                        elementIFrameMsg?.removeAttribute("style")
                        elementMessage?.setAttribute("style",
                            "background: #FFC5AB; position: absolute; top:"+ rectPatientsList?.top?.minus(window.innerWidth/100)+
                                    "px;left:"+ rectMessage?.left?.minus(window.innerWidth/100) +
                                    "px;width:calc(30vw - 30px);height:calc(10vh - 2vw - 30px); visibility:visible")
                        counterMsg = 0
                    }
                }
            }else if (activePatient != "" && counterMsg==0) {
                // Shrinkable
                p {
                    className = ClassName("p-patient-name")
                    +activePatientFullName
                }
                i {
                    className = ClassName("arrow down")
                    onClick = {
                        elementIFrameMsg?.setAttribute("style","visibility:hidden; height: 0;")
                        elementMessage?.removeAttribute("style")
                        elementMessage?.setAttribute("style", "background: #FFC5AB; visibility:visible")
                        counterMsg++
                    }
                }
            }
        }
        div {
            className = ClassName("profile")
            id = "profile"
            p {
                className = ClassName("cross-hidden")
                id = "cross"
                +"╳"
                onClick = {
                    elementIFramePrf?.setAttribute("style","visibility:hidden; width: 0; left:100vw;")
                    elementProfile?.setAttribute("class", "profile")
                    elementCross?.setAttribute("class","cross-hidden")
                }
            }
        }
    }
}
