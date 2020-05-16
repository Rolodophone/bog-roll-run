package net.rolodophone.covidsim

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import net.rolodophone.core.*

class DeathMessage(private val window: GameWindow) {
    fun draw() {
        paint.textSize = w(10)
        paint.textAlign = Paint.Align.CENTER
        paint.typeface = Typeface.MONOSPACE
        paint.color = Color.WHITE
        canvas.drawText("Oh no! You contracted COVID-19!", width / 2, height / 2 - w(10), paint)
        canvas.drawText("Tap to retry."                  , width / 2, height / 2 + w(10), paint)
    }
}