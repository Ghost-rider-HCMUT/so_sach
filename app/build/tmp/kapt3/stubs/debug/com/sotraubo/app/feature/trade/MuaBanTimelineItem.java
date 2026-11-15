package com.sotraubo.app.feature.trade;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.TopAppBarDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import com.sotraubo.app.core.common.format.CurrencyFormatter;
import com.sotraubo.app.core.common.format.DateFormatter;
import com.sotraubo.app.core.common.search.VietnameseTextNormalizer;
import com.sotraubo.app.core.designsystem.component.TimeFilterMode;
import com.sotraubo.app.core.model.CashFlowType;
import com.sotraubo.app.core.model.CashTransactionWithContact;
import com.sotraubo.app.core.model.TradeOrderType;
import com.sotraubo.app.core.model.TradeOrderWithPayment;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\br\u0018\u00002\u00020\u0001:\u0002\f\rR\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0012\u0010\n\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\u0005\u0082\u0001\u0002\u000e\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/sotraubo/app/feature/trade/MuaBanTimelineItem;", "", "createdAt", "", "getCreatedAt", "()J", "key", "", "getKey", "()Ljava/lang/String;", "transactionDate", "getTransactionDate", "CashItem", "OrderItem", "Lcom/sotraubo/app/feature/trade/MuaBanTimelineItem$CashItem;", "Lcom/sotraubo/app/feature/trade/MuaBanTimelineItem$OrderItem;", "app_debug"})
abstract interface MuaBanTimelineItem {
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String getKey();
    
    public abstract long getTransactionDate();
    
    public abstract long getCreatedAt();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\fH\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\fX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0014\u0010\u000f\u001a\u00020\bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\n\u00a8\u0006\u001a"}, d2 = {"Lcom/sotraubo/app/feature/trade/MuaBanTimelineItem$CashItem;", "Lcom/sotraubo/app/feature/trade/MuaBanTimelineItem;", "cash", "Lcom/sotraubo/app/core/model/CashTransactionWithContact;", "(Lcom/sotraubo/app/core/model/CashTransactionWithContact;)V", "getCash", "()Lcom/sotraubo/app/core/model/CashTransactionWithContact;", "createdAt", "", "getCreatedAt", "()J", "key", "", "getKey", "()Ljava/lang/String;", "transactionDate", "getTransactionDate", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
    public static final class CashItem implements com.sotraubo.app.feature.trade.MuaBanTimelineItem {
        @org.jetbrains.annotations.NotNull()
        private final com.sotraubo.app.core.model.CashTransactionWithContact cash = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String key = null;
        private final long transactionDate = 0L;
        private final long createdAt = 0L;
        
        public CashItem(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.core.model.CashTransactionWithContact cash) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.core.model.CashTransactionWithContact getCash() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String getKey() {
            return null;
        }
        
        @java.lang.Override()
        public long getTransactionDate() {
            return 0L;
        }
        
        @java.lang.Override()
        public long getCreatedAt() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.core.model.CashTransactionWithContact component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.feature.trade.MuaBanTimelineItem.CashItem copy(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.core.model.CashTransactionWithContact cash) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\nH\u00d6\u0001R\u0014\u0010\u0005\u001a\u00020\u0006X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\nX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0014\u0010\u000f\u001a\u00020\u0006X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\b\u00a8\u0006\u001a"}, d2 = {"Lcom/sotraubo/app/feature/trade/MuaBanTimelineItem$OrderItem;", "Lcom/sotraubo/app/feature/trade/MuaBanTimelineItem;", "order", "Lcom/sotraubo/app/core/model/TradeOrderWithPayment;", "(Lcom/sotraubo/app/core/model/TradeOrderWithPayment;)V", "createdAt", "", "getCreatedAt", "()J", "key", "", "getKey", "()Ljava/lang/String;", "getOrder", "()Lcom/sotraubo/app/core/model/TradeOrderWithPayment;", "transactionDate", "getTransactionDate", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
    public static final class OrderItem implements com.sotraubo.app.feature.trade.MuaBanTimelineItem {
        @org.jetbrains.annotations.NotNull()
        private final com.sotraubo.app.core.model.TradeOrderWithPayment order = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String key = null;
        private final long transactionDate = 0L;
        private final long createdAt = 0L;
        
        public OrderItem(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.core.model.TradeOrderWithPayment order) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.core.model.TradeOrderWithPayment getOrder() {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String getKey() {
            return null;
        }
        
        @java.lang.Override()
        public long getTransactionDate() {
            return 0L;
        }
        
        @java.lang.Override()
        public long getCreatedAt() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.core.model.TradeOrderWithPayment component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.feature.trade.MuaBanTimelineItem.OrderItem copy(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.core.model.TradeOrderWithPayment order) {
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