package com.sotraubo.app.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sotraubo.app.core.common.format.CurrencyFormatter
import com.sotraubo.app.core.common.format.DateFormatter
import com.sotraubo.app.core.designsystem.component.CurrencyTextField
import com.sotraubo.app.core.designsystem.component.VietnameseDatePickerField
import com.sotraubo.app.core.designsystem.theme.GreenReceive
import com.sotraubo.app.core.designsystem.theme.OrangePayable

/**
 * Reusable dialog for recording an additional installment payment with amount, date, and note.
 */
@Composable
fun InstallmentPaymentDialog(
    title: String,
    remainingAmount: Long,
    onDismiss: () -> Unit,
    onConfirm: (addedAmount: Long, dateMillis: Long, installmentNote: String) -> Unit
) {
    var addedAmount by remember { mutableLongStateOf(0L) }
    var dateMillis by remember { mutableLongStateOf(DateFormatter.todayNoonMillis()) }
    var installmentNote by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (remainingAmount > 0L) {
                    Text(
                        text = "Còn thiếu hiện tại: ${CurrencyFormatter.formatVnd(remainingAmount)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = OrangePayable
                    )
                    OutlinedButton(
                        onClick = { addedAmount = remainingAmount },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Điền đủ ${CurrencyFormatter.formatVnd(remainingAmount)}",
                            color = GreenReceive,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                CurrencyTextField(
                    label = "Số tiền chuyển thêm (*)",
                    amount = addedAmount,
                    onAmountChange = { addedAmount = it },
                    showVietnameseWords = true
                )
                VietnameseDatePickerField(
                    label = "Ngày chuyển",
                    selectedDateMillis = dateMillis,
                    onDateSelected = { dateMillis = it }
                )
                OutlinedTextField(
                    value = installmentNote,
                    onValueChange = { installmentNote = it },
                    label = { Text("Ghi chú đợt này") },
                    placeholder = { Text("VD: Chuyển khoản ACB, trả tiền mặt...") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirm(addedAmount, dateMillis, installmentNote) },
                        enabled = addedAmount > 0L
                    ) {
                        Text("Lưu đợt này")
                    }
                }
            }
        }
    }
}
