package app.getsizzle.web

import kotlinx.browser.document
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter


val Application = FC<Props> {
    BrowserRouter {
        Routes {
            Route {
                path = "/signUp"
                element = SignUp.create()
            }
            Route {
                path = "/chatpage"
                element = ChatPage.create()
            }
            Route {
                //index=true
                path="/"
                element = LogIn.create()
            }
            Route {
                path="/home"
                element = Home.create()
            }
            Route {
                path="/chatroom"
                element = PatientsList.create()
            }
            Route{
                path="/patientprofile"
                element = PatientProfile.create()
            }
        }
    }
}

fun main() {
    val container = document.getElementById("root") ?: error("Couldn't find container!")
    createRoot(container).render(Application.create())

}