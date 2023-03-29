package app.getsizzle.shared

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun currentMoment(): Instant {
    return Clock.System.now()
}

fun displayTime(textMoment: Instant): String {
    val temp = textMoment.toLocalDateTime(TimeZone.currentSystemDefault()).time.toString()
    val timeSplit = temp.split(":").toMutableList()
    var tempInt = timeSplit[0].toInt()
    if(tempInt in 0..11){
        if(tempInt == 0){
            tempInt = 12
        }
        timeSplit[2] = "am"
    }
    else{
        if(tempInt > 12){
            tempInt -= 12
        }
        timeSplit[2] = "pm"
    }
    timeSplit[0] = tempInt.toString()
    return "${timeSplit[0]}:${timeSplit[1]} ${timeSplit[2]}"
}

fun displayDate(momentText: Instant):String {
    val currentMoment = currentMoment().toLocalDateTime(TimeZone.currentSystemDefault())
    val currentYear = currentMoment.year
    val currentMonth = currentMoment.monthNumber
    val currentMonthDay = currentMoment.dayOfMonth
    val textMoment = momentText.toLocalDateTime(TimeZone.currentSystemDefault())
    val textYear = textMoment.year
    val textMonthNumber = textMoment.monthNumber
    val textMonth = textMoment.month
    val textMonthDay = textMoment.dayOfMonth
    val textMonthString:String = textMonth.toString().substring(0,3)
    if(textYear == currentYear){
        return if(textMonthNumber == currentMonth && currentMonthDay - textMonthDay == 0){
            return "TODAY"
        }
        else if(textMonthNumber == currentMonth && currentMonthDay - textMonthDay < 2 ){
            return "YESTERDAY"
        }
        else if(textMonthNumber == currentMonth && currentMonthDay - textMonthDay < 8) {
            return "${textMoment.dayOfWeek.toString().substring(0, 3)} $textMonthNumber / $textMonthDay"
        } else{
            "$textMonthString $textMonthDay"
        }
    }
    return "$textMonthString $textMonthDay $textYear"
}


fun isInstantSameDay(instant: Instant): Boolean {
    return instant.toLocalDateTime(TimeZone.currentSystemDefault()).dayOfYear==currentMoment().toLocalDateTime(TimeZone.currentSystemDefault()).dayOfYear
}

fun convertSlashToStar(str:String): String {
    return str.replace("/", "***")
}

fun convertStarToSlash(str:String): String{
    return str.replace("***","/")
}