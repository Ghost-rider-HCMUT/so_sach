package com.sotraubo.app.core.model;

import androidx.compose.runtime.Immutable;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b0\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u008f\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u0012\u0006\u0010\r\u001a\u00020\u0005\u0012\u0006\u0010\u000e\u001a\u00020\u0003\u0012\u0006\u0010\u000f\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0005\u0012\u0006\u0010\u0012\u001a\u00020\u0005\u0012\u0006\u0010\u0013\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0016J\t\u0010*\u001a\u00020\u0003H\u00c6\u0003J\t\u0010+\u001a\u00020\u0005H\u00c6\u0003J\t\u0010,\u001a\u00020\u0005H\u00c6\u0003J\t\u0010-\u001a\u00020\u0005H\u00c6\u0003J\t\u0010.\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010/\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u00100\u001a\u00020\u0005H\u00c6\u0003J\t\u00101\u001a\u00020\u0005H\u00c6\u0003J\t\u00102\u001a\u00020\u0007H\u00c6\u0003J\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\u000b\u00104\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\u000b\u00105\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\t\u00106\u001a\u00020\u0005H\u00c6\u0003J\t\u00107\u001a\u00020\u0003H\u00c6\u0003J\t\u00108\u001a\u00020\u0005H\u00c6\u0003J\u00a5\u0001\u00109\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\r\u001a\u00020\u00052\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00052\b\b\u0002\u0010\u0010\u001a\u00020\u00052\b\b\u0002\u0010\u0011\u001a\u00020\u00052\b\b\u0002\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u0013\u001a\u00020\u00032\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010:\u001a\u00020\u00072\b\u0010;\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010<\u001a\u00020=H\u00d6\u0001J\t\u0010>\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u000f\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\r\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0018R\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u0015\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0018R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u001fR\u0011\u0010\u0010\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0018R\u0013\u0010\u0014\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001dR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0018R\u0011\u0010\u0011\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0018R\u0011\u0010\u0013\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001dR\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001dR\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0011\u0010\u0012\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u0018R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001d\u00a8\u0006?"}, d2 = {"Lcom/sotraubo/app/core/model/UnifiedActivityItem;", "", "uniqueKey", "", "recordId", "", "isTradeOrder", "", "titleLabel", "tradeOrderType", "Lcom/sotraubo/app/core/model/TradeOrderType;", "cashFlowType", "Lcom/sotraubo/app/core/model/CashFlowType;", "contactId", "contactName", "amount", "paidAmount", "remainingDebt", "transactionDate", "shortDescription", "paymentMethodLabel", "createdAt", "(Ljava/lang/String;JZLjava/lang/String;Lcom/sotraubo/app/core/model/TradeOrderType;Lcom/sotraubo/app/core/model/CashFlowType;JLjava/lang/String;JJJJLjava/lang/String;Ljava/lang/String;J)V", "getAmount", "()J", "getCashFlowType", "()Lcom/sotraubo/app/core/model/CashFlowType;", "getContactId", "getContactName", "()Ljava/lang/String;", "getCreatedAt", "()Z", "getPaidAmount", "getPaymentMethodLabel", "getRecordId", "getRemainingDebt", "getShortDescription", "getTitleLabel", "getTradeOrderType", "()Lcom/sotraubo/app/core/model/TradeOrderType;", "getTransactionDate", "getUniqueKey", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
@androidx.compose.runtime.Immutable()
public final class UnifiedActivityItem {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String uniqueKey = null;
    private final long recordId = 0L;
    private final boolean isTradeOrder = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String titleLabel = null;
    @org.jetbrains.annotations.Nullable()
    private final com.sotraubo.app.core.model.TradeOrderType tradeOrderType = null;
    @org.jetbrains.annotations.Nullable()
    private final com.sotraubo.app.core.model.CashFlowType cashFlowType = null;
    private final long contactId = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String contactName = null;
    private final long amount = 0L;
    private final long paidAmount = 0L;
    private final long remainingDebt = 0L;
    private final long transactionDate = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String shortDescription = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String paymentMethodLabel = null;
    private final long createdAt = 0L;
    
    public UnifiedActivityItem(@org.jetbrains.annotations.NotNull()
    java.lang.String uniqueKey, long recordId, boolean isTradeOrder, @org.jetbrains.annotations.NotNull()
    java.lang.String titleLabel, @org.jetbrains.annotations.Nullable()
    com.sotraubo.app.core.model.TradeOrderType tradeOrderType, @org.jetbrains.annotations.Nullable()
    com.sotraubo.app.core.model.CashFlowType cashFlowType, long contactId, @org.jetbrains.annotations.NotNull()
    java.lang.String contactName, long amount, long paidAmount, long remainingDebt, long transactionDate, @org.jetbrains.annotations.NotNull()
    java.lang.String shortDescription, @org.jetbrains.annotations.Nullable()
    java.lang.String paymentMethodLabel, long createdAt) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUniqueKey() {
        return null;
    }
    
    public final long getRecordId() {
        return 0L;
    }
    
    public final boolean isTradeOrder() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitleLabel() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.sotraubo.app.core.model.TradeOrderType getTradeOrderType() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.sotraubo.app.core.model.CashFlowType getCashFlowType() {
        return null;
    }
    
    public final long getContactId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getContactName() {
        return null;
    }
    
    public final long getAmount() {
        return 0L;
    }
    
    public final long getPaidAmount() {
        return 0L;
    }
    
    public final long getRemainingDebt() {
        return 0L;
    }
    
    public final long getTransactionDate() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getShortDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPaymentMethodLabel() {
        return null;
    }
    
    public final long getCreatedAt() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final long component10() {
        return 0L;
    }
    
    public final long component11() {
        return 0L;
    }
    
    public final long component12() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component14() {
        return null;
    }
    
    public final long component15() {
        return 0L;
    }
    
    public final long component2() {
        return 0L;
    }
    
    public final boolean component3() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.sotraubo.app.core.model.TradeOrderType component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.sotraubo.app.core.model.CashFlowType component6() {
        return null;
    }
    
    public final long component7() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    public final long component9() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.model.UnifiedActivityItem copy(@org.jetbrains.annotations.NotNull()
    java.lang.String uniqueKey, long recordId, boolean isTradeOrder, @org.jetbrains.annotations.NotNull()
    java.lang.String titleLabel, @org.jetbrains.annotations.Nullable()
    com.sotraubo.app.core.model.TradeOrderType tradeOrderType, @org.jetbrains.annotations.Nullable()
    com.sotraubo.app.core.model.CashFlowType cashFlowType, long contactId, @org.jetbrains.annotations.NotNull()
    java.lang.String contactName, long amount, long paidAmount, long remainingDebt, long transactionDate, @org.jetbrains.annotations.NotNull()
    java.lang.String shortDescription, @org.jetbrains.annotations.Nullable()
    java.lang.String paymentMethodLabel, long createdAt) {
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