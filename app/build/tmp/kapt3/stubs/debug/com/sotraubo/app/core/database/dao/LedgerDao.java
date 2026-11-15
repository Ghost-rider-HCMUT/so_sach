package com.sotraubo.app.core.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity;
import com.sotraubo.app.core.database.entity.FamilyExpenseEntity;
import com.sotraubo.app.core.database.entity.GoodsPurchaseEntity;
import com.sotraubo.app.core.database.entity.QuotaFeeEntity;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0014\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0006\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0007\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u0016\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u0016\u0010\r\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u0016\u0010\u000e\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\u0018\u001a\u00020\u00032\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u001aJ\u001c\u0010\u001b\u001a\u00020\u00032\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00130\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u001aJ\u001c\u0010\u001c\u001a\u00020\u00032\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00150\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u001aJ\u001c\u0010\u001d\u001a\u00020\u00032\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00170\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u001aJ\u0014\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00100\u001fH\'J\u0014\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00100\u001fH\'J\u0014\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u00100\u001fH\'J\u0014\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00100\u001fH\'JH\u0010#\u001a\u00020\u00032\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00150\u00102\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00110\u00102\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00130\u00102\u000e\b\u0002\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00170\u0010H\u0097@\u00a2\u0006\u0002\u0010(J\u0016\u0010)\u001a\u00020\n2\u0006\u0010*\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010+J\u0016\u0010,\u001a\u00020\n2\u0006\u0010*\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010-J\u0016\u0010.\u001a\u00020\n2\u0006\u0010*\u001a\u00020\u0015H\u00a7@\u00a2\u0006\u0002\u0010/J\u0016\u00100\u001a\u00020\n2\u0006\u00101\u001a\u00020\u0017H\u00a7@\u00a2\u0006\u0002\u00102\u00a8\u00063"}, d2 = {"Lcom/sotraubo/app/core/database/dao/LedgerDao;", "", "clearAllExchanges", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearAllExpenses", "clearAllGoods", "clearAllQuotaFees", "deleteExchange", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteExpense", "deleteGoods", "deleteQuotaFee", "getAllExchangesSnapshot", "", "Lcom/sotraubo/app/core/database/entity/CurrencyExchangeEntity;", "getAllExpensesSnapshot", "Lcom/sotraubo/app/core/database/entity/FamilyExpenseEntity;", "getAllGoodsSnapshot", "Lcom/sotraubo/app/core/database/entity/GoodsPurchaseEntity;", "getAllQuotaFeesOnce", "Lcom/sotraubo/app/core/database/entity/QuotaFeeEntity;", "insertAllExchanges", "items", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertAllExpenses", "insertAllGoods", "insertAllQuotaFees", "observeExchanges", "Lkotlinx/coroutines/flow/Flow;", "observeExpenses", "observeGoods", "observeQuotaFees", "replaceAllLedgerDataInTransaction", "goods", "exchanges", "expenses", "quotaFees", "(Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertExchange", "entity", "(Lcom/sotraubo/app/core/database/entity/CurrencyExchangeEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertExpense", "(Lcom/sotraubo/app/core/database/entity/FamilyExpenseEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertGoods", "(Lcom/sotraubo/app/core/database/entity/GoodsPurchaseEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertQuotaFee", "item", "(Lcom/sotraubo/app/core/database/entity/QuotaFeeEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface LedgerDao {
    
    @androidx.room.Query(value = "SELECT * FROM goods_purchases ORDER BY purchase_date DESC, id DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity>> observeGoods();
    
    @androidx.room.Query(value = "SELECT * FROM goods_purchases ORDER BY purchase_date DESC, id DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllGoodsSnapshot(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertGoods(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.GoodsPurchaseEntity entity, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAllGoods(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity> items, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM goods_purchases WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteGoods(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM goods_purchases")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAllGoods(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM currency_exchanges ORDER BY exchange_date DESC, id DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity>> observeExchanges();
    
    @androidx.room.Query(value = "SELECT * FROM currency_exchanges ORDER BY exchange_date DESC, id DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllExchangesSnapshot(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertExchange(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.CurrencyExchangeEntity entity, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAllExchanges(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity> items, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM currency_exchanges WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteExchange(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM currency_exchanges")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAllExchanges(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM family_expenses ORDER BY expense_date DESC, id DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity>> observeExpenses();
    
    @androidx.room.Query(value = "SELECT * FROM family_expenses ORDER BY expense_date DESC, id DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllExpensesSnapshot(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertExpense(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.FamilyExpenseEntity entity, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAllExpenses(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity> items, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM family_expenses WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteExpense(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM family_expenses")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAllExpenses(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM quota_fees ORDER BY fee_date DESC, created_at DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity>> observeQuotaFees();
    
    @androidx.room.Query(value = "SELECT * FROM quota_fees ORDER BY fee_date DESC, created_at DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllQuotaFeesOnce(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertQuotaFee(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.QuotaFeeEntity item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAllQuotaFees(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity> items, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM quota_fees WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteQuotaFee(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM quota_fees")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAllQuotaFees(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Transaction()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object replaceAllLedgerDataInTransaction(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity> goods, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity> exchanges, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity> expenses, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity> quotaFees, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
        
        @androidx.room.Transaction()
        @org.jetbrains.annotations.Nullable()
        public static java.lang.Object replaceAllLedgerDataInTransaction(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.core.database.dao.LedgerDao $this, @org.jetbrains.annotations.NotNull()
        java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity> goods, @org.jetbrains.annotations.NotNull()
        java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity> exchanges, @org.jetbrains.annotations.NotNull()
        java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity> expenses, @org.jetbrains.annotations.NotNull()
        java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity> quotaFees, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
            return null;
        }
    }
}