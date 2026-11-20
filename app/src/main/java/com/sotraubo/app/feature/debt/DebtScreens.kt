package com.sotraubo.app.feature.debt

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.sotraubo.app.core.designsystem.component.DropdownSelector
import com.sotraubo.app.core.designsystem.component.TimeFilterMode
import com.sotraubo.app.core.designsystem.component.VietnameseTimePeriodStepper
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sotraubo.app.core.common.format.CurrencyFormatter
import com.sotraubo.app.core.common.format.DateFormatter
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity
import com.sotraubo.app.core.designsystem.component.DropdownSelector
import com.sotraubo.app.core.designsystem.component.EmptyStateView
import com.sotraubo.app.core.designsystem.component.ScreenHelpButton
import com.sotraubo.app.core.designsystem.component.ScreenHelpGuides
import com.sotraubo.app.core.designsystem.component.ScrollToTopButton
import com.sotraubo.app.core.designsystem.component.SemanticBadge
import com.sotraubo.app.core.designsystem.theme.GreenReceive
import com.sotraubo.app.core.designsystem.theme.GreenReceiveContainer
import com.sotraubo.app.core.designsystem.theme.OrangePayable
import com.sotraubo.app.core.designsystem.theme.OrangePayableContainer
import com.sotraubo.app.core.designsystem.theme.responsiveSp
import com.sotraubo.app.core.model.CashTransactionWithContact
import com.sotraubo.app.core.model.DebtSortOption
import com.sotraubo.app.core.model.PersonBidirectionalDebtDetail
import com.sotraubo.app.core.model.PersonDebtSummaryItem
import com.sotraubo.app.core.model.TradeOrderWithPayment

