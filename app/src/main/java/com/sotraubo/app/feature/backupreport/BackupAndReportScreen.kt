package com.sotraubo.app.feature.backupreport

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sotraubo.app.core.common.format.CurrencyFormatter
import com.sotraubo.app.core.common.format.DateFormatter
import com.sotraubo.app.core.designsystem.component.DropdownSelector
import com.sotraubo.app.core.designsystem.component.ErrorBanner
import com.sotraubo.app.core.designsystem.component.TimeFilterMode
import com.sotraubo.app.core.designsystem.component.VietnameseTimeFilterBar
import com.sotraubo.app.core.designsystem.theme.BluePrimary
import com.sotraubo.app.core.designsystem.theme.GreenReceive
import com.sotraubo.app.core.designsystem.theme.OrangePayable
import com.sotraubo.app.core.designsystem.theme.RedTransfer
import com.sotraubo.app.core.model.DashboardFinancialSummary
import com.sotraubo.app.core.model.PeriodBusinessStats
import com.sotraubo.app.feature.security.BiometricAuthHelper
import com.sotraubo.app.feature.security.BiometricAvailability
import com.sotraubo.app.feature.security.ChangePinDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupAndReportScreen(
    summary: DashboardFinancialSummary,
    periodStats: PeriodBusinessStats,
    statusMessage: String?,
    errorMessage: String?,
    pendingRestorePreview: BackupPreviewInfo?,
    onBack: () -> Unit,
    onExportBackupToUri: (Uri) -> Unit,
    onSelectBackupUriForValidation: (Uri) -> Unit,
    onConfirmRestoreOverwrite: () -> Unit,
    onCancelRestoreOverwrite: () -> Unit,
    onExportReportPdfToUri: (Uri, PdfReportCategory, TimeFilterMode, Long) -> Unit,
    onChangePin: (oldPin: String, newPin: String) -> Result<Unit>,
    onLockAppNow: () -> Unit,
    isBiometricEnabled: Boolean = false,
    onToggleBiometric: (Boolean) -> Unit = {},
    onClearAllDataWithPin: (String) -> Boolean = { false }
) {
    val context = LocalContext.current
    val biometricAvailability = remember(context) { BiometricAuthHelper.checkAvailability(context) }
    var showChangePinDialog by rememberSaveable { mutableStateOf(false) }
    var showClearDataDialog by rememberSaveable { mutableStateOf(false) }
    var selectedPdfCategory by rememberSaveable { mutableStateOf(PdfReportCategory.ALL) }
    var pdfTimeMode by rememberSaveable { mutableStateOf(TimeFilterMode.BY_DAY) }
    var pdfAnchorMillis by rememberSaveable { mutableLongStateOf(DateFormatter.todayNoonMillis()) }
    val exportBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip")
    ) { uri: Uri? ->
        if (uri != null) {
            onExportBackupToUri(uri)
        }
    }

    val importBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            onSelectBackupUriForValidation(uri)
        }
    }

    val exportPdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri: Uri? ->
        if (uri != null) {
            onExportReportPdfToUri(uri, selectedPdfCategory, pdfTimeMode, pdfAnchorMillis)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Báo cáo PDF & Sao lưu dữ liệu", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
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

            if (statusMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = com.sotraubo.app.core.designsystem.theme.GreenReceiveContainer
                    )
                ) {
                    Text(
                        text = statusMessage,
                        color = GreenReceive,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }

            // Khu vực 1: Báo cáo & Xuất PDF
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
                        text = "1. BÁO CÁO & XUẤT PDF",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    val netActualCash = summary.totalCashIn - summary.totalCashOut
                    val theoreticalProfit = summary.totalSaleAmount - summary.totalPurchaseAmount

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ReportHighlightBox(
                            title = "Tiền thực có",
                            amount = netActualCash,
                            modifier = Modifier.fillMaxWidth()
                        )
                        ReportHighlightBox(
                            title = "Lời lý thuyết",
                            amount = theoreticalProfit,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ReportStatRow(
                            label = "Đã thu thực tế",
                            value = CurrencyFormatter.formatVnd(summary.totalCashIn)
                        )
                        ReportStatRow(
                            label = "Đã chi thực tế",
                            value = CurrencyFormatter.formatVnd(summary.totalCashOut)
                        )
                        HorizontalDivider()
                        ReportStatRow(
                            label = "Tổng mua hàng",
                            value = CurrencyFormatter.formatVnd(summary.totalPurchaseAmount)
                        )
                        ReportStatRow(
                            label = "Tổng bán hàng",
                            value = CurrencyFormatter.formatVnd(summary.totalSaleAmount)
                        )
                        HorizontalDivider()
                        ReportStatRow(
                            label = "Phải thu (${summary.receivableDebtorCount} người)",
                            value = CurrencyFormatter.formatVnd(summary.totalReceivableDebt),
                            valueColor = GreenReceive,
                            boldValue = true
                        )
                        ReportStatRow(
                            label = "Phải trả (${summary.payableCreditorCount} người)",
                            value = CurrencyFormatter.formatVnd(summary.totalPayableDebt),
                            valueColor = OrangePayable,
                            boldValue = true
                        )
                    }

                    HorizontalDivider()

                    Text(
                        text = "Chọn phạm vi xuất tệp PDF",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    DropdownSelector(
                        prefix = "Loại báo cáo",
                        options = PdfReportCategory.entries,
                        selected = selectedPdfCategory,
                        optionLabel = { it.label },
                        onSelected = { selectedPdfCategory = it }
                    )

                    VietnameseTimeFilterBar(
                        mode = pdfTimeMode,
                        anchorMillis = pdfAnchorMillis,
                        onModeChange = { pdfTimeMode = it },
                        onAnchorMillisChange = { pdfAnchorMillis = it }
                    )

                    Text(
                        text = "Tạo tệp PDF khổ A4 rõ nét để gửi đối chiếu qua Zalo hoặc in lưu trữ.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = {
                            val timestamp = DateFormatter.formatFileSafeTimestamp(System.currentTimeMillis())
                            val prefix = when (selectedPdfCategory) {
                                PdfReportCategory.CATTLE_BUSINESS -> "BaoCao_TrauBo"
                                PdfReportCategory.BATH_EXCHANGE -> "BaoCao_DoiBath"
                                PdfReportCategory.ALL -> "BaoCao_QuanLy"
                            }
                            exportPdfLauncher.launch("${prefix}_$timestamp.pdf")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Xuất báo cáo PDF",
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            // Khu vực 2: Sao lưu & Khôi phục dữ liệu
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "2. SAO LƯU & KHÔI PHỤC DỮ LIỆU",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Ứng dụng lưu toàn bộ dữ liệu trực tiếp trên điện thoại của bạn (không cần mạng). " +
                            "Hãy xuất tệp sao lưu định kỳ để cất giữ hoặc chuyển sang điện thoại mới.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = {
                            val timestamp = DateFormatter.formatFileSafeTimestamp(System.currentTimeMillis())
                            exportBackupLauncher.launch("SoTrauBo_SaoLuu_$timestamp.zip")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenReceive)
                    ) {
                        Icon(Icons.Default.Backup, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Xuất dữ liệu ra tệp Sao lưu (.zip)", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            importBackupLauncher.launch(arrayOf("application/zip", "application/octet-stream", "*/*"))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Restore, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Khôi phục dữ liệu từ tệp Sao lưu", fontWeight = FontWeight.Bold)
                    }

                    HorizontalDivider()

                    OutlinedButton(
                        onClick = { showClearDataDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, RedTransfer),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RedTransfer)
                    ) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Xóa data (Bắt đầu lại từ đầu)", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Khu vực 3: Bảo mật Mật khẩu 6 số
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "3. BẢO MẬT MẬT KHẨU 6 SỐ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Ứng dụng luôn yêu cầu nhập mật khẩu 6 chữ số khi mở máy để bảo vệ thông tin tài chính và công nợ của bạn.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f).padding(end = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = null,
                                tint = if (biometricAvailability == BiometricAvailability.AVAILABLE) {
                                    BluePrimary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                            Column {
                                Text(
                                    text = "Mở khóa vân tay / khuôn mặt",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = when (biometricAvailability) {
                                        BiometricAvailability.AVAILABLE ->
                                            "Cho phép mở sổ nhanh bằng vân tay hoặc khuôn mặt (tùy thiết bị)."
                                        BiometricAvailability.NONE_ENROLLED ->
                                            "Thiết bị chưa cài đặt vân tay hoặc khuôn mặt trong Cài đặt máy."
                                        BiometricAvailability.UNAVAILABLE ->
                                            "Thiết bị không hỗ trợ cảm biến sinh trắc học."
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = biometricAvailability == BiometricAvailability.AVAILABLE && isBiometricEnabled,
                            onCheckedChange = onToggleBiometric,
                            enabled = biometricAvailability == BiometricAvailability.AVAILABLE
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { showChangePinDialog = true },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Đổi mật khẩu",
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        OutlinedButton(
                            onClick = onLockAppNow,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Khóa sổ ngay",
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }

    if (showChangePinDialog) {
        ChangePinDialog(
            onDismiss = { showChangePinDialog = false },
            onSubmitChangePin = onChangePin
        )
    }

    // Hộp thoại Cảnh báo trước khi ghi đè dữ liệu (Mục 8 Master Prompt)
    if (pendingRestorePreview != null) {
        AlertDialog(
            onDismissRequest = onCancelRestoreOverwrite,
            icon = {
                Icon(
                    imageVector = Icons.Default.WarningAmber,
                    contentDescription = "Cảnh báo",
                    tint = RedTransfer
                )
            },
            title = {
                Text(
                    text = "Cảnh báo trước khi ghi đè dữ liệu",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Tệp sao lưu đã được kiểm tra hợp lệ:")
                    Text("• Ngày tạo bản sao lưu: ${pendingRestorePreview.createdAtDisplay}")
                    Text("• Số người liên quan: ${pendingRestorePreview.contactCount} người")
                    Text("• Số đơn mua/bán: ${pendingRestorePreview.tradeOrderCount} đơn")
                    Text("• Số giao dịch nhận/chuyển: ${pendingRestorePreview.cashTransactionCount} giao dịch")
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "LƯU Ý QUAN TRỌNG: Khôi phục sẽ GHI ĐÈ và thay thế toàn bộ dữ liệu hiện tại trên máy bằng dữ liệu trong tệp sao lưu này. Bạn có chắc chắn muốn tiếp tục không?",
                        fontWeight = FontWeight.Bold,
                        color = RedTransfer
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = onConfirmRestoreOverwrite,
                    colors = ButtonDefaults.buttonColors(containerColor = RedTransfer)
                ) {
                    Text("Đồng ý ghi đè & Khôi phục", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = onCancelRestoreOverwrite) {
                    Text("Hủy thao tác")
                }
            }
        )
    }

    if (showClearDataDialog) {
        var pinInput by rememberSaveable { mutableStateOf("") }
        var pinError by rememberSaveable { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = null,
                    tint = RedTransfer
                )
            },
            title = { Text("Xóa toàn bộ dữ liệu", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Thao tác này sẽ xóa toàn bộ đơn hàng, thu chi, công nợ, đối tác, đổi Bath, phí Quota, chi sinh hoạt và đặt lại vốn ban đầu về 0 đ để bắt đầu lại từ đầu.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { raw ->
                            pinInput = raw.filter { it.isDigit() }.take(6)
                            pinError = null
                        },
                        label = { Text("Nhập mật khẩu 6 số để xác nhận") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        isError = pinError != null,
                        supportingText = pinError?.let { err -> { Text(err, color = RedTransfer) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (onClearAllDataWithPin(pinInput)) {
                            showClearDataDialog = false
                        } else {
                            pinError = "Mật khẩu 6 số không đúng."
                        }
                    },
                    enabled = pinInput.length == 6,
                    colors = ButtonDefaults.buttonColors(containerColor = RedTransfer)
                ) {
                    Text("Xóa data", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
private fun ReportHighlightBox(
    title: String,
    amount: Long,
    modifier: Modifier = Modifier
) {
    val isPositive = amount >= 0L
    val accent = if (isPositive) GreenReceive else RedTransfer
    val bgColor = accent.copy(alpha = 0.14f)
    val prefix = if (amount > 0L) "+" else ""

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        Text(
            text = "$prefix${CurrencyFormatter.formatVnd(amount)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = accent,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
private fun ReportStatRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    boldValue: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (boldValue) FontWeight.Bold else FontWeight.SemiBold,
            color = valueColor,
            maxLines = 1,
            softWrap = false
        )
    }
}
