package net.rolodophone.covidsim

import android.graphics.RectF
import android.os.SystemClock
import net.rolodophone.core.*
import kotlin.math.atan
import kotlin.math.sqrt

class Person(private val window: GameWindow, colour: Colour, speed: Float, startX: Int, startY: Int, endX: Int, endY: Int) {
    enum class Colour {
        CYAN, DARK_BLUE, GREEN, PINK, PURPLE, RED
    }

    private val w = w(20)
    private val h = w * (30f/32f)

    private val startX = window.tiles.getPosAtTile(startX, startY).x + window.tiles.tileWidth / 2
    private val startY = window.tiles.getPosAtTile(startX, startY).y + window.tiles.tileWidth / 2
    private val endX   = window.tiles.getPosAtTile(endX  , endY).x + window.tiles.tileWidth / 2
    private val endY   = window.tiles.getPosAtTile(endX  , endY).y + window.tiles.tileWidth / 2

    var dim = RectF(
        this.startX - w/2,
        this.startY - h/2,
        this.startX + w/2,
        this.startY + h/2
    )

    private val xSpeed: Float
    private val ySpeed: Float
    init {
        val pxSpeed = window.tiles.tileWidth * speed
        val xDistance = endX - startX
        val yDistance = endY - startY
        val distance = sqrt((xDistance * xDistance + yDistance * yDistance).toFloat())
        val speedFrac = pxSpeed / distance
        xSpeed = xDistance * speedFrac
        ySpeed = yDistance * speedFrac
    }

    private val imgs = when(colour) {
        Colour.CYAN      -> listOf(R.drawable.cyan_person_0     , R.drawable.cyan_person_1     , R.drawable.cyan_person_2     , R.drawable.cyan_person_3)
        Colour.DARK_BLUE -> listOf(R.drawable.dark_blue_person_0, R.drawable.dark_blue_person_1, R.drawable.dark_blue_person_2, R.drawable.dark_blue_person_3)
        Colour.GREEN     -> listOf(R.drawable.green_person_0    , R.drawable.green_person_1    , R.drawable.green_person_2    , R.drawable.green_person_3)
        Colour.PINK      -> listOf(R.drawable.pink_person_0     , R.drawable.pink_person_1     , R.drawable.pink_person_2     , R.drawable.pink_person_3)
        Colour.PURPLE    -> listOf(R.drawable.purple_person_0   , R.drawable.purple_person_1   , R.drawable.purple_person_2   , R.drawable.purple_person_3)
        Colour.RED       -> listOf(R.drawable.red_person_0      , R.drawable.red_person_1      , R.drawable.red_person_2      , R.drawable.red_person_3)
    }
        .map { window.ctx.bitmaps.load(it) }
    private var timeImgLastChanged = SystemClock.elapsedRealtime()
    private var imgNum = 0

    private var goingForward = true

    private var rotation = (atan(ySpeed / xSpeed).toDouble().toDegrees() + 90) % 180
    init {
        if (xSpeed < 0) rotation += 180
    }


    fun update() {
        //turn back if reached the end
        if (dim.centerX() + xSpeed / fps !in startX..endX) {
            goingForward = !goingForward
            rotation = (rotation + 180) % 360
        }


        //move
        if (goingForward) dim.offset(xSpeed / fps, ySpeed / fps)
        else dim.offset(-xSpeed / fps, -ySpeed / fps)


        //animate sprite
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - timeImgLastChanged > 200) {
            imgNum = (imgNum + 1) % 4
            timeImgLastChanged = currentTime
        }
    }

    fun draw() {
        canvas.save()
        canvas.rotate(rotation, dim.centerX(), dim.centerY())
        canvas.drawBitmap(imgs[imgNum], null, dim, bitmapPaint)
        canvas.restore()
    }
}