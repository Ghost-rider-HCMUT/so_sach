package com.sotraubo.app.core.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity;
import com.sotraubo.app.core.database.entity.FamilyExpenseEntity;
import com.sotraubo.app.core.database.entity.GoodsPurchaseEntity;
import com.sotraubo.app.core.database.entity.QuotaFeeEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LedgerDao_Impl implements LedgerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GoodsPurchaseEntity> __insertionAdapterOfGoodsPurchaseEntity;

  private final EntityInsertionAdapter<CurrencyExchangeEntity> __insertionAdapterOfCurrencyExchangeEntity;

  private final EntityInsertionAdapter<FamilyExpenseEntity> __insertionAdapterOfFamilyExpenseEntity;

  private final EntityInsertionAdapter<QuotaFeeEntity> __insertionAdapterOfQuotaFeeEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteGoods;

  private final SharedSQLiteStatement __preparedStmtOfClearAllGoods;

  private final SharedSQLiteStatement __preparedStmtOfDeleteExchange;

  private final SharedSQLiteStatement __preparedStmtOfClearAllExchanges;

  private final SharedSQLiteStatement __preparedStmtOfDeleteExpense;

  private final SharedSQLiteStatement __preparedStmtOfClearAllExpenses;

  private final SharedSQLiteStatement __preparedStmtOfDeleteQuotaFee;

  private final SharedSQLiteStatement __preparedStmtOfClearAllQuotaFees;

  public LedgerDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGoodsPurchaseEntity = new EntityInsertionAdapter<GoodsPurchaseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `goods_purchases` (`id`,`purchase_date`,`item_name`,`quantity_x100`,`unit`,`total_amount`,`supplier`,`contact_id`,`note`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GoodsPurchaseEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPurchaseDate());
        if (entity.getItemName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getItemName());
        }
        statement.bindLong(4, entity.getQuantityX100());
        if (entity.getUnit() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getUnit());
        }
        statement.bindLong(6, entity.getTotalAmount());
        if (entity.getSupplier() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getSupplier());
        }
        if (entity.getContactId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getContactId());
        }
        if (entity.getNote() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNote());
        }
        statement.bindLong(10, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfCurrencyExchangeEntity = new EntityInsertionAdapter<CurrencyExchangeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `currency_exchanges` (`id`,`exchange_date`,`direction`,`baht_amount`,`rate`,`vnd_amount`,`theoretical_vnd_amount`,`cost_rate`,`cost_vnd_amount`,`partner_name`,`contact_id`,`note`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CurrencyExchangeEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getExchangeDate());
        if (entity.getDirection() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDirection());
        }
        statement.bindLong(4, entity.getBahtAmount());
        statement.bindLong(5, entity.getRate());
        statement.bindLong(6, entity.getVndAmount());
        statement.bindLong(7, entity.getTheoreticalVndAmount());
        statement.bindLong(8, entity.getCostRate());
        statement.bindLong(9, entity.getCostVndAmount());
        if (entity.getPartnerName() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPartnerName());
        }
        if (entity.getContactId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getContactId());
        }
        if (entity.getNote() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getNote());
        }
        statement.bindLong(13, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfFamilyExpenseEntity = new EntityInsertionAdapter<FamilyExpenseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `family_expenses` (`id`,`expense_date`,`category`,`amount`,`partner_name`,`contact_id`,`note`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FamilyExpenseEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getExpenseDate());
        if (entity.getCategory() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCategory());
        }
        statement.bindLong(4, entity.getAmount());
        if (entity.getPartnerName() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPartnerName());
        }
        if (entity.getContactId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getContactId());
        }
        if (entity.getNote() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNote());
        }
        statement.bindLong(8, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfQuotaFeeEntity = new EntityInsertionAdapter<QuotaFeeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `quota_fees` (`id`,`fee_date`,`payer_name`,`contact_id`,`cattle_quantity`,`amount`,`note`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final QuotaFeeEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFeeDate());
        if (entity.getPayerName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPayerName());
        }
        if (entity.getContactId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getContactId());
        }
        statement.bindLong(5, entity.getCattleQuantity());
        statement.bindLong(6, entity.getAmount());
        if (entity.getNote() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNote());
        }
        statement.bindLong(8, entity.getCreatedAt());
      }
    };
    this.__preparedStmtOfDeleteGoods = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM goods_purchases WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllGoods = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM goods_purchases";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteExchange = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM currency_exchanges WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllExchanges = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM currency_exchanges";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteExpense = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM family_expenses WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllExpenses = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM family_expenses";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteQuotaFee = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM quota_fees WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllQuotaFees = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM quota_fees";
        return _query;
      }
    };
  }

  @Override
  public Object upsertGoods(final GoodsPurchaseEntity entity,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfGoodsPurchaseEntity.insertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAllGoods(final List<GoodsPurchaseEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGoodsPurchaseEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertExchange(final CurrencyExchangeEntity entity,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCurrencyExchangeEntity.insertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAllExchanges(final List<CurrencyExchangeEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCurrencyExchangeEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertExpense(final FamilyExpenseEntity entity,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFamilyExpenseEntity.insertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAllExpenses(final List<FamilyExpenseEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFamilyExpenseEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertQuotaFee(final QuotaFeeEntity item,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfQuotaFeeEntity.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAllQuotaFees(final List<QuotaFeeEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfQuotaFeeEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object replaceAllLedgerDataInTransaction(final List<GoodsPurchaseEntity> goods,
      final List<CurrencyExchangeEntity> exchanges, final List<FamilyExpenseEntity> expenses,
      final List<QuotaFeeEntity> quotaFees, final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> LedgerDao.DefaultImpls.replaceAllLedgerDataInTransaction(LedgerDao_Impl.this, goods, exchanges, expenses, quotaFees, __cont), $completion);
  }

  @Override
  public Object deleteGoods(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteGoods.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteGoods.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllGoods(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllGoods.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAllGoods.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteExchange(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteExchange.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteExchange.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllExchanges(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllExchanges.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAllExchanges.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteExpense(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteExpense.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteExpense.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllExpenses(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllExpenses.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAllExpenses.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteQuotaFee(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteQuotaFee.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteQuotaFee.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllQuotaFees(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllQuotaFees.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAllQuotaFees.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<GoodsPurchaseEntity>> observeGoods() {
    final String _sql = "SELECT * FROM goods_purchases ORDER BY purchase_date DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"goods_purchases"}, new Callable<List<GoodsPurchaseEntity>>() {
      @Override
      @NonNull
      public List<GoodsPurchaseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchase_date");
          final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "item_name");
          final int _cursorIndexOfQuantityX100 = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity_x100");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "total_amount");
          final int _cursorIndexOfSupplier = CursorUtil.getColumnIndexOrThrow(_cursor, "supplier");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<GoodsPurchaseEntity> _result = new ArrayList<GoodsPurchaseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GoodsPurchaseEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final String _tmpItemName;
            if (_cursor.isNull(_cursorIndexOfItemName)) {
              _tmpItemName = null;
            } else {
              _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
            }
            final long _tmpQuantityX100;
            _tmpQuantityX100 = _cursor.getLong(_cursorIndexOfQuantityX100);
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final long _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getLong(_cursorIndexOfTotalAmount);
            final String _tmpSupplier;
            if (_cursor.isNull(_cursorIndexOfSupplier)) {
              _tmpSupplier = null;
            } else {
              _tmpSupplier = _cursor.getString(_cursorIndexOfSupplier);
            }
            final Long _tmpContactId;
            if (_cursor.isNull(_cursorIndexOfContactId)) {
              _tmpContactId = null;
            } else {
              _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new GoodsPurchaseEntity(_tmpId,_tmpPurchaseDate,_tmpItemName,_tmpQuantityX100,_tmpUnit,_tmpTotalAmount,_tmpSupplier,_tmpContactId,_tmpNote,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllGoodsSnapshot(
      final Continuation<? super List<GoodsPurchaseEntity>> $completion) {
    final String _sql = "SELECT * FROM goods_purchases ORDER BY purchase_date DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<GoodsPurchaseEntity>>() {
      @Override
      @NonNull
      public List<GoodsPurchaseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchase_date");
          final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "item_name");
          final int _cursorIndexOfQuantityX100 = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity_x100");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "total_amount");
          final int _cursorIndexOfSupplier = CursorUtil.getColumnIndexOrThrow(_cursor, "supplier");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<GoodsPurchaseEntity> _result = new ArrayList<GoodsPurchaseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GoodsPurchaseEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final String _tmpItemName;
            if (_cursor.isNull(_cursorIndexOfItemName)) {
              _tmpItemName = null;
            } else {
              _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
            }
            final long _tmpQuantityX100;
            _tmpQuantityX100 = _cursor.getLong(_cursorIndexOfQuantityX100);
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final long _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getLong(_cursorIndexOfTotalAmount);
            final String _tmpSupplier;
            if (_cursor.isNull(_cursorIndexOfSupplier)) {
              _tmpSupplier = null;
            } else {
              _tmpSupplier = _cursor.getString(_cursorIndexOfSupplier);
            }
            final Long _tmpContactId;
            if (_cursor.isNull(_cursorIndexOfContactId)) {
              _tmpContactId = null;
            } else {
              _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new GoodsPurchaseEntity(_tmpId,_tmpPurchaseDate,_tmpItemName,_tmpQuantityX100,_tmpUnit,_tmpTotalAmount,_tmpSupplier,_tmpContactId,_tmpNote,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CurrencyExchangeEntity>> observeExchanges() {
    final String _sql = "SELECT * FROM currency_exchanges ORDER BY exchange_date DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"currency_exchanges"}, new Callable<List<CurrencyExchangeEntity>>() {
      @Override
      @NonNull
      public List<CurrencyExchangeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfExchangeDate = CursorUtil.getColumnIndexOrThrow(_cursor, "exchange_date");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfBahtAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "baht_amount");
          final int _cursorIndexOfRate = CursorUtil.getColumnIndexOrThrow(_cursor, "rate");
          final int _cursorIndexOfVndAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "vnd_amount");
          final int _cursorIndexOfTheoreticalVndAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "theoretical_vnd_amount");
          final int _cursorIndexOfCostRate = CursorUtil.getColumnIndexOrThrow(_cursor, "cost_rate");
          final int _cursorIndexOfCostVndAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "cost_vnd_amount");
          final int _cursorIndexOfPartnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "partner_name");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<CurrencyExchangeEntity> _result = new ArrayList<CurrencyExchangeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CurrencyExchangeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpExchangeDate;
            _tmpExchangeDate = _cursor.getLong(_cursorIndexOfExchangeDate);
            final String _tmpDirection;
            if (_cursor.isNull(_cursorIndexOfDirection)) {
              _tmpDirection = null;
            } else {
              _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            }
            final long _tmpBahtAmount;
            _tmpBahtAmount = _cursor.getLong(_cursorIndexOfBahtAmount);
            final long _tmpRate;
            _tmpRate = _cursor.getLong(_cursorIndexOfRate);
            final long _tmpVndAmount;
            _tmpVndAmount = _cursor.getLong(_cursorIndexOfVndAmount);
            final long _tmpTheoreticalVndAmount;
            _tmpTheoreticalVndAmount = _cursor.getLong(_cursorIndexOfTheoreticalVndAmount);
            final long _tmpCostRate;
            _tmpCostRate = _cursor.getLong(_cursorIndexOfCostRate);
            final long _tmpCostVndAmount;
            _tmpCostVndAmount = _cursor.getLong(_cursorIndexOfCostVndAmount);
            final String _tmpPartnerName;
            if (_cursor.isNull(_cursorIndexOfPartnerName)) {
              _tmpPartnerName = null;
            } else {
              _tmpPartnerName = _cursor.getString(_cursorIndexOfPartnerName);
            }
            final Long _tmpContactId;
            if (_cursor.isNull(_cursorIndexOfContactId)) {
              _tmpContactId = null;
            } else {
              _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CurrencyExchangeEntity(_tmpId,_tmpExchangeDate,_tmpDirection,_tmpBahtAmount,_tmpRate,_tmpVndAmount,_tmpTheoreticalVndAmount,_tmpCostRate,_tmpCostVndAmount,_tmpPartnerName,_tmpContactId,_tmpNote,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllExchangesSnapshot(
      final Continuation<? super List<CurrencyExchangeEntity>> $completion) {
    final String _sql = "SELECT * FROM currency_exchanges ORDER BY exchange_date DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CurrencyExchangeEntity>>() {
      @Override
      @NonNull
      public List<CurrencyExchangeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfExchangeDate = CursorUtil.getColumnIndexOrThrow(_cursor, "exchange_date");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfBahtAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "baht_amount");
          final int _cursorIndexOfRate = CursorUtil.getColumnIndexOrThrow(_cursor, "rate");
          final int _cursorIndexOfVndAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "vnd_amount");
          final int _cursorIndexOfTheoreticalVndAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "theoretical_vnd_amount");
          final int _cursorIndexOfCostRate = CursorUtil.getColumnIndexOrThrow(_cursor, "cost_rate");
          final int _cursorIndexOfCostVndAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "cost_vnd_amount");
          final int _cursorIndexOfPartnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "partner_name");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<CurrencyExchangeEntity> _result = new ArrayList<CurrencyExchangeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CurrencyExchangeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpExchangeDate;
            _tmpExchangeDate = _cursor.getLong(_cursorIndexOfExchangeDate);
            final String _tmpDirection;
            if (_cursor.isNull(_cursorIndexOfDirection)) {
              _tmpDirection = null;
            } else {
              _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            }
            final long _tmpBahtAmount;
            _tmpBahtAmount = _cursor.getLong(_cursorIndexOfBahtAmount);
            final long _tmpRate;
            _tmpRate = _cursor.getLong(_cursorIndexOfRate);
            final long _tmpVndAmount;
            _tmpVndAmount = _cursor.getLong(_cursorIndexOfVndAmount);
            final long _tmpTheoreticalVndAmount;
            _tmpTheoreticalVndAmount = _cursor.getLong(_cursorIndexOfTheoreticalVndAmount);
            final long _tmpCostRate;
            _tmpCostRate = _cursor.getLong(_cursorIndexOfCostRate);
            final long _tmpCostVndAmount;
            _tmpCostVndAmount = _cursor.getLong(_cursorIndexOfCostVndAmount);
            final String _tmpPartnerName;
            if (_cursor.isNull(_cursorIndexOfPartnerName)) {
              _tmpPartnerName = null;
            } else {
              _tmpPartnerName = _cursor.getString(_cursorIndexOfPartnerName);
            }
            final Long _tmpContactId;
            if (_cursor.isNull(_cursorIndexOfContactId)) {
              _tmpContactId = null;
            } else {
              _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CurrencyExchangeEntity(_tmpId,_tmpExchangeDate,_tmpDirection,_tmpBahtAmount,_tmpRate,_tmpVndAmount,_tmpTheoreticalVndAmount,_tmpCostRate,_tmpCostVndAmount,_tmpPartnerName,_tmpContactId,_tmpNote,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FamilyExpenseEntity>> observeExpenses() {
    final String _sql = "SELECT * FROM family_expenses ORDER BY expense_date DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"family_expenses"}, new Callable<List<FamilyExpenseEntity>>() {
      @Override
      @NonNull
      public List<FamilyExpenseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfExpenseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expense_date");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfPartnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "partner_name");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<FamilyExpenseEntity> _result = new ArrayList<FamilyExpenseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FamilyExpenseEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpExpenseDate;
            _tmpExpenseDate = _cursor.getLong(_cursorIndexOfExpenseDate);
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final long _tmpAmount;
            _tmpAmount = _cursor.getLong(_cursorIndexOfAmount);
            final String _tmpPartnerName;
            if (_cursor.isNull(_cursorIndexOfPartnerName)) {
              _tmpPartnerName = null;
            } else {
              _tmpPartnerName = _cursor.getString(_cursorIndexOfPartnerName);
            }
            final Long _tmpContactId;
            if (_cursor.isNull(_cursorIndexOfContactId)) {
              _tmpContactId = null;
            } else {
              _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new FamilyExpenseEntity(_tmpId,_tmpExpenseDate,_tmpCategory,_tmpAmount,_tmpPartnerName,_tmpContactId,_tmpNote,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllExpensesSnapshot(
      final Continuation<? super List<FamilyExpenseEntity>> $completion) {
    final String _sql = "SELECT * FROM family_expenses ORDER BY expense_date DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<FamilyExpenseEntity>>() {
      @Override
      @NonNull
      public List<FamilyExpenseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfExpenseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expense_date");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfPartnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "partner_name");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<FamilyExpenseEntity> _result = new ArrayList<FamilyExpenseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FamilyExpenseEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpExpenseDate;
            _tmpExpenseDate = _cursor.getLong(_cursorIndexOfExpenseDate);
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final long _tmpAmount;
            _tmpAmount = _cursor.getLong(_cursorIndexOfAmount);
            final String _tmpPartnerName;
            if (_cursor.isNull(_cursorIndexOfPartnerName)) {
              _tmpPartnerName = null;
            } else {
              _tmpPartnerName = _cursor.getString(_cursorIndexOfPartnerName);
            }
            final Long _tmpContactId;
            if (_cursor.isNull(_cursorIndexOfContactId)) {
              _tmpContactId = null;
            } else {
              _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new FamilyExpenseEntity(_tmpId,_tmpExpenseDate,_tmpCategory,_tmpAmount,_tmpPartnerName,_tmpContactId,_tmpNote,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<QuotaFeeEntity>> observeQuotaFees() {
    final String _sql = "SELECT * FROM quota_fees ORDER BY fee_date DESC, created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"quota_fees"}, new Callable<List<QuotaFeeEntity>>() {
      @Override
      @NonNull
      public List<QuotaFeeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFeeDate = CursorUtil.getColumnIndexOrThrow(_cursor, "fee_date");
          final int _cursorIndexOfPayerName = CursorUtil.getColumnIndexOrThrow(_cursor, "payer_name");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfCattleQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "cattle_quantity");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<QuotaFeeEntity> _result = new ArrayList<QuotaFeeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QuotaFeeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFeeDate;
            _tmpFeeDate = _cursor.getLong(_cursorIndexOfFeeDate);
            final String _tmpPayerName;
            if (_cursor.isNull(_cursorIndexOfPayerName)) {
              _tmpPayerName = null;
            } else {
              _tmpPayerName = _cursor.getString(_cursorIndexOfPayerName);
            }
            final Long _tmpContactId;
            if (_cursor.isNull(_cursorIndexOfContactId)) {
              _tmpContactId = null;
            } else {
              _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            }
            final int _tmpCattleQuantity;
            _tmpCattleQuantity = _cursor.getInt(_cursorIndexOfCattleQuantity);
            final long _tmpAmount;
            _tmpAmount = _cursor.getLong(_cursorIndexOfAmount);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new QuotaFeeEntity(_tmpId,_tmpFeeDate,_tmpPayerName,_tmpContactId,_tmpCattleQuantity,_tmpAmount,_tmpNote,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllQuotaFeesOnce(final Continuation<? super List<QuotaFeeEntity>> $completion) {
    final String _sql = "SELECT * FROM quota_fees ORDER BY fee_date DESC, created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<QuotaFeeEntity>>() {
      @Override
      @NonNull
      public List<QuotaFeeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFeeDate = CursorUtil.getColumnIndexOrThrow(_cursor, "fee_date");
          final int _cursorIndexOfPayerName = CursorUtil.getColumnIndexOrThrow(_cursor, "payer_name");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfCattleQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "cattle_quantity");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<QuotaFeeEntity> _result = new ArrayList<QuotaFeeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QuotaFeeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFeeDate;
            _tmpFeeDate = _cursor.getLong(_cursorIndexOfFeeDate);
            final String _tmpPayerName;
            if (_cursor.isNull(_cursorIndexOfPayerName)) {
              _tmpPayerName = null;
            } else {
              _tmpPayerName = _cursor.getString(_cursorIndexOfPayerName);
            }
            final Long _tmpContactId;
            if (_cursor.isNull(_cursorIndexOfContactId)) {
              _tmpContactId = null;
            } else {
              _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            }
            final int _tmpCattleQuantity;
            _tmpCattleQuantity = _cursor.getInt(_cursorIndexOfCattleQuantity);
            final long _tmpAmount;
            _tmpAmount = _cursor.getLong(_cursorIndexOfAmount);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new QuotaFeeEntity(_tmpId,_tmpFeeDate,_tmpPayerName,_tmpContactId,_tmpCattleQuantity,_tmpAmount,_tmpNote,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
