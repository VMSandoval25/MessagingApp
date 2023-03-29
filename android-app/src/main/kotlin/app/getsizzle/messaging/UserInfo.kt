package app.getsizzle.messaging

import app.getsizzle.shared.data.model.Dietitian

class UserInfo() {
    lateinit var fullname:String
    lateinit var email:String
    lateinit var username:String
    lateinit var password:String
    var chatRoomId:Int = 0
    lateinit var dieticianId:String
    var groupId:Int=0
    lateinit var dietitian: Dietitian
}