package com.sotraubo.app.feature.trade

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sotraubo.app.core.common.format.CurrencyFormatter
import com.sotraubo.app.core.common.format.DateFormatter
import com.sotraubo.app.core.common.search.VietnameseTextNormalizer
import com.sotraubo.app.core.designsystem.component.DateSectionHeader
import com.sotraubo.app.core.designsystem.component.DropdownSelector
import com.sotraubo.app.core.designsystem.component.EmptyStateView
import com.sotraubo.app.core.designsystem.component.ScreenHelpButton
import com.sotraubo.app.core.designsystem.component.ScreenHelpGuides
import com.sotraubo.app.core.designsystem.component.InfoLine
import com.sotraubo.app.core.designsystem.component.ScrollToTopButton
import com.sotraubo.app.core.designsystem.theme.responsiveSp
import com.sotraubo.app.core.designsystem.component.TopBarPrimaryAction
import com.sotraubo.app.core.designsystem.component.SemanticBadge
import com.sotraubo.app.core.designsystem.component.TimeFilterMode
import com.sotraubo.app.core.designsystem.component.VietnameseTimeFilterBar
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
import com.sotraubo.app.core.designsystem.theme.SurfaceDarkCard
import com.sotraubo.app.core.model.CashFlowType
import com.sotraubo.app.core.model.CashTransactionWithContact
import com.sotraubo.app.core.model.TradeOrderType
import com.sotraubo.app.core.model.TradeOrderWithPayment

enum class MuaBanFilterType(val label: String) {
    ALL("Tất cả"),
    PURCHASE("Mua hàng"),
    SALE("Bán hàng"),
    CASH_IN("Nhận tiền"),
    CASH_OUT("Chuyển tiền")
}

private sealed interface MuaBanTimelineItem {
    val key: String
    val transactionDate: Long
    val createdAt: Long

    data class OrderItem(val order: TradeOrderWithPayment) : MuaBanTimelineItem {
        override val key: String = "ORDER_${order.id}"
        override val transactionDate: Long = order.transactionDate
        override val createdAt: Long = order.createdAt
    }

    data class CashItem(val cash: CashTransactionWithContact) : MuaBanTimelineItem {
        override val key: String = "CASH_${cash.id}"
        override val transactionDate: Long = cash.transactionDate
        override val createdAt: Long = cash.createdAt
    }
}

