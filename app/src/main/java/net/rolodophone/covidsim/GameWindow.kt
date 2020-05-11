package net.rolodophone.covidsim

import net.rolodophone.core.MainActivityCore
import net.rolodophone.core.Window
import net.rolodophone.core.canvas

class GameWindow(ctx: MainActivityCore) : Window(ctx) {
    val joystick = Joystick(this)
    val tiles = Tiles(this)
    val player = Player(this)
    val camera = Camera(this)
    val debug = Debug(this)

    override val seekables = listOf(joystick)

    override fun update() {
        camera.update()
        player.update()
        joystick.update()
        debug.update()
    }

    override fun draw() {
        canvas.drawRGB(0, 0, 0)

        canvas.save()
        camera.applyShift()

        tiles.draw()
        player.draw()

        canvas.restore()

        joystick.draw()
        debug.draw()
    }
}