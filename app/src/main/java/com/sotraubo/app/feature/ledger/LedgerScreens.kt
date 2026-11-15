package com.sotraubo.app.feature.ledger

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sotraubo.app.core.common.format.CurrencyFormatter
import com.sotraubo.app.core.common.format.DateFormatter
import com.sotraubo.app.core.common.format.InstallmentNoteFormatter
import com.sotraubo.app.core.ui.components.InstallmentPaymentDialog
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity
import com.sotraubo.app.core.database.entity.FamilyExpenseEntity
import com.sotraubo.app.core.database.entity.GoodsPurchaseEntity
import com.sotraubo.app.core.database.entity.QuotaFeeEntity
import com.sotraubo.app.core.model.ContactWithSummary
import com.sotraubo.app.feature.contact.ContactPickerBottomSheet
import com.sotraubo.app.core.designsystem.component.CurrencyTextField
import com.sotraubo.app.core.designsystem.component.DateSectionHeader
import com.sotraubo.app.core.designsystem.component.DropdownSelector
import com.sotraubo.app.core.designsystem.component.EmptyStateView
import com.sotraubo.app.core.designsystem.component.InfoLine
import com.sotraubo.app.core.designsystem.component.ScrollToTopButton
import com.sotraubo.app.core.designsystem.component.TopBarPrimaryAction
import com.sotraubo.app.core.designsystem.component.TimeFilterMode
import com.sotraubo.app.core.designsystem.component.VietnameseDatePickerField
import com.sotraubo.app.core.designsystem.component.VietnameseTimeFilterBar
import com.sotraubo.app.core.designsystem.theme.BluePrimary
import com.sotraubo.app.core.designsystem.theme.GreenReceive
import com.sotraubo.app.core.designsystem.theme.GreenReceiveContainer
import com.sotraubo.app.core.designsystem.theme.OrangePayable
import com.sotraubo.app.core.designsystem.theme.RedTransfer
import com.sotraubo.app.core.designsystem.theme.RedTransferContainer
import java.time.Instant
import java.time.LocalDate
import kotlin.math.roundToLong

// ============================================================================
// Tiện ích chung
// ============================================================================

private fun formatBaht(amount: Long): String = "${CurrencyFormatter.formatNumberOnly(amount)} ฿"

private fun formatQuantity(x100: Long): String =
    if (x100 % 100L == 0L) (x100 / 100L).toString() else "%.2f".format(x100 / 100.0).trimEnd('0').trimEnd(',', '.')

private fun toLocalDate(millis: Long): LocalDate =
    Instant.ofEpochMilli(millis).atZone(DateFormatter.VIETNAM_ZONE).toLocalDate()

private fun matchesTime(millis: Long, mode: TimeFilterMode, anchor: Long): Boolean = when (mode) {
    TimeFilterMode.ALL -> true
    TimeFilterMode.BY_MONTH -> DateFormatter.isSameMonth(millis, anchor)
    TimeFilterMode.BY_DAY -> DateFormatter.isSameDay(millis, anchor)
    TimeFilterMode.TODAY -> DateFormatter.isSameDay(millis, DateFormatter.todayNoonMillis())
}

@Composable
private fun LedgerCard(
    title: String,
    amountText: String,
    amountColor: Color,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = amountText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = amountColor,
                    maxLines = 1,
                    softWrap = false
                )
            }
            content()
        }
    }
}

