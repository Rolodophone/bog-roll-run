package net.rolodophone.covidsim

import android.graphics.Bitmap
import android.graphics.RectF
import android.os.SystemClock
import net.rolodophone.core.*
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt

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

    private val walkWood       = window.soundPool.load(window.ctx, R.raw.walk_wood       , 1)
    private val walkTiles      = window.soundPool.load(window.ctx, R.raw.walk_tiles      , 1)
    private val walkPavement   = window.soundPool.load(window.ctx, R.raw.walk_pavement   , 1)
    private val walkGrass      = window.soundPool.load(window.ctx, R.raw.walk_grass      , 1)
    private val openDoor       = window.soundPool.load(window.ctx, R.raw.open_door       , 1)
    private val closeDoor      = window.soundPool.load(window.ctx, R.raw.close_door      , 1)
    private val openFrontDoor  = window.soundPool.load(window.ctx, R.raw.open_front_door , 1)
    private val closeFrontDoor = window.soundPool.load(window.ctx, R.raw.close_front_door, 1)
    private val openShopDoor   = window.soundPool.load(window.ctx, R.raw.open_shop_door  , 1)
    private val closeShopDoor  = window.soundPool.load(window.ctx, R.raw.close_shop_door , 1)
    private val openGate       = window.soundPool.load(window.ctx, R.raw.open_gate       , 1)
    private val closeGate      = window.soundPool.load(window.ctx, R.raw.close_gate      , 1)

    var currentSound = 0
    var currentTileSound = walkWood
    var moving = false

    var rotation = 0f

    var holdingDoor = false
    var currentDoorSound = 0

    private val warningDistance = 5 * window.tiles.tileWidth
    private val deathDistance = 2 * window.tiles.tileWidth

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

        if (window.joystick.velocityX() != 0f || window.joystick.velocityY() != 0f) {
            if (moving) {
                val newCurrentTileSound = when (window.tiles.getTileAt(dim.centerX(), dim.centerY())) {
                    in window.tiles.woodTiles -> walkWood
                    in window.tiles.grassTiles -> walkGrass
                    in window.tiles.pavementTiles -> walkPavement
                    in window.tiles.tileTiles -> walkTiles
                    else -> currentTileSound
                }
                if (newCurrentTileSound != currentTileSound) {
                    window.soundPool.pause(currentSound)
                    currentSound = window.soundPool.play(newCurrentTileSound, 1f, 1f, 1, -1, 1f)
                    currentTileSound = newCurrentTileSound
                }
            }
            else {
                if (currentSound == 0) { //not initialised yet
                    currentSound = window.soundPool.play(walkWood, 1f, 1f, 1, -1, 1f)
                }
                else { //resume same walking sound
                    window.soundPool.resume(currentSound)
                    moving = true
                }
            }
        }
        else { //stop walking sound
             if (moving) {
                 window.soundPool.pause(currentSound)
                 moving = false
             }
        }

        val newCenterX = dim.centerX() + xOffset
        val newCenterY = dim.centerY() + yOffset

        //close doors
        if (holdingDoor && window.tiles.getTileAt(newCenterX, newCenterY) !in window.tiles.doorMap.values) {
            if (window.tiles.tryCloseDoor(dim.centerX(), dim.centerY())) {

                window.soundPool.stop(currentDoorSound)
                
                window.soundPool.play(
                    when (window.tiles.getTileAt(dim.centerX(), dim.centerY())) {
                        in window.tiles.closeDoorTiles -> closeDoor
                        in window.tiles.closeFrontDoorTiles -> closeFrontDoor
                        in window.tiles.closeShopDoorTiles -> closeShopDoor
                        in window.tiles.closeGateTiles -> closeGate
                        else -> throw IllegalStateException("Can't close tile ID ${window.tiles.getTileAt(newCenterX, newCenterY)} like a door")
                    },
                    1f, 1f, 1, 0, 1f
                )
                
                holdingDoor = false
            }
        }

        //open doors
        if (window.tiles.tryOpenDoor(newCenterX, newCenterY)) {

            currentDoorSound = window.soundPool.play(
                when (window.tiles.getTileAt(newCenterX, newCenterY)) {
                    in window.tiles.openDoorTiles -> openDoor
                    in window.tiles.openFrontDoorTiles -> openFrontDoor
                    in window.tiles.openShopDoorTiles -> openShopDoor
                    in window.tiles.openGateTiles -> openGate
                    else -> throw IllegalStateException("Can't open tile ID ${window.tiles.getTileAt(newCenterX, newCenterY)} like a door")
                },
                1f, 1f, 1, 0, 1f
            )

            holdingDoor = true
        }

        dim.offset(xOffset, yOffset)


        //rotate based on movement
        if (window.joystick.velocityX() != 0f) rotation = (atan(window.joystick.velocityY() / window.joystick.velocityX()).toDouble().toDegrees() + 90) % 180
        // I add 90 degrees because it seems to need it. Something with how atan works I guess
        if (window.joystick.velocityX() < 0f) rotation += 180 // because atan only returns 0-180


        //check if player is within a set distance of any player and if so how far
        var minDistSq = Float.POSITIVE_INFINITY

        for (person in window.people.people) {

            //quick square check to see if it's worth a precise check
            if ((abs(person.dim.left - this.dim.right) < warningDistance || abs(this.dim.left - person.dim.right) < warningDistance) &&
                (abs(person.dim.top - this.dim.bottom) < warningDistance || abs(this.dim.top - person.dim.bottom) < warningDistance)) {

                val newMinDistSq = abs((this.dim.centerX() - person.dim.centerX()).pow(2) + (this.dim.centerY() - person.dim.centerY()).pow(2)) // distance^2
                if (newMinDistSq < minDistSq) minDistSq = newMinDistSq
            }
        }

        val minDist = sqrt(minDistSq)
        when {
            minDist > warningDistance -> window.deathWarning.opacity = 0 // not in danger
            minDist <= deathDistance -> die()  // dead
            else -> window.deathWarning.opacity = (((warningDistance - deathDistance) - (minDist - deathDistance)) / (warningDistance - deathDistance) * 255).toInt()  // in danger but not dead
        }


        //check if player has reached toilet paper
        if (sqrt((this.dim.centerX() - window.arrow.target.x).pow(2) + (this.dim.centerY() - window.arrow.target.y).pow(2)) < window.tiles.tileWidth) victory()
    }


    override fun draw() {
        super.draw()

        canvas.save()
        canvas.rotate(rotation, dim.centerX(), dim.centerY())
        canvas.drawBitmap(currentImgs[imgNum], null, dim, bitmapPaint)
        canvas.restore()
    }


    private fun die() {
        window.dead = true
        window.soundPool.release()
    }


    private fun victory() {
        window.victorious = true
        window.soundPool.release()
    }
}