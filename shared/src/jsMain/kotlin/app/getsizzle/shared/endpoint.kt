package app.getsizzle.shared

actual val endpoint: String
    get() = "0.0.0.0"

//actual val jsonClient: HttpClient
//    get() = HttpClient () {
//        install(ContentNegotiation) {
//            json()
//        }
//    }