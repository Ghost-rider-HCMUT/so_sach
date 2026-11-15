package com.sotraubo.app.core.designsystem.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import com.sotraubo.app.core.common.format.DateFormatter;
import java.io.File;
import java.io.FileOutputStream;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00004\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\u001aR\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a\"\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a6\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\r2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001aJ\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00142\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00010\u00072\u0006\u0010\u0016\u001a\u00020\r2\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a6\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\r2\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u00a8\u0006\u0019"}, d2 = {"AttachmentPickerSection", "", "label", "", "attachmentPaths", "", "onAddAttachmentPath", "Lkotlin/Function1;", "onRemoveAttachmentPath", "modifier", "Landroidx/compose/ui/Modifier;", "DateSectionHeader", "dateMillis", "", "countLabel", "VietnameseDatePickerField", "selectedDateMillis", "onDateSelected", "VietnameseTimeFilterBar", "mode", "Lcom/sotraubo/app/core/designsystem/component/TimeFilterMode;", "onModeChange", "anchorMillis", "onAnchorMillisChange", "VietnameseTimePeriodStepper", "app_release"})
public final class AttachmentAndDateComponentsKt {
    
    @androidx.compose.runtime.Composable()
    public static final void AttachmentPickerSection(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> attachmentPaths, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onAddAttachmentPath, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onRemoveAttachmentPath, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void VietnameseDatePickerField(@org.jetbrains.annotations.NotNull()
    java.lang.String label, long selectedDateMillis, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onDateSelected, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Thanh lọc thời gian thông minh theo Tháng / Ngày / Hôm nay (100% inline, chuyển tháng/ngày bằng 1 chạm).
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void VietnameseTimeFilterBar(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.designsystem.component.TimeFilterMode mode, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.sotraubo.app.core.designsystem.component.TimeFilterMode, kotlin.Unit> onModeChange, long anchorMillis, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onAnchorMillisChange, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Thanh chuyển nhanh Tháng / Ngày (`◀ Tháng 11/2026 ▶`) dùng độc lập hoặc bên dưới bộ lọc thời gian.
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void VietnameseTimePeriodStepper(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.designsystem.component.TimeFilterMode mode, long anchorMillis, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onAnchorMillisChange, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Tiêu đề nhóm theo Ngày (hiển thị inline 1 dòng giữa danh sách giao dịch / mua bán).
     */
    @androidx.compose.runtime.Composable()
    public static final void DateSectionHeader(long dateMillis, @org.jetbrains.annotations.NotNull()
    java.lang.String countLabel, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}