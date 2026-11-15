package com.sotraubo.app.core.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Đổi tiền (VND <-> Baht Thái).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b,\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u007f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0006\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0011J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\u0006H\u00c6\u0003J\u0010\u0010%\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0015J\t\u0010&\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\'\u001a\u00020\u0003H\u00c6\u0003J\t\u0010(\u001a\u00020\u0003H\u00c6\u0003J\t\u0010)\u001a\u00020\u0006H\u00c6\u0003J\t\u0010*\u001a\u00020\u0003H\u00c6\u0003J\t\u0010+\u001a\u00020\u0003H\u00c6\u0003J\t\u0010,\u001a\u00020\u0003H\u00c6\u0003J\t\u0010-\u001a\u00020\u0003H\u00c6\u0003J\t\u0010.\u001a\u00020\u0003H\u00c6\u0003J\t\u0010/\u001a\u00020\u0003H\u00c6\u0003J\u0092\u0001\u00100\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00062\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00062\b\b\u0002\u0010\u0010\u001a\u00020\u0003H\u00c6\u0001\u00a2\u0006\u0002\u00101J\u0013\u00102\u001a\u0002032\b\u00104\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00105\u001a\u000206H\u00d6\u0001J\t\u00107\u001a\u00020\u0006H\u00d6\u0001R\u0016\u0010\u0007\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001a\u0010\u000e\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0016\u001a\u0004\b\u0014\u0010\u0015R\u0016\u0010\u000b\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0013R\u0016\u0010\f\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0013R\u0016\u0010\u0010\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0013R\u0016\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0016\u0010\u0004\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0013R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0013R\u0016\u0010\u000f\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001bR\u0016\u0010\r\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001bR\u0016\u0010\b\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0013R\u0016\u0010\n\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0013R\u0016\u0010\t\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0013\u00a8\u00068"}, d2 = {"Lcom/sotraubo/app/core/database/entity/CurrencyExchangeEntity;", "", "id", "", "exchangeDate", "direction", "", "bahtAmount", "rate", "vndAmount", "theoreticalVndAmount", "costRate", "costVndAmount", "partnerName", "contactId", "note", "createdAt", "(JJLjava/lang/String;JJJJJJLjava/lang/String;Ljava/lang/Long;Ljava/lang/String;J)V", "getBahtAmount", "()J", "getContactId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getCostRate", "getCostVndAmount", "getCreatedAt", "getDirection", "()Ljava/lang/String;", "getExchangeDate", "getId", "getNote", "getPartnerName", "getRate", "getTheoreticalVndAmount", "getVndAmount", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JJLjava/lang/String;JJJJJJLjava/lang/String;Ljava/lang/Long;Ljava/lang/String;J)Lcom/sotraubo/app/core/database/entity/CurrencyExchangeEntity;", "equals", "", "other", "hashCode", "", "toString", "app_release"})
@androidx.room.Entity(tableName = "currency_exchanges", indices = {@androidx.room.Index(value = {"exchange_date"})})
public final class CurrencyExchangeEntity {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @androidx.room.ColumnInfo(name = "id")
    private final long id = 0L;
    @androidx.room.ColumnInfo(name = "exchange_date")
    private final long exchangeDate = 0L;
    
    /**
     * "VND_TO_THB" (đổi VND lấy Bath) | "THB_TO_VND" (đổi Bath lấy VND).
     */
    @androidx.room.ColumnInfo(name = "direction")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String direction = null;
    @androidx.room.ColumnInfo(name = "baht_amount")
    private final long bahtAmount = 0L;
    
    /**
     * Tỷ giá bán / cần thu: số VND cho 1 Bath.
     */
    @androidx.room.ColumnInfo(name = "rate")
    private final long rate = 0L;
    @androidx.room.ColumnInfo(name = "vnd_amount")
    private final long vndAmount = 0L;
    @androidx.room.ColumnInfo(name = "theoretical_vnd_amount", defaultValue = "0")
    private final long theoreticalVndAmount = 0L;
    
    /**
     * Tỷ giá vốn / giá Bath mình mua hoặc chuyển đi (đ / 1 Bath).
     */
    @androidx.room.ColumnInfo(name = "cost_rate", defaultValue = "0")
    private final long costRate = 0L;
    
    /**
     * Số tiền Việt vốn đã chuyển đi cho giao dịch Đổi Bath (đ).
     */
    @androidx.room.ColumnInfo(name = "cost_vnd_amount", defaultValue = "0")
    private final long costVndAmount = 0L;
    @androidx.room.ColumnInfo(name = "partner_name", defaultValue = "")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String partnerName = null;
    @androidx.room.ColumnInfo(name = "contact_id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long contactId = null;
    @androidx.room.ColumnInfo(name = "note")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String note = null;
    @androidx.room.ColumnInfo(name = "created_at")
    private final long createdAt = 0L;
    
    public CurrencyExchangeEntity(long id, long exchangeDate, @org.jetbrains.annotations.NotNull()
    java.lang.String direction, long bahtAmount, long rate, long vndAmount, long theoreticalVndAmount, long costRate, long costVndAmount, @org.jetbrains.annotations.NotNull()
    java.lang.String partnerName, @org.jetbrains.annotations.Nullable()
    java.lang.Long contactId, @org.jetbrains.annotations.NotNull()
    java.lang.String note, long createdAt) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    public final long getExchangeDate() {
        return 0L;
    }
    
    /**
     * "VND_TO_THB" (đổi VND lấy Bath) | "THB_TO_VND" (đổi Bath lấy VND).
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDirection() {
        return null;
    }
    
    public final long getBahtAmount() {
        return 0L;
    }
    
    /**
     * Tỷ giá bán / cần thu: số VND cho 1 Bath.
     */
    public final long getRate() {
        return 0L;
    }
    
    public final long getVndAmount() {
        return 0L;
    }
    
    public final long getTheoreticalVndAmount() {
        return 0L;
    }
    
    /**
     * Tỷ giá vốn / giá Bath mình mua hoặc chuyển đi (đ / 1 Bath).
     */
    public final long getCostRate() {
        return 0L;
    }
    
    /**
     * Số tiền Việt vốn đã chuyển đi cho giao dịch Đổi Bath (đ).
     */
    public final long getCostVndAmount() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPartnerName() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getContactId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNote() {
        return null;
    }
    
    public final long getCreatedAt() {
        return 0L;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component12() {
        return null;
    }
    
    public final long component13() {
        return 0L;
    }
    
    public final long component2() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    public final long component4() {
        return 0L;
    }
    
    public final long component5() {
        return 0L;
    }
    
    public final long component6() {
        return 0L;
    }
    
    public final long component7() {
        return 0L;
    }
    
    public final long component8() {
        return 0L;
    }
    
    public final long component9() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.database.entity.CurrencyExchangeEntity copy(long id, long exchangeDate, @org.jetbrains.annotations.NotNull()
    java.lang.String direction, long bahtAmount, long rate, long vndAmount, long theoreticalVndAmount, long costRate, long costVndAmount, @org.jetbrains.annotations.NotNull()
    java.lang.String partnerName, @org.jetbrains.annotations.Nullable()
    java.lang.Long contactId, @org.jetbrains.annotations.NotNull()
    java.lang.String note, long createdAt) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}