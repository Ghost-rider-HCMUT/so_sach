package com.sotraubo.app;

import android.content.Context;
import android.net.Uri;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.sotraubo.app.core.common.format.DateFormatter;
import com.sotraubo.app.core.common.format.ReportPeriodType;
import com.sotraubo.app.core.designsystem.component.TimeFilterMode;
import com.sotraubo.app.feature.backupreport.BackupAndPdfManager;
import com.sotraubo.app.feature.backupreport.BackupPreviewInfo;
import com.sotraubo.app.feature.backupreport.PdfReportCategory;
import com.sotraubo.app.core.data.repository.CattleBusinessRepository;
import com.sotraubo.app.core.data.repository.OperationOutcome;
import com.sotraubo.app.core.model.CashFlowType;
import com.sotraubo.app.core.model.CashTransactionWithContact;
import com.sotraubo.app.core.model.ContactWithSummary;
import com.sotraubo.app.core.model.DashboardFinancialSummary;
import com.sotraubo.app.core.model.DebtSortOption;
import com.sotraubo.app.core.model.PaymentMethod;
import com.sotraubo.app.core.model.PeriodBusinessStats;
import com.sotraubo.app.core.model.PersonBidirectionalDebtDetail;
import com.sotraubo.app.core.model.PersonDebtSummaryItem;
import com.sotraubo.app.core.model.TradeOrderType;
import com.sotraubo.app.core.model.TradeOrderWithPayment;
import com.sotraubo.app.core.model.UnifiedActivityItem;
import com.sotraubo.app.feature.security.PinSecurityStorage;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00ee\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0012\u0018\u0000 \u0091\u00012\u00020\u0001:\u0002\u0091\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0006\u0010C\u001a\u00020DJ)\u0010E\u001a\b\u0012\u0004\u0012\u00020D0F2\u0006\u0010G\u001a\u00020\u000b2\u0006\u0010H\u001a\u00020\u000b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bI\u0010JJ&\u0010K\u001a\u00020\u00112\u0006\u0010L\u001a\u00020M2\u0006\u0010N\u001a\u00020\u000b2\u000e\b\u0002\u0010O\u001a\b\u0012\u0004\u0012\u00020D0PJ\u0006\u0010Q\u001a\u00020DJ\u000e\u0010R\u001a\u00020D2\u0006\u0010L\u001a\u00020MJ\u001c\u0010S\u001a\u00020D2\u0006\u0010T\u001a\u00020U2\f\u0010V\u001a\b\u0012\u0004\u0012\u00020D0PJ\u001c\u0010W\u001a\u00020D2\u0006\u0010X\u001a\u00020U2\f\u0010V\u001a\b\u0012\u0004\u0012\u00020D0PJ\u001c\u0010Y\u001a\u00020D2\u0006\u0010Z\u001a\u00020U2\f\u0010V\u001a\b\u0012\u0004\u0012\u00020D0PJ\u0016\u0010[\u001a\u00020D2\u0006\u0010L\u001a\u00020M2\u0006\u0010\\\u001a\u00020\u0017J4\u0010]\u001a\u00020D2\u0006\u0010L\u001a\u00020M2\u0006\u0010\\\u001a\u00020\u00172\b\b\u0002\u0010^\u001a\u00020_2\b\b\u0002\u0010`\u001a\u00020a2\b\b\u0002\u0010b\u001a\u00020UJ\u0016\u0010c\u001a\u00020D2\u0006\u0010L\u001a\u00020M2\u0006\u0010d\u001a\u00020eJ\u0006\u0010f\u001a\u00020DJ\u0006\u0010g\u001a\u00020DJ\u0016\u0010h\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010e0\u001b2\u0006\u0010X\u001a\u00020UJ}\u0010i\u001a\u00020D2\b\b\u0002\u0010T\u001a\u00020U2\u0006\u0010j\u001a\u00020k2\b\u0010X\u001a\u0004\u0018\u00010U2\b\u0010l\u001a\u0004\u0018\u00010U2\u0006\u0010m\u001a\u00020\u00112\u0006\u0010n\u001a\u00020U2\u0006\u0010o\u001a\u00020U2\u0006\u0010p\u001a\u00020q2\u0006\u0010r\u001a\u00020\u000b2\u0006\u0010s\u001a\u00020\u000b2\f\u0010t\u001a\b\u0012\u0004\u0012\u00020\u000b0\u001c2\f\u0010V\u001a\b\u0012\u0004\u0012\u00020D0P\u00a2\u0006\u0002\u0010uJF\u0010v\u001a\u00020D2\b\b\u0002\u0010w\u001a\u00020U2\u0006\u0010x\u001a\u00020\u000b2\u0006\u0010y\u001a\u00020\u000b2\u0006\u0010z\u001a\u00020\u000b2\u0006\u0010s\u001a\u00020\u000b2\u0014\b\u0002\u0010V\u001a\u000e\u0012\u0004\u0012\u00020U\u0012\u0004\u0012\u00020D0{J\u0081\u0001\u0010|\u001a\u00020D2\b\b\u0002\u0010Z\u001a\u00020U2\u0006\u0010}\u001a\u00020~2\b\u0010X\u001a\u0004\u0018\u00010U2\u0006\u0010o\u001a\u00020U2\u0007\u0010\u007f\u001a\u00030\u0080\u00012\u0007\u0010\u0081\u0001\u001a\u00020\u000b2\u0007\u0010\u0082\u0001\u001a\u00020U2\u0007\u0010\u0083\u0001\u001a\u00020U2\u0007\u0010\u0084\u0001\u001a\u00020q2\u0006\u0010s\u001a\u00020\u000b2\f\u0010t\u001a\b\u0012\u0004\u0012\u00020\u000b0\u001c2\f\u0010V\u001a\b\u0012\u0004\u0012\u00020D0P\u00a2\u0006\u0003\u0010\u0085\u0001J\u0010\u0010\u0086\u0001\u001a\u00020D2\u0007\u0010\u0087\u0001\u001a\u00020\u0019J\u0010\u0010\u0088\u0001\u001a\u00020D2\u0007\u0010\u0089\u0001\u001a\u00020\u0011J\u000f\u0010\u008a\u0001\u001a\u00020\u00112\u0006\u0010N\u001a\u00020\u000bJ\u0010\u0010\u008b\u0001\u001a\u00020D2\u0007\u0010\u008c\u0001\u001a\u00020\u000bJ\u0010\u0010\u008d\u0001\u001a\u00020D2\u0007\u0010\u008e\u0001\u001a\u00020\u000eJ\u0017\u0010\u008f\u0001\u001a\u00020D2\u0006\u0010L\u001a\u00020M2\u0006\u0010\\\u001a\u00020\u0017J\u000f\u0010\u0090\u0001\u001a\u00020\u00112\u0006\u0010N\u001a\u00020\u000bR\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00110\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00110\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00150\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0016\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00170\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001d0\u001c0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u001d\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020!0\u001c0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001fR\u001d\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020$0\u001c0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010&\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001fR\u0017\u0010(\u001a\b\u0012\u0004\u0012\u00020)0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001fR\u0017\u0010+\u001a\b\u0012\u0004\u0012\u00020\u000b0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001fR\u0017\u0010-\u001a\b\u0012\u0004\u0012\u00020\u000e0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\u001fR\u0019\u0010/\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010\u001fR\u0017\u00101\u001a\b\u0012\u0004\u0012\u00020\u00110\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010\u001fR\u0017\u00102\u001a\b\u0012\u0004\u0012\u00020\u00110\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\u001fR\u0017\u00103\u001a\b\u0012\u0004\u0012\u00020\u00110\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010\u001fR\u001d\u00104\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002050\u001c0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010\u001fR\u0019\u00107\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010\u001fR\u0017\u00109\u001a\b\u0012\u0004\u0012\u00020:0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010\u001fR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010<\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002050\u001c0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010\u001fR\u001d\u0010>\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020?0\u001c0\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010\u001fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010A\u001a\b\u0012\u0004\u0012\u00020\u00190\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010\u001f\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0092\u0001"}, d2 = {"Lcom/sotraubo/app/SoTrauBoViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/sotraubo/app/core/data/repository/CattleBusinessRepository;", "backupAndPdfManager", "Lcom/sotraubo/app/feature/backupreport/BackupAndPdfManager;", "pinSecurityStorage", "Lcom/sotraubo/app/feature/security/PinSecurityStorage;", "(Lcom/sotraubo/app/core/data/repository/CattleBusinessRepository;Lcom/sotraubo/app/feature/backupreport/BackupAndPdfManager;Lcom/sotraubo/app/feature/security/PinSecurityStorage;)V", "_backupStatusMessage", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_debtSearchQuery", "_debtSortOption", "Lcom/sotraubo/app/core/model/DebtSortOption;", "_formErrorMessage", "_isBiometricEnabled", "", "_isPinConfigured", "_isUnlocked", "_pendingRestorePreview", "Lcom/sotraubo/app/feature/backupreport/BackupPreviewInfo;", "_pendingRestoreUri", "Landroid/net/Uri;", "_selectedPeriod", "Lcom/sotraubo/app/core/common/format/ReportPeriodType;", "allCashTransactions", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/sotraubo/app/core/model/CashTransactionWithContact;", "getAllCashTransactions", "()Lkotlinx/coroutines/flow/StateFlow;", "allContacts", "Lcom/sotraubo/app/core/model/ContactWithSummary;", "getAllContacts", "allTradeOrders", "Lcom/sotraubo/app/core/model/TradeOrderWithPayment;", "getAllTradeOrders", "backupStatusMessage", "getBackupStatusMessage", "dashboardSummary", "Lcom/sotraubo/app/core/model/DashboardFinancialSummary;", "getDashboardSummary", "debtSearchQuery", "getDebtSearchQuery", "debtSortOption", "getDebtSortOption", "formErrorMessage", "getFormErrorMessage", "isBiometricEnabled", "isPinConfigured", "isUnlocked", "payableCreditors", "Lcom/sotraubo/app/core/model/PersonDebtSummaryItem;", "getPayableCreditors", "pendingRestorePreview", "getPendingRestorePreview", "periodStats", "Lcom/sotraubo/app/core/model/PeriodBusinessStats;", "getPeriodStats", "receivableDebtors", "getReceivableDebtors", "recentActivities", "Lcom/sotraubo/app/core/model/UnifiedActivityItem;", "getRecentActivities", "selectedPeriod", "getSelectedPeriod", "cancelRestoreBackup", "", "changePin", "Lkotlin/Result;", "oldPin6Digits", "newPin6Digits", "changePin-gIAlu-s", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;", "clearAllDataWithPin", "context", "Landroid/content/Context;", "pin6Digits", "onCleared", "Lkotlin/Function0;", "clearMessages", "confirmRestoreBackup", "deleteCashTransaction", "transactionId", "", "onSuccess", "deleteContact", "contactId", "deleteTradeOrder", "orderId", "exportBackup", "uri", "exportOverviewReportPdf", "category", "Lcom/sotraubo/app/feature/backupreport/PdfReportCategory;", "timeFilterMode", "Lcom/sotraubo/app/core/designsystem/component/TimeFilterMode;", "anchorMillis", "exportPersonDebtPdf", "detail", "Lcom/sotraubo/app/core/model/PersonBidirectionalDebtDetail;", "lockAppNow", "markUnlocked", "observePersonBidirectionalDebt", "saveCashTransaction", "flowType", "Lcom/sotraubo/app/core/model/CashFlowType;", "linkedTradeOrderId", "autoAllocateOldestDebt", "amount", "transactionDate", "paymentMethod", "Lcom/sotraubo/app/core/model/PaymentMethod;", "shortContent", "note", "attachmentPaths", "(JLcom/sotraubo/app/core/model/CashFlowType;Ljava/lang/Long;Ljava/lang/Long;ZJJLcom/sotraubo/app/core/model/PaymentMethod;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Lkotlin/jvm/functions/Function0;)V", "saveContact", "id", "fullName", "phone", "address", "Lkotlin/Function1;", "saveTradeOrder", "orderType", "Lcom/sotraubo/app/core/model/TradeOrderType;", "cattleQuantity", "", "description", "totalAmount", "initialPaidAmount", "initialPaymentMethod", "(JLcom/sotraubo/app/core/model/TradeOrderType;Ljava/lang/Long;JILjava/lang/String;JJLcom/sotraubo/app/core/model/PaymentMethod;Ljava/lang/String;Ljava/util/List;Lkotlin/jvm/functions/Function0;)V", "selectPeriod", "periodType", "setBiometricEnabled", "enabled", "setupNewPin", "updateDebtSearchQuery", "query", "updateDebtSortOption", "option", "validateBackupForRestore", "verifyPin", "Companion", "app_debug"})
@kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
public final class SoTrauBoViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.core.data.repository.CattleBusinessRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.feature.backupreport.BackupAndPdfManager backupAndPdfManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.feature.security.PinSecurityStorage pinSecurityStorage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isUnlocked = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isUnlocked = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isPinConfigured = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isPinConfigured = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isBiometricEnabled = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isBiometricEnabled = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.sotraubo.app.core.common.format.ReportPeriodType> _selectedPeriod = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.sotraubo.app.core.common.format.ReportPeriodType> selectedPeriod = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _debtSearchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> debtSearchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.sotraubo.app.core.model.DebtSortOption> _debtSortOption = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.sotraubo.app.core.model.DebtSortOption> debtSortOption = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _formErrorMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> formErrorMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _backupStatusMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> backupStatusMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<android.net.Uri> _pendingRestoreUri = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.sotraubo.app.feature.backupreport.BackupPreviewInfo> _pendingRestorePreview = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.sotraubo.app.feature.backupreport.BackupPreviewInfo> pendingRestorePreview = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.sotraubo.app.core.model.DashboardFinancialSummary> dashboardSummary = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.model.ContactWithSummary>> allContacts = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment>> allTradeOrders = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact>> allCashTransactions = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.model.UnifiedActivityItem>> recentActivities = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.sotraubo.app.core.model.PeriodBusinessStats> periodStats = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.model.PersonDebtSummaryItem>> receivableDebtors = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.model.PersonDebtSummaryItem>> payableCreditors = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.sotraubo.app.SoTrauBoViewModel.Companion Companion = null;
    
    public SoTrauBoViewModel(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.data.repository.CattleBusinessRepository repository, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.feature.backupreport.BackupAndPdfManager backupAndPdfManager, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.feature.security.PinSecurityStorage pinSecurityStorage) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isUnlocked() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isPinConfigured() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isBiometricEnabled() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.sotraubo.app.core.common.format.ReportPeriodType> getSelectedPeriod() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getDebtSearchQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.sotraubo.app.core.model.DebtSortOption> getDebtSortOption() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getFormErrorMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getBackupStatusMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.sotraubo.app.feature.backupreport.BackupPreviewInfo> getPendingRestorePreview() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.sotraubo.app.core.model.DashboardFinancialSummary> getDashboardSummary() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.model.ContactWithSummary>> getAllContacts() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.model.TradeOrderWithPayment>> getAllTradeOrders() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.model.CashTransactionWithContact>> getAllCashTransactions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.model.UnifiedActivityItem>> getRecentActivities() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.sotraubo.app.core.model.PeriodBusinessStats> getPeriodStats() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.model.PersonDebtSummaryItem>> getReceivableDebtors() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.model.PersonDebtSummaryItem>> getPayableCreditors() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.sotraubo.app.core.model.PersonBidirectionalDebtDetail> observePersonBidirectionalDebt(long contactId) {
        return null;
    }
    
    public final void selectPeriod(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.common.format.ReportPeriodType periodType) {
    }
    
    public final void updateDebtSearchQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void updateDebtSortOption(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.DebtSortOption option) {
    }
    
    public final void clearMessages() {
    }
    
    public final boolean setupNewPin(@org.jetbrains.annotations.NotNull()
    java.lang.String pin6Digits) {
        return false;
    }
    
    public final boolean verifyPin(@org.jetbrains.annotations.NotNull()
    java.lang.String pin6Digits) {
        return false;
    }
    
    public final void lockAppNow() {
    }
    
    public final void markUnlocked() {
    }
    
    public final void setBiometricEnabled(boolean enabled) {
    }
    
    public final void saveContact(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String fullName, @org.jetbrains.annotations.NotNull()
    java.lang.String phone, @org.jetbrains.annotations.NotNull()
    java.lang.String address, @org.jetbrains.annotations.NotNull()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onSuccess) {
    }
    
    public final void deleteContact(long contactId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess) {
    }
    
    public final void saveTradeOrder(long orderId, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.TradeOrderType orderType, @org.jetbrains.annotations.Nullable()
    java.lang.Long contactId, long transactionDate, int cattleQuantity, @org.jetbrains.annotations.NotNull()
    java.lang.String description, long totalAmount, long initialPaidAmount, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.PaymentMethod initialPaymentMethod, @org.jetbrains.annotations.NotNull()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> attachmentPaths, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess) {
    }
    
    public final void deleteTradeOrder(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess) {
    }
    
    public final void saveCashTransaction(long transactionId, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.CashFlowType flowType, @org.jetbrains.annotations.Nullable()
    java.lang.Long contactId, @org.jetbrains.annotations.Nullable()
    java.lang.Long linkedTradeOrderId, boolean autoAllocateOldestDebt, long amount, long transactionDate, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.PaymentMethod paymentMethod, @org.jetbrains.annotations.NotNull()
    java.lang.String shortContent, @org.jetbrains.annotations.NotNull()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> attachmentPaths, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess) {
    }
    
    public final void deleteCashTransaction(long transactionId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess) {
    }
    
    public final void exportBackup(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    public final void validateBackupForRestore(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    public final void confirmRestoreBackup(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    public final void cancelRestoreBackup() {
    }
    
    public final boolean clearAllDataWithPin(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String pin6Digits, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCleared) {
        return false;
    }
    
    public final void exportOverviewReportPdf(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.feature.backupreport.PdfReportCategory category, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.designsystem.component.TimeFilterMode timeFilterMode, long anchorMillis) {
    }
    
    public final void exportPersonDebtPdf(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.model.PersonBidirectionalDebtDetail detail) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/sotraubo/app/SoTrauBoViewModel$Companion;", "", "()V", "provideFactory", "Landroidx/lifecycle/ViewModelProvider$Factory;", "container", "Lcom/sotraubo/app/AppServiceContainer;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.lifecycle.ViewModelProvider.Factory provideFactory(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.AppServiceContainer container) {
            return null;
        }
    }
}