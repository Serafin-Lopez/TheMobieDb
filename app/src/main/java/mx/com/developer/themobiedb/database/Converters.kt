package mx.com.developer.themobiedb.database

import androidx.room.TypeConverter
import com.google.gson.Gson


class Converters {
    companion object {

        @TypeConverter
        @JvmStatic
        fun fromArrayList(list: ArrayList<String?>?): String? {
            val gson = Gson()
            return gson.toJson(list)
        }

        @TypeConverter
        @JvmStatic
        fun toArrayListString(value: String): ArrayList<String> {
            return ArrayList(value.split(","))
        }
    }
}