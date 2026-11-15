package com.sotraubo.app

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.Density
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sotraubo.app.core.common.format.ReportPeriodType
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity
import com.sotraubo.app.core.database.entity.FamilyExpenseEntity
import com.sotraubo.app.core.designsystem.theme.SoTrauBoTheme
import com.sotraubo.app.core.model.Contact
import com.sotraubo.app.core.model.ContactWithSummary
import com.sotraubo.app.core.model.DashboardFinancialSummary
import com.sotraubo.app.core.model.DebtSortOption
import com.sotraubo.app.core.model.PeriodBusinessStats
import com.sotraubo.app.core.model.PersonDebtSummaryItem
import com.sotraubo.app.core.model.TradeOrderType
import com.sotraubo.app.core.model.TradeOrderWithPayment
import com.sotraubo.app.core.ui.components.InstallmentPaymentDialog
import com.sotraubo.app.feature.contact.ContactListScreen
import com.sotraubo.app.feature.dashboard.DashboardScreen
import com.sotraubo.app.feature.debt.DebtOverviewScreen
import com.sotraubo.app.feature.ledger.CurrencyExchangeScreen
import com.sotraubo.app.feature.ledger.FamilyExpenseScreen
import com.sotraubo.app.feature.security.PinLockScreen
import com.sotraubo.app.feature.trade.TradeOrderFormScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Full-app UI/UX QC test suite covering PIN Lock, Dashboard, Bath Exchange,
 * Trade Order Forms, Debt Overview, Family Expenses, Contacts, Installment Dialogs,
 * and Accessibility Font Scaling (70 test cases).
 */
