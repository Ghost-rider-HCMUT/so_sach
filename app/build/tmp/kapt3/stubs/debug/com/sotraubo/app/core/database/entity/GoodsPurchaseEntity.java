package com.sotraubo.app.core.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Hàng hóa mua (cám, thuốc thú y, cỏ, dụng cụ...).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b#\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001Ba\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0006\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0006\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0006\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010 \u001a\u00020\u0006H\u00c6\u0003J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\"\u001a\u00020\u0006H\u00c6\u0003J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\u0006H\u00c6\u0003J\u0010\u0010%\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0010J\t\u0010&\u001a\u00020\u0006H\u00c6\u0003Jt\u0010\'\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00062\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\f\u001a\u00020\u00062\b\b\u0002\u0010\r\u001a\u00020\u0003H\u00c6\u0001\u00a2\u0006\u0002\u0010(J\u0013\u0010)\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010,\u001a\u00020-H\u00d6\u0001J\t\u0010.\u001a\u00020\u0006H\u00d6\u0001R\u001a\u0010\u000b\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0011\u001a\u0004\b\u000f\u0010\u0010R\u0016\u0010\r\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0016\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0016\u0010\f\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016R\u0016\u0010\u0004\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0013R\u0016\u0010\u0007\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0013R\u0016\u0010\n\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016R\u0016\u0010\t\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0013R\u0016\u0010\b\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0016\u00a8\u0006/"}, d2 = {"Lcom/sotraubo/app/core/database/entity/GoodsPurchaseEntity;", "", "id", "", "purchaseDate", "itemName", "", "quantityX100", "unit", "totalAmount", "supplier", "contactId", "note", "createdAt", "(JJLjava/lang/String;JLjava/lang/String;JLjava/lang/String;Ljava/lang/Long;Ljava/lang/String;J)V", "getContactId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getCreatedAt", "()J", "getId", "getItemName", "()Ljava/lang/String;", "getNote", "getPurchaseDate", "getQuantityX100", "getSupplier", "getTotalAmount", "getUnit", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JJLjava/lang/String;JLjava/lang/String;JLjava/lang/String;Ljava/lang/Long;Ljava/lang/String;J)Lcom/sotraubo/app/core/database/entity/GoodsPurchaseEntity;", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
@androidx.room.Entity(tableName = "goods_purchases", indices = {@androidx.room.Index(value = {"purchase_date"})})
public final class GoodsPurchaseEntity {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @androidx.room.ColumnInfo(name = "id")
    private final long id = 0L;
    @androidx.room.ColumnInfo(name = "purchase_date")
    private final long purchaseDate = 0L;
    @androidx.room.ColumnInfo(name = "item_name")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String itemName = null;
    
    /**
     * Số lượng nhân 100 để lưu số thập phân (vd 2,5 kg = 250).
     */
    @androidx.room.ColumnInfo(name = "quantity_x100")
    private final long quantityX100 = 0L;
    @androidx.room.ColumnInfo(name = "unit")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String unit = null;
    @androidx.room.ColumnInfo(name = "total_amount")
    private final long totalAmount = 0L;
    @androidx.room.ColumnInfo(name = "supplier")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String supplier = null;
    @androidx.room.ColumnInfo(name = "contact_id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long contactId = null;
    @androidx.room.ColumnInfo(name = "note")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String note = null;
    @androidx.room.ColumnInfo(name = "created_at")
    private final long createdAt = 0L;
    
    public GoodsPurchaseEntity(long id, long purchaseDate, @org.jetbrains.annotations.NotNull()
    java.lang.String itemName, long quantityX100, @org.jetbrains.annotations.NotNull()
    java.lang.String unit, long totalAmount, @org.jetbrains.annotations.NotNull()
    java.lang.String supplier, @org.jetbrains.annotations.Nullable()
    java.lang.Long contactId, @org.jetbrains.annotations.NotNull()
    java.lang.String note, long createdAt) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    public final long getPurchaseDate() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getItemName() {
        return null;
    }
    
    /**
     * Số lượng nhân 100 để lưu số thập phân (vd 2,5 kg = 250).
     */
    public final long getQuantityX100() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUnit() {
        return null;
    }
    
    public final long getTotalAmount() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSupplier() {
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
    
    public final long component10() {
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
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    public final long component6() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.database.entity.GoodsPurchaseEntity copy(long id, long purchaseDate, @org.jetbrains.annotations.NotNull()
    java.lang.String itemName, long quantityX100, @org.jetbrains.annotations.NotNull()
    java.lang.String unit, long totalAmount, @org.jetbrains.annotations.NotNull()
    java.lang.String supplier, @org.jetbrains.annotations.Nullable()
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