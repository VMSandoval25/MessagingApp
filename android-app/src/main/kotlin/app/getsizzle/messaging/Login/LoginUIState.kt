package app.getsizzle.messaging.Login


data class LoginUiState(
    val username: String = "",
    val password: String ="",
    val error: String?=null
)