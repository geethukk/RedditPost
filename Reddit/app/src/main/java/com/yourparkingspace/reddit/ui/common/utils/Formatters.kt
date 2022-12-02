package com.yourparkingspace.reddit.ui.common.utils

import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

fun formatPostScore(score: Int): String {
    return when {
        score < 1000 -> score.toString()
        else -> {
            val thousands = score / 1000
            val hundreds = (score - (thousands * 1000)) / 100
            val hundredsStr = if (hundreds == 0) "" else ".${hundreds}"

            "$thousands${hundredsStr}k"
        }
    }
}

fun formatPostDate(dateSeconds: Long, nowMs: Long = System.currentTimeMillis()): String {

    val difference = nowMs - dateSeconds * 1000

    val diffInMin: Long = TimeUnit.MILLISECONDS.toMinutes(difference)
    val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(difference)
    val diffInDays: Long = TimeUnit.MILLISECONDS.toDays(difference)

    if (diffInMin < 1) return "Now"
    if (diffInHours < 1) return "${diffInMin}m"
    if (diffInHours < 24) return "${diffInHours}h"
    if (diffInDays < 31) return "${diffInDays}d"
    if (diffInDays < 366) return "${diffInDays / 30}mo"

    return "${diffInDays / 365}y"
}

fun formatNumber(number: Int): String {
    return DecimalFormat("###,###.##").format(number)
}

fun getWebsiteFromUrl(url: String): String {

    val doubleSlashIndex = url.indexOf("//")
    val without = url.substring(doubleSlashIndex + 2)
    val splits = without.split("/")
    val namePart = splits[0]
    val splits2 = namePart.split(".")
    if (splits2.size < 3) return namePart
    return splits2[splits2.lastIndex - 1] + "." + splits2[splits2.lastIndex]
}
