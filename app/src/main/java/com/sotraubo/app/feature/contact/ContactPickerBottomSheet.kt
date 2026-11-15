package com.sotraubo.app.feature.contact

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sotraubo.app.core.common.format.CurrencyFormatter
import com.sotraubo.app.core.common.search.VietnameseTextNormalizer
import com.sotraubo.app.core.designsystem.component.ErrorBanner
import com.sotraubo.app.core.designsystem.component.SemanticBadge
import com.sotraubo.app.core.designsystem.theme.GreenReceive
import com.sotraubo.app.core.designsystem.theme.GreenReceiveContainer
import com.sotraubo.app.core.designsystem.theme.OrangePayable
import com.sotraubo.app.core.designsystem.theme.OrangePayableContainer
import com.sotraubo.app.core.model.ContactWithSummary

/**
 * Hộp thoại trượt lên (Bottom Sheet) cho phép:
 * 1. Tìm kiếm người liên quan đã lưu theo Tên (có dấu / không dấu) hoặc Số điện thoại.
 * 2. Hiển thị công nợ 2 chiều ngay dưới tên từng người.
 * 3. Nút "+ Thêm người mới" ngay tại chỗ và tự động chọn người vừa tạo cho biểu mẫu hiện tại.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactPickerBottomSheet(
    title: String,
    contacts: List<ContactWithSummary>,
    onSelectContact: (ContactWithSummary) -> Unit,
    onCreateAndSelectNewContact: (fullName: String, phone: String, address: String, note: String) -> Unit,
    onDismiss: () -> Unit,
    errorMessage: String? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showAddNewDialog by rememberSaveable { mutableStateOf(false) }

    val filteredContacts = remember(contacts, searchQuery) {
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

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Đóng"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Tìm tên hoặc SĐT...", maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Tìm kiếm"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Xóa từ khóa"
                            )
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { showAddNewDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                val buttonText = if (searchQuery.isNotBlank()) {
                    "Thêm người mới (\"${searchQuery.trim()}\")"
                } else {
                    "Thêm người mới"
                }
                Text(text = buttonText, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredContacts.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (searchQuery.isBlank()) {
                            "Danh bạ hiện chưa có người nào."
                        } else {
                            "Không tìm thấy người nào khớp với \"$searchQuery\"."
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Nhấn nút 'Thêm người mới' ở trên để tạo nhanh.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Text(
                    text = "Danh sách người đã lưu (${filteredContacts.size} người):",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = filteredContacts,
                        key = { it.contact.id }
                    ) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectContact(item) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
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
                                        text = "Chọn >",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (item.contact.phone.isNotBlank() || item.contact.address.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    val sub = listOfNotNull(
                                        item.contact.phone.takeIf { it.isNotBlank() },
                                        item.contact.address.takeIf { it.isNotBlank() }
                                    ).joinToString(" • ")
                                    Text(
                                        text = sub,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (item.receivableDebt > 0L || item.payableDebt > 0L) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
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
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddNewDialog) {
        val initialDigits = searchQuery.filter { it.isDigit() }
        val isPhoneQuery = initialDigits.length >= 7
        QuickCreateContactDialog(
            initialName = if (isPhoneQuery) "" else searchQuery.trim(),
            initialPhone = if (isPhoneQuery) searchQuery.trim() else "",
            errorMessage = errorMessage,
            onDismiss = { showAddNewDialog = false },
            onConfirmSave = { fullName, phone, address, note ->
                onCreateAndSelectNewContact(fullName, phone, address, note)
                showAddNewDialog = false
            }
        )
    }
}

@Composable
fun QuickCreateContactDialog(
    title: String = "Thêm người liên quan mới",
    initialName: String = "",
    initialPhone: String = "",
    initialAddress: String = "",
    initialNote: String = "",
    errorMessage: String? = null,
    onDismiss: () -> Unit,
    onConfirmSave: (fullName: String, phone: String, address: String, note: String) -> Unit
) {
    var fullName by rememberSaveable { mutableStateOf(initialName) }
    var phone by rememberSaveable { mutableStateOf(initialPhone) }
    var address by rememberSaveable { mutableStateOf(initialAddress) }
    var note by rememberSaveable { mutableStateOf(initialNote) }
    var localError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val displayError = localError ?: errorMessage
                if (displayError != null) {
                    ErrorBanner(message = displayError)
                }

                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        localError = null
                    },
                    label = { Text("Họ và tên (*)") },
                    placeholder = { Text("Ví dụ: Nguyễn Văn A") },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { raw -> phone = sanitizePhoneInput(raw) },
                    label = { Text("Số điện thoại") },
                    placeholder = { Text("Ví dụ: 0912 345 678") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Địa chỉ / Khu vực") },
                    placeholder = { Text("Ví dụ: Xã Tân Lập, Đan Phượng") },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Ghi chú") },
                    placeholder = { Text("Ví dụ: Bạn buôn bò thịt BBB") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (fullName.trim().isEmpty()) {
                        localError = "Vui lòng nhập họ và tên người liên quan."
                    } else {
                        onConfirmSave(fullName.trim(), phone.trim(), address.trim(), note.trim())
                    }
                }
            ) {
                Text(text = "Lưu lại", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Hủy")
            }
        }
    )
}

/**
 * Lọc bỏ chữ cái và ký tự đặc biệt khi nhập hoặc dán số điện thoại,
 * chỉ giữ chữ số, dấu '+' ở đầu (mã quốc gia) và khoảng trắng phân cách.
 */
internal fun sanitizePhoneInput(raw: String): String {
    return buildString {
        raw.forEach { ch ->
            when {
                ch.isDigit() -> append(ch)
                ch == '+' && isEmpty() -> append(ch)
                ch == ' ' && isNotEmpty() && last() != ' ' -> append(ch)
            }
        }
    }.take(16)
}
