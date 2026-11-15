package com.sotraubo.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity
import com.sotraubo.app.core.database.entity.FamilyExpenseEntity
import com.sotraubo.app.core.database.entity.GoodsPurchaseEntity
import com.sotraubo.app.core.database.entity.QuotaFeeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LedgerDao {

    // ---- Hàng hóa mua ----
    @Query("SELECT * FROM goods_purchases ORDER BY purchase_date DESC, id DESC")
    fun observeGoods(): Flow<List<GoodsPurchaseEntity>>

    @Query("SELECT * FROM goods_purchases ORDER BY purchase_date DESC, id DESC")
    suspend fun getAllGoodsSnapshot(): List<GoodsPurchaseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGoods(entity: GoodsPurchaseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGoods(items: List<GoodsPurchaseEntity>)

    @Query("DELETE FROM goods_purchases WHERE id = :id")
    suspend fun deleteGoods(id: Long)

    @Query("DELETE FROM goods_purchases")
    suspend fun clearAllGoods()

    // ---- Đổi tiền ----
    @Query("SELECT * FROM currency_exchanges ORDER BY exchange_date DESC, id DESC")
    fun observeExchanges(): Flow<List<CurrencyExchangeEntity>>

    @Query("SELECT * FROM currency_exchanges ORDER BY exchange_date DESC, id DESC")
    suspend fun getAllExchangesSnapshot(): List<CurrencyExchangeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExchange(entity: CurrencyExchangeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExchanges(items: List<CurrencyExchangeEntity>)

    @Query("DELETE FROM currency_exchanges WHERE id = :id")
    suspend fun deleteExchange(id: Long)

    @Query("DELETE FROM currency_exchanges")
    suspend fun clearAllExchanges()

    // ---- Chi tiêu gia đình ----
    @Query("SELECT * FROM family_expenses ORDER BY expense_date DESC, id DESC")
    fun observeExpenses(): Flow<List<FamilyExpenseEntity>>

    @Query("SELECT * FROM family_expenses ORDER BY expense_date DESC, id DESC")
    suspend fun getAllExpensesSnapshot(): List<FamilyExpenseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExpense(entity: FamilyExpenseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExpenses(items: List<FamilyExpenseEntity>)

    @Query("DELETE FROM family_expenses WHERE id = :id")
    suspend fun deleteExpense(id: Long)

    @Query("DELETE FROM family_expenses")
    suspend fun clearAllExpenses()

    // --- Phí quota biên giới ---
    @Query("SELECT * FROM quota_fees ORDER BY fee_date DESC, created_at DESC")
    fun observeQuotaFees(): Flow<List<QuotaFeeEntity>>

    @Query("SELECT * FROM quota_fees ORDER BY fee_date DESC, created_at DESC")
    suspend fun getAllQuotaFeesOnce(): List<QuotaFeeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertQuotaFee(item: QuotaFeeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllQuotaFees(items: List<QuotaFeeEntity>)

    @Query("DELETE FROM quota_fees WHERE id = :id")
    suspend fun deleteQuotaFee(id: Long)

    @Query("DELETE FROM quota_fees")
    suspend fun clearAllQuotaFees()

    @Transaction
    suspend fun replaceAllLedgerDataInTransaction(
        goods: List<GoodsPurchaseEntity>,
        exchanges: List<CurrencyExchangeEntity>,
        expenses: List<FamilyExpenseEntity>,
        quotaFees: List<QuotaFeeEntity> = emptyList()
    ) {
        clearAllGoods()
        clearAllExchanges()
        clearAllExpenses()
        clearAllQuotaFees()
        if (goods.isNotEmpty()) insertAllGoods(goods)
        if (exchanges.isNotEmpty()) insertAllExchanges(exchanges)
        if (expenses.isNotEmpty()) insertAllExpenses(expenses)
        if (quotaFees.isNotEmpty()) insertAllQuotaFees(quotaFees)
    }
}
