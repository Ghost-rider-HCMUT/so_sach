package com.sotraubo.app.core.common.format;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b\u00a8\u0006\f"}, d2 = {"Lcom/sotraubo/app/core/common/format/ReportPeriodType;", "", "displayLabel", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getDisplayLabel", "()Ljava/lang/String;", "TODAY", "THIS_WEEK", "THIS_MONTH", "THIS_YEAR", "CUSTOM", "app_release"})
public enum ReportPeriodType {
    /*public static final*/ TODAY /* = new TODAY(null) */,
    /*public static final*/ THIS_WEEK /* = new THIS_WEEK(null) */,
    /*public static final*/ THIS_MONTH /* = new THIS_MONTH(null) */,
    /*public static final*/ THIS_YEAR /* = new THIS_YEAR(null) */,
    /*public static final*/ CUSTOM /* = new CUSTOM(null) */;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String displayLabel = null;
    
    ReportPeriodType(java.lang.String displayLabel) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDisplayLabel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.sotraubo.app.core.common.format.ReportPeriodType> getEntries() {
        return null;
    }
}