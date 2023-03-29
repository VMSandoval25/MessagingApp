package app.getsizzle.web.regex

// 6 minimum characters, alphanumeric, special character needed
val passwordReq = Regex("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#\$%^&+=.])(?=\\S*\$).{4,}\$")
// must contain @ and .
val emailReq = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
// 6 minimum characters, alphanumeric, no double _ or .
val userIdReq = Regex("^(?=.{6,20}\$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])\$")