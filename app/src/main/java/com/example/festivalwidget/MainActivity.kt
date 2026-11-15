package com.example.festivalwidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        val pad = (24 * resources.displayMetrics.density).toInt()
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)
            setBackgroundColor(Color.parseColor("#0B140B"))
        }
        root.addView(TextView(this).apply {
            text = "Festival Widget\n\nTap the button below, or long-press your home screen, choose Widgets, then Festival Widget."
            setTextColor(Color.parseColor("#33FF66")); textSize = 16f
        })
        root.addView(Button(this).apply {
            text = "Add widget to home screen"
            setOnClickListener {
                val m = getSystemService(AppWidgetManager::class.java)
                if (m.isRequestPinAppWidgetSupported)
                    m.requestPinAppWidget(ComponentName(this@MainActivity, FestivalWidget::class.java), null, null)
            }
        })
        setContentView(root)
    }
}
