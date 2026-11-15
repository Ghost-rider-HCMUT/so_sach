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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0014R\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\b\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0012"}, d2 = {"Lcom/sotraubo/app/MainActivity;", "Landroidx/fragment/app/FragmentActivity;", "()V", "ledgerViewModel", "Lcom/sotraubo/app/feature/ledger/LedgerViewModel;", "getLedgerViewModel", "()Lcom/sotraubo/app/feature/ledger/LedgerViewModel;", "ledgerViewModel$delegate", "Lkotlin/Lazy;", "viewModel", "Lcom/sotraubo/app/SoTrauBoViewModel;", "getViewModel", "()Lcom/sotraubo/app/SoTrauBoViewModel;", "viewModel$delegate", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "app_release"})
public final class MainActivity extends androidx.fragment.app.FragmentActivity {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy ledgerViewModel$delegate = null;
    
    public MainActivity() {
        super();
    }
    
    private final com.sotraubo.app.SoTrauBoViewModel getViewModel() {
        return null;
    }
    
    private final com.sotraubo.app.feature.ledger.LedgerViewModel getLedgerViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
}