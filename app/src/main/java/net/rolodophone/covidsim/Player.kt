package net.rolodophone.covidsim

import android.graphics.Bitmap
import android.graphics.RectF
import android.os.SystemClock
import net.rolodophone.core.*
import kotlin.math.atan

class Player(override val window: GameWindow) : Object {
    override val dim: RectF
        init {
            val w = w(10)
            val h = w * (30f/32f)
            val left = window.tiles.getPosAtTile(78f, 110f).x
            val top = window.tiles.getPosAtTile(78f, 110f).y
            dim = RectF(left, top, left + w, top + h)
        }

    val allImgs = listOf(
        window.ctx.bitmaps.load(R.drawable.player0),
        window.ctx.bitmaps.load(R.drawable.player1),
        window.ctx.bitmaps.load(R.drawable.player2),
        window.ctx.bitmaps.load(R.drawable.player3),
        window.ctx.bitmaps.load(R.drawable.player4),
        window.ctx.bitmaps.load(R.drawable.player5)
    )
    var currentImgs = listOf<Bitmap>()
    var imgNum = 0
    var timeImgLastChanged = SystemClock.elapsedRealtime()

    var rotation = 0f

    var holdingDoor = false

    override fun update() {
        //change sprite list based on speed
        currentImgs = when {
            //running
            window.joystick.speed() > window.joystick.dim.width() * (3/4f) -> allImgs
            //walking
            window.joystick.speed() > 0f -> listOf(allImgs[0], allImgs[1], allImgs[3], allImgs[4])
            //stationary
            else -> listOf(allImgs[0], allImgs[3])
        }

        //animate
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - timeImgLastChanged > 200) {
            imgNum += 1
            timeImgLastChanged = currentTime
        }

        //ensure imgNum is within the currentImgs length
        imgNum %= currentImgs.size


        //--move--
        var xOffset = window.joystick.velocityX() / fps
        if (window.tiles.getTileAt(dim.centerX() + xOffset, dim.centerY()) !in window.tiles.walkableTiles) xOffset = 0f
        var yOffset = window.joystick.velocityY() / fps
        if (window.tiles.getTileAt(dim.centerX(), dim.centerY() + yOffset) !in window.tiles.walkableTiles) yOffset = 0f

        val newCenterX = dim.centerX() + xOffset
        val newCenterY = dim.centerY() + yOffset

        //close doors
        if (holdingDoor && window.tiles.getTileAt(newCenterX, newCenterY) !in window.tiles.doorMap.values) {
            window.tiles.closeDoor(dim.centerX(), dim.centerY())
            holdingDoor = false
        }

        //open doors
        if (window.tiles.tryOpenDoor(newCenterX, newCenterY)) holdingDoor = true

        dim.offset(xOffset, yOffset)


        //rotate based on movement
        if (window.joystick.velocityX() != 0f) rotation = (atan(window.joystick.velocityY() / window.joystick.velocityX()).toDouble().toDegrees() + 90) % 180
        // I add 90 degrees because it seems to need it. Something with how atan works I guess
        if (window.joystick.velocityX() < 0f) rotation += 180 // because atan only returns 0-180
    }


    override fun draw() {
        super.draw()

        canvas.save()
        canvas.rotate(rotation, dim.centerX(), dim.centerY())
        canvas.drawBitmap(currentImgs[imgNum], null, dim, bitmapPaint)
        canvas.restore()
    }
}