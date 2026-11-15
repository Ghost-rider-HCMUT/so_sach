package com.sotraubo.app.feature.ledger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sotraubo.app.core.database.dao.LedgerDao
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity
import com.sotraubo.app.core.database.entity.FamilyExpenseEntity
import com.sotraubo.app.core.database.entity.GoodsPurchaseEntity
import com.sotraubo.app.core.database.entity.QuotaFeeEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Quản lý 4 sổ phụ: Hàng hóa mua, Đổi tiền (Bath), Chi tiêu gia đình, Phí Quota biên giới. */
class LedgerViewModel(private val dao: LedgerDao) : ViewModel() {

    val goods: StateFlow<List<GoodsPurchaseEntity>> = dao.observeGoods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val exchanges: StateFlow<List<CurrencyExchangeEntity>> = dao.observeExchanges()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val expenses: StateFlow<List<FamilyExpenseEntity>> = dao.observeExpenses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val quotaFees: StateFlow<List<QuotaFeeEntity>> = dao.observeQuotaFees()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun saveGoods(entity: GoodsPurchaseEntity) {
        viewModelScope.launch { dao.upsertGoods(entity) }
    }

    fun deleteGoods(id: Long) {
        viewModelScope.launch { dao.deleteGoods(id) }
    }

    fun saveExchange(entity: CurrencyExchangeEntity) {
        viewModelScope.launch { dao.upsertExchange(entity) }
    }

    fun deleteExchange(id: Long) {
        viewModelScope.launch { dao.deleteExchange(id) }
    }

    fun saveExpense(entity: FamilyExpenseEntity) {
        viewModelScope.launch { dao.upsertExpense(entity) }
    }

    fun deleteExpense(id: Long) {
        viewModelScope.launch { dao.deleteExpense(id) }
    }

    fun saveQuotaFee(entity: QuotaFeeEntity) {
        viewModelScope.launch { dao.upsertQuotaFee(entity) }
    }

    fun deleteQuotaFee(id: Long) {
        viewModelScope.launch { dao.deleteQuotaFee(id) }
    }

    companion object {
        fun provideFactory(dao: LedgerDao): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    LedgerViewModel(dao) as T
            }
    }
}
