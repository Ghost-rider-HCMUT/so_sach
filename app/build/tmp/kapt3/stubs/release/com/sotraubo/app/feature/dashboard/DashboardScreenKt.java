package com.sotraubo.app.feature.dashboard;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.TopAppBarDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import com.sotraubo.app.core.common.format.CurrencyFormatter;
import com.sotraubo.app.core.common.format.DateFormatter;
import com.sotraubo.app.core.common.format.ReportPeriodType;
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity;
import com.sotraubo.app.core.database.entity.FamilyExpenseEntity;
import com.sotraubo.app.core.database.entity.QuotaFeeEntity;
import com.sotraubo.app.core.model.CashFlowType;
import com.sotraubo.app.core.model.CashTransactionWithContact;
import com.sotraubo.app.core.model.DashboardFinancialSummary;
import com.sotraubo.app.core.model.PeriodBusinessStats;
import com.sotraubo.app.core.model.PersonDebtSummaryItem;
import com.sotraubo.app.core.model.TradeOrderType;
import com.sotraubo.app.core.model.TradeOrderWithPayment;
import com.sotraubo.app.core.model.UnifiedActivityItem;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u009a\u0001\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u000b\u001a2\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0003\u001a^\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00010\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a\u00ea\u0002\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00192\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00192\u0006\u0010\u001c\u001a\u00020\f2\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00010\u00112\u0006\u0010\u001e\u001a\u00020\u001f2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00112\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\u000e\b\u0002\u0010&\u001a\b\u0012\u0004\u0012\u00020\'0\u00192\u000e\b\u0002\u0010(\u001a\b\u0012\u0004\u0012\u00020)0\u00192\u000e\b\u0002\u0010*\u001a\b\u0012\u0004\u0012\u00020+0\u00192\u000e\b\u0002\u0010,\u001a\b\u0012\u0004\u0012\u00020-0\u00192\u000e\b\u0002\u0010.\u001a\b\u0012\u0004\u0012\u00020/0\u00192\u000e\b\u0002\u00100\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\u0014\b\u0002\u00101\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00112\u000e\b\u0002\u00102\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\u000e\b\u0002\u00103\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u00104\u001a\u00020\u00052\u0014\b\u0002\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00112\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001a\u008a\u0001\u00106\u001a\u00020\u00012\u0006\u00107\u001a\u00020\u00032\u0006\u00108\u001a\u00020\u00032\u0006\u00109\u001a\u00020\u00052\u0006\u0010:\u001a\u00020\u00032\u0006\u0010;\u001a\u00020\u00032\u0006\u0010<\u001a\u00020\u00032\f\u0010=\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00192\u0006\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020?2\u0006\u0010A\u001a\u00020\u00032\f\u0010B\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\u0012\u0010C\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0011H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\bD\u0010E\u001af\u0010F\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010G\u001a\u00020\u00032\u0006\u0010H\u001a\u00020\u00052\u001e\u0010I\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020?0J0\u00192\n\b\u0002\u0010K\u001a\u0004\u0018\u00010\u00032\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0003\u001aT\u0010L\u001a\u00020\u00012\u0006\u0010M\u001a\u00020N2\u0006\u00104\u001a\u00020\u00052\u0006\u0010O\u001a\u00020\u000f2\u0006\u0010P\u001a\u00020\u00032\u0006\u0010Q\u001a\u00020\u00052\u0006\u0010R\u001a\u00020\u00052\f\u0010S\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001aT\u0010T\u001a\u00020\u00012\u0006\u0010U\u001a\u00020\u00032\b\b\u0002\u0010V\u001a\u00020\u00032\u0006\u0010W\u001a\u00020X2\u0006\u0010Y\u001a\u00020?2\u0006\u0010Z\u001a\u00020?2\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b[\u0010\\\u001a\u001e\u0010]\u001a\u00020\u00012\u0006\u0010^\u001a\u00020_2\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a0\u0010`\u001a\u00020\u00012\u0006\u0010U\u001a\u00020\u00032\u0006\u0010a\u001a\u00020\u000f2\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0003\u001a4\u0010b\u001a\u00020\u00012\u001e\u0010c\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020?0J0\u00192\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u0003\u001a,\u0010d\u001a\u00020\u00012\u0006\u0010U\u001a\u00020\u00032\u0006\u0010e\u001a\u00020\u00032\b\b\u0002\u0010Y\u001a\u00020?H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\bf\u0010g\u001a\u0010\u0010h\u001a\u00020\u00012\u0006\u0010i\u001a\u00020\u001fH\u0003\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006j"}, d2 = {"CapitalIndicatorCard", "", "title", "", "amount", "", "onClick", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "DashboardScopeBar", "selectedScopeType", "Lcom/sotraubo/app/core/common/format/ReportPeriodType;", "activeScopeLabel", "isCurrentPeriod", "", "onSelectScopeType", "Lkotlin/Function1;", "onPrevPeriod", "onNextPeriod", "onResetToCurrent", "DashboardScreen", "summary", "Lcom/sotraubo/app/core/model/DashboardFinancialSummary;", "receivableDebtors", "", "Lcom/sotraubo/app/core/model/PersonDebtSummaryItem;", "payableCreditors", "selectedPeriod", "onSelectPeriod", "periodStats", "Lcom/sotraubo/app/core/model/PeriodBusinessStats;", "onQuickBuyCattle", "onOpenReceivableTab", "onOpenPayableTab", "onOpenPersonDebtDetail", "onOpenAllActivities", "onOpenBackupAndReport", "bathExchanges", "Lcom/sotraubo/app/core/database/entity/CurrencyExchangeEntity;", "quotaFees", "Lcom/sotraubo/app/core/database/entity/QuotaFeeEntity;", "familyExpenses", "Lcom/sotraubo/app/core/database/entity/FamilyExpenseEntity;", "tradeOrders", "Lcom/sotraubo/app/core/model/TradeOrderWithPayment;", "cashTransactions", "Lcom/sotraubo/app/core/model/CashTransactionWithContact;", "onOpenBathExchange", "onQuickBathExchange", "onOpenQuotaFees", "onOpenFamilyExpenses", "initialCapital", "onUpdateInitialCapital", "HighlightedDebtTableCard", "headerTitle", "totalDebtLabel", "totalDebtAmount", "personCountLabel", "columnPersonHeader", "columnAmountHeader", "items", "accentColor", "Landroidx/compose/ui/graphics/Color;", "headerBackgroundColor", "emptyText", "onViewAllClick", "onPersonRowClick", "HighlightedDebtTableCard-0nD-MI0", "(Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;JJLjava/lang/String;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;)V", "ItemOverviewCard", "profitLabel", "profit", "lines", "Lkotlin/Triple;", "note", "PlainLanguageOverviewCard", "metrics", "Lcom/sotraubo/app/feature/dashboard/DashboardComputedMetrics;", "isMonthlyMode", "scopeLabel", "payableDebt", "receivableGoodsDebt", "onEditInitialCapital", "QuickActionTile", "label", "subLabel", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "color", "containerColor", "QuickActionTile-ZkgLGzA", "(Ljava/lang/String;Ljava/lang/String;Landroidx/compose/ui/graphics/vector/ImageVector;JJLkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;)V", "RecentActivityRow", "item", "Lcom/sotraubo/app/core/model/UnifiedActivityItem;", "ScopeSegmentButton", "selected", "SimpleBarChart", "bars", "SubInfoLine", "value", "SubInfoLine-mxwnekA", "(Ljava/lang/String;Ljava/lang/String;J)V", "VietnameseSimpleBarChart", "stats", "app_release"})
public final class DashboardScreenKt {
    
