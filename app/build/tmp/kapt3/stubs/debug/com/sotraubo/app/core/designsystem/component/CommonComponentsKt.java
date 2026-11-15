package com.sotraubo.app.core.designsystem.component;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.lazy.LazyListState;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.CardDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import com.sotraubo.app.core.common.format.CurrencyFormatter;
import com.sotraubo.app.core.model.DashboardFinancialSummary;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000J\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a<\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u0007H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000b\u0010\f\u001aJ\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00112\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00032\u0010\b\u0002\u0010\u0013\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u0007H\u0007\u001a\u001a\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u001a6\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0019\u0010\u001a\u001a\u001c\u0010\u001b\u001a\u00020\u00012\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u001a\u001a\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u001e2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u001a4\u0010\u001f\u001a\u00020\u00012\u0006\u0010 \u001a\u00020\u00032\u0006\u0010!\u001a\u00020\t2\u0006\u0010\"\u001a\u00020\t2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b#\u0010$\u001a^\u0010%\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020\t2\u0006\u0010)\u001a\u00020\t2\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00010\u00142\b\b\u0002\u0010\u0010\u001a\u00020\u00112\n\b\u0002\u0010+\u001a\u0004\u0018\u00010\u0003H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b,\u0010-\u001a\u001a\u0010.\u001a\u00020\u00012\u0006\u0010/\u001a\u0002002\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u001a(\u00101\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00010\u00142\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u00062"}, d2 = {"BridgeRow", "", "label", "", "amount", "", "showExplicitPlus", "", "valueColor", "Landroidx/compose/ui/graphics/Color;", "boldLabel", "BridgeRow-42QJj7c", "(Ljava/lang/String;JZJZ)V", "EmptyStateView", "title", "message", "modifier", "Landroidx/compose/ui/Modifier;", "actionLabel", "onActionClick", "Lkotlin/Function0;", "scrollable", "ErrorBanner", "InfoLine", "value", "InfoLine-g2O1Hgs", "(Ljava/lang/String;Ljava/lang/String;Landroidx/compose/ui/Modifier;J)V", "LoadingStateView", "ScrollToTopButton", "listState", "Landroidx/compose/foundation/lazy/LazyListState;", "SemanticBadge", "text", "textColor", "backgroundColor", "SemanticBadge-IbeAmgk", "(Ljava/lang/String;JJLandroidx/compose/ui/Modifier;)V", "SummaryMetricCard", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "accentColor", "containerColor", "onClick", "subtitle", "SummaryMetricCard-jA1GFJw", "(Ljava/lang/String;JLandroidx/compose/ui/graphics/vector/ImageVector;JJLkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;Ljava/lang/String;)V", "TheoreticalProfitBridgeCard", "summary", "Lcom/sotraubo/app/core/model/DashboardFinancialSummary;", "TopBarPrimaryAction", "app_debug"})
public final class CommonComponentsKt {
    
    /**
     * Compact primary action pill for TopAppBar actions on both small and large screens.
     */
    @androidx.compose.runtime.Composable()
    public static final void TopBarPrimaryAction(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void EmptyStateView(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.Nullable()
    java.lang.String actionLabel, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onActionClick, boolean scrollable) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void LoadingStateView(@org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ErrorBanner(@org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Thẻ giải trình dòng tiền: chỉ rõ "Lời lý thuyết (Bán − Mua)" đã đi đâu
     * để ra được "Tiền thực có (Thu − Chi)" (kẹt ở khách nợ, giữ lại nhờ nợ người bán, hay thu/chi ngoài đơn).
     */
    @androidx.compose.runtime.Composable()
    public static final void TheoreticalProfitBridgeCard(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.DashboardFinancialSummary summary, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Nút mũi tên nổi ở góc dưới bên phải giúp cuộn nhanh về đầu danh sách khi người dùng đã cuộn xuống.
     */
    @androidx.compose.runtime.Composable()
    public static final void ScrollToTopButton(@org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.lazy.LazyListState listState, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}