@RunWith(AndroidJUnit4::class)
class FullAppUiUxQcTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun sampleSummary() = DashboardFinancialSummary(
        totalPurchaseAmount = 724_000_000L,
        totalSaleAmount = 862_500_000L,
        totalCashIn = 643_000_000L,
        totalCashOut = 522_000_000L,
        totalReceivableDebt = 243_500_000L,
        totalPayableDebt = 247_000_000L,
        receivableDebtorCount = 6,
        payableCreditorCount = 4
    )

    private fun samplePeriodStats() = PeriodBusinessStats(
        periodLabel = "Tháng 11/2026",
        totalPurchaseAmount = 724_000_000L,
        totalSaleAmount = 862_500_000L,
        totalCashIn = 643_000_000L,
        totalCashOut = 522_000_000L,
        purchaseCattleCount = 12,
        saleCattleCount = 12
    )

    private fun sampleContact(id: Long = 1L, name: String = "Chị Ngọc", phone: String = "0901234567") =
        ContactWithSummary(
            contact = Contact(
                id = id,
                fullName = name,
                nameNormalized = name.lowercase(),
                phone = phone,
                phoneNormalized = phone,
                address = "Tây Ninh",
                note = ""
            ),
            receivableDebt = 89_000_000L,
            payableDebt = 0L,
            saleOrderCount = 2,
            lastTransactionDate = System.currentTimeMillis()
        )

    private fun sampleBathExchanges(): List<CurrencyExchangeEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            CurrencyExchangeEntity(
                id = 1L,
                exchangeDate = now,
                direction = "VND_TO_THB",
                bahtAmount = 355_000L,
                rate = 0L,
                vndAmount = 256_900_000L,
                theoreticalVndAmount = 0L,
                note = "Chuyển Bath đợt 1",
                createdAt = now
            ),
            CurrencyExchangeEntity(
                id = 2L,
                exchangeDate = now,
                direction = "THB_TO_VND",
                bahtAmount = 85_000L,
                rate = 0L,
                vndAmount = 40_000_000L,
                theoreticalVndAmount = 65_000_000L,
                note = "Chia nhiều đợt còn nợ 25tr",
                createdAt = now
            )
        )
    }

    // =========================================================================
    // 1. PIN LOCK & SETUP (8 cases)
    // =========================================================================

    @Test
    fun qc01_pinSetup_displaysCreateTitle() {
        composeRule.setContent {
            SoTrauBoTheme {
                PinLockScreen(isCustomPinConfigured = false, onVerifyPin = { false }, onSetupPin = { true }, onUnlocked = {})
            }
        }
        composeRule.onNodeWithText("Tạo mật khẩu 6 số").assertIsDisplayed()
        composeRule.onNodeWithText("Thiết lập mật khẩu").assertIsDisplayed()
    }

    @Test
    fun qc02_pinSetup_showsAllTenDigits() {
        composeRule.setContent {
            SoTrauBoTheme {
                PinLockScreen(isCustomPinConfigured = false, onVerifyPin = { false }, onSetupPin = { true }, onUnlocked = {})
            }
        }
        for (d in 0..9) {
            composeRule.onNodeWithText(d.toString()).assertIsDisplayed()
        }
    }

    @Test
    fun qc03_pinSetup_transitionsToConfirmStepAfterSixDigits() {
        composeRule.setContent {
            SoTrauBoTheme {
                PinLockScreen(isCustomPinConfigured = false, onVerifyPin = { false }, onSetupPin = { true }, onUnlocked = {})
            }
        }
        "123456".forEach { composeRule.onNodeWithText(it.toString()).performClick() }
        composeRule.onNodeWithText("Nhập lại mật khẩu").assertIsDisplayed()
        composeRule.onNodeWithText("Xác nhận lại 6 chữ số").assertIsDisplayed()
    }

    @Test
    fun qc04_pinSetup_mismatchedConfirmShowsError() {
        composeRule.setContent {
            SoTrauBoTheme {
                PinLockScreen(isCustomPinConfigured = false, onVerifyPin = { false }, onSetupPin = { true }, onUnlocked = {})
            }
        }
        "123456".forEach { composeRule.onNodeWithText(it.toString()).performClick() }
        "654321".forEach { composeRule.onNodeWithText(it.toString()).performClick() }
        composeRule.onNodeWithText("Tạo mật khẩu 6 số").assertIsDisplayed()
    }

    @Test
    fun qc05_pinSetup_matchingConfirmCallsOnSetupAndUnlocks() {
        var setupPin = ""
        var unlocked = false
        composeRule.setContent {
            SoTrauBoTheme {
                PinLockScreen(
                    isCustomPinConfigured = false,
                    onVerifyPin = { false },
                    onSetupPin = { setupPin = it; true },
                    onUnlocked = { unlocked = true }
                )
            }
        }
        "123456".forEach { composeRule.onNodeWithText(it.toString()).performClick() }
        "123456".forEach { composeRule.onNodeWithText(it.toString()).performClick() }
        assertEquals("123456", setupPin)
        assertTrue(unlocked)
    }

    @Test
    fun qc06_pinVerify_displaysUnlockTitleWhenConfigured() {
        composeRule.setContent {
            SoTrauBoTheme {
                PinLockScreen(isCustomPinConfigured = true, onVerifyPin = { true }, onSetupPin = { true }, onUnlocked = {})
            }
        }
        composeRule.onNodeWithText("QUẢN LÝ").assertIsDisplayed()
        composeRule.onNodeWithText("DEL").assertIsDisplayed()
    }

    @Test
    fun qc07_pinVerify_wrongPinDoesNotUnlock() {
        var unlocked = false
        composeRule.setContent {
            SoTrauBoTheme {
                PinLockScreen(
                    isCustomPinConfigured = true,
                    onVerifyPin = { it == "123456" },
                    onSetupPin = { true },
                    onUnlocked = { unlocked = true }
                )
            }
        }
        "111111".forEach { composeRule.onNodeWithText(it.toString()).performClick() }
        assertTrue(!unlocked)
    }

    @Test
    fun qc08_pinVerify_correctPinUnlocks() {
        var unlocked = false
        composeRule.setContent {
            SoTrauBoTheme {
                PinLockScreen(
                    isCustomPinConfigured = true,
                    onVerifyPin = { it == "123456" },
                    onSetupPin = { true },
                    onUnlocked = { unlocked = true }
                )
            }
        }
        "123456".forEach { composeRule.onNodeWithText(it.toString()).performClick() }
        assertTrue(unlocked)
    }

    // =========================================================================
    // 2. DASHBOARD SCREEN (18 cases)
    // =========================================================================

    private fun setEmptyDashboard() {
        composeRule.setContent {
            SoTrauBoTheme {
                DashboardScreen(
                    summary = DashboardFinancialSummary(),
                    receivableDebtors = emptyList(),
                    payableCreditors = emptyList(),
                    selectedPeriod = ReportPeriodType.THIS_MONTH,
                    onSelectPeriod = {},
                    periodStats = PeriodBusinessStats(),
                    onQuickBuyCattle = {},
                    onOpenReceivableTab = {},
                    onOpenPayableTab = {},
                    onOpenPersonDebtDetail = {},
                    onOpenAllActivities = {},
                    onOpenBackupAndReport = {}
                )
            }
        }
    }

    private fun setPopulatedDashboard(
        initialCapital: Long = 0L,
        onQuickBuy: () -> Unit = {},
        onQuickBath: (String) -> Unit = {},
        onOpenFamily: () -> Unit = {},
        onOpenBackup: () -> Unit = {},
        onUpdateCapital: (Long) -> Unit = {}
    ) {
        composeRule.setContent {
            SoTrauBoTheme {
                DashboardScreen(
                    summary = sampleSummary(),
                    receivableDebtors = listOf(
                        PersonDebtSummaryItem(1L, "Chị Ngọc", "0901234567", 89_000_000L, 2, System.currentTimeMillis())
                    ),
                    payableCreditors = listOf(
                        PersonDebtSummaryItem(2L, "Chị Mai", "0909998888", 50_000_000L, 1, System.currentTimeMillis())
                    ),
                    selectedPeriod = ReportPeriodType.THIS_MONTH,
                    onSelectPeriod = {},
                    periodStats = samplePeriodStats(),
                    onQuickBuyCattle = onQuickBuy,
                    onOpenReceivableTab = {},
                    onOpenPayableTab = {},
                    onOpenPersonDebtDetail = {},
                    onOpenAllActivities = {},
                    onOpenBackupAndReport = onOpenBackup,
                    bathExchanges = sampleBathExchanges(),
                    onQuickBathExchange = onQuickBath,
                    onOpenFamilyExpenses = onOpenFamily,
                    initialCapital = initialCapital,
                    onUpdateInitialCapital = onUpdateCapital
                )
            }
        }
    }

    @Test
    fun qc09_dashboardEmpty_showsHeaderAndSingleBackupPdfButton() {
        setEmptyDashboard()
        composeRule.onNodeWithText("Quản Lý").assertIsDisplayed()
        composeRule.onNodeWithText("Sao lưu / PDF").assertIsDisplayed()
    }

    @Test
    fun qc10_dashboardEmpty_showsQuickActionTilesWithSubLabels() {
        setEmptyDashboard()
        composeRule.onAllNodesWithText("Hàng hóa").onFirst().assertIsDisplayed()
        composeRule.onNodeWithText("Mua / Bán").assertIsDisplayed()
        composeRule.onAllNodesWithText("Đổi Bath").onFirst().assertIsDisplayed()
        composeRule.onNodeWithText("Ghi đổi tiền").assertIsDisplayed()
        composeRule.onNodeWithText("Sinh hoạt").assertIsDisplayed()
        composeRule.onNodeWithText("Chi gia đình").assertIsDisplayed()
    }

    @Test
    fun qc11_dashboardEmpty_showsPlainLanguageOverviewSections() {
        setEmptyDashboard()
        composeRule.onNodeWithText("TIỀN ĐANG CẦM TRONG TAY").assertIsDisplayed()
        composeRule.onNodeWithText("1. TIỀN ĐÃ QUA TAY (THỰC TẾ)").assertIsDisplayed()
        composeRule.onNodeWithText("2. NỢ CHƯA THU / CHƯA TRẢ").assertIsDisplayed()
        composeRule.onNodeWithText("3. NẾU THU & TRẢ HẾT NỢ (DỰ KIẾN)").assertIsDisplayed()
    }

    @Test
    fun qc12_dashboardEmpty_showsZeroValuesCleanly() {
        setEmptyDashboard()
        composeRule.onAllNodesWithText("0 đ").onFirst().assertIsDisplayed()
        composeRule.onNode(hasScrollToIndexAction()).performScrollToIndex(1)
        composeRule.onNodeWithText("Tổng thu (đã thu + nợ):").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Tổng chi (đã chi + nợ):").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun qc13_dashboardPopulated_showsCombinedCashInAndCashOut() {
        setPopulatedDashboard()
        // 643M + 40M = 683.000.000 đ; 522M + 256.9M = 778.900.000 đ
        composeRule.onNodeWithText("683.000.000 đ").assertIsDisplayed()
        composeRule.onNodeWithText("778.900.000 đ").assertIsDisplayed()
    }

    @Test
    fun qc14_dashboardPopulated_showsBathDebtBreakdown() {
        setPopulatedDashboard()
        composeRule.onNode(hasScrollToIndexAction()).performScrollToIndex(1)
        composeRule.onNodeWithText("Người ta nợ mình (Hàng + Bath):").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("268.500.000 đ").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun qc15_dashboardPopulated_initialCapitalUpdatesCashInHand() {
        setPopulatedDashboard(initialCapital = 200_000_000L)
        composeRule.onNodeWithText("200.000.000 đ").assertIsDisplayed()
    }

    @Test
    fun qc16_dashboard_quickActionBuyTriggersCallback() {
        var clicked = false
        setPopulatedDashboard(onQuickBuy = { clicked = true })
        composeRule.onAllNodesWithText("Hàng hóa").onFirst().performClick()
        assertTrue(clicked)
    }

    @Test
    fun qc17_dashboard_quickActionBathTriggersCallback() {
        var dir = ""
        setPopulatedDashboard(onQuickBath = { dir = it })
        composeRule.onAllNodesWithText("Đổi Bath").onFirst().performClick()
        assertEquals("VND_TO_THB", dir)
    }

    @Test
    fun qc18_dashboard_quickActionFamilyTriggersCallback() {
        var clicked = false
        setPopulatedDashboard(onOpenFamily = { clicked = true })
        composeRule.onNodeWithText("Sinh hoạt").performClick()
        assertTrue(clicked)
    }

    @Test
    fun qc19_dashboard_backupPdfButtonTriggersCallback() {
        var clicked = false
        setPopulatedDashboard(onOpenBackup = { clicked = true })
        composeRule.onNodeWithText("Sao lưu / PDF").performClick()
        assertTrue(clicked)
    }

    @Test
    fun qc20_dashboard_initialCapitalClickOpensDialog() {
        setPopulatedDashboard()
        composeRule.onNodeWithText("Vốn ban đầu (bấm để sửa)").performClick()
        composeRule.onNodeWithText("Lưu vốn").assertIsDisplayed()
    }

    @Test
    fun qc21_dashboard_scrollShowsBathChartSection() {
        setPopulatedDashboard()
        composeRule.onNode(hasScrollToIndexAction()).performScrollToIndex(3)
        composeRule.onNodeWithText("BIỂU ĐỒ ĐỔI BATH THÁI").assertIsDisplayed()
    }

    @Test
    fun qc22_dashboard_scrollShowsDebtSectionAndDebtor() {
        setPopulatedDashboard()
        composeRule.onNode(hasScrollToIndexAction()).performScrollToIndex(3)
        composeRule.onNodeWithText("CÔNG NỢ NỔI BẬT").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Chị Ngọc", substring = true).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun qc23_dashboard_fontScale130_rendersTopBarWithoutCrash() {
        composeRule.setContent {
            CompositionLocalProvider(LocalDensity provides Density(density = 2.75f, fontScale = 1.3f)) {
                SoTrauBoTheme {
                    DashboardScreen(
                        summary = sampleSummary(),
                        receivableDebtors = emptyList(),
                        payableCreditors = emptyList(),
                        selectedPeriod = ReportPeriodType.THIS_MONTH,
                        onSelectPeriod = {},
                        periodStats = samplePeriodStats(),
                        onQuickBuyCattle = {},
                        onOpenReceivableTab = {},
                        onOpenPayableTab = {},
                        onOpenPersonDebtDetail = {},
                        onOpenAllActivities = {},
                        onOpenBackupAndReport = {}
                    )
                }
            }
        }
        composeRule.onNodeWithText("Quản Lý").assertIsDisplayed()
        composeRule.onNodeWithText("Sao lưu / PDF").assertIsDisplayed()
        composeRule.onNodeWithText("TIỀN ĐANG CẦM TRONG TAY").assertIsDisplayed()
    }

    @Test
    fun qc24_dashboard_fontScale150_rendersOverviewValues() {
        composeRule.setContent {
            CompositionLocalProvider(LocalDensity provides Density(density = 2.75f, fontScale = 1.5f)) {
                SoTrauBoTheme {
                    DashboardScreen(
                        summary = sampleSummary(),
                        receivableDebtors = emptyList(),
                        payableCreditors = emptyList(),
                        selectedPeriod = ReportPeriodType.THIS_MONTH,
                        onSelectPeriod = {},
                        periodStats = samplePeriodStats(),
                        onQuickBuyCattle = {},
                        onOpenReceivableTab = {},
                        onOpenPayableTab = {},
                        onOpenPersonDebtDetail = {},
                        onOpenAllActivities = {},
                        onOpenBackupAndReport = {}
                    )
                }
            }
        }
        composeRule.onNodeWithText("Quản Lý").assertIsDisplayed()
        composeRule.onNodeWithText("1. TIỀN ĐÃ QUA TAY (THỰC TẾ)").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun qc25_dashboard_negativeProfitDisplaysRedWithoutPlus() {
        composeRule.setContent {
            SoTrauBoTheme {
                DashboardScreen(
                    summary = DashboardFinancialSummary(totalCashIn = 10_000_000L, totalCashOut = 25_000_000L),
                    receivableDebtors = emptyList(),
                    payableCreditors = emptyList(),
                    selectedPeriod = ReportPeriodType.THIS_MONTH,
                    onSelectPeriod = {},
                    periodStats = PeriodBusinessStats(),
                    onQuickBuyCattle = {},
                    onOpenReceivableTab = {},
                    onOpenPayableTab = {},
                    onOpenPersonDebtDetail = {},
                    onOpenAllActivities = {},
                    onOpenBackupAndReport = {}
                )
            }
        }
        composeRule.onAllNodesWithText("-15.000.000 đ").onFirst().assertIsDisplayed()
    }

    @Test
    fun qc26_dashboard_periodStatsCardShowsCattleCounts() {
        setPopulatedDashboard()
        composeRule.onNode(hasScrollToIndexAction()).performScrollToNode(hasText("THỐNG KÊ KINH DOANH"))
        composeRule.onNodeWithText("THỐNG KÊ KINH DOANH").assertIsDisplayed()
    }

    // =========================================================================
    // 3. SỔ BATH THÁI & TRẢ NHIỀU ĐỢT (16 cases)
    // =========================================================================

    @Test
    fun qc27_bathList_emptyStateShowsGuidance() {
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(exchanges = emptyList(), onBack = {}, onSave = {}, onDelete = {})
            }
        }
        composeRule.onNodeWithText("Sổ Bath Thái").assertIsDisplayed()
        composeRule.onNodeWithText("Thêm Bath", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun qc28_bathList_summaryShowsRemainingDebtLine() {
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(exchanges = sampleBathExchanges(), onBack = {}, onSave = {}, onDelete = {})
            }
        }
        composeRule.onNode(hasScrollToIndexAction()).performTouchInput { swipeUp() }
        composeRule.onAllNodesWithText("25.000.000 đ").onFirst().assertIsDisplayed()
    }

    @Test
    fun qc29_bathList_cardDisplaysTheoreticalActualAndDebt() {
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(exchanges = sampleBathExchanges(), onBack = {}, onSave = {}, onDelete = {})
            }
        }
        composeRule.onNode(hasScrollToIndexAction()).performTouchInput { swipeUp() }
        composeRule.onAllNodesWithText("Cần thu (Lý thuyết)", substring = true).onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithText("Đã thu (Thực tế)", substring = true).onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithText("Còn nợ", substring = true).onFirst().assertIsDisplayed()
    }

    @Test
    fun qc30_bathList_addButtonOpensCreateForm() {
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(exchanges = emptyList(), onBack = {}, onSave = {}, onDelete = {})
            }
        }
        composeRule.onNodeWithText("Thêm Bath", useUnmergedTree = true).performClick()
        composeRule.onNodeWithText("Đổi Bath Thái").assertIsDisplayed()
        composeRule.onNodeWithText("Lưu thông tin").assertIsDisplayed()
    }

    @Test
    fun qc31_bathForm_emptySubmitShowsValidationError() {
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(
                    exchanges = emptyList(),
                    onBack = {},
                    onSave = {},
                    onDelete = {},
                    initialDirectionCode = "VND_TO_THB"
                )
            }
        }
        composeRule.onNodeWithText("Lưu thông tin").performScrollTo().performClick()
        composeRule.onNodeWithText("Vui lòng nhập Số tiền Việt, hoặc nhập đủ Số Bath và Tỉ giá.").assertIsDisplayed()
    }

    @Test
    fun qc32_bathForm_switchToReceiveShowsTwoVndFields() {
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(
                    exchanges = emptyList(),
                    onBack = {},
                    onSave = {},
                    onDelete = {},
                    initialDirectionCode = "VND_TO_THB"
                )
            }
        }
        composeRule.onNodeWithText("Khách chuyển Việt").performClick()
        composeRule.onNodeWithText("Tiền Việt cần thu - Lý thuyết (đ)").assertIsDisplayed()
        composeRule.onNodeWithText("Tiền Việt đã thu - Thực tế (đ)").assertIsDisplayed()
    }

    @Test
    fun qc33_bathForm_receiveEmptySubmitShowsValidationError() {
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(
                    exchanges = emptyList(),
                    onBack = {},
                    onSave = {},
                    onDelete = {},
                    initialDirectionCode = "THB_TO_VND"
                )
            }
        }
        composeRule.onNodeWithText("Lưu thông tin").performScrollTo().performClick()
        composeRule.onNodeWithText("Vui lòng nhập Số tiền Việt (đã thu hoặc cần thu), hoặc nhập đủ Số Bath và Tỉ giá.").assertIsDisplayed()
    }

    @Test
    fun qc34_bathForm_shortPlaceholdersAreVisible() {
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(
                    exchanges = emptyList(),
                    onBack = {},
                    onSave = {},
                    onDelete = {},
                    initialDirectionCode = "VND_TO_THB"
                )
            }
        }
        composeRule.onNodeWithText("Số Bath (฿ - tùy chọn)").assertIsDisplayed()
        composeRule.onNodeWithText("Tỉ giá (đ / 1 Bath - tùy chọn)").assertIsDisplayed()
        composeRule.onNodeWithText("Số tiền Việt (đ)").assertIsDisplayed()
    }

    @Test
    fun qc35_bathEdit_clickCardOpensEditFormWithInstallmentButton() {
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(exchanges = sampleBathExchanges(), onBack = {}, onSave = {}, onDelete = {})
            }
        }
        composeRule.onNode(hasScrollToIndexAction()).performScrollToNode(hasText("Khách chuyển Việt"))
        composeRule.onNodeWithText("Khách chuyển Việt").performClick()
        composeRule.onNodeWithText("Sửa giao dịch Bath").assertIsDisplayed()
        composeRule.onNodeWithText("Ghi thêm đợt khách chuyển tiền").assertIsDisplayed()
    }

    @Test
    fun qc36_bathEdit_showsRemainingDebtBanner() {
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(exchanges = sampleBathExchanges(), onBack = {}, onSave = {}, onDelete = {})
            }
        }
        composeRule.onNode(hasScrollToIndexAction()).performScrollToNode(hasText("Khách chuyển Việt"))
        composeRule.onNodeWithText("Khách chuyển Việt").performClick()
        composeRule.onNodeWithText("Còn thiếu: 25.000.000 đ (Đã thu 40.000.000 đ / 65.000.000 đ)").assertIsDisplayed()
    }

    @Test
    fun qc37_bathEdit_clickInstallmentOpensDialogWithQuickFill() {
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(exchanges = sampleBathExchanges(), onBack = {}, onSave = {}, onDelete = {})
            }
        }
        composeRule.onNode(hasScrollToIndexAction()).performScrollToNode(hasText("Khách chuyển Việt"))
        composeRule.onNodeWithText("Khách chuyển Việt").performClick()
        composeRule.onNodeWithText("Ghi thêm đợt khách chuyển tiền").performClick()
        composeRule.onNodeWithText("Ghi thêm đợt khách chuyển").assertIsDisplayed()
        composeRule.onNodeWithText("Điền đủ 25.000.000 đ").assertIsDisplayed()
        composeRule.onNodeWithText("Lưu đợt này").assertIsNotEnabled()
    }

    @Test
    fun qc38_bathEdit_quickFillEnablesSaveAndAppendsNote() {
        var saved: CurrencyExchangeEntity? = null
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(
                    exchanges = sampleBathExchanges(),
                    onBack = {},
                    onSave = { saved = it },
                    onDelete = {}
                )
            }
        }
        composeRule.onNode(hasScrollToIndexAction()).performScrollToNode(hasText("Khách chuyển Việt"))
        composeRule.onNodeWithText("Khách chuyển Việt").performClick()
        composeRule.onNodeWithText("Ghi thêm đợt khách chuyển tiền").performClick()
        composeRule.onNodeWithText("Điền đủ 25.000.000 đ").performClick()
        composeRule.onNodeWithText("Lưu đợt này").assertIsEnabled().performClick()

        assertEquals(65_000_000L, saved?.vndAmount)
        assertTrue(saved?.note?.contains("Khách chuyển thêm 25.000.000 đ") == true)
        assertTrue(saved?.note?.contains("Đã đủ") == true)
    }

    @Test
    fun qc39_bathEdit_installmentDialogNoteIncludedInSavedNote() {
        var saved: CurrencyExchangeEntity? = null
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(
                    exchanges = sampleBathExchanges(),
                    onBack = {},
                    onSave = { saved = it },
                    onDelete = {}
                )
            }
        }
        composeRule.onNode(hasScrollToIndexAction()).performScrollToNode(hasText("Khách chuyển Việt"))
        composeRule.onNodeWithText("Khách chuyển Việt").performClick()
        composeRule.onNodeWithText("Ghi thêm đợt khách chuyển tiền").performClick()
        composeRule.onNodeWithText("Điền đủ 25.000.000 đ").performClick()
        composeRule.onNodeWithText("Ghi chú đợt này (tùy chọn)").performTextInput("CK VCB")
        composeRule.onNodeWithText("Lưu đợt này").performClick()

        assertTrue(saved?.note?.contains("CK VCB") == true)
    }

    @Test
    fun qc40_bathEdit_cancelInstallmentDialogKeepsFormOpen() {
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(exchanges = sampleBathExchanges(), onBack = {}, onSave = {}, onDelete = {})
            }
        }
        composeRule.onNode(hasScrollToIndexAction()).performScrollToNode(hasText("Khách chuyển Việt"))
        composeRule.onNodeWithText("Khách chuyển Việt").performClick()
        composeRule.onNodeWithText("Ghi thêm đợt khách chuyển tiền").performClick()
        composeRule.onNodeWithText("Hủy").performClick()
        composeRule.onNodeWithText("Sửa giao dịch Bath").assertIsDisplayed()
    }

    @Test
    fun qc41_bathEdit_deleteButtonShowsConfirmationDialog() {
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(exchanges = sampleBathExchanges(), onBack = {}, onSave = {}, onDelete = {})
            }
        }
        composeRule.onNode(hasScrollToIndexAction()).performScrollToNode(hasText("Khách chuyển Việt"))
        composeRule.onNodeWithText("Khách chuyển Việt").performClick()
        composeRule.onNodeWithContentDescription("Xóa").performClick()
        composeRule.onNodeWithText("Bạn có chắc chắn muốn xóa mục này không?").assertIsDisplayed()
    }

    @Test
    fun qc42_bathList_directionFilterWorks() {
        composeRule.setContent {
            SoTrauBoTheme {
                CurrencyExchangeScreen(exchanges = sampleBathExchanges(), onBack = {}, onSave = {}, onDelete = {})
            }
        }
        composeRule.onNodeWithText("Chiều", substring = true).performClick()
        composeRule.onNodeWithText("Mình chuyển (Bath)").performClick()
        composeRule.onNode(hasScrollToIndexAction()).performTouchInput { swipeUp() }
        composeRule.onAllNodesWithText("355.000 ฿").onFirst().assertIsDisplayed()
    }

    // =========================================================================
    // 4. ĐƠN MUA / BÁN & TRẢ NHIỀU ĐỢT (14 cases)
    // =========================================================================

    private fun sampleTradeOrder(type: TradeOrderType = TradeOrderType.SALE, paid: Long = 30_000_000L): TradeOrderWithPayment {
        val now = System.currentTimeMillis()
        return TradeOrderWithPayment(
            id = 10L,
            orderType = type,
            contactId = 1L,
            contactName = "Chị Ngọc",
            contactPhone = "0901234567",
            transactionDate = now,
            cattleQuantity = 2,
            description = "2 con bò cái",
            totalAmount = 80_000_000L,
            paidAmount = paid,
            remainingDebt = (80_000_000L - paid).coerceAtLeast(0L),
            note = "Hẹn tuần sau",
            attachmentPaths = emptyList(),
            createdAt = now
        )
    }

    @Test
    fun qc43_tradeCreate_purchaseTitleAndLabels() {
        composeRule.setContent {
            SoTrauBoTheme {
                TradeOrderFormScreen(
                    orderType = TradeOrderType.PURCHASE,
                    contacts = listOf(sampleContact()),
                    onBack = {},
                    onCreateNewContactInline = { _, _, _, _, _ -> },
                    onSaveOrder = { _, _, _, _, _, _, _, _, _, _ -> }
                )
            }
        }
        composeRule.onNodeWithText("Tạo đơn mua hàng (Mua trâu bò)").assertIsDisplayed()
        composeRule.onNodeWithText("Chọn người bán hoặc thêm người mới").assertIsDisplayed()
    }

    @Test
    fun qc44_tradeCreate_saleTitleAndLabels() {
        composeRule.setContent {
            SoTrauBoTheme {
                TradeOrderFormScreen(
                    orderType = TradeOrderType.SALE,
                    contacts = listOf(sampleContact()),
                    onBack = {},
                    onCreateNewContactInline = { _, _, _, _, _ -> },
                    onSaveOrder = { _, _, _, _, _, _, _, _, _, _ -> }
                )
            }
        }
        composeRule.onNodeWithText("Tạo đơn bán hàng (Bán trâu bò)").assertIsDisplayed()
        composeRule.onNodeWithText("Chọn người mua hoặc thêm người mới").assertIsDisplayed()
    }

    @Test
    fun qc45_tradeCreate_quantityIncrementAndFloorAtOne() {
        composeRule.setContent {
            SoTrauBoTheme {
                TradeOrderFormScreen(
                    orderType = TradeOrderType.SALE,
                    contacts = listOf(sampleContact()),
                    onBack = {},
                    onCreateNewContactInline = { _, _, _, _, _ -> },
                    onSaveOrder = { _, _, _, _, _, _, _, _, _, _ -> }
                )
            }
        }
        composeRule.onNodeWithContentDescription("Giảm số lượng").performClick()
        composeRule.onNodeWithText("1 con").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Tăng số lượng").performClick()
        composeRule.onNodeWithText("2 con").assertIsDisplayed()
    }

    @Test
    fun qc46_tradeCreate_installmentButtonHiddenWhenCreating() {
        composeRule.setContent {
            SoTrauBoTheme {
                TradeOrderFormScreen(
                    orderType = TradeOrderType.SALE,
                    contacts = listOf(sampleContact()),
                    onBack = {},
                    onCreateNewContactInline = { _, _, _, _, _ -> },
                    onSaveOrder = { _, _, _, _, _, _, _, _, _, _ -> },
                    onRecordInstallment = { _, _, _, _ -> }
                )
            }
        }
        composeRule.onAllNodesWithText("Ghi thêm đợt khách trả tiền").fetchSemanticsNodes().let {
            assertEquals(0, it.size)
        }
    }

    @Test
    fun qc47_tradeEditSale_showsRemainingDebtAndInstallmentButton() {
        composeRule.setContent {
            SoTrauBoTheme {
                TradeOrderFormScreen(
                    orderType = TradeOrderType.SALE,
                    contacts = listOf(sampleContact()),
                    existingOrder = sampleTradeOrder(TradeOrderType.SALE, paid = 30_000_000L),
                    existingInitialPaidAmount = 30_000_000L,
                    onBack = {},
                    onCreateNewContactInline = { _, _, _, _, _ -> },
                    onSaveOrder = { _, _, _, _, _, _, _, _, _, _ -> },
                    onRecordInstallment = { _, _, _, _ -> }
                )
            }
        }
        composeRule.onNodeWithText("Ghi thêm đợt khách trả tiền").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun qc48_tradeEditPurchase_showsPayInstallmentButton() {
        composeRule.setContent {
            SoTrauBoTheme {
                TradeOrderFormScreen(
                    orderType = TradeOrderType.PURCHASE,
                    contacts = listOf(sampleContact()),
                    existingOrder = sampleTradeOrder(TradeOrderType.PURCHASE, paid = 30_000_000L),
                    existingInitialPaidAmount = 30_000_000L,
                    onBack = {},
                    onCreateNewContactInline = { _, _, _, _, _ -> },
                    onSaveOrder = { _, _, _, _, _, _, _, _, _, _ -> },
                    onRecordInstallment = { _, _, _, _ -> }
                )
            }
        }
        composeRule.onNodeWithText("Ghi thêm đợt trả tiền").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun qc49_tradeEditFullyPaid_hidesInstallmentButton() {
        composeRule.setContent {
            SoTrauBoTheme {
                TradeOrderFormScreen(
                    orderType = TradeOrderType.SALE,
                    contacts = listOf(sampleContact()),
                    existingOrder = sampleTradeOrder(TradeOrderType.SALE, paid = 80_000_000L),
                    existingInitialPaidAmount = 80_000_000L,
                    onBack = {},
                    onCreateNewContactInline = { _, _, _, _, _ -> },
                    onSaveOrder = { _, _, _, _, _, _, _, _, _, _ -> },
                    onRecordInstallment = { _, _, _, _ -> }
                )
            }
        }
        composeRule.onAllNodesWithText("Ghi thêm đợt khách trả tiền").fetchSemanticsNodes().let {
            assertEquals(0, it.size)
        }
    }

    @Test
    fun qc50_tradeEdit_installmentDialogQuickFillSavesAndFormatsOrderNote() {
        var capturedAdded = 0L
        var capturedOrderNote = ""
        composeRule.setContent {
            SoTrauBoTheme {
                TradeOrderFormScreen(
                    orderType = TradeOrderType.SALE,
                    contacts = listOf(sampleContact()),
                    existingOrder = sampleTradeOrder(TradeOrderType.SALE, paid = 30_000_000L),
                    existingInitialPaidAmount = 30_000_000L,
                    onBack = {},
                    onCreateNewContactInline = { _, _, _, _, _ -> },
                    onSaveOrder = { _, _, _, _, _, _, _, _, _, _ -> },
                    onRecordInstallment = { added, _, _, orderNote ->
                        capturedAdded = added
                        capturedOrderNote = orderNote
                    }
                )
            }
        }
        composeRule.onNodeWithText("Ghi thêm đợt khách trả tiền").performScrollTo().performClick()
        composeRule.onNodeWithText("Điền đủ 50.000.000 đ").performClick()
        composeRule.onNodeWithText("Ghi chú đợt này (tùy chọn)").performTextInput("Trả đủ")
        composeRule.onNodeWithText("Lưu đợt này").performClick()

        assertEquals(50_000_000L, capturedAdded)
        assertTrue(capturedOrderNote.contains("Khách trả thêm 50.000.000 đ"))
        assertTrue(capturedOrderNote.contains("Đã đủ"))
        assertTrue(capturedOrderNote.contains("Trả đủ"))
    }

    @Test
    fun qc51_tradeEdit_deleteIconOpensConfirmDialog() {
        composeRule.setContent {
            SoTrauBoTheme {
                TradeOrderFormScreen(
                    orderType = TradeOrderType.SALE,
                    contacts = listOf(sampleContact()),
                    existingOrder = sampleTradeOrder(),
                    existingInitialPaidAmount = 30_000_000L,
                    onBack = {},
                    onCreateNewContactInline = { _, _, _, _, _ -> },
                    onSaveOrder = { _, _, _, _, _, _, _, _, _, _ -> },
                    onDeleteOrder = {}
                )
            }
        }
        composeRule.onNodeWithContentDescription("Xóa đơn").performClick()
        composeRule.onNodeWithText("Xác nhận xóa đơn hàng").assertIsDisplayed()
    }

    @Test
    fun qc52_tradeCreate_contactPickerOpensAndFilters() {
        composeRule.setContent {
            SoTrauBoTheme {
                TradeOrderFormScreen(
                    orderType = TradeOrderType.SALE,
                    contacts = listOf(sampleContact(1L, "Chị Ngọc"), sampleContact(2L, "Anh Hải")),
                    onBack = {},
                    onCreateNewContactInline = { _, _, _, _, _ -> },
                    onSaveOrder = { _, _, _, _, _, _, _, _, _, _ -> }
                )
            }
        }
        composeRule.onNodeWithText("Chọn người mua hoặc thêm người mới").performClick()
        composeRule.onNodeWithText("Chị Ngọc").assertIsDisplayed()
        composeRule.onNodeWithText("Anh Hải").assertIsDisplayed()
    }

    @Test
    fun qc53_installmentDialog_zeroAmountDisablesConfirm() {
        composeRule.setContent {
            SoTrauBoTheme {
                InstallmentPaymentDialog(
                    title = "Ghi thêm đợt",
                    remainingAmount = 20_000_000L,
                    onDismiss = {},
                    onConfirm = { _, _, _ -> }
                )
            }
        }
        composeRule.onNodeWithText("Lưu đợt này").assertIsNotEnabled()
    }

    @Test
    fun qc54_installmentDialog_noRemainingHidesQuickFill() {
        composeRule.setContent {
            SoTrauBoTheme {
                InstallmentPaymentDialog(
                    title = "Ghi thêm đợt",
                    remainingAmount = 0L,
                    onDismiss = {},
                    onConfirm = { _, _, _ -> }
                )
            }
        }
        composeRule.onAllNodesWithText("Còn thiếu hiện tại:", substring = true).fetchSemanticsNodes().let {
            assertEquals(0, it.size)
        }
    }

    @Test
    fun qc55_installmentDialog_quickFillEnablesConfirmAndPassesAmount() {
        var amt = 0L
        composeRule.setContent {
            SoTrauBoTheme {
                InstallmentPaymentDialog(
                    title = "Ghi thêm đợt",
                    remainingAmount = 12_500_000L,
                    onDismiss = {},
                    onConfirm = { a, _, _ -> amt = a }
                )
            }
        }
        composeRule.onNodeWithText("Điền đủ 12.500.000 đ").performClick()
        composeRule.onNodeWithText("Lưu đợt này").assertIsEnabled().performClick()
        assertEquals(12_500_000L, amt)
    }

    @Test
    fun qc56_tradeEdit_errorBannerDisplaysWhenErrorMessageProvided() {
        composeRule.setContent {
            SoTrauBoTheme {
                TradeOrderFormScreen(
                    orderType = TradeOrderType.SALE,
                    contacts = listOf(sampleContact()),
                    errorMessage = "Số tiền không hợp lệ",
                    onBack = {},
                    onCreateNewContactInline = { _, _, _, _, _ -> },
                    onSaveOrder = { _, _, _, _, _, _, _, _, _, _ -> }
                )
            }
        }
        composeRule.onNodeWithText("Số tiền không hợp lệ").assertIsDisplayed()
    }

    // =========================================================================
    // 5. CÔNG NỢ, CHI TIÊU GIA ĐÌNH & DANH BẠ (14 cases)
    // =========================================================================

    @Test
    fun qc57_debtOverview_emptyReceivableShowsEmptyState() {
        composeRule.setContent {
            SoTrauBoTheme {
                DebtOverviewScreen(
                    initialTab = 0,
                    searchQuery = "",
                    onSearchQueryChange = {},
                    sortOption = DebtSortOption.AMOUNT_DESC,
                    onSortOptionChange = {},
                    receivableDebtors = emptyList(),
                    payableCreditors = emptyList(),
                    onPersonClick = {},
                    onQuickCollectDebt = {},
                    onQuickPayDebt = {}
                )
            }
        }
        composeRule.onNodeWithText("Hiện không có ai đang nợ tiền bạn").assertIsDisplayed()
    }

    @Test
    fun qc58_debtOverview_populatedReceivableShowsPersonAndCollectButton() {
        composeRule.setContent {
            SoTrauBoTheme {
                DebtOverviewScreen(
                    initialTab = 0,
                    searchQuery = "",
                    onSearchQueryChange = {},
                    sortOption = DebtSortOption.AMOUNT_DESC,
                    onSortOptionChange = {},
                    receivableDebtors = listOf(
                        PersonDebtSummaryItem(1L, "Chị Ngọc", "0901234567", 89_000_000L, 2, System.currentTimeMillis())
                    ),
                    payableCreditors = emptyList(),
                    onPersonClick = {},
                    onQuickCollectDebt = {},
                    onQuickPayDebt = {}
                )
            }
        }
        composeRule.onNodeWithText("Chị Ngọc").assertIsDisplayed()
        composeRule.onNodeWithText("Thu tiền").assertIsDisplayed()
    }

    @Test
    fun qc59_debtOverview_payableTabShowsPayButton() {
        composeRule.setContent {
            SoTrauBoTheme {
                DebtOverviewScreen(
                    initialTab = 1,
                    searchQuery = "",
                    onSearchQueryChange = {},
                    sortOption = DebtSortOption.AMOUNT_DESC,
                    onSortOptionChange = {},
                    receivableDebtors = emptyList(),
                    payableCreditors = listOf(
                        PersonDebtSummaryItem(2L, "Chị Mai", "0909998888", 50_000_000L, 1, System.currentTimeMillis())
                    ),
                    onPersonClick = {},
                    onQuickCollectDebt = {},
                    onQuickPayDebt = {}
                )
            }
        }
        composeRule.onNodeWithText("Chị Mai").assertIsDisplayed()
        composeRule.onNodeWithText("Trả tiền").assertIsDisplayed()
    }

    @Test
    fun qc60_debtOverview_quickCollectTriggersCallbackWithId() {
        var collectedId = 0L
        composeRule.setContent {
            SoTrauBoTheme {
                DebtOverviewScreen(
                    initialTab = 0,
                    searchQuery = "",
                    onSearchQueryChange = {},
                    sortOption = DebtSortOption.AMOUNT_DESC,
                    onSortOptionChange = {},
                    receivableDebtors = listOf(
                        PersonDebtSummaryItem(7L, "Anh Hải", "090", 30_000_000L, 1, System.currentTimeMillis())
                    ),
                    payableCreditors = emptyList(),
                    onPersonClick = {},
                    onQuickCollectDebt = { collectedId = it },
                    onQuickPayDebt = {}
                )
            }
        }
        composeRule.onNodeWithText("Thu tiền").performClick()
        assertEquals(7L, collectedId)
    }

    @Test
    fun qc61_debtOverview_personCardClickTriggersDetailCallback() {
        var clickedId = 0L
        composeRule.setContent {
            SoTrauBoTheme {
                DebtOverviewScreen(
                    initialTab = 0,
                    searchQuery = "",
                    onSearchQueryChange = {},
                    sortOption = DebtSortOption.AMOUNT_DESC,
                    onSortOptionChange = {},
                    receivableDebtors = listOf(
                        PersonDebtSummaryItem(9L, "Anh Hùng", "090", 25_000_000L, 1, System.currentTimeMillis())
                    ),
                    payableCreditors = emptyList(),
                    onPersonClick = { clickedId = it },
                    onQuickCollectDebt = {},
                    onQuickPayDebt = {}
                )
            }
        }
        composeRule.onNodeWithText("Anh Hùng").performClick()
        assertEquals(9L, clickedId)
    }

    @Test
    fun qc62_familyExpense_emptyStateShowsGuidance() {
        composeRule.setContent {
            SoTrauBoTheme {
                FamilyExpenseScreen(expenses = emptyList(), onBack = {}, onSave = {}, onDelete = {})
            }
        }
        composeRule.onNodeWithText("Chi tiêu gia đình").assertIsDisplayed()
        composeRule.onNodeWithText("Tạo khoản chi", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun qc63_familyExpense_populatedShowsSummaryAndItem() {
        val now = System.currentTimeMillis()
        composeRule.setContent {
            SoTrauBoTheme {
                FamilyExpenseScreen(
                    expenses = listOf(FamilyExpenseEntity(1L, now, "", 1_500_000L, "Tiền điện", now)),
                    onBack = {},
                    onSave = {},
                    onDelete = {}
                )
            }
        }
        composeRule.onNodeWithText("Tiền điện").assertIsDisplayed()
        composeRule.onNodeWithText("-1.500.000 đ").assertIsDisplayed()
    }

    @Test
    fun qc64_familyExpense_createButtonOpensForm() {
        composeRule.setContent {
            SoTrauBoTheme {
                FamilyExpenseScreen(expenses = emptyList(), onBack = {}, onSave = {}, onDelete = {})
            }
        }
        composeRule.onNodeWithText("Tạo khoản chi", useUnmergedTree = true).performClick()
        composeRule.onNodeWithText("Lý do chi (*)").assertIsDisplayed()
        composeRule.onNodeWithText("Số tiền (*)").assertIsDisplayed()
    }

    @Test
    fun qc65_familyExpense_clickItemOpensEditWithDelete() {
        val now = System.currentTimeMillis()
        composeRule.setContent {
            SoTrauBoTheme {
                FamilyExpenseScreen(
                    expenses = listOf(FamilyExpenseEntity(1L, now, "", 500_000L, "Tiền nước", now)),
                    onBack = {},
                    onSave = {},
                    onDelete = {}
                )
            }
        }
        composeRule.onNodeWithText("Tiền nước").performClick()
        composeRule.onNodeWithText("Sửa khoản chi").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Xóa").assertIsDisplayed()
    }

    @Test
    fun qc66_contacts_emptyShowsGuidance() {
        composeRule.setContent {
            SoTrauBoTheme {
                ContactListScreen(contacts = emptyList(), onContactClick = {}, onCreateContact = { _, _, _, _ -> })
            }
        }
        composeRule.onNodeWithText("Người liên quan", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Chưa có người liên quan nào").assertIsDisplayed()
    }

    @Test
    fun qc67_contacts_populatedShowsContactAndPhone() {
        composeRule.setContent {
            SoTrauBoTheme {
                ContactListScreen(
                    contacts = listOf(sampleContact(1L, "Nguyễn Văn Đạt", "0912345678")),
                    
                    onContactClick = {},
                    onCreateContact = { _, _, _, _ -> }
                )
            }
        }
        composeRule.onNodeWithText("Nguyễn Văn Đạt").assertIsDisplayed()
    }

    @Test
    fun qc68_contacts_searchOutOfOrderUnaccentedMatches() {
        composeRule.setContent {
            SoTrauBoTheme {
                ContactListScreen(
                    contacts = listOf(
                        sampleContact(1L, "Nguyễn Văn Đạt", "0912345678"),
                        sampleContact(2L, "Trần Thị Mai", "0987654321")
                    ),
                    
                    onContactClick = {},
                    onCreateContact = { _, _, _, _ -> }
                )
            }
        }
        composeRule.onNodeWithText("Tìm tên hoặc số điện thoại...").performTextInput("dat nguyen")
        composeRule.onNodeWithText("Nguyễn Văn Đạt").assertIsDisplayed()
        composeRule.onAllNodesWithText("Trần Thị Mai").fetchSemanticsNodes().let {
            assertEquals(0, it.size)
        }
    }

    @Test
    fun qc69_contacts_clickContactTriggersCallback() {
        var clickedId = 0L
        composeRule.setContent {
            SoTrauBoTheme {
                ContactListScreen(
                    contacts = listOf(sampleContact(42L, "Cô Nga", "090")),
                    
                    onContactClick = { clickedId = it },
                    onCreateContact = { _, _, _, _ -> }
                )
            }
        }
        composeRule.onNodeWithText("Cô Nga").performClick()
        assertEquals(42L, clickedId)
    }

    @Test
    fun qc70_contacts_fontScale130_rendersWithoutCrash() {
        composeRule.setContent {
            CompositionLocalProvider(LocalDensity provides Density(density = 2.75f, fontScale = 1.3f)) {
                SoTrauBoTheme {
                    ContactListScreen(
                        contacts = listOf(sampleContact(1L, "Nguyễn Văn Đạt", "0912345678")),
                        
                        onContactClick = {},
                        onCreateContact = { _, _, _, _ -> }
                    )
                }
            }
        }
        composeRule.onNodeWithText("Người liên quan", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Nguyễn Văn Đạt").assertIsDisplayed()
    }

    @Test
    fun qc71_dashboard_veryLongNumber_ellipsizesCleanly() {
        composeRule.setContent {
            SoTrauBoTheme {
                DashboardScreen(
                    summary = sampleSummary().copy(
                        totalCashIn = 999_999_999_999_999L,
                        totalCashOut = 0L,
                        totalReceivableDebt = 888_888_888_888_888L
                    ),
                    receivableDebtors = emptyList(),
                    payableCreditors = emptyList(),
                    selectedPeriod = ReportPeriodType.THIS_MONTH,
                    onSelectPeriod = {},
                    periodStats = samplePeriodStats(),
                    initialCapital = 100_000_000_000_000L,
                    onUpdateInitialCapital = {},
                    onQuickBuyCattle = {},
                    onOpenReceivableTab = {},
                    onOpenPayableTab = {},
                    onOpenPersonDebtDetail = {},
                    onOpenAllActivities = {},
                    onOpenBackupAndReport = {}
                )
            }
        }
        composeRule.onNodeWithText("TIỀN ĐANG CẦM TRONG TAY").assertIsDisplayed()
        composeRule.onNodeWithText("Lời/Lỗ thực tế:").assertIsDisplayed()
        composeRule.onAllNodesWithText("999.999.999.999.999 đ").onFirst().assertIsDisplayed()
    }
}
