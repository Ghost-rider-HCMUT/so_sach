package com.sotraubo.app.feature.backupreport;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import com.sotraubo.app.core.common.dispatcher.AppDispatchers;
import com.sotraubo.app.core.common.format.CurrencyFormatter;
import com.sotraubo.app.core.common.format.DateFormatter;
import com.sotraubo.app.core.designsystem.component.TimeFilterMode;
import com.sotraubo.app.core.data.repository.OperationOutcome;
import com.sotraubo.app.core.database.dao.CattleBusinessDao;
import com.sotraubo.app.core.database.dao.LedgerDao;
import com.sotraubo.app.core.database.entity.AttachmentEntity;
import com.sotraubo.app.core.database.entity.CashTransactionEntity;
import com.sotraubo.app.core.database.entity.ContactEntity;
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity;
import com.sotraubo.app.core.database.entity.FamilyExpenseEntity;
import com.sotraubo.app.core.database.entity.GoodsPurchaseEntity;
import com.sotraubo.app.core.database.entity.QuotaFeeEntity;
import com.sotraubo.app.core.database.entity.TradeOrderEntity;
import com.sotraubo.app.core.model.DashboardFinancialSummary;
import com.sotraubo.app.core.model.PeriodBusinessStats;
import com.sotraubo.app.core.model.PersonBidirectionalDebtDetail;
import com.sotraubo.app.core.model.PersonDebtSummaryItem;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Bộ quản lý Sao lưu & Khôi phục dữ liệu (.zip chứa manifest.json + database_export.json + SHA-256)
 * và Xuất báo cáo PDF 100% Tiếng Việt.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00b2\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u0012\n\u0002\b\u0004\u0018\u0000 H2\u00020\u0001:\u0001HB\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0085\u0001\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\f2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\f2\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\f2\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\f2\u000e\b\u0002\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\f2\u000e\b\u0002\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\fH\u0000\u00a2\u0006\u0002\b\u001cJ\u001c\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001e2\u0006\u0010 \u001a\u00020!H\u0086@\u00a2\u0006\u0002\u0010\"J,\u0010#\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001e2\u0006\u0010 \u001a\u00020!2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\'H\u0086@\u00a2\u0006\u0002\u0010(J$\u0010)\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001e2\u0006\u0010 \u001a\u00020!2\u0006\u0010*\u001a\u00020+H\u0086@\u00a2\u0006\u0002\u0010,J$\u0010-\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001e2\u0006\u0010 \u001a\u00020!2\u0006\u0010.\u001a\u00020%H\u0086@\u00a2\u0006\u0002\u0010/Jn\u00100\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001e2\u0006\u0010 \u001a\u00020!2\u0006\u0010.\u001a\u00020%2\u0006\u00101\u001a\u0002022\u0006\u00103\u001a\u0002042\f\u00105\u001a\b\u0012\u0004\u0012\u0002060\f2\f\u00107\u001a\b\u0012\u0004\u0012\u0002060\f2\b\b\u0002\u00108\u001a\u0002092\b\b\u0002\u0010:\u001a\u00020;2\b\b\u0002\u0010<\u001a\u00020=H\u0086@\u00a2\u0006\u0002\u0010>J\u001d\u0010?\u001a\u00020\'2\u0006\u0010@\u001a\u00020\n2\u0006\u0010A\u001a\u00020\u001fH\u0000\u00a2\u0006\u0002\bBJ\u0010\u0010C\u001a\u00020\u001f2\u0006\u0010D\u001a\u00020EH\u0002J$\u0010F\u001a\b\u0012\u0004\u0012\u00020\'0\u001e2\u0006\u0010 \u001a\u00020!2\u0006\u0010$\u001a\u00020%H\u0086@\u00a2\u0006\u0002\u0010/J\f\u0010G\u001a\u00020=*\u00020\u0017H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006I"}, d2 = {"Lcom/sotraubo/app/feature/backupreport/BackupAndPdfManager;", "", "dao", "Lcom/sotraubo/app/core/database/dao/CattleBusinessDao;", "ledgerDao", "Lcom/sotraubo/app/core/database/dao/LedgerDao;", "dispatchers", "Lcom/sotraubo/app/core/common/dispatcher/AppDispatchers;", "(Lcom/sotraubo/app/core/database/dao/CattleBusinessDao;Lcom/sotraubo/app/core/database/dao/LedgerDao;Lcom/sotraubo/app/core/common/dispatcher/AppDispatchers;)V", "buildDatabaseJson", "Lorg/json/JSONObject;", "contacts", "", "Lcom/sotraubo/app/core/database/entity/ContactEntity;", "orders", "Lcom/sotraubo/app/core/database/entity/TradeOrderEntity;", "cashTransactions", "Lcom/sotraubo/app/core/database/entity/CashTransactionEntity;", "attachments", "Lcom/sotraubo/app/core/database/entity/AttachmentEntity;", "goodsPurchases", "Lcom/sotraubo/app/core/database/entity/GoodsPurchaseEntity;", "currencyExchanges", "Lcom/sotraubo/app/core/database/entity/CurrencyExchangeEntity;", "familyExpenses", "Lcom/sotraubo/app/core/database/entity/FamilyExpenseEntity;", "quotaFees", "Lcom/sotraubo/app/core/database/entity/QuotaFeeEntity;", "buildDatabaseJson$app_debug", "clearAllDemoData", "Lcom/sotraubo/app/core/data/repository/OperationOutcome;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "confirmRestoreBackup", "sourceUri", "Landroid/net/Uri;", "preview", "Lcom/sotraubo/app/feature/backupreport/BackupPreviewInfo;", "(Landroid/content/Context;Landroid/net/Uri;Lcom/sotraubo/app/feature/backupreport/BackupPreviewInfo;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportAndSharePersonDebtPdf", "detail", "Lcom/sotraubo/app/core/model/PersonBidirectionalDebtDetail;", "(Landroid/content/Context;Lcom/sotraubo/app/core/model/PersonBidirectionalDebtDetail;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportBackupToUri", "destinationUri", "(Landroid/content/Context;Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportOverviewReportPdf", "summary", "Lcom/sotraubo/app/core/model/DashboardFinancialSummary;", "periodStats", "Lcom/sotraubo/app/core/model/PeriodBusinessStats;", "receivableDebtors", "Lcom/sotraubo/app/core/model/PersonDebtSummaryItem;", "payableCreditors", "category", "Lcom/sotraubo/app/feature/backupreport/PdfReportCategory;", "timeFilterMode", "Lcom/sotraubo/app/core/designsystem/component/TimeFilterMode;", "anchorMillis", "", "(Landroid/content/Context;Landroid/net/Uri;Lcom/sotraubo/app/core/model/DashboardFinancialSummary;Lcom/sotraubo/app/core/model/PeriodBusinessStats;Ljava/util/List;Ljava/util/List;Lcom/sotraubo/app/feature/backupreport/PdfReportCategory;Lcom/sotraubo/app/core/designsystem/component/TimeFilterMode;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "parseDatabaseJson", "root", "createdAtDisplay", "parseDatabaseJson$app_debug", "sha256Hex", "bytes", "", "validateBackupUri", "resolvedCostVnd", "Companion", "app_debug"})
public final class BackupAndPdfManager {
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.core.database.dao.CattleBusinessDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.core.database.dao.LedgerDao ledgerDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.core.common.dispatcher.AppDispatchers dispatchers = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String APP_IDENTIFIER = "com.sotraubo.app";
    private static final int SCHEMA_VERSION = 2;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String MANIFEST_ENTRY = "manifest.json";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String DATABASE_ENTRY = "database_export.json";
    @org.jetbrains.annotations.NotNull()
    public static final com.sotraubo.app.feature.backupreport.BackupAndPdfManager.Companion Companion = null;
    
    public BackupAndPdfManager(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.dao.CattleBusinessDao dao, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.dao.LedgerDao ledgerDao, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.common.dispatcher.AppDispatchers dispatchers) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object clearAllDemoData(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<java.lang.String>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object exportBackupToUri(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.net.Uri destinationUri, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<java.lang.String>> $completion) {
        return null;
    }
    
    /**
     * Kiểm tra tính hợp lệ của tệp sao lưu trước khi khôi phục (Mục 8 Master Prompt).
     * Trả về BackupPreviewInfo để hiển thị hộp thoại Cảnh báo ghi đè cho người dùng.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object validateBackupUri(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.net.Uri sourceUri, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<com.sotraubo.app.feature.backupreport.BackupPreviewInfo>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object confirmRestoreBackup(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.net.Uri sourceUri, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.feature.backupreport.BackupPreviewInfo preview, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<java.lang.String>> $completion) {
        return null;
    }
    
    /**
     * Xuất báo cáo PDF theo loại (Buôn bán hàng trâu bò / Đổi Bath Thái / Tất cả) và theo ngày/tháng.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object exportOverviewReportPdf(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.net.Uri destinationUri, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.DashboardFinancialSummary summary, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.PeriodBusinessStats periodStats, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.PersonDebtSummaryItem> receivableDebtors, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.model.PersonDebtSummaryItem> payableCreditors, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.feature.backupreport.PdfReportCategory category, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.designsystem.component.TimeFilterMode timeFilterMode, long anchorMillis, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<java.lang.String>> $completion) {
        return null;
    }
    
    /**
     * Xuất Phiếu Đối Chiếu Công Nợ 2 Chiều của 1 người ra tệp PDF trong bộ nhớ cache và mở hộp thoại Chia sẻ (Zalo / In).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object exportAndSharePersonDebtPdf(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.PersonBidirectionalDebtDetail detail, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.data.repository.OperationOutcome<java.lang.String>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.json.JSONObject buildDatabaseJson$app_debug(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.ContactEntity> contacts, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.TradeOrderEntity> orders, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CashTransactionEntity> cashTransactions, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity> attachments, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity> goodsPurchases, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity> currencyExchanges, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity> familyExpenses, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity> quotaFees) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.feature.backupreport.BackupPreviewInfo parseDatabaseJson$app_debug(@org.jetbrains.annotations.NotNull()
    org.json.JSONObject root, @org.jetbrains.annotations.NotNull()
    java.lang.String createdAtDisplay) {
        return null;
    }
    
    private final long resolvedCostVnd(com.sotraubo.app.core.database.entity.CurrencyExchangeEntity $this$resolvedCostVnd) {
        return 0L;
    }
    
    private final java.lang.String sha256Hex(byte[] bytes) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/sotraubo/app/feature/backupreport/BackupAndPdfManager$Companion;", "", "()V", "APP_IDENTIFIER", "", "DATABASE_ENTRY", "MANIFEST_ENTRY", "SCHEMA_VERSION", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}