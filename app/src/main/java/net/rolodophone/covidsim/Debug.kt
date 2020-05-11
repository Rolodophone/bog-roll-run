package net.rolodophone.covidsim

import android.graphics.Color
import net.rolodophone.core.*

class Debug(private val window: GameWindow) {
    companion object {
        const val showFPSGraph = false
        const val FPSGraphHeight = 4

        const val showFPSText = false
    }

    private val fpsValues = mutableListOf<Byte>()

    fun update() {
        fpsValues.add(fps.toByte())

        //only keep the required number of frames
        if (fpsValues.size == width.toInt() + 1) fpsValues.removeAt(0)
    }

    fun draw() {
        if (showFPSGraph) {
            paint.color = Color.GREEN
            paint.strokeWidth = 0f
            for (fpsValue in fpsValues.reversed().withIndex()) {
                canvas.drawLine(width - fpsValue.index, height, width - fpsValue.index, height - fpsValue.value * FPSGraphHeight, paint)
            }
            paint.color = Color.RED
            canvas.drawLine(0f, height - 60 * FPSGraphHeight, width, height - 60 * FPSGraphHeight, paint)
        }

        if (showFPSText) {
            paint.color = Color.WHITE
            paint.textSize = w(10)
            canvas.drawText("FPS: ${fps.toInt()}", w(5), w(15), paint)
        }
    }
}