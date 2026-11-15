package com.sotraubo.app.feature.debt;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.TopAppBarDefaults;
import androidx.compose.runtime.Composable;
import com.sotraubo.app.core.designsystem.component.TimeFilterMode;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.sotraubo.app.core.common.format.CurrencyFormatter;
import com.sotraubo.app.core.common.format.DateFormatter;
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity;
import com.sotraubo.app.core.model.CashTransactionWithContact;
import com.sotraubo.app.core.model.DebtSortOption;
import com.sotraubo.app.core.model.PersonBidirectionalDebtDetail;
import com.sotraubo.app.core.model.PersonDebtSummaryItem;
import com.sotraubo.app.core.model.TradeOrderWithPayment;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0080\u0001\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u001e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a\u00e4\u0001\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\b2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0006\u0010\u0015\u001a\u00020\b2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\b2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u001d2\u0006\u0010\u001e\u001a\u00020\b2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020 0\u00172\u0006\u0010!\u001a\u00020\b2\u0012\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u001dH\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b#\u0010$\u001aH\u0010%\u001a\u00020\u00012\u0006\u0010&\u001a\u00020\u00182\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\'\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010(\u001a\u00020\b2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b*\u0010+\u001a\u00cc\u0001\u0010,\u001a\u00020\u00012\b\b\u0002\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020\b2\u0012\u00100\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u001d2\u0006\u00101\u001a\u0002022\u0012\u00103\u001a\u000e\u0012\u0004\u0012\u000202\u0012\u0004\u0012\u00020\u00010\u001d2\f\u00104\u001a\b\u0012\u0004\u0012\u0002050\u00172\f\u00106\u001a\b\u0012\u0004\u0012\u0002050\u00172\u000e\b\u0002\u00107\u001a\b\u0012\u0004\u0012\u00020\u00030\u00172\u000e\b\u0002\u00108\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u00109\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u001d2\u0012\u0010:\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u001d2\u0012\u0010;\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u001d2\b\b\u0002\u0010<\u001a\u00020=H\u0007\u001a4\u0010>\u001a\u00020\u00012\u0006\u0010?\u001a\u0002052\u0006\u0010@\u001a\u00020A2\f\u0010B\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a8\u0010C\u001a\u00020\u00012\u0006\u0010D\u001a\u00020 2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010E\u001a\u00020\b2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\bF\u0010G\u001a4\u0010H\u001a\u00020\u00012\u0006\u0010I\u001a\u00020\b2\u0006\u0010J\u001a\u00020\u000e2\u0006\u0010K\u001a\u00020\n2\b\b\u0002\u0010<\u001a\u00020=H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\bL\u0010M\u001a\u009e\u0001\u0010N\u001a\u00020\u00012\u0006\u0010O\u001a\u00020P2\f\u0010Q\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052#\u0010R\u001a\u001f\u0012\u0015\u0012\u0013\u0018\u00010\u000e\u00a2\u0006\f\bS\u0012\b\bT\u0012\u0004\b\b(U\u0012\u0004\u0012\u00020\u00010\u001d2#\u0010V\u001a\u001f\u0012\u0015\u0012\u0013\u0018\u00010\u000e\u00a2\u0006\f\bS\u0012\b\bT\u0012\u0004\b\b(U\u0012\u0004\u0012\u00020\u00010\u001d2\f\u0010W\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u001d2\u0012\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u001dH\u0007\u001a\u0018\u0010X\u001a\u00020\u00012\u0006\u0010I\u001a\u00020\b2\u0006\u0010J\u001a\u00020\u000eH\u0003\u001a\u0018\u0010Y\u001a\u00020\b2\u0006\u0010Z\u001a\u00020\b2\u0006\u0010[\u001a\u00020.H\u0002\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\\"}, d2 = {"BathDebtRowCard", "", "exchange", "Lcom/sotraubo/app/core/database/entity/CurrencyExchangeEntity;", "onOpenBath", "Lkotlin/Function0;", "DebtDirectionSectionCard", "sectionTitle", "", "accentColor", "Landroidx/compose/ui/graphics/Color;", "containerColor", "initialLabel", "initialTotal", "", "paidLabel", "paidTotal", "currentDebtLabel", "currentDebt", "actionLabel", "onActionClick", "ordersHeader", "orders", "", "Lcom/sotraubo/app/core/model/TradeOrderWithPayment;", "orderTotalLabel", "orderPaidLabel", "orderDebtLabel", "onOrderClick", "Lkotlin/Function1;", "cashHeader", "cashTransactions", "Lcom/sotraubo/app/core/model/CashTransactionWithContact;", "cashAmountPrefix", "onCashTransactionClick", "DebtDirectionSectionCard-zfVFVmM", "(Ljava/lang/String;JJLjava/lang/String;JLjava/lang/String;JLjava/lang/String;JLjava/lang/String;Lkotlin/jvm/functions/Function0;Ljava/lang/String;Ljava/util/List;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Ljava/lang/String;Ljava/util/List;Ljava/lang/String;Lkotlin/jvm/functions/Function1;)V", "DebtOrderItemCard", "order", "totalLabel", "debtLabel", "onClick", "DebtOrderItemCard-3IgeMak", "(Lcom/sotraubo/app/core/model/TradeOrderWithPayment;JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function0;)V", "DebtOverviewScreen", "initialTab", "", "searchQuery", "onSearchQueryChange", "sortOption", "Lcom/sotraubo/app/core/model/DebtSortOption;", "onSortOptionChange", "receivableDebtors", "Lcom/sotraubo/app/core/model/PersonDebtSummaryItem;", "payableCreditors", "bathExchanges", "onOpenBathExchange", "onPersonClick", "onQuickCollectDebt", "onQuickPayDebt", "modifier", "Landroidx/compose/ui/Modifier;", "DebtPersonRowCard", "item", "isReceivable", "", "onCardClick", "LinkedCashItemRow", "tx", "amountPrefix", "LinkedCashItemRow-RPmYEkk", "(Lcom/sotraubo/app/core/model/CashTransactionWithContact;JLjava/lang/String;Lkotlin/jvm/functions/Function0;)V", "OrderMetricRow", "label", "amount", "valueColor", "OrderMetricRow-9LQNqLg", "(Ljava/lang/String;JJLandroidx/compose/ui/Modifier;)V", "PersonDebtDetailScreen", "detail", "Lcom/sotraubo/app/core/model/PersonBidirectionalDebtDetail;", "onBack", "onCollectReceivableClick", "Lkotlin/ParameterName;", "name", "preselectedOrderId", "onPayPayableClick", "onExportPdfClick", "SummaryRow", "formatOrderDescriptionWithQuantity", "description", "quantity", "app_release"})
public final class DebtScreensKt {
    
