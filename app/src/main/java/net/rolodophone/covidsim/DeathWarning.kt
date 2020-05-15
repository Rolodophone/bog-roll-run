package net.rolodophone.covidsim

import android.graphics.RectF
import net.rolodophone.core.canvas
import net.rolodophone.core.height
import net.rolodophone.core.paint
import net.rolodophone.core.width

class DeathWarning(private val window: GameWindow) {
    private val img = window.ctx.bitmaps.load(R.drawable.death_warning)
    private val dim = RectF(0f, 0f, width, height)

    var opacity = 0

    fun draw() {
        paint.alpha = opacity
        canvas.drawBitmap(img, null, dim, paint)
        paint.alpha = 255
    }
}