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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b\u00a8\u0006\f"}, d2 = {"Lcom/sotraubo/app/feature/trade/MuaBanFilterType;", "", "label", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getLabel", "()Ljava/lang/String;", "ALL", "PURCHASE", "SALE", "CASH_IN", "CASH_OUT", "app_debug"})
public enum MuaBanFilterType {
    /*public static final*/ ALL /* = new ALL(null) */,
    /*public static final*/ PURCHASE /* = new PURCHASE(null) */,
    /*public static final*/ SALE /* = new SALE(null) */,
    /*public static final*/ CASH_IN /* = new CASH_IN(null) */,
    /*public static final*/ CASH_OUT /* = new CASH_OUT(null) */;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String label = null;
    
    MuaBanFilterType(java.lang.String label) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLabel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.sotraubo.app.feature.trade.MuaBanFilterType> getEntries() {
        return null;
    }
}