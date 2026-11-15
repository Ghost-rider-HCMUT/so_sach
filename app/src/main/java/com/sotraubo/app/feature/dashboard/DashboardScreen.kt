package com.sotraubo.app.feature.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Surface
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sotraubo.app.core.common.format.CurrencyFormatter
import com.sotraubo.app.core.common.format.DateFormatter
import com.sotraubo.app.core.designsystem.component.CurrencyTextField
import com.sotraubo.app.core.common.format.ReportPeriodType
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity
import com.sotraubo.app.core.database.entity.FamilyExpenseEntity
import com.sotraubo.app.core.database.entity.QuotaFeeEntity
import com.sotraubo.app.core.designsystem.component.DropdownSelector
import com.sotraubo.app.core.designsystem.component.InfoLine
import com.sotraubo.app.core.designsystem.component.ScrollToTopButton
import com.sotraubo.app.core.designsystem.component.SemanticBadge
import com.sotraubo.app.core.designsystem.component.SummaryMetricCard
import com.sotraubo.app.core.designsystem.theme.BlueContainer
import com.sotraubo.app.core.designsystem.theme.BluePrimary
import com.sotraubo.app.core.designsystem.theme.BluePrimaryDark
import com.sotraubo.app.core.designsystem.theme.DividerDark
import com.sotraubo.app.core.designsystem.theme.GreenReceive
import com.sotraubo.app.core.designsystem.theme.GreenReceiveContainer
import com.sotraubo.app.core.designsystem.theme.OrangePayable
import com.sotraubo.app.core.designsystem.theme.OrangePayableContainer
import com.sotraubo.app.core.designsystem.theme.RedTransfer
import com.sotraubo.app.core.designsystem.theme.RedTransferContainer
import com.sotraubo.app.core.designsystem.theme.responsiveSp
import com.sotraubo.app.core.designsystem.theme.SurfaceDarkCard
import com.sotraubo.app.core.model.CashFlowType
import com.sotraubo.app.core.model.CashTransactionWithContact
import com.sotraubo.app.core.model.DashboardFinancialSummary
import com.sotraubo.app.core.model.PeriodBusinessStats
import com.sotraubo.app.core.model.PersonDebtSummaryItem
import com.sotraubo.app.core.model.TradeOrderType
import com.sotraubo.app.core.model.TradeOrderWithPayment
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import com.sotraubo.app.core.model.UnifiedActivityItem

