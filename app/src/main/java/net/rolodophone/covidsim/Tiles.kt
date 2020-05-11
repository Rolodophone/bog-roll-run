package net.rolodophone.covidsim

import android.graphics.PointF
import android.graphics.RectF
import com.opencsv.CSVReader
import net.rolodophone.core.bitmapPaint
import net.rolodophone.core.canvas
import net.rolodophone.core.w
import java.io.InputStreamReader
import kotlin.math.floor

class Tiles(private val window: GameWindow) {
    private val tileWidth = w(20).toInt().toFloat()
    private val tileOffset = tileWidth - 1

    //first index is level number, second is the byte that represents the bitmap
    private val bitmaps = listOf(
        null,                                           // 0
        window.ctx.bitmaps.load(R.drawable.planks),     // 1
        window.ctx.bitmaps.load(R.drawable.wall_hori),  // 2
        window.ctx.bitmaps.load(R.drawable.wall_vert),  // 3
        window.ctx.bitmaps.load(R.drawable.wall_tl),    // 4
        window.ctx.bitmaps.load(R.drawable.wall_tr),    // 5
        window.ctx.bitmaps.load(R.drawable.wall_bl),    // 6
        window.ctx.bitmaps.load(R.drawable.wall_br),    // 7
        window.ctx.bitmaps.load(R.drawable.wall_split_left),  // 8
        window.ctx.bitmaps.load(R.drawable.wall_split_top),   // 9
        window.ctx.bitmaps.load(R.drawable.wall_split_right), // 10
        window.ctx.bitmaps.load(R.drawable.wall_split_bottom),// 11
        window.ctx.bitmaps.load(R.drawable.door_hori_closed), // 12
        window.ctx.bitmaps.load(R.drawable.door_hori_open),   // 13
        window.ctx.bitmaps.load(R.drawable.door_vert_closed), // 14
        window.ctx.bitmaps.load(R.drawable.door_vert_open),   // 15
        window.ctx.bitmaps.load(R.drawable.front_door_left_closed),   // 16
        window.ctx.bitmaps.load(R.drawable.front_door_left_open),     // 17
        window.ctx.bitmaps.load(R.drawable.front_door_top_closed),    // 18
        window.ctx.bitmaps.load(R.drawable.front_door_top_open),      // 19
        window.ctx.bitmaps.load(R.drawable.front_door_right_closed),  // 20
        window.ctx.bitmaps.load(R.drawable.front_door_right_open),    // 21
        window.ctx.bitmaps.load(R.drawable.front_door_bottom_closed), // 22
        window.ctx.bitmaps.load(R.drawable.front_door_bottom_open),   // 23
        window.ctx.bitmaps.load(R.drawable.grass),                    // 24
        window.ctx.bitmaps.load(R.drawable.road),                     // 25
        window.ctx.bitmaps.load(R.drawable.pavement),                 // 26
        window.ctx.bitmaps.load(R.drawable.tiles),                    // 27
        window.ctx.bitmaps.load(R.drawable.plant),                    // 28
        window.ctx.bitmaps.load(R.drawable.bush),                     // 29
        window.ctx.bitmaps.load(R.drawable.fence_hori),               // 30
        window.ctx.bitmaps.load(R.drawable.fence_vert),               // 31
        window.ctx.bitmaps.load(R.drawable.fence_tl),                 // 32
        window.ctx.bitmaps.load(R.drawable.fence_tr),                 // 33
        window.ctx.bitmaps.load(R.drawable.fence_bl),                 // 34
        window.ctx.bitmaps.load(R.drawable.fence_br),                 // 35
        window.ctx.bitmaps.load(R.drawable.shop_doors_top_closed),    // 36
        window.ctx.bitmaps.load(R.drawable.shop_doors_top_open),      // 37
        window.ctx.bitmaps.load(R.drawable.shop_doors_bottom_closed), // 38
        window.ctx.bitmaps.load(R.drawable.shop_doors_bottom_open),   // 39
        window.ctx.bitmaps.load(R.drawable.shelves_left),             // 40
        window.ctx.bitmaps.load(R.drawable.shelves_top),              // 41
        window.ctx.bitmaps.load(R.drawable.shelves_right),            // 42
        window.ctx.bitmaps.load(R.drawable.shelves_bottom),           // 43
        window.ctx.bitmaps.load(R.drawable.shelves_top_end),          // 44
        window.ctx.bitmaps.load(R.drawable.shelves_bottom_end),       // 45
        window.ctx.bitmaps.load(R.drawable.shelves_tl),               // 46
        window.ctx.bitmaps.load(R.drawable.shelves_tr),               // 47
        window.ctx.bitmaps.load(R.drawable.shelves_bl),               // 48
        window.ctx.bitmaps.load(R.drawable.shelves_br),               // 49
        window.ctx.bitmaps.load(R.drawable.shelves_ctl),              // 50
        window.ctx.bitmaps.load(R.drawable.shelves_ctr),              // 51
        window.ctx.bitmaps.load(R.drawable.shelves_cbl),              // 52
        window.ctx.bitmaps.load(R.drawable.shelves_cbr),              // 53
        window.ctx.bitmaps.load(R.drawable.shelves_target),           // 54
        window.ctx.bitmaps.load(R.drawable.counter),                  // 55
        window.ctx.bitmaps.load(R.drawable.counter_top),              // 56
        window.ctx.bitmaps.load(R.drawable.counter_bottom),           // 57
        window.ctx.bitmaps.load(R.drawable.till),                     // 58
        window.ctx.bitmaps.load(R.drawable.gate_hori),                     // 59
        window.ctx.bitmaps.load(R.drawable.gate_hori_open),                     // 60
        window.ctx.bitmaps.load(R.drawable.gate_vert),                     // 61
        window.ctx.bitmaps.load(R.drawable.gate_vert_open)                     // 62
    )

