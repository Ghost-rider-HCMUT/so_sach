package com.sotraubo.app.core.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.sotraubo.app.core.database.entity.AttachmentEntity;
import com.sotraubo.app.core.database.entity.CashTransactionEntity;
import com.sotraubo.app.core.database.entity.ContactEntity;
import com.sotraubo.app.core.database.entity.TradeOrderEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class CattleBusinessDao_Impl implements CattleBusinessDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ContactEntity> __insertionAdapterOfContactEntity;

  private final EntityInsertionAdapter<TradeOrderEntity> __insertionAdapterOfTradeOrderEntity;

  private final EntityInsertionAdapter<CashTransactionEntity> __insertionAdapterOfCashTransactionEntity;

  private final EntityInsertionAdapter<AttachmentEntity> __insertionAdapterOfAttachmentEntity;

  private final EntityDeletionOrUpdateAdapter<ContactEntity> __updateAdapterOfContactEntity;

  private final EntityDeletionOrUpdateAdapter<TradeOrderEntity> __updateAdapterOfTradeOrderEntity;

  private final EntityDeletionOrUpdateAdapter<CashTransactionEntity> __updateAdapterOfCashTransactionEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteContactById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTradeOrderById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCashTransactionById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAttachmentsForOrder;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAttachmentsForCashTransaction;

  private final SharedSQLiteStatement __preparedStmtOfClearAllAttachments;

  private final SharedSQLiteStatement __preparedStmtOfClearAllCashTransactions;

  private final SharedSQLiteStatement __preparedStmtOfClearAllTradeOrders;

  private final SharedSQLiteStatement __preparedStmtOfClearAllContacts;

  public CattleBusinessDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfContactEntity = new EntityInsertionAdapter<ContactEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `contacts` (`id`,`full_name`,`name_normalized`,`phone`,`phone_normalized`,`address`,`note`,`created_at`,`updated_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ContactEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getFullName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getFullName());
        }
        if (entity.getNameNormalized() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNameNormalized());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPhone());
        }
        if (entity.getPhoneNormalized() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPhoneNormalized());
        }
        if (entity.getAddress() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getAddress());
        }
        if (entity.getNote() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNote());
        }
        statement.bindLong(8, entity.getCreatedAt());
        statement.bindLong(9, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfTradeOrderEntity = new EntityInsertionAdapter<TradeOrderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `trade_orders` (`id`,`order_type`,`contact_id`,`transaction_date`,`cattle_quantity`,`description`,`total_amount`,`note`,`created_at`,`updated_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TradeOrderEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getOrderType() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getOrderType());
        }
        statement.bindLong(3, entity.getContactId());
        statement.bindLong(4, entity.getTransactionDate());
        statement.bindLong(5, entity.getCattleQuantity());
        if (entity.getDescription() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDescription());
        }
        statement.bindLong(7, entity.getTotalAmount());
        if (entity.getNote() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNote());
        }
        statement.bindLong(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfCashTransactionEntity = new EntityInsertionAdapter<CashTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `cash_transactions` (`id`,`flow_type`,`contact_id`,`trade_order_id`,`is_initial_payment`,`amount`,`transaction_date`,`payment_method`,`short_content`,`note`,`created_at`,`updated_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CashTransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getFlowType() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getFlowType());
        }
        statement.bindLong(3, entity.getContactId());
        if (entity.getTradeOrderId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getTradeOrderId());
        }
        final int _tmp = entity.isInitialPayment() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getAmount());
        statement.bindLong(7, entity.getTransactionDate());
        if (entity.getPaymentMethod() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPaymentMethod());
        }
        if (entity.getShortContent() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getShortContent());
        }
        if (entity.getNote() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getNote());
        }
        statement.bindLong(11, entity.getCreatedAt());
        statement.bindLong(12, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfAttachmentEntity = new EntityInsertionAdapter<AttachmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `attachments` (`id`,`trade_order_id`,`cash_transaction_id`,`local_file_path`,`created_at`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AttachmentEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTradeOrderId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getTradeOrderId());
        }
        if (entity.getCashTransactionId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getCashTransactionId());
        }
        if (entity.getLocalFilePath() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLocalFilePath());
        }
        statement.bindLong(5, entity.getCreatedAt());
      }
    };
    this.__updateAdapterOfContactEntity = new EntityDeletionOrUpdateAdapter<ContactEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `contacts` SET `id` = ?,`full_name` = ?,`name_normalized` = ?,`phone` = ?,`phone_normalized` = ?,`address` = ?,`note` = ?,`created_at` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ContactEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getFullName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getFullName());
        }
        if (entity.getNameNormalized() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNameNormalized());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPhone());
        }
        if (entity.getPhoneNormalized() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPhoneNormalized());
        }
        if (entity.getAddress() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getAddress());
        }
        if (entity.getNote() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNote());
        }
        statement.bindLong(8, entity.getCreatedAt());
        statement.bindLong(9, entity.getUpdatedAt());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__updateAdapterOfTradeOrderEntity = new EntityDeletionOrUpdateAdapter<TradeOrderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `trade_orders` SET `id` = ?,`order_type` = ?,`contact_id` = ?,`transaction_date` = ?,`cattle_quantity` = ?,`description` = ?,`total_amount` = ?,`note` = ?,`created_at` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TradeOrderEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getOrderType() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getOrderType());
        }
        statement.bindLong(3, entity.getContactId());
        statement.bindLong(4, entity.getTransactionDate());
        statement.bindLong(5, entity.getCattleQuantity());
        if (entity.getDescription() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDescription());
        }
        statement.bindLong(7, entity.getTotalAmount());
        if (entity.getNote() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNote());
        }
        statement.bindLong(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getUpdatedAt());
        statement.bindLong(11, entity.getId());
      }
    };
    this.__updateAdapterOfCashTransactionEntity = new EntityDeletionOrUpdateAdapter<CashTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `cash_transactions` SET `id` = ?,`flow_type` = ?,`contact_id` = ?,`trade_order_id` = ?,`is_initial_payment` = ?,`amount` = ?,`transaction_date` = ?,`payment_method` = ?,`short_content` = ?,`note` = ?,`created_at` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CashTransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getFlowType() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getFlowType());
        }
        statement.bindLong(3, entity.getContactId());
        if (entity.getTradeOrderId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getTradeOrderId());
        }
        final int _tmp = entity.isInitialPayment() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getAmount());
        statement.bindLong(7, entity.getTransactionDate());
        if (entity.getPaymentMethod() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPaymentMethod());
        }
        if (entity.getShortContent() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getShortContent());
        }
        if (entity.getNote() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getNote());
        }
        statement.bindLong(11, entity.getCreatedAt());
        statement.bindLong(12, entity.getUpdatedAt());
        statement.bindLong(13, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteContactById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM contacts WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteTradeOrderById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM trade_orders WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCashTransactionById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cash_transactions WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAttachmentsForOrder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM attachments WHERE trade_order_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAttachmentsForCashTransaction = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM attachments WHERE cash_transaction_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllAttachments = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM attachments";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllCashTransactions = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cash_transactions";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllTradeOrders = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM trade_orders";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllContacts = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM contacts";
        return _query;
      }
    };
  }

  @Override
  public Object insertContact(final ContactEntity contact, final Continuation<? super Long> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfContactEntity.insertAndReturnId(contact);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertTradeOrder(final TradeOrderEntity order,
      final Continuation<? super Long> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTradeOrderEntity.insertAndReturnId(order);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertCashTransaction(final CashTransactionEntity transaction,
      final Continuation<? super Long> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCashTransactionEntity.insertAndReturnId(transaction);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertAttachments(final List<AttachmentEntity> attachments,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAttachmentEntity.insert(attachments);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object updateContact(final ContactEntity contact, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfContactEntity.handle(contact);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object updateTradeOrder(final TradeOrderEntity order,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTradeOrderEntity.handle(order);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object updateCashTransaction(final CashTransactionEntity transaction,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCashTransactionEntity.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object replaceAllDataInTransaction(final List<ContactEntity> contacts,
      final List<TradeOrderEntity> orders, final List<CashTransactionEntity> cashTransactions,
      final List<AttachmentEntity> attachments, final Continuation<? super Unit> arg4) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> CattleBusinessDao.DefaultImpls.replaceAllDataInTransaction(CattleBusinessDao_Impl.this, contacts, orders, cashTransactions, attachments, __cont), arg4);
  }

  @Override
  public Object deleteContactById(final long contactId, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteContactById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, contactId);
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
          __preparedStmtOfDeleteContactById.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteTradeOrderById(final long orderId, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTradeOrderById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, orderId);
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
          __preparedStmtOfDeleteTradeOrderById.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteCashTransactionById(final long transactionId,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCashTransactionById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, transactionId);
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
          __preparedStmtOfDeleteCashTransactionById.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteAttachmentsForOrder(final long orderId,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAttachmentsForOrder.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, orderId);
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
          __preparedStmtOfDeleteAttachmentsForOrder.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteAttachmentsForCashTransaction(final long transactionId,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAttachmentsForCashTransaction.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, transactionId);
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
          __preparedStmtOfDeleteAttachmentsForCashTransaction.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Object clearAllAttachments(final Continuation<? super Unit> arg0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllAttachments.acquire();
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
          __preparedStmtOfClearAllAttachments.release(_stmt);
        }
      }
    }, arg0);
  }

  @Override
  public Object clearAllCashTransactions(final Continuation<? super Unit> arg0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllCashTransactions.acquire();
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
          __preparedStmtOfClearAllCashTransactions.release(_stmt);
        }
      }
    }, arg0);
  }

  @Override
  public Object clearAllTradeOrders(final Continuation<? super Unit> arg0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllTradeOrders.acquire();
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
          __preparedStmtOfClearAllTradeOrders.release(_stmt);
        }
      }
    }, arg0);
  }

  @Override
  public Object clearAllContacts(final Continuation<? super Unit> arg0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllContacts.acquire();
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
          __preparedStmtOfClearAllContacts.release(_stmt);
        }
      }
    }, arg0);
  }

  @Override
  public Flow<List<ContactEntity>> observeAllContacts() {
    final String _sql = "SELECT * FROM contacts ORDER BY full_name COLLATE LOCALIZED ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"contacts"}, new Callable<List<ContactEntity>>() {
      @Override
      @NonNull
      public List<ContactEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "full_name");
          final int _cursorIndexOfNameNormalized = CursorUtil.getColumnIndexOrThrow(_cursor, "name_normalized");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfPhoneNormalized = CursorUtil.getColumnIndexOrThrow(_cursor, "phone_normalized");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<ContactEntity> _result = new ArrayList<ContactEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ContactEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFullName;
            if (_cursor.isNull(_cursorIndexOfFullName)) {
              _tmpFullName = null;
            } else {
              _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
            }
            final String _tmpNameNormalized;
            if (_cursor.isNull(_cursorIndexOfNameNormalized)) {
              _tmpNameNormalized = null;
            } else {
              _tmpNameNormalized = _cursor.getString(_cursorIndexOfNameNormalized);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpPhoneNormalized;
            if (_cursor.isNull(_cursorIndexOfPhoneNormalized)) {
              _tmpPhoneNormalized = null;
            } else {
              _tmpPhoneNormalized = _cursor.getString(_cursorIndexOfPhoneNormalized);
            }
            final String _tmpAddress;
            if (_cursor.isNull(_cursorIndexOfAddress)) {
              _tmpAddress = null;
            } else {
              _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ContactEntity(_tmpId,_tmpFullName,_tmpNameNormalized,_tmpPhone,_tmpPhoneNormalized,_tmpAddress,_tmpNote,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<ContactEntity> observeContactById(final long contactId) {
    final String _sql = "SELECT * FROM contacts WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, contactId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"contacts"}, new Callable<ContactEntity>() {
      @Override
      @Nullable
      public ContactEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "full_name");
          final int _cursorIndexOfNameNormalized = CursorUtil.getColumnIndexOrThrow(_cursor, "name_normalized");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfPhoneNormalized = CursorUtil.getColumnIndexOrThrow(_cursor, "phone_normalized");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final ContactEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFullName;
            if (_cursor.isNull(_cursorIndexOfFullName)) {
              _tmpFullName = null;
            } else {
              _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
            }
            final String _tmpNameNormalized;
            if (_cursor.isNull(_cursorIndexOfNameNormalized)) {
              _tmpNameNormalized = null;
            } else {
              _tmpNameNormalized = _cursor.getString(_cursorIndexOfNameNormalized);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpPhoneNormalized;
            if (_cursor.isNull(_cursorIndexOfPhoneNormalized)) {
              _tmpPhoneNormalized = null;
            } else {
              _tmpPhoneNormalized = _cursor.getString(_cursorIndexOfPhoneNormalized);
            }
            final String _tmpAddress;
            if (_cursor.isNull(_cursorIndexOfAddress)) {
              _tmpAddress = null;
            } else {
              _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new ContactEntity(_tmpId,_tmpFullName,_tmpNameNormalized,_tmpPhone,_tmpPhoneNormalized,_tmpAddress,_tmpNote,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
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
  public Object getContactById(final long contactId,
      final Continuation<? super ContactEntity> arg1) {
    final String _sql = "SELECT * FROM contacts WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, contactId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ContactEntity>() {
      @Override
      @Nullable
      public ContactEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "full_name");
          final int _cursorIndexOfNameNormalized = CursorUtil.getColumnIndexOrThrow(_cursor, "name_normalized");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfPhoneNormalized = CursorUtil.getColumnIndexOrThrow(_cursor, "phone_normalized");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final ContactEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFullName;
            if (_cursor.isNull(_cursorIndexOfFullName)) {
              _tmpFullName = null;
            } else {
              _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
            }
            final String _tmpNameNormalized;
            if (_cursor.isNull(_cursorIndexOfNameNormalized)) {
              _tmpNameNormalized = null;
            } else {
              _tmpNameNormalized = _cursor.getString(_cursorIndexOfNameNormalized);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpPhoneNormalized;
            if (_cursor.isNull(_cursorIndexOfPhoneNormalized)) {
              _tmpPhoneNormalized = null;
            } else {
              _tmpPhoneNormalized = _cursor.getString(_cursorIndexOfPhoneNormalized);
            }
            final String _tmpAddress;
            if (_cursor.isNull(_cursorIndexOfAddress)) {
              _tmpAddress = null;
            } else {
              _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new ContactEntity(_tmpId,_tmpFullName,_tmpNameNormalized,_tmpPhone,_tmpPhoneNormalized,_tmpAddress,_tmpNote,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Object getAllContactsSnapshot(final Continuation<? super List<ContactEntity>> arg0) {
    final String _sql = "SELECT * FROM contacts ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ContactEntity>>() {
      @Override
      @NonNull
      public List<ContactEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "full_name");
          final int _cursorIndexOfNameNormalized = CursorUtil.getColumnIndexOrThrow(_cursor, "name_normalized");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfPhoneNormalized = CursorUtil.getColumnIndexOrThrow(_cursor, "phone_normalized");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<ContactEntity> _result = new ArrayList<ContactEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ContactEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFullName;
            if (_cursor.isNull(_cursorIndexOfFullName)) {
              _tmpFullName = null;
            } else {
              _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
            }
            final String _tmpNameNormalized;
            if (_cursor.isNull(_cursorIndexOfNameNormalized)) {
              _tmpNameNormalized = null;
            } else {
              _tmpNameNormalized = _cursor.getString(_cursorIndexOfNameNormalized);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpPhoneNormalized;
            if (_cursor.isNull(_cursorIndexOfPhoneNormalized)) {
              _tmpPhoneNormalized = null;
            } else {
              _tmpPhoneNormalized = _cursor.getString(_cursorIndexOfPhoneNormalized);
            }
            final String _tmpAddress;
            if (_cursor.isNull(_cursorIndexOfAddress)) {
              _tmpAddress = null;
            } else {
              _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ContactEntity(_tmpId,_tmpFullName,_tmpNameNormalized,_tmpPhone,_tmpPhoneNormalized,_tmpAddress,_tmpNote,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Object countTransactionsForContact(final long contactId,
      final Continuation<? super Integer> arg1) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM (\n"
            + "            SELECT id FROM trade_orders WHERE contact_id = ?\n"
            + "            UNION ALL\n"
            + "            SELECT id FROM cash_transactions WHERE contact_id = ?\n"
            + "        )\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, contactId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, contactId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Flow<List<TradeOrderQueryProjection>> observeAllTradeOrdersWithPayment() {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            o.id AS id,\n"
            + "            o.order_type AS order_type,\n"
            + "            o.contact_id AS contact_id,\n"
            + "            c.full_name AS contact_name,\n"
            + "            c.phone AS contact_phone,\n"
            + "            o.transaction_date AS transaction_date,\n"
            + "            o.cattle_quantity AS cattle_quantity,\n"
            + "            o.description AS description,\n"
            + "            o.total_amount AS total_amount,\n"
            + "            COALESCE(SUM(ct.amount), 0) AS paid_amount,\n"
            + "            (o.total_amount - COALESCE(SUM(ct.amount), 0)) AS remaining_debt,\n"
            + "            o.note AS note,\n"
            + "            o.created_at AS created_at\n"
            + "        FROM trade_orders o\n"
            + "        INNER JOIN contacts c ON c.id = o.contact_id\n"
            + "        LEFT JOIN cash_transactions ct ON ct.trade_order_id = o.id\n"
            + "        GROUP BY o.id\n"
            + "        ORDER BY o.transaction_date DESC, o.id DESC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"trade_orders", "contacts",
        "cash_transactions"}, new Callable<List<TradeOrderQueryProjection>>() {
      @Override
      @NonNull
      public List<TradeOrderQueryProjection> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfOrderType = 1;
          final int _cursorIndexOfContactId = 2;
          final int _cursorIndexOfContactName = 3;
          final int _cursorIndexOfContactPhone = 4;
          final int _cursorIndexOfTransactionDate = 5;
          final int _cursorIndexOfCattleQuantity = 6;
          final int _cursorIndexOfDescription = 7;
          final int _cursorIndexOfTotalAmount = 8;
          final int _cursorIndexOfPaidAmount = 9;
          final int _cursorIndexOfRemainingDebt = 10;
          final int _cursorIndexOfNote = 11;
          final int _cursorIndexOfCreatedAt = 12;
          final List<TradeOrderQueryProjection> _result = new ArrayList<TradeOrderQueryProjection>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TradeOrderQueryProjection _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpOrderType;
            if (_cursor.isNull(_cursorIndexOfOrderType)) {
              _tmpOrderType = null;
            } else {
              _tmpOrderType = _cursor.getString(_cursorIndexOfOrderType);
            }
            final long _tmpContactId;
            _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            final String _tmpContactName;
            if (_cursor.isNull(_cursorIndexOfContactName)) {
              _tmpContactName = null;
            } else {
              _tmpContactName = _cursor.getString(_cursorIndexOfContactName);
            }
            final String _tmpContactPhone;
            if (_cursor.isNull(_cursorIndexOfContactPhone)) {
              _tmpContactPhone = null;
            } else {
              _tmpContactPhone = _cursor.getString(_cursorIndexOfContactPhone);
            }
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final int _tmpCattleQuantity;
            _tmpCattleQuantity = _cursor.getInt(_cursorIndexOfCattleQuantity);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getLong(_cursorIndexOfTotalAmount);
            final long _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getLong(_cursorIndexOfPaidAmount);
            final long _tmpRemainingDebt;
            _tmpRemainingDebt = _cursor.getLong(_cursorIndexOfRemainingDebt);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new TradeOrderQueryProjection(_tmpId,_tmpOrderType,_tmpContactId,_tmpContactName,_tmpContactPhone,_tmpTransactionDate,_tmpCattleQuantity,_tmpDescription,_tmpTotalAmount,_tmpPaidAmount,_tmpRemainingDebt,_tmpNote,_tmpCreatedAt);
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
  public Object getTradeOrderWithPaymentById(final long orderId,
      final Continuation<? super TradeOrderQueryProjection> arg1) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            o.id AS id,\n"
            + "            o.order_type AS order_type,\n"
            + "            o.contact_id AS contact_id,\n"
            + "            c.full_name AS contact_name,\n"
            + "            c.phone AS contact_phone,\n"
            + "            o.transaction_date AS transaction_date,\n"
            + "            o.cattle_quantity AS cattle_quantity,\n"
            + "            o.description AS description,\n"
            + "            o.total_amount AS total_amount,\n"
            + "            COALESCE(SUM(ct.amount), 0) AS paid_amount,\n"
            + "            (o.total_amount - COALESCE(SUM(ct.amount), 0)) AS remaining_debt,\n"
            + "            o.note AS note,\n"
            + "            o.created_at AS created_at\n"
            + "        FROM trade_orders o\n"
            + "        INNER JOIN contacts c ON c.id = o.contact_id\n"
            + "        LEFT JOIN cash_transactions ct ON ct.trade_order_id = o.id\n"
            + "        WHERE o.id = ?\n"
            + "        GROUP BY o.id\n"
            + "        LIMIT 1\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, orderId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TradeOrderQueryProjection>() {
      @Override
      @Nullable
      public TradeOrderQueryProjection call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfOrderType = 1;
          final int _cursorIndexOfContactId = 2;
          final int _cursorIndexOfContactName = 3;
          final int _cursorIndexOfContactPhone = 4;
          final int _cursorIndexOfTransactionDate = 5;
          final int _cursorIndexOfCattleQuantity = 6;
          final int _cursorIndexOfDescription = 7;
          final int _cursorIndexOfTotalAmount = 8;
          final int _cursorIndexOfPaidAmount = 9;
          final int _cursorIndexOfRemainingDebt = 10;
          final int _cursorIndexOfNote = 11;
          final int _cursorIndexOfCreatedAt = 12;
          final TradeOrderQueryProjection _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpOrderType;
            if (_cursor.isNull(_cursorIndexOfOrderType)) {
              _tmpOrderType = null;
            } else {
              _tmpOrderType = _cursor.getString(_cursorIndexOfOrderType);
            }
            final long _tmpContactId;
            _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            final String _tmpContactName;
            if (_cursor.isNull(_cursorIndexOfContactName)) {
              _tmpContactName = null;
            } else {
              _tmpContactName = _cursor.getString(_cursorIndexOfContactName);
            }
            final String _tmpContactPhone;
            if (_cursor.isNull(_cursorIndexOfContactPhone)) {
              _tmpContactPhone = null;
            } else {
              _tmpContactPhone = _cursor.getString(_cursorIndexOfContactPhone);
            }
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final int _tmpCattleQuantity;
            _tmpCattleQuantity = _cursor.getInt(_cursorIndexOfCattleQuantity);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getLong(_cursorIndexOfTotalAmount);
            final long _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getLong(_cursorIndexOfPaidAmount);
            final long _tmpRemainingDebt;
            _tmpRemainingDebt = _cursor.getLong(_cursorIndexOfRemainingDebt);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new TradeOrderQueryProjection(_tmpId,_tmpOrderType,_tmpContactId,_tmpContactName,_tmpContactPhone,_tmpTransactionDate,_tmpCattleQuantity,_tmpDescription,_tmpTotalAmount,_tmpPaidAmount,_tmpRemainingDebt,_tmpNote,_tmpCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Object getRawTradeOrderById(final long orderId,
      final Continuation<? super TradeOrderEntity> arg1) {
    final String _sql = "SELECT * FROM trade_orders WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, orderId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TradeOrderEntity>() {
      @Override
      @Nullable
      public TradeOrderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOrderType = CursorUtil.getColumnIndexOrThrow(_cursor, "order_type");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transaction_date");
          final int _cursorIndexOfCattleQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "cattle_quantity");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "total_amount");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final TradeOrderEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpOrderType;
            if (_cursor.isNull(_cursorIndexOfOrderType)) {
              _tmpOrderType = null;
            } else {
              _tmpOrderType = _cursor.getString(_cursorIndexOfOrderType);
            }
            final long _tmpContactId;
            _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final int _tmpCattleQuantity;
            _tmpCattleQuantity = _cursor.getInt(_cursorIndexOfCattleQuantity);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getLong(_cursorIndexOfTotalAmount);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new TradeOrderEntity(_tmpId,_tmpOrderType,_tmpContactId,_tmpTransactionDate,_tmpCattleQuantity,_tmpDescription,_tmpTotalAmount,_tmpNote,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Object getAllTradeOrdersSnapshot(final Continuation<? super List<TradeOrderEntity>> arg0) {
    final String _sql = "SELECT * FROM trade_orders ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TradeOrderEntity>>() {
      @Override
      @NonNull
      public List<TradeOrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOrderType = CursorUtil.getColumnIndexOrThrow(_cursor, "order_type");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transaction_date");
          final int _cursorIndexOfCattleQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "cattle_quantity");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "total_amount");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<TradeOrderEntity> _result = new ArrayList<TradeOrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TradeOrderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpOrderType;
            if (_cursor.isNull(_cursorIndexOfOrderType)) {
              _tmpOrderType = null;
            } else {
              _tmpOrderType = _cursor.getString(_cursorIndexOfOrderType);
            }
            final long _tmpContactId;
            _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final int _tmpCattleQuantity;
            _tmpCattleQuantity = _cursor.getInt(_cursorIndexOfCattleQuantity);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getLong(_cursorIndexOfTotalAmount);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new TradeOrderEntity(_tmpId,_tmpOrderType,_tmpContactId,_tmpTransactionDate,_tmpCattleQuantity,_tmpDescription,_tmpTotalAmount,_tmpNote,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Flow<List<CashTransactionQueryProjection>> observeAllCashTransactions() {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            ct.id AS id,\n"
            + "            ct.flow_type AS flow_type,\n"
            + "            ct.contact_id AS contact_id,\n"
            + "            c.full_name AS contact_name,\n"
            + "            c.phone AS contact_phone,\n"
            + "            ct.trade_order_id AS trade_order_id,\n"
            + "            o.description AS trade_order_description,\n"
            + "            ct.is_initial_payment AS is_initial_payment,\n"
            + "            ct.amount AS amount,\n"
            + "            ct.transaction_date AS transaction_date,\n"
            + "            ct.payment_method AS payment_method,\n"
            + "            ct.short_content AS short_content,\n"
            + "            ct.note AS note,\n"
            + "            ct.created_at AS created_at\n"
            + "        FROM cash_transactions ct\n"
            + "        INNER JOIN contacts c ON c.id = ct.contact_id\n"
            + "        LEFT JOIN trade_orders o ON o.id = ct.trade_order_id\n"
            + "        ORDER BY ct.transaction_date DESC, ct.id DESC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cash_transactions", "contacts",
        "trade_orders"}, new Callable<List<CashTransactionQueryProjection>>() {
      @Override
      @NonNull
      public List<CashTransactionQueryProjection> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfFlowType = 1;
          final int _cursorIndexOfContactId = 2;
          final int _cursorIndexOfContactName = 3;
          final int _cursorIndexOfContactPhone = 4;
          final int _cursorIndexOfTradeOrderId = 5;
          final int _cursorIndexOfTradeOrderDescription = 6;
          final int _cursorIndexOfIsInitialPayment = 7;
          final int _cursorIndexOfAmount = 8;
          final int _cursorIndexOfTransactionDate = 9;
          final int _cursorIndexOfPaymentMethod = 10;
          final int _cursorIndexOfShortContent = 11;
          final int _cursorIndexOfNote = 12;
          final int _cursorIndexOfCreatedAt = 13;
          final List<CashTransactionQueryProjection> _result = new ArrayList<CashTransactionQueryProjection>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CashTransactionQueryProjection _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFlowType;
            if (_cursor.isNull(_cursorIndexOfFlowType)) {
              _tmpFlowType = null;
            } else {
              _tmpFlowType = _cursor.getString(_cursorIndexOfFlowType);
            }
            final long _tmpContactId;
            _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            final String _tmpContactName;
            if (_cursor.isNull(_cursorIndexOfContactName)) {
              _tmpContactName = null;
            } else {
              _tmpContactName = _cursor.getString(_cursorIndexOfContactName);
            }
            final String _tmpContactPhone;
            if (_cursor.isNull(_cursorIndexOfContactPhone)) {
              _tmpContactPhone = null;
            } else {
              _tmpContactPhone = _cursor.getString(_cursorIndexOfContactPhone);
            }
            final Long _tmpTradeOrderId;
            if (_cursor.isNull(_cursorIndexOfTradeOrderId)) {
              _tmpTradeOrderId = null;
            } else {
              _tmpTradeOrderId = _cursor.getLong(_cursorIndexOfTradeOrderId);
            }
            final String _tmpTradeOrderDescription;
            if (_cursor.isNull(_cursorIndexOfTradeOrderDescription)) {
              _tmpTradeOrderDescription = null;
            } else {
              _tmpTradeOrderDescription = _cursor.getString(_cursorIndexOfTradeOrderDescription);
            }
            final boolean _tmpIsInitialPayment;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsInitialPayment);
            _tmpIsInitialPayment = _tmp != 0;
            final long _tmpAmount;
            _tmpAmount = _cursor.getLong(_cursorIndexOfAmount);
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final String _tmpShortContent;
            if (_cursor.isNull(_cursorIndexOfShortContent)) {
              _tmpShortContent = null;
            } else {
              _tmpShortContent = _cursor.getString(_cursorIndexOfShortContent);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CashTransactionQueryProjection(_tmpId,_tmpFlowType,_tmpContactId,_tmpContactName,_tmpContactPhone,_tmpTradeOrderId,_tmpTradeOrderDescription,_tmpIsInitialPayment,_tmpAmount,_tmpTransactionDate,_tmpPaymentMethod,_tmpShortContent,_tmpNote,_tmpCreatedAt);
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
  public Object getRawCashTransactionById(final long transactionId,
      final Continuation<? super CashTransactionEntity> arg1) {
    final String _sql = "SELECT * FROM cash_transactions WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, transactionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CashTransactionEntity>() {
      @Override
      @Nullable
      public CashTransactionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFlowType = CursorUtil.getColumnIndexOrThrow(_cursor, "flow_type");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfTradeOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "trade_order_id");
          final int _cursorIndexOfIsInitialPayment = CursorUtil.getColumnIndexOrThrow(_cursor, "is_initial_payment");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transaction_date");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "payment_method");
          final int _cursorIndexOfShortContent = CursorUtil.getColumnIndexOrThrow(_cursor, "short_content");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final CashTransactionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFlowType;
            if (_cursor.isNull(_cursorIndexOfFlowType)) {
              _tmpFlowType = null;
            } else {
              _tmpFlowType = _cursor.getString(_cursorIndexOfFlowType);
            }
            final long _tmpContactId;
            _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            final Long _tmpTradeOrderId;
            if (_cursor.isNull(_cursorIndexOfTradeOrderId)) {
              _tmpTradeOrderId = null;
            } else {
              _tmpTradeOrderId = _cursor.getLong(_cursorIndexOfTradeOrderId);
            }
            final boolean _tmpIsInitialPayment;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsInitialPayment);
            _tmpIsInitialPayment = _tmp != 0;
            final long _tmpAmount;
            _tmpAmount = _cursor.getLong(_cursorIndexOfAmount);
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final String _tmpShortContent;
            if (_cursor.isNull(_cursorIndexOfShortContent)) {
              _tmpShortContent = null;
            } else {
              _tmpShortContent = _cursor.getString(_cursorIndexOfShortContent);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new CashTransactionEntity(_tmpId,_tmpFlowType,_tmpContactId,_tmpTradeOrderId,_tmpIsInitialPayment,_tmpAmount,_tmpTransactionDate,_tmpPaymentMethod,_tmpShortContent,_tmpNote,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Object getInitialPaymentForOrder(final long orderId,
      final Continuation<? super CashTransactionEntity> arg1) {
    final String _sql = "\n"
            + "        SELECT * FROM cash_transactions \n"
            + "        WHERE trade_order_id = ? AND is_initial_payment = 1 \n"
            + "        LIMIT 1\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, orderId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CashTransactionEntity>() {
      @Override
      @Nullable
      public CashTransactionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFlowType = CursorUtil.getColumnIndexOrThrow(_cursor, "flow_type");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfTradeOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "trade_order_id");
          final int _cursorIndexOfIsInitialPayment = CursorUtil.getColumnIndexOrThrow(_cursor, "is_initial_payment");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transaction_date");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "payment_method");
          final int _cursorIndexOfShortContent = CursorUtil.getColumnIndexOrThrow(_cursor, "short_content");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final CashTransactionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFlowType;
            if (_cursor.isNull(_cursorIndexOfFlowType)) {
              _tmpFlowType = null;
            } else {
              _tmpFlowType = _cursor.getString(_cursorIndexOfFlowType);
            }
            final long _tmpContactId;
            _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            final Long _tmpTradeOrderId;
            if (_cursor.isNull(_cursorIndexOfTradeOrderId)) {
              _tmpTradeOrderId = null;
            } else {
              _tmpTradeOrderId = _cursor.getLong(_cursorIndexOfTradeOrderId);
            }
            final boolean _tmpIsInitialPayment;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsInitialPayment);
            _tmpIsInitialPayment = _tmp != 0;
            final long _tmpAmount;
            _tmpAmount = _cursor.getLong(_cursorIndexOfAmount);
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final String _tmpShortContent;
            if (_cursor.isNull(_cursorIndexOfShortContent)) {
              _tmpShortContent = null;
            } else {
              _tmpShortContent = _cursor.getString(_cursorIndexOfShortContent);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new CashTransactionEntity(_tmpId,_tmpFlowType,_tmpContactId,_tmpTradeOrderId,_tmpIsInitialPayment,_tmpAmount,_tmpTransactionDate,_tmpPaymentMethod,_tmpShortContent,_tmpNote,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Object sumPaidAmountForOrder(final long orderId, final Long excludeTransactionId,
      final Continuation<? super Long> arg2) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(amount), 0) \n"
            + "        FROM cash_transactions \n"
            + "        WHERE trade_order_id = ? AND (? IS NULL OR id != ?)\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, orderId);
    _argIndex = 2;
    if (excludeTransactionId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, excludeTransactionId);
    }
    _argIndex = 3;
    if (excludeTransactionId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, excludeTransactionId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg2);
  }

  @Override
  public Object getAllCashTransactionsSnapshot(
      final Continuation<? super List<CashTransactionEntity>> arg0) {
    final String _sql = "SELECT * FROM cash_transactions ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CashTransactionEntity>>() {
      @Override
      @NonNull
      public List<CashTransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFlowType = CursorUtil.getColumnIndexOrThrow(_cursor, "flow_type");
          final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
          final int _cursorIndexOfTradeOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "trade_order_id");
          final int _cursorIndexOfIsInitialPayment = CursorUtil.getColumnIndexOrThrow(_cursor, "is_initial_payment");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transaction_date");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "payment_method");
          final int _cursorIndexOfShortContent = CursorUtil.getColumnIndexOrThrow(_cursor, "short_content");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<CashTransactionEntity> _result = new ArrayList<CashTransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CashTransactionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFlowType;
            if (_cursor.isNull(_cursorIndexOfFlowType)) {
              _tmpFlowType = null;
            } else {
              _tmpFlowType = _cursor.getString(_cursorIndexOfFlowType);
            }
            final long _tmpContactId;
            _tmpContactId = _cursor.getLong(_cursorIndexOfContactId);
            final Long _tmpTradeOrderId;
            if (_cursor.isNull(_cursorIndexOfTradeOrderId)) {
              _tmpTradeOrderId = null;
            } else {
              _tmpTradeOrderId = _cursor.getLong(_cursorIndexOfTradeOrderId);
            }
            final boolean _tmpIsInitialPayment;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsInitialPayment);
            _tmpIsInitialPayment = _tmp != 0;
            final long _tmpAmount;
            _tmpAmount = _cursor.getLong(_cursorIndexOfAmount);
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final String _tmpShortContent;
            if (_cursor.isNull(_cursorIndexOfShortContent)) {
              _tmpShortContent = null;
            } else {
              _tmpShortContent = _cursor.getString(_cursorIndexOfShortContent);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new CashTransactionEntity(_tmpId,_tmpFlowType,_tmpContactId,_tmpTradeOrderId,_tmpIsInitialPayment,_tmpAmount,_tmpTransactionDate,_tmpPaymentMethod,_tmpShortContent,_tmpNote,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Flow<List<AttachmentEntity>> observeAllAttachments() {
    final String _sql = "SELECT * FROM attachments ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attachments"}, new Callable<List<AttachmentEntity>>() {
      @Override
      @NonNull
      public List<AttachmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTradeOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "trade_order_id");
          final int _cursorIndexOfCashTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "cash_transaction_id");
          final int _cursorIndexOfLocalFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_path");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<AttachmentEntity> _result = new ArrayList<AttachmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttachmentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpTradeOrderId;
            if (_cursor.isNull(_cursorIndexOfTradeOrderId)) {
              _tmpTradeOrderId = null;
            } else {
              _tmpTradeOrderId = _cursor.getLong(_cursorIndexOfTradeOrderId);
            }
            final Long _tmpCashTransactionId;
            if (_cursor.isNull(_cursorIndexOfCashTransactionId)) {
              _tmpCashTransactionId = null;
            } else {
              _tmpCashTransactionId = _cursor.getLong(_cursorIndexOfCashTransactionId);
            }
            final String _tmpLocalFilePath;
            if (_cursor.isNull(_cursorIndexOfLocalFilePath)) {
              _tmpLocalFilePath = null;
            } else {
              _tmpLocalFilePath = _cursor.getString(_cursorIndexOfLocalFilePath);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new AttachmentEntity(_tmpId,_tmpTradeOrderId,_tmpCashTransactionId,_tmpLocalFilePath,_tmpCreatedAt);
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
  public Object getAllAttachmentsSnapshot(final Continuation<? super List<AttachmentEntity>> arg0) {
    final String _sql = "SELECT * FROM attachments ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AttachmentEntity>>() {
      @Override
      @NonNull
      public List<AttachmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTradeOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "trade_order_id");
          final int _cursorIndexOfCashTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "cash_transaction_id");
          final int _cursorIndexOfLocalFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "local_file_path");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<AttachmentEntity> _result = new ArrayList<AttachmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttachmentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpTradeOrderId;
            if (_cursor.isNull(_cursorIndexOfTradeOrderId)) {
              _tmpTradeOrderId = null;
            } else {
              _tmpTradeOrderId = _cursor.getLong(_cursorIndexOfTradeOrderId);
            }
            final Long _tmpCashTransactionId;
            if (_cursor.isNull(_cursorIndexOfCashTransactionId)) {
              _tmpCashTransactionId = null;
            } else {
              _tmpCashTransactionId = _cursor.getLong(_cursorIndexOfCashTransactionId);
            }
            final String _tmpLocalFilePath;
            if (_cursor.isNull(_cursorIndexOfLocalFilePath)) {
              _tmpLocalFilePath = null;
            } else {
              _tmpLocalFilePath = _cursor.getString(_cursorIndexOfLocalFilePath);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new AttachmentEntity(_tmpId,_tmpTradeOrderId,_tmpCashTransactionId,_tmpLocalFilePath,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
