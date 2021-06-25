package com.mobiweb.challenge.weatherforecast.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter : JsonDeserializer<LocalDateTime> {

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        private val FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime {
        val date =  json!!.asString.replace(' ', 'T')

        return LocalDateTime.parse(date, FORMATTER)
    }
}