    val walkableTiles = setOf<Byte>(1, 13, 15, 17, 19, 21, 23, 24, 25, 26, 27, 37, 39, 60, 62)

    val doorMap = mapOf<Byte, Byte>(
        Pair(12, 13),
        Pair(14, 15),
        Pair(16, 17),
        Pair(18, 19),
        Pair(20, 21),
        Pair(22, 23),
        Pair(36, 37),
        Pair(38, 39),
        Pair(59, 60),
        Pair(61, 62)
    )

    private val map: List<MutableList<Byte>>
    init {
        val tmpTileMap = mutableListOf<MutableList<Byte>>()

        val readerOutput = CSVReader(InputStreamReader(window.ctx.resources.openRawResource(R.raw.map))).readAll()

        readerOutput.forEach { line: Array<String> ->

            tmpTileMap.add(
                MutableList(line.size) { line[it].toByte() }
            )

        }

        map = tmpTileMap
    }

    private val currentTileDim = RectF(0f, 0f, tileWidth, tileWidth)

    fun draw() {

        for (row in map.withIndex()) {
            for (tile in row.value.withIndex()) {

                val bitmap = bitmaps[tile.value.toInt()]
                if (bitmap != null) canvas.drawBitmap(bitmap, null, currentTileDim, bitmapPaint)

                currentTileDim.offset(tileOffset, 0f)
            }

            currentTileDim.offset(-map[0].size * tileOffset, tileOffset)
        }

        currentTileDim.offsetTo(0f, 0f)
    }

    
    fun getTileAt(x: Float, y: Float): Byte? {
        val index = getIndexAt(x, y) ?: return null
        return map.getOrNull(index.first)?.getOrNull(index.second)
    }

    
    fun getPosAtTile(tileX: Int, tileY: Int): PointF {
        return PointF(tileX * tileOffset, tileY * tileOffset)
    }

    
    fun tryOpenDoor(x: Float, y: Float): Boolean {
        val index = getIndexAt(x, y) ?: return false
        val value = map[index.first][index.second]

        if (value in doorMap) {

            val openedDoor = doorMap[value]
            if (openedDoor != null) {
                map[index.first][index.second] = openedDoor
            }

            return true
        }

        return false
    }


    fun closeDoor(x: Float, y: Float): Boolean {
        val index = getIndexAt(x, y) ?: return false
        val doorId = map[index.first][index.second]

        if (doorId in doorMap.values) {

            val openedDoor = doorMap.entries.find { it.value == doorId }?.key
            if (openedDoor != null) {
                map[index.first][index.second] = openedDoor
            }

            return true
        }

        return false
    }


    private fun getIndexAt(x: Float, y: Float): Pair<Int, Int>? {
        val yIndex = floor(y / tileOffset).toInt()
        val xIndex = floor(x / tileOffset).toInt()
        return if (yIndex in map.indices && xIndex in map[0].indices) Pair(yIndex, xIndex) else null
    }
}