package com.sotraubo.app.feature.security

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import com.sotraubo.app.feature.security.BiometricAuthHelper.findFragmentActivity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sotraubo.app.core.designsystem.component.ErrorBanner
import com.sotraubo.app.core.designsystem.theme.BlueContainer
import com.sotraubo.app.core.designsystem.theme.BluePrimary
import com.sotraubo.app.core.designsystem.theme.RedTransfer
import com.sotraubo.app.core.designsystem.theme.SurfaceElevatedDark

/**
 * Màn hình Nhập Mật Khẩu 6 Số (Best Practices — 100% Tiếng Việt):
 * - Bỏ hoàn toàn màn hình bắt thiết lập lần đầu: Luôn hiển thị thẳng màn hình "Nhập mật khẩu mở sổ".
 * - Bàn phím số tròn chuẩn Banking/iOS/Material 3 (76dp mỗi phím, hiệu ứng gợn sóng tròn, phản hồi xúc giác Haptic).
 */
@Composable
fun PinLockScreen(
    isCustomPinConfigured: Boolean,
    onVerifyPin: (String) -> Boolean,
    onSetupPin: (String) -> Boolean,
    onUnlocked: () -> Unit,
    modifier: Modifier = Modifier,
    isBiometricEnabled: Boolean = false
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val fragmentActivity = remember(context) { context.findFragmentActivity() }
    val biometricAvailability = remember(context) { BiometricAuthHelper.checkAvailability(context) }
    val canUseBiometric = isCustomPinConfigured &&
        isBiometricEnabled &&
        biometricAvailability == BiometricAvailability.AVAILABLE &&
        fragmentActivity != null

    var currentPinInput by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    // Lần đầu: bước 1 tạo mật khẩu, bước 2 nhập lại để xác nhận (setupFirstPin khác rỗng = đang ở bước 2)
    var setupFirstPin by rememberSaveable { mutableStateOf("") }
    val isConfirmStep = !isCustomPinConfigured && setupFirstPin.isNotEmpty()

    fun triggerBiometricPrompt() {
        val activity = fragmentActivity ?: return
        BiometricAuthHelper.showPrompt(
            activity = activity,
            onSuccess = {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onUnlocked()
            },
            onError = { message ->
                errorMessage = message
            }
        )
    }

    LaunchedEffect(canUseBiometric) {
        if (canUseBiometric) {
            triggerBiometricPrompt()
        }
    }

    fun handleDigitPress(digit: Char) {
        if (currentPinInput.length >= PinSecurityManager.PIN_LENGTH) return
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        errorMessage = null
        val updated = currentPinInput + digit
        currentPinInput = updated

        if (updated.length == PinSecurityManager.PIN_LENGTH) {
            if (!isCustomPinConfigured) {
                if (setupFirstPin.isEmpty()) {
                    setupFirstPin = updated
                    currentPinInput = ""
                } else if (updated == setupFirstPin) {
                    currentPinInput = ""
                    setupFirstPin = ""
                    if (onSetupPin(updated)) {
                        onUnlocked()
                    } else {
                        errorMessage = "Không thể lưu mật khẩu. Thử lại."
                    }
                } else {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    currentPinInput = ""
                    setupFirstPin = ""
                    errorMessage = "Mật khẩu không khớp. Tạo lại từ đầu."
                }
            } else if (onVerifyPin(updated)) {
                currentPinInput = ""
                onUnlocked()
            } else {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                currentPinInput = ""
                errorMessage = "Mật khẩu không đúng. Thử lại."
            }
        }
    }

    fun handleBackspace() {
        if (currentPinInput.isNotEmpty()) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            currentPinInput = currentPinInput.dropLast(1)
            errorMessage = null
        }
    }

    fun handleClearAll() {
        if (currentPinInput.isNotEmpty() || errorMessage != null) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            currentPinInput = ""
            errorMessage = null
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Phần đầu: Biểu tượng khóa, Tiêu đề và 6 chấm mật khẩu
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(BlueContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Khóa bảo mật",
                        tint = BluePrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "QUẢN LÝ",
                    style = MaterialTheme.typography.labelLarge,
                    color = BluePrimary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = when {
                        isCustomPinConfigured -> "Nhập mật khẩu 6 số"
                        isConfirmStep -> "Nhập lại mật khẩu"
                        else -> "Tạo mật khẩu 6 số"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = when {
                        isCustomPinConfigured -> "Nhập 6 số để mở sổ"
                        isConfirmStep -> "Xác nhận lại 6 chữ số"
                        else -> "Thiết lập mật khẩu"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(22.dp))

                // 6 chấm tròn hiển thị tiến độ nhập mật khẩu
                Row(
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(PinSecurityManager.PIN_LENGTH) { index ->
                        val isFilled = index < currentPinInput.length
                        val dotColor by animateColorAsState(
                            targetValue = if (isFilled) BluePrimary else Color.Transparent,
                            label = "pinDotColor"
                        )
                        val borderColor by animateColorAsState(
                            targetValue = if (errorMessage != null) {
                                RedTransfer
                            } else if (isFilled) {
                                BluePrimary
                            } else {
                                MaterialTheme.colorScheme.outline
                            },
                            label = "pinBorderColor"
                        )

                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(dotColor)
                                .border(
                                    width = 2.dp,
                                    color = borderColor,
                                    shape = CircleShape
                                )
                        )
                    }
                }

                // Vùng thông báo lỗi cố định chiều cao để bàn phím luôn cân đối, không bị xô lệch
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (errorMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = RedTransfer.copy(alpha = 0.14f),
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = RedTransfer.copy(alpha = 0.35f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    tint = RedTransfer,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = errorMessage.orEmpty(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = RedTransfer,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Bàn phím số tròn chuẩn Banking / Material 3 Best Practice
            Column(
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val keypadRows = listOf(
                    listOf('1', '2', '3'),
                    listOf('4', '5', '6'),
                    listOf('7', '8', '9')
                )

                keypadRows.forEach { rowDigits ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        rowDigits.forEach { digit ->
                            CircularNumberKey(
                                digit = digit,
                                onClick = { handleDigitPress(digit) }
                            )
                        }
                    }
                }

                // Hàng cuối: DEL | 0 | Xóa lùi (×)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularKeypadButton(
                        description = "Xóa hết",
                        onClick = { handleClearAll() }
                    ) {
                        Text(
                            text = "DEL",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (currentPinInput.isNotEmpty()) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }

                    CircularNumberKey(
                        digit = '0',
                        onClick = { handleDigitPress('0') }
                    )

                    CircularKeypadButton(
                        description = "Xóa lùi",
                        onClick = { handleBackspace() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Xóa lùi",
                            tint = if (currentPinInput.isNotEmpty()) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

            if (canUseBiometric) {
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedButton(
                    onClick = { triggerBiometricPrompt() },
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = null,
                        tint = BluePrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mở khóa bằng vân tay / khuôn mặt",
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun CircularKeypadButton(
    description: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(68.dp)
            .clip(CircleShape)
            .background(SurfaceElevatedDark)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f),
                shape = CircleShape
            )
            .semantics { contentDescription = description }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true, color = BluePrimary),
                role = Role.Button,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun CircularNumberKey(
    digit: Char,
    onClick: () -> Unit
) {
    CircularKeypadButton(
        description = "Phím số $digit",
        onClick = onClick
    ) {
        Text(
            text = digit.toString(),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Hộp thoại Đổi Mật Khẩu 6 Số (dành cho chủ hộ khi muốn thay đổi mật khẩu).
 */
@Composable
fun ChangePinDialog(
    onDismiss: () -> Unit,
    onSubmitChangePin: (oldPin: String, newPin: String) -> Result<Unit>
) {
    var oldPin by rememberSaveable { mutableStateOf("") }
    var newPin by rememberSaveable { mutableStateOf("") }
    var confirmNewPin by rememberSaveable { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Đổi mật khẩu 6 số",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                localError?.let { error ->
                    ErrorBanner(message = error)
                }

                OutlinedTextField(
                    value = oldPin,
                    onValueChange = { raw ->
                        oldPin = raw.filter { it.isDigit() }.take(PinSecurityManager.PIN_LENGTH)
                        localError = null
                    },
                    label = { Text("Mật khẩu 6 số hiện tại (*)") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = newPin,
                    onValueChange = { raw ->
                        newPin = raw.filter { it.isDigit() }.take(PinSecurityManager.PIN_LENGTH)
                        localError = null
                    },
                    label = { Text("Mật khẩu 6 số mới (*)") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = confirmNewPin,
                    onValueChange = { raw ->
                        confirmNewPin = raw.filter { it.isDigit() }.take(PinSecurityManager.PIN_LENGTH)
                        localError = null
                    },
                    label = { Text("Nhập lại mật khẩu 6 số mới (*)") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    when {
                        !PinSecurityManager.isValidSixDigitPin(oldPin) -> {
                            localError = "Vui lòng nhập đủ 6 chữ số của mật khẩu hiện tại."
                        }
                        !PinSecurityManager.isValidSixDigitPin(newPin) -> {
                            localError = "Mật khẩu mới phải gồm đúng 6 chữ số."
                        }
                        newPin != confirmNewPin -> {
                            localError = "Mật khẩu mới nhập lại không khớp."
                        }
                        else -> {
                            val result = onSubmitChangePin(oldPin, newPin)
                            result.fold(
                                onSuccess = { onDismiss() },
                                onFailure = { err ->
                                    localError = err.message ?: "Không thể đổi mật khẩu."
                                }
                            )
                        }
                    }
                }
            ) {
                Text("Lưu mật khẩu mới", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
