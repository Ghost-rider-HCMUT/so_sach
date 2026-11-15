package com.sotraubo.app.core.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B9\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u000bJ\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0016\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003JD\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0003H\u00c6\u0001\u00a2\u0006\u0002\u0010\u0019J\u0013\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001d\u001a\u00020\u001eH\u00d6\u0001J\t\u0010\u001f\u001a\u00020\u0007H\u00d6\u0001R\u001a\u0010\u0005\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\b\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0016\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\f\u001a\u0004\b\u0012\u0010\u000b\u00a8\u0006 "}, d2 = {"Lcom/sotraubo/app/core/database/entity/AttachmentEntity;", "", "id", "", "tradeOrderId", "cashTransactionId", "localFilePath", "", "createdAt", "(JLjava/lang/Long;Ljava/lang/Long;Ljava/lang/String;J)V", "getCashTransactionId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getCreatedAt", "()J", "getId", "getLocalFilePath", "()Ljava/lang/String;", "getTradeOrderId", "component1", "component2", "component3", "component4", "component5", "copy", "(JLjava/lang/Long;Ljava/lang/Long;Ljava/lang/String;J)Lcom/sotraubo/app/core/database/entity/AttachmentEntity;", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
@androidx.room.Entity(tableName = "attachments", foreignKeys = {@androidx.room.ForeignKey(entity = com.sotraubo.app.core.database.entity.TradeOrderEntity.class, parentColumns = {"id"}, childColumns = {"trade_order_id"}, onDelete = 5), @androidx.room.ForeignKey(entity = com.sotraubo.app.core.database.entity.CashTransactionEntity.class, parentColumns = {"id"}, childColumns = {"cash_transaction_id"}, onDelete = 5)}, indices = {@androidx.room.Index(value = {"trade_order_id"}), @androidx.room.Index(value = {"cash_transaction_id"})})
public final class AttachmentEntity {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @androidx.room.ColumnInfo(name = "id")
    private final long id = 0L;
    @androidx.room.ColumnInfo(name = "trade_order_id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long tradeOrderId = null;
    @androidx.room.ColumnInfo(name = "cash_transaction_id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long cashTransactionId = null;
    @androidx.room.ColumnInfo(name = "local_file_path")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String localFilePath = null;
    @androidx.room.ColumnInfo(name = "created_at")
    private final long createdAt = 0L;
    
    public AttachmentEntity(long id, @org.jetbrains.annotations.Nullable()
    java.lang.Long tradeOrderId, @org.jetbrains.annotations.Nullable()
    java.lang.Long cashTransactionId, @org.jetbrains.annotations.NotNull()
    java.lang.String localFilePath, long createdAt) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getTradeOrderId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getCashTransactionId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLocalFilePath() {
        return null;
    }
    
    public final long getCreatedAt() {
        return 0L;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    public final long component5() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.database.entity.AttachmentEntity copy(long id, @org.jetbrains.annotations.Nullable()
    java.lang.Long tradeOrderId, @org.jetbrains.annotations.Nullable()
    java.lang.Long cashTransactionId, @org.jetbrains.annotations.NotNull()
    java.lang.String localFilePath, long createdAt) {
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