package net.rolodophone.covidsim

import android.graphics.RectF
import androidx.annotation.CallSuper

interface Object {
    val dim: RectF
    val window: GameWindow

    fun update()

    @CallSuper
    fun draw() {
        if (!window.camera.dim.contains(dim)) return
    }
}