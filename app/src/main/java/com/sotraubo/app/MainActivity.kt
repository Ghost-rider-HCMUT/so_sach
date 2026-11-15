package com.sotraubo.app

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import android.view.ViewTreeObserver
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sotraubo.app.core.designsystem.theme.BlueContainer
import com.sotraubo.app.core.designsystem.theme.BluePrimary
import com.sotraubo.app.core.designsystem.theme.SoTrauBoTheme
import com.sotraubo.app.core.model.CashFlowType
import com.sotraubo.app.core.model.PaymentMethod
import com.sotraubo.app.core.model.TradeOrderType
import com.sotraubo.app.feature.backupreport.BackupAndReportScreen
import com.sotraubo.app.feature.cashflow.CashTransactionFormScreen
import com.sotraubo.app.feature.cashflow.CashTransactionListScreen
import com.sotraubo.app.feature.contact.ContactDetailScreen
import com.sotraubo.app.feature.contact.ContactListScreen
import com.sotraubo.app.feature.dashboard.DashboardScreen
import com.sotraubo.app.feature.debt.DebtOverviewScreen
import com.sotraubo.app.feature.debt.PersonDebtDetailScreen
import com.sotraubo.app.feature.ledger.CurrencyExchangeScreen
import com.sotraubo.app.feature.ledger.FamilyExpenseScreen
import com.sotraubo.app.feature.ledger.GoodsPurchaseScreen
import com.sotraubo.app.feature.ledger.LedgerViewModel
import com.sotraubo.app.feature.ledger.MoreHubScreen
import com.sotraubo.app.feature.ledger.QuotaFeeScreen
import com.sotraubo.app.feature.security.PinLockScreen
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.outlined.CurrencyExchange
import com.sotraubo.app.feature.trade.MuaBanFilterType
import com.sotraubo.app.feature.trade.MuaBanScreen
import com.sotraubo.app.feature.trade.TradeOrderFormScreen

sealed interface AppScreen {
    // 5 Tab chính trên Thanh điều hướng dưới cùng
    data object Dashboard : AppScreen
    data class MuaBan(val initialFilter: MuaBanFilterType = MuaBanFilterType.ALL) : AppScreen
    data class DebtOverview(val initialTab: Int = 0) : AppScreen
    data object Contacts : AppScreen
    data object MoreHub : AppScreen
    data object GoodsPurchases : AppScreen
    data class CurrencyExchange(val initialDirectionCode: String? = null) : AppScreen
    data object FamilyExpenses : AppScreen
    data object QuotaFees : AppScreen

    // Các màn hình chi tiết & biểu mẫu
    data class CashForm(
        val flowType: CashFlowType,
        val preselectedContactId: Long? = null,
        val preselectedOrderId: Long? = null,
        val editTransactionId: Long? = null
    ) : AppScreen

    data class TradeOrderForm(
        val orderType: TradeOrderType,
        val preselectedContactId: Long? = null,
        val editOrderId: Long? = null
    ) : AppScreen

    data class PersonDebtDetail(val contactId: Long) : AppScreen
    data class ContactDetail(val contactId: Long) : AppScreen
    data object BackupAndReport : AppScreen
}