/**
 * Khung danh sách dùng chung: Bộ lọc thời gian xổ xuống (Tất cả / Theo tháng / Theo ngày / Hôm nay),
 * khối tổng hợp từng dòng, danh sách nhóm theo ngày.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> LedgerListScaffold(
    title: String,
    addLabel: String,
    items: List<T>,
    dateOf: (T) -> Long,
    keyOf: (T) -> Long,
    summaryLines: (List<T>) -> List<Pair<String, String>>,
    dayTotalLabel: (List<T>) -> String,
    emptyTitle: String,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    showBackButton: Boolean = true,
    extraFilterContent: (@Composable () -> Unit)? = null,
    itemContent: @Composable (T) -> Unit
) {
    var mode by rememberSaveable { mutableStateOf(TimeFilterMode.BY_MONTH) }
    var anchor by rememberSaveable { mutableLongStateOf(DateFormatter.todayNoonMillis()) }

    val filtered = remember(items, mode, anchor) { items.filter { matchesTime(dateOf(it), mode, anchor) } }
    val groups = remember(filtered) { filtered.groupBy { toLocalDate(dateOf(it)) }.toList() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                        }
                    }
                },
                actions = {
                    TopBarPrimaryAction(label = addLabel, onClick = onAdd)
                }
            )
        }
    ) { padding ->
        val listState = rememberLazyListState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 4.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(key = "ledger_header") {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (extraFilterContent != null) {
                            extraFilterContent()
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                        VietnameseTimeFilterBar(
                            mode = mode,
                            onModeChange = { mode = it },
                            anchorMillis = anchor,
                            onAnchorMillisChange = { anchor = it }
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            summaryLines(filtered).forEach { (label, value) -> InfoLine(label, value) }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }

                if (groups.isEmpty()) {
                    item(key = "ledger_empty") {
                        EmptyStateView(
                            title = emptyTitle,
                            message = "Bấm nút \"+ $addLabel\" ở góc trên để thêm mới, hoặc đổi bộ lọc thời gian.",
                            scrollable = false
                        )
                    }
                } else {
                    groups.forEach { (date, dayItems) ->
                        item(key = "h_$date") {
                            DateSectionHeader(
                                dateMillis = DateFormatter.startOfDayMillis(date),
                                countLabel = dayTotalLabel(dayItems)
                            )
                        }
                        items(dayItems, key = { "i_${keyOf(it)}" }) { entry -> itemContent(entry) }
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

/**
 * Biểu mẫu toàn màn hình rộng rãi chuẩn Material 3 cho các sổ phụ (thay cho AlertDialog hẹp).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LedgerFormScaffold(
    title: String,
    canSave: Boolean,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: (() -> Unit)?,
    content: @Composable () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    BackHandler(onBack = onBack)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                actions = {
                    if (onDelete != null) {
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Xóa",
                                tint = RedTransfer
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    content()
                }
            }

            Button(
                onClick = onSave,
                enabled = canSave,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Lưu thông tin",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }

    if (showDeleteConfirm && onDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Xác nhận xóa", fontWeight = FontWeight.Bold) },
            text = { Text("Bạn có chắc chắn muốn xóa mục này không?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirm = false
                        onDelete()
                    }
                ) {
                    Text("Xóa", color = RedTransfer, fontWeight = FontWeight.Bold)
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

// ============================================================================
// Màn hình trung tâm "Thêm" (hub)
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreHubScreen(
    goods: List<GoodsPurchaseEntity>,
    exchanges: List<CurrencyExchangeEntity>,
    expenses: List<FamilyExpenseEntity>,
    quotaFees: List<QuotaFeeEntity>,
    contactCount: Int,
    onOpenGoods: () -> Unit,
    onOpenExchange: () -> Unit,
    onOpenFamily: () -> Unit,
    onOpenQuotaFees: () -> Unit,
    onOpenContacts: () -> Unit
) {
    val now = DateFormatter.todayNoonMillis()
    val goodsMonth = goods.filter { DateFormatter.isSameMonth(it.purchaseDate, now) }.sumOf { it.totalAmount }
    val bahtMonth = exchanges.filter { DateFormatter.isSameMonth(it.exchangeDate, now) }.size
    val familyMonth = expenses.filter { DateFormatter.isSameMonth(it.expenseDate, now) }.sumOf { it.amount }
    val quotaMonth = quotaFees.filter { DateFormatter.isSameMonth(it.feeDate, now) }.sumOf { it.amount }
    val monthLabel = DateFormatter.formatMonthYear(now)

    Scaffold(topBar = { TopAppBar(title = { Text("Sổ phụ & Tiện ích", fontWeight = FontWeight.Bold) }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HubCard(
                icon = Icons.Default.ReceiptLong,
                title = "Phí Quota (Biên giới)",
                line1 = "Thu $monthLabel: ${CurrencyFormatter.formatVnd(quotaMonth)}",
                line2 = "Tổng ${quotaFees.size} lượt thu • Chỉ thu, không chi",
                onClick = onOpenQuotaFees
            )
            HubCard(
                icon = Icons.Default.Inventory2,
                title = "Hàng hóa mua",
                line1 = "Chi $monthLabel: ${CurrencyFormatter.formatVnd(goodsMonth)}",
                line2 = "Tổng ${goods.size} lần mua",
                onClick = onOpenGoods
            )
            HubCard(
                icon = Icons.Default.CurrencyExchange,
                title = "Đổi tiền (Bath Thái)",
                line1 = "$monthLabel: $bahtMonth lần đổi",
                line2 = "Tổng ${exchanges.size} lần đổi",
                onClick = onOpenExchange
            )
            HubCard(
                icon = Icons.Default.Home,
                title = "Chi tiêu gia đình",
                line1 = "Chi $monthLabel: ${CurrencyFormatter.formatVnd(familyMonth)}",
                line2 = "Tổng ${expenses.size} khoản chi",
                onClick = onOpenFamily
            )
            HubCard(
                icon = Icons.Default.People,
                title = "Đối tác",
                line1 = "$contactCount người trong danh bạ",
                line2 = "Xem & quản lý thông tin đối tác",
                onClick = onOpenContacts
            )
        }
    }
}

@Composable
private fun HubCard(icon: ImageVector, title: String, line1: String, line2: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = BluePrimary)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(line1, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(line2, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ============================================================================
// 1. Hàng hóa mua
// ============================================================================

private val GOODS_UNITS = listOf("kg", "bao", "con", "cái", "lít", "chai", "bó", "khác")

@Composable
fun GoodsPurchaseScreen(
    goods: List<GoodsPurchaseEntity>,
    contacts: List<ContactWithSummary> = emptyList(),
    onCreateNewContactInline: (String, String, String, String, (Long) -> Unit) -> Unit = { _, _, _, _, _ -> },
    onBack: () -> Unit,
    onSave: (GoodsPurchaseEntity) -> Unit,
    onDelete: (Long) -> Unit,
    showBackButton: Boolean = true
) {
    var editing by remember { mutableStateOf<GoodsPurchaseEntity?>(null) }
    var showForm by remember { mutableStateOf(false) }

    if (showForm) {
        val e = editing
        var name by remember { mutableStateOf(e?.itemName.orEmpty()) }
        var qtyText by remember { mutableStateOf(e?.let { formatQuantity(it.quantityX100) }.orEmpty()) }
        var unit by remember { mutableStateOf(e?.unit ?: GOODS_UNITS.first()) }
        var total by remember { mutableLongStateOf(e?.totalAmount ?: 0L) }
        var supplier by remember { mutableStateOf(e?.supplier.orEmpty()) }
        var selectedContactId by remember(e) { mutableStateOf(e?.contactId) }
        var showSupplierPicker by remember { mutableStateOf(false) }
        var note by remember { mutableStateOf(e?.note.orEmpty()) }
        var date by remember { mutableLongStateOf(e?.purchaseDate ?: DateFormatter.todayNoonMillis()) }
        val qtyX100 = qtyText.replace(',', '.').toDoubleOrNull()?.let { (it * 100).roundToLong() } ?: 0L

        LedgerFormScaffold(
            title = if (e == null) "Mua hàng hóa" else "Sửa hàng hóa",
            canSave = name.isNotBlank() && qtyX100 > 0 && total > 0,
            onBack = { showForm = false },
            onSave = {
                onSave(
                    GoodsPurchaseEntity(
                        id = e?.id ?: 0L,
                        purchaseDate = date,
                        itemName = name.trim(),
                        quantityX100 = qtyX100,
                        unit = unit,
                        totalAmount = total,
                        supplier = supplier.trim(),
                        contactId = selectedContactId,
                        note = note.trim(),
                        createdAt = e?.createdAt ?: System.currentTimeMillis()
                    )
                )
                showForm = false
            },
            onDelete = e?.let { { onDelete(it.id); showForm = false } }
        ) {
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Tên hàng hóa (*)") },
                placeholder = { Text("Cám, thuốc thú y, cỏ...") },
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = qtyText, onValueChange = { qtyText = it.filter { c -> c.isDigit() || c == ',' || c == '.' } },
                label = { Text("Số lượng (*)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            DropdownSelector(
                prefix = "Đơn vị", options = GOODS_UNITS, selected = unit,
                optionLabel = { it }, onSelected = { unit = it }
            )
            CurrencyTextField(
                label = "Tổng tiền (*)", amount = total, onAmountChange = { total = it }
            )
            OutlinedTextField(
                value = supplier,
                onValueChange = {
                    supplier = it
                    selectedContactId = null
                },
                label = { Text("Đối tác / Nơi mua") },
                placeholder = { Text("VD: Đại lý Minh Phát (hoặc chọn Danh bạ)") },
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedButton(
                onClick = { showSupplierPicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.People, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Chọn đối tác từ Danh bạ")
            }
            VietnameseDatePickerField(label = "Ngày mua", selectedDateMillis = date, onDateSelected = { date = it })
            OutlinedTextField(
                value = note, onValueChange = { note = it },
                label = { Text("Ghi chú") }, modifier = Modifier.fillMaxWidth()
            )
        }
        if (showSupplierPicker) {
            ContactPickerBottomSheet(
                title = "Chọn đối tác / người bán hàng",
                contacts = contacts,
                onSelectContact = { chosen ->
                    selectedContactId = chosen.contact.id
                    supplier = chosen.contact.fullName
                    showSupplierPicker = false
                },
                onCreateAndSelectNewContact = { fullName, phone, address, contactNote ->
                    onCreateNewContactInline(fullName, phone, address, contactNote) { newContactId ->
                        selectedContactId = newContactId
                        supplier = fullName
                        showSupplierPicker = false
                    }
                },
                onDismiss = { showSupplierPicker = false }
            )
        }
        return
    }

    LedgerListScaffold(
        title = "Hàng hóa mua",
        addLabel = "Mua hàng hóa",
        items = goods,
        dateOf = { it.purchaseDate },
        keyOf = { it.id },
        summaryLines = { list ->
            listOf(
                "Số lần mua" to "${list.size}",
                "Tổng tiền đã chi" to CurrencyFormatter.formatVnd(list.sumOf { it.totalAmount })
            )
        },
        dayTotalLabel = { CurrencyFormatter.formatVnd(it.sumOf { g -> g.totalAmount }) },
        emptyTitle = "Chưa có hàng hóa nào được mua",
        onBack = onBack,
        onAdd = { editing = null; showForm = true },
        showBackButton = showBackButton
    ) { g ->
        LedgerCard(
            title = g.itemName,
            amountText = "-${CurrencyFormatter.formatVnd(g.totalAmount)}",
            amountColor = RedTransfer,
            onClick = { editing = g; showForm = true }
        ) {
            InfoLine("Số lượng", "${formatQuantity(g.quantityX100)} ${g.unit}")
            if (g.supplier.isNotBlank()) InfoLine("Nơi mua", g.supplier)
            InfoLine("Ngày mua", DateFormatter.formatDate(g.purchaseDate))
            if (g.note.isNotBlank()) InfoLine("Ghi chú", g.note)
        }
    }
}

// ============================================================================
// 2. Đổi tiền Bath Thái
// ============================================================================

private enum class ExchangeDirection(val code: String, val label: String) {
    VND_TO_THB("VND_TO_THB", "Mình chuyển (Bath)"),
    THB_TO_VND("THB_TO_VND", "Họ chuyển (Bath)")
}

@Composable
fun CurrencyExchangeScreen(
    exchanges: List<CurrencyExchangeEntity>,
    contacts: List<ContactWithSummary> = emptyList(),
    onCreateNewContactInline: (String, String, String, String, (Long) -> Unit) -> Unit = { _, _, _, _, _ -> },
    onBack: () -> Unit,
    onSave: (CurrencyExchangeEntity) -> Unit,
    onDelete: (Long) -> Unit,
    showBackButton: Boolean = true,
    initialDirectionCode: String? = null
) {
    var editing by remember { mutableStateOf<CurrencyExchangeEntity?>(null) }
    var showForm by remember(initialDirectionCode) { mutableStateOf(initialDirectionCode != null) }
    var directionFilter by rememberSaveable { mutableStateOf<String?>(null) }

    val visibleExchanges = remember(exchanges, directionFilter) {
        if (directionFilter == null) exchanges else exchanges.filter { it.direction == directionFilter }
    }

    if (showForm) {
        val e = editing
        var dir by remember(e, initialDirectionCode) {
            mutableStateOf(
                ExchangeDirection.entries.firstOrNull { it.code == (e?.direction ?: initialDirectionCode) }
                    ?: ExchangeDirection.VND_TO_THB
            )
        }
        var partnerName by remember(e) { mutableStateOf(e?.partnerName.orEmpty()) }
        var selectedContactId by remember(e) { mutableStateOf(e?.contactId) }
        var showPartnerPicker by remember { mutableStateOf(false) }
        var baht by remember { mutableLongStateOf(e?.bahtAmount ?: 0L) }
        var rate by remember { mutableLongStateOf(e?.rate ?: 0L) }
        var costRate by remember(e) { mutableLongStateOf(e?.costRate ?: 0L) }
        var costVndAmount by remember(e) { mutableLongStateOf(e?.costVndAmount ?: 0L) }
        var costManuallyEdited by remember(e) {
            mutableStateOf(e != null && e.costVndAmount > 0L && (e.bahtAmount == 0L || e.costRate == 0L || e.costVndAmount != e.bahtAmount * e.costRate))
        }
        var vndAmount by remember { mutableLongStateOf(e?.vndAmount ?: 0L) }
        var theoreticalVndAmount by remember { mutableLongStateOf(e?.theoreticalVndAmount ?: 0L) }
        var vndManuallyEdited by remember {
            mutableStateOf(e != null && e.vndAmount > 0L && (e.bahtAmount == 0L || e.rate == 0L || e.vndAmount != e.bahtAmount * e.rate))
        }
        var theoreticalManuallyEdited by remember {
            mutableStateOf(e != null && e.theoreticalVndAmount > 0L && (e.bahtAmount == 0L || e.rate == 0L || e.theoreticalVndAmount != e.bahtAmount * e.rate))
        }
        var validationError by remember { mutableStateOf<String?>(null) }
        var note by remember { mutableStateOf(e?.note.orEmpty()) }
        var date by remember { mutableLongStateOf(e?.exchangeDate ?: DateFormatter.todayNoonMillis()) }
        var showInstallmentDialog by remember { mutableStateOf(false) }

        val closeForm: () -> Unit = {
            if (initialDirectionCode != null) {
                onBack()
            } else {
                showForm = false
            }
        }

        if (showInstallmentDialog && e != null) {
            val remainingBefore = if (dir == ExchangeDirection.THB_TO_VND) {
                (theoreticalVndAmount - vndAmount).coerceAtLeast(0L)
            } else {
                0L
            }
            InstallmentPaymentDialog(
                title = if (dir == ExchangeDirection.THB_TO_VND) {
                    "Ghi thêm đợt khách chuyển"
                } else {
                    "Ghi thêm đợt mình chuyển"
                },
                remainingAmount = remainingBefore,
                onDismiss = { showInstallmentDialog = false },
                onConfirm = { addedAmount, payDate, payNote ->
                    val newVnd = vndAmount + addedAmount
                    val newTheoretical = if (dir == ExchangeDirection.THB_TO_VND) {
                        theoreticalVndAmount.coerceAtLeast(newVnd)
                    } else {
                        theoreticalVndAmount
                    }
                    val remainingAfter = if (dir == ExchangeDirection.THB_TO_VND) {
                        (newTheoretical - newVnd).coerceAtLeast(0L)
                    } else {
                        0L
                    }
                    val updatedNote = InstallmentNoteFormatter.appendInstallmentNote(
                        existingNote = note,
                        dateMillis = payDate,
                        addedAmount = addedAmount,
                        newTotalPaid = newVnd,
                        remainingAfter = remainingAfter,
                        actionLabel = if (dir == ExchangeDirection.THB_TO_VND) "Khách chuyển thêm" else "Mình chuyển thêm",
                        installmentNote = payNote
                    )
                    onSave(
                        e.copy(
                            exchangeDate = date,
                            direction = dir.code,
                            bahtAmount = baht,
                            rate = rate,
                            vndAmount = newVnd,
                            theoreticalVndAmount = newTheoretical,
                            note = updatedNote
                        )
                    )
                    showInstallmentDialog = false
                    closeForm()
                }
            )
        }

        LedgerFormScaffold(
            title = if (e == null) "Đổi Bath Thái" else "Sửa giao dịch Bath",
            canSave = true,
            onBack = closeForm,
            onSave = {
                when (val resolved = BathExchangeInputResolver.resolve(
                    directionCode = dir.code,
                    bahtAmount = baht,
                    rate = rate,
                    vndAmount = vndAmount,
                    theoreticalVndAmount = theoreticalVndAmount,
                    costRate = costRate,
                    costVndAmount = costVndAmount
                )) {
                    is BathExchangeResolution.Invalid -> {
                        validationError = resolved.errorMessage
                    }
                    is BathExchangeResolution.Valid -> {
                        validationError = null
                        val finalNote = if (
                            e != null &&
                            resolved.resolvedVndAmount > e.vndAmount &&
                            e.vndAmount > 0L &&
                            note.trim() == e.note.trim()
                        ) {
                            val added = resolved.resolvedVndAmount - e.vndAmount
                            val rem = if (dir == ExchangeDirection.THB_TO_VND) {
                                (resolved.resolvedTheoreticalVndAmount - resolved.resolvedVndAmount).coerceAtLeast(0L)
                            } else {
                                0L
                            }
                            InstallmentNoteFormatter.appendInstallmentNote(
                                existingNote = note,
                                dateMillis = date,
                                addedAmount = added,
                                newTotalPaid = resolved.resolvedVndAmount,
                                remainingAfter = rem,
                                actionLabel = if (dir == ExchangeDirection.THB_TO_VND) "Khách chuyển thêm" else "Mình chuyển thêm"
                            )
                        } else {
                            note.trim()
                        }
                        onSave(
                            CurrencyExchangeEntity(
                                id = e?.id ?: 0L,
                                exchangeDate = date,
                                direction = dir.code,
                                bahtAmount = baht,
                                rate = rate,
                                vndAmount = resolved.resolvedVndAmount,
                                theoreticalVndAmount = resolved.resolvedTheoreticalVndAmount,
                                costRate = if (dir == ExchangeDirection.THB_TO_VND) costRate else rate,
                                costVndAmount = resolved.resolvedCostVndAmount,
                                partnerName = partnerName.trim(),
                                contactId = selectedContactId,
                                note = finalNote,
                                createdAt = e?.createdAt ?: System.currentTimeMillis()
                            )
                        )
                        closeForm()
                    }
                }
            },
            onDelete = e?.let { { onDelete(it.id); closeForm() } }
        ) {
            if (e != null) {
                val remaining = if (dir == ExchangeDirection.THB_TO_VND) {
                    (theoreticalVndAmount - vndAmount).coerceAtLeast(0L)
                } else {
                    0L
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = GreenReceive.copy(alpha = 0.14f),
                    border = BorderStroke(1.dp, GreenReceive.copy(alpha = 0.55f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (dir == ExchangeDirection.THB_TO_VND && theoreticalVndAmount > 0L) {
                            Text(
                                text = if (remaining > 0L) {
                                    "Còn thiếu: ${CurrencyFormatter.formatVnd(remaining)} (Đã thu ${CurrencyFormatter.formatVnd(vndAmount)} / ${CurrencyFormatter.formatVnd(theoreticalVndAmount)})"
                                } else {
                                    "Đã thu đủ ${CurrencyFormatter.formatVnd(vndAmount)}"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = if (remaining > 0L) OrangePayable else GreenReceive
                            )
                        }
                        Button(
                            onClick = { showInstallmentDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (dir == ExchangeDirection.THB_TO_VND) {
                                    "Ghi thêm đợt khách chuyển tiền"
                                } else {
                                    "Ghi thêm đợt mình chuyển tiền"
                                },
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val isTransferSelected = dir == ExchangeDirection.VND_TO_THB
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            dir = ExchangeDirection.VND_TO_THB
                            validationError = null
                        },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isTransferSelected) RedTransfer.copy(alpha = 0.24f) else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        width = if (isTransferSelected) 2.dp else 1.dp,
                        color = if (isTransferSelected) RedTransfer else MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = if (isTransferSelected) Color.White else Color.White.copy(alpha = 0.75f),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "Chuyển",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1
                            )
                            Text(
                                text = "Mình chuyển Bath",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = if (isTransferSelected) Color.White.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.65f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                val isReceiveSelected = dir == ExchangeDirection.THB_TO_VND
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            dir = ExchangeDirection.THB_TO_VND
                            validationError = null
                        },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isReceiveSelected) GreenReceive.copy(alpha = 0.24f) else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        width = if (isReceiveSelected) 2.dp else 1.dp,
                        color = if (isReceiveSelected) GreenReceive else MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = if (isReceiveSelected) Color.White else Color.White.copy(alpha = 0.75f),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "Nhận",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1
                            )
                            Text(
                                text = "Khách chuyển Việt",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = if (isReceiveSelected) Color.White.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.65f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
            ContactSelectorCard(
                label = if (dir == ExchangeDirection.VND_TO_THB) {
                    "Người nhận / Giao dịch Bath"
                } else {
                    "Người mua Bath / Trả tiền"
                },
                selectedName = partnerName,
                onSelectClick = { showPartnerPicker = true },
                onClearClick = {
                    partnerName = ""
                    selectedContactId = null
                }
            )
            if (dir == ExchangeDirection.VND_TO_THB) {
                CurrencyTextField(
                    label = "Số Bath",
                    amount = baht,
                    onAmountChange = { newBaht ->
                        baht = newBaht
                        validationError = null
                        if (!vndManuallyEdited && newBaht > 0L && rate > 0L) {
                            vndAmount = newBaht * rate
                        }
                    },
                    unitSuffix = "฿",
                    showVietnameseWords = false
                )
                CurrencyTextField(
                    label = "Tỉ giá",
                    amount = rate,
                    onAmountChange = { newRate ->
                        rate = newRate
                        validationError = null
                        if (!vndManuallyEdited && baht > 0L && newRate > 0L) {
                            vndAmount = baht * newRate
                        }
                    },
                    showVietnameseWords = false
                )
                CurrencyTextField(
                    label = "Số tiền Việt",
                    amount = vndAmount,
                    onAmountChange = { newVnd ->
                        vndAmount = newVnd
                        vndManuallyEdited = newVnd > 0L
                        validationError = null
                    },
                    showVietnameseWords = true
                )
            } else {
                CurrencyTextField(
                    label = "Số Bath",
                    amount = baht,
                    onAmountChange = { newBaht ->
                        baht = newBaht
                        validationError = null
                        if (!costManuallyEdited && newBaht > 0L && costRate > 0L) {
                            costVndAmount = newBaht * costRate
                        }
                        if (!theoreticalManuallyEdited && newBaht > 0L && rate > 0L) {
                            theoreticalVndAmount = newBaht * rate
                        }
                    },
                    unitSuffix = "฿",
                    showVietnameseWords = false
                )
                CurrencyTextField(
                    label = "Giá Bath vốn",
                    amount = costRate,
                    onAmountChange = { newCostRate ->
                        costRate = newCostRate
                        validationError = null
                        if (!costManuallyEdited && baht > 0L && newCostRate > 0L) {
                            costVndAmount = baht * newCostRate
                        }
                    },
                    showVietnameseWords = false
                )
                CurrencyTextField(
                    label = "Tiền vốn đã chuyển đi",
                    amount = costVndAmount,
                    onAmountChange = { newCostVnd ->
                        costVndAmount = newCostVnd
                        costManuallyEdited = newCostVnd > 0L
                        validationError = null
                    },
                    showVietnameseWords = true
                )
                CurrencyTextField(
                    label = "Giá bán cho khách",
                    amount = rate,
                    onAmountChange = { newRate ->
                        rate = newRate
                        validationError = null
                        if (!theoreticalManuallyEdited && baht > 0L && newRate > 0L) {
                            theoreticalVndAmount = baht * newRate
                        }
                    },
                    showVietnameseWords = false
                )
                CurrencyTextField(
                    label = "Tiền Việt cần thu",
                    amount = theoreticalVndAmount,
                    onAmountChange = { newTheory ->
                        theoreticalVndAmount = newTheory
                        theoreticalManuallyEdited = newTheory > 0L
                        validationError = null
                    },
                    showVietnameseWords = true
                )
                CurrencyTextField(
                    label = "Tiền Việt đã thu",
                    amount = vndAmount,
                    onAmountChange = { newVnd ->
                        vndAmount = newVnd
                        validationError = null
                    },
                    showVietnameseWords = true
                )
                if (costVndAmount > 0L && theoreticalVndAmount > 0L) {
                    val spread = theoreticalVndAmount - costVndAmount
                    val spreadColor = if (spread >= 0L) GreenReceive else RedTransfer
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = spreadColor.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, spreadColor.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Chênh lệch (Lời dự kiến):",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = (if (spread > 0L) "+" else "") + CurrencyFormatter.formatVnd(spread),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = spreadColor,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }
            if (validationError != null) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = RedTransfer.copy(alpha = 0.16f),
                    border = BorderStroke(1.dp, RedTransfer.copy(alpha = 0.6f))
                ) {
                    Text(
                        text = validationError.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = RedTransfer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
            VietnameseDatePickerField(
                label = if (dir == ExchangeDirection.VND_TO_THB) "Ngày chuyển" else "Ngày nhận",
                selectedDateMillis = date,
                onDateSelected = { date = it }
            )
            OutlinedTextField(
                value = note, onValueChange = { note = it },
                label = { Text("Ghi chú") }, modifier = Modifier.fillMaxWidth()
            )
        }
        if (showPartnerPicker) {
            ContactPickerBottomSheet(
                title = if (dir == ExchangeDirection.VND_TO_THB) "Chọn người nhận Bath" else "Chọn người mua Bath",
                contacts = contacts,
                onSelectContact = { chosen ->
                    selectedContactId = chosen.contact.id
                    partnerName = chosen.contact.fullName
                    showPartnerPicker = false
                },
                onCreateAndSelectNewContact = { fullName, phone, address, contactNote ->
                    onCreateNewContactInline(fullName, phone, address, contactNote) { newContactId ->
                        selectedContactId = newContactId
                        partnerName = fullName
                        showPartnerPicker = false
                    }
                },
                onDismiss = { showPartnerPicker = false }
            )
        }
        return
    }

    LedgerListScaffold(
        title = "Sổ Bath Thái",
        addLabel = "Thêm Bath",
        items = visibleExchanges,
        dateOf = { it.exchangeDate },
        keyOf = { it.id },
        summaryLines = { list ->
            val mine = list.filter { it.direction == ExchangeDirection.VND_TO_THB.code }
            val theirs = list.filter { it.direction == ExchangeDirection.THB_TO_VND.code }
            val totalSentVnd = mine.sumOf { it.vndAmount } + theirs.sumOf {
                when {
                    it.costVndAmount > 0L -> it.costVndAmount
                    it.bahtAmount > 0L && it.costRate > 0L -> it.bahtAmount * it.costRate
                    else -> 0L
                }
            }
            val theirTheoretical = theirs.sumOf { if (it.theoreticalVndAmount > 0L) it.theoreticalVndAmount else it.vndAmount }
            val theirActual = theirs.sumOf { it.vndAmount }
            val theirDebt = (theirTheoretical - theirActual).coerceAtLeast(0L)
            listOf(
                "Mình chuyển Bath" to formatBaht(mine.sumOf { it.bahtAmount } + theirs.sumOf { it.bahtAmount }),
                "Đã chuyển đi (VND vốn)" to CurrencyFormatter.formatVnd(totalSentVnd),
                "Khách chuyển Việt (Lý thuyết)" to CurrencyFormatter.formatVnd(theirTheoretical),
                "Khách chuyển Việt (Đã thu - Thực tế)" to CurrencyFormatter.formatVnd(theirActual),
                "Khách còn thiếu (Nợ Bath)" to CurrencyFormatter.formatVnd(theirDebt)
            )
        },
        dayTotalLabel = { "${it.size} giao dịch" },
        emptyTitle = "Chưa có giao dịch Bath nào",
        onBack = onBack,
        onAdd = { editing = null; showForm = true },
        showBackButton = showBackButton,
        extraFilterContent = {
            DropdownSelector(
                prefix = "Chiều",
                options = listOf<String?>(null, ExchangeDirection.VND_TO_THB.code, ExchangeDirection.THB_TO_VND.code),
                selected = directionFilter,
                optionLabel = { code ->
                    when (code) {
                        ExchangeDirection.VND_TO_THB.code -> ExchangeDirection.VND_TO_THB.label
                        ExchangeDirection.THB_TO_VND.code -> ExchangeDirection.THB_TO_VND.label
                        else -> "Tất cả"
                    }
                },
                onSelected = { directionFilter = it }
            )
        }
    ) { x ->
        val isMine = x.direction == ExchangeDirection.VND_TO_THB.code
        LedgerCard(
            title = if (isMine) "Mình chuyển Bath" else "Khách chuyển Việt",
            amountText = if (x.vndAmount > 0L && (x.bahtAmount == 0L || !isMine)) {
                CurrencyFormatter.formatVnd(x.vndAmount)
            } else {
                formatBaht(x.bahtAmount)
            },
            amountColor = if (isMine) OrangePayable else GreenReceive,
            onClick = { editing = x; showForm = true }
        ) {
            if (x.partnerName.isNotBlank()) {
                InfoLine(
                    label = if (isMine) "Người nhận Bath" else "Người mua Bath",
                    value = x.partnerName
                )
            }
            if (!isMine && x.costVndAmount > 0L) {
                InfoLine("Đã chuyển đi (Giá Bath)", CurrencyFormatter.formatVnd(x.costVndAmount), valueColor = RedTransfer)
            }
            if (!isMine && x.theoreticalVndAmount > 0L && x.theoreticalVndAmount != x.vndAmount) {
                InfoLine("Cần thu (Lý thuyết)", CurrencyFormatter.formatVnd(x.theoreticalVndAmount))
                InfoLine("Đã thu (Thực tế)", CurrencyFormatter.formatVnd(x.vndAmount), valueColor = GreenReceive)
                val debt = (x.theoreticalVndAmount - x.vndAmount).coerceAtLeast(0L)
                if (debt > 0L) {
                    InfoLine("Còn nợ", CurrencyFormatter.formatVnd(debt), valueColor = OrangePayable)
                }
            }
            if (x.bahtAmount > 0L && !isMine) {
                InfoLine("Số Bath tương ứng", formatBaht(x.bahtAmount))
            }
            if (!isMine && x.costRate > 0L) {
                InfoLine("Giá Bath vốn", "${CurrencyFormatter.formatVnd(x.costRate)} / 1 ฿")
            }
            if (x.rate > 0L) {
                InfoLine(if (isMine) "Tỷ giá" else "Giá bán", "${CurrencyFormatter.formatVnd(x.rate)} / 1 ฿")
            }
            if (x.vndAmount > 0L && isMine && x.bahtAmount > 0L) {
                InfoLine("Quy đổi tiền Việt", CurrencyFormatter.formatVnd(x.vndAmount))
            }
            InfoLine(if (isMine) "Ngày chuyển" else "Ngày nhận", DateFormatter.formatDate(x.exchangeDate))
            if (x.note.isNotBlank()) InfoLine("Ghi chú", x.note)
        }
    }
}

// ============================================================================
// 3. Chi tiêu gia đình
// ============================================================================

@Composable
fun FamilyExpenseScreen(
    expenses: List<FamilyExpenseEntity>,
    contacts: List<ContactWithSummary> = emptyList(),
    onCreateNewContactInline: (String, String, String, String, (Long) -> Unit) -> Unit = { _, _, _, _, _ -> },
    onBack: () -> Unit,
    onSave: (FamilyExpenseEntity) -> Unit,
    onDelete: (Long) -> Unit,
    showBackButton: Boolean = true
) {
    var editing by remember { mutableStateOf<FamilyExpenseEntity?>(null) }
    var showForm by remember { mutableStateOf(false) }

    if (showForm) {
        val e = editing
        var partnerName by remember(e) { mutableStateOf(e?.partnerName.orEmpty()) }
        var selectedContactId by remember(e) { mutableStateOf(e?.contactId) }
        var showPartnerPicker by remember { mutableStateOf(false) }
        var amount by remember { mutableLongStateOf(e?.amount ?: 0L) }
        var note by remember { mutableStateOf(e?.note.orEmpty()) }
        var date by remember { mutableLongStateOf(e?.expenseDate ?: DateFormatter.todayNoonMillis()) }

        LedgerFormScaffold(
            title = if (e == null) "Tạo khoản chi" else "Sửa khoản chi",
            canSave = amount > 0 && note.isNotBlank(),
            onBack = { showForm = false },
            onSave = {
                onSave(
                    FamilyExpenseEntity(
                        id = e?.id ?: 0L,
                        expenseDate = date,
                        category = "",
                        amount = amount,
                        partnerName = partnerName.trim(),
                        contactId = selectedContactId,
                        note = note.trim(),
                        createdAt = e?.createdAt ?: System.currentTimeMillis()
                    )
                )
                showForm = false
            },
            onDelete = e?.let { { onDelete(it.id); showForm = false } }
        ) {
            ContactSelectorCard(
                label = "Đối tác / Người nhận tiền",
                selectedName = partnerName,
                onSelectClick = { showPartnerPicker = true },
                onClearClick = {
                    partnerName = ""
                    selectedContactId = null
                }
            )
            CurrencyTextField(
                label = "Số tiền (*)", amount = amount, onAmountChange = { amount = it }
            )
            OutlinedTextField(
                value = note, onValueChange = { note = it },
                label = { Text("Lý do chi (*)") },
                placeholder = { Text("VD: Tiền chợ, tiền điện...") },
                modifier = Modifier.fillMaxWidth()
            )
            VietnameseDatePickerField(label = "Ngày chi", selectedDateMillis = date, onDateSelected = { date = it })
        }
        if (showPartnerPicker) {
            ContactPickerBottomSheet(
                title = "Chọn đối tác / người nhận tiền",
                contacts = contacts,
                onSelectContact = { chosen ->
                    selectedContactId = chosen.contact.id
                    partnerName = chosen.contact.fullName
                    showPartnerPicker = false
                },
                onCreateAndSelectNewContact = { fullName, phone, address, contactNote ->
                    onCreateNewContactInline(fullName, phone, address, contactNote) { newContactId ->
                        selectedContactId = newContactId
                        partnerName = fullName
                        showPartnerPicker = false
                    }
                },
                onDismiss = { showPartnerPicker = false }
            )
        }
        return
    }

    LedgerListScaffold(
        title = "Chi tiêu gia đình",
        addLabel = "Tạo khoản chi",
        items = expenses,
        dateOf = { it.expenseDate },
        keyOf = { it.id },
        summaryLines = { list ->
            listOf(
                "Số khoản chi" to "${list.size}",
                "Tổng chi" to CurrencyFormatter.formatVnd(list.sumOf { it.amount })
            )
        },
        dayTotalLabel = { CurrencyFormatter.formatVnd(it.sumOf { x -> x.amount }) },
        emptyTitle = "Chưa có khoản chi tiêu nào",
        onBack = onBack,
        onAdd = { editing = null; showForm = true },
        showBackButton = showBackButton
    ) { x ->
        LedgerCard(
            title = x.note.ifBlank { "Khoản chi" },
            amountText = "-${CurrencyFormatter.formatVnd(x.amount)}",
            amountColor = RedTransfer,
            onClick = { editing = x; showForm = true }
        ) {
            if (x.partnerName.isNotBlank()) {
                InfoLine("Đối tác / Người nhận", x.partnerName)
            }
            InfoLine("Ngày chi", DateFormatter.formatDate(x.expenseDate))
        }
    }
}

// ============================================================================
// 4. Thu phí Quota qua biên giới (Chỉ thu, không có chi)
// ============================================================================

@Composable
fun QuotaFeeScreen(
    quotaFees: List<QuotaFeeEntity>,
    contacts: List<ContactWithSummary> = emptyList(),
    onCreateNewContactInline: (String, String, String, String, (Long) -> Unit) -> Unit = { _, _, _, _, _ -> },
    onBack: () -> Unit,
    onSave: (QuotaFeeEntity) -> Unit,
    onDelete: (Long) -> Unit,
    showBackButton: Boolean = true
) {
    var editing by remember { mutableStateOf<QuotaFeeEntity?>(null) }
    var showForm by remember { mutableStateOf(false) }

    if (showForm) {
        val e = editing
        var payerName by remember(e) { mutableStateOf(e?.payerName.orEmpty()) }
        var selectedContactId by remember(e) { mutableStateOf(e?.contactId) }
        var showContactPicker by remember { mutableStateOf(false) }
        var cattleQuantity by remember(e) { mutableIntStateOf(e?.cattleQuantity ?: 0) }
        var amount by remember(e) { mutableLongStateOf(e?.amount ?: 0L) }
        var note by remember(e) { mutableStateOf(e?.note.orEmpty()) }
        var date by remember(e) { mutableLongStateOf(e?.feeDate ?: DateFormatter.todayNoonMillis()) }

        LedgerFormScaffold(
            title = if (e == null) "Thu phí Quota biên giới" else "Sửa phiếu thu phí Quota",
            canSave = amount > 0L,
            onBack = { showForm = false },
            onSave = {
                onSave(
                    QuotaFeeEntity(
                        id = e?.id ?: 0L,
                        feeDate = date,
                        payerName = payerName.trim(),
                        contactId = selectedContactId,
                        cattleQuantity = cattleQuantity.coerceAtLeast(0),
                        amount = amount,
                        note = note.trim(),
                        createdAt = e?.createdAt ?: System.currentTimeMillis()
                    )
                )
                showForm = false
            },
            onDelete = e?.let { { onDelete(it.id); showForm = false } }
        ) {
            ContactSelectorCard(
                label = "Người nộp phí",
                selectedName = payerName,
                onSelectClick = { showContactPicker = true },
                onClearClick = {
                    payerName = ""
                    selectedContactId = null
                }
            )
            CurrencyTextField(
                label = "Số tiền phí Quota thu được (*)",
                amount = amount,
                onAmountChange = { amount = it }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Số lượng trâu bò (con)",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { if (cattleQuantity > 0) cattleQuantity -= 1 },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("-", fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "$cattleQuantity con",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedButton(
                        onClick = { cattleQuantity += 1 },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("+", fontWeight = FontWeight.Bold)
                    }
                }
            }
            VietnameseDatePickerField(
                label = "Ngày thu",
                selectedDateMillis = date,
                onDateSelected = { date = it }
            )
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Ghi chú") },
                placeholder = { Text("VD: Cửa khẩu, biển số xe...") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (showContactPicker) {
            ContactPickerBottomSheet(
                title = "Chọn người nộp phí Quota",
                contacts = contacts,
                onSelectContact = { chosen ->
                    selectedContactId = chosen.contact.id
                    payerName = chosen.contact.fullName
                    showContactPicker = false
                },
                onCreateAndSelectNewContact = { fullName, phone, address, contactNote ->
                    onCreateNewContactInline(fullName, phone, address, contactNote) { newContactId ->
                        selectedContactId = newContactId
                        payerName = fullName
                        showContactPicker = false
                    }
                },
                onDismiss = { showContactPicker = false }
            )
        }
        return
    }

    LedgerListScaffold(
        title = "Phí Quota biên giới",
        addLabel = "Thu phí Quota",
        items = quotaFees,
        dateOf = { it.feeDate },
        keyOf = { it.id },
        summaryLines = { list ->
            listOf(
                "Số lượt thu" to "${list.size}",
                "Tổng số con" to "${list.sumOf { it.cattleQuantity }} con",
                "Tổng tiền đã thu" to "+${CurrencyFormatter.formatVnd(list.sumOf { it.amount })}"
            )
        },
        dayTotalLabel = { "+${CurrencyFormatter.formatVnd(it.sumOf { x -> x.amount })}" },
        emptyTitle = "Chưa có khoản thu phí Quota nào",
        onBack = onBack,
        onAdd = { editing = null; showForm = true },
        showBackButton = showBackButton
    ) { q ->
        LedgerCard(
            title = q.payerName.ifBlank { "Thu phí Quota" },
            amountText = "+${CurrencyFormatter.formatVnd(q.amount)}",
            amountColor = GreenReceive,
            onClick = { editing = q; showForm = true }
        ) {
            if (q.cattleQuantity > 0) {
                InfoLine("Số lượng", "${q.cattleQuantity} con")
            }
            InfoLine("Ngày thu", DateFormatter.formatDate(q.feeDate))
            if (q.note.isNotBlank()) InfoLine("Ghi chú", q.note)
        }
    }
}

@Composable
private fun ContactSelectorCard(
    label: String,
    selectedName: String,
    onSelectClick: () -> Unit,
    onClearClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelectClick),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = if (selectedName.isNotBlank()) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            } else {
                MaterialTheme.colorScheme.outline
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.People,
                contentDescription = null,
                tint = if (selectedName.isNotBlank()) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            Column(modifier = Modifier.weight(1f)) {
                if (selectedName.isNotBlank()) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = selectedName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            if (selectedName.isNotBlank()) {
                IconButton(
                    onClick = onClearClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Bỏ chọn",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
