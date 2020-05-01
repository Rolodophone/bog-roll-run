package net.rolodophone.covidsim

import net.rolodophone.core.MainActivityCore
import net.rolodophone.core.Window
import net.rolodophone.core.canvas

class MainWindow(ctx: MainActivityCore) : Window(ctx) {
    val joystick = Joystick(this)
    val player = Player(this)

    override val seekables = listOf(joystick)

    override fun update() {
        player.update()
        joystick.update()
    }

    override fun draw() {
        canvas.drawRGB(255, 255, 255)

        player.draw()
        joystick.draw()
    }
}