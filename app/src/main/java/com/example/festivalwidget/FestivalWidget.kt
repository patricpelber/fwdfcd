package com.example.festivalwidget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.time.LocalDate
import java.time.ZoneId

class FestivalWidget : AppWidgetProvider() {

    override fun onUpdate(ctx: Context, mgr: AppWidgetManager, ids: IntArray) {
        val f = FestivalRepo.next(ctx)
        for (id in ids) {
            val v = RemoteViews(ctx.packageName, R.layout.widget)
            if (f != null) {
                val d = f.daysLeft()
                v.setTextViewText(R.id.fname, f.name)
                v.setTextViewText(R.id.fcount, when (d) { 0 -> "TODAY!"; 1 -> "1 DAY LEFT"; else -> "$d DAYS LEFT" })
                v.setTextViewText(R.id.fdesc, f.desc)
            } else {
                v.setTextViewText(R.id.fname, "No upcoming event")
                v.setTextViewText(R.id.fcount, "")
                v.setTextViewText(R.id.fdesc, "Update festivals.json")
            }
            mgr.updateAppWidget(id, v)
        }
        scheduleMidnight(ctx)
    }

    override fun onReceive(ctx: Context, intent: Intent) {
        super.onReceive(ctx, intent)
        when (intent.action) {
            Intent.ACTION_DATE_CHANGED, Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_BOOT_COMPLETED, ACTION_REFRESH -> {
                val mgr = AppWidgetManager.getInstance(ctx)
                val ids = mgr.getAppWidgetIds(ComponentName(ctx, FestivalWidget::class.java))
                if (ids.isNotEmpty()) onUpdate(ctx, mgr, ids)
            }
        }
    }

    // One inexact alarm per day; no service, no polling.
    private fun scheduleMidnight(ctx: Context) {
        val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = PendingIntent.getBroadcast(ctx, 0,
            Intent(ctx, FestivalWidget::class.java).setAction(ACTION_REFRESH),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val at = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() + 5_000
        am.set(AlarmManager.RTC, at, pi)
    }

    override fun onDisabled(ctx: Context) {
        val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(PendingIntent.getBroadcast(ctx, 0,
            Intent(ctx, FestivalWidget::class.java).setAction(ACTION_REFRESH),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
    }

    companion object { const val ACTION_REFRESH = "com.example.festivalwidget.REFRESH" }
}
