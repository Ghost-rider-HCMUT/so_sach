package com.sotraubo.app.core.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.sotraubo.app.core.database.dao.CattleBusinessDao;
import com.sotraubo.app.core.database.dao.CattleBusinessDao_Impl;
import com.sotraubo.app.core.database.dao.LedgerDao;
import com.sotraubo.app.core.database.dao.LedgerDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CattleBusinessDatabase_Impl extends CattleBusinessDatabase {
  private volatile CattleBusinessDao _cattleBusinessDao;

  private volatile LedgerDao _ledgerDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(6) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `contacts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `full_name` TEXT NOT NULL, `name_normalized` TEXT NOT NULL, `phone` TEXT NOT NULL, `phone_normalized` TEXT NOT NULL, `address` TEXT NOT NULL, `note` TEXT NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_contacts_name_normalized` ON `contacts` (`name_normalized`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_contacts_phone_normalized` ON `contacts` (`phone_normalized`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `trade_orders` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `order_type` TEXT NOT NULL, `contact_id` INTEGER NOT NULL, `transaction_date` INTEGER NOT NULL, `cattle_quantity` INTEGER NOT NULL, `description` TEXT NOT NULL, `total_amount` INTEGER NOT NULL, `note` TEXT NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, FOREIGN KEY(`contact_id`) REFERENCES `contacts`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_trade_orders_order_type` ON `trade_orders` (`order_type`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_trade_orders_contact_id` ON `trade_orders` (`contact_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_trade_orders_transaction_date` ON `trade_orders` (`transaction_date`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `cash_transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `flow_type` TEXT NOT NULL, `contact_id` INTEGER NOT NULL, `trade_order_id` INTEGER, `is_initial_payment` INTEGER NOT NULL, `amount` INTEGER NOT NULL, `transaction_date` INTEGER NOT NULL, `payment_method` TEXT NOT NULL, `short_content` TEXT NOT NULL, `note` TEXT NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, FOREIGN KEY(`contact_id`) REFERENCES `contacts`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT , FOREIGN KEY(`trade_order_id`) REFERENCES `trade_orders`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_cash_transactions_flow_type` ON `cash_transactions` (`flow_type`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_cash_transactions_contact_id` ON `cash_transactions` (`contact_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_cash_transactions_trade_order_id` ON `cash_transactions` (`trade_order_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_cash_transactions_transaction_date` ON `cash_transactions` (`transaction_date`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `attachments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `trade_order_id` INTEGER, `cash_transaction_id` INTEGER, `local_file_path` TEXT NOT NULL, `created_at` INTEGER NOT NULL, FOREIGN KEY(`trade_order_id`) REFERENCES `trade_orders`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`cash_transaction_id`) REFERENCES `cash_transactions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_attachments_trade_order_id` ON `attachments` (`trade_order_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_attachments_cash_transaction_id` ON `attachments` (`cash_transaction_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `goods_purchases` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `purchase_date` INTEGER NOT NULL, `item_name` TEXT NOT NULL, `quantity_x100` INTEGER NOT NULL, `unit` TEXT NOT NULL, `total_amount` INTEGER NOT NULL, `supplier` TEXT NOT NULL, `contact_id` INTEGER, `note` TEXT NOT NULL, `created_at` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_goods_purchases_purchase_date` ON `goods_purchases` (`purchase_date`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `currency_exchanges` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `exchange_date` INTEGER NOT NULL, `direction` TEXT NOT NULL, `baht_amount` INTEGER NOT NULL, `rate` INTEGER NOT NULL, `vnd_amount` INTEGER NOT NULL, `theoretical_vnd_amount` INTEGER NOT NULL DEFAULT 0, `cost_rate` INTEGER NOT NULL DEFAULT 0, `cost_vnd_amount` INTEGER NOT NULL DEFAULT 0, `partner_name` TEXT NOT NULL DEFAULT '', `contact_id` INTEGER, `note` TEXT NOT NULL, `created_at` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_currency_exchanges_exchange_date` ON `currency_exchanges` (`exchange_date`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `family_expenses` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `expense_date` INTEGER NOT NULL, `category` TEXT NOT NULL, `amount` INTEGER NOT NULL, `partner_name` TEXT NOT NULL DEFAULT '', `contact_id` INTEGER, `note` TEXT NOT NULL, `created_at` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_family_expenses_expense_date` ON `family_expenses` (`expense_date`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `quota_fees` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `fee_date` INTEGER NOT NULL, `payer_name` TEXT NOT NULL DEFAULT '', `contact_id` INTEGER, `cattle_quantity` INTEGER NOT NULL DEFAULT 0, `amount` INTEGER NOT NULL, `note` TEXT NOT NULL DEFAULT '', `created_at` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_quota_fees_fee_date` ON `quota_fees` (`fee_date`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3ef5cc70ca8e959789df0ad6532711d3')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `contacts`");
        db.execSQL("DROP TABLE IF EXISTS `trade_orders`");
        db.execSQL("DROP TABLE IF EXISTS `cash_transactions`");
        db.execSQL("DROP TABLE IF EXISTS `attachments`");
        db.execSQL("DROP TABLE IF EXISTS `goods_purchases`");
        db.execSQL("DROP TABLE IF EXISTS `currency_exchanges`");
        db.execSQL("DROP TABLE IF EXISTS `family_expenses`");
        db.execSQL("DROP TABLE IF EXISTS `quota_fees`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsContacts = new HashMap<String, TableInfo.Column>(9);
        _columnsContacts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContacts.put("full_name", new TableInfo.Column("full_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContacts.put("name_normalized", new TableInfo.Column("name_normalized", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContacts.put("phone", new TableInfo.Column("phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContacts.put("phone_normalized", new TableInfo.Column("phone_normalized", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContacts.put("address", new TableInfo.Column("address", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContacts.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContacts.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContacts.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysContacts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesContacts = new HashSet<TableInfo.Index>(2);
        _indicesContacts.add(new TableInfo.Index("index_contacts_name_normalized", false, Arrays.asList("name_normalized"), Arrays.asList("ASC")));
        _indicesContacts.add(new TableInfo.Index("index_contacts_phone_normalized", false, Arrays.asList("phone_normalized"), Arrays.asList("ASC")));
        final TableInfo _infoContacts = new TableInfo("contacts", _columnsContacts, _foreignKeysContacts, _indicesContacts);
        final TableInfo _existingContacts = TableInfo.read(db, "contacts");
        if (!_infoContacts.equals(_existingContacts)) {
          return new RoomOpenHelper.ValidationResult(false, "contacts(com.sotraubo.app.core.database.entity.ContactEntity).\n"
                  + " Expected:\n" + _infoContacts + "\n"
                  + " Found:\n" + _existingContacts);
        }
        final HashMap<String, TableInfo.Column> _columnsTradeOrders = new HashMap<String, TableInfo.Column>(10);
        _columnsTradeOrders.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTradeOrders.put("order_type", new TableInfo.Column("order_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTradeOrders.put("contact_id", new TableInfo.Column("contact_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTradeOrders.put("transaction_date", new TableInfo.Column("transaction_date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTradeOrders.put("cattle_quantity", new TableInfo.Column("cattle_quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTradeOrders.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTradeOrders.put("total_amount", new TableInfo.Column("total_amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTradeOrders.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTradeOrders.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTradeOrders.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTradeOrders = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTradeOrders.add(new TableInfo.ForeignKey("contacts", "RESTRICT", "NO ACTION", Arrays.asList("contact_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTradeOrders = new HashSet<TableInfo.Index>(3);
        _indicesTradeOrders.add(new TableInfo.Index("index_trade_orders_order_type", false, Arrays.asList("order_type"), Arrays.asList("ASC")));
        _indicesTradeOrders.add(new TableInfo.Index("index_trade_orders_contact_id", false, Arrays.asList("contact_id"), Arrays.asList("ASC")));
        _indicesTradeOrders.add(new TableInfo.Index("index_trade_orders_transaction_date", false, Arrays.asList("transaction_date"), Arrays.asList("ASC")));
        final TableInfo _infoTradeOrders = new TableInfo("trade_orders", _columnsTradeOrders, _foreignKeysTradeOrders, _indicesTradeOrders);
        final TableInfo _existingTradeOrders = TableInfo.read(db, "trade_orders");
        if (!_infoTradeOrders.equals(_existingTradeOrders)) {
          return new RoomOpenHelper.ValidationResult(false, "trade_orders(com.sotraubo.app.core.database.entity.TradeOrderEntity).\n"
                  + " Expected:\n" + _infoTradeOrders + "\n"
                  + " Found:\n" + _existingTradeOrders);
        }
        final HashMap<String, TableInfo.Column> _columnsCashTransactions = new HashMap<String, TableInfo.Column>(12);
        _columnsCashTransactions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCashTransactions.put("flow_type", new TableInfo.Column("flow_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCashTransactions.put("contact_id", new TableInfo.Column("contact_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCashTransactions.put("trade_order_id", new TableInfo.Column("trade_order_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCashTransactions.put("is_initial_payment", new TableInfo.Column("is_initial_payment", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCashTransactions.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCashTransactions.put("transaction_date", new TableInfo.Column("transaction_date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCashTransactions.put("payment_method", new TableInfo.Column("payment_method", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCashTransactions.put("short_content", new TableInfo.Column("short_content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCashTransactions.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCashTransactions.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCashTransactions.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCashTransactions = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysCashTransactions.add(new TableInfo.ForeignKey("contacts", "RESTRICT", "NO ACTION", Arrays.asList("contact_id"), Arrays.asList("id")));
        _foreignKeysCashTransactions.add(new TableInfo.ForeignKey("trade_orders", "CASCADE", "NO ACTION", Arrays.asList("trade_order_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesCashTransactions = new HashSet<TableInfo.Index>(4);
        _indicesCashTransactions.add(new TableInfo.Index("index_cash_transactions_flow_type", false, Arrays.asList("flow_type"), Arrays.asList("ASC")));
        _indicesCashTransactions.add(new TableInfo.Index("index_cash_transactions_contact_id", false, Arrays.asList("contact_id"), Arrays.asList("ASC")));
        _indicesCashTransactions.add(new TableInfo.Index("index_cash_transactions_trade_order_id", false, Arrays.asList("trade_order_id"), Arrays.asList("ASC")));
        _indicesCashTransactions.add(new TableInfo.Index("index_cash_transactions_transaction_date", false, Arrays.asList("transaction_date"), Arrays.asList("ASC")));
        final TableInfo _infoCashTransactions = new TableInfo("cash_transactions", _columnsCashTransactions, _foreignKeysCashTransactions, _indicesCashTransactions);
        final TableInfo _existingCashTransactions = TableInfo.read(db, "cash_transactions");
        if (!_infoCashTransactions.equals(_existingCashTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "cash_transactions(com.sotraubo.app.core.database.entity.CashTransactionEntity).\n"
                  + " Expected:\n" + _infoCashTransactions + "\n"
                  + " Found:\n" + _existingCashTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsAttachments = new HashMap<String, TableInfo.Column>(5);
        _columnsAttachments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("trade_order_id", new TableInfo.Column("trade_order_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("cash_transaction_id", new TableInfo.Column("cash_transaction_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("local_file_path", new TableInfo.Column("local_file_path", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAttachments = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysAttachments.add(new TableInfo.ForeignKey("trade_orders", "CASCADE", "NO ACTION", Arrays.asList("trade_order_id"), Arrays.asList("id")));
        _foreignKeysAttachments.add(new TableInfo.ForeignKey("cash_transactions", "CASCADE", "NO ACTION", Arrays.asList("cash_transaction_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesAttachments = new HashSet<TableInfo.Index>(2);
        _indicesAttachments.add(new TableInfo.Index("index_attachments_trade_order_id", false, Arrays.asList("trade_order_id"), Arrays.asList("ASC")));
        _indicesAttachments.add(new TableInfo.Index("index_attachments_cash_transaction_id", false, Arrays.asList("cash_transaction_id"), Arrays.asList("ASC")));
        final TableInfo _infoAttachments = new TableInfo("attachments", _columnsAttachments, _foreignKeysAttachments, _indicesAttachments);
        final TableInfo _existingAttachments = TableInfo.read(db, "attachments");
        if (!_infoAttachments.equals(_existingAttachments)) {
          return new RoomOpenHelper.ValidationResult(false, "attachments(com.sotraubo.app.core.database.entity.AttachmentEntity).\n"
                  + " Expected:\n" + _infoAttachments + "\n"
                  + " Found:\n" + _existingAttachments);
        }
        final HashMap<String, TableInfo.Column> _columnsGoodsPurchases = new HashMap<String, TableInfo.Column>(10);
        _columnsGoodsPurchases.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoodsPurchases.put("purchase_date", new TableInfo.Column("purchase_date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoodsPurchases.put("item_name", new TableInfo.Column("item_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoodsPurchases.put("quantity_x100", new TableInfo.Column("quantity_x100", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoodsPurchases.put("unit", new TableInfo.Column("unit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoodsPurchases.put("total_amount", new TableInfo.Column("total_amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoodsPurchases.put("supplier", new TableInfo.Column("supplier", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoodsPurchases.put("contact_id", new TableInfo.Column("contact_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoodsPurchases.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoodsPurchases.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGoodsPurchases = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGoodsPurchases = new HashSet<TableInfo.Index>(1);
        _indicesGoodsPurchases.add(new TableInfo.Index("index_goods_purchases_purchase_date", false, Arrays.asList("purchase_date"), Arrays.asList("ASC")));
        final TableInfo _infoGoodsPurchases = new TableInfo("goods_purchases", _columnsGoodsPurchases, _foreignKeysGoodsPurchases, _indicesGoodsPurchases);
        final TableInfo _existingGoodsPurchases = TableInfo.read(db, "goods_purchases");
        if (!_infoGoodsPurchases.equals(_existingGoodsPurchases)) {
          return new RoomOpenHelper.ValidationResult(false, "goods_purchases(com.sotraubo.app.core.database.entity.GoodsPurchaseEntity).\n"
                  + " Expected:\n" + _infoGoodsPurchases + "\n"
                  + " Found:\n" + _existingGoodsPurchases);
        }
        final HashMap<String, TableInfo.Column> _columnsCurrencyExchanges = new HashMap<String, TableInfo.Column>(13);
        _columnsCurrencyExchanges.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrencyExchanges.put("exchange_date", new TableInfo.Column("exchange_date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrencyExchanges.put("direction", new TableInfo.Column("direction", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrencyExchanges.put("baht_amount", new TableInfo.Column("baht_amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrencyExchanges.put("rate", new TableInfo.Column("rate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrencyExchanges.put("vnd_amount", new TableInfo.Column("vnd_amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrencyExchanges.put("theoretical_vnd_amount", new TableInfo.Column("theoretical_vnd_amount", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrencyExchanges.put("cost_rate", new TableInfo.Column("cost_rate", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrencyExchanges.put("cost_vnd_amount", new TableInfo.Column("cost_vnd_amount", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrencyExchanges.put("partner_name", new TableInfo.Column("partner_name", "TEXT", true, 0, "''", TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrencyExchanges.put("contact_id", new TableInfo.Column("contact_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrencyExchanges.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrencyExchanges.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCurrencyExchanges = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCurrencyExchanges = new HashSet<TableInfo.Index>(1);
        _indicesCurrencyExchanges.add(new TableInfo.Index("index_currency_exchanges_exchange_date", false, Arrays.asList("exchange_date"), Arrays.asList("ASC")));
        final TableInfo _infoCurrencyExchanges = new TableInfo("currency_exchanges", _columnsCurrencyExchanges, _foreignKeysCurrencyExchanges, _indicesCurrencyExchanges);
        final TableInfo _existingCurrencyExchanges = TableInfo.read(db, "currency_exchanges");
        if (!_infoCurrencyExchanges.equals(_existingCurrencyExchanges)) {
          return new RoomOpenHelper.ValidationResult(false, "currency_exchanges(com.sotraubo.app.core.database.entity.CurrencyExchangeEntity).\n"
                  + " Expected:\n" + _infoCurrencyExchanges + "\n"
                  + " Found:\n" + _existingCurrencyExchanges);
        }
        final HashMap<String, TableInfo.Column> _columnsFamilyExpenses = new HashMap<String, TableInfo.Column>(8);
        _columnsFamilyExpenses.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFamilyExpenses.put("expense_date", new TableInfo.Column("expense_date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFamilyExpenses.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFamilyExpenses.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFamilyExpenses.put("partner_name", new TableInfo.Column("partner_name", "TEXT", true, 0, "''", TableInfo.CREATED_FROM_ENTITY));
        _columnsFamilyExpenses.put("contact_id", new TableInfo.Column("contact_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFamilyExpenses.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFamilyExpenses.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFamilyExpenses = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFamilyExpenses = new HashSet<TableInfo.Index>(1);
        _indicesFamilyExpenses.add(new TableInfo.Index("index_family_expenses_expense_date", false, Arrays.asList("expense_date"), Arrays.asList("ASC")));
        final TableInfo _infoFamilyExpenses = new TableInfo("family_expenses", _columnsFamilyExpenses, _foreignKeysFamilyExpenses, _indicesFamilyExpenses);
        final TableInfo _existingFamilyExpenses = TableInfo.read(db, "family_expenses");
        if (!_infoFamilyExpenses.equals(_existingFamilyExpenses)) {
          return new RoomOpenHelper.ValidationResult(false, "family_expenses(com.sotraubo.app.core.database.entity.FamilyExpenseEntity).\n"
                  + " Expected:\n" + _infoFamilyExpenses + "\n"
                  + " Found:\n" + _existingFamilyExpenses);
        }
        final HashMap<String, TableInfo.Column> _columnsQuotaFees = new HashMap<String, TableInfo.Column>(8);
        _columnsQuotaFees.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuotaFees.put("fee_date", new TableInfo.Column("fee_date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuotaFees.put("payer_name", new TableInfo.Column("payer_name", "TEXT", true, 0, "''", TableInfo.CREATED_FROM_ENTITY));
        _columnsQuotaFees.put("contact_id", new TableInfo.Column("contact_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuotaFees.put("cattle_quantity", new TableInfo.Column("cattle_quantity", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsQuotaFees.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuotaFees.put("note", new TableInfo.Column("note", "TEXT", true, 0, "''", TableInfo.CREATED_FROM_ENTITY));
        _columnsQuotaFees.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysQuotaFees = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesQuotaFees = new HashSet<TableInfo.Index>(1);
        _indicesQuotaFees.add(new TableInfo.Index("index_quota_fees_fee_date", false, Arrays.asList("fee_date"), Arrays.asList("ASC")));
        final TableInfo _infoQuotaFees = new TableInfo("quota_fees", _columnsQuotaFees, _foreignKeysQuotaFees, _indicesQuotaFees);
        final TableInfo _existingQuotaFees = TableInfo.read(db, "quota_fees");
        if (!_infoQuotaFees.equals(_existingQuotaFees)) {
          return new RoomOpenHelper.ValidationResult(false, "quota_fees(com.sotraubo.app.core.database.entity.QuotaFeeEntity).\n"
                  + " Expected:\n" + _infoQuotaFees + "\n"
                  + " Found:\n" + _existingQuotaFees);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3ef5cc70ca8e959789df0ad6532711d3", "a3ec4aa8ec7f1f5473973c15bd6b4190");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "contacts","trade_orders","cash_transactions","attachments","goods_purchases","currency_exchanges","family_expenses","quota_fees");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `trade_orders`");
      _db.execSQL("DELETE FROM `cash_transactions`");
      _db.execSQL("DELETE FROM `contacts`");
      _db.execSQL("DELETE FROM `attachments`");
      _db.execSQL("DELETE FROM `goods_purchases`");
      _db.execSQL("DELETE FROM `currency_exchanges`");
      _db.execSQL("DELETE FROM `family_expenses`");
      _db.execSQL("DELETE FROM `quota_fees`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CattleBusinessDao.class, CattleBusinessDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LedgerDao.class, LedgerDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CattleBusinessDao cattleBusinessDao() {
    if (_cattleBusinessDao != null) {
      return _cattleBusinessDao;
    } else {
      synchronized(this) {
        if(_cattleBusinessDao == null) {
          _cattleBusinessDao = new CattleBusinessDao_Impl(this);
        }
        return _cattleBusinessDao;
      }
    }
  }

  @Override
  public LedgerDao ledgerDao() {
    if (_ledgerDao != null) {
      return _ledgerDao;
    } else {
      synchronized(this) {
        if(_ledgerDao == null) {
          _ledgerDao = new LedgerDao_Impl(this);
        }
        return _ledgerDao;
      }
    }
  }
}
