package com.sotraubo.app.feature.security;

import android.content.Context;
import android.content.ContextWrapper;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

/**
 * Trạng thái hỗ trợ sinh trắc học (Vân tay / Khuôn mặt) trên thiết bị.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/sotraubo/app/feature/security/BiometricAvailability;", "", "(Ljava/lang/String;I)V", "AVAILABLE", "NONE_ENROLLED", "UNAVAILABLE", "app_release"})
public enum BiometricAvailability {
    /*public static final*/ AVAILABLE /* = new AVAILABLE() */,
    /*public static final*/ NONE_ENROLLED /* = new NONE_ENROLLED() */,
    /*public static final*/ UNAVAILABLE /* = new UNAVAILABLE() */;
    
    BiometricAvailability() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.sotraubo.app.feature.security.BiometricAvailability> getEntries() {
        return null;
    }
}