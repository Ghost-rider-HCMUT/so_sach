package com.sotraubo.app.core.common.format;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\t\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\f2\b\b\u0002\u0010\u000f\u001a\u00020\bJ \u0010\u0010\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u000f\u001a\u00020\bJ\u0018\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\bJ\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\r\u001a\u00020\f2\b\b\u0002\u0010\u000f\u001a\u00020\bJ\u0018\u0010\u0017\u001a\u00020\u00162\u0006\u0010\r\u001a\u00020\f2\b\b\u0002\u0010\u000f\u001a\u00020\bJ\u0018\u0010\u0018\u001a\u00020\u00162\u0006\u0010\r\u001a\u00020\f2\b\b\u0002\u0010\u000f\u001a\u00020\bJ\"\u0010\u0019\u001a\u00020\u00162\u0006\u0010\r\u001a\u00020\f2\b\b\u0002\u0010\u001a\u001a\u00020\f2\b\b\u0002\u0010\u000f\u001a\u00020\bJ\u0018\u0010\u001b\u001a\u00020\u00162\u0006\u0010\r\u001a\u00020\f2\b\b\u0002\u0010\u000f\u001a\u00020\bJ?\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\b\b\u0002\u0010\u001a\u001a\u00020\f2\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\f2\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\u000f\u001a\u00020\b\u00a2\u0006\u0002\u0010\"J \u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\f2\u0006\u0010&\u001a\u00020\f2\b\b\u0002\u0010\u000f\u001a\u00020\bJ \u0010\'\u001a\u00020$2\u0006\u0010%\u001a\u00020\f2\u0006\u0010&\u001a\u00020\f2\b\b\u0002\u0010\u000f\u001a\u00020\bJ\u001f\u0010(\u001a\u0004\u0018\u00010\f2\u0006\u0010)\u001a\u00020\u00162\b\b\u0002\u0010\u000f\u001a\u00020\b\u00a2\u0006\u0002\u0010*J\u0018\u0010+\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\bJ\u001a\u0010,\u001a\u00020\f2\b\b\u0002\u0010\u001a\u001a\u00020\f2\b\b\u0002\u0010\u000f\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006-"}, d2 = {"Lcom/sotraubo/app/core/common/format/DateFormatter;", "", "()V", "DD_MM_YYYY", "Ljava/time/format/DateTimeFormatter;", "DD_MM_YYYY_HH_MM", "FILE_SAFE_TIMESTAMP", "VIETNAM_ZONE", "Ljava/time/ZoneId;", "getVIETNAM_ZONE", "()Ljava/time/ZoneId;", "addDays", "", "epochMillis", "days", "zoneId", "addMonths", "months", "endOfDayMillis", "localDate", "Ljava/time/LocalDate;", "formatDate", "", "formatDateTime", "formatFileSafeTimestamp", "formatFriendlyDateHeader", "nowMillis", "formatMonthYear", "getPeriodRange", "Lcom/sotraubo/app/core/common/format/DateRangeMillis;", "periodType", "Lcom/sotraubo/app/core/common/format/ReportPeriodType;", "customStartMillis", "customEndMillis", "(Lcom/sotraubo/app/core/common/format/ReportPeriodType;JLjava/lang/Long;Ljava/lang/Long;Ljava/time/ZoneId;)Lcom/sotraubo/app/core/common/format/DateRangeMillis;", "isSameDay", "", "millisA", "millisB", "isSameMonth", "parseDdMmYyyyToMillis", "dateStr", "(Ljava/lang/String;Ljava/time/ZoneId;)Ljava/lang/Long;", "startOfDayMillis", "todayNoonMillis", "app_debug"})
public final class DateFormatter {
    @org.jetbrains.annotations.NotNull()
    private static final java.time.ZoneId VIETNAM_ZONE = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.time.format.DateTimeFormatter DD_MM_YYYY = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.time.format.DateTimeFormatter DD_MM_YYYY_HH_MM = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.time.format.DateTimeFormatter FILE_SAFE_TIMESTAMP = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.sotraubo.app.core.common.format.DateFormatter INSTANCE = null;
    
    private DateFormatter() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.ZoneId getVIETNAM_ZONE() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatDate(long epochMillis, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zoneId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatDateTime(long epochMillis, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zoneId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatFileSafeTimestamp(long epochMillis, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zoneId) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long parseDdMmYyyyToMillis(@org.jetbrains.annotations.NotNull()
    java.lang.String dateStr, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zoneId) {
        return null;
    }
    
    public final long startOfDayMillis(@org.jetbrains.annotations.NotNull()
    java.time.LocalDate localDate, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zoneId) {
        return 0L;
    }
    
    public final long endOfDayMillis(@org.jetbrains.annotations.NotNull()
    java.time.LocalDate localDate, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zoneId) {
        return 0L;
    }
    
    public final long todayNoonMillis(long nowMillis, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zoneId) {
        return 0L;
    }
    
    public final long addDays(long epochMillis, long days, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zoneId) {
        return 0L;
    }
    
    public final long addMonths(long epochMillis, long months, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zoneId) {
        return 0L;
    }
    
    public final boolean isSameDay(long millisA, long millisB, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zoneId) {
        return false;
    }
    
    public final boolean isSameMonth(long millisA, long millisB, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zoneId) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatMonthYear(long epochMillis, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zoneId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatFriendlyDateHeader(long epochMillis, long nowMillis, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zoneId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.common.format.DateRangeMillis getPeriodRange(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.common.format.ReportPeriodType periodType, long nowMillis, @org.jetbrains.annotations.Nullable()
    java.lang.Long customStartMillis, @org.jetbrains.annotations.Nullable()
    java.lang.Long customEndMillis, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zoneId) {
        return null;
    }
}