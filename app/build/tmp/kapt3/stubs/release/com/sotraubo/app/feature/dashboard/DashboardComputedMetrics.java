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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b?\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0082\b\u0018\u00002\u00020\u0001B\u00c5\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u0012\u0006\u0010\f\u001a\u00020\u0003\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0003\u0012\u0006\u0010\u0010\u001a\u00020\u0003\u0012\u0006\u0010\u0011\u001a\u00020\u000e\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0003\u0012\u0006\u0010\u0015\u001a\u00020\u0003\u0012\u0006\u0010\u0016\u001a\u00020\u0003\u0012\u0006\u0010\u0017\u001a\u00020\u0003\u0012\u0006\u0010\u0018\u001a\u00020\u0003\u0012\u0006\u0010\u0019\u001a\u00020\u0003\u0012\u0006\u0010\u001a\u001a\u00020\u0003\u0012\u0006\u0010\u001b\u001a\u00020\u0003\u0012\u0006\u0010\u001c\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u001dJ\t\u00109\u001a\u00020\u0003H\u00c6\u0003J\t\u0010:\u001a\u00020\u0003H\u00c6\u0003J\t\u0010;\u001a\u00020\u000eH\u00c6\u0003J\t\u0010<\u001a\u00020\u0003H\u00c6\u0003J\t\u0010=\u001a\u00020\u0003H\u00c6\u0003J\t\u0010>\u001a\u00020\u000eH\u00c6\u0003J\t\u0010?\u001a\u00020\u0013H\u00c6\u0003J\t\u0010@\u001a\u00020\u0003H\u00c6\u0003J\t\u0010A\u001a\u00020\u0003H\u00c6\u0003J\t\u0010B\u001a\u00020\u0003H\u00c6\u0003J\t\u0010C\u001a\u00020\u0003H\u00c6\u0003J\t\u0010D\u001a\u00020\u0003H\u00c6\u0003J\t\u0010E\u001a\u00020\u0003H\u00c6\u0003J\t\u0010F\u001a\u00020\u0003H\u00c6\u0003J\t\u0010G\u001a\u00020\u0003H\u00c6\u0003J\t\u0010H\u001a\u00020\u0003H\u00c6\u0003J\t\u0010I\u001a\u00020\u0003H\u00c6\u0003J\t\u0010J\u001a\u00020\u0003H\u00c6\u0003J\t\u0010K\u001a\u00020\u0003H\u00c6\u0003J\t\u0010L\u001a\u00020\u0003H\u00c6\u0003J\t\u0010M\u001a\u00020\u0003H\u00c6\u0003J\t\u0010N\u001a\u00020\u0003H\u00c6\u0003J\t\u0010O\u001a\u00020\u0003H\u00c6\u0003J\t\u0010P\u001a\u00020\u0003H\u00c6\u0003J\u00f9\u0001\u0010Q\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u000e2\b\b\u0002\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00032\b\b\u0002\u0010\u0016\u001a\u00020\u00032\b\b\u0002\u0010\u0017\u001a\u00020\u00032\b\b\u0002\u0010\u0018\u001a\u00020\u00032\b\b\u0002\u0010\u0019\u001a\u00020\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u00032\b\b\u0002\u0010\u001b\u001a\u00020\u00032\b\b\u0002\u0010\u001c\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010R\u001a\u00020S2\b\u0010T\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010U\u001a\u00020\u000eH\u00d6\u0001J\t\u0010V\u001a\u00020\u0013H\u00d6\u0001R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001fR\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001fR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001fR\u0011\u0010\u001b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001fR\u0011\u0010\u001c\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001fR\u0011\u0010\u0012\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0011\u0010\u001a\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001fR\u0011\u0010\u0018\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001fR\u0011\u0010\u0019\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001fR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001fR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001fR\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\u001fR\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\u001fR\u0011\u0010\u0011\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u0011\u0010\u0010\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010\u001fR\u0011\u0010\u0016\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\u001fR\u0011\u0010\u0014\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010\u001fR\u0011\u0010\u0015\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u0010\u001fR\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010\u001fR\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u00100R\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u0010\u001fR\u0011\u0010\u0017\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010\u001f\u00a8\u0006W"}, d2 = {"Lcom/sotraubo/app/feature/dashboard/DashboardComputedMetrics;", "", "cattleActualCash", "", "cattleProfit", "myBathBaht", "myBathVnd", "theirBathActualVnd", "theirBathTheoreticalVnd", "bathActualProfit", "bathTheoreticalProfit", "bathDebt", "totalQuotaFees", "totalQuotaCattle", "", "totalFamilyExpenses", "thisMonthFamilyExpenses", "thisMonthFamilyExpenseCount", "currentMonthLabel", "", "totalCashReceivedAll", "totalCashSpentAll", "totalActualCash", "totalReceivableAll", "expectedTotalReceivedAll", "expectedTotalSpentAll", "expectedProfitAll", "currentCashActual", "currentCashTheoretical", "(JJJJJJJJJJIJJILjava/lang/String;JJJJJJJJJ)V", "getBathActualProfit", "()J", "getBathDebt", "getBathTheoreticalProfit", "getCattleActualCash", "getCattleProfit", "getCurrentCashActual", "getCurrentCashTheoretical", "getCurrentMonthLabel", "()Ljava/lang/String;", "getExpectedProfitAll", "getExpectedTotalReceivedAll", "getExpectedTotalSpentAll", "getMyBathBaht", "getMyBathVnd", "getTheirBathActualVnd", "getTheirBathTheoreticalVnd", "getThisMonthFamilyExpenseCount", "()I", "getThisMonthFamilyExpenses", "getTotalActualCash", "getTotalCashReceivedAll", "getTotalCashSpentAll", "getTotalFamilyExpenses", "getTotalQuotaCattle", "getTotalQuotaFees", "getTotalReceivableAll", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "toString", "app_release"})
final class DashboardComputedMetrics {
    private final long cattleActualCash = 0L;
    private final long cattleProfit = 0L;
    private final long myBathBaht = 0L;
    private final long myBathVnd = 0L;
    private final long theirBathActualVnd = 0L;
    private final long theirBathTheoreticalVnd = 0L;
    private final long bathActualProfit = 0L;
    private final long bathTheoreticalProfit = 0L;
    private final long bathDebt = 0L;
    private final long totalQuotaFees = 0L;
    private final int totalQuotaCattle = 0;
    private final long totalFamilyExpenses = 0L;
    private final long thisMonthFamilyExpenses = 0L;
    private final int thisMonthFamilyExpenseCount = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String currentMonthLabel = null;
    private final long totalCashReceivedAll = 0L;
    private final long totalCashSpentAll = 0L;
    private final long totalActualCash = 0L;
    private final long totalReceivableAll = 0L;
    private final long expectedTotalReceivedAll = 0L;
    private final long expectedTotalSpentAll = 0L;
    private final long expectedProfitAll = 0L;
    private final long currentCashActual = 0L;
    private final long currentCashTheoretical = 0L;
    
