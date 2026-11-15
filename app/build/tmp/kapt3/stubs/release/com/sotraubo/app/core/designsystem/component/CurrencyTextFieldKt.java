package com.sotraubo.app.core.designsystem.component;

import androidx.compose.foundation.interaction.PressInteraction;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.AssistChipDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.hapticfeedback.HapticFeedbackType;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import com.sotraubo.app.core.common.format.CurrencyExpressionEvaluator;
import com.sotraubo.app.core.common.format.CurrencyFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\u001aU\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0011\u0010\u000b\u001a\r\u0012\u0004\u0012\u00020\u00010\n\u00a2\u0006\u0002\b\fH\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\r\u0010\u000e\u001aL\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\u0011\u001a\u00020\u00122\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\nH\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0013\u0010\u0014\u001ac\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u00122\b\u0010\u001b\u001a\u0004\u0018\u00010\u00032\b\u0010\u001c\u001a\u0004\u0018\u00010\u00182\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u00010\u001e2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00010\nH\u0003\u00a2\u0006\u0002\u0010 \u001as\u0010!\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020\u00182\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u00010\u001e2\b\b\u0002\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010$\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u00182\b\b\u0002\u0010\u0019\u001a\u00020\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u0012H\u0007\u00a2\u0006\u0002\u0010%\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006&"}, d2 = {"CalcIconKeyButton", "", "description", "", "containerColor", "Landroidx/compose/ui/graphics/Color;", "contentColor", "modifier", "Landroidx/compose/ui/Modifier;", "onClick", "Lkotlin/Function0;", "icon", "Landroidx/compose/runtime/Composable;", "CalcIconKeyButton-RIQooxk", "(Ljava/lang/String;JJLandroidx/compose/ui/Modifier;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;)V", "CalcKeyButton", "text", "enabled", "", "CalcKeyButton-RIQooxk", "(Ljava/lang/String;JJLandroidx/compose/ui/Modifier;ZLkotlin/jvm/functions/Function0;)V", "CurrencyCalculatorSheet", "label", "initialAmount", "", "unitSuffix", "showVietnameseWords", "quickFillLabel", "quickFillAmount", "onApply", "Lkotlin/Function1;", "onClose", "(Ljava/lang/String;JLjava/lang/String;ZLjava/lang/String;Ljava/lang/Long;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;)V", "CurrencyTextField", "amount", "onAmountChange", "errorMessage", "(Ljava/lang/String;JLkotlin/jvm/functions/Function1;Landroidx/compose/ui/Modifier;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/String;Z)V", "app_release"})
public final class CurrencyTextFieldKt {
    
    /**
     * Ô nhập số tiền chuyên dụng cho hộ kinh doanh:
     * - Tích hợp bàn phím máy tính tùy chỉnh (+, -, ×, ÷, 000, 00, =) để tính nhanh khi mua/bán nhiều con.
     * - Hiển thị số tiền có dấu chấm phân cách và chữ đọc tiếng Việt bên dưới.
     * - Hỗ trợ các nút chọn nhanh (Điền đủ số tiền gợi ý).
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void CurrencyTextField(@org.jetbrains.annotations.NotNull()
    java.lang.String label, long amount, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onAmountChange, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage, @org.jetbrains.annotations.Nullable()
    java.lang.String quickFillLabel, @org.jetbrains.annotations.Nullable()
    java.lang.Long quickFillAmount, @org.jetbrains.annotations.NotNull()
    java.lang.String unitSuffix, boolean showVietnameseWords) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CurrencyCalculatorSheet(java.lang.String label, long initialAmount, java.lang.String unitSuffix, boolean showVietnameseWords, java.lang.String quickFillLabel, java.lang.Long quickFillAmount, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onApply, kotlin.jvm.functions.Function0<kotlin.Unit> onClose) {
    }
}