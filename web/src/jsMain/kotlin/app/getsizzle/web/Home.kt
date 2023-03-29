package app.getsizzle.web

import app.getsizzle.shared.Api.addDummyDietitian
import app.getsizzle.shared.Api.addDummyPatient
import app.getsizzle.shared.Api.dummyChatRoom
import csstype.*
import emotion.react.css
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.nav
import react.dom.html.ReactHTML.ul
import react.router.useNavigate

private val scope = MainScope()


val Home = FC<Props> {
    val navigate = useNavigate()
    nav {
        css { }
        div {
            ul {
                css{height = 10.vh; marginLeft = 5.vw;}
                li {
                    css { display = Display.inline; margin = 5.vh; cursor = Cursor.default;}
                    +"List Me"
                }
                li {
                    css { display = Display.inline; margin = 5.vh; cursor = Cursor.pointer;}
                    +"LogIn"
                    onClick = {
                        navigate("/")
                    }
                }
                li {
                    css { display = Display.inline; margin = 5.vh; cursor = Cursor.pointer;}
                    +"Sign Up"
                    onClick = {
                        navigate("/signUp")
                    }
                }
                li{
                    css { display = Display.inline; margin = 5.vh; cursor = Cursor.pointer;}
                    +"Dummy Patient"
                    onClick = {
                        scope.launch {
                            addDummyPatient()
                        }
                    }
                }
                li{
                    css { display = Display.inline; margin = 5.vh; cursor = Cursor.pointer;}
                    +"Dummy Dietitian"
                    onClick = {
                        scope.launch {
                            addDummyDietitian()
                        }
                    }
                }
                li{
                    css { display = Display.inline; margin = 5.vh; cursor = Cursor.pointer;}
                    +"Dummy ChatRoom"
                    onClick = {
                        scope.launch {
                            dummyChatRoom()
                        }
                    }
                }
            }
        }
    }
    div{
        h1{
            +"Welcome"
        }
    }
}