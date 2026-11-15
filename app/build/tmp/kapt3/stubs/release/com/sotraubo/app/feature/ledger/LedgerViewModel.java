package com.sotraubo.app.feature.ledger;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.sotraubo.app.core.database.dao.LedgerDao;
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity;
import com.sotraubo.app.core.database.entity.FamilyExpenseEntity;
import com.sotraubo.app.core.database.entity.GoodsPurchaseEntity;
import com.sotraubo.app.core.database.entity.QuotaFeeEntity;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;

/**
 * Quản lý 4 sổ phụ: Hàng hóa mua, Đổi tiền (Bath), Chi tiêu gia đình, Phí Quota biên giới.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\n\u0018\u0000  2\u00020\u0001:\u0001 B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\bJ\u000e\u0010\u001d\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\fJ\u000e\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u000fJ\u000e\u0010\u001f\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u001d\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\nR\u001d\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\n\u00a8\u0006!"}, d2 = {"Lcom/sotraubo/app/feature/ledger/LedgerViewModel;", "Landroidx/lifecycle/ViewModel;", "dao", "Lcom/sotraubo/app/core/database/dao/LedgerDao;", "(Lcom/sotraubo/app/core/database/dao/LedgerDao;)V", "exchanges", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/sotraubo/app/core/database/entity/CurrencyExchangeEntity;", "getExchanges", "()Lkotlinx/coroutines/flow/StateFlow;", "expenses", "Lcom/sotraubo/app/core/database/entity/FamilyExpenseEntity;", "getExpenses", "goods", "Lcom/sotraubo/app/core/database/entity/GoodsPurchaseEntity;", "getGoods", "quotaFees", "Lcom/sotraubo/app/core/database/entity/QuotaFeeEntity;", "getQuotaFees", "deleteExchange", "", "id", "", "deleteExpense", "deleteGoods", "deleteQuotaFee", "saveExchange", "entity", "saveExpense", "saveGoods", "saveQuotaFee", "Companion", "app_release"})
public final class LedgerViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.sotraubo.app.core.database.dao.LedgerDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity>> goods = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity>> exchanges = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity>> expenses = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity>> quotaFees = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.sotraubo.app.feature.ledger.LedgerViewModel.Companion Companion = null;
    
    public LedgerViewModel(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.dao.LedgerDao dao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.database.entity.GoodsPurchaseEntity>> getGoods() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.database.entity.CurrencyExchangeEntity>> getExchanges() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.database.entity.FamilyExpenseEntity>> getExpenses() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sotraubo.app.core.database.entity.QuotaFeeEntity>> getQuotaFees() {
        return null;
    }
    
    public final void saveGoods(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.GoodsPurchaseEntity entity) {
    }
    
    public final void deleteGoods(long id) {
    }
    
    public final void saveExchange(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.CurrencyExchangeEntity entity) {
    }
    
    public final void deleteExchange(long id) {
    }
    
    public final void saveExpense(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.FamilyExpenseEntity entity) {
    }
    
    public final void deleteExpense(long id) {
    }
    
    public final void saveQuotaFee(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.QuotaFeeEntity entity) {
    }
    
    public final void deleteQuotaFee(long id) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/sotraubo/app/feature/ledger/LedgerViewModel$Companion;", "", "()V", "provideFactory", "Landroidx/lifecycle/ViewModelProvider$Factory;", "dao", "Lcom/sotraubo/app/core/database/dao/LedgerDao;", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.lifecycle.ViewModelProvider.Factory provideFactory(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.core.database.dao.LedgerDao dao) {
            return null;
        }
    }
}