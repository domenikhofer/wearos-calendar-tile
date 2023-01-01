package ch.domenik.calendar.tile

import android.graphics.Color
import android.util.Log
import androidx.wear.tiles.*
import androidx.wear.tiles.ColorBuilders.argb
import androidx.wear.tiles.DimensionBuilders.dp
import androidx.wear.tiles.LayoutElementBuilders.LayoutElement
import androidx.wear.tiles.ModifiersBuilders.Modifiers
import ch.domenik.calendar.ListItem
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val RESOURCES_VERSION = "0"

class CalendarTileService : TileService() {

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest) =
        Futures.immediateFuture(
            ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .build()
        )

    public override fun onTileRequest(
        requestParams: RequestBuilders.TileRequest
    ): ListenableFuture<TileBuilders.Tile> {
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

        val tile =  TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setFreshnessIntervalMillis(1 * 1 * 1000) // 1 second
            .setTimeline(singleTileTimeline)
            .build()
        return Futures.immediateFuture(tile)
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


    private fun tileLayout(): LayoutElement {

            val calendarDTO = runBlocking { CalendarApi
                .apiInstance
                .getCalendarData() }

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

}
