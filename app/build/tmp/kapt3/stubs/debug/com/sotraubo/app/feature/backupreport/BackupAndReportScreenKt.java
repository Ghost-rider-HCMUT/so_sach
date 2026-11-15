package com.sotraubo.app.feature.backupreport;

import android.net.Uri;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.TopAppBarDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.text.input.KeyboardType;
import androidx.compose.ui.text.input.PasswordVisualTransformation;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import com.sotraubo.app.core.common.format.CurrencyFormatter;
import com.sotraubo.app.core.common.format.DateFormatter;
import com.sotraubo.app.core.designsystem.component.TimeFilterMode;
import com.sotraubo.app.core.model.DashboardFinancialSummary;
import com.sotraubo.app.core.model.PeriodBusinessStats;
import com.sotraubo.app.feature.security.BiometricAuthHelper;
import com.sotraubo.app.feature.security.BiometricAvailability;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000r\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u00b0\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\u0010\b\u001a\u0004\u0018\u00010\u00072\b\u0010\t\u001a\u0004\u0018\u00010\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u000e2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u000e2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\f2$\u0010\u0013\u001a \u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00010\u00142<\u0010\u0018\u001a8\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\u001a\u0012\b\b\u001b\u0012\u0004\b\b(\u001c\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\u001a\u0012\b\b\u001b\u0012\u0004\b\b(\u001d\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u001e0\u00192\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\b\b\u0002\u0010 \u001a\u00020!2\u0014\b\u0002\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020\u00010\u000e2\u0014\b\u0002\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020!0\u000eH\u0007\u001a\"\u0010$\u001a\u00020\u00012\u0006\u0010%\u001a\u00020\u00072\u0006\u0010&\u001a\u00020\u00172\b\b\u0002\u0010\'\u001a\u00020(H\u0003\u001a6\u0010)\u001a\u00020\u00012\u0006\u0010*\u001a\u00020\u00072\u0006\u0010+\u001a\u00020\u00072\b\b\u0002\u0010,\u001a\u00020-2\b\b\u0002\u0010.\u001a\u00020!H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b/\u00100\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u00061"}, d2 = {"BackupAndReportScreen", "", "summary", "Lcom/sotraubo/app/core/model/DashboardFinancialSummary;", "periodStats", "Lcom/sotraubo/app/core/model/PeriodBusinessStats;", "statusMessage", "", "errorMessage", "pendingRestorePreview", "Lcom/sotraubo/app/feature/backupreport/BackupPreviewInfo;", "onBack", "Lkotlin/Function0;", "onExportBackupToUri", "Lkotlin/Function1;", "Landroid/net/Uri;", "onSelectBackupUriForValidation", "onConfirmRestoreOverwrite", "onCancelRestoreOverwrite", "onExportReportPdfToUri", "Lkotlin/Function4;", "Lcom/sotraubo/app/feature/backupreport/PdfReportCategory;", "Lcom/sotraubo/app/core/designsystem/component/TimeFilterMode;", "", "onChangePin", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "oldPin", "newPin", "Lkotlin/Result;", "onLockAppNow", "isBiometricEnabled", "", "onToggleBiometric", "onClearAllDataWithPin", "ReportHighlightBox", "title", "amount", "modifier", "Landroidx/compose/ui/Modifier;", "ReportStatRow", "label", "value", "valueColor", "Landroidx/compose/ui/graphics/Color;", "boldValue", "ReportStatRow-9LQNqLg", "(Ljava/lang/String;Ljava/lang/String;JZ)V", "app_debug"})
public final class BackupAndReportScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void BackupAndReportScreen(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.DashboardFinancialSummary summary, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.PeriodBusinessStats periodStats, @org.jetbrains.annotations.Nullable()
    java.lang.String statusMessage, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage, @org.jetbrains.annotations.Nullable()
    com.sotraubo.app.feature.backupreport.BackupPreviewInfo pendingRestorePreview, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super android.net.Uri, kotlin.Unit> onExportBackupToUri, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super android.net.Uri, kotlin.Unit> onSelectBackupUriForValidation, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onConfirmRestoreOverwrite, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCancelRestoreOverwrite, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function4<? super android.net.Uri, ? super com.sotraubo.app.feature.backupreport.PdfReportCategory, ? super com.sotraubo.app.core.designsystem.component.TimeFilterMode, ? super java.lang.Long, kotlin.Unit> onExportReportPdfToUri, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Result<kotlin.Unit>> onChangePin, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onLockAppNow, boolean isBiometricEnabled, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onToggleBiometric, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, java.lang.Boolean> onClearAllDataWithPin) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ReportHighlightBox(java.lang.String title, long amount, androidx.compose.ui.Modifier modifier) {
    }
}