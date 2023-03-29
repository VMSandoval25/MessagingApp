package app.getsizzle.web

import csstype.ClassName
import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.events.ChangeEventHandler
import react.dom.events.FormEventHandler
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.router.useNavigate
import react.useState

external interface SignUpProps : Props {
    var onSubmit: (String,String, String, String) -> Unit
}

val signUpComponent = FC<SignUpProps> { props -> // this takes onsubmit which is a string
    val (fullname, setFullname) = useState("")
    val(userId, setUserId) = useState("")
    val (pass, setPass) = useState("")
    val (email, setEmail) = useState("")

    val submitHandler: FormEventHandler<HTMLFormElement> = { // on submit do this
        it.preventDefault() // prevents autosubmit or something
        setFullname("")
        setUserId("")
        setPass("")
        setEmail("")
        props.onSubmit(fullname, userId, pass, email)
    }
    val navigate = useNavigate()

    val changeHandlerName: ChangeEventHandler<HTMLInputElement> = {
        setFullname(it.target.value)
    }
    val changeHandlerId: ChangeEventHandler<HTMLInputElement> = {
        setUserId(it.target.value)
    }
    val changeHandlerPass: ChangeEventHandler<HTMLInputElement> = {
        setPass(it.target.value)
    }
    val changeHandlerEmail: ChangeEventHandler<HTMLInputElement> = {
        setEmail(it.target.value)
    }


    div {
        form {
            className = ClassName("form1")
            onSubmit = submitHandler

            label {
                //htmlFor = "name"

                input {
                    id = "submit2"
                    type = InputType.text
                    onChange = changeHandlerName
                    name = "name"
                    value = fullname
                    placeholder = "Full Name"
                }
            }
            label {
                //htmlFor = "userId"

                input {
                    id = "submit2"
                    type = InputType.text
                    onChange = changeHandlerId
                    name = "userId"
                    value = userId
                    placeholder = "username"
                }
            }
            label {
                input {
                    id = "submit2"
                    type = InputType.password
                    onChange = changeHandlerPass
                    name = "password"
                    value = pass
                    placeholder = "password"
                }
            }

            label {
                input {
                    id = "submit2"
                    type = InputType.email
                    onChange = changeHandlerEmail
                    name = "email"
                    value = email
                    placeholder = "email"
                }
            }

            div {
                //className = ClassName("signUp")
                input {
                    id = "button1"
                    type = InputType.submit
                    value = "REGISTER"
                }
            }

            div{
                className = ClassName("text-login")
                +"Have an account? Log in"
                onClick = {
                    navigate("/")
                }
            }
        }
    }

}
