package com.sotraubo.app;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.compose.foundation.layout.Arrangement;
import android.view.ViewTreeObserver;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.semantics.Role;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import com.sotraubo.app.core.model.CashFlowType;
import com.sotraubo.app.core.model.PaymentMethod;
import com.sotraubo.app.core.model.TradeOrderType;
import com.sotraubo.app.feature.ledger.LedgerViewModel;
import com.sotraubo.app.feature.trade.MuaBanFilterType;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bv\u0018\u00002\u00020\u0001:\u000e\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0082\u0001\u000e\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u00a8\u0006\u001e"}, d2 = {"Lcom/sotraubo/app/AppScreen;", "", "BackupAndReport", "CashForm", "ContactDetail", "Contacts", "CurrencyExchange", "Dashboard", "DebtOverview", "FamilyExpenses", "GoodsPurchases", "MoreHub", "MuaBan", "PersonDebtDetail", "QuotaFees", "TradeOrderForm", "Lcom/sotraubo/app/AppScreen$BackupAndReport;", "Lcom/sotraubo/app/AppScreen$CashForm;", "Lcom/sotraubo/app/AppScreen$ContactDetail;", "Lcom/sotraubo/app/AppScreen$Contacts;", "Lcom/sotraubo/app/AppScreen$CurrencyExchange;", "Lcom/sotraubo/app/AppScreen$Dashboard;", "Lcom/sotraubo/app/AppScreen$DebtOverview;", "Lcom/sotraubo/app/AppScreen$FamilyExpenses;", "Lcom/sotraubo/app/AppScreen$GoodsPurchases;", "Lcom/sotraubo/app/AppScreen$MoreHub;", "Lcom/sotraubo/app/AppScreen$MuaBan;", "Lcom/sotraubo/app/AppScreen$PersonDebtDetail;", "Lcom/sotraubo/app/AppScreen$QuotaFees;", "Lcom/sotraubo/app/AppScreen$TradeOrderForm;", "app_release"})
public abstract interface AppScreen {
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/sotraubo/app/AppScreen$BackupAndReport;", "Lcom/sotraubo/app/AppScreen;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
    public static final class BackupAndReport implements com.sotraubo.app.AppScreen {
        @org.jetbrains.annotations.NotNull()
        public static final com.sotraubo.app.AppScreen.BackupAndReport INSTANCE = null;
        
