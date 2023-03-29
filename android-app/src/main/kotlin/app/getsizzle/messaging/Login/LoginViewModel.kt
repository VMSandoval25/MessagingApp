package app.getsizzle.messaging.Login

import android.content.Context
import app.getsizzle.messaging.BaseStateViewModel
import app.getsizzle.messaging.MainActivity
import kotlinx.coroutines.flow.update


class LoginViewModel(context: MainActivity) : BaseStateViewModel<LoginUiState, LoginViewModel.LoginEvent>(LoginUiState()) {
    private val context=context
    sealed class LoginEvent(){
        object ShowMessagingScreen : LoginEvent()
        object ShowRegisterScreen : LoginEvent()
    }
    init {
        retrieveUsername()
    }
    private fun saveUsername() {
        val sharedPref = context?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putString("username",uiState.value.username )
        editor?.apply()
    }
    private fun retrieveUsername(){
        val sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val username = sharedPref?.getString("username", "")
        if(username!=null) {
            _uiState.update { currentState ->
                currentState.copy(username = username)
            }
        }
    }

    fun resetLogin() { // RENAME THIS LATER TO APPROPRIATE NAME FOR ONLY CHANGING PASSWORD/ERROR
        _uiState.update { currentState->
            currentState.copy(password="",error="")
        }
    }

    fun checkLogin() {
        if (_uiState.value.username.length>3 && _uiState.value.password.length>3) {
            resetLogin()
            sendEvent(LoginEvent.ShowMessagingScreen)
        } else {
            _uiState.value = _uiState.value.copy(error = "Username and password must be at least 4 characters long.")
        }
    }
    fun register(){
        sendEvent(LoginEvent.ShowRegisterScreen)
    }
    fun updatePassword(it: String) {
        _uiState.update { currentState ->
            currentState.copy(password=it)
            }
    }
    fun updateUsername(it: String) {
        _uiState.update { currentState ->
            currentState.copy(username=it)
        }

    }

    fun updateError(it: String) {
        _uiState.update { currentState ->
            currentState.copy(error=it)
        }
        if(_uiState.value.error=="Log in successful") {
            _uiState.update { currentState ->
                currentState.copy(error="")
            }
            saveUsername()
            resetLogin()
            sendEvent(LoginViewModel.LoginEvent.ShowMessagingScreen)
        }
    }
}

