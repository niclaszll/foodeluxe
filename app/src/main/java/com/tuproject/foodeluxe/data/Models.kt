package com.tuproject.foodeluxe.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import android.os.Parcelable
import android.util.Log
import com.tuproject.foodeluxe.commons.adapter.AdapterConstants
import com.tuproject.foodeluxe.commons.adapter.ViewType
import kotlinx.android.parcel.Parcelize


@Parcelize
data class RecipeSearchResults(
    val q: String,
    //val from: Int,
    //val to: Int,
    val recipes: List<RecipeItem>
) : Parcelable

@Entity
@Parcelize
data class RecipeItem(
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "thumbnail") val thumbnail: String,
    @ColumnInfo(name = "source") val source: String,
    @ColumnInfo(name = "calories") val calories: Float,
    @ColumnInfo(name = "total_time") val totalTime: Int,
    @ColumnInfo(name = "yield") val yield: Int,
    @ColumnInfo(name = "diet_labels") val dietLabels: List<String>,
    @ColumnInfo(name = "health_labels") val healthLabels: List<String>,
    @ColumnInfo(name = "uri") val uri: String,
    @PrimaryKey val url: String,
    @ColumnInfo(name = "cautions") val cautions: List<String>,
    @ColumnInfo(name = "ingredient_list") val ingredientLines: List<String>,
    @ColumnInfo(name = "total_weight") val totalWeight: Float,
    @ColumnInfo(name = "favourite") var isFav: Boolean
) : ViewType, Parcelable {
    override fun getViewType() = AdapterConstants.RECIPE
}

class ListConverter {

    @TypeConverter
    fun storedStringToList(value: String): List<String> {
        return value.split(";").map { it.trim() }.dropLast(1)
    }

    @TypeConverter
    fun languagesToStoredString(list: List<String>): String {
        var value = ""

        for (item in list)
            value += "$item;"

        Log.v("Converter", value)

        return value
    }
}