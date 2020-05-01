package net.rolodophone.covidsim

import android.graphics.Color
import android.graphics.RectF
import net.rolodophone.core.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Joystick(private val window: MainWindow) : Seekable(RectF(w(5), height - w(105), w(105), height - w(5)), listOf()) {
    override fun onSeek(x: Float, y: Float) {

        val distanceFromCenter = abs(sqrt((x - dim.centerX()).pow(2) + (y - dim.centerY()).pow(2)))
        val allowedDistanceFromCenter = dim.width() / 3f

        if (distanceFromCenter > allowedDistanceFromCenter) {
            stickX = dim.centerX() + (x - dim.centerX()) * (allowedDistanceFromCenter / distanceFromCenter)
            stickY = dim.centerY() + (y - dim.centerY()) * (allowedDistanceFromCenter / distanceFromCenter)
        }
        else {
            stickX = x
            stickY = y
        }
    }

    override fun onStopSeek() {
        stickX = dim.centerX()
        stickY = dim.centerY()
    }

    override fun onFling(vx: Float, vy: Float) = onStopSeek()

    var stickX = dim.centerX()
    var stickY = dim.centerY()

    fun velocityX() = (stickX - dim.centerX()) * 4
    fun velocityY() = (stickY - dim.centerY()) * 4
    fun speed() = abs(sqrt((stickX - dim.centerX()).pow(2) + (stickY - dim.centerY()).pow(2))) * 4

    override fun draw() {
        super.draw()

        //draw circle
        paint.color = Color.argb(50, 0, 0, 0)
        canvas.drawCircle(dim.centerX(), dim.centerY(), dim.width() / 2f, paint)

        //draw actual stick
        paint.color = Color.argb(150, 0, 0, 0)
        canvas.drawCircle(stickX, stickY, dim.width() / 4f, paint)
    }
}