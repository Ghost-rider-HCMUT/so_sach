package com.sotraubo.app.feature.cashflow;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.TopAppBarDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.KeyboardCapitalization;
import com.sotraubo.app.core.common.format.CurrencyFormatter;
import com.sotraubo.app.core.common.format.DateFormatter;
import com.sotraubo.app.core.common.search.VietnameseTextNormalizer;
import com.sotraubo.app.core.designsystem.component.TimeFilterMode;
import com.sotraubo.app.core.model.CashFlowType;
import com.sotraubo.app.core.model.CashTransactionWithContact;
import com.sotraubo.app.core.model.ContactWithSummary;
import com.sotraubo.app.core.model.PaymentMethod;
import com.sotraubo.app.core.model.TradeOrderType;
import com.sotraubo.app.core.model.TradeOrderWithPayment;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000j\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\u001a\u00dc\u0003\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u00112\u0081\u0001\u0010\u0012\u001a}\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0016\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0017\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0019\u0012\u001f\u0012\u001d\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u001a\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u001b\u0012\u0004\u0012\u00020\u00010\u00132\u00d4\u0001\u0010\u001c\u001a\u00cf\u0001\u0012\u0015\u0012\u0013\u0018\u00010\n\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u001e\u0012\u0015\u0012\u0013\u0018\u00010\n\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u001f\u0012\u0013\u0012\u00110 \u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(!\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\"\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(#\u0012\u0013\u0012\u00110$\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(%\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(&\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0019\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u00020\u000f0\u0005\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\'\u0012\u0004\u0012\u00020\u00010\u001d2\u0010\b\u0002\u0010(\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0011H\u0007\u00a2\u0006\u0002\u0010)\u001aZ\u0010*\u001a\u00020\u00012\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\r0\u00052\b\b\u0002\u0010,\u001a\u00020\u000f2\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00010\u00112\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00010\u00112\u0012\u0010/\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u001a2\b\b\u0002\u00100\u001a\u000201H\u0007\u00a8\u00062"}, d2 = {"CashTransactionFormScreen", "", "flowType", "Lcom/sotraubo/app/core/model/CashFlowType;", "contacts", "", "Lcom/sotraubo/app/core/model/ContactWithSummary;", "allTradeOrders", "Lcom/sotraubo/app/core/model/TradeOrderWithPayment;", "preselectedContactId", "", "preselectedOrderId", "existingTransaction", "Lcom/sotraubo/app/core/model/CashTransactionWithContact;", "errorMessage", "", "onBack", "Lkotlin/Function0;", "onCreateNewContactInline", "Lkotlin/Function5;", "Lkotlin/ParameterName;", "name", "fullName", "phone", "address", "note", "Lkotlin/Function1;", "onCreated", "onSaveTransaction", "Lkotlin/Function9;", "contactId", "linkedTradeOrderId", "", "autoAllocateOldestDebt", "amount", "transactionDate", "Lcom/sotraubo/app/core/model/PaymentMethod;", "paymentMethod", "shortContent", "attachmentPaths", "onDeleteTransaction", "(Lcom/sotraubo/app/core/model/CashFlowType;Ljava/util/List;Ljava/util/List;Ljava/lang/Long;Ljava/lang/Long;Lcom/sotraubo/app/core/model/CashTransactionWithContact;Ljava/lang/String;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function5;Lkotlin/jvm/functions/Function9;Lkotlin/jvm/functions/Function0;)V", "CashTransactionListScreen", "transactions", "initialFilter", "onCreateCashInClick", "onCreateCashOutClick", "onTransactionClick", "modifier", "Landroidx/compose/ui/Modifier;", "app_debug"})
public final class CashTransactionScreensKt {
    
    /**
     * Biểu mẫu Ghi nhận Nhận Tiền (Thu tiền) hoặc Chuyển Tiền (Chi tiền).
     * - Hỗ trợ chọn liên kết với một đơn Bán/Mua đang còn nợ để trừ nợ cho đơn đó.
     * - Hỗ trợ tùy chọn tự động trừ vào các đơn nợ cũ nhất hoặc giao dịch độc lập.
     * - Chặn nhập vượt quá số tiền còn nợ của đơn liên kết.
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void CashTransactionFormScreen(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.CashFlowType flowType, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.ContactWithSummary> contacts, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment> allTradeOrders, @org.jetbrains.annotations.Nullable()
    java.lang.Long preselectedContactId, @org.jetbrains.annotations.Nullable()
    java.lang.Long preselectedOrderId, @org.jetbrains.annotations.Nullable()
    com.sotraubo.app.core.model.CashTransactionWithContact existingTransaction, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function5<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit>, kotlin.Unit> onCreateNewContactInline, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function9<? super java.lang.Long, ? super java.lang.Long, ? super java.lang.Boolean, ? super java.lang.Long, ? super java.lang.Long, ? super com.sotraubo.app.core.model.PaymentMethod, ? super java.lang.String, ? super java.lang.String, ? super java.util.List<java.lang.String>, kotlin.Unit> onSaveTransaction, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDeleteTransaction) {
    }
    
    /**
     * Màn hình Lịch sử Giao dịch Nhận tiền & Chuyển tiền (Tab 2 trên Thanh điều hướng dưới cùng).
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void CashTransactionListScreen(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> transactions, @org.jetbrains.annotations.NotNull()
    java.lang.String initialFilter, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCreateCashInClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCreateCashOutClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.sotraubo.app.core.model.CashTransactionWithContact, kotlin.Unit> onTransactionClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}