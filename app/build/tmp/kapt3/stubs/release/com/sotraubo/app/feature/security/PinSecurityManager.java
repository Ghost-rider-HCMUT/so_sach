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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u0000 %2\u00020\u0001:\u0001%B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u0002J+\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0016\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000e\u0010\u000fJ\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0002J\u0010\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0011H\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\u0010\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\fH\u0002J\b\u0010\u0019\u001a\u00020\u0005H\u0016J\b\u0010\u001a\u001a\u00020\u0005H\u0016J\u0016\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\f\u0018\u00010\u001cH\u0002J\u0010\u0010\u001d\u001a\u00020\n2\u0006\u0010\u001e\u001a\u00020\u0005H\u0016J\u0010\u0010\u001f\u001a\u00020\u00052\u0006\u0010 \u001a\u00020\fH\u0016J\u0010\u0010!\u001a\u00020\u00052\u0006\u0010 \u001a\u00020\fH\u0016J\u0018\u0010\"\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\f2\u0006\u0010$\u001a\u00020\fH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006&"}, d2 = {"Lcom/sotraubo/app/feature/security/PinSecurityManager;", "Lcom/sotraubo/app/feature/security/PinSecurityStorage;", "storageFile", "Ljava/io/File;", "useAndroidKeystore", "", "(Ljava/io/File;Z)V", "biometricFlagFile", "changePin", "Lkotlin/Result;", "", "oldPin6Digits", "", "newPin6Digits", "changePin-gIAlu-s", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;", "decryptWithKeystore", "", "wrappedBytes", "encryptWithKeystore", "plainBytes", "getOrCreateSecretKey", "Ljavax/crypto/SecretKey;", "hexToBytes", "hex", "isBiometricEnabled", "isPinConfigured", "readRecord", "Lkotlin/Pair;", "setBiometricEnabled", "enabled", "setupNewPin", "pin6Digits", "verifyPin", "writeRecord", "saltHex", "hashHex", "Companion", "app_release"})
public final class PinSecurityManager implements com.sotraubo.app.feature.security.PinSecurityStorage {
    @org.jetbrains.annotations.NotNull()
    private final java.io.File storageFile = null;
    private final boolean useAndroidKeystore = false;
    public static final int PIN_LENGTH = 6;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DEFAULT_PIN = "123456";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String ANDROID_KEYSTORE = "AndroidKeyStore";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_ALIAS = "so_trau_bo_pin_aes_key";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_BITS = 128;
    @org.jetbrains.annotations.NotNull()
    public static final com.sotraubo.app.feature.security.PinSecurityManager.Companion Companion = null;
    
    public PinSecurityManager(@org.jetbrains.annotations.NotNull()
    java.io.File storageFile, boolean useAndroidKeystore) {
        super();
    }
    
    @java.lang.Override()
    public boolean isPinConfigured() {
        return false;
    }
    
    @java.lang.Override()
    public boolean setupNewPin(@org.jetbrains.annotations.NotNull()
    java.lang.String pin6Digits) {
        return false;
    }
    
    @java.lang.Override()
    public boolean verifyPin(@org.jetbrains.annotations.NotNull()
    java.lang.String pin6Digits) {
        return false;
    }
    
    @java.lang.Override()
    public boolean isBiometricEnabled() {
        return false;
    }
    
    @java.lang.Override()
    public void setBiometricEnabled(boolean enabled) {
    }
    
    private final java.io.File biometricFlagFile() {
        return null;
    }
    
    private final boolean writeRecord(java.lang.String saltHex, java.lang.String hashHex) {
        return false;
    }
    
    private final kotlin.Pair<java.lang.String, java.lang.String> readRecord() {
        return null;
    }
    
    private final javax.crypto.SecretKey getOrCreateSecretKey() {
        return null;
    }
    
    private final byte[] encryptWithKeystore(byte[] plainBytes) {
        return null;
    }
    
    private final byte[] decryptWithKeystore(byte[] wrappedBytes) {
        return null;
    }
    
    private final byte[] hexToBytes(java.lang.String hex) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u001d\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u0012H\u0000\u00a2\u0006\u0002\b\u0013J\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0010\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/sotraubo/app/feature/security/PinSecurityManager$Companion;", "", "()V", "ANDROID_KEYSTORE", "", "DEFAULT_PIN", "GCM_TAG_BITS", "", "KEY_ALIAS", "PIN_LENGTH", "TRANSFORMATION", "create", "Lcom/sotraubo/app/feature/security/PinSecurityManager;", "context", "Landroid/content/Context;", "hashPinWithSalt", "pin", "salt", "", "hashPinWithSalt$app_release", "isValidSixDigitPin", "", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.feature.security.PinSecurityManager create(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        public final boolean isValidSixDigitPin(@org.jetbrains.annotations.NotNull()
        java.lang.String pin) {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String hashPinWithSalt$app_release(@org.jetbrains.annotations.NotNull()
        java.lang.String pin, @org.jetbrains.annotations.NotNull()
        byte[] salt) {
            return null;
        }
    }
}