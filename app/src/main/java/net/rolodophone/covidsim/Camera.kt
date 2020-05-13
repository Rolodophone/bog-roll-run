package net.rolodophone.covidsim

import android.graphics.RectF
import net.rolodophone.core.canvas
import net.rolodophone.core.height
import net.rolodophone.core.w
import net.rolodophone.core.width

class Camera(private val window: GameWindow) {
    val dim = RectF(0f, 0f, width, height)

    fun update() {
        val xDistanceFromCenter = getScreenDim(window.player).centerX() - width/2
        val xShift = when {
            xDistanceFromCenter > w(30) -> xDistanceFromCenter - w(30)
            xDistanceFromCenter < -w(30) -> xDistanceFromCenter + w(30)
            else -> 0f
        }

        val yShift = getScreenDim(window.player).centerY() - height/2

        dim.offset(xShift, yShift)
    }

    fun applyShift() {
        canvas.translate(-dim.left, -dim.top)
    }

    fun getScreenDim(someObject: Object): RectF {
        val x = RectF(someObject.dim)
        x.offset(-dim.left, -dim.top)
        return x
    }
}