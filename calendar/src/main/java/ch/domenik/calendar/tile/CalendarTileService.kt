package ch.domenik.calendar.tile

import android.graphics.Color
import android.util.Log
import androidx.wear.tiles.*
import androidx.wear.tiles.ColorBuilders.argb
import androidx.wear.tiles.DimensionBuilders.dp
import androidx.wear.tiles.LayoutElementBuilders.LayoutElement
import androidx.wear.tiles.ModifiersBuilders.Modifiers
import ch.domenik.calendar.ListItem
import com.google.android.horologist.tiles.CoroutinesTileService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val RESOURCES_VERSION = "0"

class CalendarTileService : CoroutinesTileService() {

    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ): ResourceBuilders.Resources {
        return ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .build()
    }

    override suspend fun tileRequest(
        requestParams: RequestBuilders.TileRequest
    ): TileBuilders.Tile {
        val singleTileTimeline = TimelineBuilders.Timeline.Builder()
            .addTimelineEntry(
                TimelineBuilders.TimelineEntry.Builder()
                    .setLayout(
                        LayoutElementBuilders.Layout.Builder()
                            .setRoot(tileLayout())
                            .build()
                    )

                    .build()
            )
            .build()

        return TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTimeline(singleTileTimeline)
            .build()
    }

    data class CalendarDTO(
        val id: String,
        val title: String,
    )

    interface CalendarApi {

        companion object {
            const val BASE_URL = "https://jsonplaceholder.typicode.com/"


            val apiInstance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CalendarApi::class.java)
        }

        @GET("/todos/1")
        suspend fun getCalendarData(): CalendarDTO
    }


    private suspend fun tileLayout(): LayoutElement {


            val calendarDTO = CalendarApi
                .apiInstance
                .getCalendarData()
            Log.d("abcdefg", calendarDTO.title)


        val listItems: ArrayList<ListItem> = ArrayList()
        listItems.add(ListItem("Sun, 6 Nov", "Anniversary Jeanpieree", "light"))
        listItems.add(ListItem("Mon, 7 Nov", "Fränzi 🎂", "dark"))
        listItems.add(ListItem("Mon, 8 Nov", "Marilen 🎂", "dark"))
        listItems.add(ListItem( "Thu, 10 Nov", "ERZ Abfuhr: Papier\n6:30 at Friesstrasse 22", "light"))
        listItems.add(ListItem("Sun, 6 Nov", "Anniversary Jeanpiere", "light"))
        listItems.add(ListItem("Mon, 7 Nov", "Fränzi 🎂", "dark"))
        listItems.add(ListItem("Mon, 8 Nov", "Marilen 🎂", "dark"))
        listItems.add(ListItem( "Thu, 10 Nov", "ERZ Abfuhr: Papier\n6:30 at Friesstrasse 22", "light"))

        return LayoutElementBuilders.Column.Builder()
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .apply {
                listItems.take(5).forEach { listItem ->
                    addContent(
                    LayoutElementBuilders.Column.Builder()
                        .addContent(
                            LayoutElementBuilders.Text.Builder()
                                .setText(listItem.date)
                                .setFontStyle(LayoutElementBuilders.FontStyle.Builder()
                                    .setColor(argb(Color.parseColor("#000000")))
                                    .setSize(DimensionBuilders.sp(12f))
                                    .build())
                                .build()
                        ).build()
                )
                    .addContent(
                        LayoutElementBuilders.Column.Builder()
                            .addContent(
                                LayoutElementBuilders.Text.Builder()
                                    .setText(listItem.text)
                                    .setFontStyle(LayoutElementBuilders.FontStyle.Builder()
                                        .setSize(DimensionBuilders.sp(12f))
                                        .build())
                                    .build()
                            ).setModifiers(
                                Modifiers.Builder()
                                    .setBackground(
                                        ModifiersBuilders.Background.Builder()
                                            .setColor(argb(Color.parseColor(if (listItem.type == "light") "#4CAF50" else "#009688"))).build()
                                    )
                                    .build()
                            ).setWidth(DimensionBuilders.expand())

                            .build()

                    )
                        .addContent(
                            LayoutElementBuilders.Text.Builder()
                                .setText(" ")
                                .setFontStyle(LayoutElementBuilders.FontStyle.Builder()
                                    .setSize(DimensionBuilders.sp(6f))
                                    .build())
                                .build()
                        )
                }
            }

            .setModifiers(
                Modifiers.Builder()
                    .setBackground(
                        ModifiersBuilders.Background.Builder()
                            .setColor(argb(Color.parseColor("#ffffff"))).build()
                    )
                    .setPadding(ModifiersBuilders.Padding.Builder().setTop(dp(10f)).build())
                    .build()
            )
            .build()
    }

    fun getRgbFromHex(hex: String): IntArray {
        val initColor = Color.parseColor(hex)
        val r = Color.red(initColor)
        val g = Color.green(initColor)
        val b = Color.blue(initColor)
        return intArrayOf(r, g, b)
    }

}