        private BackupAndReport() {
            super();
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B1\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\bJ\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\nJ\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\nJ\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\nJ<\u0010\u0014\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001\u00a2\u0006\u0002\u0010\u0015J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u001bH\u00d6\u0001J\t\u0010\u001c\u001a\u00020\u001dH\u00d6\u0001R\u0015\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\u000e\u0010\nR\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\u000f\u0010\n\u00a8\u0006\u001e"}, d2 = {"Lcom/sotraubo/app/AppScreen$CashForm;", "Lcom/sotraubo/app/AppScreen;", "flowType", "Lcom/sotraubo/app/core/model/CashFlowType;", "preselectedContactId", "", "preselectedOrderId", "editTransactionId", "(Lcom/sotraubo/app/core/model/CashFlowType;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/Long;)V", "getEditTransactionId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getFlowType", "()Lcom/sotraubo/app/core/model/CashFlowType;", "getPreselectedContactId", "getPreselectedOrderId", "component1", "component2", "component3", "component4", "copy", "(Lcom/sotraubo/app/core/model/CashFlowType;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/Long;)Lcom/sotraubo/app/AppScreen$CashForm;", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
    public static final class CashForm implements com.sotraubo.app.AppScreen {
        @org.jetbrains.annotations.NotNull()
        private final com.sotraubo.app.core.model.CashFlowType flowType = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Long preselectedContactId = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Long preselectedOrderId = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Long editTransactionId = null;
        
        public CashForm(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.core.model.CashFlowType flowType, @org.jetbrains.annotations.Nullable()
        java.lang.Long preselectedContactId, @org.jetbrains.annotations.Nullable()
        java.lang.Long preselectedOrderId, @org.jetbrains.annotations.Nullable()
        java.lang.Long editTransactionId) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.core.model.CashFlowType getFlowType() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long getPreselectedContactId() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long getPreselectedOrderId() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long getEditTransactionId() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.core.model.CashFlowType component1() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long component2() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long component3() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.AppScreen.CashForm copy(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.core.model.CashFlowType flowType, @org.jetbrains.annotations.Nullable()
        java.lang.Long preselectedContactId, @org.jetbrains.annotations.Nullable()
        java.lang.Long preselectedOrderId, @org.jetbrains.annotations.Nullable()
        java.lang.Long editTransactionId) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/sotraubo/app/AppScreen$ContactDetail;", "Lcom/sotraubo/app/AppScreen;", "contactId", "", "(J)V", "getContactId", "()J", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
    public static final class ContactDetail implements com.sotraubo.app.AppScreen {
        private final long contactId = 0L;
        
        public ContactDetail(long contactId) {
            super();
        }
        
        public final long getContactId() {
            return 0L;
        }
        
        public final long component1() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.AppScreen.ContactDetail copy(long contactId) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/sotraubo/app/AppScreen$Contacts;", "Lcom/sotraubo/app/AppScreen;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
    public static final class Contacts implements com.sotraubo.app.AppScreen {
        @org.jetbrains.annotations.NotNull()
        public static final com.sotraubo.app.AppScreen.Contacts INSTANCE = null;
        
        private Contacts() {
            super();
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\u000b\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0015\u0010\b\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/sotraubo/app/AppScreen$CurrencyExchange;", "Lcom/sotraubo/app/AppScreen;", "initialDirectionCode", "", "(Ljava/lang/String;)V", "getInitialDirectionCode", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_release"})
    public static final class CurrencyExchange implements com.sotraubo.app.AppScreen {
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String initialDirectionCode = null;
        
        public CurrencyExchange(@org.jetbrains.annotations.Nullable()
        java.lang.String initialDirectionCode) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getInitialDirectionCode() {
            return null;
        }
        
        public CurrencyExchange() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.AppScreen.CurrencyExchange copy(@org.jetbrains.annotations.Nullable()
        java.lang.String initialDirectionCode) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/sotraubo/app/AppScreen$Dashboard;", "Lcom/sotraubo/app/AppScreen;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
    public static final class Dashboard implements com.sotraubo.app.AppScreen {
        @org.jetbrains.annotations.NotNull()
        public static final com.sotraubo.app.AppScreen.Dashboard INSTANCE = null;
        
        private Dashboard() {
            super();
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/sotraubo/app/AppScreen$DebtOverview;", "Lcom/sotraubo/app/AppScreen;", "initialTab", "", "(I)V", "getInitialTab", "()I", "component1", "copy", "equals", "", "other", "", "hashCode", "toString", "", "app_release"})
    public static final class DebtOverview implements com.sotraubo.app.AppScreen {
        private final int initialTab = 0;
        
        public DebtOverview(int initialTab) {
            super();
        }
        
        public final int getInitialTab() {
            return 0;
        }
        
        public DebtOverview() {
            super();
        }
        
        public final int component1() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.AppScreen.DebtOverview copy(int initialTab) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/sotraubo/app/AppScreen$FamilyExpenses;", "Lcom/sotraubo/app/AppScreen;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
    public static final class FamilyExpenses implements com.sotraubo.app.AppScreen {
        @org.jetbrains.annotations.NotNull()
        public static final com.sotraubo.app.AppScreen.FamilyExpenses INSTANCE = null;
        
        private FamilyExpenses() {
            super();
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/sotraubo/app/AppScreen$GoodsPurchases;", "Lcom/sotraubo/app/AppScreen;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
    public static final class GoodsPurchases implements com.sotraubo.app.AppScreen {
        @org.jetbrains.annotations.NotNull()
        public static final com.sotraubo.app.AppScreen.GoodsPurchases INSTANCE = null;
        
        private GoodsPurchases() {
            super();
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/sotraubo/app/AppScreen$MoreHub;", "Lcom/sotraubo/app/AppScreen;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
    public static final class MoreHub implements com.sotraubo.app.AppScreen {
        @org.jetbrains.annotations.NotNull()
        public static final com.sotraubo.app.AppScreen.MoreHub INSTANCE = null;
        
        private MoreHub() {
            super();
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/sotraubo/app/AppScreen$MuaBan;", "Lcom/sotraubo/app/AppScreen;", "initialFilter", "Lcom/sotraubo/app/feature/trade/MuaBanFilterType;", "(Lcom/sotraubo/app/feature/trade/MuaBanFilterType;)V", "getInitialFilter", "()Lcom/sotraubo/app/feature/trade/MuaBanFilterType;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
    public static final class MuaBan implements com.sotraubo.app.AppScreen {
        @org.jetbrains.annotations.NotNull()
        private final com.sotraubo.app.feature.trade.MuaBanFilterType initialFilter = null;
        
        public MuaBan(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.feature.trade.MuaBanFilterType initialFilter) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.feature.trade.MuaBanFilterType getInitialFilter() {
            return null;
        }
        
        public MuaBan() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.feature.trade.MuaBanFilterType component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.AppScreen.MuaBan copy(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.feature.trade.MuaBanFilterType initialFilter) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/sotraubo/app/AppScreen$PersonDebtDetail;", "Lcom/sotraubo/app/AppScreen;", "contactId", "", "(J)V", "getContactId", "()J", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
    public static final class PersonDebtDetail implements com.sotraubo.app.AppScreen {
        private final long contactId = 0L;
        
        public PersonDebtDetail(long contactId) {
            super();
        }
        
        public final long getContactId() {
            return 0L;
        }
        
        public final long component1() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.AppScreen.PersonDebtDetail copy(long contactId) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/sotraubo/app/AppScreen$QuotaFees;", "Lcom/sotraubo/app/AppScreen;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
    public static final class QuotaFees implements com.sotraubo.app.AppScreen {
        @org.jetbrains.annotations.NotNull()
        public static final com.sotraubo.app.AppScreen.QuotaFees INSTANCE = null;
        
        private QuotaFees() {
            super();
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0007J\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\tJ0\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001\u00a2\u0006\u0002\u0010\u0012J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001R\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\n\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\n\u001a\u0004\b\r\u0010\t\u00a8\u0006\u001b"}, d2 = {"Lcom/sotraubo/app/AppScreen$TradeOrderForm;", "Lcom/sotraubo/app/AppScreen;", "orderType", "Lcom/sotraubo/app/core/model/TradeOrderType;", "preselectedContactId", "", "editOrderId", "(Lcom/sotraubo/app/core/model/TradeOrderType;Ljava/lang/Long;Ljava/lang/Long;)V", "getEditOrderId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getOrderType", "()Lcom/sotraubo/app/core/model/TradeOrderType;", "getPreselectedContactId", "component1", "component2", "component3", "copy", "(Lcom/sotraubo/app/core/model/TradeOrderType;Ljava/lang/Long;Ljava/lang/Long;)Lcom/sotraubo/app/AppScreen$TradeOrderForm;", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
    public static final class TradeOrderForm implements com.sotraubo.app.AppScreen {
        @org.jetbrains.annotations.NotNull()
        private final com.sotraubo.app.core.model.TradeOrderType orderType = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Long preselectedContactId = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Long editOrderId = null;
        
        public TradeOrderForm(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.core.model.TradeOrderType orderType, @org.jetbrains.annotations.Nullable()
        java.lang.Long preselectedContactId, @org.jetbrains.annotations.Nullable()
        java.lang.Long editOrderId) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.core.model.TradeOrderType getOrderType() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long getPreselectedContactId() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long getEditOrderId() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.core.model.TradeOrderType component1() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long component2() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.AppScreen.TradeOrderForm copy(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.core.model.TradeOrderType orderType, @org.jetbrains.annotations.Nullable()
        java.lang.Long preselectedContactId, @org.jetbrains.annotations.Nullable()
        java.lang.Long editOrderId) {
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
}