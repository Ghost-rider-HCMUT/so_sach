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

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00008\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001aJ\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\bH\u0003\u001a\u0018\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0007\u001a\b\u0010\u0013\u001a\u00020\bH\u0003\u00a8\u0006\u0014"}, d2 = {"NavExpandTile", "", "title", "", "subtitle", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "selected", "", "onClick", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "centered", "SoTrauBoAppContent", "viewModel", "Lcom/sotraubo/app/SoTrauBoViewModel;", "ledgerViewModel", "Lcom/sotraubo/app/feature/ledger/LedgerViewModel;", "rememberIsKeyboardVisible", "app_release"})
public final class MainActivityKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void SoTrauBoAppContent(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.SoTrauBoViewModel viewModel, @org.jetbrains.annotations.NotNull()
    com.sotraubo.app.feature.ledger.LedgerViewModel ledgerViewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void NavExpandTile(java.lang.String title, java.lang.String subtitle, androidx.compose.ui.graphics.vector.ImageVector icon, boolean selected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier, boolean centered) {
    }
    
    /**
     * Theo dõi bàn phím đang mở hay không bằng trạng thái thật của window.
     * (WindowInsets.isImeVisible của Compose báo sai khi app chưa bật edge-to-edge.)
     */
    @androidx.compose.runtime.Composable()
    private static final boolean rememberIsKeyboardVisible() {
        return false;
    }
}