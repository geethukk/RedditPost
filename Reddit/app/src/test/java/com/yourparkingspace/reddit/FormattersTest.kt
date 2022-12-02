package com.yourparkingspace.reddit

import com.yourparkingspace.reddit.ui.common.utils.formatPostScore
import com.yourparkingspace.reddit.ui.common.utils.getWebsiteFromUrl
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FormattersTest {

    @Test
    fun formatUrlTest() {

        val values = mapOf(
            "https://www.reddit.com/r/Android/hot.json" to "reddit.com",
        )

        values.forEach { (t, u) ->
            assertEquals(u, getWebsiteFromUrl(t))
        }
    }

    @Test
    fun formatPostScoreTest() {

        val values = mapOf(
            0 to "0",
            50 to "50",
            250 to "250",
            999 to "999",
            1000 to "1k",
            1234 to "1.2k",
            1999 to "1.9k",
            9999 to "9.9k",
            10001 to "10k",
            50000 to "50k"
        )

        values.forEach { (t, u) ->
            assertEquals(u, formatPostScore(t))
        }
    }

    /*  @Test fun formatDateTest() {

          val nowInstant = Instant.no
          val nowMs = nowInstant.toEpochMilli()


          val now = LocalDateTime.ofInstant(nowInstant, ZoneId.systemDefault())
          val now2 = now.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli()
          print("nowMs $nowMs $now2")


          val almostMinuteAgo = now.minusSeconds(59)
          val minuteAgo = now.minusMinutes(1).minusSeconds(5)
          val almostHourAgo = now.minusMinutes(59)
          val hourAgo = now.minusHours(1)
          val almostDayAgo = now.minusHours(23)
          val dayAgo = now.minusDays(1)
          val almostMonthAgo = now.minusDays(29)
          val monthAgo = now.minusDays(30)
          val almostYearAgo = now.minusDays(364)
          val yearAgo = now.minusDays(365)

          val values = mapOf(
              almostMinuteAgo to "Now",
              minuteAgo to "1m",
              almostHourAgo to "59m",
              hourAgo to "1h",
              almostDayAgo to "23h",
              dayAgo to "1d",
              almostMonthAgo to "29d",
              monthAgo to "1mo",
              almostYearAgo to "11mo",
              yearAgo to "1y"
          )

          values.forEach { (t, u) ->

              val seconds = t.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() / 1000
              println("seconds $seconds")
              val formatted = formatPostDate(seconds, nowMs)

              assertEquals(u, formatted)
          }

      }*/

}