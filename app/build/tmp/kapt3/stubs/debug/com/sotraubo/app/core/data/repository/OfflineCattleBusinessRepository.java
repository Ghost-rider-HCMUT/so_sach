package com.sotraubo.app.core.data.repository;

import com.sotraubo.app.core.common.dispatcher.AppDispatchers;
import com.sotraubo.app.core.common.format.DateFormatter;
import com.sotraubo.app.core.common.format.ReportPeriodType;
import com.sotraubo.app.core.common.search.VietnameseTextNormalizer;
import com.sotraubo.app.core.data.FinancialRuleValidator;
import com.sotraubo.app.core.data.ValidationResult;
import com.sotraubo.app.core.database.CattleBusinessDatabase;
import com.sotraubo.app.core.database.dao.CashTransactionQueryProjection;
import com.sotraubo.app.core.database.dao.CattleBusinessDao;
import com.sotraubo.app.core.database.dao.TradeOrderQueryProjection;
import com.sotraubo.app.core.database.entity.AttachmentEntity;
import com.sotraubo.app.core.database.entity.CashTransactionEntity;
import com.sotraubo.app.core.database.entity.ContactEntity;
import com.sotraubo.app.core.database.entity.TradeOrderEntity;
import com.sotraubo.app.core.model.CashFlowType;
import com.sotraubo.app.core.model.CashTransactionWithContact;
import com.sotraubo.app.core.model.Contact;
import com.sotraubo.app.core.model.ContactWithSummary;
import com.sotraubo.app.core.model.DashboardFinancialSummary;
import com.sotraubo.app.core.model.DebtHistoryEvent;
import com.sotraubo.app.core.model.DebtSortOption;
import com.sotraubo.app.core.model.PaymentMethod;
import com.sotraubo.app.core.model.PeriodBusinessStats;
import com.sotraubo.app.core.model.PersonBidirectionalDebtDetail;
import com.sotraubo.app.core.model.PersonDebtSummaryItem;
import com.sotraubo.app.core.model.TradeOrderType;
import com.sotraubo.app.core.model.TradeOrderWithPayment;
import com.sotraubo.app.core.model.UnifiedActivityItem;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00d8\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u001f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJb\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00140\u00172\u0006\u0010\u0018\u001a\u00020\u000bH\u0082@\u00a2\u0006\u0002\u0010\u0019J\u001c\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\n2\u0006\u0010\u001c\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u001dJ\u001c\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001b0\n2\u0006\u0010\u000e\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u001dJ\u001c\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u001b0\n2\u0006\u0010 \u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u001dJ\u0018\u0010!\u001a\u0004\u0018\u00010\"2\u0006\u0010\u001c\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u001dJ\u0018\u0010#\u001a\u0004\u0018\u00010\"2\u0006\u0010 \u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u001dJ\u0018\u0010$\u001a\u0004\u0018\u00010%2\u0006\u0010 \u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u001dJ\u0014\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0\u00170\'H\u0016J\u0014\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020)0\u00170\'H\u0016J\u0014\u0010*\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0\u00170\'H\u0016J\u0018\u0010+\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010)0\'2\u0006\u0010\u000e\u001a\u00020\u000bH\u0016J\u000e\u0010,\u001a\b\u0012\u0004\u0012\u00020-0\'H\u0016J$\u0010.\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020/0\u00170\'2\u0006\u00100\u001a\u00020\u00142\u0006\u00101\u001a\u000202H\u0016J/\u00103\u001a\b\u0012\u0004\u0012\u0002040\'2\u0006\u00105\u001a\u0002062\b\u00107\u001a\u0004\u0018\u00010\u000b2\b\u00108\u001a\u0004\u0018\u00010\u000bH\u0016\u00a2\u0006\u0002\u00109J\u0018\u0010:\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010;0\'2\u0006\u0010\u000e\u001a\u00020\u000bH\u0016J$\u0010<\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020/0\u00170\'2\u0006\u00100\u001a\u00020\u00142\u0006\u00101\u001a\u000202H\u0016J\u001c\u0010=\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020>0\u00170\'2\u0006\u0010?\u001a\u00020@H\u0016J2\u0010A\u001a\u0002HB\"\u0004\b\u0000\u0010B2\u001c\u0010C\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002HB0E\u0012\u0006\u0012\u0004\u0018\u00010F0DH\u0082@\u00a2\u0006\u0002\u0010GJv\u0010H\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000b2\b\u0010I\u001a\u0004\u0018\u00010\u000b2\u0006\u0010J\u001a\u00020K2\u0006\u0010L\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00140\u0017H\u0096@\u00a2\u0006\u0002\u0010MJ<\u0010N\u001a\b\u0012\u0004\u0012\u00020O0\n2\u0006\u0010P\u001a\u00020\u000b2\u0006\u0010Q\u001a\u00020\u00142\u0006\u0010R\u001a\u00020\u00142\u0006\u0010S\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0014H\u0096@\u00a2\u0006\u0002\u0010TJt\u0010U\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010 \u001a\u00020\u000b2\u0006\u0010V\u001a\u00020W2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0010\u001a\u00020\u000b2\u0006\u0010X\u001a\u00020@2\u0006\u0010Y\u001a\u00020\u00142\u0006\u0010Z\u001a\u00020\u000b2\u0006\u0010[\u001a\u00020\u000b2\u0006\u0010\\\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u00142\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00140\u0017H\u0096@\u00a2\u0006\u0002\u0010]J\u001a\u0010^\u001a\u00020\"*\u00020_2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00140\u0017H\u0002J\u001a\u0010^\u001a\u00020%*\u00020`2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00140\u0017H\u0002J\f\u0010^\u001a\u00020O*\u00020aH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006b"}, d2 = {"Lcom/sotraubo/app/core/data/repository/OfflineCattleBusinessRepository;", "Lcom/sotraubo/app/core/data/repository/CattleBusinessRepository;", "database", "Lcom/sotraubo/app/core/database/CattleBusinessDatabase;", "dao", "Lcom/sotraubo/app/core/database/dao/CattleBusinessDao;", "dispatchers", "Lcom/sotraubo/app/core/common/dispatcher/AppDispatchers;", "(Lcom/sotraubo/app/core/database/CattleBusinessDatabase;Lcom/sotraubo/app/core/database/dao/CattleBusinessDao;Lcom/sotraubo/app/core/common/dispatcher/AppDispatchers;)V", "allocatePaymentToOldestOrders", "Lcom/sotraubo/app/core/data/repository/OperationOutcome;", "", "flowType", "Lcom/sotraubo/app/core/model/CashFlowType;", "contactId", "totalPaymentAmount", "transactionDate", "paymentMethod", "Lcom/sotraubo/app/core/model/PaymentMethod;", "shortContent", "", "note", "attachmentPaths", "", "now", "(Lcom/sotraubo/app/core/model/CashFlowType;JJJLcom/sotraubo/app/core/model/PaymentMethod;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteCashTransaction", "", "transactionId", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteContact", "deleteTradeOrder", "orderId", "getCashTransactionById", "Lcom/sotraubo/app/core/model/CashTransactionWithContact;", "getInitialPaymentForOrder", "getTradeOrderById", "Lcom/sotraubo/app/core/model/TradeOrderWithPayment;", "observeAllCashTransactions", "Lkotlinx/coroutines/flow/Flow;", "observeAllContactsWithSummary", "Lcom/sotraubo/app/core/model/ContactWithSummary;", "observeAllTradeOrders", "observeContactWithSummary", "observeDashboardSummary", "Lcom/sotraubo/app/core/model/DashboardFinancialSummary;", "observePayableCreditors", "Lcom/sotraubo/app/core/model/PersonDebtSummaryItem;", "searchQuery", "sortOption", "Lcom/sotraubo/app/core/model/DebtSortOption;", "observePeriodBusinessStats", "Lcom/sotraubo/app/core/model/PeriodBusinessStats;", "periodType", "Lcom/sotraubo/app/core/common/format/ReportPeriodType;", "customStartMillis", "customEndMillis", "(Lcom/sotraubo/app/core/common/format/ReportPeriodType;Ljava/lang/Long;Ljava/lang/Long;)Lkotlinx/coroutines/flow/Flow;", "observePersonBidirectionalDebtDetail", "Lcom/sotraubo/app/core/model/PersonBidirectionalDebtDetail;", "observeReceivableDebtors", "observeRecentUnifiedActivities", "Lcom/sotraubo/app/core/model/UnifiedActivityItem;", "limit", "", "runInTransaction", "R", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveCashTransaction", "linkedTradeOrderId", "autoAllocateOldestDebt", "", "amount", "(JLcom/sotraubo/app/core/model/CashFlowType;Ljava/lang/Long;Ljava/lang/Long;ZJJLcom/sotraubo/app/core/model/PaymentMethod;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveContact", "Lcom/sotraubo/app/core/model/Contact;", "id", "fullName", "phone", "address", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveTradeOrder", "orderType", "Lcom/sotraubo/app/core/model/TradeOrderType;", "cattleQuantity", "description", "totalAmount", "initialPaidAmount", "initialPaymentMethod", "(JLcom/sotraubo/app/core/model/TradeOrderType;Ljava/lang/Long;JILjava/lang/String;JJLcom/sotraubo/app/core/model/PaymentMethod;Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toDomain", "Lcom/sotraubo/app/core/database/dao/CashTransactionQueryProjection;", "Lcom/sotraubo/app/core/database/dao/TradeOrderQueryProjection;", "Lcom/sotraubo/app/core/database/entity/ContactEntity;", "app_debug"})
public final class OfflineCattleBusinessRepository implements com.sotraubo.app.core.data.repository.CattleBusinessRepository {
    @org.jetbrains.annotations.Nullable()
    private final com.sotraubo.app.core.database.CattleBusinessDatabase database = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.core.database.dao.CattleBusinessDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.core.common.dispatcher.AppDispatchers dispatchers = null;
    
    public OfflineCattleBusinessRepository(@org.jetbrains.annotations.Nullable()
    com.sotraubo.app.core.database.CattleBusinessDatabase database, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.dao.CattleBusinessDao dao, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.common.dispatcher.AppDispatchers dispatchers) {
        super();
    }
    
    private final <R extends java.lang.Object>java.lang.Object runInTransaction(kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> block, kotlin.coroutines.Continuation<? super R> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment>> observeAllTradeOrders() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact>> observeAllCashTransactions() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.model.ContactWithSummary>> observeAllContactsWithSummary() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.sotraubo.app.core.model.ContactWithSummary> observeContactWithSummary(long contactId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.sotraubo.app.core.model.DashboardFinancialSummary> observeDashboardSummary() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.model.PersonDebtSummaryItem>> observeReceivableDebtors(@org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.DebtSortOption sortOption) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.model.PersonDebtSummaryItem>> observePayableCreditors(@org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.DebtSortOption sortOption) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.sotraubo.app.core.model.PersonBidirectionalDebtDetail> observePersonBidirectionalDebtDetail(long contactId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.model.UnifiedActivityItem>> observeRecentUnifiedActivities(int limit) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.sotraubo.app.core.model.PeriodBusinessStats> observePeriodBusinessStats(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.common.format.ReportPeriodType periodType, @org.jetbrains.annotations.Nullable()
    java.lang.Long customStartMillis, @org.jetbrains.annotations.Nullable()
    java.lang.Long customEndMillis) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object saveContact(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String fullName, @org.jetbrains.annotations.NotNull()
    java.lang.String phone, @org.jetbrains.annotations.NotNull()
    java.lang.String address, @org.jetbrains.annotations.NotNull()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<com.sotraubo.app.core.model.Contact>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteContact(long contactId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<kotlin.Unit>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object saveTradeOrder(long orderId, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.TradeOrderType orderType, @org.jetbrains.annotations.Nullable()
    java.lang.Long contactId, long transactionDate, int cattleQuantity, @org.jetbrains.annotations.NotNull()
    java.lang.String description, long totalAmount, long initialPaidAmount, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.PaymentMethod initialPaymentMethod, @org.jetbrains.annotations.NotNull()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> attachmentPaths, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<java.lang.Long>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteTradeOrder(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<kotlin.Unit>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object saveCashTransaction(long transactionId, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.CashFlowType flowType, @org.jetbrains.annotations.Nullable()
    java.lang.Long contactId, @org.jetbrains.annotations.Nullable()
    java.lang.Long linkedTradeOrderId, boolean autoAllocateOldestDebt, long amount, long transactionDate, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.PaymentMethod paymentMethod, @org.jetbrains.annotations.NotNull()
    java.lang.String shortContent, @org.jetbrains.annotations.NotNull()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> attachmentPaths, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<java.lang.Long>> $completion) {
        return null;
    }
    
    private final java.lang.Object allocatePaymentToOldestOrders(com.sotraubo.app.core.model.CashFlowType flowType, long contactId, long totalPaymentAmount, long transactionDate, com.sotraubo.app.core.model.PaymentMethod paymentMethod, java.lang.String shortContent, java.lang.String note, java.util.List<java.lang.String> attachmentPaths, long now, kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<java.lang.Long>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteCashTransaction(long transactionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<kotlin.Unit>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getTradeOrderById(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.model.TradeOrderWithPayment> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getInitialPaymentForOrder(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.model.CashTransactionWithContact> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getCashTransactionById(long transactionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.model.CashTransactionWithContact> $completion) {
        return null;
    }
    
    private final com.sotraubo.app.core.model.Contact toDomain(com.sotraubo.app.core.database.entity.ContactEntity $this$toDomain) {
        return null;
    }
    
    private final com.sotraubo.app.core.model.TradeOrderWithPayment toDomain(com.sotraubo.app.core.database.dao.TradeOrderQueryProjection $this$toDomain, java.util.List<java.lang.String> attachmentPaths) {
        return null;
    }
    
    private final com.sotraubo.app.core.model.CashTransactionWithContact toDomain(com.sotraubo.app.core.database.dao.CashTransactionQueryProjection $this$toDomain, java.util.List<java.lang.String> attachmentPaths) {
        return null;
    }
}