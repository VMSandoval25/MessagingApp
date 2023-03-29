package app.getsizzle.messaging.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import app.getsizzle.messaging.R
import app.getsizzle.messaging.UserInfo
import app.getsizzle.messaging.collectEvents
import app.getsizzle.messaging.collectState
import app.getsizzle.messaging.ui.SizzleTheme
import app.getsizzle.shared.Api
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    showMessageScreen: () -> Unit,
    showRegisterScreen: () -> Unit,
    patient: UserInfo
) {
    val loginUiState by loginViewModel.collectState()
    val scope = rememberCoroutineScope()

    loginViewModel.collectEvents{
        when(it)
        {
            LoginViewModel.LoginEvent.ShowMessagingScreen -> showMessageScreen()
            LoginViewModel.LoginEvent.ShowRegisterScreen -> showRegisterScreen()
        }
    }
    LazyColumn(
        Modifier
            .navigationBarsPadding()
            .imePadding()
            .fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Image(
                painter = painterResource(id = R.drawable.full_sizzle_logo),
                contentDescription = "UserImage",
                modifier = Modifier
                    .requiredSize(270.dp)
            )
            OutlinedTextField(
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                value = loginUiState.username,
                singleLine = true,
                onValueChange = { loginViewModel.updateUsername(it) },
                label = { Text("Username") },
                modifier = Modifier
                    .focusTarget()
                    .width(300.dp),
            )
            TextField(
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                modifier=Modifier.width(300.dp),
                value = loginUiState.password,
                onValueChange = { loginViewModel.updatePassword(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
            )
            if (loginUiState.error != null) {
                Text(
                    text = loginUiState.error!!,
                )
            }

            Column() {
                Spacer(modifier=Modifier.height(12.dp))
                Button(
                    // Perform login logic here, such as sending a request to a server
                    // This will soon trigger an intent to login which triggers login logic, and then triggers redirect
                    onClick = {
                        scope.launch {
                            try {
                                patient.username = loginUiState.username
                                patient.password = loginUiState.password
                                var temp = Api.getPatient(loginUiState.username)
                                patient.email = temp.email
                                patient.groupId = temp.groupId!!
                                patient.fullname = temp.fullName
                                patient.dieticianId = Api.getDietitian(patient.groupId)
                                patient.dietitian= Api.getDietitianObject(patient.groupId)

                                patient.chatRoomId = Api.findChatRoomId(
                                    dietitianId = patient.dieticianId,
                                    patientId = patient.username
                                )
                                loginViewModel.updateError(
                                    Api.loginPatient(
                                        loginUiState.username,
                                        loginUiState.password
                                    )
                                )
                            } catch (e: Exception) {
                                e.localizedMessage ?: "error"
                                loginViewModel.updateError("Connection to server absent")
                            }
                        }
                    },
                    modifier = Modifier.width(300.dp)
                )
                {
                    Text("Login", color = SizzleTheme.colors.background)
                }
            Spacer(modifier=Modifier.height(8.dp))
            OutlinedButton(
                // Perform register logic here, such as sending a request to a server
                onClick = loginViewModel::register,
                modifier = Modifier.width(300.dp),
            ) {
                Text("Register")
            }
            }
        }

    }
}
