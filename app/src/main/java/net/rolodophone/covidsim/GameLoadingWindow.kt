package net.rolodophone.covidsim

import net.rolodophone.core.LoadingWindow
import net.rolodophone.core.MainActivityCore

class GameLoadingWindow(ctx: MainActivityCore) : LoadingWindow(ctx, { GameWindow(ctx, it) }, 13)