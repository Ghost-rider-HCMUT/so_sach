package com.sotraubo.app.core.common.format

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

enum class ReportPeriodType(val displayLabel: String) {
    TODAY("Hôm nay"),
    THIS_WEEK("Tuần này"),
    THIS_MONTH("Tháng này"),
    THIS_YEAR("Năm nay"),
    CUSTOM("Tùy chọn")
}

data class DateRangeMillis(
    val startMillis: Long,
    val endMillis: Long,
    val label: String
)

object DateFormatter {

    val VIETNAM_ZONE: ZoneId = ZoneId.of("Asia/Ho_Chi_Minh")
    private val DD_MM_YYYY: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val DD_MM_YYYY_HH_MM: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    private val FILE_SAFE_TIMESTAMP: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HHmm")

    fun formatDate(epochMillis: Long, zoneId: ZoneId = VIETNAM_ZONE): String {
        val date = Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDate()
        return date.format(DD_MM_YYYY)
    }

    fun formatDateTime(epochMillis: Long, zoneId: ZoneId = VIETNAM_ZONE): String {
        val dateTime = Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDateTime()
        return dateTime.format(DD_MM_YYYY_HH_MM)
    }

    fun formatFileSafeTimestamp(epochMillis: Long, zoneId: ZoneId = VIETNAM_ZONE): String {
        val dateTime = Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDateTime()
        return dateTime.format(FILE_SAFE_TIMESTAMP)
    }

    fun parseDdMmYyyyToMillis(dateStr: String, zoneId: ZoneId = VIETNAM_ZONE): Long? {
        return try {
            val localDate = LocalDate.parse(dateStr.trim(), DD_MM_YYYY)
            localDate.atTime(12, 0).atZone(zoneId).toInstant().toEpochMilli()
        } catch (e: Exception) {
            null
        }
    }

    fun startOfDayMillis(localDate: LocalDate, zoneId: ZoneId = VIETNAM_ZONE): Long {
        return localDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
    }

    fun endOfDayMillis(localDate: LocalDate, zoneId: ZoneId = VIETNAM_ZONE): Long {
        return localDate.atTime(LocalTime.MAX).atZone(zoneId).toInstant().toEpochMilli()
    }

    fun todayNoonMillis(nowMillis: Long = System.currentTimeMillis(), zoneId: ZoneId = VIETNAM_ZONE): Long {
        val localDate = Instant.ofEpochMilli(nowMillis).atZone(zoneId).toLocalDate()
        return localDate.atTime(12, 0).atZone(zoneId).toInstant().toEpochMilli()
    }

    fun addDays(epochMillis: Long, days: Long, zoneId: ZoneId = VIETNAM_ZONE): Long {
        val localDate = Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDate().plusDays(days)
        return localDate.atTime(12, 0).atZone(zoneId).toInstant().toEpochMilli()
    }

    fun addMonths(epochMillis: Long, months: Long, zoneId: ZoneId = VIETNAM_ZONE): Long {
        val localDate = Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDate().plusMonths(months)
        return localDate.atTime(12, 0).atZone(zoneId).toInstant().toEpochMilli()
    }

    fun isSameDay(millisA: Long, millisB: Long, zoneId: ZoneId = VIETNAM_ZONE): Boolean {
        val dateA = Instant.ofEpochMilli(millisA).atZone(zoneId).toLocalDate()
        val dateB = Instant.ofEpochMilli(millisB).atZone(zoneId).toLocalDate()
        return dateA == dateB
    }

    fun isSameMonth(millisA: Long, millisB: Long, zoneId: ZoneId = VIETNAM_ZONE): Boolean {
        val dateA = Instant.ofEpochMilli(millisA).atZone(zoneId).toLocalDate()
        val dateB = Instant.ofEpochMilli(millisB).atZone(zoneId).toLocalDate()
        return dateA.year == dateB.year && dateA.monthValue == dateB.monthValue
    }

    fun formatMonthYear(epochMillis: Long, zoneId: ZoneId = VIETNAM_ZONE): String {
        val date = Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDate()
        return "Tháng ${date.monthValue.toString().padStart(2, '0')}/${date.year}"
    }

