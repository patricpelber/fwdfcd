package com.example.festivalwidget

import android.content.Context
import org.json.JSONArray
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Festival(val name: String, val date: LocalDate, val desc: String, val intl: Boolean) {
    fun daysLeft(today: LocalDate = LocalDate.now()) = ChronoUnit.DAYS.between(today, date).toInt()
}

object FestivalRepo {
    @Volatile private var cache: List<Festival>? = null

    private fun load(ctx: Context): List<Festival> = cache ?: synchronized(this) {
        cache ?: run {
            val txt = ctx.assets.open("festivals.json").bufferedReader().use { it.readText() }
            val arr = JSONArray(txt)
            List(arr.length()) {
                val o = arr.getJSONObject(it)
                Festival(o.getString("name"), LocalDate.parse(o.getString("date")),
                    o.getString("desc"), o.optBoolean("intl"))
            }.also { l -> cache = l }
        }
    }

    /** Next festival on or after today (LocalDate.now() follows device time zone). */
    fun next(ctx: Context, includeIntl: Boolean = true, today: LocalDate = LocalDate.now()): Festival? =
        load(ctx).filter { !it.date.isBefore(today) && (includeIntl || !it.intl) }.minByOrNull { it.date }
}
