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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b \n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B\u00a5\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u0012\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\n\u0012\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\n\u0012\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\n\u0012\u000e\b\u0002\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\n\u0012\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\n\u0012\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\n\u0012\u000e\b\u0002\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\n\u00a2\u0006\u0002\u0010\u001aJ\t\u0010+\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00130\nH\u00c6\u0003J\u000f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00150\nH\u00c6\u0003J\u000f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00170\nH\u00c6\u0003J\u000f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00190\nH\u00c6\u0003J\t\u00100\u001a\u00020\u0005H\u00c6\u0003J\t\u00101\u001a\u00020\u0005H\u00c6\u0003J\t\u00102\u001a\u00020\u0005H\u00c6\u0003J\t\u00103\u001a\u00020\u0005H\u00c6\u0003J\u000f\u00104\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u00c6\u0003J\u000f\u00105\u001a\b\u0012\u0004\u0012\u00020\r0\nH\u00c6\u0003J\u000f\u00106\u001a\b\u0012\u0004\u0012\u00020\u000f0\nH\u00c6\u0003J\u000f\u00107\u001a\b\u0012\u0004\u0012\u00020\u00110\nH\u00c6\u0003J\u00bb\u0001\u00108\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\n2\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\n2\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\n2\u000e\b\u0002\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\n2\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\n2\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\n2\u000e\b\u0002\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\nH\u00c6\u0001J\u0013\u00109\u001a\u00020:2\b\u0010;\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010<\u001a\u00020\u0005H\u00d6\u0001J\t\u0010=\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001cR\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001eR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001cR\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001eR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\n\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001eR\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\n\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001eR\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001eR\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\n\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001eR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001cR\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001e\u00a8\u0006>"}, d2 = {"Lcom/sotraubo/app/feature/backupreport/BackupPreviewInfo;", "", "createdAtDisplay", "", "contactCount", "", "tradeOrderCount", "cashTransactionCount", "attachmentCount", "contacts", "", "Lcom/sotraubo/app/core/database/entity/ContactEntity;", "tradeOrders", "Lcom/sotraubo/app/core/database/entity/TradeOrderEntity;", "cashTransactions", "Lcom/sotraubo/app/core/database/entity/CashTransactionEntity;", "attachments", "Lcom/sotraubo/app/core/database/entity/AttachmentEntity;", "goodsPurchases", "Lcom/sotraubo/app/core/database/entity/GoodsPurchaseEntity;", "currencyExchanges", "Lcom/sotraubo/app/core/database/entity/CurrencyExchangeEntity;", "familyExpenses", "Lcom/sotraubo/app/core/database/entity/FamilyExpenseEntity;", "quotaFees", "Lcom/sotraubo/app/core/database/entity/QuotaFeeEntity;", "(Ljava/lang/String;IIIILjava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;)V", "getAttachmentCount", "()I", "getAttachments", "()Ljava/util/List;", "getCashTransactionCount", "getCashTransactions", "getContactCount", "getContacts", "getCreatedAtDisplay", "()Ljava/lang/String;", "getCurrencyExchanges", "getFamilyExpenses", "getGoodsPurchases", "getQuotaFees", "getTradeOrderCount", "getTradeOrders", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "toString", "app_release"})
public final class BackupPreviewInfo {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String createdAtDisplay = null;
    private final int contactCount = 0;
    private final int tradeOrderCount = 0;
    private final int cashTransactionCount = 0;
    private final int attachmentCount = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.sotraubo.app.core.database.entity.ContactEntity> contacts = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.sotraubo.app.core.database.entity.TradeOrderEntity> tradeOrders = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.sotraubo.app.core.database.entity.CashTransactionEntity> cashTransactions = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity> attachments = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity> goodsPurchases = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity> currencyExchanges = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity> familyExpenses = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity> quotaFees = null;
    
    public BackupPreviewInfo(@org.jetbrains.annotations.NotNull()
    java.lang.String createdAtDisplay, int contactCount, int tradeOrderCount, int cashTransactionCount, int attachmentCount, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.ContactEntity> contacts, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.TradeOrderEntity> tradeOrders, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CashTransactionEntity> cashTransactions, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity> attachments, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity> goodsPurchases, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity> currencyExchanges, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity> familyExpenses, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity> quotaFees) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCreatedAtDisplay() {
        return null;
    }
    
    public final int getContactCount() {
        return 0;
    }
    
    public final int getTradeOrderCount() {
        return 0;
    }
    
    public final int getCashTransactionCount() {
        return 0;
    }
    
    public final int getAttachmentCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.ContactEntity> getContacts() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.TradeOrderEntity> getTradeOrders() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.CashTransactionEntity> getCashTransactions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity> getAttachments() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity> getGoodsPurchases() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity> getCurrencyExchanges() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity> getFamilyExpenses() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity> getQuotaFees() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity> component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity> component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity> component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity> component13() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final int component5() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.ContactEntity> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.TradeOrderEntity> component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.CashTransactionEntity> component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sotraubo.app.feature.backupreport.BackupPreviewInfo copy(@org.jetbrains.annotations.NotNull()
    java.lang.String createdAtDisplay, int contactCount, int tradeOrderCount, int cashTransactionCount, int attachmentCount, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.ContactEntity> contacts, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.TradeOrderEntity> tradeOrders, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CashTransactionEntity> cashTransactions, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity> attachments, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity> goodsPurchases, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity> currencyExchanges, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity> familyExpenses, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity> quotaFees) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}