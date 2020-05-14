package net.rolodophone.covidsim

import android.graphics.RectF
import android.os.SystemClock
import android.util.Log
import net.rolodophone.core.*
import kotlin.math.atan
import kotlin.math.sqrt

class Person(override val window: GameWindow, colour: Colour, speed: Float, startTileX: Int, startTileY: Int, endTileX: Int, endTileY: Int): Object {
    enum class Colour {
        CYAN, DARK_BLUE, GREEN, PINK, PURPLE, RED
    }

    private val w = w(10)
    private val h = w * (30f/32f)

    private val startX = window.tiles.getPosAtTile(startTileX + .5f, startTileY + .5f).x
    private val startY = window.tiles.getPosAtTile(startTileX + .5f, startTileY + .5f).y
    private val endX   = window.tiles.getPosAtTile(endTileX   + .5f, endTileY   + .5f).x
    private val endY   = window.tiles.getPosAtTile(endTileX   + .5f, endTileY   + .5f).y

    override var dim = RectF(
        this.startX - w/2,
        this.startY - h/2,
        this.startX + w/2,
        this.startY + h/2
    )

    private val xSpeed: Float
    private val ySpeed: Float
    private var rotation: Float

    init {
        val pxSpeed = window.tiles.tileWidth * speed
        val xDistance = endTileX - startTileX
        val yDistance = endTileY - startTileY
        val distance = sqrt((xDistance * xDistance + yDistance * yDistance).toFloat())
        val speedFrac = pxSpeed / distance

        xSpeed = xDistance * speedFrac
        ySpeed = yDistance * speedFrac

        rotation = if (xDistance == 0) { //prevents divide by 0 error
            if (yDistance > 0) 180f else 0f
        }
        else {
            (atan((yDistance / xDistance.toFloat()).toDouble()).toDegrees() + 90) % 180
        }

        if (xSpeed < 0) rotation += 180
        assert(!rotation.isNaN()) { Log.e("Person", "Rotation is NaN; this Person will not be drawn") }
    }

    private val imgs = if (speed > 0f) { //keep the full walking animation
        when (colour) {
            Colour.CYAN -> listOf(R.drawable.cyan_person_0          , R.drawable.cyan_person_1     , R.drawable.cyan_person_2     , R.drawable.cyan_person_3)
            Colour.DARK_BLUE -> listOf(R.drawable.dark_blue_person_0, R.drawable.dark_blue_person_1, R.drawable.dark_blue_person_2, R.drawable.dark_blue_person_3)
            Colour.GREEN -> listOf(R.drawable.green_person_0        , R.drawable.green_person_1    , R.drawable.green_person_2    , R.drawable.green_person_3)
            Colour.PINK -> listOf(R.drawable.pink_person_0          , R.drawable.pink_person_1     , R.drawable.pink_person_2     , R.drawable.pink_person_3)
            Colour.PURPLE -> listOf(R.drawable.purple_person_0      , R.drawable.purple_person_1   , R.drawable.purple_person_2   , R.drawable.purple_person_3)
            Colour.RED -> listOf(R.drawable.red_person_0            , R.drawable.red_person_1      , R.drawable.red_person_2      , R.drawable.red_person_3)
        }
    }
    else { //keep only stationary part of the animation
        when(colour) {
            Colour.CYAN      -> listOf(R.drawable.cyan_person_0     , R.drawable.cyan_person_2)
            Colour.DARK_BLUE -> listOf(R.drawable.dark_blue_person_0, R.drawable.dark_blue_person_2)
            Colour.GREEN     -> listOf(R.drawable.green_person_0    , R.drawable.green_person_2)
            Colour.PINK      -> listOf(R.drawable.pink_person_0     , R.drawable.pink_person_2)
            Colour.PURPLE    -> listOf(R.drawable.purple_person_0   , R.drawable.purple_person_2)
            Colour.RED       -> listOf(R.drawable.red_person_0      , R.drawable.red_person_2)
        }
    }
        .map { window.ctx.bitmaps.load(it) }

    private var timeImgLastChanged = SystemClock.elapsedRealtime()
    private var imgNum = 0

    private var goingForward = true
    private var xRange: ClosedFloatingPointRange<Float> = if (startX < endX) startX..endX else endX..startX


    override fun update() {
        //turn back if reached the end
        if (goingForward) {
            if (dim.centerX() + xSpeed / fps !in xRange) {
                goingForward = false
                rotation = (rotation + 180) % 360
            }
        }
        else {
            if (dim.centerX() - xSpeed / fps !in xRange) {
                goingForward = true
                rotation = (rotation + 180) % 360
            }
        }



        //move
        if (goingForward) dim.offset(xSpeed / fps, ySpeed / fps)
        else dim.offset(-xSpeed / fps, -ySpeed / fps)


        //animate sprite
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - timeImgLastChanged > 200) {
            imgNum = (imgNum + 1) % imgs.size
            timeImgLastChanged = currentTime
        }
    }

    override fun draw() {
        super.draw()

        canvas.save()
        canvas.rotate(rotation, dim.centerX(), dim.centerY())
        canvas.drawBitmap(imgs[imgNum], null, dim, bitmapPaint)
        canvas.restore()
    }
}