    /**
     * Màn hình Quản lý Công nợ chi tiết (4.1, 4.2, 4.5 Master Prompt):
     * - Tab 1: Người khác nợ mình (chỉ hiển thị người có dư nợ phải thu > 0).
     * - Tab 2: Mình nợ người khác (chỉ hiển thị người có dư nợ phải trả > 0).
     * - Ô tìm kiếm nhanh ở phía trên (theo tên có dấu/không dấu và SĐT).
     * - Sắp xếp theo Số tiền nợ từ cao xuống thấp hoặc Thời gian giao dịch gần nhất.
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void DebtOverviewScreen(int initialTab, @org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSearchQueryChange, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.DebtSortOption sortOption, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.sotraubo.app.core.model.DebtSortOption, kotlin.Unit> onSortOptionChange, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.PersonDebtSummaryItem> receivableDebtors, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.PersonDebtSummaryItem> payableCreditors, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity> bathExchanges, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenBathExchange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onPersonClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onQuickCollectDebt, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onQuickPayDebt, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void BathDebtRowCard(com.sotraubo.app.core.database.entity.CurrencyExchangeEntity exchange, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenBath) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DebtPersonRowCard(com.sotraubo.app.core.model.PersonDebtSummaryItem item, boolean isReceivable, kotlin.jvm.functions.Function0<kotlin.Unit> onCardClick, kotlin.jvm.functions.Function0<kotlin.Unit> onActionClick) {
    }
    
    /**
     * Màn hình Chi tiết Công nợ của một người (4.3 Master Prompt):
     * - Nếu một người vừa nợ mình vừa là người mình nợ, hiển thị thành HAI PHẦN RIÊNG BIỆT:
     *  1. Người đó còn nợ mình.
     *  2. Mình còn nợ người đó.
     * - Tuyệt đối không tự động bù trừ hai khoản nợ.
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void PersonDebtDetailScreen(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.PersonBidirectionalDebtDetail detail, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onCollectReceivableClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onPayPayableClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onExportPdfClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onOrderClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onCashTransactionClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SummaryRow(java.lang.String label, long amount) {
    }
    
    private static final java.lang.String formatOrderDescriptionWithQuantity(java.lang.String description, int quantity) {
        return null;
    }
}