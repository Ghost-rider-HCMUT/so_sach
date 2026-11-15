package com.sotraubo.app.core.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00072\u00020\u0001:\u0001\u0007B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\b"}, d2 = {"Lcom/sotraubo/app/core/database/CattleBusinessDatabase;", "Landroidx/room/RoomDatabase;", "()V", "cattleBusinessDao", "Lcom/sotraubo/app/core/database/dao/CattleBusinessDao;", "ledgerDao", "Lcom/sotraubo/app/core/database/dao/LedgerDao;", "Companion", "app_release"})
@androidx.room.Database(entities = {com.sotraubo.app.core.database.entity.ContactEntity.class, com.sotraubo.app.core.database.entity.TradeOrderEntity.class, com.sotraubo.app.core.database.entity.CashTransactionEntity.class, com.sotraubo.app.core.database.entity.AttachmentEntity.class, com.sotraubo.app.core.database.entity.GoodsPurchaseEntity.class, com.sotraubo.app.core.database.entity.CurrencyExchangeEntity.class, com.sotraubo.app.core.database.entity.FamilyExpenseEntity.class, com.sotraubo.app.core.database.entity.QuotaFeeEntity.class}, version = 6, exportSchema = false)
public abstract class CattleBusinessDatabase extends androidx.room.RoomDatabase {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DATABASE_NAME = "so_trau_bo.db";
    
    /**
     * v1 -> v2: thêm 3 bảng Hàng hóa mua, Đổi tiền, Chi tiêu gia đình (giữ nguyên dữ liệu cũ).
     */
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_1_2 = null;
    
    /**
     * v2 -> v3: thêm theoretical_vnd_amount cho giao dịch Đổi Bath (thu lý thuyết vs thực tế).
     */
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_2_3 = null;
    
    /**
     * v3 -> v4: thêm partner_name/contact_id cho Đổi Bath và bảng quota_fees thu phí quota biên giới.
     */
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_3_4 = null;
    
    /**
     * v4 -> v5: thêm contact_id cho Hàng hóa mua và partner_name/contact_id cho Chi tiêu gia đình.
     */
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_4_5 = null;
    
    /**
     * v5 -> v6: thêm cost_rate và cost_vnd_amount cho giao dịch Đổi Bath (giá Bath vốn & giá bán).
     */
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_5_6 = null;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.sotraubo.app.core.database.CattleBusinessDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.sotraubo.app.core.database.CattleBusinessDatabase.Companion Companion = null;
    
    public CattleBusinessDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.sotraubo.app.core.database.dao.CattleBusinessDao cattleBusinessDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.sotraubo.app.core.database.dao.LedgerDao ledgerDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u0015R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\r\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\nR\u0011\u0010\u000f\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\nR\u0011\u0010\u0011\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\n\u00a8\u0006\u0016"}, d2 = {"Lcom/sotraubo/app/core/database/CattleBusinessDatabase$Companion;", "", "()V", "DATABASE_NAME", "", "INSTANCE", "Lcom/sotraubo/app/core/database/CattleBusinessDatabase;", "MIGRATION_1_2", "Landroidx/room/migration/Migration;", "getMIGRATION_1_2", "()Landroidx/room/migration/Migration;", "MIGRATION_2_3", "getMIGRATION_2_3", "MIGRATION_3_4", "getMIGRATION_3_4", "MIGRATION_4_5", "getMIGRATION_4_5", "MIGRATION_5_6", "getMIGRATION_5_6", "getInstance", "context", "Landroid/content/Context;", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * v1 -> v2: thêm 3 bảng Hàng hóa mua, Đổi tiền, Chi tiêu gia đình (giữ nguyên dữ liệu cũ).
         */
        @org.jetbrains.annotations.NotNull()
        public final androidx.room.migration.Migration getMIGRATION_1_2() {
            return null;
        }
        
        /**
         * v2 -> v3: thêm theoretical_vnd_amount cho giao dịch Đổi Bath (thu lý thuyết vs thực tế).
         */
        @org.jetbrains.annotations.NotNull()
        public final androidx.room.migration.Migration getMIGRATION_2_3() {
            return null;
        }
        
        /**
         * v3 -> v4: thêm partner_name/contact_id cho Đổi Bath và bảng quota_fees thu phí quota biên giới.
         */
        @org.jetbrains.annotations.NotNull()
        public final androidx.room.migration.Migration getMIGRATION_3_4() {
            return null;
        }
        
        /**
         * v4 -> v5: thêm contact_id cho Hàng hóa mua và partner_name/contact_id cho Chi tiêu gia đình.
         */
        @org.jetbrains.annotations.NotNull()
        public final androidx.room.migration.Migration getMIGRATION_4_5() {
            return null;
        }
        
        /**
         * v5 -> v6: thêm cost_rate và cost_vnd_amount cho giao dịch Đổi Bath (giá Bath vốn & giá bán).
         */
        @org.jetbrains.annotations.NotNull()
        public final androidx.room.migration.Migration getMIGRATION_5_6() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sotraubo.app.core.database.CattleBusinessDatabase getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}