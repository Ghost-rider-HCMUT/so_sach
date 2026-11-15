package com.sotraubo.app.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sotraubo.app.core.database.dao.CattleBusinessDao
import com.sotraubo.app.core.database.dao.LedgerDao
import com.sotraubo.app.core.database.entity.AttachmentEntity
import com.sotraubo.app.core.database.entity.CashTransactionEntity
import com.sotraubo.app.core.database.entity.ContactEntity
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity
import com.sotraubo.app.core.database.entity.FamilyExpenseEntity
import com.sotraubo.app.core.database.entity.GoodsPurchaseEntity
import com.sotraubo.app.core.database.entity.QuotaFeeEntity
import com.sotraubo.app.core.database.entity.TradeOrderEntity

@Database(
    entities = [
        ContactEntity::class,
        TradeOrderEntity::class,
        CashTransactionEntity::class,
        AttachmentEntity::class,
        GoodsPurchaseEntity::class,
        CurrencyExchangeEntity::class,
        FamilyExpenseEntity::class,
        QuotaFeeEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class CattleBusinessDatabase : RoomDatabase() {

    abstract fun cattleBusinessDao(): CattleBusinessDao

    abstract fun ledgerDao(): LedgerDao

    companion object {
        const val DATABASE_NAME = "so_trau_bo.db"

        /** v1 -> v2: thêm 3 bảng Hàng hóa mua, Đổi tiền, Chi tiêu gia đình (giữ nguyên dữ liệu cũ). */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `goods_purchases` (" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`purchase_date` INTEGER NOT NULL, " +
                        "`item_name` TEXT NOT NULL, " +
                        "`quantity_x100` INTEGER NOT NULL, " +
                        "`unit` TEXT NOT NULL, " +
                        "`total_amount` INTEGER NOT NULL, " +
                        "`supplier` TEXT NOT NULL, " +
                        "`note` TEXT NOT NULL, " +
                        "`created_at` INTEGER NOT NULL)"
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_goods_purchases_purchase_date` ON `goods_purchases` (`purchase_date`)")
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `currency_exchanges` (" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`exchange_date` INTEGER NOT NULL, " +
                        "`direction` TEXT NOT NULL, " +
                        "`baht_amount` INTEGER NOT NULL, " +
                        "`rate` INTEGER NOT NULL, " +
                        "`vnd_amount` INTEGER NOT NULL, " +
                        "`note` TEXT NOT NULL, " +
                        "`created_at` INTEGER NOT NULL)"
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_currency_exchanges_exchange_date` ON `currency_exchanges` (`exchange_date`)")
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `family_expenses` (" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`expense_date` INTEGER NOT NULL, " +
                        "`category` TEXT NOT NULL, " +
                        "`amount` INTEGER NOT NULL, " +
                        "`note` TEXT NOT NULL, " +
                        "`created_at` INTEGER NOT NULL)"
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_family_expenses_expense_date` ON `family_expenses` (`expense_date`)")
            }
        }

        /** v2 -> v3: thêm theoretical_vnd_amount cho giao dịch Đổi Bath (thu lý thuyết vs thực tế). */
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `currency_exchanges` ADD COLUMN `theoretical_vnd_amount` INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        /** v3 -> v4: thêm partner_name/contact_id cho Đổi Bath và bảng quota_fees thu phí quota biên giới. */
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `currency_exchanges` ADD COLUMN `partner_name` TEXT NOT NULL DEFAULT ''"
                )
                db.execSQL(
                    "ALTER TABLE `currency_exchanges` ADD COLUMN `contact_id` INTEGER"
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `quota_fees` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `fee_date` INTEGER NOT NULL,
                        `payer_name` TEXT NOT NULL DEFAULT '',
                        `contact_id` INTEGER,
                        `cattle_quantity` INTEGER NOT NULL DEFAULT 0,
                        `amount` INTEGER NOT NULL,
                        `note` TEXT NOT NULL DEFAULT '',
                        `created_at` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_quota_fees_fee_date` ON `quota_fees` (`fee_date`)"
                )
            }
        }

        /** v4 -> v5: thêm contact_id cho Hàng hóa mua và partner_name/contact_id cho Chi tiêu gia đình. */
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `goods_purchases` ADD COLUMN `contact_id` INTEGER"
                )
                db.execSQL(
                    "ALTER TABLE `family_expenses` ADD COLUMN `partner_name` TEXT NOT NULL DEFAULT ''"
                )
                db.execSQL(
                    "ALTER TABLE `family_expenses` ADD COLUMN `contact_id` INTEGER"
                )
            }
        }

        /** v5 -> v6: thêm cost_rate và cost_vnd_amount cho giao dịch Đổi Bath (giá Bath vốn & giá bán). */
        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `currency_exchanges` ADD COLUMN `cost_rate` INTEGER NOT NULL DEFAULT 0"
                )
                db.execSQL(
                    "ALTER TABLE `currency_exchanges` ADD COLUMN `cost_vnd_amount` INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        @Volatile
        private var INSTANCE: CattleBusinessDatabase? = null

        fun getInstance(context: Context): CattleBusinessDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CattleBusinessDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
