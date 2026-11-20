package com.sotraubo.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sotraubo.app.core.designsystem.component.CurrencyTextField
import com.sotraubo.app.core.designsystem.component.ScreenHelpButton
import com.sotraubo.app.core.designsystem.component.ScreenHelpGuides
import com.sotraubo.app.core.designsystem.theme.SoTrauBoTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CurrencyTextFieldUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun calculatorSheet_appliesEnteredDigitsOnXong() {
        var latestAmount = 0L

        composeRule.setContent {
            SoTrauBoTheme {
                var amount by remember { mutableLongStateOf(0L) }
                CurrencyTextField(
                    label = "5. Tổng tiền mua (*)",
                    amount = amount,
                    onAmountChange = {
                        amount = it
                        latestAmount = it
                    }
                )
            }
        }

        composeRule.onNodeWithContentDescription("Mở bàn phím tính tiền").performClick()

        composeRule.onNodeWithText("1").performClick()
        composeRule.onNodeWithText("9").performClick()
        composeRule.onNodeWithText("3").performClick()
        composeRule.onNodeWithText("000").performClick()
        composeRule.onNodeWithText("000").performClick()

        composeRule.onNodeWithText("= 193.000.000 đ").assertIsDisplayed()
        assertEquals(193_000_000L, latestAmount)

        composeRule.onNodeWithText("Xong").performClick()
        composeRule.waitForIdle()

        assertEquals(193_000_000L, latestAmount)
        composeRule.onNodeWithText("193.000.000").assertIsDisplayed()
    }

    @Test
    fun calculatorSheet_retainsEnteredAmountWhenClosedViaCloseButton() {
        var latestAmount = 0L

        composeRule.setContent {
            SoTrauBoTheme {
                var amount by remember { mutableLongStateOf(0L) }
                CurrencyTextField(
                    label = "Số Bath",
                    amount = amount,
                    unitSuffix = "฿",
                    showVietnameseWords = false,
                    onAmountChange = {
                        amount = it
                        latestAmount = it
                    }
                )
            }
        }

        composeRule.onNodeWithContentDescription("Mở bàn phím tính tiền").performClick()

        composeRule.onNodeWithText("1").performClick()
        composeRule.onNodeWithText("000").performClick()
        composeRule.onNodeWithText("000").performClick()

        composeRule.onNodeWithText("= 1.000.000 ฿").assertIsDisplayed()
        assertEquals(1_000_000L, latestAmount)

        composeRule.onNodeWithContentDescription("Đóng bàn phím").performClick()
        composeRule.waitForIdle()

        assertEquals(1_000_000L, latestAmount)
        composeRule.onNodeWithText("1.000.000").assertIsDisplayed()
    }

    @Test
    fun calculatorSheet_evaluatesExpressionAndAppliesOnXong() {
        var latestAmount = 0L

        composeRule.setContent {
            SoTrauBoTheme {
                var amount by remember { mutableLongStateOf(0L) }
                CurrencyTextField(
                    label = "5. Tổng tiền mua (*)",
                    amount = amount,
                    onAmountChange = {
                        amount = it
                        latestAmount = it
                    }
                )
            }
        }

        composeRule.onNodeWithContentDescription("Mở bàn phím tính tiền").performClick()

        composeRule.onNodeWithText("5").performClick()
        composeRule.onNodeWithText("×").performClick()
        composeRule.onNodeWithText("2").performClick()
        composeRule.onNodeWithText("0").performClick()
        composeRule.onNodeWithText("000").performClick()
        composeRule.onNodeWithText("000").performClick()

        composeRule.onNodeWithText("= 100.000.000 đ").assertIsDisplayed()

        composeRule.onNodeWithText("Xong").performClick()
        composeRule.waitForIdle()

        assertEquals(100_000_000L, latestAmount)
        composeRule.onNodeWithText("100.000.000").assertIsDisplayed()
    }

    @Test
    fun screenHelpButton_opensAndClosesGuideDialog() {
        composeRule.setContent {
            SoTrauBoTheme {
                ScreenHelpButton(guide = ScreenHelpGuides.forLedgerForm("Đổi Bath Thái"))
            }
        }

        composeRule.onNodeWithContentDescription("Hướng dẫn màn hình").performClick()
        composeRule.onNodeWithText("HƯỚNG DẪN SỬ DỤNG").assertIsDisplayed()
        composeRule.onNodeWithText("Đổi Bath Thái").assertIsDisplayed()
        composeRule.onNodeWithText("Đã hiểu").performClick()
        composeRule.waitForIdle()
    }
}
