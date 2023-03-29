package app.getsizzle.web

import csstype.ClassName
import kotlinx.browser.document
import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.events.ChangeEventHandler
import react.dom.events.FormEventHandler
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.br
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.router.useNavigate
import react.useState

external interface chatPageProps : Props {
    var onSubmit: (String) -> Unit
}

val chatPageComponent = FC<chatPageProps> { props -> // this takes onsubmit which is a string
    val (message, setMessage) = useState("")

    val submitHandler: FormEventHandler<HTMLFormElement> = { // on submit do this
        it.preventDefault() // prevents autosubmit or something
        setMessage("")
        props.onSubmit(message)
    }
    val changeHandlerName: ChangeEventHandler<HTMLInputElement> = {
        setMessage(it.target.value)
    }
    div {
        form {
            onSubmit = submitHandler

            label {
                htmlFor = "name"
                input {
                    id = "users"
                    type = InputType.text
                    onChange = changeHandlerName
                    name = "name"
                    value = message
                }
            }
            input {
                id = "send"
                type = InputType.submit
                className = ClassName("btn")
                value = "send"
            }
        }
    }
}