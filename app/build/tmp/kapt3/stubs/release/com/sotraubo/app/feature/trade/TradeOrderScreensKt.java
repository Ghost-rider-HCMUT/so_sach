package com.sotraubo.app.feature.trade;

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
import com.sotraubo.app.core.common.format.InstallmentNoteFormatter;
import com.sotraubo.app.core.common.search.VietnameseTextNormalizer;
import com.sotraubo.app.core.designsystem.component.TimeFilterMode;
import com.sotraubo.app.core.model.ContactWithSummary;
import com.sotraubo.app.core.model.PaymentMethod;
import com.sotraubo.app.core.model.TradeOrderType;
import com.sotraubo.app.core.model.TradeOrderWithPayment;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000j\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\u001a\u00d3\u0004\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u00112\u0081\u0001\u0010\u0012\u001a}\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0016\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0017\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0019\u0012\u001f\u0012\u001d\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u001a\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u001b\u0012\u0004\u0012\u00020\u00010\u00132\u00e7\u0001\u0010\u001c\u001a\u00e2\u0001\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0002\u0012\u0015\u0012\u0013\u0018\u00010\b\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u001e\u0012\u0013\u0012\u00110\b\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u001f\u0012\u0013\u0012\u00110 \u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(!\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\"\u0012\u0013\u0012\u00110\b\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(#\u0012\u0013\u0012\u00110\b\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b($\u0012\u0013\u0012\u00110\r\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(%\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0019\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u00020\u000f0\u0005\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(&\u0012\u0004\u0012\u00020\u00010\u001d2\u0010\b\u0002\u0010\'\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u00112d\b\u0002\u0010(\u001a^\u0012\u0013\u0012\u00110\b\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(*\u0012\u0013\u0012\u00110\b\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(+\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(,\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(-\u0012\u0004\u0012\u00020\u0001\u0018\u00010)H\u0007\u00a2\u0006\u0002\u0010.\u001ad\u0010/\u001a\u00020\u00012\f\u00100\u001a\b\u0012\u0004\u0012\u00020\n0\u00052\f\u00101\u001a\b\u0012\u0004\u0012\u00020\u00010\u00112\f\u00102\u001a\b\u0012\u0004\u0012\u00020\u00010\u00112\u0012\u00103\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u001a2\u0012\u00104\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u001a2\b\b\u0002\u00105\u001a\u000206H\u0007\u00a8\u00067"}, d2 = {"TradeOrderFormScreen", "", "orderType", "Lcom/sotraubo/app/core/model/TradeOrderType;", "contacts", "", "Lcom/sotraubo/app/core/model/ContactWithSummary;", "preselectedContactId", "", "existingOrder", "Lcom/sotraubo/app/core/model/TradeOrderWithPayment;", "existingInitialPaidAmount", "existingInitialPaymentMethod", "Lcom/sotraubo/app/core/model/PaymentMethod;", "errorMessage", "", "onBack", "Lkotlin/Function0;", "onCreateNewContactInline", "Lkotlin/Function5;", "Lkotlin/ParameterName;", "name", "fullName", "phone", "address", "note", "Lkotlin/Function1;", "onCreated", "onSaveOrder", "Lkotlin/Function10;", "contactId", "transactionDate", "", "cattleQuantity", "description", "totalAmount", "initialPaidAmount", "initialPaymentMethod", "attachmentPaths", "onDeleteOrder", "onRecordInstallment", "Lkotlin/Function4;", "addedAmount", "dateMillis", "installmentNote", "updatedOrderNote", "(Lcom/sotraubo/app/core/model/TradeOrderType;Ljava/util/List;Ljava/lang/Long;Lcom/sotraubo/app/core/model/TradeOrderWithPayment;Ljava/lang/Long;Lcom/sotraubo/app/core/model/PaymentMethod;Ljava/lang/String;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function5;Lkotlin/jvm/functions/Function10;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function4;)V", "TradeOrderListScreen", "orders", "onCreatePurchaseClick", "onCreateSaleClick", "onOrderClick", "onSettleOrderDebtClick", "modifier", "Landroidx/compose/ui/Modifier;", "app_release"})
public final class TradeOrderScreensKt {
    
    /**
     * Biểu mẫu tạo / chỉnh sửa Đơn Mua Hàng (Mua trâu bò) hoặc Đơn Bán Hàng (Bán trâu bò).
     * - Chọn lại người đã lưu hoặc tạo người mới ngay trong luồng.
     * - Tự động tính Số tiền còn nợ = Tổng tiền - Đã trả/nhận.
     * - Chặn thanh toán vượt tổng tiền đơn hàng.
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void TradeOrderFormScreen(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.TradeOrderType orderType, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.ContactWithSummary> contacts, @org.jetbrains.annotations.Nullable()
    java.lang.Long preselectedContactId, @org.jetbrains.annotations.Nullable()
    com.sotraubo.app.core.model.TradeOrderWithPayment existingOrder, @org.jetbrains.annotations.Nullable()
    java.lang.Long existingInitialPaidAmount, @org.jetbrains.annotations.Nullable()
    com.sotraubo.app.core.model.PaymentMethod existingInitialPaymentMethod, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function5<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit>, kotlin.Unit> onCreateNewContactInline, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function10<? super com.sotraubo.app.core.model.TradeOrderType, ? super java.lang.Long, ? super java.lang.Long, ? super java.lang.Integer, ? super java.lang.String, ? super java.lang.Long, ? super java.lang.Long, ? super com.sotraubo.app.core.model.PaymentMethod, ? super java.lang.String, ? super java.util.List<java.lang.String>, kotlin.Unit> onSaveOrder, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDeleteOrder, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function4<? super java.lang.Long, ? super java.lang.Long, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> onRecordInstallment) {
    }
    
    /**
     * Màn hình Danh sách Mua bán (Tab 3 trên Thanh điều hướng dưới cùng).
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void TradeOrderListScreen(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment> orders, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCreatePurchaseClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCreateSaleClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.sotraubo.app.core.model.TradeOrderWithPayment, kotlin.Unit> onOrderClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.sotraubo.app.core.model.TradeOrderWithPayment, kotlin.Unit> onSettleOrderDebtClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}