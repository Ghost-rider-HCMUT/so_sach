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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00a0\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u00a6@\u00a2\u0006\u0002\u0010\u0007J\u001c\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\t\u001a\u00020\u0006H\u00a6@\u00a2\u0006\u0002\u0010\u0007J\u001c\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u000b\u001a\u00020\u0006H\u00a6@\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0005\u001a\u00020\u0006H\u00a6@\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000b\u001a\u00020\u0006H\u00a6@\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u000b\u001a\u00020\u0006H\u00a6@\u00a2\u0006\u0002\u0010\u0007J\u0014\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00130\u0012H&J\u0014\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u00130\u0012H&J\u0014\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u00130\u0012H&J\u0018\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u00122\u0006\u0010\t\u001a\u00020\u0006H&J\u000e\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u0012H&J$\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\u00130\u00122\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH&J3\u0010 \u001a\b\u0012\u0004\u0012\u00020!0\u00122\u0006\u0010\"\u001a\u00020#2\n\b\u0002\u0010$\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010%\u001a\u0004\u0018\u00010\u0006H&\u00a2\u0006\u0002\u0010&J\u0018\u0010\'\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010(0\u00122\u0006\u0010\t\u001a\u00020\u0006H&J$\u0010)\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\u00130\u00122\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH&J\u001e\u0010*\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020+0\u00130\u00122\b\b\u0002\u0010,\u001a\u00020-H&J|\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00060\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\u0006\u0010/\u001a\u0002002\b\u0010\t\u001a\u0004\u0018\u00010\u00062\b\u00101\u001a\u0004\u0018\u00010\u00062\b\b\u0002\u00102\u001a\u0002032\u0006\u00104\u001a\u00020\u00062\u0006\u00105\u001a\u00020\u00062\u0006\u00106\u001a\u0002072\u0006\u00108\u001a\u00020\u001d2\u0006\u00109\u001a\u00020\u001d2\u000e\b\u0002\u0010:\u001a\b\u0012\u0004\u0012\u00020\u001d0\u0013H\u00a6@\u00a2\u0006\u0002\u0010;J>\u0010<\u001a\b\u0012\u0004\u0012\u00020=0\u00032\b\b\u0002\u0010>\u001a\u00020\u00062\u0006\u0010?\u001a\u00020\u001d2\u0006\u0010@\u001a\u00020\u001d2\u0006\u0010A\u001a\u00020\u001d2\u0006\u00109\u001a\u00020\u001dH\u00a6@\u00a2\u0006\u0002\u0010BJx\u0010C\u001a\b\u0012\u0004\u0012\u00020\u00060\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00062\u0006\u0010D\u001a\u00020E2\b\u0010\t\u001a\u0004\u0018\u00010\u00062\u0006\u00105\u001a\u00020\u00062\u0006\u0010F\u001a\u00020-2\u0006\u0010G\u001a\u00020\u001d2\u0006\u0010H\u001a\u00020\u00062\u0006\u0010I\u001a\u00020\u00062\u0006\u0010J\u001a\u0002072\u0006\u00109\u001a\u00020\u001d2\u000e\b\u0002\u0010:\u001a\b\u0012\u0004\u0012\u00020\u001d0\u0013H\u00a6@\u00a2\u0006\u0002\u0010K\u00a8\u0006L"}, d2 = {"Lcom/sotraubo/app/core/data/repository/CattleBusinessRepository;", "", "deleteCashTransaction", "Lcom/sotraubo/app/core/data/repository/OperationOutcome;", "", "transactionId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteContact", "contactId", "deleteTradeOrder", "orderId", "getCashTransactionById", "Lcom/sotraubo/app/core/model/CashTransactionWithContact;", "getInitialPaymentForOrder", "getTradeOrderById", "Lcom/sotraubo/app/core/model/TradeOrderWithPayment;", "observeAllCashTransactions", "Lkotlinx/coroutines/flow/Flow;", "", "observeAllContactsWithSummary", "Lcom/sotraubo/app/core/model/ContactWithSummary;", "observeAllTradeOrders", "observeContactWithSummary", "observeDashboardSummary", "Lcom/sotraubo/app/core/model/DashboardFinancialSummary;", "observePayableCreditors", "Lcom/sotraubo/app/core/model/PersonDebtSummaryItem;", "searchQuery", "", "sortOption", "Lcom/sotraubo/app/core/model/DebtSortOption;", "observePeriodBusinessStats", "Lcom/sotraubo/app/core/model/PeriodBusinessStats;", "periodType", "Lcom/sotraubo/app/core/common/format/ReportPeriodType;", "customStartMillis", "customEndMillis", "(Lcom/sotraubo/app/core/common/format/ReportPeriodType;Ljava/lang/Long;Ljava/lang/Long;)Lkotlinx/coroutines/flow/Flow;", "observePersonBidirectionalDebtDetail", "Lcom/sotraubo/app/core/model/PersonBidirectionalDebtDetail;", "observeReceivableDebtors", "observeRecentUnifiedActivities", "Lcom/sotraubo/app/core/model/UnifiedActivityItem;", "limit", "", "saveCashTransaction", "flowType", "Lcom/sotraubo/app/core/model/CashFlowType;", "linkedTradeOrderId", "autoAllocateOldestDebt", "", "amount", "transactionDate", "paymentMethod", "Lcom/sotraubo/app/core/model/PaymentMethod;", "shortContent", "note", "attachmentPaths", "(JLcom/sotraubo/app/core/model/CashFlowType;Ljava/lang/Long;Ljava/lang/Long;ZJJLcom/sotraubo/app/core/model/PaymentMethod;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveContact", "Lcom/sotraubo/app/core/model/Contact;", "id", "fullName", "phone", "address", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveTradeOrder", "orderType", "Lcom/sotraubo/app/core/model/TradeOrderType;", "cattleQuantity", "description", "totalAmount", "initialPaidAmount", "initialPaymentMethod", "(JLcom/sotraubo/app/core/model/TradeOrderType;Ljava/lang/Long;JILjava/lang/String;JJLcom/sotraubo/app/core/model/PaymentMethod;Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface CattleBusinessRepository {
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.model.ContactWithSummary>> observeAllContactsWithSummary();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment>> observeAllTradeOrders();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact>> observeAllCashTransactions();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.sotraubo.app.core.model.DashboardFinancialSummary> observeDashboardSummary();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.model.PersonDebtSummaryItem>> observeReceivableDebtors(@org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.DebtSortOption sortOption);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.model.PersonDebtSummaryItem>> observePayableCreditors(@org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.DebtSortOption sortOption);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.sotraubo.app.core.model.PersonBidirectionalDebtDetail> observePersonBidirectionalDebtDetail(long contactId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.sotraubo.app.core.model.ContactWithSummary> observeContactWithSummary(long contactId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.model.UnifiedActivityItem>> observeRecentUnifiedActivities(int limit);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.sotraubo.app.core.model.PeriodBusinessStats> observePeriodBusinessStats(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.common.format.ReportPeriodType periodType, @org.jetbrains.annotations.Nullable()
    java.lang.Long customStartMillis, @org.jetbrains.annotations.Nullable()
    java.lang.Long customEndMillis);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveContact(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String fullName, @org.jetbrains.annotations.NotNull()
    java.lang.String phone, @org.jetbrains.annotations.NotNull()
    java.lang.String address, @org.jetbrains.annotations.NotNull()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<com.sotraubo.app.core.model.Contact>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteContact(long contactId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<kotlin.Unit>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveTradeOrder(long orderId, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.TradeOrderType orderType, @org.jetbrains.annotations.Nullable()
    java.lang.Long contactId, long transactionDate, int cattleQuantity, @org.jetbrains.annotations.NotNull()
    java.lang.String description, long totalAmount, long initialPaidAmount, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.PaymentMethod initialPaymentMethod, @org.jetbrains.annotations.NotNull()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> attachmentPaths, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<java.lang.Long>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteTradeOrder(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<kotlin.Unit>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveCashTransaction(long transactionId, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.CashFlowType flowType, @org.jetbrains.annotations.Nullable()
    java.lang.Long contactId, @org.jetbrains.annotations.Nullable()
    java.lang.Long linkedTradeOrderId, boolean autoAllocateOldestDebt, long amount, long transactionDate, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.PaymentMethod paymentMethod, @org.jetbrains.annotations.NotNull()
    java.lang.String shortContent, @org.jetbrains.annotations.NotNull()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> attachmentPaths, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<java.lang.Long>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteCashTransaction(long transactionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<kotlin.Unit>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTradeOrderById(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.model.TradeOrderWithPayment> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getInitialPaymentForOrder(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.model.CashTransactionWithContact> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCashTransactionById(long transactionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.model.CashTransactionWithContact> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}