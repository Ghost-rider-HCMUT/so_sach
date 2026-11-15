package com.sotraubo.app.feature.security;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.hapticfeedback.HapticFeedbackType;
import androidx.compose.ui.semantics.Role;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.KeyboardType;
import androidx.compose.ui.text.input.PasswordVisualTransformation;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000L\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u001aT\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032<\u0010\u0004\u001a8\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000b0\u0005H\u0007\u001a1\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u00062\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0011\u0010\u000f\u001a\r\u0012\u0004\u0012\u00020\u00010\u0003\u00a2\u0006\u0002\b\u0010H\u0003\u001a\u001e\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u001aZ\u0010\u0014\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u00162\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00160\u00182\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00160\u00182\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\b\b\u0002\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u0016H\u0007\u00a8\u0006\u001e"}, d2 = {"ChangePinDialog", "", "onDismiss", "Lkotlin/Function0;", "onSubmitChangePin", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", "name", "oldPin", "newPin", "Lkotlin/Result;", "CircularKeypadButton", "description", "onClick", "content", "Landroidx/compose/runtime/Composable;", "CircularNumberKey", "digit", "", "PinLockScreen", "isCustomPinConfigured", "", "onVerifyPin", "Lkotlin/Function1;", "onSetupPin", "onUnlocked", "modifier", "Landroidx/compose/ui/Modifier;", "isBiometricEnabled", "app_debug"})
public final class PinLockScreenKt {
    
    /**
     * Màn hình Nhập Mật Khẩu 6 Số (Best Practices — 100% Tiếng Việt):
     * - Bỏ hoàn toàn màn hình bắt thiết lập lần đầu: Luôn hiển thị thẳng màn hình "Nhập mật khẩu mở sổ".
     * - Bàn phím số tròn chuẩn Banking/iOS/Material 3 (76dp mỗi phím, hiệu ứng gợn sóng tròn, phản hồi xúc giác Haptic).
     */
    @androidx.compose.runtime.Composable()
    public static final void PinLockScreen(boolean isCustomPinConfigured, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, java.lang.Boolean> onVerifyPin, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, java.lang.Boolean> onSetupPin, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onUnlocked, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, boolean isBiometricEnabled) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CircularKeypadButton(java.lang.String description, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CircularNumberKey(char digit, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    /**
     * Hộp thoại Đổi Mật Khẩu 6 Số (dành cho chủ hộ khi muốn thay đổi mật khẩu).
     */
    @androidx.compose.runtime.Composable()
    public static final void ChangePinDialog(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Result<kotlin.Unit>> onSubmitChangePin) {
    }
}