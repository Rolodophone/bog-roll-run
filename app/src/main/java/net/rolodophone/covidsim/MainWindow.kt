package net.rolodophone.covidsim

import net.rolodophone.core.MainActivityCore
import net.rolodophone.core.Window
import net.rolodophone.core.canvas

class MainWindow(ctx: MainActivityCore) : Window(ctx) {
    val joystick = Joystick(this)
    val player = Player(this)
    val camera = Camera(this)

    override val seekables = listOf(joystick)

    override fun update() {
        camera.update()
        player.update()
        joystick.update()
    }

    override fun draw() {
        canvas.drawRGB(255, 255, 255)

        canvas.save()
        camera.applyShift()

        player.draw()

        canvas.restore()

        joystick.draw()
    }
}