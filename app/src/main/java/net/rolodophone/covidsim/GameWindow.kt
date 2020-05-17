package net.rolodophone.covidsim

import android.graphics.RectF
import android.media.AudioManager
import android.media.SoundPool
import net.rolodophone.core.*

class GameWindow(ctx: MainActivityCore, loadingWindow: LoadingWindow) : LoadableWindow(ctx, loadingWindow) {
    var dead = false
    var victorious = false

    val soundPool = SoundPool(10, AudioManager.STREAM_MUSIC, 0)

    init {
        soundPool.setOnLoadCompleteListener { _, _, _ ->
            loadingWindow.countDown()
        }
    }

    val joystick = Joystick(this)
    val tiles = Tiles(this)
    val player = Player(this)
    val people = People(this)
    val camera = Camera(this)
    val debug = Debug(this)
    val arrow = Arrow(this)
    val deathWarning = DeathWarning(this)

    val deathMessage = DeathMessage(this)
    val victoryMessage = VictoryMessage(this)
    val retryButton = Button(RectF(0f, 0f, width, height)) {
        ctx.activeWindow = GameLoadingWindow(ctx)
    }

    override val seekables = listOf(joystick)
    override val upButtons = mutableListOf(retryButton)

    override fun update() {
        when {
            dead || victorious -> {
                retryButton.update()
            }
            else -> {
                player.update()
                people.update()
                joystick.update()
                debug.update()
                camera.update()
                arrow.update()
            }
        }
    }

    override fun draw() {
        canvas.drawRGB(0, 0, 0)

        when {
            dead -> {
                deathMessage.draw()
                retryButton.draw()
            }

            victorious -> {
                victoryMessage.draw()
                retryButton.draw()
            }

            else -> {
                canvas.save()
                camera.applyShift()

                tiles.draw()
                player.draw()
                people.draw()
                arrow.drawTarget()

                canvas.restore()

                arrow.draw()
                deathWarning.draw()
                joystick.draw()
                debug.draw()
            }
        }
    }
}