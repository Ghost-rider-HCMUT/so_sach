package com.sotraubo.app.feature.contact

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sotraubo.app.core.common.format.CurrencyFormatter
import com.sotraubo.app.core.common.format.DateFormatter
import com.sotraubo.app.core.common.search.VietnameseTextNormalizer
import com.sotraubo.app.core.designsystem.component.EmptyStateView
import com.sotraubo.app.core.designsystem.component.ScreenHelpButton
import com.sotraubo.app.core.designsystem.component.ScreenHelpGuides
import com.sotraubo.app.core.designsystem.component.ScrollToTopButton
import com.sotraubo.app.core.designsystem.component.TopBarPrimaryAction
import com.sotraubo.app.core.designsystem.component.ErrorBanner
import com.sotraubo.app.core.designsystem.component.SemanticBadge
import com.sotraubo.app.core.designsystem.theme.BlueContainer
import com.sotraubo.app.core.designsystem.theme.BluePrimary
import com.sotraubo.app.core.designsystem.theme.GreenReceive
import com.sotraubo.app.core.designsystem.theme.GreenReceiveContainer
import com.sotraubo.app.core.designsystem.theme.OrangePayable
import com.sotraubo.app.core.designsystem.theme.OrangePayableContainer
import com.sotraubo.app.core.designsystem.theme.RedTransfer
import com.sotraubo.app.core.designsystem.theme.RedTransferContainer
import com.sotraubo.app.core.model.CashFlowType
import com.sotraubo.app.core.model.CashTransactionWithContact
import com.sotraubo.app.core.model.ContactWithSummary
import com.sotraubo.app.core.model.TradeOrderType
import com.sotraubo.app.core.model.TradeOrderWithPayment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    contacts: List<ContactWithSummary>,
    onContactClick: (Long) -> Unit,
    onCreateContact: (fullName: String, phone: String, address: String, note: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showCreateDialog by rememberSaveable { mutableStateOf(false) }

    val filtered = remember(contacts, searchQuery) {
        if (searchQuery.isBlank()) {
            contacts
        } else {
            contacts.filter {
                VietnameseTextNormalizer.matchesQuery(
                    fullName = it.contact.fullName,
                    phone = it.contact.phone,
                    rawQuery = searchQuery
                )
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Người liên quan (${contacts.size})",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                },
                actions = {
                    ScreenHelpButton(guide = ScreenHelpGuides.CONTACT_LIST)
                    TopBarPrimaryAction(
                        label = "Thêm mới",
                        onClick = { showCreateDialog = true }
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
                contentPadding = PaddingValues(top = 12.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item(key = "contact_search_header") {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Tìm tên hoặc số điện thoại...", maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Tìm kiếm") },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Xóa tìm kiếm")
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (filtered.isEmpty()) {
                    item(key = "contact_empty") {
                        EmptyStateView(
                            title = if (searchQuery.isBlank()) "Chưa có người liên quan nào" else "Không tìm thấy kết quả",
                            message = if (searchQuery.isBlank()) {
                                "Hãy thêm người mua hoặc người bán trâu bò để quản lý lịch sử và công nợ."
                            } else {
                                "Không tìm thấy người nào khớp với từ khóa \"$searchQuery\"."
                            },
                            actionLabel = "Thêm người mới",
                            onActionClick = { showCreateDialog = true },
                            scrollable = false
                        )
                    }
                } else {
                    items(
                        items = filtered,
                        key = { it.contact.id }
                    ) { item ->
                        ContactSummaryCard(
                            item = item,
                            onClick = { onContactClick(item.contact.id) }
                        )
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

    if (showCreateDialog) {
        QuickCreateContactDialog(
            initialName = searchQuery.trim(),
            onDismiss = { showCreateDialog = false },
            onConfirmSave = { fullName, phone, address, note ->
                onCreateContact(fullName, phone, address, note)
                showCreateDialog = false
            }
        )
    }
}

@Composable
private fun ContactSummaryCard(
    item: ContactWithSummary,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.contact.fullName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Hồ sơ >",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            if (item.contact.phone.isNotBlank() || item.contact.address.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                val details = listOfNotNull(
                    item.contact.phone.takeIf { it.isNotBlank() }?.let { "SĐT: $it" },
                    item.contact.address.takeIf { it.isNotBlank() }
                ).joinToString(" • ")
                Text(
                    text = details,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (item.receivableDebt > 0L) {
                    SemanticBadge(
                        text = "Nợ mình: ${CurrencyFormatter.formatVnd(item.receivableDebt)}",
                        textColor = GreenReceive,
                        backgroundColor = GreenReceiveContainer
                    )
                }
                if (item.payableDebt > 0L) {
                    SemanticBadge(
                        text = "Mình nợ: ${CurrencyFormatter.formatVnd(item.payableDebt)}",
                        textColor = OrangePayable,
                        backgroundColor = OrangePayableContainer
                    )
                }
                if (item.receivableDebt == 0L && item.payableDebt == 0L) {
                    SemanticBadge(
                        text = "Không có nợ hiện tại",
                        textColor = BluePrimary,
                        backgroundColor = BlueContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Đã mua: ${CurrencyFormatter.formatVnd(item.totalPurchaseAmount)} • Đã bán: ${CurrencyFormatter.formatVnd(item.totalSaleAmount)}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Màn hình Chi tiết Hồ sơ Người liên quan (2.5 Master Prompt):
 * Hiển thị đầy đủ:
 * - Họ tên, Số điện thoại, Địa chỉ, Ghi chú.
 * - 6 chỉ số: Tổng tiền mua, Tổng tiền bán, Tổng tiền đã nhận, Tổng tiền đã chuyển, Người đó còn nợ mình, Mình còn nợ người đó.
 * - Lịch sử mua/bán & Lịch sử nhận/chuyển tiền.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreen(
    contactSummary: ContactWithSummary,
    personTradeOrders: List<TradeOrderWithPayment>,
    personCashTransactions: List<CashTransactionWithContact>,
    errorMessage: String? = null,
    onBack: () -> Unit,
    onOpenDebtDetail: (Long) -> Unit,
    onUpdateContact: (fullName: String, phone: String, address: String, note: String) -> Unit,
    onDeleteContact: () -> Unit,
    onExportPersonPdf: () -> Unit,
    onOrderClick: (Long) -> Unit,
    onCashTransactionClick: (Long) -> Unit
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hồ sơ đối tác", fontWeight = FontWeight.Bold, maxLines = 1) },
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
                    ScreenHelpButton(guide = ScreenHelpGuides.CONTACT_DETAIL)
                    IconButton(onClick = onExportPersonPdf) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = "Xuất báo cáo PDF",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Sửa hồ sơ",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Xóa hồ sơ",
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (errorMessage != null) {
                item {
                    ErrorBanner(message = errorMessage)
                }
            }

            // Thông tin cá nhân
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = contactSummary.contact.fullName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (contactSummary.contact.phone.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Call, contentDescription = null, tint = BluePrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Số điện thoại: ${contactSummary.contact.phone}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                        if (contactSummary.contact.address.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = BluePrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Địa chỉ: ${contactSummary.contact.address}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                        if (contactSummary.contact.note.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Ghi chú: ${contactSummary.contact.note}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // 6 Chỉ số tổng hợp của người liên quan
            item {
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
                        Text(
                            text = "TỔNG HỢP TÀI CHÍNH",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )

                        MiniStatBox(
                            label = "Tổng tiền mua hàng",
                            amount = contactSummary.totalPurchaseAmount,
                            color = BluePrimary,
                            modifier = Modifier.fillMaxWidth()
                        )
                        MiniStatBox(
                            label = "Tổng tiền bán hàng",
                            amount = contactSummary.totalSaleAmount,
                            color = BluePrimary,
                            modifier = Modifier.fillMaxWidth()
                        )
                        MiniStatBox(
                            label = "Tổng tiền đã nhận",
                            amount = contactSummary.totalCashInAmount,
                            color = GreenReceive,
                            modifier = Modifier.fillMaxWidth()
                        )
                        MiniStatBox(
                            label = "Tổng tiền đã chuyển",
                            amount = contactSummary.totalCashOutAmount,
                            color = RedTransfer,
                            modifier = Modifier.fillMaxWidth()
                        )
                        MiniStatBox(
                            label = "Công nợ phải thu",
                            amount = contactSummary.receivableDebt,
                            color = GreenReceive,
                            modifier = Modifier.fillMaxWidth()
                        )
                        MiniStatBox(
                            label = "Công nợ phải trả",
                            amount = contactSummary.payableDebt,
                            color = OrangePayable,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedButton(
                            onClick = { onOpenDebtDetail(contactSummary.contact.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Chi tiết công nợ hai chiều >",
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // Tab chọn Lịch sử Mua/Bán hoặc Lịch sử Nhận/Chuyển tiền
            item {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                text = "Mua / Bán (${personTradeOrders.size})",
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                text = "Thu / Chi (${personCashTransactions.size})",
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    )
                }
            }

            if (selectedTab == 0) {
                if (personTradeOrders.isEmpty()) {
                    item {
                        EmptyStateView(
                            title = "Chưa có lịch sử mua bán",
                            message = "Chưa có chuyến mua hoặc bán trâu bò nào với người này."
                        )
                    }
                } else {
                    items(
                        items = personTradeOrders,
                        key = { "ORDER_${it.id}" }
                    ) { order ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOrderClick(order.id) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val isSale = order.orderType == TradeOrderType.SALE
                                    SemanticBadge(
                                        text = "${order.orderType.vietnameseLabel} (${order.cattleQuantity} con)",
                                        textColor = if (isSale) GreenReceive else BluePrimary,
                                        backgroundColor = if (isSale) GreenReceiveContainer else BlueContainer
                                    )
                                    Text(
                                        text = DateFormatter.formatDate(order.transactionDate),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = order.description,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Tổng tiền: ${CurrencyFormatter.formatVnd(order.totalAmount)}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Đã thanh toán: ${CurrencyFormatter.formatVnd(order.paidAmount)}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (order.remainingDebt > 0L) {
                                    Text(
                                        text = if (order.orderType == TradeOrderType.SALE) {
                                            "Khách còn nợ: ${CurrencyFormatter.formatVnd(order.remainingDebt)}"
                                        } else {
                                            "Mình còn nợ: ${CurrencyFormatter.formatVnd(order.remainingDebt)}"
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (order.orderType == TradeOrderType.SALE) GreenReceive else OrangePayable
                                    )
                                } else {
                                    Text(
                                        text = "Đã thanh toán đủ (0 đ)",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = GreenReceive
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                if (personCashTransactions.isEmpty()) {
                    item {
                        EmptyStateView(
                            title = "Chưa có lịch sử nhận/chuyển tiền",
                            message = "Chưa có giao dịch tiền mặt hoặc chuyển khoản nào với người này."
                        )
                    }
                } else {
                    items(
                        items = personCashTransactions,
                        key = { "CASH_${it.id}" }
                    ) { tx ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onCashTransactionClick(tx.id) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                val isCashIn = tx.flowType == CashFlowType.CASH_IN
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    SemanticBadge(
                                        text = "${tx.flowType.vietnameseLabel} • ${tx.paymentMethod.vietnameseLabel}",
                                        textColor = if (isCashIn) GreenReceive else RedTransfer,
                                        backgroundColor = if (isCashIn) GreenReceiveContainer else RedTransferContainer
                                    )
                                    Text(
                                        text = CurrencyFormatter.formatSignedVnd(tx.amount, isPositive = isCashIn),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCashIn) GreenReceive else RedTransfer
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = tx.shortContent,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Ngày: ${DateFormatter.formatDate(tx.transactionDate)}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        QuickCreateContactDialog(
            title = "Chỉnh sửa hồ sơ người liên quan",
            initialName = contactSummary.contact.fullName,
            initialPhone = contactSummary.contact.phone,
            initialAddress = contactSummary.contact.address,
            initialNote = contactSummary.contact.note,
            onDismiss = { showEditDialog = false },
            onConfirmSave = { fullName, phone, address, note ->
                onUpdateContact(fullName, phone, address, note)
                showEditDialog = false
            }
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Xác nhận xóa hồ sơ", fontWeight = FontWeight.Bold) },
            text = {
                Text("Bạn có chắc chắn muốn xóa hồ sơ của \"${contactSummary.contact.fullName}\" không?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteContact()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedTransfer)
                ) {
                    Text("Xóa hồ sơ")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
private fun MiniStatBox(
    label: String,
    amount: Long,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Text(
                text = CurrencyFormatter.formatVnd(amount),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}
