package com.sotraubo.app.feature.cashflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.sotraubo.app.core.common.format.CurrencyFormatter
import com.sotraubo.app.core.common.format.DateFormatter
import com.sotraubo.app.core.common.search.VietnameseTextNormalizer
import com.sotraubo.app.core.designsystem.component.AttachmentPickerSection
import com.sotraubo.app.core.designsystem.component.CurrencyTextField
import com.sotraubo.app.core.designsystem.component.ScreenHelpButton
import com.sotraubo.app.core.designsystem.component.ScreenHelpGuides
import com.sotraubo.app.core.designsystem.component.DateSectionHeader
import com.sotraubo.app.core.designsystem.component.EmptyStateView
import com.sotraubo.app.core.designsystem.component.ErrorBanner
import com.sotraubo.app.core.designsystem.component.SemanticBadge
import com.sotraubo.app.core.designsystem.component.TimeFilterMode
import com.sotraubo.app.core.designsystem.component.VietnameseDatePickerField
import com.sotraubo.app.core.designsystem.component.VietnameseTimeFilterBar
import com.sotraubo.app.core.designsystem.theme.responsiveSp
import com.sotraubo.app.core.designsystem.theme.GreenReceive
import com.sotraubo.app.core.designsystem.theme.GreenReceiveContainer
import com.sotraubo.app.core.designsystem.theme.OrangePayable
import com.sotraubo.app.core.designsystem.theme.OrangePayableContainer
import com.sotraubo.app.core.designsystem.theme.RedTransfer
import com.sotraubo.app.core.designsystem.theme.RedTransferContainer
import com.sotraubo.app.core.model.CashFlowType
import com.sotraubo.app.core.model.CashTransactionWithContact
import com.sotraubo.app.core.model.ContactWithSummary
import com.sotraubo.app.core.model.PaymentMethod
import com.sotraubo.app.core.model.TradeOrderType
import com.sotraubo.app.core.model.TradeOrderWithPayment
import com.sotraubo.app.feature.contact.ContactPickerBottomSheet