    /**
     * MÀN HÌNH TỔNG QUAN (DASHBOARD — Mục 3 Master Prompt):
     * 3.1. Khu vực Tổng quan tài chính (6 chỉ số tách biệt, nhấn để mở chi tiết).
     * 3.2. Khu vực Công nợ nổi bật (A. Người khác còn nợ mình & B. Mình còn nợ người khác).
     * 3.3. Giao dịch gần đây (Nhận tiền, Chuyển tiền, Mua hàng, Bán hàng).
     * 3.4. Thống kê kinh doanh (Hôm nay, Tuần này, Tháng này, Năm nay, Tùy chọn + Biểu đồ tiếng Việt).
     * 3.5. Thao tác nhanh trên Dashboard (Nhận tiền, Chuyển tiền, Mua hàng, Bán hàng).
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void DashboardScreen(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.DashboardFinancialSummary summary, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.PersonDebtSummaryItem> receivableDebtors, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.PersonDebtSummaryItem> payableCreditors, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.common.format.ReportPeriodType selectedPeriod, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.sotraubo.app.core.common.format.ReportPeriodType, kotlin.Unit> onSelectPeriod, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.PeriodBusinessStats periodStats, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onQuickBuyCattle, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenReceivableTab, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenPayableTab, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onOpenPersonDebtDetail, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenAllActivities, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenBackupAndReport, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity> bathExchanges, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity> quotaFees, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity> familyExpenses, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment> tradeOrders, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> cashTransactions, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenBathExchange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onQuickBathExchange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenQuotaFees, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenFamilyExpenses, long initialCapital, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onUpdateInitialCapital, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void VietnameseSimpleBarChart(com.sotraubo.app.core.model.PeriodBusinessStats stats) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SimpleBarChart(java.util.List<kotlin.Triple<java.lang.String, java.lang.Long, androidx.compose.ui.graphics.Color>> bars, java.lang.String title) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void RecentActivityRow(com.sotraubo.app.core.model.UnifiedActivityItem item, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ItemOverviewCard(java.lang.String title, java.lang.String profitLabel, long profit, java.util.List<kotlin.Triple<java.lang.String, java.lang.String, androidx.compose.ui.graphics.Color>> lines, java.lang.String note, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CapitalIndicatorCard(java.lang.String title, long amount, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DashboardScopeBar(com.sotraubo.app.core.common.format.ReportPeriodType selectedScopeType, java.lang.String activeScopeLabel, boolean isCurrentPeriod, kotlin.jvm.functions.Function1<? super com.sotraubo.app.core.common.format.ReportPeriodType, kotlin.Unit> onSelectScopeType, kotlin.jvm.functions.Function0<kotlin.Unit> onPrevPeriod, kotlin.jvm.functions.Function0<kotlin.Unit> onNextPeriod, kotlin.jvm.functions.Function0<kotlin.Unit> onResetToCurrent) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ScopeSegmentButton(java.lang.String label, boolean selected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PlainLanguageOverviewCard(com.sotraubo.app.feature.dashboard.DashboardComputedMetrics metrics, long initialCapital, boolean isMonthlyMode, java.lang.String scopeLabel, long payableDebt, long receivableGoodsDebt, kotlin.jvm.functions.Function0<kotlin.Unit> onEditInitialCapital, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}