package net.rolodophone.covidsim

import android.graphics.RectF
import net.rolodophone.core.*

class Camera(private val window: GameWindow) {
    val dim = RectF(0f, 0f, width, height)

    fun update() {
        val xDistanceFromCenter = getScreenDim(window.player).centerX() - width/2
        val xShift = when {
            xDistanceFromCenter > w(90) -> xDistanceFromCenter - w(90)
            xDistanceFromCenter < -w(90) -> xDistanceFromCenter + w(90)
            else -> 0f
        }

        val yDistanceFromCenter = getScreenDim(window.player).centerY() - height/2
        val yShift = when {
            yDistanceFromCenter > h(90) -> yDistanceFromCenter - h(90)
            yDistanceFromCenter < -h(90) -> yDistanceFromCenter + h(90)
            else -> 0f
        }

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