/**
 * Màn hình Mua bán hợp nhất (Đơn Mua/Bán trâu bò + Phiếu Nhận/Chuyển tiền):
 * - Gom toàn bộ hoạt động mua bán trâu bò về 1 tab duy nhất.
 * - Tất cả bộ lọc dùng Dropdown gọn gàng (Loại giao dịch + Thời gian).
 * - Đủ tính năng thêm/sửa/xem chi tiết cả 4 nghiệp vụ: Mua hàng, Bán hàng, Nhận tiền, Chuyển tiền.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuaBanScreen(
    orders: List<TradeOrderWithPayment>,
    cashTransactions: List<CashTransactionWithContact>,
    initialFilter: MuaBanFilterType = MuaBanFilterType.ALL,
    onAddPurchase: () -> Unit,
    onAddSale: () -> Unit,
    onAddCashIn: () -> Unit,
    onAddCashOut: () -> Unit,
    onOrderClick: (Long) -> Unit,
    onCashClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by rememberSaveable(initialFilter) { mutableStateOf(initialFilter) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var timeFilterMode by rememberSaveable { mutableStateOf(TimeFilterMode.ALL) }
    var timeAnchorMillis by rememberSaveable { mutableLongStateOf(DateFormatter.todayNoonMillis()) }
    var showCreatePicker by remember { mutableStateOf(false) }

    fun matchesTime(dateMillis: Long): Boolean = when (timeFilterMode) {
        TimeFilterMode.ALL -> true
        TimeFilterMode.BY_MONTH -> DateFormatter.isSameMonth(dateMillis, timeAnchorMillis)
        TimeFilterMode.BY_DAY -> DateFormatter.isSameDay(dateMillis, timeAnchorMillis)
        TimeFilterMode.TODAY -> DateFormatter.isSameDay(dateMillis, DateFormatter.todayNoonMillis())
    }

    val timeFilteredOrders = remember(orders, timeFilterMode, timeAnchorMillis) {
        orders.filter { matchesTime(it.transactionDate) }
    }
    val timeFilteredCash = remember(cashTransactions, timeFilterMode, timeAnchorMillis) {
        cashTransactions.filter { matchesTime(it.transactionDate) }
    }

    val timelineItems = remember(
        timeFilteredOrders,
        timeFilteredCash,
        selectedFilter,
        searchQuery
    ) {
        val merged = buildList {
            if (selectedFilter == MuaBanFilterType.ALL ||
                selectedFilter == MuaBanFilterType.PURCHASE ||
                selectedFilter == MuaBanFilterType.SALE
            ) {
                timeFilteredOrders
                    .filter { order ->
                        when (selectedFilter) {
                            MuaBanFilterType.PURCHASE -> order.orderType == TradeOrderType.PURCHASE
                            MuaBanFilterType.SALE -> order.orderType == TradeOrderType.SALE
                            else -> true
                        }
                    }
                    .filter { order ->
                        searchQuery.isBlank() ||
                            VietnameseTextNormalizer.matchesQuery(order.contactName, order.contactPhone, searchQuery) ||
                            VietnameseTextNormalizer.matchesQuery(order.description, null, searchQuery) ||
                            VietnameseTextNormalizer.matchesQuery(order.note, null, searchQuery)
                    }
                    .forEach { add(MuaBanTimelineItem.OrderItem(it)) }
            }

            if (selectedFilter == MuaBanFilterType.ALL ||
                selectedFilter == MuaBanFilterType.CASH_IN ||
                selectedFilter == MuaBanFilterType.CASH_OUT
            ) {
                timeFilteredCash
                    .filter { cash ->
                        when (selectedFilter) {
                            MuaBanFilterType.CASH_IN -> cash.flowType == CashFlowType.CASH_IN
                            MuaBanFilterType.CASH_OUT -> cash.flowType == CashFlowType.CASH_OUT
                            else -> true
                        }
                    }
                    .filter { cash ->
                        searchQuery.isBlank() ||
                            VietnameseTextNormalizer.matchesQuery(cash.contactName, cash.contactPhone, searchQuery) ||
                            VietnameseTextNormalizer.matchesQuery(cash.shortContent, null, searchQuery) ||
                            VietnameseTextNormalizer.matchesQuery(cash.note, null, searchQuery)
                    }
                    .forEach { add(MuaBanTimelineItem.CashItem(it)) }
            }
        }
        merged.sortedWith(
            compareByDescending<MuaBanTimelineItem> { it.transactionDate }
                .thenByDescending { it.createdAt }
        )
    }

    val groupedByDate = remember(timelineItems) {
        timelineItems.groupBy { DateFormatter.formatFriendlyDateHeader(it.transactionDate) }
    }

    val totalPurchase = timeFilteredOrders
        .filter { it.orderType == TradeOrderType.PURCHASE }
        .sumOf { it.totalAmount }
    val totalSale = timeFilteredOrders
        .filter { it.orderType == TradeOrderType.SALE }
        .sumOf { it.totalAmount }
    val totalCashIn = timeFilteredCash
        .filter { it.flowType == CashFlowType.CASH_IN }
        .sumOf { it.amount }
    val totalCashOut = timeFilteredCash
        .filter { it.flowType == CashFlowType.CASH_OUT }
        .sumOf { it.amount }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sổ Mua Bán & Thu Chi",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                },
                actions = {
                    ScreenHelpButton(guide = ScreenHelpGuides.MUA_BAN)
                    TopBarPrimaryAction(
                        label = "Ghi sổ",
                        onClick = { showCreatePicker = true }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        val listState = rememberLazyListState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item(key = "muaban_header") {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    text = "Tìm tên, SĐT, mô tả...",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Xóa tìm kiếm")
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Bộ lọc dạng Dropdown gọn gàng
                        DropdownSelector(
                            prefix = "Loại",
                            options = MuaBanFilterType.entries,
                            selected = selectedFilter,
                            optionLabel = { it.label },
                            onSelected = { selectedFilter = it }
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        VietnameseTimeFilterBar(
                            mode = timeFilterMode,
                            anchorMillis = timeAnchorMillis,
                            onModeChange = { timeFilterMode = it },
                            onAnchorMillisChange = { timeAnchorMillis = it }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceDarkCard),
                            border = BorderStroke(1.dp, DividerDark)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                InfoLine("Tổng mua hàng", CurrencyFormatter.formatVnd(totalPurchase), valueColor = BluePrimary)
                                InfoLine("Tổng bán hàng", CurrencyFormatter.formatVnd(totalSale), valueColor = BluePrimaryDark)
                                InfoLine("Tiền đã nhận", CurrencyFormatter.formatVnd(totalCashIn), valueColor = GreenReceive)
                                InfoLine("Tiền đã chuyển", CurrencyFormatter.formatVnd(totalCashOut), valueColor = RedTransfer)
                            }
                        }
                    }
                }

                if (timelineItems.isEmpty()) {
                    item(key = "muaban_empty") {
                        EmptyStateView(
                            title = "Chưa có hoạt động mua bán nào",
                            message = "Bấm nút \"+ Ghi sổ\" ở góc trên để thêm đơn mua/bán hoặc phiếu thu/chi tiền.",
                            scrollable = false
                        )
                    }
                } else {
                    groupedByDate.forEach { (dateTitle, itemsInDay) ->
                        item(key = "header_$dateTitle") {
                            DateSectionHeader(
                                dateMillis = itemsInDay.first().transactionDate,
                                countLabel = "${itemsInDay.size} mục"
                            )
                        }
                        items(
                            items = itemsInDay,
                            key = { it.key }
                        ) { item ->
                            when (item) {
                                is MuaBanTimelineItem.OrderItem -> {
                                    UnifiedTradeOrderCard(
                                        order = item.order,
                                        onClick = { onOrderClick(item.order.id) }
                                    )
                                }
                                is MuaBanTimelineItem.CashItem -> {
                                    UnifiedCashCard(
                                        cash = item.cash,
                                        onClick = { onCashClick(item.cash.id) }
                                    )
                                }
                            }
                        }
                    }
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

    if (showCreatePicker) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showCreatePicker = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Chọn loại ghi sổ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                CreateOptionRow(
                    title = "Mua hàng (Nhập trâu/bò)",
                    subtitle = "Ghi số lượng con, tổng tiền và phần đã thanh toán",
                    icon = Icons.Default.ShoppingCart,
                    iconColor = BluePrimary,
                    containerColor = BlueContainer,
                    onClick = {
                        showCreatePicker = false
                        onAddPurchase()
                    }
                )
                CreateOptionRow(
                    title = "Bán hàng (Xuất trâu/bò)",
                    subtitle = "Ghi số lượng con, tổng tiền và phần khách đã trả",
                    icon = Icons.Default.Storefront,
                    iconColor = BluePrimaryDark,
                    containerColor = BlueContainer,
                    onClick = {
                        showCreatePicker = false
                        onAddSale()
                    }
                )
                CreateOptionRow(
                    title = "Nhận tiền (Thu tiền / Thu nợ)",
                    subtitle = "Ghi nhận tiền mặt hoặc chuyển khoản thu vào",
                    icon = Icons.Default.ArrowDownward,
                    iconColor = GreenReceive,
                    containerColor = GreenReceiveContainer,
                    onClick = {
                        showCreatePicker = false
                        onAddCashIn()
                    }
                )
                CreateOptionRow(
                    title = "Chuyển tiền (Chi tiền / Trả nợ)",
                    subtitle = "Ghi nhận tiền mặt hoặc chuyển khoản chi ra",
                    icon = Icons.Default.ArrowUpward,
                    iconColor = RedTransfer,
                    containerColor = RedTransferContainer,
                    onClick = {
                        showCreatePicker = false
                        onAddCashOut()
                    }
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun CreateOptionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    containerColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = SurfaceDarkCard,
        border = BorderStroke(1.dp, DividerDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = containerColor,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconColor)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun UnifiedTradeOrderCard(
    order: TradeOrderWithPayment,
    onClick: () -> Unit
) {
    val isPurchase = order.orderType == TradeOrderType.PURCHASE
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkCard),
        border = BorderStroke(1.dp, DividerDark)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SemanticBadge(
                    text = if (isPurchase) "Mua hàng • ${order.cattleQuantity} con" else "Bán hàng • ${order.cattleQuantity} con",
                    textColor = if (isPurchase) BluePrimary else BluePrimaryDark,
                    backgroundColor = BlueContainer
                )
                Text(
                    text = CurrencyFormatter.formatVnd(order.totalAmount),
                    fontSize = responsiveSp(15f),
                    fontWeight = FontWeight.Bold,
                    color = if (isPurchase) BluePrimary else BluePrimaryDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = order.contactName,
                fontSize = responsiveSp(14f),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (order.description.isNotBlank()) {
                Text(
                    text = order.description,
                    fontSize = responsiveSp(13f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Đã thanh toán: ${CurrencyFormatter.formatVnd(order.paidAmount)}",
                    fontSize = responsiveSp(13f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (order.remainingDebt > 0L) {
                    SemanticBadge(
                        text = if (isPurchase) "Còn nợ: ${CurrencyFormatter.formatVnd(order.remainingDebt)}"
                        else "Khách nợ: ${CurrencyFormatter.formatVnd(order.remainingDebt)}",
                        textColor = if (isPurchase) OrangePayable else GreenReceive,
                        backgroundColor = if (isPurchase) OrangePayableContainer else GreenReceiveContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun UnifiedCashCard(
    cash: CashTransactionWithContact,
    onClick: () -> Unit
) {
    val isCashIn = cash.flowType == CashFlowType.CASH_IN
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkCard),
        border = BorderStroke(1.dp, DividerDark)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SemanticBadge(
                    text = if (isCashIn) "Nhận tiền" else "Chuyển tiền",
                    textColor = if (isCashIn) GreenReceive else RedTransfer,
                    backgroundColor = if (isCashIn) GreenReceiveContainer else RedTransferContainer
                )
                Text(
                    text = (if (isCashIn) "+" else "-") + CurrencyFormatter.formatVnd(cash.amount),
                    fontSize = responsiveSp(15f),
                    fontWeight = FontWeight.Bold,
                    color = if (isCashIn) GreenReceive else RedTransfer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = cash.contactName,
                fontSize = responsiveSp(14f),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            val methodText = if (cash.paymentMethod == com.sotraubo.app.core.model.PaymentMethod.CASH) "Tiền mặt" else "Chuyển khoản"
            val detail = cash.shortContent.ifBlank { methodText }
            Text(
                text = detail,
                fontSize = responsiveSp(13f),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
