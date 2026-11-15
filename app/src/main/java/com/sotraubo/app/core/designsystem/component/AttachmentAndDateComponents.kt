package com.sotraubo.app.core.designsystem.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sotraubo.app.core.common.format.DateFormatter
import com.sotraubo.app.core.designsystem.theme.BlueContainer
import com.sotraubo.app.core.designsystem.theme.BluePrimary
import com.sotraubo.app.core.designsystem.theme.SurfaceElevatedDark
import com.sotraubo.app.core.designsystem.theme.responsiveSp
import java.io.File
import java.io.FileOutputStream

object AttachmentStorageHelper {
    private const val SUBDIR = "attachments"

    fun copyAndCompressUriToInternalStorage(context: Context, uri: Uri): String? {
        return try {
            val dir = File(context.filesDir, SUBDIR).apply { if (!exists()) mkdirs() }
            val fileName = "ChungTu_${System.currentTimeMillis()}.jpg"
            val destFile = File(dir, fileName)

            context.contentResolver.openInputStream(uri)?.use { input ->
                val originalBitmap = BitmapFactory.decodeStream(input)
                if (originalBitmap != null) {
                    val maxDim = 1600
                    val ratio = minOf(
                        maxDim.toFloat() / originalBitmap.width.coerceAtLeast(1),
                        maxDim.toFloat() / originalBitmap.height.coerceAtLeast(1),
                        1.0f
                    )
                    val targetW = (originalBitmap.width * ratio).toInt().coerceAtLeast(1)
                    val targetH = (originalBitmap.height * ratio).toInt().coerceAtLeast(1)
                    val scaled = if (ratio < 1.0f) {
                        Bitmap.createScaledBitmap(originalBitmap, targetW, targetH, true)
                    } else {
                        originalBitmap
                    }
                    FileOutputStream(destFile).use { out ->
                        scaled.compress(Bitmap.CompressFormat.JPEG, 82, out)
                    }
                } else {
                    context.contentResolver.openInputStream(uri)?.use { rawIn ->
                        FileOutputStream(destFile).use { rawOut ->
                            rawIn.copyTo(rawOut)
                        }
                    }
                }
            }
            destFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    fun saveCameraBitmapToInternalStorage(context: Context, bitmap: Bitmap): String? {
        return try {
            val dir = File(context.filesDir, SUBDIR).apply { if (!exists()) mkdirs() }
            val fileName = "AnhChup_${System.currentTimeMillis()}.jpg"
            val destFile = File(dir, fileName)
            FileOutputStream(destFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }
            destFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }
}

@Composable
fun AttachmentPickerSection(
    label: String = "Ảnh chứng từ / Ảnh trâu bò đính kèm",
    attachmentPaths: List<String>,
    onAddAttachmentPath: (String) -> Unit,
    onRemoveAttachmentPath: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val savedPath = AttachmentStorageHelper.copyAndCompressUriToInternalStorage(context, uri)
            if (savedPath != null) {
                onAddAttachmentPath(savedPath)
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val savedPath = AttachmentStorageHelper.saveCameraBitmapToInternalStorage(context, bitmap)
            if (savedPath != null) {
                onAddAttachmentPath(savedPath)
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "$label (${attachmentPaths.size} ảnh)",
            style = MaterialTheme.typography.titleMedium,
            fontSize = responsiveSp(13.5f),
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { cameraLauncher.launch(null) },
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Chụp ảnh", maxLines = 1, softWrap = false)
            }

            OutlinedButton(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Chọn từ máy", maxLines = 1, softWrap = false)
            }
        }

        if (attachmentPaths.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                attachmentPaths.forEachIndexed { index, path ->
                    val shortName = File(path).name.ifEmpty { "Ảnh #${index + 1}" }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 10.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Ảnh #${index + 1} ($shortName)",
                                style = MaterialTheme.typography.labelMedium
                            )
                            IconButton(
                                onClick = { onRemoveAttachmentPath(path) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Xóa ảnh",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VietnameseDatePickerField(
    label: String,
    selectedDateMillis: Long,
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val todayMillis = DateFormatter.todayNoonMillis()
    val yesterdayMillis = DateFormatter.addDays(todayMillis, -1)
    val twoDaysAgoMillis = DateFormatter.addDays(todayMillis, -2)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Hàng 1: Thanh chọn ngày full-width 1 dòng (Nút lùi 1 ngày | Ngày hiện tại | Nút tiến 1 ngày)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceElevatedDark)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { onDateSelected(DateFormatter.addDays(selectedDateMillis, -1)) },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Lùi 1 ngày",
                    tint = BluePrimary
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showDatePicker = true }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = BluePrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = DateFormatter.formatFriendlyDateHeader(selectedDateMillis),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    softWrap = false
                )
            }

            IconButton(
                onClick = { onDateSelected(DateFormatter.addDays(selectedDateMillis, 1)) },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Tiến 1 ngày",
                    tint = BluePrimary
                )
            }
        }

    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                        showDatePicker = false
                    }
                ) {
                    Text("Chọn ngày", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Hủy")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    Text(
                        text = "Chọn ngày giao dịch (dd/MM/yyyy)",
                        modifier = Modifier.padding(start = 24.dp, top = 16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    }
}

enum class TimeFilterMode(val label: String) {
    ALL("Tất cả"),
    BY_MONTH("Theo tháng"),
    BY_DAY("Theo ngày"),
    TODAY("Hôm nay")
}

/**
 * Thanh lọc thời gian thông minh theo Tháng / Ngày / Hôm nay (100% inline, chuyển tháng/ngày bằng 1 chạm).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VietnameseTimeFilterBar(
    mode: TimeFilterMode,
    onModeChange: (TimeFilterMode) -> Unit,
    anchorMillis: Long,
    onAnchorMillisChange: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCalendarDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        DropdownSelector(
            prefix = "Thời gian",
            options = TimeFilterMode.entries.toList(),
            selected = mode,
            optionLabel = { it.label },
            onSelected = { itemMode ->
                if (itemMode == TimeFilterMode.TODAY) {
                    onAnchorMillisChange(DateFormatter.todayNoonMillis())
                }
                onModeChange(itemMode)
            }
        )

        if (mode == TimeFilterMode.BY_MONTH || mode == TimeFilterMode.BY_DAY) {
            Spacer(modifier = Modifier.height(6.dp))
            VietnameseTimePeriodStepper(
                mode = mode,
                anchorMillis = anchorMillis,
                onAnchorMillisChange = onAnchorMillisChange
            )
        }
    }
}

/**
 * Thanh chuyển nhanh Tháng / Ngày (`◀ Tháng 11/2026 ▶`) dùng độc lập hoặc bên dưới bộ lọc thời gian.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VietnameseTimePeriodStepper(
    mode: TimeFilterMode,
    anchorMillis: Long,
    onAnchorMillisChange: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (mode != TimeFilterMode.BY_MONTH && mode != TimeFilterMode.BY_DAY) return
    var showCalendarDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BlueContainer)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    val next = if (mode == TimeFilterMode.BY_MONTH) {
                        DateFormatter.addMonths(anchorMillis, -1)
                    } else {
                        DateFormatter.addDays(anchorMillis, -1)
                    }
                    onAnchorMillisChange(next)
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Trước",
                    tint = BluePrimary
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (mode == TimeFilterMode.BY_DAY) {
                            showCalendarDialog = true
                        }
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = BluePrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (mode == TimeFilterMode.BY_MONTH) {
                        DateFormatter.formatMonthYear(anchorMillis)
                    } else {
                        DateFormatter.formatFriendlyDateHeader(anchorMillis)
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary,
                    maxLines = 1,
                    softWrap = false
                )
            }

            IconButton(
                onClick = {
                    val next = if (mode == TimeFilterMode.BY_MONTH) {
                        DateFormatter.addMonths(anchorMillis, 1)
                    } else {
                        DateFormatter.addDays(anchorMillis, 1)
                    }
                    onAnchorMillisChange(next)
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Sau",
                    tint = BluePrimary
                )
            }
        }
    }

    if (showCalendarDialog) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = anchorMillis)
        DatePickerDialog(
            onDismissRequest = { showCalendarDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        pickerState.selectedDateMillis?.let { onAnchorMillisChange(it) }
                        showCalendarDialog = false
                    }
                ) {
                    Text("Chọn ngày", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCalendarDialog = false }) {
                    Text("Hủy")
                }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}

/**
 * Tiêu đề nhóm theo Ngày (hiển thị inline 1 dòng giữa danh sách giao dịch / mua bán).
 */
@Composable
fun DateSectionHeader(
    dateMillis: Long,
    countLabel: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceElevatedDark)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = BluePrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = DateFormatter.formatFriendlyDateHeader(dateMillis),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                softWrap = false
            )
        }
        Text(
            text = countLabel,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            softWrap = false
        )
    }
}