    public DashboardComputedMetrics(long cattleActualCash, long cattleProfit, long myBathBaht, long myBathVnd, long theirBathActualVnd, long theirBathTheoreticalVnd, long bathActualProfit, long bathTheoreticalProfit, long bathDebt, long totalQuotaFees, int totalQuotaCattle, long totalFamilyExpenses, long thisMonthFamilyExpenses, int thisMonthFamilyExpenseCount, @org.jetbrains.annotations.NotNull()
    java.lang.String currentMonthLabel, long totalCashReceivedAll, long totalCashSpentAll, long totalActualCash, long totalReceivableAll, long expectedTotalReceivedAll, long expectedTotalSpentAll, long expectedProfitAll, long currentCashActual, long currentCashTheoretical) {
        super();
    }
    
    public final long getCattleActualCash() {
        return 0L;
    }
    
    public final long getCattleProfit() {
        return 0L;
    }
    
    public final long getMyBathBaht() {
        return 0L;
    }
    
    public final long getMyBathVnd() {
        return 0L;
    }
    
    public final long getTheirBathActualVnd() {
        return 0L;
    }
    
    public final long getTheirBathTheoreticalVnd() {
        return 0L;
    }
    
    public final long getBathActualProfit() {
        return 0L;
    }
    
    public final long getBathTheoreticalProfit() {
        return 0L;
    }
    
    public final long getBathDebt() {
        return 0L;
    }
    
    public final long getTotalQuotaFees() {
        return 0L;
    }
    
    public final int getTotalQuotaCattle() {
        return 0;
    }
    
    public final long getTotalFamilyExpenses() {
        return 0L;
    }
    
    public final long getThisMonthFamilyExpenses() {
        return 0L;
    }
    
    public final int getThisMonthFamilyExpenseCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentMonthLabel() {
        return null;
    }
    
    public final long getTotalCashReceivedAll() {
        return 0L;
    }
    
    public final long getTotalCashSpentAll() {
        return 0L;
    }
    
    public final long getTotalActualCash() {
        return 0L;
    }
    
    public final long getTotalReceivableAll() {
        return 0L;
    }
    
    public final long getExpectedTotalReceivedAll() {
        return 0L;
    }
    
    public final long getExpectedTotalSpentAll() {
        return 0L;
    }
    
    public final long getExpectedProfitAll() {
        return 0L;
    }
    
    public final long getCurrentCashActual() {
        return 0L;
    }
    
    public final long getCurrentCashTheoretical() {
        return 0L;
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final long component10() {
        return 0L;
    }
    
    public final int component11() {
        return 0;
    }
    
    public final long component12() {
        return 0L;
    }
    
    public final long component13() {
        return 0L;
    }
    
    public final int component14() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component15() {
        return null;
    }
    
    public final long component16() {
        return 0L;
    }
    
    public final long component17() {
        return 0L;
    }
    
    public final long component18() {
        return 0L;
    }
    
    public final long component19() {
        return 0L;
    }
    
    public final long component2() {
        return 0L;
    }
    
    public final long component20() {
        return 0L;
    }
    
    public final long component21() {
        return 0L;
    }
    
    public final long component22() {
        return 0L;
    }
    
    public final long component23() {
        return 0L;
    }
    
    public final long component24() {
        return 0L;
    }
    
    public final long component3() {
        return 0L;
    }
    
    public final long component4() {
        return 0L;
    }
    
    public final long component5() {
        return 0L;
    }
    
    public final long component6() {
        return 0L;
    }
    
    public final long component7() {
        return 0L;
    }
    
    public final long component8() {
        return 0L;
    }
    
    public final long component9() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.feature.dashboard.DashboardComputedMetrics copy(long cattleActualCash, long cattleProfit, long myBathBaht, long myBathVnd, long theirBathActualVnd, long theirBathTheoreticalVnd, long bathActualProfit, long bathTheoreticalProfit, long bathDebt, long totalQuotaFees, int totalQuotaCattle, long totalFamilyExpenses, long thisMonthFamilyExpenses, int thisMonthFamilyExpenseCount, @org.jetbrains.annotations.NotNull()
    java.lang.String currentMonthLabel, long totalCashReceivedAll, long totalCashSpentAll, long totalActualCash, long totalReceivableAll, long expectedTotalReceivedAll, long expectedTotalSpentAll, long expectedProfitAll, long currentCashActual, long currentCashTheoretical) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}