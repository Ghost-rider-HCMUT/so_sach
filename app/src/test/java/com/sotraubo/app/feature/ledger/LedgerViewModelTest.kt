package com.sotraubo.app.feature.ledger

import com.sotraubo.app.core.database.dao.LedgerDao
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity
import com.sotraubo.app.core.database.entity.FamilyExpenseEntity
import com.sotraubo.app.core.database.entity.GoodsPurchaseEntity
import com.sotraubo.app.core.database.entity.QuotaFeeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LedgerViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeDao: FakeLedgerDao
    private lateinit var viewModel: LedgerViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeLedgerDao()
        viewModel = LedgerViewModel(fakeDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `save and delete goods, exchanges, and family expenses update state flows`() = runTest {
        val goodsJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.goods.collect() }
        val exJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.exchanges.collect() }
        val expJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.expenses.collect() }
        val quotaJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.quotaFees.collect() }

        viewModel.saveGoods(
            GoodsPurchaseEntity(
                purchaseDate = 1_700_000_000_000L,
                itemName = "Cám bò",
                quantityX100 = 250L,
                unit = "kg",
                totalAmount = 500_000L,
                supplier = "Đại lý Minh Phát",
                contactId = 9L
            )
        )
        viewModel.saveExchange(
            CurrencyExchangeEntity(
                exchangeDate = 1_700_000_000_000L,
                direction = "VND_TO_THB",
                bahtAmount = 10_000L,
                rate = 720L,
                vndAmount = 7_200_000L,
                partnerName = "Anh Tuấn",
                contactId = 42L
            )
        )
        viewModel.saveExpense(
            FamilyExpenseEntity(
                expenseDate = 1_700_000_000_000L,
                category = "",
                amount = 250_000L,
                partnerName = "Chị Lan",
                contactId = 15L,
                note = "Tiền chợ"
            )
        )
        viewModel.saveQuotaFee(
            QuotaFeeEntity(
                feeDate = 1_700_000_000_000L,
                payerName = "Anh Hùng",
                contactId = 7L,
                cattleQuantity = 12,
                amount = 6_000_000L,
                note = "Cửa khẩu"
            )
        )
        advanceUntilIdle()

        assertEquals(1, viewModel.goods.value.size)
        assertEquals("Cám bò", viewModel.goods.value.first().itemName)
        assertEquals(9L, viewModel.goods.value.first().contactId)
        assertEquals(1, viewModel.exchanges.value.size)
        assertEquals(7_200_000L, viewModel.exchanges.value.first().vndAmount)
        assertEquals("Anh Tuấn", viewModel.exchanges.value.first().partnerName)
        assertEquals(42L, viewModel.exchanges.value.first().contactId)
        assertEquals(1, viewModel.expenses.value.size)
        assertEquals("Tiền chợ", viewModel.expenses.value.first().note)
        assertEquals("Chị Lan", viewModel.expenses.value.first().partnerName)
        assertEquals(15L, viewModel.expenses.value.first().contactId)
        assertEquals(1, viewModel.quotaFees.value.size)
        assertEquals("Anh Hùng", viewModel.quotaFees.value.first().payerName)
        assertEquals(12, viewModel.quotaFees.value.first().cattleQuantity)
        assertEquals(6_000_000L, viewModel.quotaFees.value.first().amount)

        val savedGoodsId = viewModel.goods.value.first().id
        val savedExId = viewModel.exchanges.value.first().id
        val savedExpId = viewModel.expenses.value.first().id
        val savedQuotaId = viewModel.quotaFees.value.first().id

        viewModel.deleteGoods(savedGoodsId)
        viewModel.deleteExchange(savedExId)
        viewModel.deleteExpense(savedExpId)
        viewModel.deleteQuotaFee(savedQuotaId)
        advanceUntilIdle()

        assertTrue(viewModel.goods.value.isEmpty())
        assertTrue(viewModel.exchanges.value.isEmpty())
        assertTrue(viewModel.expenses.value.isEmpty())
        assertTrue(viewModel.quotaFees.value.isEmpty())

        goodsJob.cancel()
        exJob.cancel()
        expJob.cancel()
        quotaJob.cancel()
    }
}

