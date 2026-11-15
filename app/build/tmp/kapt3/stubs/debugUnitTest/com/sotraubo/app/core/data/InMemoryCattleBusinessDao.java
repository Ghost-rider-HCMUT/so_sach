package com.sotraubo.app.core.data;

import com.sotraubo.app.core.common.dispatcher.AppDispatchers;
import com.sotraubo.app.core.common.format.ReportPeriodType;
import com.sotraubo.app.core.data.repository.OfflineCattleBusinessRepository;
import com.sotraubo.app.core.data.repository.OperationOutcome;
import com.sotraubo.app.core.database.dao.CashTransactionQueryProjection;
import com.sotraubo.app.core.database.dao.CattleBusinessDao;
import com.sotraubo.app.core.database.dao.TradeOrderQueryProjection;
import com.sotraubo.app.core.database.entity.AttachmentEntity;
import com.sotraubo.app.core.database.entity.CashTransactionEntity;
import com.sotraubo.app.core.database.entity.ContactEntity;
import com.sotraubo.app.core.database.entity.TradeOrderEntity;
import com.sotraubo.app.core.model.CashFlowType;
import com.sotraubo.app.core.model.DebtSortOption;
import com.sotraubo.app.core.model.PaymentMethod;
import com.sotraubo.app.core.model.TradeOrderType;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.flow.Flow;
import org.junit.Before;
import org.junit.Test;

