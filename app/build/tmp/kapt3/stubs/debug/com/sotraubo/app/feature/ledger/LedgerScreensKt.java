package com.sotraubo.app.feature.ledger;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.layout.IntrinsicSize;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.KeyboardType;
import androidx.compose.ui.text.style.TextOverflow;
import com.sotraubo.app.core.common.format.CurrencyFormatter;
import com.sotraubo.app.core.common.format.DateFormatter;
import com.sotraubo.app.core.common.format.InstallmentNoteFormatter;
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity;
import com.sotraubo.app.core.database.entity.FamilyExpenseEntity;
import com.sotraubo.app.core.database.entity.GoodsPurchaseEntity;
import com.sotraubo.app.core.database.entity.QuotaFeeEntity;
import com.sotraubo.app.core.model.ContactWithSummary;
import com.sotraubo.app.core.designsystem.component.TimeFilterMode;
import java.time.Instant;
import java.time.LocalDate;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u008a\u0001\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a4\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00022\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\bH\u0003\u001a\u00ac\u0001\u0010\n\u001a\u00020\u00042\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u00012\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000128\b\u0002\u0010\u000f\u001a2\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00040\u0011\u0012\u0004\u0012\u00020\u00040\u00102\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00040\u00112\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00040\u00112\b\b\u0002\u0010\u0016\u001a\u00020\u00172\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u0002H\u0007\u001a\u00a0\u0001\u0010\u0019\u001a\u00020\u00042\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u00012\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000128\b\u0002\u0010\u000f\u001a2\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00040\u0011\u0012\u0004\u0012\u00020\u00040\u00102\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u001b\u0012\u0004\u0012\u00020\u00040\u00112\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00040\u00112\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0007\u001a\u00a0\u0001\u0010\u001c\u001a\u00020\u00042\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001e0\u00012\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000128\b\u0002\u0010\u000f\u001a2\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00040\u0011\u0012\u0004\u0012\u00020\u00040\u00102\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\u00040\u00112\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00040\u00112\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0007\u001a6\u0010\u001f\u001a\u00020\u00042\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u00022\u0006\u0010#\u001a\u00020\u00022\u0006\u0010$\u001a\u00020\u00022\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00040\bH\u0003\u001aK\u0010&\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\u00022\u0006\u0010\'\u001a\u00020\u00022\u0006\u0010(\u001a\u00020)2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\u0011\u0010*\u001a\r\u0012\u0004\u0012\u00020\u00040\b\u00a2\u0006\u0002\b+H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b,\u0010-\u001aW\u0010.\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\u00022\u0006\u0010/\u001a\u00020\u00172\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\u000e\u0010\u0015\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\b2\u0011\u0010*\u001a\r\u0012\u0004\u0012\u00020\u00040\b\u00a2\u0006\u0002\b+H\u0003\u001a\u00f8\u0001\u00100\u001a\u00020\u0004\"\u0004\b\u0000\u001012\u0006\u0010\"\u001a\u00020\u00022\u0006\u00102\u001a\u00020\u00022\f\u00103\u001a\b\u0012\u0004\u0012\u0002H10\u00012\u0012\u00104\u001a\u000e\u0012\u0004\u0012\u0002H1\u0012\u0004\u0012\u00020\u00120\u00112\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u0002H1\u0012\u0004\u0012\u00020\u00120\u00112*\u00106\u001a&\u0012\n\u0012\b\u0012\u0004\u0012\u0002H10\u0001\u0012\u0016\u0012\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002070\u00010\u00112\u0018\u00108\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002H10\u0001\u0012\u0004\u0012\u00020\u00020\u00112\u0006\u00109\u001a\u00020\u00022\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\f\u0010:\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\b\b\u0002\u0010\u0016\u001a\u00020\u00172\u0015\b\u0002\u0010;\u001a\u000f\u0012\u0004\u0012\u00020\u0004\u0018\u00010\b\u00a2\u0006\u0002\b+2\u0017\u0010<\u001a\u0013\u0012\u0004\u0012\u0002H1\u0012\u0004\u0012\u00020\u00040\u0011\u00a2\u0006\u0002\b+H\u0003\u001a\u008e\u0001\u0010=\u001a\u00020\u00042\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001e0\u00012\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u00012\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u00012\f\u0010>\u001a\b\u0012\u0004\u0012\u00020?0\u00012\u0006\u0010@\u001a\u00020A2\f\u0010B\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\f\u0010C\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\f\u0010D\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\f\u0010E\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\f\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00040\bH\u0007\u001a\u00a0\u0001\u0010G\u001a\u00020\u00042\f\u0010>\u001a\b\u0012\u0004\u0012\u00020?0\u00012\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000128\b\u0002\u0010\u000f\u001a2\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00040\u0011\u0012\u0004\u0012\u00020\u00040\u00102\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020?\u0012\u0004\u0012\u00020\u00040\u00112\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00040\u00112\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0007\u001a\u0010\u0010H\u001a\u00020\u00022\u0006\u0010I\u001a\u00020\u0012H\u0002\u001a\u0010\u0010J\u001a\u00020\u00022\u0006\u0010K\u001a\u00020\u0012H\u0002\u001a \u0010L\u001a\u00020\u00172\u0006\u0010M\u001a\u00020\u00122\u0006\u0010N\u001a\u00020O2\u0006\u0010P\u001a\u00020\u0012H\u0002\u001a\u0010\u0010Q\u001a\u00020R2\u0006\u0010M\u001a\u00020\u0012H\u0002\"\u0014\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006S"}, d2 = {"GOODS_UNITS", "", "", "ContactSelectorCard", "", "label", "selectedName", "onSelectClick", "Lkotlin/Function0;", "onClearClick", "CurrencyExchangeScreen", "exchanges", "Lcom/sotraubo/app/core/database/entity/CurrencyExchangeEntity;", "contacts", "Lcom/sotraubo/app/core/model/ContactWithSummary;", "onCreateNewContactInline", "Lkotlin/Function5;", "Lkotlin/Function1;", "", "onBack", "onSave", "onDelete", "showBackButton", "", "initialDirectionCode", "FamilyExpenseScreen", "expenses", "Lcom/sotraubo/app/core/database/entity/FamilyExpenseEntity;", "GoodsPurchaseScreen", "goods", "Lcom/sotraubo/app/core/database/entity/GoodsPurchaseEntity;", "HubCard", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "title", "line1", "line2", "onClick", "LedgerCard", "amountText", "amountColor", "Landroidx/compose/ui/graphics/Color;", "content", "Landroidx/compose/runtime/Composable;", "LedgerCard-XO-JAsU", "(Ljava/lang/String;Ljava/lang/String;JLkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;)V", "LedgerFormScaffold", "canSave", "LedgerListScaffold", "T", "addLabel", "items", "dateOf", "keyOf", "summaryLines", "Lkotlin/Pair;", "dayTotalLabel", "emptyTitle", "onAdd", "extraFilterContent", "itemContent", "MoreHubScreen", "quotaFees", "Lcom/sotraubo/app/core/database/entity/QuotaFeeEntity;", "contactCount", "", "onOpenGoods", "onOpenExchange", "onOpenFamily", "onOpenQuotaFees", "onOpenContacts", "QuotaFeeScreen", "formatBaht", "amount", "formatQuantity", "x100", "matchesTime", "millis", "mode", "Lcom/sotraubo/app/core/designsystem/component/TimeFilterMode;", "anchor", "toLocalDate", "Ljava/time/LocalDate;", "app_debug"})
public final class LedgerScreensKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> GOODS_UNITS = null;
    
    private static final java.lang.String formatBaht(long amount) {
        return null;
    }
    
    private static final java.lang.String formatQuantity(long x100) {
        return null;
    }
    
    private static final java.time.LocalDate toLocalDate(long millis) {
        return null;
    }
    
    private static final boolean matchesTime(long millis, com.sotraubo.app.core.designsystem.component.TimeFilterMode mode, long anchor) {
        return false;
    }
    
    /**
     * Khung danh sách dùng chung: Bộ lọc thời gian xổ xuống (Tất cả / Theo tháng / Theo ngày / Hôm nay),
     * khối tổng hợp từng dòng, danh sách nhóm theo ngày.
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final <T extends java.lang.Object>void LedgerListScaffold(java.lang.String title, java.lang.String addLabel, java.util.List<? extends T> items, kotlin.jvm.functions.Function1<? super T, java.lang.Long> dateOf, kotlin.jvm.functions.Function1<? super T, java.lang.Long> keyOf, kotlin.jvm.functions.Function1<? super java.util.List<? extends T>, ? extends java.util.List<kotlin.Pair<java.lang.String, java.lang.String>>> summaryLines, kotlin.jvm.functions.Function1<? super java.util.List<? extends T>, java.lang.String> dayTotalLabel, java.lang.String emptyTitle, kotlin.jvm.functions.Function0<kotlin.Unit> onBack, kotlin.jvm.functions.Function0<kotlin.Unit> onAdd, boolean showBackButton, kotlin.jvm.functions.Function0<kotlin.Unit> extraFilterContent, kotlin.jvm.functions.Function1<? super T, kotlin.Unit> itemContent) {
    }
    
    /**
     * Biểu mẫu toàn màn hình rộng rãi chuẩn Material 3 cho các sổ phụ (thay cho AlertDialog hẹp).
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void LedgerFormScaffold(java.lang.String title, boolean canSave, kotlin.jvm.functions.Function0<kotlin.Unit> onBack, kotlin.jvm.functions.Function0<kotlin.Unit> onSave, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete, kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void MoreHubScreen(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity> goods, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity> exchanges, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity> expenses, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity> quotaFees, int contactCount, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenGoods, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenExchange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenFamily, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenQuotaFees, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenContacts) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void HubCard(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String title, java.lang.String line1, java.lang.String line2, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void GoodsPurchaseScreen(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity> goods, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.ContactWithSummary> contacts, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function5<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit>, kotlin.Unit> onCreateNewContactInline, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.sotraubo.app.core.database.entity.GoodsPurchaseEntity, kotlin.Unit> onSave, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onDelete, boolean showBackButton) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void CurrencyExchangeScreen(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity> exchanges, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.ContactWithSummary> contacts, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function5<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit>, kotlin.Unit> onCreateNewContactInline, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.sotraubo.app.core.database.entity.CurrencyExchangeEntity, kotlin.Unit> onSave, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onDelete, boolean showBackButton, @org.jetbrains.annotations.Nullable()
    java.lang.String initialDirectionCode) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void FamilyExpenseScreen(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity> expenses, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.ContactWithSummary> contacts, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function5<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit>, kotlin.Unit> onCreateNewContactInline, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.sotraubo.app.core.database.entity.FamilyExpenseEntity, kotlin.Unit> onSave, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onDelete, boolean showBackButton) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void QuotaFeeScreen(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity> quotaFees, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.ContactWithSummary> contacts, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function5<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit>, kotlin.Unit> onCreateNewContactInline, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.sotraubo.app.core.database.entity.QuotaFeeEntity, kotlin.Unit> onSave, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onDelete, boolean showBackButton) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ContactSelectorCard(java.lang.String label, java.lang.String selectedName, kotlin.jvm.functions.Function0<kotlin.Unit> onSelectClick, kotlin.jvm.functions.Function0<kotlin.Unit> onClearClick) {
    }
}