    fun formatFriendlyDateHeader(epochMillis: Long, nowMillis: Long = System.currentTimeMillis(), zoneId: ZoneId = VIETNAM_ZONE): String {
        val date = Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDate()
        val today = Instant.ofEpochMilli(nowMillis).atZone(zoneId).toLocalDate()
        val dateStr = date.format(DD_MM_YYYY)
        return when (date) {
            today -> "Hôm nay • $dateStr"
            today.minusDays(1) -> "Hôm qua • $dateStr"
            else -> {
                val dow = when (date.dayOfWeek) {
                    DayOfWeek.MONDAY -> "Thứ 2"
                    DayOfWeek.TUESDAY -> "Thứ 3"
                    DayOfWeek.WEDNESDAY -> "Thứ 4"
                    DayOfWeek.THURSDAY -> "Thứ 5"
                    DayOfWeek.FRIDAY -> "Thứ 6"
                    DayOfWeek.SATURDAY -> "Thứ 7"
                    DayOfWeek.SUNDAY -> "Chủ nhật"
                }
                "$dow • $dateStr"
            }
        }
    }

    fun getPeriodRange(
        periodType: ReportPeriodType,
        nowMillis: Long = System.currentTimeMillis(),
        customStartMillis: Long? = null,
        customEndMillis: Long? = null,
        zoneId: ZoneId = VIETNAM_ZONE
    ): DateRangeMillis {
        val today = Instant.ofEpochMilli(nowMillis).atZone(zoneId).toLocalDate()
        return when (periodType) {
            ReportPeriodType.TODAY -> {
                DateRangeMillis(
                    startMillis = startOfDayMillis(today, zoneId),
                    endMillis = endOfDayMillis(today, zoneId),
                    label = "Hôm nay (${today.format(DD_MM_YYYY)})"
                )
            }
            ReportPeriodType.THIS_WEEK -> {
                val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                DateRangeMillis(
                    startMillis = startOfDayMillis(monday, zoneId),
                    endMillis = endOfDayMillis(sunday, zoneId),
                    label = "Tuần này (${monday.format(DD_MM_YYYY)} - ${sunday.format(DD_MM_YYYY)})"
                )
            }
            ReportPeriodType.THIS_MONTH -> {
                val firstDay = today.withDayOfMonth(1)
                val lastDay = today.with(TemporalAdjusters.lastDayOfMonth())
                DateRangeMillis(
                    startMillis = startOfDayMillis(firstDay, zoneId),
                    endMillis = endOfDayMillis(lastDay, zoneId),
                    label = "Tháng ${today.monthValue}/${today.year}"
                )
            }
            ReportPeriodType.THIS_YEAR -> {
                val firstDay = today.withDayOfYear(1)
                val lastDay = today.with(TemporalAdjusters.lastDayOfYear())
                DateRangeMillis(
                    startMillis = startOfDayMillis(firstDay, zoneId),
                    endMillis = endOfDayMillis(lastDay, zoneId),
                    label = "Năm ${today.year}"
                )
            }
            ReportPeriodType.CUSTOM -> {
                val startLocal = customStartMillis?.let {
                    Instant.ofEpochMilli(it).atZone(zoneId).toLocalDate()
                } ?: today.withDayOfMonth(1)
                val endLocal = customEndMillis?.let {
                    Instant.ofEpochMilli(it).atZone(zoneId).toLocalDate()
                } ?: today
                val actualStart = if (startLocal.isAfter(endLocal)) endLocal else startLocal
                val actualEnd = if (endLocal.isBefore(startLocal)) startLocal else endLocal
                DateRangeMillis(
                    startMillis = startOfDayMillis(actualStart, zoneId),
                    endMillis = endOfDayMillis(actualEnd, zoneId),
                    label = "Từ ${actualStart.format(DD_MM_YYYY)} đến ${actualEnd.format(DD_MM_YYYY)}"
                )
            }
        }
    }
}
