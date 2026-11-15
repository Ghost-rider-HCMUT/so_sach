package com.sotraubo.app.feature.contact;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.KeyboardCapitalization;
import androidx.compose.ui.text.input.KeyboardType;
import com.sotraubo.app.core.common.format.CurrencyFormatter;
import com.sotraubo.app.core.common.search.VietnameseTextNormalizer;
import com.sotraubo.app.core.model.ContactWithSummary;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\n\u001a\u00ae\u0001\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\b2`\u0010\t\u001a\\\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\r\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u000f\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003H\u0007\u001a\u00b6\u0001\u0010\u0014\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00032\b\b\u0002\u0010\u0016\u001a\u00020\u00032\b\b\u0002\u0010\u0017\u001a\u00020\u00032\b\b\u0002\u0010\u0018\u001a\u00020\u00032\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00032\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u00122`\u0010\u0019\u001a\\\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\r\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u000f\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u00010\nH\u0007\u001a\u0010\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\u0003H\u0000\u00a8\u0006\u001c"}, d2 = {"ContactPickerBottomSheet", "", "title", "", "contacts", "", "Lcom/sotraubo/app/core/model/ContactWithSummary;", "onSelectContact", "Lkotlin/Function1;", "onCreateAndSelectNewContact", "Lkotlin/Function4;", "Lkotlin/ParameterName;", "name", "fullName", "phone", "address", "note", "onDismiss", "Lkotlin/Function0;", "errorMessage", "QuickCreateContactDialog", "initialName", "initialPhone", "initialAddress", "initialNote", "onConfirmSave", "sanitizePhoneInput", "raw", "app_release"})
public final class ContactPickerBottomSheetKt {
    
    /**
     * Hộp thoại trượt lên (Bottom Sheet) cho phép:
     * 1. Tìm kiếm người liên quan đã lưu theo Tên (có dấu / không dấu) hoặc Số điện thoại.
     * 2. Hiển thị công nợ 2 chiều ngay dưới tên từng người.
     * 3. Nút "+ Thêm người mới" ngay tại chỗ và tự động chọn người vừa tạo cho biểu mẫu hiện tại.
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ContactPickerBottomSheet(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.ContactWithSummary> contacts, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.sotraubo.app.core.model.ContactWithSummary, kotlin.Unit> onSelectContact, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function4<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> onCreateAndSelectNewContact, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void QuickCreateContactDialog(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String initialName, @org.jetbrains.annotations.NotNull()
    java.lang.String initialPhone, @org.jetbrains.annotations.NotNull()
    java.lang.String initialAddress, @org.jetbrains.annotations.NotNull()
    java.lang.String initialNote, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function4<? super java.lang.String, ? super java.lang.String, ? super java.lang.String, ? super java.lang.String, kotlin.Unit> onConfirmSave) {
    }
    
    /**
     * Lọc bỏ chữ cái và ký tự đặc biệt khi nhập hoặc dán số điện thoại,
     * chỉ giữ chữ số, dấu '+' ở đầu (mã quốc gia) và khoảng trắng phân cách.
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String sanitizePhoneInput(@org.jetbrains.annotations.NotNull()
    java.lang.String raw) {
        return null;
    }
}