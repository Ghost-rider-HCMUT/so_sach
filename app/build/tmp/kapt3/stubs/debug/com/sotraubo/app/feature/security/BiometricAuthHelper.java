package com.sotraubo.app.feature.security;

import android.content.Context;
import android.content.ContextWrapper;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

/**
 * Bộ hỗ trợ mở khóa bằng Sinh trắc học (Vân tay hoặc Nhận diện khuôn mặt tùy máy):
 * - Sử dụng BIOMETRIC_STRONG | BIOMETRIC_WEAK để hỗ trợ cả cảm biến vân tay lẫn mở khóa khuôn mặt trên Android.
 * - Luôn có nút "Dùng mã PIN" làm phương án dự phòng tức thì.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ0\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\n0\u000e2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\n0\u0010J\u000f\u0010\u0012\u001a\u0004\u0018\u00010\f*\u00020\bH\u0086\u0010R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/sotraubo/app/feature/security/BiometricAuthHelper;", "", "()V", "ALLOWED_AUTHENTICATORS", "", "checkAvailability", "Lcom/sotraubo/app/feature/security/BiometricAvailability;", "context", "Landroid/content/Context;", "showPrompt", "", "activity", "Landroidx/fragment/app/FragmentActivity;", "onSuccess", "Lkotlin/Function0;", "onError", "Lkotlin/Function1;", "", "findFragmentActivity", "app_debug"})
public final class BiometricAuthHelper {
    private static final int ALLOWED_AUTHENTICATORS = 255;
    @org.jetbrains.annotations.NotNull()
    public static final com.sotraubo.app.feature.security.BiometricAuthHelper INSTANCE = null;
    
    private BiometricAuthHelper() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.feature.security.BiometricAvailability checkAvailability(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    public final void showPrompt(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.FragmentActivity activity, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onError) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final androidx.fragment.app.FragmentActivity findFragmentActivity(@org.jetbrains.annotations.NotNull()
    android.content.Context $this$findFragmentActivity) {
        return null;
    }
}