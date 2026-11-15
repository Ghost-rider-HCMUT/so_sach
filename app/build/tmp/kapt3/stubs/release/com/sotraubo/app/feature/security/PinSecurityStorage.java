package com.sotraubo.app.feature.security;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import java.io.File;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * Bộ quản lý mật khẩu 6 chữ số (PIN 6 số) bảo vệ ứng dụng khi mở máy:
 * - Bỏ hoàn toàn bước bắt thiết lập lần đầu: Nếu chưa có mật khẩu tùy chỉnh, sử dụng mật khẩu 6 số mặc định (123456).
 * - Khi người dùng đổi mật khẩu, lưu băm SHA-256 kèm Salt 16 bytes và mã hóa bằng AES-256-GCM trong Android Keystore.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J+\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H&\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\b\u0010\tJ\b\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\u000bH&J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000bH\u0016J\u0010\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0006H&J\u0010\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0006H&\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0012"}, d2 = {"Lcom/sotraubo/app/feature/security/PinSecurityStorage;", "", "changePin", "Lkotlin/Result;", "", "oldPin6Digits", "", "newPin6Digits", "changePin-gIAlu-s", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;", "isBiometricEnabled", "", "isPinConfigured", "setBiometricEnabled", "enabled", "setupNewPin", "pin6Digits", "verifyPin", "app_release"})
public abstract interface PinSecurityStorage {
    
    public abstract boolean isPinConfigured();
    
    public abstract boolean setupNewPin(@org.jetbrains.annotations.NotNull()
    java.lang.String pin6Digits);
    
    public abstract boolean verifyPin(@org.jetbrains.annotations.NotNull()
    java.lang.String pin6Digits);
    
    public abstract boolean isBiometricEnabled();
    
    public abstract void setBiometricEnabled(boolean enabled);
    
    /**
     * Bộ quản lý mật khẩu 6 chữ số (PIN 6 số) bảo vệ ứng dụng khi mở máy:
     * - Bỏ hoàn toàn bước bắt thiết lập lần đầu: Nếu chưa có mật khẩu tùy chỉnh, sử dụng mật khẩu 6 số mặc định (123456).
     * - Khi người dùng đổi mật khẩu, lưu băm SHA-256 kèm Salt 16 bytes và mã hóa bằng AES-256-GCM trong Android Keystore.
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
        
        public static boolean isBiometricEnabled(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.feature.security.PinSecurityStorage $this) {
            return false;
        }
        
        public static void setBiometricEnabled(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.feature.security.PinSecurityStorage $this, boolean enabled) {
        }
    }
}