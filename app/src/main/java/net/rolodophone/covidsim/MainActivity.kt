package net.rolodophone.covidsim

import net.rolodophone.core.MainActivityCore

class MainActivity : MainActivityCore(R.string.app_name, { ctx -> GameLoadingWindow(ctx) }) {
    override fun onStop() {
        val x = activeWindow is GameWindow
        activeWindow.let {
            if (it is GameWindow)
                it.exoplayer.playWhenReady = false
        }
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
        activeWindow.let {
            if (it is GameWindow)
                it.exoplayer.playWhenReady = true
        }
    }
}