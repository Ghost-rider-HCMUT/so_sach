package com.sotraubo.app.core.model;

import androidx.compose.runtime.Immutable;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b \n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B\u00a9\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u0012\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\t\u0012\b\b\u0002\u0010\r\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\n0\t\u0012\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\f0\t\u0012\u000e\b\u0002\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\f0\t\u0012\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\t\u00a2\u0006\u0002\u0010\u0015J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\'\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u00c6\u0003J\u000f\u0010(\u001a\b\u0012\u0004\u0012\u00020\f0\tH\u00c6\u0003J\u000f\u0010)\u001a\b\u0012\u0004\u0012\u00020\f0\tH\u00c6\u0003J\u000f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00140\tH\u00c6\u0003J\t\u0010+\u001a\u00020\u0005H\u00c6\u0003J\t\u0010,\u001a\u00020\u0005H\u00c6\u0003J\t\u0010-\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010.\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u00c6\u0003J\u000f\u0010/\u001a\b\u0012\u0004\u0012\u00020\f0\tH\u00c6\u0003J\t\u00100\u001a\u00020\u0005H\u00c6\u0003J\t\u00101\u001a\u00020\u0005H\u00c6\u0003J\t\u00102\u001a\u00020\u0005H\u00c6\u0003J\u00af\u0001\u00103\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\t2\b\b\u0002\u0010\r\u001a\u00020\u00052\b\b\u0002\u0010\u000e\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\u00052\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\f0\t2\u000e\b\u0002\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\f0\t2\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\tH\u00c6\u0001J\u0013\u00104\u001a\u0002052\b\u00106\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00107\u001a\u000208H\u00d6\u0001J\t\u00109\u001a\u00020:H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u000f\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0019R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\r\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0019R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0019R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001cR\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\f0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001cR\u0011\u0010\u000e\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0019R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0019R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001cR\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001cR\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\f0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001c\u00a8\u0006;"}, d2 = {"Lcom/sotraubo/app/core/model/PersonBidirectionalDebtDetail;", "", "contact", "Lcom/sotraubo/app/core/model/Contact;", "initialReceivableTotal", "", "paidReceivableTotal", "currentReceivableDebt", "saleOrders", "", "Lcom/sotraubo/app/core/model/TradeOrderWithPayment;", "linkedCashInTransactions", "Lcom/sotraubo/app/core/model/CashTransactionWithContact;", "initialPayableTotal", "paidPayableTotal", "currentPayableDebt", "purchaseOrders", "linkedCashOutTransactions", "standaloneTransactions", "debtTimeline", "Lcom/sotraubo/app/core/model/DebtHistoryEvent;", "(Lcom/sotraubo/app/core/model/Contact;JJJLjava/util/List;Ljava/util/List;JJJLjava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;)V", "getContact", "()Lcom/sotraubo/app/core/model/Contact;", "getCurrentPayableDebt", "()J", "getCurrentReceivableDebt", "getDebtTimeline", "()Ljava/util/List;", "getInitialPayableTotal", "getInitialReceivableTotal", "getLinkedCashInTransactions", "getLinkedCashOutTransactions", "getPaidPayableTotal", "getPaidReceivableTotal", "getPurchaseOrders", "getSaleOrders", "getStandaloneTransactions", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_release"})
@androidx.compose.runtime.Immutable()
public final class PersonBidirectionalDebtDetail {
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.core.model.Contact contact = null;
    private final long initialReceivableTotal = 0L;
    private final long paidReceivableTotal = 0L;
    private final long currentReceivableDebt = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment> saleOrders = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> linkedCashInTransactions = null;
    private final long initialPayableTotal = 0L;
    private final long paidPayableTotal = 0L;
    private final long currentPayableDebt = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment> purchaseOrders = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> linkedCashOutTransactions = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> standaloneTransactions = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.sotraubo.app.core.model.DebtHistoryEvent> debtTimeline = null;
    
    public PersonBidirectionalDebtDetail(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.Contact contact, long initialReceivableTotal, long paidReceivableTotal, long currentReceivableDebt, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment> saleOrders, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> linkedCashInTransactions, long initialPayableTotal, long paidPayableTotal, long currentPayableDebt, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment> purchaseOrders, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> linkedCashOutTransactions, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> standaloneTransactions, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.DebtHistoryEvent> debtTimeline) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.model.Contact getContact() {
        return null;
    }
    
    public final long getInitialReceivableTotal() {
        return 0L;
    }
    
    public final long getPaidReceivableTotal() {
        return 0L;
    }
    
    public final long getCurrentReceivableDebt() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment> getSaleOrders() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> getLinkedCashInTransactions() {
        return null;
    }
    
    public final long getInitialPayableTotal() {
        return 0L;
    }
    
    public final long getPaidPayableTotal() {
        return 0L;
    }
    
    public final long getCurrentPayableDebt() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment> getPurchaseOrders() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> getLinkedCashOutTransactions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> getStandaloneTransactions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.model.DebtHistoryEvent> getDebtTimeline() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.core.model.Contact component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment> component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.model.DebtHistoryEvent> component13() {
        return null;
    }
    
    public final long component2() {
        return 0L;
    }
    
    public final long component3() {
        return 0L;
    }
    
    public final long component4() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> component6() {
        return null;
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
    public final com.sotraubo.app.core.model.PersonBidirectionalDebtDetail copy(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.Contact contact, long initialReceivableTotal, long paidReceivableTotal, long currentReceivableDebt, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment> saleOrders, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> linkedCashInTransactions, long initialPayableTotal, long paidPayableTotal, long currentPayableDebt, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment> purchaseOrders, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> linkedCashOutTransactions, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> standaloneTransactions, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.DebtHistoryEvent> debtTimeline) {
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