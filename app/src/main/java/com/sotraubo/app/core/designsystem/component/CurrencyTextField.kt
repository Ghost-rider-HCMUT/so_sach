package com.sotraubo.app.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sotraubo.app.core.common.format.CurrencyExpressionEvaluator
import com.sotraubo.app.core.common.format.CurrencyFormatter
import com.sotraubo.app.core.designsystem.theme.BlueContainer
import com.sotraubo.app.core.designsystem.theme.BluePrimary
import com.sotraubo.app.core.designsystem.theme.DividerDark
import com.sotraubo.app.core.designsystem.theme.RedTransfer
import com.sotraubo.app.core.designsystem.theme.SurfaceDarkCard
import com.sotraubo.app.core.designsystem.theme.SurfaceElevatedDark

/**
 * Ô nhập số tiền chuyên dụng cho hộ kinh doanh:
 * - Tích hợp bàn phím máy tính tùy chỉnh (+, -, ×, ÷, 000, 00, =) để tính nhanh khi mua/bán nhiều con.
 * - Hiển thị số tiền có dấu chấm phân cách và chữ đọc tiếng Việt bên dưới.
 * - Hỗ trợ các nút chọn nhanh (Điền đủ số tiền gợi ý).
 */
@Composable
fun CurrencyTextField(
    label: String,
    amount: Long,
    onAmountChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    quickFillLabel: String? = null,
    quickFillAmount: Long? = null,
    unitSuffix: String = "đ",
    showVietnameseWords: Boolean = true
) {
    val displayValue = if (amount == 0L) "" else CurrencyFormatter.formatNumberOnly(amount)
    var showCalculator by rememberSaveable { mutableStateOf(false) }
    val currentOnAmountChange by rememberUpdatedState(onAmountChange)
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    fun openCalculator() {
        keyboardController?.hide()
        focusManager.clearFocus(force = true)
        showCalculator = true
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) {
                openCalculator()
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = displayValue,
            onValueChange = {},
            readOnly = true,
            interactionSource = interactionSource,
            label = { Text(text = label) },
            placeholder = { Text(text = "0") },
            suffix = if (unitSuffix.isNotEmpty()) {
                {
                    Text(
                        text = unitSuffix,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else null,
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (amount > 0L) {
                        IconButton(onClick = { onAmountChange(0L) }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Xóa số tiền"
                            )
                        }
                    }
                    IconButton(onClick = { openCalculator() }) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = "Mở bàn phím tính tiền",
                            tint = BluePrimary
                        )
                    }
                }
            },
            singleLine = true,
            isError = errorMessage != null,
            textStyle = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        } else if (showVietnameseWords && amount > 0L) {
            Text(
                text = "Bằng chữ: ${CurrencyFormatter.toVietnameseWords(amount)}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (quickFillLabel != null && quickFillAmount != null && quickFillAmount >= 0L) {
                AssistChip(
                    onClick = { onAmountChange(quickFillAmount) },
                    label = { Text(text = quickFillLabel, fontWeight = FontWeight.Bold) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = BlueContainer,
                        labelColor = BluePrimary
                    )
                )
            }
        }
    }

    if (showCalculator) {
        Dialog(
            onDismissRequest = { showCalculator = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { showCalculator = false }
                    )
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {}
                        ),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    border = BorderStroke(1.5.dp, BluePrimary.copy(alpha = 0.65f))
                ) {
                    CurrencyCalculatorSheet(
                        label = label,
                        initialAmount = amount,
                        unitSuffix = unitSuffix,
                        showVietnameseWords = showVietnameseWords,
                        quickFillLabel = quickFillLabel,
                        quickFillAmount = quickFillAmount,
                        onLiveChange = { evaluated ->
                            currentOnAmountChange(evaluated)
                        },
                        onApply = { evaluated ->
                            currentOnAmountChange(evaluated)
                            showCalculator = false
                        },
                        onClose = { showCalculator = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrencyCalculatorSheet(
    label: String,
    initialAmount: Long,
    unitSuffix: String,
    showVietnameseWords: Boolean,
    quickFillLabel: String?,
    quickFillAmount: Long?,
    onLiveChange: (Long) -> Unit,
    onApply: (Long) -> Unit,
    onClose: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var rawExpression by rememberSaveable {
        mutableStateOf(if (initialAmount > 0L) initialAmount.toString() else "")
    }

    val formattedExpression = remember(rawExpression) {
        CurrencyExpressionEvaluator.formatExpressionForDisplay(rawExpression)
    }
    val evaluatedValue = remember(rawExpression) {
        if (rawExpression.isEmpty()) 0L else CurrencyExpressionEvaluator.evaluate(rawExpression)
    }
    val isDivisionByZero = rawExpression.isNotEmpty() && evaluatedValue == null

    fun updateExpression(newExpression: String) {
        rawExpression = newExpression
        val evaluated = if (newExpression.isEmpty()) 0L else CurrencyExpressionEvaluator.evaluate(newExpression)
        if (evaluated != null) {
            onLiveChange(evaluated)
        }
    }

    fun appendDigits(digits: String) {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        val next = if (rawExpression == "0") {
            if (digits.all { it == '0' }) "0" else digits.trimStart('0').ifEmpty { "0" }
        } else {
            rawExpression + digits
        }
        updateExpression(next)
    }

    fun appendOperator(op: Char) {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        if (rawExpression.isEmpty()) return
        val last = rawExpression.last()
        val next = if (CurrencyExpressionEvaluator.isOperator(last)) {
            rawExpression.dropLast(1) + op
        } else {
            rawExpression + op
        }
        updateExpression(next)
    }

    fun backspace() {
        if (rawExpression.isNotEmpty()) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            updateExpression(rawExpression.dropLast(1))
        }
    }

    fun clearAll() {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        updateExpression("")
    }

    fun evaluateInPlace() {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        val result = CurrencyExpressionEvaluator.evaluate(rawExpression)
        if (result != null) {
            updateExpression(if (result == 0L) "" else result.toString())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Header tiêu đề + nút đóng
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    val latest = if (rawExpression.isEmpty()) 0L else CurrencyExpressionEvaluator.evaluate(rawExpression)
                    if (latest != null) {
                        onApply(latest)
                    } else {
                        onClose()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Đóng bàn phím"
                )
            }
        }

        // Màn hình hiển thị phép tính + kết quả tức thì
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = SurfaceDarkCard,
            border = BorderStroke(1.dp, DividerDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.End
            ) {
                val exprScroll = rememberScrollState()
                LaunchedEffect(formattedExpression) {
                    exprScroll.scrollTo(exprScroll.maxValue)
                }

                Text(
                    text = formattedExpression.ifEmpty { "0" },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(exprScroll)
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (isDivisionByZero) {
                    Text(
                        text = "Không thể chia cho 0",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = RedTransfer
                    )
                } else {
                    val resultAmount = evaluatedValue ?: 0L
                    val suffixText = if (unitSuffix.isNotEmpty()) " $unitSuffix" else ""
                    Text(
                        text = "= ${CurrencyFormatter.formatNumberOnly(resultAmount)}$suffixText",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = BluePrimary,
                        maxLines = 1
                    )
                    if (showVietnameseWords && resultAmount > 0L) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = CurrencyFormatter.toVietnameseWords(resultAmount),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        if (quickFillLabel != null && quickFillAmount != null && quickFillAmount >= 0L) {
            AssistChip(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    updateExpression(if (quickFillAmount == 0L) "" else quickFillAmount.toString())
                },
                label = { Text(text = quickFillLabel, fontWeight = FontWeight.Bold) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = BlueContainer,
                    labelColor = BluePrimary
                )
            )
        }

        // Lưới bàn phím máy tính 4 cột x 5 hàng đối xứng
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcKeyButton(
                    text = "C",
                    containerColor = RedTransfer.copy(alpha = 0.16f),
                    contentColor = RedTransfer,
                    modifier = Modifier.weight(1f),
                    onClick = ::clearAll
                )
                CalcIconKeyButton(
                    description = "Xóa lùi",
                    containerColor = SurfaceElevatedDark,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    onClick = ::backspace
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Backspace,
                        contentDescription = "Xóa lùi",
                        modifier = Modifier.size(22.dp)
                    )
                }
                CalcKeyButton(
                    text = "000",
                    containerColor = SurfaceElevatedDark,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    onClick = { appendDigits("000") }
                )
                CalcKeyButton(
                    text = "÷",
                    containerColor = BlueContainer,
                    contentColor = BluePrimary,
                    modifier = Modifier.weight(1f),
                    onClick = { appendOperator('÷') }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcKeyButton("7", SurfaceDarkCard, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f)) { appendDigits("7") }
                CalcKeyButton("8", SurfaceDarkCard, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f)) { appendDigits("8") }
                CalcKeyButton("9", SurfaceDarkCard, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f)) { appendDigits("9") }
                CalcKeyButton("×", BlueContainer, BluePrimary, Modifier.weight(1f)) { appendOperator('×') }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcKeyButton("4", SurfaceDarkCard, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f)) { appendDigits("4") }
                CalcKeyButton("5", SurfaceDarkCard, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f)) { appendDigits("5") }
                CalcKeyButton("6", SurfaceDarkCard, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f)) { appendDigits("6") }
                CalcKeyButton("-", BlueContainer, BluePrimary, Modifier.weight(1f)) { appendOperator('-') }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcKeyButton("1", SurfaceDarkCard, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f)) { appendDigits("1") }
                CalcKeyButton("2", SurfaceDarkCard, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f)) { appendDigits("2") }
                CalcKeyButton("3", SurfaceDarkCard, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f)) { appendDigits("3") }
                CalcKeyButton("+", BlueContainer, BluePrimary, Modifier.weight(1f)) { appendOperator('+') }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcKeyButton("0", SurfaceDarkCard, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f)) { appendDigits("0") }
                CalcKeyButton("00", SurfaceDarkCard, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f)) { appendDigits("00") }
                CalcKeyButton("=", SurfaceElevatedDark, BluePrimary, Modifier.weight(1f)) { evaluateInPlace() }
                CalcKeyButton(
                    text = "Xong",
                    containerColor = BluePrimary,
                    contentColor = Color.White,
                    modifier = Modifier.weight(1f),
                    enabled = !isDivisionByZero,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        val latestValue = if (rawExpression.isEmpty()) {
                            0L
                        } else {
                            CurrencyExpressionEvaluator.evaluate(rawExpression) ?: return@CalcKeyButton
                        }
                        onApply(latestValue)
                    }
                )
            }
        }
    }
}

@Composable
private fun CalcKeyButton(
    text: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (enabled) containerColor else containerColor.copy(alpha = 0.45f),
        border = BorderStroke(1.dp, DividerDark.copy(alpha = 0.65f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontSize = if (text == "Xong") 16.sp else 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (enabled) contentColor else contentColor.copy(alpha = 0.5f),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun CalcIconKeyButton(
    description: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(14.dp),
        color = containerColor,
        border = BorderStroke(1.dp, DividerDark.copy(alpha = 0.65f)),
        contentColor = contentColor
    ) {
        Box(contentAlignment = Alignment.Center) {
            icon()
        }
    }
}