/**
 * Fake In-Memory DAO mô phỏng chính xác 100% hành vi của SQLite/Room (Bao gồm ForeignKey CASCADE)
 * để chạy kiểm thử tức thì toàn bộ 14 kịch bản nghiệp vụ trong 11_TEST_CASES.md.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0012\u001a\u00020\u0013H\u0096@\u00a2\u0006\u0002\u0010\u0014J\u000e\u0010\u0015\u001a\u00020\u0013H\u0096@\u00a2\u0006\u0002\u0010\u0014J\u000e\u0010\u0016\u001a\u00020\u0013H\u0096@\u00a2\u0006\u0002\u0010\u0014J\u000e\u0010\u0017\u001a\u00020\u0013H\u0096@\u00a2\u0006\u0002\u0010\u0014J\u0016\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001e\u001a\u00020\u00132\u0006\u0010\u001f\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010 \u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010!\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\"\u001a\u00020\u00132\u0006\u0010\u001f\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0014\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0096@\u00a2\u0006\u0002\u0010\u0014J\u0014\u0010$\u001a\b\u0012\u0004\u0012\u00020\b0\u0005H\u0096@\u00a2\u0006\u0002\u0010\u0014J\u0014\u0010%\u001a\b\u0012\u0004\u0012\u00020\n0\u0005H\u0096@\u00a2\u0006\u0002\u0010\u0014J\u0014\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00110\u0005H\u0096@\u00a2\u0006\u0002\u0010\u0014J\u0018\u0010\'\u001a\u0004\u0018\u00010\n2\u0006\u0010\u001a\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0018\u0010(\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001f\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0018\u0010)\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001d\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0018\u0010*\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u001f\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0018\u0010+\u001a\u0004\u0018\u00010,2\u0006\u0010\u001f\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u001c\u0010-\u001a\u00020\u00132\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0096@\u00a2\u0006\u0002\u0010/J\u0016\u00100\u001a\u00020\f2\u0006\u00101\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u00102J\u0016\u00103\u001a\u00020\f2\u0006\u00104\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u00105J\u0016\u00106\u001a\u00020\f2\u0006\u00107\u001a\u00020\u0011H\u0096@\u00a2\u0006\u0002\u00108J\u0014\u00109\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050:H\u0016J\u0014\u0010;\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020<0\u00050:H\u0016J\u0014\u0010=\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00050:H\u0016J\u0014\u0010>\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020,0\u00050:H\u0016J\u0018\u0010?\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0:2\u0006\u0010\u001a\u001a\u00020\fH\u0016J \u0010@\u001a\u00020\f2\u0006\u0010\u001f\u001a\u00020\f2\b\u0010A\u001a\u0004\u0018\u00010\fH\u0096@\u00a2\u0006\u0002\u0010BJ\u0016\u0010C\u001a\u00020\u00132\u0006\u00101\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u00102J\u0016\u0010D\u001a\u00020\u00132\u0006\u00104\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u00105J\u0016\u0010E\u001a\u00020\u00132\u0006\u00107\u001a\u00020\u0011H\u0096@\u00a2\u0006\u0002\u00108R\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006F"}, d2 = {"Lcom/sotraubo/app/core/data/InMemoryCattleBusinessDao;", "Lcom/sotraubo/app/core/database/dao/CattleBusinessDao;", "()V", "attachmentsFlow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/sotraubo/app/core/database/entity/AttachmentEntity;", "cashFlow", "Lcom/sotraubo/app/core/database/entity/CashTransactionEntity;", "contactsFlow", "Lcom/sotraubo/app/core/database/entity/ContactEntity;", "nextAttachmentId", "", "nextCashId", "nextContactId", "nextOrderId", "ordersFlow", "Lcom/sotraubo/app/core/database/entity/TradeOrderEntity;", "clearAllAttachments", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearAllCashTransactions", "clearAllContacts", "clearAllTradeOrders", "countTransactionsForContact", "", "contactId", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAttachmentsForCashTransaction", "transactionId", "deleteAttachmentsForOrder", "orderId", "deleteCashTransactionById", "deleteContactById", "deleteTradeOrderById", "getAllAttachmentsSnapshot", "getAllCashTransactionsSnapshot", "getAllContactsSnapshot", "getAllTradeOrdersSnapshot", "getContactById", "getInitialPaymentForOrder", "getRawCashTransactionById", "getRawTradeOrderById", "getTradeOrderWithPaymentById", "Lcom/sotraubo/app/core/database/dao/TradeOrderQueryProjection;", "insertAttachments", "attachments", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertCashTransaction", "transaction", "(Lcom/sotraubo/app/core/database/entity/CashTransactionEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertContact", "contact", "(Lcom/sotraubo/app/core/database/entity/ContactEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertTradeOrder", "order", "(Lcom/sotraubo/app/core/database/entity/TradeOrderEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeAllAttachments", "Lkotlinx/coroutines/flow/Flow;", "observeAllCashTransactions", "Lcom/sotraubo/app/core/database/dao/CashTransactionQueryProjection;", "observeAllContacts", "observeAllTradeOrdersWithPayment", "observeContactById", "sumPaidAmountForOrder", "excludeTransactionId", "(JLjava/lang/Long;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateCashTransaction", "updateContact", "updateTradeOrder", "app_debugUnitTest"})
public final class InMemoryCattleBusinessDao implements com.sotraubo.app.core.database.dao.CattleBusinessDao {
    private long nextContactId = 1L;
    private long nextOrderId = 1L;
    private long nextCashId = 1L;
    private long nextAttachmentId = 1L;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.sotraubo.app.core.database.entity.ContactEntity>> contactsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.sotraubo.app.core.database.entity.TradeOrderEntity>> ordersFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.sotraubo.app.core.database.entity.CashTransactionEntity>> cashFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity>> attachmentsFlow = null;
    
    public InMemoryCattleBusinessDao() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.entity.ContactEntity>> observeAllContacts() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.sotraubo.app.core.database.entity.ContactEntity> observeContactById(long contactId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getContactById(long contactId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.database.entity.ContactEntity> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getAllContactsSnapshot(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.ContactEntity>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object insertContact(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.ContactEntity contact, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateContact(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.ContactEntity contact, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteContactById(long contactId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object countTransactionsForContact(long contactId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.dao.TradeOrderQueryProjection>> observeAllTradeOrdersWithPayment() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getTradeOrderWithPaymentById(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.database.dao.TradeOrderQueryProjection> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getRawTradeOrderById(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.database.entity.TradeOrderEntity> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getAllTradeOrdersSnapshot(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.TradeOrderEntity>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object insertTradeOrder(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.TradeOrderEntity order, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateTradeOrder(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.TradeOrderEntity order, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteTradeOrderById(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.dao.CashTransactionQueryProjection>> observeAllCashTransactions() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getRawCashTransactionById(long transactionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.database.entity.CashTransactionEntity> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getInitialPaymentForOrder(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.database.entity.CashTransactionEntity> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object sumPaidAmountForOrder(long orderId, @org.jetbrains.annotations.Nullable()
    java.lang.Long excludeTransactionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getAllCashTransactionsSnapshot(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.CashTransactionEntity>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object insertCashTransaction(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.CashTransactionEntity transaction, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateCashTransaction(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.CashTransactionEntity transaction, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteCashTransactionById(long transactionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity>> observeAllAttachments() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getAllAttachmentsSnapshot(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object insertAttachments(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity> attachments, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteAttachmentsForOrder(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteAttachmentsForCashTransaction(long transactionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object clearAllAttachments(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object clearAllCashTransactions(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object clearAllTradeOrders(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object clearAllContacts(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @androidx.room.Transaction()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object replaceAllDataInTransaction(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.ContactEntity> contacts, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.TradeOrderEntity> orders, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CashTransactionEntity> cashTransactions, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity> attachments, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}