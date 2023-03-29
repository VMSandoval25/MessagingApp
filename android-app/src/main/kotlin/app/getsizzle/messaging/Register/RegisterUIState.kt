package app.getsizzle.messaging.Register

data class RegisterUIState(
    val fullName: String,
    val userId: String,
    val password: String,
    val email: String,
    val dietitianEmail: String,
    val age:String,
    val inches:String,
    val feet:String,
    val weight:String,
    val gender:String,
    val error:String?=null,
    val pfp: String? = null,
)