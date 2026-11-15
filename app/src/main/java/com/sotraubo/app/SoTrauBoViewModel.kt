package com.sotraubo.app

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sotraubo.app.core.common.format.DateFormatter
import com.sotraubo.app.core.common.format.ReportPeriodType
import com.sotraubo.app.core.designsystem.component.TimeFilterMode
import com.sotraubo.app.feature.backupreport.BackupAndPdfManager
import com.sotraubo.app.feature.backupreport.BackupPreviewInfo
import com.sotraubo.app.feature.backupreport.PdfReportCategory
import com.sotraubo.app.core.data.repository.CattleBusinessRepository
import com.sotraubo.app.core.data.repository.OperationOutcome
import com.sotraubo.app.core.model.CashFlowType
import com.sotraubo.app.core.model.CashTransactionWithContact
import com.sotraubo.app.core.model.ContactWithSummary
import com.sotraubo.app.core.model.DashboardFinancialSummary
import com.sotraubo.app.core.model.DebtSortOption
import com.sotraubo.app.core.model.PaymentMethod
import com.sotraubo.app.core.model.PeriodBusinessStats
import com.sotraubo.app.core.model.PersonBidirectionalDebtDetail
import com.sotraubo.app.core.model.PersonDebtSummaryItem
import com.sotraubo.app.core.model.TradeOrderType
import com.sotraubo.app.core.model.TradeOrderWithPayment
import com.sotraubo.app.core.model.UnifiedActivityItem
import com.sotraubo.app.feature.security.PinSecurityStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SoTrauBoViewModel(
    private val repository: CattleBusinessRepository,
    private val backupAndPdfManager: BackupAndPdfManager,
    private val pinSecurityStorage: PinSecurityStorage
) : ViewModel() {

    private val _isUnlocked = MutableStateFlow(false)
    val isUnlocked: StateFlow<Boolean> = _isUnlocked.asStateFlow()

    private val _isPinConfigured = MutableStateFlow(pinSecurityStorage.isPinConfigured())
    val isPinConfigured: StateFlow<Boolean> = _isPinConfigured.asStateFlow()

    private val _isBiometricEnabled = MutableStateFlow(pinSecurityStorage.isBiometricEnabled())
    val isBiometricEnabled: StateFlow<Boolean> = _isBiometricEnabled.asStateFlow()

    private val _selectedPeriod = MutableStateFlow(ReportPeriodType.THIS_MONTH)
    val selectedPeriod: StateFlow<ReportPeriodType> = _selectedPeriod.asStateFlow()

    private val _debtSearchQuery = MutableStateFlow("")
    val debtSearchQuery: StateFlow<String> = _debtSearchQuery.asStateFlow()

    private val _debtSortOption = MutableStateFlow(DebtSortOption.AMOUNT_DESC)
    val debtSortOption: StateFlow<DebtSortOption> = _debtSortOption.asStateFlow()

    private val _formErrorMessage = MutableStateFlow<String?>(null)
    val formErrorMessage: StateFlow<String?> = _formErrorMessage.asStateFlow()

    private val _backupStatusMessage = MutableStateFlow<String?>(null)
    val backupStatusMessage: StateFlow<String?> = _backupStatusMessage.asStateFlow()

    private val _pendingRestoreUri = MutableStateFlow<Uri?>(null)
    private val _pendingRestorePreview = MutableStateFlow<BackupPreviewInfo?>(null)
    val pendingRestorePreview: StateFlow<BackupPreviewInfo?> = _pendingRestorePreview.asStateFlow()

    val dashboardSummary: StateFlow<DashboardFinancialSummary> = repository
        .observeDashboardSummary()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardFinancialSummary())

    val allContacts: StateFlow<List<ContactWithSummary>> = repository
        .observeAllContactsWithSummary()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val allTradeOrders: StateFlow<List<TradeOrderWithPayment>> = repository
        .observeAllTradeOrders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val allCashTransactions: StateFlow<List<CashTransactionWithContact>> = repository
        .observeAllCashTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val recentActivities: StateFlow<List<UnifiedActivityItem>> = repository
        .observeRecentUnifiedActivities(limit = 10)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val periodStats: StateFlow<PeriodBusinessStats> = _selectedPeriod
        .flatMapLatest { period -> repository.observePeriodBusinessStats(period) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PeriodBusinessStats())

    val receivableDebtors: StateFlow<List<PersonDebtSummaryItem>> = combine(
        _debtSearchQuery,
        _debtSortOption
    ) { query, sort -> query to sort }
        .flatMapLatest { (query, sort) -> repository.observeReceivableDebtors(query, sort) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val payableCreditors: StateFlow<List<PersonDebtSummaryItem>> = combine(
        _debtSearchQuery,
        _debtSortOption
    ) { query, sort -> query to sort }
        .flatMapLatest { (query, sort) -> repository.observePayableCreditors(query, sort) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun observePersonBidirectionalDebt(contactId: Long): StateFlow<PersonBidirectionalDebtDetail?> {
        return repository.observePersonBidirectionalDebtDetail(contactId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    }

    fun selectPeriod(periodType: ReportPeriodType) {
        _selectedPeriod.value = periodType
    }

    fun updateDebtSearchQuery(query: String) {
        _debtSearchQuery.value = query
    }

    fun updateDebtSortOption(option: DebtSortOption) {
        _debtSortOption.value = option
    }

    fun clearMessages() {
        _formErrorMessage.value = null
        _backupStatusMessage.value = null
    }

    fun setupNewPin(pin6Digits: String): Boolean {
        val ok = pinSecurityStorage.setupNewPin(pin6Digits)
        if (ok) {
            _isPinConfigured.value = true
            _isUnlocked.value = true
        }
        return ok
    }

    fun verifyPin(pin6Digits: String): Boolean {
        val ok = pinSecurityStorage.verifyPin(pin6Digits)
        if (ok) {
            _isUnlocked.value = true
        }
        return ok
    }

    fun changePin(oldPin6Digits: String, newPin6Digits: String): Result<Unit> {
        val res = pinSecurityStorage.changePin(oldPin6Digits, newPin6Digits)
        if (res.isSuccess) {
            _backupStatusMessage.value = "Đã đổi mật khẩu 6 số thành công!"
        }
        return res
    }

    fun lockAppNow() {
        _isUnlocked.value = false
    }

    fun markUnlocked() {
        _isUnlocked.value = true
    }

    fun setBiometricEnabled(enabled: Boolean) {
        pinSecurityStorage.setBiometricEnabled(enabled)
        _isBiometricEnabled.value = enabled
    }

    fun saveContact(
        id: Long = 0L,
        fullName: String,
        phone: String,
        address: String,
        note: String,
        onSuccess: (Long) -> Unit = {}
    ) {
        viewModelScope.launch {
            when (val res = repository.saveContact(id, fullName, phone, address, note)) {
                is OperationOutcome.Success -> {
                    _formErrorMessage.value = null
                    onSuccess(res.data.id)
                }
                is OperationOutcome.Error -> {
                    _formErrorMessage.value = res.vietnameseMessage
                }
            }
        }
    }

    fun deleteContact(contactId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            when (val res = repository.deleteContact(contactId)) {
                is OperationOutcome.Success -> {
                    _formErrorMessage.value = null
                    onSuccess()
                }
                is OperationOutcome.Error -> {
                    _formErrorMessage.value = res.vietnameseMessage
                }
            }
        }
    }

    fun saveTradeOrder(
        orderId: Long = 0L,
        orderType: TradeOrderType,
        contactId: Long?,
        transactionDate: Long,
        cattleQuantity: Int,
        description: String,
        totalAmount: Long,
        initialPaidAmount: Long,
        initialPaymentMethod: PaymentMethod,
        note: String,
        attachmentPaths: List<String>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val outcome = repository.saveTradeOrder(
                orderId = orderId,
                orderType = orderType,
                contactId = contactId,
                transactionDate = transactionDate,
                cattleQuantity = cattleQuantity,
                description = description,
                totalAmount = totalAmount,
                initialPaidAmount = initialPaidAmount,
                initialPaymentMethod = initialPaymentMethod,
                note = note,
                attachmentPaths = attachmentPaths
            )
            when (outcome) {
                is OperationOutcome.Success -> {
                    _formErrorMessage.value = null
                    onSuccess()
                }
                is OperationOutcome.Error -> {
                    _formErrorMessage.value = outcome.vietnameseMessage
                }
            }
        }
    }

    fun deleteTradeOrder(orderId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            when (val outcome = repository.deleteTradeOrder(orderId)) {
                is OperationOutcome.Success -> {
                    _formErrorMessage.value = null
                    onSuccess()
                }
                is OperationOutcome.Error -> {
                    _formErrorMessage.value = outcome.vietnameseMessage
                }
            }
        }
    }

    fun saveCashTransaction(
        transactionId: Long = 0L,
        flowType: CashFlowType,
        contactId: Long?,
        linkedTradeOrderId: Long?,
        autoAllocateOldestDebt: Boolean,
        amount: Long,
        transactionDate: Long,
        paymentMethod: PaymentMethod,
        shortContent: String,
        note: String,
        attachmentPaths: List<String>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val outcome = repository.saveCashTransaction(
                transactionId = transactionId,
                flowType = flowType,
                contactId = contactId,
                linkedTradeOrderId = linkedTradeOrderId,
                autoAllocateOldestDebt = autoAllocateOldestDebt,
                amount = amount,
                transactionDate = transactionDate,
                paymentMethod = paymentMethod,
                shortContent = shortContent,
                note = note,
                attachmentPaths = attachmentPaths
            )
            when (outcome) {
                is OperationOutcome.Success -> {
                    _formErrorMessage.value = null
                    onSuccess()
                }
                is OperationOutcome.Error -> {
                    _formErrorMessage.value = outcome.vietnameseMessage
                }
            }
        }
    }

    fun deleteCashTransaction(transactionId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            when (val outcome = repository.deleteCashTransaction(transactionId)) {
                is OperationOutcome.Success -> {
                    _formErrorMessage.value = null
                    onSuccess()
                }
                is OperationOutcome.Error -> {
                    _formErrorMessage.value = outcome.vietnameseMessage
                }
            }
        }
    }

    fun exportBackup(context: Context, uri: Uri) {
        viewModelScope.launch {
            when (val outcome = backupAndPdfManager.exportBackupToUri(context, uri)) {
                is OperationOutcome.Success -> {
                    _formErrorMessage.value = null
                    _backupStatusMessage.value = outcome.data
                }
                is OperationOutcome.Error -> {
                    _formErrorMessage.value = outcome.vietnameseMessage
                }
            }
        }
    }

    fun validateBackupForRestore(context: Context, uri: Uri) {
        viewModelScope.launch {
            when (val outcome = backupAndPdfManager.validateBackupUri(context, uri)) {
                is OperationOutcome.Success -> {
                    _formErrorMessage.value = null
                    _pendingRestoreUri.value = uri
                    _pendingRestorePreview.value = outcome.data
                }
                is OperationOutcome.Error -> {
                    _pendingRestoreUri.value = null
                    _pendingRestorePreview.value = null
                    _formErrorMessage.value = outcome.vietnameseMessage
                }
            }
        }
    }

    fun confirmRestoreBackup(context: Context) {
        val uri = _pendingRestoreUri.value ?: return
        val preview = _pendingRestorePreview.value ?: return
        viewModelScope.launch {
            when (val outcome = backupAndPdfManager.confirmRestoreBackup(context, uri, preview)) {
                is OperationOutcome.Success -> {
                    _pendingRestoreUri.value = null
                    _pendingRestorePreview.value = null
                    _formErrorMessage.value = null
                    _backupStatusMessage.value = outcome.data
                }
                is OperationOutcome.Error -> {
                    _pendingRestoreUri.value = null
                    _pendingRestorePreview.value = null
                    _formErrorMessage.value = outcome.vietnameseMessage
                }
            }
        }
    }

    fun cancelRestoreBackup() {
        _pendingRestoreUri.value = null
        _pendingRestorePreview.value = null
    }

    fun clearAllDataWithPin(
        context: Context,
        pin6Digits: String,
        onCleared: () -> Unit = {}
    ): Boolean {
        if (!pinSecurityStorage.verifyPin(pin6Digits)) {
            return false
        }
        viewModelScope.launch {
            when (val outcome = backupAndPdfManager.clearAllDemoData(context)) {
                is OperationOutcome.Success -> {
                    _formErrorMessage.value = null
                    _backupStatusMessage.value = outcome.data
                    onCleared()
                }
                is OperationOutcome.Error -> {
                    _formErrorMessage.value = outcome.vietnameseMessage
                }
            }
        }
        return true
    }

    fun exportOverviewReportPdf(
        context: Context,
        uri: Uri,
        category: PdfReportCategory = PdfReportCategory.ALL,
        timeFilterMode: TimeFilterMode = TimeFilterMode.ALL,
        anchorMillis: Long = DateFormatter.todayNoonMillis()
    ) {
        viewModelScope.launch {
            val outcome = backupAndPdfManager.exportOverviewReportPdf(
                context = context,
                destinationUri = uri,
                summary = dashboardSummary.value,
                periodStats = periodStats.value,
                receivableDebtors = receivableDebtors.value,
                payableCreditors = payableCreditors.value,
                category = category,
                timeFilterMode = timeFilterMode,
                anchorMillis = anchorMillis
            )
            when (outcome) {
                is OperationOutcome.Success -> {
                    _formErrorMessage.value = null
                    _backupStatusMessage.value = outcome.data
                }
                is OperationOutcome.Error -> {
                    _formErrorMessage.value = outcome.vietnameseMessage
                }
            }
        }
    }

    fun exportPersonDebtPdf(context: Context, detail: PersonBidirectionalDebtDetail) {
        viewModelScope.launch {
            when (val outcome = backupAndPdfManager.exportAndSharePersonDebtPdf(context, detail)) {
                is OperationOutcome.Success -> {
                    _backupStatusMessage.value = outcome.data
                }
                is OperationOutcome.Error -> {
                    _formErrorMessage.value = outcome.vietnameseMessage
                }
            }
        }
    }

    companion object {
        fun provideFactory(container: AppServiceContainer): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SoTrauBoViewModel(
                        repository = container.repository,
                        backupAndPdfManager = container.backupAndPdfManager,
                        pinSecurityStorage = container.pinSecurityManager
                    ) as T
                }
            }
    }
}
