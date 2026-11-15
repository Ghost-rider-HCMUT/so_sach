package com.sotraubo.app.core.database.dao;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import com.sotraubo.app.core.database.entity.AttachmentEntity;
import com.sotraubo.app.core.database.entity.CashTransactionEntity;
import com.sotraubo.app.core.database.entity.ContactEntity;
import com.sotraubo.app.core.database.entity.TradeOrderEntity;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0006\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0007\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\u0012\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u001d\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0018\u0010\u001e\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0010\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0018\u0010\u001f\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u000e\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0018\u0010 \u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u0010\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0018\u0010!\u001a\u0004\u0018\u00010\"2\u0006\u0010\u0010\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u001c\u0010#\u001a\u00020\u00032\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015H\u00a7@\u00a2\u0006\u0002\u0010%J\u0016\u0010&\u001a\u00020\u000b2\u0006\u0010\'\u001a\u00020\u0018H\u00a7@\u00a2\u0006\u0002\u0010(J\u0016\u0010)\u001a\u00020\u000b2\u0006\u0010*\u001a\u00020\u001aH\u00a7@\u00a2\u0006\u0002\u0010+J\u0016\u0010,\u001a\u00020\u000b2\u0006\u0010-\u001a\u00020\u001cH\u00a7@\u00a2\u0006\u0002\u0010.J\u0014\u0010/\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u001500H\'J\u0014\u00101\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002020\u001500H\'J\u0014\u00103\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u001500H\'J\u0014\u00104\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0\u001500H\'J\u0018\u00105\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001a002\u0006\u0010\n\u001a\u00020\u000bH\'JF\u00106\u001a\u00020\u00032\f\u00107\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00152\f\u00108\u001a\b\u0012\u0004\u0012\u00020\u001c0\u00152\f\u00109\u001a\b\u0012\u0004\u0012\u00020\u00180\u00152\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015H\u0097@\u00a2\u0006\u0002\u0010:J\"\u0010;\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000b2\n\b\u0002\u0010<\u001a\u0004\u0018\u00010\u000bH\u00a7@\u00a2\u0006\u0002\u0010=J\u0016\u0010>\u001a\u00020\u00032\u0006\u0010\'\u001a\u00020\u0018H\u00a7@\u00a2\u0006\u0002\u0010(J\u0016\u0010?\u001a\u00020\u00032\u0006\u0010*\u001a\u00020\u001aH\u00a7@\u00a2\u0006\u0002\u0010+J\u0016\u0010@\u001a\u00020\u00032\u0006\u0010-\u001a\u00020\u001cH\u00a7@\u00a2\u0006\u0002\u0010.\u00a8\u0006A"}, d2 = {"Lcom/sotraubo/app/core/database/dao/CattleBusinessDao;", "", "clearAllAttachments", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearAllCashTransactions", "clearAllContacts", "clearAllTradeOrders", "countTransactionsForContact", "", "contactId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAttachmentsForCashTransaction", "transactionId", "deleteAttachmentsForOrder", "orderId", "deleteCashTransactionById", "deleteContactById", "deleteTradeOrderById", "getAllAttachmentsSnapshot", "", "Lcom/sotraubo/app/core/database/entity/AttachmentEntity;", "getAllCashTransactionsSnapshot", "Lcom/sotraubo/app/core/database/entity/CashTransactionEntity;", "getAllContactsSnapshot", "Lcom/sotraubo/app/core/database/entity/ContactEntity;", "getAllTradeOrdersSnapshot", "Lcom/sotraubo/app/core/database/entity/TradeOrderEntity;", "getContactById", "getInitialPaymentForOrder", "getRawCashTransactionById", "getRawTradeOrderById", "getTradeOrderWithPaymentById", "Lcom/sotraubo/app/core/database/dao/TradeOrderQueryProjection;", "insertAttachments", "attachments", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertCashTransaction", "transaction", "(Lcom/sotraubo/app/core/database/entity/CashTransactionEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertContact", "contact", "(Lcom/sotraubo/app/core/database/entity/ContactEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertTradeOrder", "order", "(Lcom/sotraubo/app/core/database/entity/TradeOrderEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeAllAttachments", "Lkotlinx/coroutines/flow/Flow;", "observeAllCashTransactions", "Lcom/sotraubo/app/core/database/dao/CashTransactionQueryProjection;", "observeAllContacts", "observeAllTradeOrdersWithPayment", "observeContactById", "replaceAllDataInTransaction", "contacts", "orders", "cashTransactions", "(Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sumPaidAmountForOrder", "excludeTransactionId", "(JLjava/lang/Long;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateCashTransaction", "updateContact", "updateTradeOrder", "app_debug"})
@androidx.room.Dao()
public abstract interface CattleBusinessDao {
    
