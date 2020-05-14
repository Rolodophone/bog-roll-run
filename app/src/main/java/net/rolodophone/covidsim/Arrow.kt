package net.rolodophone.covidsim

import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import net.rolodophone.core.*
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt

class Arrow(private val window: GameWindow) {
    val target = window.tiles.getPosAtTile(70.33f, 26.8f)

    private val margin = w(5)

    private lateinit var point: PointF
    private lateinit var base: PointF
    private lateinit var left: PointF
    private lateinit var right: PointF

    fun update() {
        val xDiff = target.x - window.player.dim.centerX()
        val yDiff = target.y - window.player.dim.centerY()

        val grad = (target.y - window.player.dim.centerY()) / (target.x - window.player.dim.centerX())

        point = PointF(
            if (yDiff < 0) window.camera.getScreenDim(window.player).centerX() + (-(height/2 - margin) / grad)
            else           window.camera.getScreenDim(window.player).centerX() + ( (height/2 - margin) / grad),

            if (xDiff < 0) window.camera.getScreenDim(window.player).centerY() + (grad * (-window.camera.getScreenDim(window.player).centerX() + margin))
            else           window.camera.getScreenDim(window.player).centerY() + (grad * (-window.camera.getScreenDim(window.player).centerX() + width - margin))
        )

        //constrain x
        if (point.x > width - margin) point.x = width - margin
        else if (point.x < margin) point.x = margin

        //constrain y
        if (point.y > height - margin) point.y = height - margin
        else if (point.y < margin) point.y = margin


        val arrowXlength = (if (xDiff < 0) 1 else -1) * sqrt(
            (w(20).pow(2))
                    /
                    (1 + grad.pow(2))
        )
        val arrowYlength = arrowXlength * grad

        base = PointF(
            point.x + arrowXlength,
            point.y + arrowYlength
        )


        var angle = atan(grad.toDouble()).toDegrees()
        if (xDiff > 0) angle += 180f

        left = posFromDeg(point.x, point.y, w(10), angle + 45f)
        right = posFromDeg(point.x, point.y, w(10), angle - 45f)
    }

    fun draw() {
        paint.color = Color.RED
        paint.strokeWidth = 15f
        paint.strokeCap = Paint.Cap.ROUND

        canvas.drawLine(base.x, base.y, point.x, point.y, paint)
        canvas.drawLine(point.x, point.y, left.x, left.y, paint)
        canvas.drawLine(point.x, point.y, right.x, right.y, paint)
    }

    fun drawTarget() {
        paint.color = Color.GREEN
        paint.strokeWidth = 5f
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawPoint(target.x, target.y, paint)
    }
}