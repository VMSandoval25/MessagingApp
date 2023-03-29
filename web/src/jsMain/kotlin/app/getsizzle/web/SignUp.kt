package app.getsizzle.web

import app.getsizzle.shared.Api.registerDietitian
import app.getsizzle.shared.data.model.Dietitian
import app.getsizzle.web.regex.emailReq
import app.getsizzle.web.regex.passwordReq
import app.getsizzle.web.regex.userIdReq
import csstype.ClassName
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p
import react.router.useNavigate
import react.useState

private val scope = MainScope()


val SignUp = FC<Props> {
    val navigate = useNavigate()
    val (message, setMessage) = useState("")

    div {
        className = ClassName("container-pages")

        div {
            className = ClassName("left-side")
            ReactHTML.img {
                src = "/pics/sizzle_white.png"
                alt = "sizzle logo"
            }
        }

        div {
            className = ClassName("right-side")

            h1{
                className = ClassName("register")
                +"Create Account"
            }
            signUpComponent {
                onSubmit = { fullname, userId, password, email -> // differet onSubmit than the other??
                    val tempDietitian = Dietitian(fullname, userId, password, email)
                    scope.launch {
                        if(fullname == "" || userId =="" || password == "" || email =="")
                        {
                            setMessage("Not all fields entered")
                        }
                        else{
                            if (passwordReq.matches(password)) {
                                if (emailReq.matches(tempDietitian.email)) {
                                    if (userIdReq.matches(tempDietitian.userId)) {
//                                        setMessage(registerDietitian(tempDietitian))
                                        val messageString = registerDietitian(tempDietitian)
                                        if(messageString == "Successfully registered")
                                            navigate("/")
                                        else setMessage(messageString)
                                    } else {
                                        setMessage("Username invalid. Please try again")
                                    }
                                } else {
                                    setMessage("Email invalid. Please try again")
                                }
                            } else {
                                setMessage("Password invalid. Please try again")
                            }
                        }
                    }
                }
            }
            p{
                id = "error-message"
                if(message != ""){
                    +"** $message **"
                }

            }
        }
    }
}
