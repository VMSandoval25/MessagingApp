package app.getsizzle.messaging.Profile

import androidx.compose.foundation.lazy.LazyListState
import app.getsizzle.messaging.BaseStateViewModel
import kotlinx.coroutines.flow.update

class ProfileViewModel : BaseStateViewModel<ProfileUIState, ProfileViewModel.ProfileEvent>(ProfileUIState()) {
    var lazyColumnState: LazyListState? = null


    sealed class ProfileEvent(){
        object  ShowLastScreen: ProfileEvent()

    }
    fun onBackClicked() {
        sendEvent(ProfileEvent.ShowLastScreen)
    }
    fun updateEmail(it: String) {
        _uiState.update { currentState ->
            currentState.copy(email = it)
        }
    }
    fun updateName(it: String) {
        _uiState.update { currentState ->
            currentState.copy(name=it)
        }
    }


}