/**
 * MÀN HÌNH TỔNG QUAN (DASHBOARD — Mục 3 Master Prompt):
 * 3.1. Khu vực Tổng quan tài chính (6 chỉ số tách biệt, nhấn để mở chi tiết).
 * 3.2. Khu vực Công nợ nổi bật (A. Người khác còn nợ mình & B. Mình còn nợ người khác).
 * 3.3. Giao dịch gần đây (Nhận tiền, Chuyển tiền, Mua hàng, Bán hàng).
 * 3.4. Thống kê kinh doanh (Hôm nay, Tuần này, Tháng này, Năm nay, Tùy chọn + Biểu đồ tiếng Việt).
 * 3.5. Thao tác nhanh trên Dashboard (Nhận tiền, Chuyển tiền, Mua hàng, Bán hàng).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    summary: DashboardFinancialSummary,
    receivableDebtors: List<PersonDebtSummaryItem>,
    payableCreditors: List<PersonDebtSummaryItem>,
    selectedPeriod: ReportPeriodType,
    onSelectPeriod: (ReportPeriodType) -> Unit,
    periodStats: PeriodBusinessStats,
    onQuickBuyCattle: () -> Unit,
    onOpenReceivableTab: () -> Unit,
    onOpenPayableTab: () -> Unit,
    onOpenPersonDebtDetail: (Long) -> Unit,
    onOpenAllActivities: () -> Unit,
    onOpenBackupAndReport: () -> Unit,
    bathExchanges: List<CurrencyExchangeEntity> = emptyList(),
    quotaFees: List<QuotaFeeEntity> = emptyList(),
    familyExpenses: List<FamilyExpenseEntity> = emptyList(),
    tradeOrders: List<TradeOrderWithPayment> = emptyList(),
    cashTransactions: List<CashTransactionWithContact> = emptyList(),
    onOpenBathExchange: () -> Unit = {},
    onQuickBathExchange: (String) -> Unit = {},
    onOpenQuotaFees: () -> Unit = {},
    onOpenFamilyExpenses: () -> Unit = {},
    initialCapital: Long = 0L,
    onUpdateInitialCapital: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showInitialCapitalDialog by remember { mutableStateOf(false) }
    var selectedScopeType by rememberSaveable { mutableStateOf(ReportPeriodType.THIS_YEAR) }
    var selectedAnchorMillis by rememberSaveable { mutableLongStateOf(DateFormatter.todayNoonMillis()) }
    val isPeriodFlowMode = selectedScopeType != ReportPeriodType.THIS_YEAR
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Quản Lý",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Tổng quan tài chính & công nợ",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onOpenBackupAndReport) {
                        Icon(
                            imageVector = Icons.Default.Backup,
                            contentDescription = "Sao lưu dữ liệu",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sao lưu",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        val listState = rememberLazyListState()
        val periodRange = remember(selectedScopeType, selectedAnchorMillis) {
            DateFormatter.getPeriodRange(selectedScopeType, selectedAnchorMillis)
        }
        val selectedDate = remember(selectedAnchorMillis) {
            java.time.Instant.ofEpochMilli(selectedAnchorMillis)
                .atZone(DateFormatter.VIETNAM_ZONE)
                .toLocalDate()
        }
        val activeScopeLabel = when (selectedScopeType) {
            ReportPeriodType.THIS_YEAR -> "Năm ${selectedDate.year}"
            ReportPeriodType.THIS_MONTH -> "Tháng ${selectedDate.monthValue}/${selectedDate.year}"
            ReportPeriodType.THIS_WEEK -> {
                val weekStart = java.time.Instant.ofEpochMilli(periodRange.startMillis)
                    .atZone(DateFormatter.VIETNAM_ZONE)
                    .toLocalDate()
                val weekEnd = java.time.Instant.ofEpochMilli(periodRange.endMillis)
                    .atZone(DateFormatter.VIETNAM_ZONE)
                    .toLocalDate()
                val fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                "${weekStart.format(fmt)} - ${weekEnd.format(fmt)}"
            }
            ReportPeriodType.TODAY -> "Ngày ${selectedDate.dayOfMonth.toString().padStart(2, '0')}/${selectedDate.monthValue.toString().padStart(2, '0')}/${selectedDate.year}"
            ReportPeriodType.CUSTOM -> periodRange.label
        }
        val todayDate = remember {
            java.time.Instant.ofEpochMilli(DateFormatter.todayNoonMillis())
                .atZone(DateFormatter.VIETNAM_ZONE)
                .toLocalDate()
        }
        val isCurrentPeriod = when (selectedScopeType) {
            ReportPeriodType.THIS_YEAR -> selectedDate.year == todayDate.year
            ReportPeriodType.THIS_MONTH -> selectedDate.year == todayDate.year && selectedDate.monthValue == todayDate.monthValue
            ReportPeriodType.THIS_WEEK -> DateFormatter.todayNoonMillis() in periodRange.startMillis..periodRange.endMillis
            ReportPeriodType.TODAY -> selectedDate == todayDate
            ReportPeriodType.CUSTOM -> true
        }
        val ordersInPeriod = remember(tradeOrders, periodRange) {
            tradeOrders.filter { it.transactionDate in periodRange.startMillis..periodRange.endMillis }
        }
        val cashInPeriod = remember(cashTransactions, periodRange) {
            cashTransactions.filter { it.transactionDate in periodRange.startMillis..periodRange.endMillis }
        }
        val effectiveSummary = remember(ordersInPeriod, cashInPeriod) {
            DashboardFinancialSummary(
                totalCashIn = cashInPeriod.filter { it.flowType == CashFlowType.CASH_IN }.sumOf { it.amount },
                totalCashOut = cashInPeriod.filter { it.flowType == CashFlowType.CASH_OUT }.sumOf { it.amount },
                totalPurchaseAmount = ordersInPeriod.filter { it.orderType == TradeOrderType.PURCHASE }.sumOf { it.totalAmount },
                totalSaleAmount = ordersInPeriod.filter { it.orderType == TradeOrderType.SALE }.sumOf { it.totalAmount },
                totalReceivableDebt = ordersInPeriod.filter { it.orderType == TradeOrderType.SALE }.sumOf { it.remainingDebt },
                totalPayableDebt = ordersInPeriod.filter { it.orderType == TradeOrderType.PURCHASE }.sumOf { it.remainingDebt }
            )
        }
        val scopeStats = remember(effectiveSummary, ordersInPeriod, activeScopeLabel) {
            PeriodBusinessStats(
                periodLabel = activeScopeLabel,
                totalPurchaseAmount = effectiveSummary.totalPurchaseAmount,
                totalSaleAmount = effectiveSummary.totalSaleAmount,
                totalCashIn = effectiveSummary.totalCashIn,
                totalCashOut = effectiveSummary.totalCashOut,
                purchaseCattleCount = ordersInPeriod.filter { it.orderType == TradeOrderType.PURCHASE }.sumOf { it.cattleQuantity },
                saleCattleCount = ordersInPeriod.filter { it.orderType == TradeOrderType.SALE }.sumOf { it.cattleQuantity }
            )
        }
        val effectiveBathExchanges = remember(bathExchanges, periodRange) {
            bathExchanges.filter { it.exchangeDate in periodRange.startMillis..periodRange.endMillis }
        }
        val effectiveQuotaFees = remember(quotaFees, periodRange) {
            quotaFees.filter { it.feeDate in periodRange.startMillis..periodRange.endMillis }
        }
        val effectiveFamilyExpenses = remember(familyExpenses, periodRange) {
            familyExpenses.filter { it.expenseDate in periodRange.startMillis..periodRange.endMillis }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
            // 3.5. THAO TÁC NHANH TRÊN DASHBOARD (Chuẩn Banking: thoáng, chữ 1 dòng không bị cắt)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp, horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        QuickActionTile(
                            label = "Hàng hóa",
                            subLabel = "Mua / Bán",
                            icon = Icons.Default.ShoppingCart,
                            color = BluePrimary,
                            containerColor = BlueContainer,
                            onClick = onQuickBuyCattle,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionTile(
                            label = "Đổi Bath",
                            subLabel = "Ghi đổi tiền",
                            icon = Icons.Default.CurrencyExchange,
                            color = GreenReceive,
                            containerColor = GreenReceiveContainer,
                            onClick = { onQuickBathExchange("VND_TO_THB") },
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionTile(
                            label = "Phí Quota",
                            subLabel = "Thu biên giới",
                            icon = Icons.AutoMirrored.Filled.ReceiptLong,
                            color = GreenReceive,
                            containerColor = GreenReceiveContainer,
                            onClick = onOpenQuotaFees,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionTile(
                            label = "Sinh hoạt",
                            subLabel = "Chi gia đình",
                            icon = Icons.Default.Home,
                            color = OrangePayable,
                            containerColor = OrangePayableContainer,
                            onClick = onOpenFamilyExpenses,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // 3.1. BỘ CHỌN KỲ THỐNG KÊ DUY NHẤT + TÌNH HÌNH TIỀN BẠC
            item {
                val dashMetrics = remember(
                    effectiveSummary,
                    effectiveBathExchanges,
                    effectiveQuotaFees,
                    effectiveFamilyExpenses,
                    familyExpenses,
                    initialCapital,
                    isPeriodFlowMode,
                    activeScopeLabel
                ) {
                    val cattleActualCash = effectiveSummary.totalCashIn - effectiveSummary.totalCashOut
                    val cattleProfit = effectiveSummary.totalSaleAmount - effectiveSummary.totalPurchaseAmount
                    var myBathBaht = 0L
                    var myBathVnd = 0L
                    var theirBathActualVnd = 0L
                    var theirBathTheoreticalVnd = 0L
                    for (ex in effectiveBathExchanges) {
                        if (ex.direction == "VND_TO_THB") {
                            myBathBaht += ex.bahtAmount
                            myBathVnd += ex.vndAmount
                        } else if (ex.direction == "THB_TO_VND") {
                            val exCostVnd = when {
                                ex.costVndAmount > 0L -> ex.costVndAmount
                                ex.bahtAmount > 0L && ex.costRate > 0L -> ex.bahtAmount * ex.costRate
                                else -> 0L
                            }
                            myBathBaht += ex.bahtAmount
                            myBathVnd += exCostVnd
                            theirBathActualVnd += ex.vndAmount
                            theirBathTheoreticalVnd += if (ex.theoreticalVndAmount > 0L) ex.theoreticalVndAmount else ex.vndAmount
                        }
                    }
                    val totalQuotaFees = effectiveQuotaFees.sumOf { it.amount }
                    val totalQuotaCattle = effectiveQuotaFees.sumOf { it.cattleQuantity }
                    val totalFamilyExpenses = effectiveFamilyExpenses.sumOf { it.amount }
                    val currentMonthRange = DateFormatter.getPeriodRange(ReportPeriodType.THIS_MONTH, DateFormatter.todayNoonMillis())
                    val currentMonthLabel = if (isPeriodFlowMode) activeScopeLabel else "Tháng ${todayDate.monthValue}/${todayDate.year}"
                    val thisMonthExpenses = if (isPeriodFlowMode) {
                        effectiveFamilyExpenses
                    } else {
                        familyExpenses.filter { it.expenseDate in currentMonthRange.startMillis..currentMonthRange.endMillis }
                    }
                    val thisMonthFamilyExpenses = thisMonthExpenses.sumOf { it.amount }
                    val thisMonthFamilyExpenseCount = thisMonthExpenses.size
                    val bathActualProfit = theirBathActualVnd - myBathVnd
                    val bathTheoreticalProfit = theirBathTheoreticalVnd - myBathVnd
                    val bathDebt = (theirBathTheoreticalVnd - theirBathActualVnd).coerceAtLeast(0L)
                    val totalActualCash = cattleActualCash + bathActualProfit + totalQuotaFees
                    val totalCashReceivedAll = effectiveSummary.totalCashIn + theirBathActualVnd + totalQuotaFees
                    val totalCashSpentAll = effectiveSummary.totalCashOut + myBathVnd
                    val totalReceivableAll = effectiveSummary.totalReceivableDebt + bathDebt
                    val expectedTotalReceivedAll = totalCashReceivedAll + totalReceivableAll
                    val expectedTotalSpentAll = totalCashSpentAll + effectiveSummary.totalPayableDebt
                    val expectedProfitAll = expectedTotalReceivedAll - expectedTotalSpentAll
                    val currentCashActual = if (isPeriodFlowMode) {
                        totalActualCash
                    } else {
                        initialCapital + totalActualCash - totalFamilyExpenses
                    }
                    val currentCashTheoretical = if (isPeriodFlowMode) {
                        expectedProfitAll
                    } else {
                        initialCapital + expectedProfitAll - totalFamilyExpenses
                    }
                    DashboardComputedMetrics(
                        cattleActualCash = cattleActualCash,
                        cattleProfit = cattleProfit,
                        myBathBaht = myBathBaht,
                        myBathVnd = myBathVnd,
                        theirBathActualVnd = theirBathActualVnd,
                        theirBathTheoreticalVnd = theirBathTheoreticalVnd,
                        bathActualProfit = bathActualProfit,
                        bathTheoreticalProfit = bathTheoreticalProfit,
                        bathDebt = bathDebt,
                        totalQuotaFees = totalQuotaFees,
                        totalQuotaCattle = totalQuotaCattle,
                        totalFamilyExpenses = totalFamilyExpenses,
                        thisMonthFamilyExpenses = thisMonthFamilyExpenses,
                        thisMonthFamilyExpenseCount = thisMonthFamilyExpenseCount,
                        currentMonthLabel = currentMonthLabel,
                        totalCashReceivedAll = totalCashReceivedAll,
                        totalCashSpentAll = totalCashSpentAll,
                        totalActualCash = totalActualCash,
                        totalReceivableAll = totalReceivableAll,
                        expectedTotalReceivedAll = expectedTotalReceivedAll,
                        expectedTotalSpentAll = expectedTotalSpentAll,
                        expectedProfitAll = expectedProfitAll,
                        currentCashActual = currentCashActual,
                        currentCashTheoretical = currentCashTheoretical
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DashboardScopeBar(
                        selectedScopeType = selectedScopeType,
                        activeScopeLabel = activeScopeLabel,
                        isCurrentPeriod = isCurrentPeriod,
                        onSelectScopeType = { selectedScopeType = it },
                        onPrevPeriod = {
                            val d = when (selectedScopeType) {
                                ReportPeriodType.THIS_YEAR -> selectedDate.minusYears(1)
                                ReportPeriodType.THIS_MONTH -> selectedDate.minusMonths(1)
                                ReportPeriodType.THIS_WEEK -> selectedDate.minusWeeks(1)
                                ReportPeriodType.TODAY -> selectedDate.minusDays(1)
                                ReportPeriodType.CUSTOM -> selectedDate
                            }
                            selectedAnchorMillis = d
                                .atTime(12, 0)
                                .atZone(DateFormatter.VIETNAM_ZONE)
                                .toInstant()
                                .toEpochMilli()
                        },
                        onNextPeriod = {
                            val d = when (selectedScopeType) {
                                ReportPeriodType.THIS_YEAR -> selectedDate.plusYears(1)
                                ReportPeriodType.THIS_MONTH -> selectedDate.plusMonths(1)
                                ReportPeriodType.THIS_WEEK -> selectedDate.plusWeeks(1)
                                ReportPeriodType.TODAY -> selectedDate.plusDays(1)
                                ReportPeriodType.CUSTOM -> selectedDate
                            }
                            selectedAnchorMillis = d
                                .atTime(12, 0)
                                .atZone(DateFormatter.VIETNAM_ZONE)
                                .toInstant()
                                .toEpochMilli()
                        },
                        onResetToCurrent = {
                            selectedAnchorMillis = DateFormatter.todayNoonMillis()
                        }
                    )

                        var overviewExpanded by rememberSaveable { mutableStateOf(false) }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { overviewExpanded = !overviewExpanded },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceDarkCard),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (dashMetrics.currentCashActual >= 0L) GreenReceive.copy(alpha = 0.45f) else RedTransfer.copy(alpha = 0.45f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "TÌNH HÌNH TIỀN BẠC",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = CurrencyFormatter.formatVnd(dashMetrics.currentCashActual),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (dashMetrics.currentCashActual >= 0L) GreenReceive else RedTransfer,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                    Icon(
                                        imageVector = if (overviewExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = if (overviewExpanded) "Thu gọn" else "Mở rộng",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(visible = overviewExpanded) {
                            PlainLanguageOverviewCard(
                                metrics = dashMetrics,
                                initialCapital = initialCapital,
                                isMonthlyMode = isPeriodFlowMode,
                                scopeLabel = activeScopeLabel,
                                payableDebt = effectiveSummary.totalPayableDebt,
                                receivableGoodsDebt = effectiveSummary.totalReceivableDebt,
                                onEditInitialCapital = { showInitialCapitalDialog = true },
                                onClick = onOpenBackupAndReport
                            )
                        }
                }
            }

            // 3.4. THỐNG KÊ KINH DOANH & BIỂU ĐỒ TIẾNG VIỆT
            item {
                var statsExpanded by rememberSaveable { mutableStateOf(false) }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { statsExpanded = !statsExpanded }
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "THỐNG KÊ KINH DOANH",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Icon(
                                imageVector = if (statsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (statsExpanded) "Thu gọn" else "Mở rộng",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        AnimatedVisibility(visible = statsExpanded) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                val quotaRevenueInPeriod = effectiveQuotaFees.sumOf { it.amount }
                                val quotaCattleInPeriod = effectiveQuotaFees.sumOf { it.cattleQuantity }

                                VietnameseSimpleBarChart(stats = scopeStats)

                                HorizontalDivider()

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "• Mua hàng (${scopeStats.purchaseCattleCount} con):",
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = CurrencyFormatter.formatVnd(scopeStats.totalPurchaseAmount),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = BluePrimary,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "• Bán hàng (${scopeStats.saleCattleCount} con):",
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = CurrencyFormatter.formatVnd(scopeStats.totalSaleAmount),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = BluePrimaryDark,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "• Tiền nhận trong kỳ:",
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = CurrencyFormatter.formatVnd(scopeStats.totalCashIn),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = GreenReceive,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "• Tiền chuyển trong kỳ:",
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = CurrencyFormatter.formatVnd(scopeStats.totalCashOut),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = RedTransfer,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }

                                HorizontalDivider()

                                SimpleBarChart(
                                    title = "Biểu đồ Phí Quota",
                                    bars = listOf(
                                        Triple("Doanh thu phí Quota ($quotaCattleInPeriod con)", quotaRevenueInPeriod, GreenReceive)
                                    )
                                )

                                HorizontalDivider()

                                Text(
                                    text = "BIỂU ĐỒ ĐỔI BATH THÁI",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                val bathPeriodMyVnd = effectiveBathExchanges.sumOf { ex ->
                                    when (ex.direction) {
                                        "VND_TO_THB" -> ex.vndAmount
                                        "THB_TO_VND" -> when {
                                            ex.costVndAmount > 0L -> ex.costVndAmount
                                            ex.bahtAmount > 0L && ex.costRate > 0L -> ex.bahtAmount * ex.costRate
                                            else -> 0L
                                        }
                                        else -> 0L
                                    }
                                }
                                val bathPeriodTheirTheoretical = effectiveBathExchanges
                                    .filter { it.direction == "THB_TO_VND" }
                                    .sumOf { if (it.theoreticalVndAmount > 0L) it.theoreticalVndAmount else it.vndAmount }
                                val bathPeriodTheirActual = effectiveBathExchanges
                                    .filter { it.direction == "THB_TO_VND" }
                                    .sumOf { it.vndAmount }
                                val bathPeriodDebt = (bathPeriodTheirTheoretical - bathPeriodTheirActual).coerceAtLeast(0L)

                                SimpleBarChart(
                                    title = "Biểu đồ Đổi Bath",
                                    bars = listOf(
                                        Triple("Đã chuyển đi (VND)", bathPeriodMyVnd, RedTransfer),
                                        Triple("Tổng phải thu về", bathPeriodTheirTheoretical, BluePrimary),
                                        Triple("Tiền đã thu về", bathPeriodTheirActual, GreenReceive),
                                        Triple("Khách còn thiếu", bathPeriodDebt, OrangePayable)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 3.2. KHU VỰC CÔNG NỢ NỔI BẬT (ĐƯA XUỐNG DƯỚI THEO YÊU CẦU)
            item {
                var debtExpanded by rememberSaveable { mutableStateOf(false) }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { debtExpanded = !debtExpanded },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "CÔNG NỢ NỔI BẬT",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Icon(
                                imageVector = if (debtExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (debtExpanded) "Thu gọn" else "Mở rộng",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    AnimatedVisibility(visible = debtExpanded) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            // A. Công nợ phải thu
                            HighlightedDebtTableCard(
                                headerTitle = "A. CÔNG NỢ PHẢI THU",
                                totalDebtLabel = "Tổng phải thu (${receivableDebtors.size} người):",
                                totalDebtAmount = summary.totalReceivableDebt,
                                personCountLabel = "",
                                columnPersonHeader = "Đối tác",
                                columnAmountHeader = "Số tiền còn nợ",
                                items = receivableDebtors.take(5),
                                accentColor = GreenReceive,
                                headerBackgroundColor = GreenReceiveContainer,
                                emptyText = "Hiện không có công nợ phải thu.",
                                onViewAllClick = onOpenReceivableTab,
                                onPersonRowClick = onOpenPersonDebtDetail
                            )

                            // B. Công nợ phải trả
                            HighlightedDebtTableCard(
                                headerTitle = "B. CÔNG NỢ PHẢI TRẢ",
                                totalDebtLabel = "Tổng phải trả (${payableCreditors.size} người):",
                                totalDebtAmount = summary.totalPayableDebt,
                                personCountLabel = "",
                                columnPersonHeader = "Đối tác",
                                columnAmountHeader = "Số tiền còn nợ",
                                items = payableCreditors.take(5),
                                accentColor = OrangePayable,
                                headerBackgroundColor = OrangePayableContainer,
                                emptyText = "Hiện không có công nợ phải trả.",
                                onViewAllClick = onOpenPayableTab,
                                onPersonRowClick = onOpenPersonDebtDetail
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }

        ScrollToTopButton(
            listState = listState,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
    }

    if (showInitialCapitalDialog) {
        var draftCapital by remember(initialCapital) { mutableLongStateOf(initialCapital) }
        val capitalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showInitialCapitalDialog = false },
            sheetState = capitalSheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Khai báo vốn ban đầu",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Nhập số tiền vốn hiện đang có ban đầu để tính Tiền đang có và số dự kiến khi thu/trả hết nợ.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                CurrencyTextField(
                    label = "Vốn ban đầu (đ)",
                    amount = draftCapital,
                    onAmountChange = { draftCapital = it },
                    showVietnameseWords = true
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
                ) {
                    TextButton(onClick = { showInitialCapitalDialog = false }) {
                        Text("Hủy")
                    }
                    Button(
                        onClick = {
                            onUpdateInitialCapital(draftCapital)
                            showInitialCapitalDialog = false
                        }
                    ) {
                        Text("Lưu vốn")
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionTile(
    label: String,
    subLabel: String = "",
    icon: ImageVector,
    color: Color,
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = responsiveSp(12.5f),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        if (subLabel.isNotBlank()) {
            Text(
                text = subLabel,
                fontSize = responsiveSp(10.5f),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
@Composable
private fun HighlightedDebtTableCard(
    headerTitle: String,
    totalDebtLabel: String,
    totalDebtAmount: Long,
    personCountLabel: String,
    columnPersonHeader: String,
    columnAmountHeader: String,
    items: List<PersonDebtSummaryItem>,
    accentColor: Color,
    headerBackgroundColor: Color,
    emptyText: String,
    onViewAllClick: () -> Unit,
    onPersonRowClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Tiêu đề màu phân biệt rõ ràng, gọn 1 dòng
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerBackgroundColor)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = headerTitle,
                        fontSize = responsiveSp(13.5f),
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        maxLines = 1
                    )
                    if (personCountLabel.isNotBlank()) {
                        Text(
                            text = personCountLabel,
                            fontSize = responsiveSp(12.5f),
                            color = accentColor
                        )
                    }
                }
                TextButton(onClick = onViewAllClick) {
                    Text(
                        text = "Xem tất cả >",
                        fontSize = responsiveSp(12.5f),
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = totalDebtLabel,
                        fontSize = responsiveSp(14.5f),
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = CurrencyFormatter.formatVnd(totalDebtAmount),
                        fontSize = responsiveSp(14.5f),
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                HorizontalDivider()

                if (items.isEmpty()) {
                    Text(
                        text = emptyText,
                        fontSize = responsiveSp(13.5f),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                } else {
                    // Tiêu đề bảng
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = columnPersonHeader,
                            fontSize = responsiveSp(12.5f),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                        Text(
                            text = columnAmountHeader,
                            fontSize = responsiveSp(12.5f),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }

                    items.forEachIndexed { idx, person ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onPersonRowClick(person.contactId) }
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${idx + 1}. ${person.fullName}",
                                fontSize = responsiveSp(14f),
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = CurrencyFormatter.formatVnd(person.remainingDebt),
                                fontSize = responsiveSp(14f),
                                fontWeight = FontWeight.Bold,
                                color = accentColor,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VietnameseSimpleBarChart(stats: PeriodBusinessStats) {
    SimpleBarChart(
        title = "Biểu đồ Hàng hóa",
        bars = listOf(
            Triple("Tổng bán (cả nợ)", stats.totalSaleAmount, BluePrimary),
            Triple("Tiền đã thu vào", stats.totalCashIn, GreenReceive),
            Triple("Tổng mua (cả nợ)", stats.totalPurchaseAmount, OrangePayable),
            Triple("Tiền đã chi ra", stats.totalCashOut, RedTransfer)
        )
    )
}

@Composable
private fun SimpleBarChart(
    bars: List<Triple<String, Long, Color>>,
    title: String? = null
) {
    val maxVal = bars.maxOfOrNull { it.second }?.coerceAtLeast(1L) ?: 1L

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (!title.isNullOrBlank()) {
            Text(
                text = title,
                fontSize = responsiveSp(14f),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        bars.forEach { (label, amount, barColor) ->
            val progress = if (amount <= 0L) 0f else (amount.toFloat() / maxVal.toFloat()).coerceIn(0.03f, 1.0f)
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .clip(CircleShape)
                                .background(barColor)
                        )
                        Text(
                            text = label,
                            fontSize = responsiveSp(13.5f),
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = CurrencyFormatter.formatVnd(amount),
                        fontSize = responsiveSp(13.5f),
                        fontWeight = FontWeight.Bold,
                        color = if (amount > 0L) barColor else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                ) {
                    if (progress > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(5.dp))
                                .background(barColor)
                        )
                    }
                }
            }
        }
    }
}
@Composable
private fun RecentActivityRow(
    item: UnifiedActivityItem,
    onClick: () -> Unit
) {
    val badgeColor: Color
    val badgeBg: Color
    val amountText: String

    if (item.isTradeOrder) {
        val isSale = item.tradeOrderType == TradeOrderType.SALE
        badgeColor = if (isSale) BluePrimaryDark else BluePrimary
        badgeBg = BlueContainer
        amountText = CurrencyFormatter.formatVnd(item.amount)
    } else {
        val isCashIn = item.cashFlowType == CashFlowType.CASH_IN
        badgeColor = if (isCashIn) GreenReceive else RedTransfer
        badgeBg = if (isCashIn) GreenReceiveContainer else RedTransferContainer
        amountText = CurrencyFormatter.formatSignedVnd(item.amount, isPositive = isCashIn)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Dòng 1: Loại giao dịch & Số tiền
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SemanticBadge(
                text = item.titleLabel,
                textColor = badgeColor,
                backgroundColor = badgeBg
            )
            Text(
                text = amountText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = badgeColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Dòng 2: Họ tên người liên quan (tường minh 1 dòng riêng, không gộp)
        Text(
            text = "Đối tác: ${item.contactName}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Dòng 3: Ngày giao dịch (tường minh 1 dòng riêng)
        Text(
            text = "Ngày giao dịch: ${DateFormatter.formatDate(item.transactionDate)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Dòng 4: Nội dung chi tiết (tường minh 1 dòng riêng, bỏ mã #...)
        val cleanDesc = item.shortDescription.replace(Regex("\\s*#\\d+"), "").trim()
        if (cleanDesc.isNotBlank()) {
            Text(
                text = "Nội dung: $cleanDesc",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ItemOverviewCard(
    title: String,
    profitLabel: String,
    profit: Long,
    lines: List<Triple<String, String, Color>>,
    note: String? = null,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isPositive = profit >= 0L
    val accent = if (isPositive) GreenReceive else RedTransfer

    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, accent.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = profitLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = (if (profit > 0L) "+" else "") + CurrencyFormatter.formatVnd(profit),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = accent,
                    maxLines = 1,
                    softWrap = false
                )
            }
            HorizontalDivider(color = DividerDark.copy(alpha = 0.6f))
            lines.forEach { (label, value, valueColor) ->
                InfoLine(
                    label = label,
                    value = value,
                    valueColor = valueColor
                )
            }
            if (!note.isNullOrBlank()) {
                Text(
                    text = note,
                    style = MaterialTheme.typography.labelSmall,
                    color = OrangePayable
                )
            }
        }
    }
}

@Composable
private fun CapitalIndicatorCard(
    title: String,
    amount: Long,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isPositive = amount >= 0L
    val accent = if (isPositive) GreenReceive else RedTransfer

    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, accent.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = (if (amount > 0L) "+" else "") + CurrencyFormatter.formatVnd(amount),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = accent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private data class DashboardComputedMetrics(
    val cattleActualCash: Long,
    val cattleProfit: Long,
    val myBathBaht: Long,
    val myBathVnd: Long,
    val theirBathActualVnd: Long,
    val theirBathTheoreticalVnd: Long,
    val bathActualProfit: Long,
    val bathTheoreticalProfit: Long,
    val bathDebt: Long,
    val totalQuotaFees: Long,
    val totalQuotaCattle: Int,
    val totalFamilyExpenses: Long,
    val thisMonthFamilyExpenses: Long,
    val thisMonthFamilyExpenseCount: Int,
    val currentMonthLabel: String,
    val totalCashReceivedAll: Long,
    val totalCashSpentAll: Long,
    val totalActualCash: Long,
    val totalReceivableAll: Long,
    val expectedTotalReceivedAll: Long,
    val expectedTotalSpentAll: Long,
    val expectedProfitAll: Long,
    val currentCashActual: Long,
    val currentCashTheoretical: Long
)

@Composable
private fun DashboardScopeBar(
    selectedScopeType: ReportPeriodType,
    activeScopeLabel: String,
    isCurrentPeriod: Boolean,
    onSelectScopeType: (ReportPeriodType) -> Unit,
    onPrevPeriod: () -> Unit,
    onNextPeriod: () -> Unit,
    onResetToCurrent: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, DividerDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ScopeSegmentButton(
                    label = "Năm",
                    selected = selectedScopeType == ReportPeriodType.THIS_YEAR,
                    onClick = { onSelectScopeType(ReportPeriodType.THIS_YEAR) },
                    modifier = Modifier.weight(1f)
                )
                ScopeSegmentButton(
                    label = "Tháng",
                    selected = selectedScopeType == ReportPeriodType.THIS_MONTH,
                    onClick = { onSelectScopeType(ReportPeriodType.THIS_MONTH) },
                    modifier = Modifier.weight(1f)
                )
                ScopeSegmentButton(
                    label = "Tuần",
                    selected = selectedScopeType == ReportPeriodType.THIS_WEEK,
                    onClick = { onSelectScopeType(ReportPeriodType.THIS_WEEK) },
                    modifier = Modifier.weight(1f)
                )
                ScopeSegmentButton(
                    label = "Ngày",
                    selected = selectedScopeType == ReportPeriodType.TODAY,
                    onClick = { onSelectScopeType(ReportPeriodType.TODAY) },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onPrevPeriod,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Kỳ trước",
                        tint = BluePrimary
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = BluePrimary,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = activeScopeLabel,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!isCurrentPeriod) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Hiện tại",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = BluePrimary,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(BlueContainer.copy(alpha = 0.4f))
                                .clickable(onClick = onResetToCurrent)
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }
                IconButton(
                    onClick = onNextPeriod,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Kỳ sau",
                        tint = BluePrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun ScopeSegmentButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) BluePrimary else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}

@Composable
private fun PlainLanguageOverviewCard(
    metrics: DashboardComputedMetrics,
    initialCapital: Long,
    isMonthlyMode: Boolean,
    scopeLabel: String,
    payableDebt: Long,
    receivableGoodsDebt: Long,
    onEditInitialCapital: () -> Unit,
    onClick: () -> Unit
) {
    var showCashFormulaHelp by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkCard),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (metrics.currentCashActual >= 0L) GreenReceive.copy(alpha = 0.45f) else RedTransfer.copy(alpha = 0.45f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (isMonthlyMode) scopeLabel else "TIỀN ĐANG CẦM",
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = responsiveSp(12.5f),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!isMonthlyMode) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Giải thích cách tính tiền đang cầm",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .size(15.dp)
                                .clip(CircleShape)
                                .clickable { showCashFormulaHelp = true }
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = CurrencyFormatter.formatVnd(metrics.currentCashActual),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = responsiveSp(15f),
                    fontWeight = FontWeight.Bold,
                    color = if (metrics.currentCashActual >= 0L) GreenReceive else RedTransfer,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
            if (!isMonthlyMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onEditInitialCapital)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Vốn ban đầu",
                            fontSize = responsiveSp(14f),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Nhập vốn ban đầu",
                            tint = BluePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = CurrencyFormatter.formatVnd(initialCapital),
                        fontSize = responsiveSp(14f),
                        fontWeight = FontWeight.Bold,
                        color = BluePrimary
                    )
                }
            }

            HorizontalDivider(color = DividerDark.copy(alpha = 0.6f))
            Text(
                text = "1. NẾU THU & TRẢ HẾT NỢ (DỰ KIẾN)",
                fontSize = responsiveSp(12.5f),
                fontWeight = FontWeight.Bold,
                color = BluePrimary
            )
            InfoLine(
                label = "Tổng thu",
                value = CurrencyFormatter.formatVnd(metrics.expectedTotalReceivedAll),
                valueColor = MaterialTheme.colorScheme.onSurface
            )
            InfoLine(
                label = "Tổng chi",
                value = CurrencyFormatter.formatVnd(metrics.expectedTotalSpentAll),
                valueColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
            InfoLine(
                label = "Lời/Lỗ dự kiến",
                value = (if (metrics.expectedProfitAll > 0L) "+" else "") + CurrencyFormatter.formatVnd(metrics.expectedProfitAll),
                valueColor = if (metrics.expectedProfitAll >= 0L) GreenReceive else RedTransfer
            )
            if (initialCapital != 0L) {
                InfoLine(
                    label = "Tổng tiền dự kiến",
                    value = CurrencyFormatter.formatVnd(metrics.currentCashTheoretical),
                    valueColor = if (metrics.currentCashTheoretical >= 0L) GreenReceive else RedTransfer
                )
            }

            HorizontalDivider(color = DividerDark.copy(alpha = 0.6f))
            Text(
                text = "2. TIỀN ĐÃ QUA TAY (THỰC TẾ)",
                fontSize = responsiveSp(12.5f),
                fontWeight = FontWeight.Bold,
                color = BluePrimary
            )
            val goodsCashIn = (metrics.totalCashReceivedAll - metrics.theirBathActualVnd - metrics.totalQuotaFees).coerceAtLeast(0L)
            val goodsCashOut = (metrics.totalCashSpentAll - metrics.myBathVnd).coerceAtLeast(0L)
            InfoLine(
                label = "Đã thu vào",
                value = CurrencyFormatter.formatVnd(metrics.totalCashReceivedAll),
                valueColor = GreenReceive
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                SubInfoLine(
                    label = "• Hàng hóa",
                    value = CurrencyFormatter.formatVnd(goodsCashIn)
                )
                SubInfoLine(
                    label = "• Đổi Bath",
                    value = CurrencyFormatter.formatVnd(metrics.theirBathActualVnd)
                )
                SubInfoLine(
                    label = "• Phí Quota",
                    value = CurrencyFormatter.formatVnd(metrics.totalQuotaFees)
                )
            }
            InfoLine(
                label = "Đã chi ra",
                value = CurrencyFormatter.formatVnd(metrics.totalCashSpentAll),
                valueColor = RedTransfer
            )
            if (metrics.totalCashSpentAll > 0L) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    SubInfoLine(
                        label = "• Hàng hóa",
                        value = CurrencyFormatter.formatVnd(goodsCashOut)
                    )
                    SubInfoLine(
                        label = "• Đổi Bath",
                        value = CurrencyFormatter.formatVnd(metrics.myBathVnd)
                    )
                }
            }
            InfoLine(
                label = "Lời/Lỗ thực tế",
                value = (if (metrics.totalActualCash > 0L) "+" else "") + CurrencyFormatter.formatVnd(metrics.totalActualCash),
                valueColor = if (metrics.totalActualCash >= 0L) GreenReceive else RedTransfer
            )
            val remainingAfterLiving = metrics.totalActualCash - metrics.totalFamilyExpenses
            InfoLine(
                label = "Còn lại sau sinh hoạt",
                value = (if (remainingAfterLiving > 0L) "+" else "") + CurrencyFormatter.formatVnd(remainingAfterLiving),
                valueColor = if (remainingAfterLiving >= 0L) GreenReceive else RedTransfer
            )

            HorizontalDivider(color = DividerDark.copy(alpha = 0.6f))
            Text(
                text = "3. NỢ CHƯA THU / CHƯA TRẢ",
                fontSize = responsiveSp(12.5f),
                fontWeight = FontWeight.Bold,
                color = BluePrimary
            )
            InfoLine(
                label = "Công nợ phải thu",
                value = CurrencyFormatter.formatVnd(metrics.totalReceivableAll),
                valueColor = GreenReceive
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                SubInfoLine(
                    label = "• Hàng hóa",
                    value = CurrencyFormatter.formatVnd(receivableGoodsDebt)
                )
                SubInfoLine(
                    label = "• Đổi Bath",
                    value = CurrencyFormatter.formatVnd(metrics.bathDebt)
                )
                SubInfoLine(
                    label = "• Phí Quota",
                    value = "0 đ"
                )
            }
            InfoLine(
                label = "Công nợ phải trả",
                value = CurrencyFormatter.formatVnd(payableDebt),
                valueColor = OrangePayable
            )

            HorizontalDivider(color = DividerDark.copy(alpha = 0.6f))
            Text(
                text = "4. PHÍ SINH HOẠT",
                fontSize = responsiveSp(12.5f),
                fontWeight = FontWeight.Bold,
                color = BluePrimary
            )
            InfoLine(
                label = "Chi sinh hoạt",
                value = CurrencyFormatter.formatVnd(metrics.totalFamilyExpenses),
                valueColor = RedTransfer
            )
        }
    }

    if (showCashFormulaHelp) {
        AlertDialog(
            onDismissRequest = { showCashFormulaHelp = false },
            title = { Text("Cách tính tiền đang cầm", fontWeight = FontWeight.Bold) },
            text = {
                Text("Số tiền thật hiện có = Vốn ban đầu + Tổng tiền đã thu vào − Tổng tiền đã chi ra − Tổng phí sinh hoạt.")
            },
            confirmButton = {
                TextButton(onClick = { showCashFormulaHelp = false }) {
                    Text("Đã hiểu")
                }
            }
        )
    }
}

@Composable
private fun SubInfoLine(
    label: String,
    value: String,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = responsiveSp(13.5f),
            color = color
        )
        Text(
            text = value,
            fontSize = responsiveSp(13.5f),
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}
