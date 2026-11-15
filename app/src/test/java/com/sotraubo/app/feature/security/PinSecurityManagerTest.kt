package com.sotraubo.app.feature.security

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class PinSecurityManagerTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    @Test
    fun `setup verify and change 6-digit PIN works accurately`() {
        val file = File(tempFolder.newFolder(), "test_pin.dat")
        val manager = PinSecurityManager(storageFile = file, useAndroidKeystore = false)

        // Ban đầu chưa có mật khẩu -> chưa thể xác thực bằng mật khẩu mặc định
        assertFalse(manager.isPinConfigured())
        assertFalse(manager.verifyPin("123456"))
        assertFalse(manager.verifyPin("000000"))

        // Không cho phép đặt mật khẩu 4 số hoặc chứa chữ cái
        assertFalse(manager.setupNewPin("1234"))
        assertFalse(manager.setupNewPin("12345a"))

        // Đặt mật khẩu 6 số hợp lệ
        assertTrue(manager.setupNewPin("123456"))
        assertTrue(manager.isPinConfigured())

        // Kiểm tra xác thực mật khẩu 6 số
        assertTrue(manager.verifyPin("123456"))
        assertFalse(manager.verifyPin("654321"))
        assertFalse(manager.verifyPin("1234"))

        // Đổi mật khẩu với mật khẩu cũ sai -> thất bại
        val badChange = manager.changePin(oldPin6Digits = "000000", newPin6Digits = "654321")
        assertTrue(badChange.isFailure)
        assertTrue(manager.verifyPin("123456"))

        // Đổi mật khẩu với mật khẩu cũ đúng -> thành công
        val goodChange = manager.changePin(oldPin6Digits = "123456", newPin6Digits = "888999")
        assertTrue(goodChange.isSuccess)
        assertFalse(manager.verifyPin("123456"))
        assertTrue(manager.verifyPin("888999"))
    }

    @Test
    fun `biometric preference defaults to true and persists toggle`() {
        val file = File(tempFolder.newFolder(), "test_pin.dat")
        val manager = PinSecurityManager(storageFile = file, useAndroidKeystore = false)

        assertTrue(manager.isBiometricEnabled())

        manager.setBiometricEnabled(false)
        assertFalse(manager.isBiometricEnabled())

        manager.setBiometricEnabled(true)
        assertTrue(manager.isBiometricEnabled())
    }
}
