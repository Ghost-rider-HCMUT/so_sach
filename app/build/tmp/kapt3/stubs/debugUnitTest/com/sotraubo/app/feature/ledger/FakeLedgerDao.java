package com.sotraubo.app.feature.ledger;

import com.sotraubo.app.core.database.dao.LedgerDao;
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity;
import com.sotraubo.app.core.database.entity.FamilyExpenseEntity;
import com.sotraubo.app.core.database.entity.GoodsPurchaseEntity;
import com.sotraubo.app.core.database.entity.QuotaFeeEntity;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.flow.Flow;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\b\u000e\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000f\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u0012\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u0013\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u0014\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0018\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u0017J\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0096@\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\b0\u0005H\u0096@\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\n0\u0005H\u0096@\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0005H\u0096@\u00a2\u0006\u0002\u0010\u0011J\u001c\u0010\u001f\u001a\u00020\u00102\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0096@\u00a2\u0006\u0002\u0010!J\u001c\u0010\"\u001a\u00020\u00102\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\b0\u0005H\u0096@\u00a2\u0006\u0002\u0010!J\u001c\u0010#\u001a\u00020\u00102\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\n0\u0005H\u0096@\u00a2\u0006\u0002\u0010!J\u001c\u0010$\u001a\u00020\u00102\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u000e0\u0005H\u0096@\u00a2\u0006\u0002\u0010!J\u0014\u0010%\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050&H\u0016J\u0014\u0010\'\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00050&H\u0016J\u0014\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00050&H\u0016J\u0014\u0010)\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00050&H\u0016J\u0016\u0010*\u001a\u00020\f2\u0006\u0010+\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010,J\u0016\u0010-\u001a\u00020\f2\u0006\u0010+\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010.J\u0016\u0010/\u001a\u00020\f2\u0006\u0010+\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u00100J\u0016\u00101\u001a\u00020\f2\u0006\u00102\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u00103R\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00064"}, d2 = {"Lcom/sotraubo/app/feature/ledger/FakeLedgerDao;", "Lcom/sotraubo/app/core/database/dao/LedgerDao;", "()V", "exchangesFlow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/sotraubo/app/core/database/entity/CurrencyExchangeEntity;", "expensesFlow", "Lcom/sotraubo/app/core/database/entity/FamilyExpenseEntity;", "goodsFlow", "Lcom/sotraubo/app/core/database/entity/GoodsPurchaseEntity;", "nextId", "", "quotaFeesFlow", "Lcom/sotraubo/app/core/database/entity/QuotaFeeEntity;", "clearAllExchanges", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearAllExpenses", "clearAllGoods", "clearAllQuotaFees", "deleteExchange", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteExpense", "deleteGoods", "deleteQuotaFee", "getAllExchangesSnapshot", "getAllExpensesSnapshot", "getAllGoodsSnapshot", "getAllQuotaFeesOnce", "insertAllExchanges", "items", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertAllExpenses", "insertAllGoods", "insertAllQuotaFees", "observeExchanges", "Lkotlinx/coroutines/flow/Flow;", "observeExpenses", "observeGoods", "observeQuotaFees", "upsertExchange", "entity", "(Lcom/sotraubo/app/core/database/entity/CurrencyExchangeEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertExpense", "(Lcom/sotraubo/app/core/database/entity/FamilyExpenseEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertGoods", "(Lcom/sotraubo/app/core/database/entity/GoodsPurchaseEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertQuotaFee", "item", "(Lcom/sotraubo/app/core/database/entity/QuotaFeeEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debugUnitTest"})
final class FakeLedgerDao implements com.sotraubo.app.core.database.dao.LedgerDao {
    private long nextId = 1L;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity>> goodsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity>> exchangesFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity>> expensesFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity>> quotaFeesFlow = null;
    
    public FakeLedgerDao() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity>> observeGoods() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getAllGoodsSnapshot(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object upsertGoods(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.GoodsPurchaseEntity entity, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object insertAllGoods(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity> items, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteGoods(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object clearAllGoods(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity>> observeExchanges() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getAllExchangesSnapshot(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object upsertExchange(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.CurrencyExchangeEntity entity, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object insertAllExchanges(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity> items, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteExchange(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object clearAllExchanges(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity>> observeExpenses() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getAllExpensesSnapshot(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object upsertExpense(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.FamilyExpenseEntity entity, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object insertAllExpenses(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity> items, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteExpense(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object clearAllExpenses(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity>> observeQuotaFees() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getAllQuotaFeesOnce(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object upsertQuotaFee(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.QuotaFeeEntity item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object insertAllQuotaFees(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity> items, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteQuotaFee(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object clearAllQuotaFees(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @androidx.room.Transaction()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object replaceAllLedgerDataInTransaction(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity> goods, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity> exchanges, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity> expenses, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity> quotaFees, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}