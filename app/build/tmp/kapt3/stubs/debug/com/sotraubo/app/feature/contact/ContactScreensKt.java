package com.sotraubo.app.feature.contact;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.TopAppBarDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.sotraubo.app.core.common.format.CurrencyFormatter;
import com.sotraubo.app.core.common.format.DateFormatter;
import com.sotraubo.app.core.common.search.VietnameseTextNormalizer;
import com.sotraubo.app.core.model.CashFlowType;
import com.sotraubo.app.core.model.CashTransactionWithContact;
import com.sotraubo.app.core.model.ContactWithSummary;
import com.sotraubo.app.core.model.TradeOrderType;
import com.sotraubo.app.core.model.TradeOrderWithPayment;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000P\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0080\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u000e2`\u0010\u0010\u001a\\\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0014\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0016\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0017\u0012\u0004\u0012\u00020\u00010\u00112\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u000e2\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u000eH\u0007\u001a\u0096\u0001\u0010\u001c\u001a\u00020\u00012\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00030\u00052\u0012\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u000e2`\u0010\u001f\u001a\\\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0014\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0016\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0017\u0012\u0004\u0012\u00020\u00010\u00112\b\b\u0002\u0010 \u001a\u00020!H\u0007\u001a\u001e\u0010\"\u001a\u00020\u00012\u0006\u0010#\u001a\u00020\u00032\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00010\fH\u0003\u001a4\u0010%\u001a\u00020\u00012\u0006\u0010&\u001a\u00020\n2\u0006\u0010\'\u001a\u00020\u000f2\u0006\u0010(\u001a\u00020)2\b\b\u0002\u0010 \u001a\u00020!H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b*\u0010+\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006,"}, d2 = {"ContactDetailScreen", "", "contactSummary", "Lcom/sotraubo/app/core/model/ContactWithSummary;", "personTradeOrders", "", "Lcom/sotraubo/app/core/model/TradeOrderWithPayment;", "personCashTransactions", "Lcom/sotraubo/app/core/model/CashTransactionWithContact;", "errorMessage", "", "onBack", "Lkotlin/Function0;", "onOpenDebtDetail", "Lkotlin/Function1;", "", "onUpdateContact", "Lkotlin/Function4;", "Lkotlin/ParameterName;", "name", "fullName", "phone", "address", "note", "onDeleteContact", "onExportPersonPdf", "onOrderClick", "onCashTransactionClick", "ContactListScreen", "contacts", "onContactClick", "onCreateContact", "modifier", "Landroidx/compose/ui/Modifier;", "ContactSummaryCard", "item", "onClick", "MiniStatBox", "label", "amount", "color", "Landroidx/compose/ui/graphics/Color;", "MiniStatBox-9LQNqLg", "(Ljava/lang/String;JJLandroidx/compose/ui/Modifier;)V", "app_debug"})
public final class ContactScreensKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ContactListScreen(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.ContactWithSummary> contacts, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onContactClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function4<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> onCreateContact, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ContactSummaryCard(com.sotraubo.app.core.model.ContactWithSummary item, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    /**
     * Màn hình Chi tiết Hồ sơ Người liên quan (2.5 Master Prompt):
     * Hiển thị đầy đủ:
     * - Họ tên, Số điện thoại, Địa chỉ, Ghi chú.
     * - 6 chỉ số: Tổng tiền mua, Tổng tiền bán, Tổng tiền đã nhận, Tổng tiền đã chuyển, Người đó còn nợ mình, Mình còn nợ người đó.
     * - Lịch sử mua/bán & Lịch sử nhận/chuyển tiền.
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ContactDetailScreen(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.ContactWithSummary contactSummary, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment> personTradeOrders, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact> personCashTransactions, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onOpenDebtDetail, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function4<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> onUpdateContact, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDeleteContact, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onExportPersonPdf, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onOrderClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onCashTransactionClick) {
    }
}