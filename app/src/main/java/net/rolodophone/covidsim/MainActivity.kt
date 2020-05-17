package net.rolodophone.covidsim

import net.rolodophone.core.MainActivityCore

class MainActivity : MainActivityCore(R.string.app_name, { ctx -> GameLoadingWindow(ctx) })