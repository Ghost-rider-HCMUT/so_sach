package com.sotraubo.app.feature.trade;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.TopAppBarDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import com.sotraubo.app.core.common.format.CurrencyFormatter;
import com.sotraubo.app.core.common.format.DateFormatter;
import com.sotraubo.app.core.common.search.VietnameseTextNormalizer;
import com.sotraubo.app.core.designsystem.component.TimeFilterMode;
import com.sotraubo.app.core.model.CashFlowType;
import com.sotraubo.app.core.model.CashTransactionWithContact;
import com.sotraubo.app.core.model.TradeOrderType;
import com.sotraubo.app.core.model.TradeOrderWithPayment;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000R\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u001aH\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\f\u0010\r\u001a\u0098\u0001\u0010\u000e\u001a\u00020\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u00102\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u00102\b\b\u0002\u0010\u0014\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u00010\u001b2\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u00010\u001b2\b\b\u0002\u0010\u001e\u001a\u00020\u001fH\u0007\u001a\u001e\u0010 \u001a\u00020\u00012\u0006\u0010!\u001a\u00020\u00132\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u001a\u001e\u0010\"\u001a\u00020\u00012\u0006\u0010#\u001a\u00020\u00112\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006$"}, d2 = {"CreateOptionRow", "", "title", "", "subtitle", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "iconColor", "Landroidx/compose/ui/graphics/Color;", "containerColor", "onClick", "Lkotlin/Function0;", "CreateOptionRow-BQnUqu0", "(Ljava/lang/String;Ljava/lang/String;Landroidx/compose/ui/graphics/vector/ImageVector;JJLkotlin/jvm/functions/Function0;)V", "MuaBanScreen", "orders", "", "Lcom/sotraubo/app/core/model/TradeOrderWithPayment;", "cashTransactions", "Lcom/sotraubo/app/core/model/CashTransactionWithContact;", "initialFilter", "Lcom/sotraubo/app/feature/trade/MuaBanFilterType;", "onAddPurchase", "onAddSale", "onAddCashIn", "onAddCashOut", "onOrderClick", "Lkotlin/Function1;", "", "onCashClick", "modifier", "Landroidx/compose/ui/Modifier;", "UnifiedCashCard", "cash", "UnifiedTradeOrderCard", "order", "app_release"})
public final class MuaBanScreenKt {
    
    /**
     * Màn hình Mua bán hợp nhất (Đơn Mua/Bán trâu bò + Phiếu Nhận/Chuyển tiền):
     * - Gom toàn bộ hoạt động mua bán trâu bò về 1 tab duy nhất.
     * - Tất cả bộ lọc dùng Dropdown gọn gàng (Loại giao dịch + Thời gian).
     * - Đủ tính năng thêm/sửa/xem chi tiết cả 4 nghiệp vụ: Mua hàng, Bán hàng, Nhận tiền, Chuyển tiền.
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void MuaBanScreen(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment> orders, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> cashTransactions, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.feature.trade.MuaBanFilterType initialFilter, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onAddPurchase, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onAddSale, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onAddCashIn, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onAddCashOut, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onOrderClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onCashClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void UnifiedTradeOrderCard(com.sotraubo.app.core.model.TradeOrderWithPayment order, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void UnifiedCashCard(com.sotraubo.app.core.model.CashTransactionWithContact cash, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}