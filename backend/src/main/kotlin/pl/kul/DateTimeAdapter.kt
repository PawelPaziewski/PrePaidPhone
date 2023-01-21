package pl.kul

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


//The adapter will be developed according to needed functionality
class DateTimeAdapter(val date: LocalDateTime) {

    companion object {
        fun now(): DateTimeAdapter {
            return DateTimeAdapter(LocalDateTime.now())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DateTimeAdapter

        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }
}

@WritingConverter
class DataTimeAdapterToLocalDateTimeConverter : Converter<DateTimeAdapter, LocalDateTime> {
    override fun convert(source: DateTimeAdapter): LocalDateTime {
        return source.date
    }
}

@ReadingConverter
class DateToDateTimeAdapterConverter : Converter<Date, DateTimeAdapter> {
    override fun convert(source: Date): DateTimeAdapter {
        return DateTimeAdapter(LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault()))
    }
}