/**
 * Màn hình Quản lý Công nợ chi tiết (4.1, 4.2, 4.5 Master Prompt):
 * - Tab 1: Người khác nợ mình (chỉ hiển thị người có dư nợ phải thu > 0).
 * - Tab 2: Mình nợ người khác (chỉ hiển thị người có dư nợ phải trả > 0).
 * - Ô tìm kiếm nhanh ở phía trên (theo tên có dấu/không dấu và SĐT).
 * - Sắp xếp theo Số tiền nợ từ cao xuống thấp hoặc Thời gian giao dịch gần nhất.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtOverviewScreen(
    initialTab: Int = 0,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    sortOption: DebtSortOption,
    onSortOptionChange: (DebtSortOption) -> Unit,
    receivableDebtors: List<PersonDebtSummaryItem>,
    payableCreditors: List<PersonDebtSummaryItem>,
    bathExchanges: List<CurrencyExchangeEntity> = emptyList(),
    onOpenBathExchange: () -> Unit = {},
    onPersonClick: (Long) -> Unit,
    onQuickCollectDebt: (Long) -> Unit,
    onQuickPayDebt: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable(initialTab) { mutableIntStateOf(initialTab) }
    var timeFilterMode by rememberSaveable { mutableStateOf(TimeFilterMode.ALL) }
    var timeAnchorMillis by rememberSaveable { mutableLongStateOf(DateFormatter.todayNoonMillis()) }

    val totalReceivable = receivableDebtors.sumOf { it.remainingDebt }
    val totalPayable = payableCreditors.sumOf { it.remainingDebt }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Quản lý Công nợ", fontWeight = FontWeight.Bold) },
                actions = {
                    ScreenHelpButton(guide = ScreenHelpGuides.DEBT_OVERVIEW)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        val isReceivableTab = (selectedTab == 0)
        val activeList = remember(
            isReceivableTab, receivableDebtors, payableCreditors, timeFilterMode, timeAnchorMillis
        ) {
            (if (isReceivableTab) receivableDebtors else payableCreditors).filter { p ->
                when (timeFilterMode) {
                    TimeFilterMode.ALL -> true
                    TimeFilterMode.BY_MONTH -> DateFormatter.isSameMonth(p.lastTransactionDate, timeAnchorMillis)
                    TimeFilterMode.BY_DAY -> DateFormatter.isSameDay(p.lastTransactionDate, timeAnchorMillis)
                    TimeFilterMode.TODAY -> DateFormatter.isSameDay(p.lastTransactionDate, DateFormatter.todayNoonMillis())
                }
            }
        }
        val totalAmount = activeList.sumOf { it.remainingDebt }
        val bathDebtItems = remember(isReceivableTab, bathExchanges, timeFilterMode, timeAnchorMillis, searchQuery) {
            if (!isReceivableTab) {
                emptyList()
            } else {
                val queryTrim = searchQuery.trim().lowercase()
                bathExchanges.filter { ex ->
                    val debt = if (ex.direction == "THB_TO_VND") {
                        (ex.theoreticalVndAmount - ex.vndAmount).coerceAtLeast(0L)
                    } else {
                        0L
                    }
                    val matchesTime = when (timeFilterMode) {
                        TimeFilterMode.ALL -> true
                        TimeFilterMode.BY_MONTH -> DateFormatter.isSameMonth(ex.exchangeDate, timeAnchorMillis)
                        TimeFilterMode.BY_DAY -> DateFormatter.isSameDay(ex.exchangeDate, timeAnchorMillis)
                        TimeFilterMode.TODAY -> DateFormatter.isSameDay(ex.exchangeDate, DateFormatter.todayNoonMillis())
                    }
                    val matchesQuery = queryTrim.isBlank() ||
                        ex.partnerName.lowercase().contains(queryTrim) ||
                        "bath".contains(queryTrim)
                    debt > 0L && matchesTime && matchesQuery
                }
            }
        }
        val totalBathReceivable = bathDebtItems.sumOf { (it.theoreticalVndAmount - it.vndAmount).coerceAtLeast(0L) }
        val displayedTotalAmount = if (isReceivableTab) totalAmount + totalBathReceivable else totalAmount
        val accentColor = if (isReceivableTab) GreenReceive else OrangePayable
        val containerColor = if (isReceivableTab) GreenReceiveContainer else OrangePayableContainer
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
                contentPadding = PaddingValues(top = 8.dp, bottom = 84.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item(key = "debt_header") {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        DropdownSelector(
                            prefix = "Loại công nợ",
                            options = listOf(0, 1),
                            selected = selectedTab,
                            optionLabel = { tab ->
                                if (tab == 0) "Phải thu (${receivableDebtors.size})"
                                else "Phải trả (${payableCreditors.size})"
                            },
                            onSelected = { selectedTab = it }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Ô tìm kiếm nhanh bắt buộc ở phía trên danh sách
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            label = { Text("Tìm tên hoặc SĐT...", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Tìm kiếm") },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { onSearchQueryChange("") }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Xóa tìm kiếm")
                                    }
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Sắp xếp + Lọc thời gian cùng 1 hàng gọn để nhường không gian cho danh sách
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DropdownSelector(
                                prefix = "Xếp",
                                options = DebtSortOption.entries.toList(),
                                selected = sortOption,
                                optionLabel = {
                                    when (it) {
                                        DebtSortOption.AMOUNT_DESC -> "Cao → thấp"
                                        DebtSortOption.RECENT_DATE_DESC -> "Mới nhất"
                                    }
                                },
                                onSelected = onSortOptionChange,
                                modifier = Modifier.weight(1f)
                            )
                            DropdownSelector(
                                prefix = "Lọc",
                                options = TimeFilterMode.entries.toList(),
                                selected = timeFilterMode,
                                optionLabel = { it.label },
                                onSelected = { itemMode ->
                                    if (itemMode == TimeFilterMode.TODAY) {
                                        timeAnchorMillis = DateFormatter.todayNoonMillis()
                                    }
                                    timeFilterMode = itemMode
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (timeFilterMode == TimeFilterMode.BY_MONTH || timeFilterMode == TimeFilterMode.BY_DAY) {
                            Spacer(modifier = Modifier.height(6.dp))
                            VietnameseTimePeriodStepper(
                                mode = timeFilterMode,
                                anchorMillis = timeAnchorMillis,
                                onAnchorMillisChange = { timeAnchorMillis = it }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Hộp tổng hợp đầu tab (ưu tiên inline 1 dòng)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(containerColor)
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isReceivableTab) {
                                    if (bathDebtItems.isNotEmpty()) {
                                        "Tổng phải thu (${activeList.size + bathDebtItems.size} khoản):"
                                    } else {
                                        "Tổng phải thu (${activeList.size} người):"
                                    }
                                } else {
                                    "Tổng phải trả (${activeList.size} người):"
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = responsiveSp(13f)),
                                fontWeight = FontWeight.Bold,
                                color = accentColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = CurrencyFormatter.formatVnd(displayedTotalAmount),
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = responsiveSp(15f),
                                fontWeight = FontWeight.Bold,
                                color = accentColor,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                if (activeList.isEmpty() && (!isReceivableTab || bathDebtItems.isEmpty())) {
                    item(key = "debt_empty") {
                        EmptyStateView(
                            title = if (searchQuery.isNotBlank()) {
                                "Không tìm thấy người nào phù hợp"
                            } else if (isReceivableTab) {
                                "Hiện không có ai đang nợ tiền bạn"
                            } else {
                                "Hiện bạn không còn nợ tiền ai"
                            },
                            message = if (searchQuery.isNotBlank()) {
                                "Không có người đang nợ nào khớp với từ khóa \"$searchQuery\"."
                            } else {
                                "Những khoản nợ đã thanh toán hết (0 đ) được lưu trữ an toàn trong Danh bạ và Lịch sử giao dịch."
                            },
                            scrollable = false
                        )
                    }
                } else {
                    items(
                        items = activeList,
                        key = { "${selectedTab}_${it.contactId}" }
                    ) { item ->
                        DebtPersonRowCard(
                            item = item,
                            isReceivable = isReceivableTab,
                            onCardClick = { onPersonClick(item.contactId) },
                            onActionClick = {
                                if (isReceivableTab) {
                                    onQuickCollectDebt(item.contactId)
                                } else {
                                    onQuickPayDebt(item.contactId)
                                }
                            }
                        )
                    }
                    if (isReceivableTab && bathDebtItems.isNotEmpty()) {
                        items(
                            items = bathDebtItems,
                            key = { "bath_debt_${it.id}" }
                        ) { ex ->
                            BathDebtRowCard(
                                exchange = ex,
                                onOpenBath = onOpenBathExchange
                            )
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
}

@Composable
private fun BathDebtRowCard(
    exchange: CurrencyExchangeEntity,
    onOpenBath: () -> Unit
) {
    val debt = (exchange.theoreticalVndAmount - exchange.vndAmount).coerceAtLeast(0L)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenBath),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = GreenReceive
                )
                Text(
                    text = exchange.partnerName.ifBlank { "Khách đổi Bath" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                SemanticBadge(
                    text = "Nợ Bath",
                    textColor = GreenReceive,
                    backgroundColor = GreenReceiveContainer
                )
            }
            Text(
                text = "Số Bath: ${CurrencyFormatter.formatNumberOnly(exchange.bahtAmount)} ฿",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Đã thu: ${CurrencyFormatter.formatVnd(exchange.vndAmount)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Ngày giao dịch: ${DateFormatter.formatDate(exchange.exchangeDate)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Còn nợ mình (Bath):",
                        style = MaterialTheme.typography.labelMedium,
                        color = GreenReceive,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = CurrencyFormatter.formatVnd(debt),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GreenReceive,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                OutlinedButton(
                    onClick = onOpenBath,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenReceive)
                ) {
                    Text(
                        text = "Thu tiền",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

@Composable
private fun DebtPersonRowCard(
    item: PersonDebtSummaryItem,
    isReceivable: Boolean,
    onCardClick: () -> Unit,
    onActionClick: () -> Unit
) {
    val accentColor = if (isReceivable) GreenReceive else OrangePayable

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Dòng 1: Họ tên người liên quan (tường minh 1 dòng riêng)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = accentColor
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = item.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Dòng 2: Số điện thoại (tường minh 1 dòng riêng)
            if (item.phone.isNotBlank()) {
                Text(
                    text = "Số điện thoại: ${item.phone}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Dòng 3: Số đơn liên quan (tường minh 1 dòng riêng)
            Text(
                text = if (isReceivable) {
                    "Số đơn liên quan: ${item.relatedOrderCount} lần bán hàng"
                } else {
                    "Số đơn liên quan: ${item.relatedOrderCount} lần mua hàng"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Dòng 4: Ngày giao dịch gần nhất (tường minh 1 dòng riêng)
            if (item.lastTransactionDate > 0L) {
                Text(
                    text = "Giao dịch gần nhất: ${DateFormatter.formatDate(item.lastTransactionDate)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Dòng 5: Số tiền còn nợ & Nút hành động
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isReceivable) "Còn nợ mình:" else "Mình còn nợ:",
                        style = MaterialTheme.typography.labelMedium,
                        color = accentColor
                    )
                    Text(
                        text = CurrencyFormatter.formatVnd(item.remainingDebt),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = responsiveSp(15f),
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = onActionClick,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (isReceivable) "Thu tiền" else "Trả tiền",
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

/**
 * Màn hình Chi tiết Công nợ của một người (4.3 Master Prompt):
 * - Nếu một người vừa nợ mình vừa là người mình nợ, hiển thị thành HAI PHẦN RIÊNG BIỆT:
 *   1. Người đó còn nợ mình.
 *   2. Mình còn nợ người đó.
 * - Tuyệt đối không tự động bù trừ hai khoản nợ.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDebtDetailScreen(
    detail: PersonBidirectionalDebtDetail,
    onBack: () -> Unit,
    onCollectReceivableClick: (preselectedOrderId: Long?) -> Unit,
    onPayPayableClick: (preselectedOrderId: Long?) -> Unit,
    onExportPdfClick: () -> Unit,
    onOrderClick: (Long) -> Unit,
    onCashTransactionClick: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết công nợ", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    ScreenHelpButton(guide = ScreenHelpGuides.PERSON_DEBT_DETAIL)
                    IconButton(onClick = onExportPdfClick) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = "Xuất phiếu đối chiếu PDF",
                            tint = Color.White
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Thông tin người liên quan
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = detail.contact.fullName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (detail.contact.phone.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Số điện thoại: ${detail.contact.phone}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        if (detail.currentReceivableDebt > 0L && detail.currentPayableDebt > 0L) {
                            Spacer(modifier = Modifier.height(8.dp))
                            SemanticBadge(
                                text = "Đang có công nợ ở cả 2 chiều (Hiển thị tách biệt, không tự động bù trừ)",
                                textColor = OrangePayable,
                                backgroundColor = OrangePayableContainer
                            )
                        }
                    }
                }
            }

            // PHẦN 1: NGƯỜI ĐÓ CÒN NỢ MÌNH (PHẢI THU)
            item {
                DebtDirectionSectionCard(
                    sectionTitle = "1. ${detail.contact.fullName.uppercase()} CÒN NỢ MÌNH",
                    accentColor = GreenReceive,
                    containerColor = GreenReceiveContainer,
                    initialLabel = "Tổng tiền bán (Nợ gốc)",
                    initialTotal = detail.initialReceivableTotal,
                    paidLabel = "Đã thanh toán (Đã nhận)",
                    paidTotal = detail.paidReceivableTotal,
                    currentDebtLabel = "CÒN NỢ HIỆN TẠI",
                    currentDebt = detail.currentReceivableDebt,
                    actionLabel = "Thu nợ từ ${detail.contact.fullName}",
                    onActionClick = { onCollectReceivableClick(null) },
                    ordersHeader = "Đơn bán hàng (${detail.saleOrders.size})",
                    orders = detail.saleOrders,
                    orderTotalLabel = "Tổng bán",
                    orderPaidLabel = "Đã nhận",
                    orderDebtLabel = "Còn nợ",
                    onOrderClick = onOrderClick,
                    cashHeader = "Lịch sử thu tiền (${detail.linkedCashInTransactions.size})",
                    cashTransactions = detail.linkedCashInTransactions,
                    cashAmountPrefix = "+",
                    onCashTransactionClick = onCashTransactionClick
                )
            }

            // PHẦN 2: MÌNH CÒN NỢ NGƯỜI ĐÓ (PHẢI TRẢ)
            item {
                DebtDirectionSectionCard(
                    sectionTitle = "2. MÌNH CÒN NỢ ${detail.contact.fullName.uppercase()}",
                    accentColor = OrangePayable,
                    containerColor = OrangePayableContainer,
                    initialLabel = "Tổng tiền mua (Nợ gốc)",
                    initialTotal = detail.initialPayableTotal,
                    paidLabel = "Đã thanh toán (Đã trả)",
                    paidTotal = detail.paidPayableTotal,
                    currentDebtLabel = "MÌNH CÒN NỢ HIỆN TẠI",
                    currentDebt = detail.currentPayableDebt,
                    actionLabel = "Trả nợ cho ${detail.contact.fullName}",
                    onActionClick = { onPayPayableClick(null) },
                    ordersHeader = "Đơn mua hàng (${detail.purchaseOrders.size})",
                    orders = detail.purchaseOrders,
                    orderTotalLabel = "Tổng mua",
                    orderPaidLabel = "Đã trả",
                    orderDebtLabel = "Mình nợ",
                    onOrderClick = onOrderClick,
                    cashHeader = "Lịch sử trả tiền (${detail.linkedCashOutTransactions.size})",
                    cashTransactions = detail.linkedCashOutTransactions,
                    cashAmountPrefix = "-",
                    onCashTransactionClick = onCashTransactionClick
                )
            }

            // Lịch sử thay đổi công nợ
            if (detail.debtTimeline.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "LỊCH SỬ THAY ĐỔI CÔNG NỢ (${detail.debtTimeline.size} mốc)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            detail.debtTimeline.forEach { ev ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${DateFormatter.formatDate(ev.dateMillis)} • ${ev.title}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = ev.description,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    val prefix = if (ev.isIncreaseDebt) "Phát sinh " else "Thanh toán "
                                    Text(
                                        text = "$prefix${CurrencyFormatter.formatVnd(ev.amount)}",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = if (ev.isReceivableSide) GreenReceive else OrangePayable
                                    )
                                }
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, amount: Long) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = CurrencyFormatter.formatVnd(amount),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
private fun DebtDirectionSectionCard(
    sectionTitle: String,
    accentColor: Color,
    containerColor: Color,
    initialLabel: String,
    initialTotal: Long,
    paidLabel: String,
    paidTotal: Long,
    currentDebtLabel: String,
    currentDebt: Long,
    actionLabel: String,
    onActionClick: () -> Unit,
    ordersHeader: String,
    orders: List<TradeOrderWithPayment>,
    orderTotalLabel: String,
    orderPaidLabel: String,
    orderDebtLabel: String,
    onOrderClick: (Long) -> Unit,
    cashHeader: String,
    cashTransactions: List<CashTransactionWithContact>,
    cashAmountPrefix: String,
    onCashTransactionClick: (Long) -> Unit
) {
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
                    .background(containerColor)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sectionTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            SummaryRow("$initialLabel:", initialTotal)
            SummaryRow("$paidLabel:", paidTotal)
            HorizontalDivider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$currentDebtLabel:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = CurrencyFormatter.formatVnd(currentDebt),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = responsiveSp(15f),
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (currentDebt > 0L) {
                Button(
                    onClick = onActionClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text(
                        text = actionLabel,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (orders.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = ordersHeader,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                orders.forEach { order ->
                    DebtOrderItemCard(
                        order = order,
                        accentColor = accentColor,
                        totalLabel = orderTotalLabel,
                        paidLabel = orderPaidLabel,
                        debtLabel = orderDebtLabel,
                        onClick = { onOrderClick(order.id) }
                    )
                }
            }

            if (cashTransactions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = cashHeader,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                cashTransactions.forEach { tx ->
                    LinkedCashItemRow(
                        tx = tx,
                        accentColor = accentColor,
                        amountPrefix = cashAmountPrefix,
                        onClick = { onCashTransactionClick(tx.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DebtOrderItemCard(
    order: TradeOrderWithPayment,
    accentColor: Color,
    totalLabel: String,
    paidLabel: String,
    debtLabel: String,
    onClick: () -> Unit
) {
    val cleanDesc = formatOrderDescriptionWithQuantity(order.description, order.cattleQuantity)
    val isSettled = order.remainingDebt <= 0L

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = DateFormatter.formatDate(order.transactionDate),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
            SemanticBadge(
                text = if (isSettled) "Đã tất toán" else "Còn nợ",
                textColor = if (isSettled) MaterialTheme.colorScheme.onSurfaceVariant else accentColor,
                backgroundColor = if (isSettled) {
                    MaterialTheme.colorScheme.surfaceVariant
                } else {
                    accentColor.copy(alpha = 0.16f)
                }
            )
        }

        Text(
            text = cleanDesc,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            OrderMetricRow(
                label = totalLabel,
                amount = order.totalAmount,
                valueColor = MaterialTheme.colorScheme.onSurface
            )
            OrderMetricRow(
                label = paidLabel,
                amount = order.paidAmount,
                valueColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OrderMetricRow(
                label = debtLabel,
                amount = order.remainingDebt,
                valueColor = if (isSettled) MaterialTheme.colorScheme.onSurfaceVariant else accentColor
            )
        }
    }
}

@Composable
private fun OrderMetricRow(
    label: String,
    amount: Long,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = CurrencyFormatter.formatVnd(amount),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = valueColor,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
private fun LinkedCashItemRow(
    tx: CashTransactionWithContact,
    accentColor: Color,
    amountPrefix: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "${DateFormatter.formatDate(tx.transactionDate)} • ${tx.paymentMethod.vietnameseLabel}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = tx.shortContent.ifBlank { "Thanh toán công nợ" },
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = "$amountPrefix${CurrencyFormatter.formatVnd(tx.amount)}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = accentColor,
            maxLines = 1,
            softWrap = false
        )
    }
}

private fun formatOrderDescriptionWithQuantity(description: String, quantity: Int): String {
    val trimmed = description.trim()
    val qtySuffix = "($quantity con)"
    return when {
        trimmed.isEmpty() -> "$quantity con"
        trimmed.contains(qtySuffix, ignoreCase = true) || trimmed.contains("con)", ignoreCase = true) -> trimmed
        else -> "$trimmed ($quantity con)"
    }
}