    @androidx.room.Query(value = "SELECT * FROM contacts ORDER BY full_name COLLATE LOCALIZED ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.entity.ContactEntity>> observeAllContacts();
    
    @androidx.room.Query(value = "SELECT * FROM contacts WHERE id = :contactId LIMIT 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.sotraubo.app.core.database.entity.ContactEntity> observeContactById(long contactId);
    
    @androidx.room.Query(value = "SELECT * FROM contacts WHERE id = :contactId LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getContactById(long contactId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.database.entity.ContactEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM contacts ORDER BY id ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllContactsSnapshot(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.ContactEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertContact(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.ContactEntity contact, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateContact(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.ContactEntity contact, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM contacts WHERE id = :contactId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteContactById(long contactId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        SELECT COUNT(*) FROM (\n            SELECT id FROM trade_orders WHERE contact_id = :contactId\n            UNION ALL\n            SELECT id FROM cash_transactions WHERE contact_id = :contactId\n        )\n        ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object countTransactionsForContact(long contactId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "\n        SELECT \n            o.id AS id,\n            o.order_type AS order_type,\n            o.contact_id AS contact_id,\n            c.full_name AS contact_name,\n            c.phone AS contact_phone,\n            o.transaction_date AS transaction_date,\n            o.cattle_quantity AS cattle_quantity,\n            o.description AS description,\n            o.total_amount AS total_amount,\n            COALESCE(SUM(ct.amount), 0) AS paid_amount,\n            (o.total_amount - COALESCE(SUM(ct.amount), 0)) AS remaining_debt,\n            o.note AS note,\n            o.created_at AS created_at\n        FROM trade_orders o\n        INNER JOIN contacts c ON c.id = o.contact_id\n        LEFT JOIN cash_transactions ct ON ct.trade_order_id = o.id\n        GROUP BY o.id\n        ORDER BY o.transaction_date DESC, o.id DESC\n        ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.dao.TradeOrderQueryProjection>> observeAllTradeOrdersWithPayment();
    
    @androidx.room.Query(value = "\n        SELECT \n            o.id AS id,\n            o.order_type AS order_type,\n            o.contact_id AS contact_id,\n            c.full_name AS contact_name,\n            c.phone AS contact_phone,\n            o.transaction_date AS transaction_date,\n            o.cattle_quantity AS cattle_quantity,\n            o.description AS description,\n            o.total_amount AS total_amount,\n            COALESCE(SUM(ct.amount), 0) AS paid_amount,\n            (o.total_amount - COALESCE(SUM(ct.amount), 0)) AS remaining_debt,\n            o.note AS note,\n            o.created_at AS created_at\n        FROM trade_orders o\n        INNER JOIN contacts c ON c.id = o.contact_id\n        LEFT JOIN cash_transactions ct ON ct.trade_order_id = o.id\n        WHERE o.id = :orderId\n        GROUP BY o.id\n        LIMIT 1\n        ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTradeOrderWithPaymentById(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.database.dao.TradeOrderQueryProjection> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM trade_orders WHERE id = :orderId LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRawTradeOrderById(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.database.entity.TradeOrderEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM trade_orders ORDER BY id ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllTradeOrdersSnapshot(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.TradeOrderEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertTradeOrder(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.TradeOrderEntity order, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateTradeOrder(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.TradeOrderEntity order, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM trade_orders WHERE id = :orderId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteTradeOrderById(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        SELECT \n            ct.id AS id,\n            ct.flow_type AS flow_type,\n            ct.contact_id AS contact_id,\n            c.full_name AS contact_name,\n            c.phone AS contact_phone,\n            ct.trade_order_id AS trade_order_id,\n            o.description AS trade_order_description,\n            ct.is_initial_payment AS is_initial_payment,\n            ct.amount AS amount,\n            ct.transaction_date AS transaction_date,\n            ct.payment_method AS payment_method,\n            ct.short_content AS short_content,\n            ct.note AS note,\n            ct.created_at AS created_at\n        FROM cash_transactions ct\n        INNER JOIN contacts c ON c.id = ct.contact_id\n        LEFT JOIN trade_orders o ON o.id = ct.trade_order_id\n        ORDER BY ct.transaction_date DESC, ct.id DESC\n        ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.dao.CashTransactionQueryProjection>> observeAllCashTransactions();
    
    @androidx.room.Query(value = "SELECT * FROM cash_transactions WHERE id = :transactionId LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRawCashTransactionById(long transactionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.database.entity.CashTransactionEntity> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM cash_transactions \n        WHERE trade_order_id = :orderId AND is_initial_payment = 1 \n        LIMIT 1\n        ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getInitialPaymentForOrder(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.sotraubo.app.core.database.entity.CashTransactionEntity> $completion);
    
    @androidx.room.Query(value = "\n        SELECT COALESCE(SUM(amount), 0) \n        FROM cash_transactions \n        WHERE trade_order_id = :orderId AND (:excludeTransactionId IS NULL OR id != :excludeTransactionId)\n        ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object sumPaidAmountForOrder(long orderId, @org.jetbrains.annotations.Nullable()
    java.lang.Long excludeTransactionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM cash_transactions ORDER BY id ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllCashTransactionsSnapshot(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.CashTransactionEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertCashTransaction(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.CashTransactionEntity transaction, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateCashTransaction(@org.jetbrains.annotations.NotNull()
    com.sotraubo.app.core.database.entity.CashTransactionEntity transaction, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM cash_transactions WHERE id = :transactionId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteCashTransactionById(long transactionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM attachments ORDER BY id ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity>> observeAllAttachments();
    
    @androidx.room.Query(value = "SELECT * FROM attachments ORDER BY id ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllAttachmentsSnapshot(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAttachments(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity> attachments, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM attachments WHERE trade_order_id = :orderId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAttachmentsForOrder(long orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM attachments WHERE cash_transaction_id = :transactionId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAttachmentsForCashTransaction(long transactionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM attachments")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAllAttachments(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM cash_transactions")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAllCashTransactions(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM trade_orders")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAllTradeOrders(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM contacts")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAllContacts(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Transaction()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object replaceAllDataInTransaction(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.ContactEntity> contacts, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.TradeOrderEntity> orders, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.CashTransactionEntity> cashTransactions, @org.jetbrains.annotations.NotNull()
    java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity> attachments, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
        
        @androidx.room.Transaction()
        @org.jetbrains.annotations.Nullable()
        public static java.lang.Object replaceAllDataInTransaction(@org.jetbrains.annotations.NotNull()
        com.sotraubo.app.core.database.dao.CattleBusinessDao $this, @org.jetbrains.annotations.NotNull()
        java.util.List<com.sotraubo.app.core.database.entity.ContactEntity> contacts, @org.jetbrains.annotations.NotNull()
        java.util.List<com.sotraubo.app.core.database.entity.TradeOrderEntity> orders, @org.jetbrains.annotations.NotNull()
        java.util.List<com.sotraubo.app.core.database.entity.CashTransactionEntity> cashTransactions, @org.jetbrains.annotations.NotNull()
        java.util.List<com.sotraubo.app.core.database.entity.AttachmentEntity> attachments, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
            return null;
        }
    }
}