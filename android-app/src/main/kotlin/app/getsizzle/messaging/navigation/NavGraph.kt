package app.getsizzle.messaging

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.getsizzle.messaging.Chatroom.ChatroomUI
import app.getsizzle.messaging.Chatroom.ChatroomViewModel
import app.getsizzle.messaging.Login.LoginScreen
import app.getsizzle.messaging.Login.LoginViewModel
import app.getsizzle.messaging.Profile.ProfileUI
import app.getsizzle.messaging.Profile.ProfileViewModel
import app.getsizzle.messaging.Register.RegisterScreen
import app.getsizzle.messaging.Register.RegisterViewModel
import app.getsizzle.messaging.navigation.Screen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    context:MainActivity,
    loginViewModel: LoginViewModel = LoginViewModel(context),
    chatroomViewModel: ChatroomViewModel = ChatroomViewModel(),
    registerViewModel: RegisterViewModel = RegisterViewModel(),
    profileViewModel: ProfileViewModel = ProfileViewModel()){
    val lazyColumnState = rememberLazyListState()
    var patient= UserInfo()
    chatroomViewModel.lazyColumnState = lazyColumnState
    NavHost(navController=navController, startDestination= Screen.Login.route) {
        composable(
            route = Screen.Login.route
        ) {
                LoginScreen(
                    loginViewModel = loginViewModel,
                    showMessageScreen = {
                        navController.navigate(Screen.Chatroom.route)
                    },
                    showRegisterScreen = {
                        navController.navigate(Screen.Register.route)
                    }, patient = patient

                )

        }
        composable(
            route = Screen.Register.route
        ) {
            RegisterScreen(
                registerViewModel = registerViewModel,
                showLoginScreen = { navController.navigate(Screen.Login.route) },
            )
        }
        composable(
            route = Screen.Chatroom.route
        ) {
            ChatroomUI(
                viewModel = chatroomViewModel,
                lazyColumnState = lazyColumnState,
                showLastScreen = { navController.navigateUp() },
                showProfileScreen = { navController.navigate(Screen.Profile.route) },
                patient = patient
            )
        }
        composable(
            route = Screen.Banana.route
        ) {
            BananaScreen()
        }
        composable(
            route = Screen.Profile.route
        ) {

            ProfileUI(
                viewModel = profileViewModel,
                showLastScreen = { navController.navigateUp() },
                patient = patient
            )
        }
    }
}