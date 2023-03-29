package app.getsizzle.messaging.Register

import app.getsizzle.messaging.BaseStateViewModel
import kotlinx.coroutines.flow.update


class RegisterViewModel : BaseStateViewModel<RegisterUIState, RegisterViewModel.RegisterEvent>(RegisterUIState("","","","","","","","","","","")) {
    sealed class RegisterEvent(){
        object ShowMessagingScreen : RegisterEvent()
    }

    fun onBackClicked() {
        sendEvent(RegisterViewModel.RegisterEvent.ShowMessagingScreen)
        resetRegister()
    }

    private fun resetRegister() {
        _uiState.update { currentState->
            currentState.copy(fullName="",userId="",password="",email="", dietitianEmail ="",error=null,age="",weight="", gender="")
        }
    }
    fun updateName(it: String) {
        _uiState.update { currentState ->
            currentState.copy(fullName=it)
        }
    }

    fun updatePassword(it: String) {
        _uiState.update { currentState ->
            currentState.copy(password=it)
        }

    }
    fun updateUsername(it: String) {
        _uiState.update { currentState ->
            currentState.copy(userId=it)
        }
    }
    fun updateEmail(it: String) {
        _uiState.update { currentState ->
            currentState.copy(email=it)
        }
    }
    fun updateAge(it: String) {
        _uiState.update { currentState ->
            currentState.copy(age = it)
        }
    }
    fun updateFeet(it: String) {
        _uiState.update { currentState ->
            currentState.copy(feet = it)
        }
    }
    fun updateInches(it: String) {
        _uiState.update { currentState ->
            currentState.copy(inches = it)
        }
    }

    fun updateWeight(it: String) {
        _uiState.update { currentState ->
            currentState.copy(weight = it)
        }
    }
    fun updateGender(it: String) {
        _uiState.update { currentState ->
            currentState.copy(gender = it)
        }
    }
    fun updateDietitianEmail(it: String) {
        _uiState.update { currentState ->
            currentState.copy(dietitianEmail = it)
        }
    }

    fun updateError(it: String) {
        _uiState.update { currentState ->
            currentState.copy(error=it)
        }
        if(_uiState.value.error=="Successfully registered") {
            resetRegister()
            sendEvent(RegisterEvent.ShowMessagingScreen)
        }
    }



}

