package com.sotraubo.app.feature.trade

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.sp
import com.sotraubo.app.core.common.format.CurrencyFormatter
import com.sotraubo.app.core.common.format.DateFormatter
import com.sotraubo.app.core.common.format.InstallmentNoteFormatter
import com.sotraubo.app.core.common.search.VietnameseTextNormalizer
import com.sotraubo.app.core.designsystem.theme.responsiveSp
import com.sotraubo.app.core.ui.components.InstallmentPaymentDialog
import com.sotraubo.app.core.designsystem.component.AttachmentPickerSection
import com.sotraubo.app.core.designsystem.component.CurrencyTextField
import com.sotraubo.app.core.designsystem.component.DateSectionHeader
import com.sotraubo.app.core.designsystem.component.EmptyStateView
import com.sotraubo.app.core.designsystem.component.ErrorBanner
import com.sotraubo.app.core.designsystem.component.SemanticBadge
import com.sotraubo.app.core.designsystem.component.TimeFilterMode
import com.sotraubo.app.core.designsystem.component.VietnameseDatePickerField
import com.sotraubo.app.core.designsystem.component.VietnameseTimeFilterBar
import com.sotraubo.app.core.designsystem.theme.BlueContainer
import com.sotraubo.app.core.designsystem.theme.BluePrimary
import com.sotraubo.app.core.designsystem.theme.GreenReceive
import com.sotraubo.app.core.designsystem.theme.GreenReceiveContainer
import com.sotraubo.app.core.designsystem.theme.OrangePayable
import com.sotraubo.app.core.designsystem.theme.OrangePayableContainer
import com.sotraubo.app.core.designsystem.theme.RedTransfer
import com.sotraubo.app.core.designsystem.theme.RedTransferContainer
import com.sotraubo.app.core.model.ContactWithSummary
import com.sotraubo.app.core.model.PaymentMethod
import com.sotraubo.app.core.model.TradeOrderType
import com.sotraubo.app.core.model.TradeOrderWithPayment
import com.sotraubo.app.feature.contact.ContactPickerBottomSheet