enum class BottomNavTab(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    DASHBOARD("Tổng quan", Icons.Filled.Home, Icons.Outlined.Home),
    BUSINESS("Thu chi", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
    DEBT("Công nợ", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet),
    CONTACTS("Đối tác", Icons.Filled.People, Icons.Outlined.People)
}

class MainActivity : FragmentActivity() {

    private val viewModel: SoTrauBoViewModel by viewModels {
        val app = application as SoTrauBoApplication
        SoTrauBoViewModel.provideFactory(app.container)
    }

    private val ledgerViewModel: LedgerViewModel by viewModels {
        val app = application as SoTrauBoApplication
        LedgerViewModel.provideFactory(app.container.ledgerDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoTrauBoTheme {
                SoTrauBoAppContent(viewModel = viewModel, ledgerViewModel = ledgerViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoTrauBoAppContent(viewModel: SoTrauBoViewModel, ledgerViewModel: LedgerViewModel) {
    val context = LocalContext.current
    val isUnlocked by viewModel.isUnlocked.collectAsStateWithLifecycle()
    val isPinConfigured by viewModel.isPinConfigured.collectAsStateWithLifecycle()
    val isBiometricEnabled by viewModel.isBiometricEnabled.collectAsStateWithLifecycle()

    if (!isUnlocked) {
        PinLockScreen(
            isCustomPinConfigured = isPinConfigured,
            onVerifyPin = viewModel::verifyPin,
            onSetupPin = viewModel::setupNewPin,
            onUnlocked = viewModel::markUnlocked,
            isBiometricEnabled = isBiometricEnabled
        )
        return
    }

    val backStack = remember { mutableStateListOf<AppScreen>(AppScreen.Dashboard) }
    val currentScreen = backStack.lastOrNull() ?: AppScreen.Dashboard

    fun navigateTo(screen: AppScreen) {
        viewModel.clearMessages()
        backStack.add(screen)
    }

    fun switchRootTab(tabScreen: AppScreen) {
        viewModel.clearMessages()
        backStack.clear()
        backStack.add(tabScreen)
    }

    fun navigateBack() {
        viewModel.clearMessages()
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }

    BackHandler(enabled = backStack.size > 1) {
        navigateBack()
    }

    val dashboardSummary by viewModel.dashboardSummary.collectAsStateWithLifecycle()
    val contacts by viewModel.allContacts.collectAsStateWithLifecycle()
    val tradeOrders by viewModel.allTradeOrders.collectAsStateWithLifecycle()
    val cashTransactions by viewModel.allCashTransactions.collectAsStateWithLifecycle()
    val recentActivities by viewModel.recentActivities.collectAsStateWithLifecycle()
    val selectedPeriod by viewModel.selectedPeriod.collectAsStateWithLifecycle()
    val periodStats by viewModel.periodStats.collectAsStateWithLifecycle()
    val debtSearchQuery by viewModel.debtSearchQuery.collectAsStateWithLifecycle()
    val debtSortOption by viewModel.debtSortOption.collectAsStateWithLifecycle()
    val receivableDebtors by viewModel.receivableDebtors.collectAsStateWithLifecycle()
    val payableCreditors by viewModel.payableCreditors.collectAsStateWithLifecycle()
    val goodsList by ledgerViewModel.goods.collectAsStateWithLifecycle()
    val exchangeList by ledgerViewModel.exchanges.collectAsStateWithLifecycle()
    val expenseList by ledgerViewModel.expenses.collectAsStateWithLifecycle()
    val quotaFeeList by ledgerViewModel.quotaFees.collectAsStateWithLifecycle()
    val formErrorMessage by viewModel.formErrorMessage.collectAsStateWithLifecycle()
    val backupStatusMessage by viewModel.backupStatusMessage.collectAsStateWithLifecycle()
    val pendingRestorePreview by viewModel.pendingRestorePreview.collectAsStateWithLifecycle()
    val appPrefs = remember(context) { context.getSharedPreferences("sotraubo_prefs", Context.MODE_PRIVATE) }
    var initialCapital by remember { mutableLongStateOf(appPrefs.getLong("initial_capital_vnd", 0L)) }
    var showNavExpandSheet by remember { mutableStateOf(false) }

    // Ẩn thanh điều hướng khi bàn phím mở để nhường chỗ hiển thị nội dung.
    val isImeVisible = rememberIsKeyboardVisible()
    val isBottomBarVisible = !isImeVisible && when (currentScreen) {
        is AppScreen.Dashboard,
        is AppScreen.MuaBan,
        is AppScreen.CurrencyExchange,
        is AppScreen.DebtOverview,
        is AppScreen.QuotaFees,
        is AppScreen.GoodsPurchases,
        is AppScreen.FamilyExpenses,
        is AppScreen.Contacts,
        is AppScreen.MoreHub -> true
        else -> false
    }

    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
                val activeTab = when (currentScreen) {
                    is AppScreen.Dashboard -> BottomNavTab.DASHBOARD
                    is AppScreen.DebtOverview -> BottomNavTab.DEBT
                    is AppScreen.Contacts -> BottomNavTab.CONTACTS
                    is AppScreen.MuaBan,
                    is AppScreen.QuotaFees,
                    is AppScreen.CurrencyExchange,
                    is AppScreen.GoodsPurchases,
                    is AppScreen.FamilyExpenses,
                    is AppScreen.MoreHub -> BottomNavTab.BUSINESS
                    else -> BottomNavTab.DASHBOARD
                }

                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(horizontal = 6.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BottomNavTab.entries.forEach { tab ->
                                val isSelected = (activeTab == tab) || (tab == BottomNavTab.BUSINESS && showNavExpandSheet)
                                val tintColor by animateColorAsState(
                                    targetValue = if (isSelected) BluePrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    label = "navTint"
                                )
                                val badgeColor by animateColorAsState(
                                    targetValue = if (isSelected) BlueContainer else Color.Transparent,
                                    label = "navBadge"
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = rememberRipple(bounded = true, color = BluePrimary),
                                            role = Role.Tab,
                                            onClick = {
                                                when (tab) {
                                                    BottomNavTab.DASHBOARD -> switchRootTab(AppScreen.Dashboard)
                                                    BottomNavTab.BUSINESS -> showNavExpandSheet = true
                                                    BottomNavTab.DEBT -> switchRootTab(AppScreen.DebtOverview(0))
                                                    BottomNavTab.CONTACTS -> switchRootTab(AppScreen.Contacts)
                                                }
                                            }
                                        )
                                        .padding(vertical = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(38.dp)
                                            .height(26.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(badgeColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = tab.label,
                                            tint = tintColor,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = tab.label,
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        softWrap = false,
                                        overflow = TextOverflow.Ellipsis,
                                        color = tintColor,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { rootPadding ->
        if (showNavExpandSheet) {
            ModalBottomSheet(
                onDismissRequest = { showNavExpandSheet = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Thu chi — Chọn mục",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        NavExpandTile(
                            title = "Hàng hóa",
                            subtitle = "Mua bán & thu chi",
                            icon = Icons.Filled.ShoppingCart,
                            selected = currentScreen is AppScreen.MuaBan,
                            onClick = {
                                showNavExpandSheet = false
                                switchRootTab(AppScreen.MuaBan(MuaBanFilterType.ALL))
                            },
                            modifier = Modifier.weight(1f)
                        )
                        NavExpandTile(
                            title = "Phí Quota",
                            subtitle = "Doanh thu biên giới",
                            icon = Icons.AutoMirrored.Filled.ReceiptLong,
                            selected = currentScreen is AppScreen.QuotaFees,
                            onClick = {
                                showNavExpandSheet = false
                                switchRootTab(AppScreen.QuotaFees)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        NavExpandTile(
                            title = "Đổi Bath",
                            subtitle = "VND ⇄ Bath",
                            icon = Icons.Filled.CurrencyExchange,
                            selected = currentScreen is AppScreen.CurrencyExchange,
                            onClick = {
                                showNavExpandSheet = false
                                switchRootTab(AppScreen.CurrencyExchange())
                            },
                            modifier = Modifier.weight(1f)
                        )
                        NavExpandTile(
                            title = "Sinh hoạt",
                            subtitle = "Chi gia đình",
                            icon = Icons.Filled.Home,
                            selected = currentScreen is AppScreen.FamilyExpenses,
                            onClick = {
                                showNavExpandSheet = false
                                switchRootTab(AppScreen.FamilyExpenses)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        NavExpandTile(
                            title = "Tổng hợp & PDF",
                            subtitle = "Sổ phụ & Sao lưu",
                            icon = Icons.Filled.Apps,
                            selected = currentScreen is AppScreen.MoreHub,
                            onClick = {
                                showNavExpandSheet = false
                                switchRootTab(AppScreen.MoreHub)
                            },
                            centered = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(rootPadding)
        ) {
            when (val screen = currentScreen) {
                is AppScreen.Dashboard -> {
                    DashboardScreen(
                        summary = dashboardSummary,
                        receivableDebtors = receivableDebtors,
                        payableCreditors = payableCreditors,
                        selectedPeriod = selectedPeriod,
                        onSelectPeriod = viewModel::selectPeriod,
                        periodStats = periodStats,
                        onQuickBuyCattle = { navigateTo(AppScreen.TradeOrderForm(TradeOrderType.PURCHASE)) },
                        onOpenReceivableTab = { switchRootTab(AppScreen.DebtOverview(0)) },
                        onOpenPayableTab = { switchRootTab(AppScreen.DebtOverview(1)) },
                        onOpenPersonDebtDetail = { contactId -> navigateTo(AppScreen.PersonDebtDetail(contactId)) },
                        onOpenAllActivities = { switchRootTab(AppScreen.MuaBan(MuaBanFilterType.ALL)) },
                        onOpenBackupAndReport = { navigateTo(AppScreen.BackupAndReport) },
                        bathExchanges = exchangeList,
                        quotaFees = quotaFeeList,
                        familyExpenses = expenseList,
                        tradeOrders = tradeOrders,
                        cashTransactions = cashTransactions,
                        onOpenBathExchange = { switchRootTab(AppScreen.CurrencyExchange()) },
                        onQuickBathExchange = { dirCode ->
                            navigateTo(AppScreen.CurrencyExchange(initialDirectionCode = dirCode))
                        },
                        onOpenQuotaFees = { navigateTo(AppScreen.QuotaFees) },
                        onOpenFamilyExpenses = { navigateTo(AppScreen.FamilyExpenses) },
                        initialCapital = initialCapital,
                        onUpdateInitialCapital = { newCapital ->
                            initialCapital = newCapital
                            appPrefs.edit().putLong("initial_capital_vnd", newCapital).apply()
                        }
                    )
                }

                is AppScreen.MuaBan -> {
                    MuaBanScreen(
                        orders = tradeOrders,
                        cashTransactions = cashTransactions,
                        initialFilter = screen.initialFilter,
                        onAddPurchase = { navigateTo(AppScreen.TradeOrderForm(TradeOrderType.PURCHASE)) },
                        onAddSale = { navigateTo(AppScreen.TradeOrderForm(TradeOrderType.SALE)) },
                        onAddCashIn = { navigateTo(AppScreen.CashForm(CashFlowType.CASH_IN)) },
                        onAddCashOut = { navigateTo(AppScreen.CashForm(CashFlowType.CASH_OUT)) },
                        onOrderClick = { orderId ->
                            val order = tradeOrders.firstOrNull { it.id == orderId }
                            if (order != null) {
                                navigateTo(AppScreen.TradeOrderForm(order.orderType, editOrderId = order.id))
                            }
                        },
                        onCashClick = { cashId ->
                            val tx = cashTransactions.firstOrNull { it.id == cashId }
                            if (tx != null) {
                                navigateTo(AppScreen.CashForm(tx.flowType, editTransactionId = tx.id))
                            }
                        }
                    )
                }

                is AppScreen.DebtOverview -> {
                    DebtOverviewScreen(
                        initialTab = screen.initialTab,
                        searchQuery = debtSearchQuery,
                        onSearchQueryChange = viewModel::updateDebtSearchQuery,
                        sortOption = debtSortOption,
                        onSortOptionChange = viewModel::updateDebtSortOption,
                        receivableDebtors = receivableDebtors,
                        payableCreditors = payableCreditors,
                        bathExchanges = exchangeList,
                        onOpenBathExchange = { switchRootTab(AppScreen.CurrencyExchange()) },
                        onPersonClick = { contactId -> navigateTo(AppScreen.PersonDebtDetail(contactId)) },
                        onQuickCollectDebt = { contactId ->
                            navigateTo(
                                AppScreen.CashForm(
                                    flowType = CashFlowType.CASH_IN,
                                    preselectedContactId = contactId
                                )
                            )
                        },
                        onQuickPayDebt = { contactId ->
                            navigateTo(
                                AppScreen.CashForm(
                                    flowType = CashFlowType.CASH_OUT,
                                    preselectedContactId = contactId
                                )
                            )
                        }
                    )
                }

                is AppScreen.MoreHub -> {
                    MoreHubScreen(
                        goods = goodsList,
                        exchanges = exchangeList,
                        expenses = expenseList,
                        quotaFees = quotaFeeList,
                        contactCount = contacts.size,
                        onOpenGoods = { navigateTo(AppScreen.GoodsPurchases) },
                        onOpenExchange = { navigateTo(AppScreen.CurrencyExchange()) },
                        onOpenFamily = { navigateTo(AppScreen.FamilyExpenses) },
                        onOpenQuotaFees = { navigateTo(AppScreen.QuotaFees) },
                        onOpenContacts = { navigateTo(AppScreen.Contacts) }
                    )
                }

                is AppScreen.GoodsPurchases -> {
                    GoodsPurchaseScreen(
                        goods = goodsList,
                        contacts = contacts,
                        onCreateNewContactInline = { fullName, phone, address, note, onCreated ->
                            viewModel.saveContact(
                                fullName = fullName,
                                phone = phone,
                                address = address,
                                note = note,
                                onSuccess = onCreated
                            )
                        },
                        onBack = ::navigateBack,
                        onSave = ledgerViewModel::saveGoods,
                        onDelete = ledgerViewModel::deleteGoods,
                        showBackButton = backStack.size > 1
                    )
                }

                is AppScreen.CurrencyExchange -> {
                    CurrencyExchangeScreen(
                        exchanges = exchangeList,
                        contacts = contacts,
                        onCreateNewContactInline = { fullName, phone, address, note, onCreated ->
                            viewModel.saveContact(
                                fullName = fullName,
                                phone = phone,
                                address = address,
                                note = note,
                                onSuccess = onCreated
                            )
                        },
                        onBack = ::navigateBack,
                        onSave = ledgerViewModel::saveExchange,
                        onDelete = ledgerViewModel::deleteExchange,
                        showBackButton = screen.initialDirectionCode != null || backStack.size > 1,
                        initialDirectionCode = screen.initialDirectionCode
                    )
                }

                is AppScreen.FamilyExpenses -> {
                    FamilyExpenseScreen(
                        expenses = expenseList,
                        contacts = contacts,
                        onCreateNewContactInline = { fullName, phone, address, note, onCreated ->
                            viewModel.saveContact(
                                fullName = fullName,
                                phone = phone,
                                address = address,
                                note = note,
                                onSuccess = onCreated
                            )
                        },
                        onBack = ::navigateBack,
                        onSave = ledgerViewModel::saveExpense,
                        onDelete = ledgerViewModel::deleteExpense,
                        showBackButton = backStack.size > 1
                    )
                }

                is AppScreen.QuotaFees -> {
                    QuotaFeeScreen(
                        quotaFees = quotaFeeList,
                        contacts = contacts,
                        onCreateNewContactInline = { fullName, phone, address, note, onCreated ->
                            viewModel.saveContact(
                                fullName = fullName,
                                phone = phone,
                                address = address,
                                note = note,
                                onSuccess = onCreated
                            )
                        },
                        onBack = ::navigateBack,
                        onSave = ledgerViewModel::saveQuotaFee,
                        onDelete = ledgerViewModel::deleteQuotaFee,
                        showBackButton = backStack.size > 1
                    )
                }

                is AppScreen.Contacts -> {
                    ContactListScreen(
                        contacts = contacts,
                        onContactClick = { contactId -> navigateTo(AppScreen.ContactDetail(contactId)) },
                        onCreateContact = { fullName, phone, address, note ->
                            viewModel.saveContact(
                                fullName = fullName,
                                phone = phone,
                                address = address,
                                note = note
                            )
                        }
                    )
                }

                is AppScreen.CashForm -> {
                    val existingTx = remember(cashTransactions, screen.editTransactionId) {
                        cashTransactions.firstOrNull { it.id == screen.editTransactionId }
                    }
                    CashTransactionFormScreen(
                        flowType = screen.flowType,
                        contacts = contacts,
                        allTradeOrders = tradeOrders,
                        preselectedContactId = screen.preselectedContactId,
                        preselectedOrderId = screen.preselectedOrderId,
                        existingTransaction = existingTx,
                        errorMessage = formErrorMessage,
                        onBack = ::navigateBack,
                        onCreateNewContactInline = { fullName, phone, address, note, onCreated ->
                            viewModel.saveContact(
                                fullName = fullName,
                                phone = phone,
                                address = address,
                                note = note,
                                onSuccess = onCreated
                            )
                        },
                        onSaveTransaction = { contactId, linkedOrderId, autoAllocate, amount, date, method, content, note, paths ->
                            viewModel.saveCashTransaction(
                                transactionId = screen.editTransactionId ?: 0L,
                                flowType = screen.flowType,
                                contactId = contactId,
                                linkedTradeOrderId = linkedOrderId,
                                autoAllocateOldestDebt = autoAllocate,
                                amount = amount,
                                transactionDate = date,
                                paymentMethod = method,
                                shortContent = content,
                                note = note,
                                attachmentPaths = paths,
                                onSuccess = ::navigateBack
                            )
                        },
                        onDeleteTransaction = if (screen.editTransactionId != null) {
                            {
                                viewModel.deleteCashTransaction(
                                    transactionId = screen.editTransactionId,
                                    onSuccess = ::navigateBack
                                )
                            }
                        } else null
                    )
                }

                is AppScreen.TradeOrderForm -> {
                    val existingOrder = remember(tradeOrders, screen.editOrderId) {
                        tradeOrders.firstOrNull { it.id == screen.editOrderId }
                    }
                    val existingInitialTx = remember(cashTransactions, screen.editOrderId) {
                        cashTransactions.firstOrNull {
                            it.tradeOrderId == screen.editOrderId && it.isInitialPayment
                        }
                    }

                    TradeOrderFormScreen(
                        orderType = screen.orderType,
                        contacts = contacts,
                        preselectedContactId = screen.preselectedContactId,
                        existingOrder = existingOrder,
                        existingInitialPaidAmount = if (screen.editOrderId != null) {
                            existingInitialTx?.amount ?: 0L
                        } else null,
                        existingInitialPaymentMethod = existingInitialTx?.paymentMethod,
                        errorMessage = formErrorMessage,
                        onBack = ::navigateBack,
                        onCreateNewContactInline = { fullName, phone, address, note, onCreated ->
                            viewModel.saveContact(
                                fullName = fullName,
                                phone = phone,
                                address = address,
                                note = note,
                                onSuccess = onCreated
                            )
                        },
                        onSaveOrder = { selectedType, contactId, date, qty, desc, total, initialPaid, method, note, paths ->
                            viewModel.saveTradeOrder(
                                orderId = screen.editOrderId ?: 0L,
                                orderType = selectedType,
                                contactId = contactId,
                                transactionDate = date,
                                cattleQuantity = qty,
                                description = desc,
                                totalAmount = total,
                                initialPaidAmount = initialPaid,
                                initialPaymentMethod = method,
                                note = note,
                                attachmentPaths = paths,
                                onSuccess = ::navigateBack
                            )
                        },
                        onDeleteOrder = if (screen.editOrderId != null) {
                            {
                                viewModel.deleteTradeOrder(
                                    orderId = screen.editOrderId,
                                    onSuccess = ::navigateBack
                                )
                            }
                        } else null,
                        onRecordInstallment = if (existingOrder != null) {
                            { addedAmount, payDate, installmentNote, updatedOrderNote ->
                                val isPurchase = (existingOrder.orderType == TradeOrderType.PURCHASE)
                                val flowType = if (isPurchase) CashFlowType.CASH_OUT else CashFlowType.CASH_IN
                                val content = if (isPurchase) {
                                    "Trả thêm nợ đơn: ${existingOrder.description}"
                                } else {
                                    "Thu thêm nợ đơn: ${existingOrder.description}"
                                }
                                viewModel.saveCashTransaction(
                                    flowType = flowType,
                                    contactId = existingOrder.contactId,
                                    linkedTradeOrderId = existingOrder.id,
                                    autoAllocateOldestDebt = false,
                                    amount = addedAmount,
                                    transactionDate = payDate,
                                    paymentMethod = existingInitialTx?.paymentMethod ?: PaymentMethod.CASH,
                                    shortContent = content,
                                    note = installmentNote,
                                    attachmentPaths = emptyList(),
                                    onSuccess = {
                                        viewModel.saveTradeOrder(
                                            orderId = existingOrder.id,
                                            orderType = existingOrder.orderType,
                                            contactId = existingOrder.contactId,
                                            transactionDate = existingOrder.transactionDate,
                                            cattleQuantity = existingOrder.cattleQuantity,
                                            description = existingOrder.description,
                                            totalAmount = existingOrder.totalAmount,
                                            initialPaidAmount = existingInitialTx?.amount ?: 0L,
                                            initialPaymentMethod = existingInitialTx?.paymentMethod ?: PaymentMethod.CASH,
                                            note = updatedOrderNote,
                                            attachmentPaths = existingOrder.attachmentPaths,
                                            onSuccess = ::navigateBack
                                        )
                                    }
                                )
                            }
                        } else null
                    )
                }

                is AppScreen.PersonDebtDetail -> {
                    val detailFlow = remember(screen.contactId) {
                        viewModel.observePersonBidirectionalDebt(screen.contactId)
                    }
                    val detail by detailFlow.collectAsStateWithLifecycle()
                    val currentDetail = detail
                    if (currentDetail != null) {
                        PersonDebtDetailScreen(
                            detail = currentDetail,
                            onBack = ::navigateBack,
                            onCollectReceivableClick = { orderId ->
                                navigateTo(
                                    AppScreen.CashForm(
                                        flowType = CashFlowType.CASH_IN,
                                        preselectedContactId = screen.contactId,
                                        preselectedOrderId = orderId
                                    )
                                )
                            },
                            onPayPayableClick = { orderId ->
                                navigateTo(
                                    AppScreen.CashForm(
                                        flowType = CashFlowType.CASH_OUT,
                                        preselectedContactId = screen.contactId,
                                        preselectedOrderId = orderId
                                    )
                                )
                            },
                            onExportPdfClick = {
                                viewModel.exportPersonDebtPdf(context, currentDetail)
                            },
                            onOrderClick = { orderId ->
                                val order = tradeOrders.firstOrNull { it.id == orderId }
                                if (order != null) {
                                    navigateTo(AppScreen.TradeOrderForm(order.orderType, editOrderId = order.id))
                                }
                            },
                            onCashTransactionClick = { txId ->
                                val tx = cashTransactions.firstOrNull { it.id == txId }
                                if (tx != null) {
                                    navigateTo(AppScreen.CashForm(tx.flowType, editTransactionId = tx.id))
                                }
                            }
                        )
                    }
                }

                is AppScreen.ContactDetail -> {
                    val contactSummary = contacts.firstOrNull { it.contact.id == screen.contactId }
                    if (contactSummary != null) {
                        val personOrders = tradeOrders.filter { it.contactId == screen.contactId }
                        val personCash = cashTransactions.filter { it.contactId == screen.contactId }
                        val detailFlow = remember(screen.contactId) {
                            viewModel.observePersonBidirectionalDebt(screen.contactId)
                        }
                        val debtDetail by detailFlow.collectAsStateWithLifecycle()

                        ContactDetailScreen(
                            contactSummary = contactSummary,
                            personTradeOrders = personOrders,
                            personCashTransactions = personCash,
                            errorMessage = formErrorMessage,
                            onBack = ::navigateBack,
                            onOpenDebtDetail = { id -> navigateTo(AppScreen.PersonDebtDetail(id)) },
                            onUpdateContact = { fullName, phone, address, note ->
                                viewModel.saveContact(
                                    id = screen.contactId,
                                    fullName = fullName,
                                    phone = phone,
                                    address = address,
                                    note = note
                                )
                            },
                            onDeleteContact = {
                                viewModel.deleteContact(
                                    contactId = screen.contactId,
                                    onSuccess = ::navigateBack
                                )
                            },
                            onExportPersonPdf = {
                                debtDetail?.let { viewModel.exportPersonDebtPdf(context, it) }
                            },
                            onOrderClick = { orderId ->
                                val order = tradeOrders.firstOrNull { it.id == orderId }
                                if (order != null) {
                                    navigateTo(AppScreen.TradeOrderForm(order.orderType, editOrderId = order.id))
                                }
                            },
                            onCashTransactionClick = { txId ->
                                val tx = cashTransactions.firstOrNull { it.id == txId }
                                if (tx != null) {
                                    navigateTo(AppScreen.CashForm(tx.flowType, editTransactionId = tx.id))
                                }
                            }
                        )
                    }
                }

                is AppScreen.BackupAndReport -> {
                    BackupAndReportScreen(
                        summary = dashboardSummary,
                        periodStats = periodStats,
                        statusMessage = backupStatusMessage,
                        errorMessage = formErrorMessage,
                        pendingRestorePreview = pendingRestorePreview,
                        onBack = ::navigateBack,
                        onExportBackupToUri = { uri -> viewModel.exportBackup(context, uri) },
                        onSelectBackupUriForValidation = { uri -> viewModel.validateBackupForRestore(context, uri) },
                        onConfirmRestoreOverwrite = { viewModel.confirmRestoreBackup(context) },
                        onCancelRestoreOverwrite = viewModel::cancelRestoreBackup,
                        onExportReportPdfToUri = { uri, category, mode, anchor ->
                            viewModel.exportOverviewReportPdf(context, uri, category, mode, anchor)
                        },
                        onChangePin = viewModel::changePin,
                        onLockAppNow = {
                            switchRootTab(AppScreen.Dashboard)
                            viewModel.lockAppNow()
                        },
                        isBiometricEnabled = isBiometricEnabled,
                        onToggleBiometric = viewModel::setBiometricEnabled,
                        onClearAllDataWithPin = { pin ->
                            viewModel.clearAllDataWithPin(context, pin) {
                                initialCapital = 0L
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NavExpandTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    centered: Boolean = false
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) BlueContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (centered) Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally) else Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) BluePrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
            Column(modifier = if (centered) Modifier else Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selected) BluePrimary else MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Theo dõi bàn phím đang mở hay không bằng trạng thái thật của window.
 * (WindowInsets.isImeVisible của Compose báo sai khi app chưa bật edge-to-edge.)
 */
@Composable
private fun rememberIsKeyboardVisible(): Boolean {
    val view = LocalView.current
    var visible by remember { mutableStateOf(false) }
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            visible = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) == true
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose { view.viewTreeObserver.removeOnGlobalLayoutListener(listener) }
    }
    return visible
}
