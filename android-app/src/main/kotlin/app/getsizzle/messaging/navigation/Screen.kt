package app.getsizzle.messaging.navigation

sealed class Screen(val route:String){
    object Login: Screen(route= "login_screen")
    object Register: Screen(route="register_screen")
    object Messaging: Screen(route="messaging_screen")
    object Banana: Screen(route="banana_screen")
    object Chatroom: Screen(route="chatroom_screen")
    object Profile: Screen(route="profile_screen")
}