/**
 * Biểu mẫu tạo / chỉnh sửa Đơn Mua Hàng (Mua trâu bò) hoặc Đơn Bán Hàng (Bán trâu bò).
 * - Chọn lại người đã lưu hoặc tạo người mới ngay trong luồng.
 * - Tự động tính Số tiền còn nợ = Tổng tiền - Đã trả/nhận.
 * - Chặn thanh toán vượt tổng tiền đơn hàng.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeOrderFormScreen(
    orderType: TradeOrderType,
    contacts: List<ContactWithSummary>,
    preselectedContactId: Long? = null,
    existingOrder: TradeOrderWithPayment? = null,
    existingInitialPaidAmount: Long? = null,
    existingInitialPaymentMethod: PaymentMethod? = null,
    errorMessage: String? = null,
    onBack: () -> Unit,
    onCreateNewContactInline: (fullName: String, phone: String, address: String, note: String, onCreated: (Long) -> Unit) -> Unit,
    onSaveOrder: (
        orderType: TradeOrderType,
        contactId: Long?,
        transactionDate: Long,
        cattleQuantity: Int,
        description: String,
        totalAmount: Long,
        initialPaidAmount: Long,
        initialPaymentMethod: PaymentMethod,
        note: String,
        attachmentPaths: List<String>
    ) -> Unit,
    onDeleteOrder: (() -> Unit)? = null,
    onRecordInstallment: ((addedAmount: Long, dateMillis: Long, installmentNote: String, updatedOrderNote: String) -> Unit)? = null
) {
    val isEditing = (existingOrder != null)
    var activeOrderTypeName by rememberSaveable(orderType, existingOrder?.id) {
        mutableStateOf((existingOrder?.orderType ?: orderType).name)
    }
    val activeOrderType = TradeOrderType.valueOf(activeOrderTypeName)
    val isPurchase = (activeOrderType == TradeOrderType.PURCHASE)

    var selectedContactId by rememberSaveable {
        mutableStateOf(existingOrder?.contactId ?: preselectedContactId)
    }
    var transactionDate by rememberSaveable {
        mutableLongStateOf(existingOrder?.transactionDate ?: DateFormatter.todayNoonMillis())
    }
    var cattleQuantity by rememberSaveable {
        mutableIntStateOf(existingOrder?.cattleQuantity ?: 1)
    }
    var description by rememberSaveable {
        mutableStateOf(existingOrder?.description ?: "")
    }
    var totalAmount by rememberSaveable {
        mutableLongStateOf(existingOrder?.totalAmount ?: 0L)
    }
    var initialPaidAmount by rememberSaveable {
        mutableLongStateOf(existingInitialPaidAmount ?: existingOrder?.paidAmount ?: 0L)
    }
    var paymentMethodName by rememberSaveable {
        mutableStateOf((existingInitialPaymentMethod ?: PaymentMethod.CASH).dbValue)
    }
    var note by rememberSaveable {
        mutableStateOf(existingOrder?.note ?: "")
    }
    val attachmentPaths = remember {
        mutableStateListOf<String>().apply {
            existingOrder?.attachmentPaths?.let { addAll(it) }
        }
    }

    LaunchedEffect(existingOrder?.id, existingInitialPaidAmount) {
        if (existingOrder != null) {
            selectedContactId = existingOrder.contactId
            transactionDate = existingOrder.transactionDate
            cattleQuantity = existingOrder.cattleQuantity
            description = existingOrder.description
            totalAmount = existingOrder.totalAmount
            initialPaidAmount = existingInitialPaidAmount ?: existingOrder.paidAmount
            note = existingOrder.note
            attachmentPaths.clear()
            attachmentPaths.addAll(existingOrder.attachmentPaths)
        }
    }

    var showContactPicker by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }
    var showInstallmentDialog by rememberSaveable { mutableStateOf(false) }

    val selectedContact = remember(contacts, selectedContactId) {
        contacts.firstOrNull { it.contact.id == selectedContactId }
    }

    val subsequentPaidAmount = remember(existingOrder, existingInitialPaidAmount) {
        if (existingOrder != null && existingInitialPaidAmount != null) {
            (existingOrder.paidAmount - existingInitialPaidAmount).coerceAtLeast(0L)
        } else {
            0L
        }
    }

    val totalPaidPreview = initialPaidAmount + subsequentPaidAmount
    val remainingDebtPreview = (totalAmount - totalPaidPreview).coerceAtLeast(0L)
    val isOverpaid = totalAmount > 0L && totalPaidPreview > totalAmount

    if (showInstallmentDialog && isEditing && onRecordInstallment != null) {
        InstallmentPaymentDialog(
            title = if (isPurchase) "Ghi thêm đợt trả tiền" else "Ghi thêm đợt khách trả tiền",
            remainingAmount = remainingDebtPreview,
            onDismiss = { showInstallmentDialog = false },
            onConfirm = { addedAmount, payDate, payNote ->
                val newTotalPaid = totalPaidPreview + addedAmount
                val remainingAfter = (totalAmount - newTotalPaid).coerceAtLeast(0L)
                val updatedOrderNote = InstallmentNoteFormatter.appendInstallmentNote(
                    existingNote = note,
                    dateMillis = payDate,
                    addedAmount = addedAmount,
                    newTotalPaid = newTotalPaid,
                    remainingAfter = remainingAfter,
                    actionLabel = if (isPurchase) "Trả thêm" else "Khách trả thêm",
                    installmentNote = payNote
                )
                note = updatedOrderNote
                showInstallmentDialog = false
                onRecordInstallment(addedAmount, payDate, payNote, updatedOrderNote)
            }
        )
    }

    val screenTitle = when {
        isEditing && isPurchase -> "Sửa đơn mua trâu bò"
        isEditing && !isPurchase -> "Sửa đơn bán trâu bò"
        isPurchase -> "Tạo đơn mua hàng (Mua trâu bò)"
        else -> "Tạo đơn bán hàng (Bán trâu bò)"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(screenTitle, fontWeight = FontWeight.Bold) },
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
                    if (isEditing && onDeleteOrder != null) {
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Xóa đơn",
                                tint = Color.White
                            )
                        }
                    }
                },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (errorMessage != null) {
                ErrorBanner(message = errorMessage)
            }

            if (!isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val isTransferSelected = activeOrderType == TradeOrderType.PURCHASE
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { activeOrderTypeName = TradeOrderType.PURCHASE.name },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isTransferSelected) RedTransfer.copy(alpha = 0.24f) else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            width = if (isTransferSelected) 2.dp else 1.dp,
                            color = if (isTransferSelected) RedTransfer else MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = if (isTransferSelected) Color.White else Color.White.copy(alpha = 0.75f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "Chuyển",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Mua hàng",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isTransferSelected) Color.White.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.65f)
                                )
                            }
                        }
                    }

                    val isReceiveSelected = activeOrderType == TradeOrderType.SALE
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { activeOrderTypeName = TradeOrderType.SALE.name },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isReceiveSelected) GreenReceive.copy(alpha = 0.24f) else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            width = if (isReceiveSelected) 2.dp else 1.dp,
                            color = if (isReceiveSelected) GreenReceive else MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = null,
                                tint = if (isReceiveSelected) Color.White else Color.White.copy(alpha = 0.75f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "Nhận",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Bán hàng",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isReceiveSelected) Color.White.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.65f)
                                )
                            }
                        }
                    }
                }
            }

            // 1. Chọn người bán / người mua
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
                        text = if (isPurchase) "1. Người bán trâu bò (*)" else "1. Người mua trâu bò (*)",
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
                                    if (selectedContact.contact.phone.isNotBlank()) {
                                        Text(
                                            text = "SĐT: ${selectedContact.contact.phone}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
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
                                text = if (isPurchase) "Chọn người bán hoặc thêm người mới" else "Chọn người mua hoặc thêm người mới",
                                fontSize = responsiveSp(12.5f),
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // 2. Ngày mua/bán & Số lượng trâu bò
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
                    VietnameseDatePickerField(
                        label = if (isPurchase) "2. Ngày mua hàng (*)" else "2. Ngày bán hàng (*)",
                        selectedDateMillis = transactionDate,
                        onDateSelected = { transactionDate = it }
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "3. Số lượng trâu/bò (*)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = Color.Transparent,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(
                                    onClick = { if (cattleQuantity > 1) cattleQuantity-- },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Giảm số lượng")
                                }
                                Text(
                                    text = "$cattleQuantity con",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(
                                    onClick = { cattleQuantity++ },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Tăng số lượng")
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("4. Mô tả trâu/bò hoặc lô hàng (*)") },
                        placeholder = { Text("Ví dụ: 2 bò đực BBB + 1 bò cái vàng") },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 3. Tổng tiền, Đã trả/Đã nhận & Còn nợ
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
                    CurrencyTextField(
                        label = if (isPurchase) "5. Tổng tiền mua (*)" else "5. Tổng tiền bán (*)",
                        amount = totalAmount,
                        onAmountChange = { totalAmount = it }
                    )

                    val maxInitialAllowed = (totalAmount - subsequentPaidAmount).coerceAtLeast(0L)
                    CurrencyTextField(
                        label = "6. Đã thanh toán",
                        amount = initialPaidAmount,
                        onAmountChange = { initialPaidAmount = it },
                        quickFillLabel = if (totalAmount > 0L) {
                            if (isPurchase) "Trả đủ (${CurrencyFormatter.formatCompactVnd(maxInitialAllowed)})"
                            else "Thu đủ (${CurrencyFormatter.formatCompactVnd(maxInitialAllowed)})"
                        } else null,
                        quickFillAmount = if (totalAmount > 0L) maxInitialAllowed else null,
                        errorMessage = if (isOverpaid) {
                            if (isPurchase) "Số tiền đã trả không được vượt quá tổng tiền mua."
                            else "Số tiền đã nhận không được vượt quá tổng tiền bán."
                        } else null
                    )

                    if (subsequentPaidAmount > 0L) {
                        Text(
                            text = "Đã có các lần trả thêm sau đó: ${CurrencyFormatter.formatVnd(subsequentPaidAmount)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = BluePrimary
                        )
                    }

                    // Chọn hình thức thanh toán (Tiền mặt / Chuyển khoản)
                    if (initialPaidAmount > 0L) {
                        Column {
                            Text(
                                text = if (isPurchase) "Hình thức trả tiền:" else "Hình thức nhận tiền:",
                                style = MaterialTheme.typography.labelLarge
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

                    // Hộp hiển thị nổi bật Số tiền còn nợ
                    val debtBoxBg = if (remainingDebtPreview == 0L) {
                        GreenReceiveContainer
                    } else if (isPurchase) {
                        OrangePayableContainer
                    } else {
                        GreenReceiveContainer
                    }
                    val debtTextColor = if (remainingDebtPreview == 0L) {
                        GreenReceive
                    } else if (isPurchase) {
                        OrangePayable
                    } else {
                        GreenReceive
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(debtBoxBg)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = if (isPurchase) "SỐ TIỀN MÌNH CÒN NỢ ĐƠN NÀY:" else "SỐ TIỀN KHÁCH CÒN NỢ ĐƠN NÀY:",
                            style = MaterialTheme.typography.labelLarge,
                            color = debtTextColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = CurrencyFormatter.formatVnd(remainingDebtPreview),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = debtTextColor
                        )
                        Text(
                            text = if (isPurchase) {
                                "Tổng: ${CurrencyFormatter.formatVnd(totalAmount)} • Đã trả: ${CurrencyFormatter.formatVnd(totalPaidPreview)}"
                            } else {
                                "Tổng: ${CurrencyFormatter.formatVnd(totalAmount)} • Đã thu: ${CurrencyFormatter.formatVnd(totalPaidPreview)}"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = debtTextColor
                        )
                        if (isEditing && onRecordInstallment != null && remainingDebtPreview > 0L) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { showInstallmentDialog = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isPurchase) "Ghi thêm đợt trả tiền" else "Ghi thêm đợt khách trả tiền",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // 4. Ghi chú & Ảnh đính kèm
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
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("7. Ghi chú thêm") },
                        placeholder = { Text("Ví dụ: Hẹn cuối tuần thanh toán nốt") },
                        maxLines = 6,
                        modifier = Modifier.fillMaxWidth()
                    )

                    AttachmentPickerSection(
                        label = "8. Ảnh đính kèm (Ảnh trâu bò / Giấy tờ)",
                        attachmentPaths = attachmentPaths,
                        onAddAttachmentPath = { attachmentPaths.add(it) },
                        onRemoveAttachmentPath = { attachmentPaths.remove(it) }
                    )
                }
            }

            Button(
                onClick = {
                    onSaveOrder(
                        activeOrderType,
                        selectedContactId,
                        transactionDate,
                        cattleQuantity,
                        description,
                        totalAmount,
                        initialPaidAmount,
                        PaymentMethod.fromDb(paymentMethodName),
                        note,
                        attachmentPaths.toList()
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (isPurchase) "LƯU ĐƠN MUA HÀNG" else "LƯU ĐƠN BÁN HÀNG",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showContactPicker) {
        ContactPickerBottomSheet(
            title = if (isPurchase) "Chọn người bán trâu bò" else "Chọn người mua trâu bò",
            contacts = contacts,
            onSelectContact = { chosen ->
                selectedContactId = chosen.contact.id
                showContactPicker = false
            },
            onCreateAndSelectNewContact = { fullName, phone, address, contactNote ->
                onCreateNewContactInline(fullName, phone, address, contactNote) { newContactId ->
                    selectedContactId = newContactId
                    showContactPicker = false
                }
            },
            onDismiss = { showContactPicker = false }
        )
    }

    if (showDeleteConfirm && onDeleteOrder != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Xác nhận xóa đơn hàng", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Khi xóa đơn ${orderType.vietnameseLabel.lowercase()} này, các khoản tiền thanh toán đã liên kết " +
                        "và số dư công nợ của đơn này cũng sẽ bị xóa. Bạn có chắc chắn muốn xóa không?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteOrder()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedTransfer)
                ) {
                    Text("Xóa đơn hàng")
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
 * Màn hình Danh sách Mua bán (Tab 3 trên Thanh điều hướng dưới cùng).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeOrderListScreen(
    orders: List<TradeOrderWithPayment>,
    onCreatePurchaseClick: () -> Unit,
    onCreateSaleClick: () -> Unit,
    onOrderClick: (TradeOrderWithPayment) -> Unit,
    onSettleOrderDebtClick: (TradeOrderWithPayment) -> Unit,
    modifier: Modifier = Modifier
) {
    var filterType by rememberSaveable { mutableStateOf("ALL") } // ALL | PURCHASE | SALE | UNPAID
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var timeFilterMode by rememberSaveable { mutableStateOf(TimeFilterMode.ALL) }
    var timeAnchorMillis by rememberSaveable { mutableLongStateOf(DateFormatter.todayNoonMillis()) }

    val filteredOrders = remember(orders, filterType, searchQuery, timeFilterMode, timeAnchorMillis) {
        orders.filter { order ->
            val matchType = when (filterType) {
                "PURCHASE" -> order.orderType == TradeOrderType.PURCHASE
                "SALE" -> order.orderType == TradeOrderType.SALE
                "UNPAID" -> order.remainingDebt > 0L
                else -> true
            }
            val matchTime = when (timeFilterMode) {
                TimeFilterMode.ALL -> true
                TimeFilterMode.BY_MONTH -> DateFormatter.isSameMonth(order.transactionDate, timeAnchorMillis)
                TimeFilterMode.BY_DAY -> DateFormatter.isSameDay(order.transactionDate, timeAnchorMillis)
                TimeFilterMode.TODAY -> DateFormatter.isSameDay(order.transactionDate, DateFormatter.todayNoonMillis())
            }
            val matchSearch = if (searchQuery.isBlank()) {
                true
            } else {
                VietnameseTextNormalizer.matchesQuery(order.contactName, order.contactPhone, searchQuery) ||
                    VietnameseTextNormalizer.normalizeName(order.description)
                        .contains(VietnameseTextNormalizer.normalizeName(searchQuery))
            }
            matchType && matchTime && matchSearch
        }
    }

    val groupedByDay = remember(filteredOrders) {
        filteredOrders.groupBy { DateFormatter.formatDate(it.transactionDate) }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Hoạt động Mua bán trâu bò", fontWeight = FontWeight.Bold) },
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
                    onClick = onCreatePurchaseClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Mua hàng", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onCreateSaleClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenReceive)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Bán hàng", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Tìm tên hoặc mô tả trâu bò...", maxLines = 1, overflow = TextOverflow.Ellipsis) },
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

            // Thanh lọc thời gian theo Tháng / Ngày / Hôm nay
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
                    label = { Text("Tất cả loại (${filteredOrders.size})", maxLines = 1, softWrap = false) }
                )
                FilterChip(
                    selected = filterType == "PURCHASE",
                    onClick = { filterType = "PURCHASE" },
                    label = { Text("Mua hàng", maxLines = 1, softWrap = false) }
                )
                FilterChip(
                    selected = filterType == "SALE",
                    onClick = { filterType = "SALE" },
                    label = { Text("Bán hàng", maxLines = 1, softWrap = false) }
                )
                FilterChip(
                    selected = filterType == "UNPAID",
                    onClick = { filterType = "UNPAID" },
                    label = { Text("Còn nợ", maxLines = 1, softWrap = false) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredOrders.isEmpty()) {
                EmptyStateView(
                    title = "Không có đơn mua bán trong kỳ đã chọn",
                    message = "Thử chuyển sang 'Tất cả' hoặc bấm mũi tên ◀ ▶ để xem tháng/ngày khác."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 84.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    groupedByDay.forEach { (dateKey, dayOrders) ->
                        val headerMillis = dayOrders.first().transactionDate
                        item(key = "header_$dateKey") {
                            DateSectionHeader(
                                dateMillis = headerMillis,
                                countLabel = "${dayOrders.size} đơn (${dayOrders.sumOf { it.cattleQuantity }} con)"
                            )
                        }
                        items(
                            items = dayOrders,
                            key = { it.id }
                        ) { order ->
                            val isSale = order.orderType == TradeOrderType.SALE
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOrderClick(order) },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    // Dòng 1: Loại đơn & Ngày giao dịch
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        SemanticBadge(
                                            text = order.orderType.vietnameseLabel,
                                            textColor = if (isSale) GreenReceive else BluePrimary,
                                            backgroundColor = if (isSale) GreenReceiveContainer else BlueContainer
                                        )
                                        Text(
                                            text = "Ngày: ${DateFormatter.formatDate(order.transactionDate)}",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }

                                    // Dòng 2: Người mua / Người bán (tường minh 1 dòng riêng)
                                    Text(
                                        text = "${if (isSale) "Người mua" else "Người bán"}: ${order.contactName}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                    // Dòng 3: Số lượng trâu bò (tường minh 1 dòng riêng)
                                    Text(
                                        text = "Số lượng: ${order.cattleQuantity} con",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    // Dòng 4: Mô tả chi tiết (tường minh 1 dòng riêng)
                                    Text(
                                        text = "Mô tả: ${order.description}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    // Dòng 5: Tổng tiền đơn hàng (tường minh 1 dòng riêng)
                                    Text(
                                        text = "Tổng tiền đơn: ${CurrencyFormatter.formatVnd(order.totalAmount)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    // Dòng 6: Đã thanh toán (tường minh 1 dòng riêng)
                                    Text(
                                        text = "${if (isSale) "Đã nhận" else "Đã trả"}: ${CurrencyFormatter.formatVnd(order.paidAmount)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    Spacer(modifier = Modifier.height(2.dp))

                                    // Dòng 7: Trạng thái công nợ & Nút hành động
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (order.remainingDebt > 0L) {
                                            Text(
                                                text = "Còn nợ: ${CurrencyFormatter.formatVnd(order.remainingDebt)}",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSale) GreenReceive else OrangePayable,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            OutlinedButton(
                                                onClick = { onSettleOrderDebtClick(order) },
                                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                            ) {
                                                Text(
                                                    text = if (isSale) "Thu tiền" else "Trả tiền",
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 1,
                                                    softWrap = false
                                                )
                                            }
                                        } else {
                                            SemanticBadge(
                                                text = "Đã thanh toán đủ (0 đ)",
                                                textColor = GreenReceive,
                                                backgroundColor = GreenReceiveContainer
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
}
