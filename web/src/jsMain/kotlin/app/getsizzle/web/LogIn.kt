package app.getsizzle.web

import app.getsizzle.shared.Api.findDietitian
import app.getsizzle.shared.Api.loginDietitian
import app.getsizzle.shared.User
import app.getsizzle.web.regex.emailReq
import app.getsizzle.web.regex.userIdReq
import csstype.ClassName
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.p
import react.router.useNavigate
import react.useEffectOnce
import react.useState

private val scope = MainScope()

val LogIn = FC<Props> {
    var user by useState(emptyList<User>())
    val(message, setMessage) = useState("")
    val navigate = useNavigate()

    useEffectOnce {
        scope.launch{
            if(findDietitian() != 0){
                navigate("/chatroom")
            }
        }
    }

        div {
            className = ClassName("container-pages")

            div {
                className = ClassName("left-side")

                img{
                    src = "/pics/sizzle_white.png"
                    alt = "sizzle logo"
                }

            }

            div {
                className = ClassName("right-side")

                div{
                    className = ClassName("image-login-container")
                    img{
                        src="https://i.pinimg.com/736x/1e/f8/15/1ef8156889dba99417ff2b3a6d99988d.jpg"
                    }
                }


                logInComponent{
                    onSubmit= { input1, input2 -> // differet onSubmit than the other??
                        val isEmail: Boolean = emailReq.matches(input1)
                        val isUsername: Boolean = userIdReq.matches(input2)
                        scope.launch {
                            if(isEmail || isUsername){
                                setMessage(loginDietitian(input1, input2))
                                if(loginDietitian(input1, input2) == "Log in successful") {
                                    navigate("/chatroom")
                                }
                            }
                            else{
                                setMessage("Invalid username")
                            }
                        }
                    }
                }

                p{
                    id = "error-message"
                    if(message!="" && message!="Log in successful")
                        +"**$message**"
                }

            }
        }
}