private class FakeLedgerDao : LedgerDao {
    private var nextId = 1L
    private val goodsFlow = MutableStateFlow<List<GoodsPurchaseEntity>>(emptyList())
    private val exchangesFlow = MutableStateFlow<List<CurrencyExchangeEntity>>(emptyList())
    private val expensesFlow = MutableStateFlow<List<FamilyExpenseEntity>>(emptyList())
    private val quotaFeesFlow = MutableStateFlow<List<QuotaFeeEntity>>(emptyList())

    override fun observeGoods(): Flow<List<GoodsPurchaseEntity>> = goodsFlow
    override suspend fun getAllGoodsSnapshot(): List<GoodsPurchaseEntity> = goodsFlow.value
    override suspend fun upsertGoods(entity: GoodsPurchaseEntity): Long {
        val id = if (entity.id == 0L) nextId++ else entity.id
        val saved = entity.copy(id = id)
        goodsFlow.value = goodsFlow.value.filterNot { it.id == id } + saved
        return id
    }
    override suspend fun insertAllGoods(items: List<GoodsPurchaseEntity>) { goodsFlow.value = items }
    override suspend fun deleteGoods(id: Long) { goodsFlow.value = goodsFlow.value.filterNot { it.id == id } }
    override suspend fun clearAllGoods() { goodsFlow.value = emptyList() }

    override fun observeExchanges(): Flow<List<CurrencyExchangeEntity>> = exchangesFlow
    override suspend fun getAllExchangesSnapshot(): List<CurrencyExchangeEntity> = exchangesFlow.value
    override suspend fun upsertExchange(entity: CurrencyExchangeEntity): Long {
        val id = if (entity.id == 0L) nextId++ else entity.id
        val saved = entity.copy(id = id)
        exchangesFlow.value = exchangesFlow.value.filterNot { it.id == id } + saved
        return id
    }
    override suspend fun insertAllExchanges(items: List<CurrencyExchangeEntity>) { exchangesFlow.value = items }
    override suspend fun deleteExchange(id: Long) { exchangesFlow.value = exchangesFlow.value.filterNot { it.id == id } }
    override suspend fun clearAllExchanges() { exchangesFlow.value = emptyList() }

    override fun observeExpenses(): Flow<List<FamilyExpenseEntity>> = expensesFlow
    override suspend fun getAllExpensesSnapshot(): List<FamilyExpenseEntity> = expensesFlow.value
    override suspend fun upsertExpense(entity: FamilyExpenseEntity): Long {
        val id = if (entity.id == 0L) nextId++ else entity.id
        val saved = entity.copy(id = id)
        expensesFlow.value = expensesFlow.value.filterNot { it.id == id } + saved
        return id
    }
    override suspend fun insertAllExpenses(items: List<FamilyExpenseEntity>) { expensesFlow.value = items }
    override suspend fun deleteExpense(id: Long) { expensesFlow.value = expensesFlow.value.filterNot { it.id == id } }
    override suspend fun clearAllExpenses() { expensesFlow.value = emptyList() }

    override fun observeQuotaFees(): Flow<List<QuotaFeeEntity>> = quotaFeesFlow
    override suspend fun getAllQuotaFeesOnce(): List<QuotaFeeEntity> = quotaFeesFlow.value
    override suspend fun upsertQuotaFee(item: QuotaFeeEntity): Long {
        val id = if (item.id == 0L) nextId++ else item.id
        val saved = item.copy(id = id)
        quotaFeesFlow.value = quotaFeesFlow.value.filterNot { it.id == id } + saved
        return id
    }
    override suspend fun insertAllQuotaFees(items: List<QuotaFeeEntity>) { quotaFeesFlow.value = items }
    override suspend fun deleteQuotaFee(id: Long) { quotaFeesFlow.value = quotaFeesFlow.value.filterNot { it.id == id } }
    override suspend fun clearAllQuotaFees() { quotaFeesFlow.value = emptyList() }
}