/**
 * Biểu mẫu Ghi nhận Nhận Tiền (Thu tiền) hoặc Chuyển Tiền (Chi tiền).
 * - Hỗ trợ chọn liên kết với một đơn Bán/Mua đang còn nợ để trừ nợ cho đơn đó.
 * - Hỗ trợ tùy chọn tự động trừ vào các đơn nợ cũ nhất hoặc giao dịch độc lập.
 * - Chặn nhập vượt quá số tiền còn nợ của đơn liên kết.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashTransactionFormScreen(
    flowType: CashFlowType,
    contacts: List<ContactWithSummary>,
    allTradeOrders: List<TradeOrderWithPayment>,
    preselectedContactId: Long? = null,
    preselectedOrderId: Long? = null,
    existingTransaction: CashTransactionWithContact? = null,
    errorMessage: String? = null,
    onBack: () -> Unit,
    onCreateNewContactInline: (fullName: String, phone: String, address: String, note: String, onCreated: (Long) -> Unit) -> Unit,
    onSaveTransaction: (
        contactId: Long?,
        linkedTradeOrderId: Long?,
        autoAllocateOldestDebt: Boolean,
        amount: Long,
        transactionDate: Long,
        paymentMethod: PaymentMethod,
        shortContent: String,
        note: String,
        attachmentPaths: List<String>
    ) -> Unit,
    onDeleteTransaction: (() -> Unit)? = null
) {
    val isCashIn = (flowType == CashFlowType.CASH_IN)
    val isEditing = (existingTransaction != null)

    var selectedContactId by rememberSaveable {
        mutableStateOf(existingTransaction?.contactId ?: preselectedContactId)
    }
    var selectedOrderId by rememberSaveable {
        mutableStateOf(existingTransaction?.tradeOrderId ?: preselectedOrderId)
    }
    var autoAllocateOldest by rememberSaveable {
        mutableStateOf(false)
    }
    var amount by rememberSaveable {
        mutableLongStateOf(existingTransaction?.amount ?: 0L)
    }
    var transactionDate by rememberSaveable {
        mutableLongStateOf(existingTransaction?.transactionDate ?: DateFormatter.todayNoonMillis())
    }
    var paymentMethodName by rememberSaveable {
        mutableStateOf((existingTransaction?.paymentMethod ?: PaymentMethod.CASH).dbValue)
    }
    var shortContent by rememberSaveable {
        mutableStateOf(existingTransaction?.shortContent ?: "")
    }
    var note by rememberSaveable {
        mutableStateOf(existingTransaction?.note ?: "")
    }
    val attachmentPaths = remember {
        mutableStateListOf<String>().apply {
            existingTransaction?.attachmentPaths?.let { addAll(it) }
        }
    }

    LaunchedEffect(existingTransaction?.id) {
        if (existingTransaction != null) {
            selectedContactId = existingTransaction.contactId
            selectedOrderId = existingTransaction.tradeOrderId
            amount = existingTransaction.amount
            transactionDate = existingTransaction.transactionDate
            paymentMethodName = existingTransaction.paymentMethod.dbValue
            shortContent = existingTransaction.shortContent
            note = existingTransaction.note
            attachmentPaths.clear()
            attachmentPaths.addAll(existingTransaction.attachmentPaths)
        }
    }

    var showContactPicker by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }

    val selectedContact = remember(contacts, selectedContactId) {
        contacts.firstOrNull { it.contact.id == selectedContactId }
    }

    // Các đơn Mua/Bán liên quan của người này đang còn nợ (hoặc đơn đang gắn với giao dịch sửa)
    val targetOrderType = if (isCashIn) TradeOrderType.SALE else TradeOrderType.PURCHASE
    val candidateOrders = remember(allTradeOrders, selectedContactId, existingTransaction) {
        allTradeOrders.filter { order ->
            order.contactId == selectedContactId &&
                order.orderType == targetOrderType &&
                (order.remainingDebt > 0L || order.id == existingTransaction?.tradeOrderId)
        }
    }

    // Tự động chọn đơn đang nợ nếu người đó có đúng 1 đơn đang nợ và người dùng chưa chọn
    LaunchedEffect(selectedContactId, candidateOrders.size) {
        if (!isEditing && selectedOrderId == null && candidateOrders.size == 1) {
            selectedOrderId = candidateOrders.first().id
        }
    }

    val selectedOrder = remember(candidateOrders, selectedOrderId) {
        candidateOrders.firstOrNull { it.id == selectedOrderId }
    }

    val maxAllowedForSelectedOrder = remember(selectedOrder, existingTransaction) {
        if (selectedOrder == null) {
            null
        } else {
            val currentTxContribution = if (existingTransaction != null && existingTransaction.tradeOrderId == selectedOrder.id) {
                existingTransaction.amount
            } else {
                0L
            }
            selectedOrder.remainingDebt + currentTxContribution
        }
    }

    val totalDebtAcrossCandidateOrders = remember(candidateOrders) {
        candidateOrders.sumOf { it.remainingDebt.coerceAtLeast(0L) }
    }

    val titleText = when {
        isEditing && isCashIn -> "Sửa giao dịch nhận tiền"
        isEditing && !isCashIn -> "Sửa giao dịch chuyển tiền"
        isCashIn -> "Ghi nhận Nhận tiền (Thu tiền)"
        else -> "Ghi nhận Chuyển tiền (Chi tiền)"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                },
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
                    ScreenHelpButton(
                        guide = ScreenHelpGuides.cashTransactionForm(
                            isCashIn = isCashIn,
                            isEditing = isEditing
                        ),
                        onColoredBar = true
                    )
                    if (isEditing && onDeleteTransaction != null) {
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Xóa giao dịch",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isCashIn) GreenReceive else RedTransfer,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (errorMessage != null) {
                ErrorBanner(message = errorMessage)
            }

            // 1. Chọn Người đưa tiền / Người nhận tiền
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showContactPicker = true },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isCashIn) "1. Người đưa tiền (*)" else "1. Người nhận tiền (*)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (selectedContact != null) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = selectedContact.contact.fullName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    val debtNote = if (isCashIn) {
                                        "Người này đang nợ mình: ${CurrencyFormatter.formatVnd(selectedContact.receivableDebt)}"
                                    } else {
                                        "Mình đang nợ người này: ${CurrencyFormatter.formatVnd(selectedContact.payableDebt)}"
                                    }
                                    Text(
                                        text = debtNote,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isCashIn) GreenReceive else OrangePayable,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                modifier = Modifier.align(Alignment.End),
                                text = "Đổi người >",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    } else {
                        OutlinedButton(
                            onClick = { showContactPicker = true },
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isCashIn) "Chọn người đưa tiền hoặc thêm mới" else "Chọn người nhận tiền hoặc thêm mới",
                                fontSize = responsiveSp(12.5f),
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // 2. Liên kết với giao dịch Mua/Bán đang nợ (nếu có)
            if (selectedContact != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isCashIn) {
                                "2. Liên kết đơn Bán hàng để trừ nợ (Nếu khách trả nợ)"
                            } else {
                                "2. Liên kết đơn Mua hàng để trừ nợ (Nếu trả nợ mua bò)"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        if (candidateOrders.isEmpty()) {
                            Text(
                                text = "Người này hiện không có đơn ${targetOrderType.vietnameseLabel.lowercase()} nào còn nợ. Khoản này sẽ được ghi nhận là giao dịch độc lập.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            candidateOrders.forEach { order ->
                                val isSelected = (!autoAllocateOldest && selectedOrderId == order.id)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isSelected) {
                                                if (isCashIn) GreenReceiveContainer else OrangePayableContainer
                                            } else {
                                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                            }
                                        )
                                        .clickable {
                                            autoAllocateOldest = false
                                            selectedOrderId = order.id
                                        }
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = {
                                            autoAllocateOldest = false
                                            selectedOrderId = order.id
                                        }
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Đơn ${DateFormatter.formatDate(order.transactionDate)}: ${order.description}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Tổng: ${CurrencyFormatter.formatVnd(order.totalAmount)} • Còn nợ: ${CurrencyFormatter.formatVnd(order.remainingDebt)}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = if (isCashIn) GreenReceive else OrangePayable,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }

                            if (!isEditing && candidateOrders.size >= 2) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            autoAllocateOldest = true
                                            selectedOrderId = null
                                        }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = autoAllocateOldest,
                                        onClick = {
                                            autoAllocateOldest = true
                                            selectedOrderId = null
                                        }
                                    )
                                    Text(
                                        text = "Tự động trừ vào các đơn nợ cũ nhất (Tổng nợ: ${CurrencyFormatter.formatVnd(totalDebtAcrossCandidateOrders)})",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        autoAllocateOldest = false
                                        selectedOrderId = null
                                    }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (!autoAllocateOldest && selectedOrderId == null),
                                    onClick = {
                                        autoAllocateOldest = false
                                        selectedOrderId = null
                                    }
                                )
                                Text(
                                    text = "Không liên kết — Ghi nhận ${flowType.vietnameseLabel.lowercase()} độc lập",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            // 3. Số tiền, Ngày & Hình thức
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val quickFillDebt = when {
                        autoAllocateOldest -> totalDebtAcrossCandidateOrders
                        maxAllowedForSelectedOrder != null -> maxAllowedForSelectedOrder
                        else -> null
                    }

                    CurrencyTextField(
                        label = if (isCashIn) "Số tiền nhận (*)" else "Số tiền chuyển (*)",
                        amount = amount,
                        onAmountChange = { amount = it },
                        quickFillLabel = if (quickFillDebt != null && quickFillDebt > 0L) {
                            "Điền đủ nợ (${CurrencyFormatter.formatVnd(quickFillDebt)})"
                        } else null,
                        quickFillAmount = quickFillDebt,
                        errorMessage = if (maxAllowedForSelectedOrder != null && amount > maxAllowedForSelectedOrder) {
                            "Số tiền vượt quá số dư còn nợ của đơn (${CurrencyFormatter.formatVnd(maxAllowedForSelectedOrder)})."
                        } else null
                    )

                    if (selectedOrder != null && maxAllowedForSelectedOrder != null && amount in 1..maxAllowedForSelectedOrder) {
                        val remainingAfterThis = maxAllowedForSelectedOrder - amount
                        Text(
                            text = "Sau khi lưu khoản này, đơn ngày ${DateFormatter.formatDate(selectedOrder.transactionDate)} sẽ còn nợ: ${CurrencyFormatter.formatVnd(remainingAfterThis)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isCashIn) GreenReceive else OrangePayable
                        )
                    }

                    VietnameseDatePickerField(
                        label = if (isCashIn) "Ngày nhận tiền (*)" else "Ngày chuyển tiền (*)",
                        selectedDateMillis = transactionDate,
                        onDateSelected = { transactionDate = it }
                    )

                    Column {
                        Text(
                            text = if (isCashIn) "Hình thức nhận (*):" else "Hình thức chuyển (*):",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            PaymentMethod.entries.forEach { method ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { paymentMethodName = method.dbValue }
                                ) {
                                    RadioButton(
                                        selected = (paymentMethodName == method.dbValue),
                                        onClick = { paymentMethodName = method.dbValue }
                                    )
                                    Text(
                                        text = method.vietnameseLabel,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Nội dung, Ghi chú & Ảnh chứng từ
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = shortContent,
                        onValueChange = { shortContent = it },
                        label = { Text("Nội dung giao dịch") },
                        placeholder = {
                            Text(
                                if (isCashIn) "Ví dụ: Trả thêm tiền mua bò chuyến trước"
                                else "Ví dụ: Chuyển trả bớt tiền mua bò"
                            )
                        },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Ghi chú thêm") },
                        placeholder = { Text("Ví dụ: Chuyển khoản ngân hàng Agribank") },
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )

                    AttachmentPickerSection(
                        label = "Ảnh chứng từ / Biên lai chuyển khoản",
                        attachmentPaths = attachmentPaths,
                        onAddAttachmentPath = { attachmentPaths.add(it) },
                        onRemoveAttachmentPath = { attachmentPaths.remove(it) }
                    )
                }
            }

            Button(
                onClick = {
                    onSaveTransaction(
                        selectedContactId,
                        if (autoAllocateOldest) null else selectedOrderId,
                        autoAllocateOldest,
                        amount,
                        transactionDate,
                        PaymentMethod.fromDb(paymentMethodName),
                        shortContent,
                        note,
                        attachmentPaths.toList()
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCashIn) GreenReceive else RedTransfer
                )
            ) {
                Text(
                    text = if (isCashIn) "LƯU GIAO DỊCH NHẬN TIỀN" else "LƯU GIAO DỊCH CHUYỂN TIỀN",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showContactPicker) {
        ContactPickerBottomSheet(
            title = if (isCashIn) "Chọn người đưa tiền" else "Chọn người nhận tiền",
            contacts = contacts,
            onSelectContact = { chosen ->
                selectedContactId = chosen.contact.id
                selectedOrderId = null
                showContactPicker = false
            },
            onCreateAndSelectNewContact = { fullName, phone, address, contactNote ->
                onCreateNewContactInline(fullName, phone, address, contactNote) { newContactId ->
                    selectedContactId = newContactId
                    selectedOrderId = null
                    showContactPicker = false
                }
            },
            onDismiss = { showContactPicker = false }
        )
    }

    if (showDeleteConfirm && onDeleteTransaction != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Xác nhận xóa giao dịch", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Khi xóa giao dịch ${flowType.vietnameseLabel.lowercase()} này, số dư công nợ liên quan sẽ được tính lại tự động. " +
                        "Bạn có chắc chắn muốn xóa không?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteTransaction()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedTransfer)
                ) {
                    Text("Xóa giao dịch")
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

/**
 * Màn hình Lịch sử Giao dịch Nhận tiền & Chuyển tiền (Tab 2 trên Thanh điều hướng dưới cùng).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashTransactionListScreen(
    transactions: List<CashTransactionWithContact>,
    initialFilter: String = "ALL",
    onCreateCashInClick: () -> Unit,
    onCreateCashOutClick: () -> Unit,
    onTransactionClick: (CashTransactionWithContact) -> Unit,
    modifier: Modifier = Modifier
) {
    var filterType by rememberSaveable(initialFilter) { mutableStateOf(initialFilter) } // ALL | CASH_IN | CASH_OUT
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var timeFilterMode by rememberSaveable { mutableStateOf(TimeFilterMode.ALL) }
    var timeAnchorMillis by rememberSaveable { mutableLongStateOf(DateFormatter.todayNoonMillis()) }

    val filtered = remember(transactions, filterType, searchQuery, timeFilterMode, timeAnchorMillis) {
        transactions.filter { tx ->
            val matchType = when (filterType) {
                "CASH_IN" -> tx.flowType == CashFlowType.CASH_IN
                "CASH_OUT" -> tx.flowType == CashFlowType.CASH_OUT
                else -> true
            }
            val matchTime = when (timeFilterMode) {
                TimeFilterMode.ALL -> true
                TimeFilterMode.BY_MONTH -> DateFormatter.isSameMonth(tx.transactionDate, timeAnchorMillis)
                TimeFilterMode.BY_DAY -> DateFormatter.isSameDay(tx.transactionDate, timeAnchorMillis)
                TimeFilterMode.TODAY -> DateFormatter.isSameDay(tx.transactionDate, DateFormatter.todayNoonMillis())
            }
            val matchSearch = if (searchQuery.isBlank()) {
                true
            } else {
                VietnameseTextNormalizer.matchesQuery(tx.contactName, tx.contactPhone, searchQuery) ||
                    VietnameseTextNormalizer.normalizeName(tx.shortContent)
                        .contains(VietnameseTextNormalizer.normalizeName(searchQuery))
            }
            matchType && matchTime && matchSearch
        }
    }

    val groupedByDay = remember(filtered) {
        filtered.groupBy { DateFormatter.formatDate(it.transactionDate) }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Lịch sử Nhận tiền & Chuyển tiền", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onCreateCashInClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenReceive)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Nhận tiền", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onCreateCashOutClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = RedTransfer)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Chuyển tiền", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Tìm tên hoặc nội dung...", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Xóa")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Thanh lọc theo Ngày / Tháng / Hôm nay
            VietnameseTimeFilterBar(
                mode = timeFilterMode,
                onModeChange = { timeFilterMode = it },
                anchorMillis = timeAnchorMillis,
                onAnchorMillisChange = { timeAnchorMillis = it }
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filterType == "ALL",
                    onClick = { filterType = "ALL" },
                    label = { Text("Tất cả loại (${filtered.size})", maxLines = 1, softWrap = false) }
                )
                FilterChip(
                    selected = filterType == "CASH_IN",
                    onClick = { filterType = "CASH_IN" },
                    label = { Text("Nhận tiền", maxLines = 1, softWrap = false) }
                )
                FilterChip(
                    selected = filterType == "CASH_OUT",
                    onClick = { filterType = "CASH_OUT" },
                    label = { Text("Chuyển tiền", maxLines = 1, softWrap = false) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (filtered.isEmpty()) {
                EmptyStateView(
                    title = "Không có giao dịch trong kỳ đã chọn",
                    message = "Thử chuyển sang 'Tất cả' hoặc bấm mũi tên ◀ ▶ để xem tháng/ngày khác."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 84.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    groupedByDay.forEach { (dateKey, dayList) ->
                        val headerMillis = dayList.first().transactionDate
                        item(key = "header_$dateKey") {
                            DateSectionHeader(
                                dateMillis = headerMillis,
                                countLabel = "${dayList.size} giao dịch"
                            )
                        }
                        items(
                            items = dayList,
                            key = { it.id }
                        ) { tx ->
                            val isCashIn = tx.flowType == CashFlowType.CASH_IN
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onTransactionClick(tx) },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    // Dòng 1: Loại giao dịch & Số tiền
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        SemanticBadge(
                                            text = tx.flowType.vietnameseLabel,
                                            textColor = if (isCashIn) GreenReceive else RedTransfer,
                                            backgroundColor = if (isCashIn) GreenReceiveContainer else RedTransferContainer
                                        )
                                        Text(
                                            text = CurrencyFormatter.formatSignedVnd(tx.amount, isPositive = isCashIn),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontSize = responsiveSp(15f),
                                            fontWeight = FontWeight.Bold,
                                            color = if (isCashIn) GreenReceive else RedTransfer,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    // Dòng 2: Người đưa / Người nhận (1 dòng riêng tường minh)
                                    Text(
                                        text = "${if (isCashIn) "Người đưa tiền" else "Người nhận tiền"}: ${tx.contactName}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                    // Dòng 3: Hình thức thanh toán (1 dòng riêng tường minh)
                                    Text(
                                        text = "Hình thức: ${tx.paymentMethod.vietnameseLabel}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    // Dòng 4: Ngày giao dịch (1 dòng riêng tường minh)
                                    Text(
                                        text = "Ngày giao dịch: ${DateFormatter.formatDate(tx.transactionDate)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    // Dòng 5: Nội dung giao dịch (1 dòng riêng tường minh, bỏ mã #...)
                                    val cleanContent = tx.shortContent.replace(Regex("\\s*#\\d+"), "").trim()
                                    if (cleanContent.isNotBlank()) {
                                        Text(
                                            text = "Nội dung: $cleanContent",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
