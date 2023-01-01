/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.domenik.calendar

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import ch.domenik.calendar.tile.MainMenuAdapter


class MainActivity : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<WearableRecyclerView>(R.id.recycler_launcher_view)
        recyclerView.setHasFixedSize(false)
        recyclerView.isEdgeItemsCenteringEnabled = false
        recyclerView.layoutManager = WearableLinearLayoutManager(this, CustomScrollingLayoutCallback())
        val menuItems: ArrayList<ListItem> = ArrayList()
        menuItems.add(ListItem("Sun, 6 Nov", "Anniversary Jeanpiere", "light"))
        menuItems.add(ListItem("Mon, 7 Nov", "Fränzi 🎂", "dark"))
        menuItems.add(ListItem("Mon, 8 Nov", "Marilen 🎂", "dark"))
        menuItems.add(ListItem( "Thu, 10 Nov", "ERZ Abfuhr: Papier\n6:30 at Friesstrasse 22", "light"))
        menuItems.add(ListItem("Sun, 6 Nov", "Anniversary Jeanpiere", "light"))
        menuItems.add(ListItem("Mon, 7 Nov", "Fränzi 🎂", "dark"))
        menuItems.add(ListItem("Mon, 8 Nov", "Marilen 🎂", "dark"))
        menuItems.add(ListItem( "Thu, 10 Nov", "ERZ Abfuhr: Papier\n6:30 at Friesstrasse 22", "light"))

        recyclerView.adapter = MainMenuAdapter(
            this, menuItems
        ) { TODO("Not yet implemented") }
    }


// TODO: Google Calendar API
    // TODO: Tap on Tile -> Open app
    // TODO: Change App + Tile Icon
    // TODO: Multiple Entries per Day



}

/** How much should we scale the icon at most.  */
private const val MAX_ICON_PROGRESS = 0.9f

class CustomScrollingLayoutCallback : WearableLinearLayoutManager.LayoutCallback() {

    private var progressToCenter: Float = 0f

    override fun onLayoutFinished(child: View, parent: RecyclerView) {
        child.apply {
            // Figure out % progress from top to bottom
            val centerOffset = height.toFloat() / 2.0f / parent.height.toFloat()
            val yRelativeToCenterOffset = y / parent.height + centerOffset

            // Normalize for center
            progressToCenter = Math.abs(0.5f - yRelativeToCenterOffset)
            // Adjust to the maximum scale
            progressToCenter = Math.min(progressToCenter, MAX_ICON_PROGRESS)

            scaleX = 1f
            scaleY = 1f
        }
    }
}

