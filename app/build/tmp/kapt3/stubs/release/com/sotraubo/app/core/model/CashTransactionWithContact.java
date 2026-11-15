package com.sotraubo.app.core.model;

import androidx.compose.runtime.Immutable;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b,\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0089\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\b\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u0003\u0012\u0006\u0010\u000f\u001a\u00020\u0003\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\b\u0012\u0006\u0010\u0013\u001a\u00020\b\u0012\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\b0\u0015\u0012\u0006\u0010\u0016\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0017J\t\u0010.\u001a\u00020\u0003H\u00c6\u0003J\t\u0010/\u001a\u00020\u0003H\u00c6\u0003J\t\u00100\u001a\u00020\u0011H\u00c6\u0003J\t\u00101\u001a\u00020\bH\u00c6\u0003J\t\u00102\u001a\u00020\bH\u00c6\u0003J\u000f\u00103\u001a\b\u0012\u0004\u0012\u00020\b0\u0015H\u00c6\u0003J\t\u00104\u001a\u00020\u0003H\u00c6\u0003J\t\u00105\u001a\u00020\u0005H\u00c6\u0003J\t\u00106\u001a\u00020\u0003H\u00c6\u0003J\t\u00107\u001a\u00020\bH\u00c6\u0003J\t\u00108\u001a\u00020\bH\u00c6\u0003J\u0010\u00109\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010+J\u000b\u0010:\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\t\u0010;\u001a\u00020\rH\u00c6\u0003J\t\u0010<\u001a\u00020\u0003H\u00c6\u0003J\u00ae\u0001\u0010=\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\b2\b\b\u0002\u0010\u0013\u001a\u00020\b2\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\b0\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u0003H\u00c6\u0001\u00a2\u0006\u0002\u0010>J\u0013\u0010?\u001a\u00020\r2\b\u0010@\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010A\u001a\u00020BH\u00d6\u0001J\t\u0010C\u001a\u00020\bH\u00d6\u0001R\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\b0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0019R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001eR\u0011\u0010\u0016\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0019R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0019R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010$R\u0011\u0010\u0013\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001eR\u0011\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0011\u0010\u0012\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001eR\u0013\u0010\u000b\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001eR\u0015\u0010\n\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010,\u001a\u0004\b*\u0010+R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\u0019\u00a8\u0006D"}, d2 = {"Lcom/sotraubo/app/core/model/CashTransactionWithContact;", "", "id", "", "flowType", "Lcom/sotraubo/app/core/model/CashFlowType;", "contactId", "contactName", "", "contactPhone", "tradeOrderId", "tradeOrderDescription", "isInitialPayment", "", "amount", "transactionDate", "paymentMethod", "Lcom/sotraubo/app/core/model/PaymentMethod;", "shortContent", "note", "attachmentPaths", "", "createdAt", "(JLcom/sotraubo/app/core/model/CashFlowType;JLjava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/String;ZJJLcom/sotraubo/app/core/model/PaymentMethod;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;J)V", "getAmount", "()J", "getAttachmentPaths", "()Ljava/util/List;", "getContactId", "getContactName", "()Ljava/lang/String;", "getContactPhone", "getCreatedAt", "getFlowType", "()Lcom/sotraubo/app/core/model/CashFlowType;", "getId", "()Z", "getNote", "getPaymentMethod", "()Lcom/sotraubo/app/core/model/PaymentMethod;", "getShortContent", "getTradeOrderDescription", "getTradeOrderId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getTransactionDate", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JLcom/sotraubo/app/core/model/CashFlowType;JLjava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/String;ZJJLcom/sotraubo/app/core/model/PaymentMethod;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;J)Lcom/sotraubo/app/core/model/CashTransactionWithContact;", "equals", "other", "hashCode", "", "toString", "app_release"})
@androidx.compose.runtime.Immutable()
public final class CashTransactionWithContact {
    private final long id = 0L;
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.core.model.CashFlowType flowType = null;
    private final long contactId = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String contactName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String contactPhone = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long tradeOrderId = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String tradeOrderDescription = null;
    private final boolean isInitialPayment = false;
    private final long amount = 0L;
    private final long transactionDate = 0L;
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.core.model.PaymentMethod paymentMethod = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String shortContent = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String note = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> attachmentPaths = null;
    private final long createdAt = 0L;
    
    public CashTransactionWithContact(long id, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.CashFlowType flowType, long contactId, @org.jetbrains.annotations.NotNull()
    java.lang.String contactName, @org.jetbrains.annotations.NotNull()
    java.lang.String contactPhone, @org.jetbrains.annotations.Nullable()
    java.lang.Long tradeOrderId, @org.jetbrains.annotations.Nullable()
    java.lang.String tradeOrderDescription, boolean isInitialPayment, long amount, long transactionDate, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.PaymentMethod paymentMethod, @org.jetbrains.annotations.NotNull()
    java.lang.String shortContent, @org.jetbrains.annotations.NotNull()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> attachmentPaths, long createdAt) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.model.CashFlowType getFlowType() {
        return null;
    }
    
    public final long getContactId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getContactName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getContactPhone() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getTradeOrderId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTradeOrderDescription() {
        return null;
    }
    
    public final boolean isInitialPayment() {
        return false;
    }
    
    public final long getAmount() {
        return 0L;
    }
    
    public final long getTransactionDate() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.model.PaymentMethod getPaymentMethod() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getShortContent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNote() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getAttachmentPaths() {
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
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.model.PaymentMethod component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component14() {
        return null;
    }
    
    public final long component15() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.model.CashFlowType component2() {
        return null;
    }
    
    public final long component3() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }
    
    public final boolean component8() {
        return false;
    }
    
    public final long component9() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.model.CashTransactionWithContact copy(long id, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.CashFlowType flowType, long contactId, @org.jetbrains.annotations.NotNull()
    java.lang.String contactName, @org.jetbrains.annotations.NotNull()
    java.lang.String contactPhone, @org.jetbrains.annotations.Nullable()
    java.lang.Long tradeOrderId, @org.jetbrains.annotations.Nullable()
    java.lang.String tradeOrderDescription, boolean isInitialPayment, long amount, long transactionDate, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.PaymentMethod paymentMethod, @org.jetbrains.annotations.NotNull()
    java.lang.String shortContent, @org.jetbrains.annotations.NotNull()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> attachmentPaths, long createdAt) {
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