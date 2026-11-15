package com.sotraubo.app.core.ui.components;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.window.DialogProperties;
import com.sotraubo.app.core.common.format.CurrencyFormatter;
import com.sotraubo.app.core.common.format.DateFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000&\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u001as\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072K\u0010\b\u001aG\u0012\u0013\u0012\u00110\u0005\u00a2\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\u0005\u00a2\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\r\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u00020\u00010\tH\u0007\u00a8\u0006\u000f"}, d2 = {"InstallmentPaymentDialog", "", "title", "", "remainingAmount", "", "onDismiss", "Lkotlin/Function0;", "onConfirm", "Lkotlin/Function3;", "Lkotlin/ParameterName;", "name", "addedAmount", "dateMillis", "installmentNote", "app_debug"})
public final class InstallmentPaymentDialogKt {
    
    /**
     * Reusable dialog for recording an additional installment payment with amount, date, and note.
     */
    @androidx.compose.runtime.Composable()
    public static final void InstallmentPaymentDialog(@org.jetbrains.annotations.NotNull()
    java.lang.String title, long remainingAmount, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function3<? super java.lang.Long, ? super java.lang.Long, ? super java.lang.String, kotlin.Unit> onConfirm) {
    }
}