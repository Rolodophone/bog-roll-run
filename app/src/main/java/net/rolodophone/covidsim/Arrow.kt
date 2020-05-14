package net.rolodophone.covidsim

import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import net.rolodophone.core.*

class Arrow(private val window: GameWindow) {
    val target = window.tiles.getPosAtTile(70.33f, 26.8f)

    private val margin = w(5)

    private lateinit var point: PointF
    private lateinit var base: PointF
    private lateinit var left: PointF
    private lateinit var right: PointF

    private var grad = 0f

    fun update() {
        val xDiff = target.x - window.player.dim.centerX()
        val yDiff = target.y - window.player.dim.centerY()

        grad = (target.y - window.player.dim.centerY()) / (target.x - window.player.dim.centerX())

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
    }

    fun draw() {
        paint.color = Color.RED
        paint.strokeWidth = 5f
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawPoint(point.x, point.y, paint)

        paint.strokeWidth = 2f
        canvas.drawLine(
            window.camera.getScreenDim(window.player).centerX(),
            window.camera.getScreenDim(window.player).centerY(),
            point.x,
            point.y,
            paint
        )

        paint.color = Color.GREEN
        canvas.drawLine(
            window.camera.getScreenDim(window.player).centerX() + width/2,
            window.camera.getScreenDim(window.player).centerY() + (width/2 * grad),
            window.camera.getScreenDim(window.player).centerX() - width/2,
            window.camera.getScreenDim(window.player).centerY() - (width/2 * grad),
            paint
        )

        paint.color = Color.BLUE
        canvas.drawLine(window.camera.getScreenDim(window.player).centerX() + (-window.camera.getScreenDim(window.player).centerX() + width - margin), 0f,
                        window.camera.getScreenDim(window.player).centerX() + (-window.camera.getScreenDim(window.player).centerX() + width - margin), height, paint)
    }

    fun drawTarget() {
        paint.color = Color.GREEN
        paint.strokeWidth = 5f
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawPoint(target.x, target.y, paint)
    }
}