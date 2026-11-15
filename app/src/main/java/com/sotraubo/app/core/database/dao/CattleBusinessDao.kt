package com.sotraubo.app.core.database.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.sotraubo.app.core.database.entity.AttachmentEntity
import com.sotraubo.app.core.database.entity.CashTransactionEntity
import com.sotraubo.app.core.database.entity.ContactEntity
import com.sotraubo.app.core.database.entity.TradeOrderEntity
import kotlinx.coroutines.flow.Flow

data class TradeOrderQueryProjection(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "order_type") val orderType: String,
    @ColumnInfo(name = "contact_id") val contactId: Long,
    @ColumnInfo(name = "contact_name") val contactName: String,
    @ColumnInfo(name = "contact_phone") val contactPhone: String,
    @ColumnInfo(name = "transaction_date") val transactionDate: Long,
    @ColumnInfo(name = "cattle_quantity") val cattleQuantity: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "total_amount") val totalAmount: Long,
    @ColumnInfo(name = "paid_amount") val paidAmount: Long,
    @ColumnInfo(name = "remaining_debt") val remainingDebt: Long,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "created_at") val createdAt: Long
)

data class CashTransactionQueryProjection(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "flow_type") val flowType: String,
    @ColumnInfo(name = "contact_id") val contactId: Long,
    @ColumnInfo(name = "contact_name") val contactName: String,
    @ColumnInfo(name = "contact_phone") val contactPhone: String,
    @ColumnInfo(name = "trade_order_id") val tradeOrderId: Long?,
    @ColumnInfo(name = "trade_order_description") val tradeOrderDescription: String?,
    @ColumnInfo(name = "is_initial_payment") val isInitialPayment: Boolean,
    @ColumnInfo(name = "amount") val amount: Long,
    @ColumnInfo(name = "transaction_date") val transactionDate: Long,
    @ColumnInfo(name = "payment_method") val paymentMethod: String,
    @ColumnInfo(name = "short_content") val shortContent: String,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "created_at") val createdAt: Long
)

@Dao
interface CattleBusinessDao {

    // ==================== CONTACTS ====================
    @Query("SELECT * FROM contacts ORDER BY full_name COLLATE LOCALIZED ASC")
    fun observeAllContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE id = :contactId LIMIT 1")
    fun observeContactById(contactId: Long): Flow<ContactEntity?>

    @Query("SELECT * FROM contacts WHERE id = :contactId LIMIT 1")
    suspend fun getContactById(contactId: Long): ContactEntity?

    @Query("SELECT * FROM contacts ORDER BY id ASC")
    suspend fun getAllContactsSnapshot(): List<ContactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity): Long

    @Update
    suspend fun updateContact(contact: ContactEntity)

    @Query("DELETE FROM contacts WHERE id = :contactId")
    suspend fun deleteContactById(contactId: Long)

    @Query(
        """
        SELECT COUNT(*) FROM (
            SELECT id FROM trade_orders WHERE contact_id = :contactId
            UNION ALL
            SELECT id FROM cash_transactions WHERE contact_id = :contactId
        )
        """
    )
    suspend fun countTransactionsForContact(contactId: Long): Int

    // ==================== TRADE ORDERS (MUA / BÁN) ====================
    @Query(
        """
        SELECT 
            o.id AS id,
            o.order_type AS order_type,
            o.contact_id AS contact_id,
            c.full_name AS contact_name,
            c.phone AS contact_phone,
            o.transaction_date AS transaction_date,
            o.cattle_quantity AS cattle_quantity,
            o.description AS description,
            o.total_amount AS total_amount,
            COALESCE(SUM(ct.amount), 0) AS paid_amount,
            (o.total_amount - COALESCE(SUM(ct.amount), 0)) AS remaining_debt,
            o.note AS note,
            o.created_at AS created_at
        FROM trade_orders o
        INNER JOIN contacts c ON c.id = o.contact_id
        LEFT JOIN cash_transactions ct ON ct.trade_order_id = o.id
        GROUP BY o.id
        ORDER BY o.transaction_date DESC, o.id DESC
        """
    )
    fun observeAllTradeOrdersWithPayment(): Flow<List<TradeOrderQueryProjection>>

    @Query(
        """
        SELECT 
            o.id AS id,
            o.order_type AS order_type,
            o.contact_id AS contact_id,
            c.full_name AS contact_name,
            c.phone AS contact_phone,
            o.transaction_date AS transaction_date,
            o.cattle_quantity AS cattle_quantity,
            o.description AS description,
            o.total_amount AS total_amount,
            COALESCE(SUM(ct.amount), 0) AS paid_amount,
            (o.total_amount - COALESCE(SUM(ct.amount), 0)) AS remaining_debt,
            o.note AS note,
            o.created_at AS created_at
        FROM trade_orders o
        INNER JOIN contacts c ON c.id = o.contact_id
        LEFT JOIN cash_transactions ct ON ct.trade_order_id = o.id
        WHERE o.id = :orderId
        GROUP BY o.id
        LIMIT 1
        """
    )
    suspend fun getTradeOrderWithPaymentById(orderId: Long): TradeOrderQueryProjection?

    @Query("SELECT * FROM trade_orders WHERE id = :orderId LIMIT 1")
    suspend fun getRawTradeOrderById(orderId: Long): TradeOrderEntity?

    @Query("SELECT * FROM trade_orders ORDER BY id ASC")
    suspend fun getAllTradeOrdersSnapshot(): List<TradeOrderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTradeOrder(order: TradeOrderEntity): Long

    @Update
    suspend fun updateTradeOrder(order: TradeOrderEntity)

    @Query("DELETE FROM trade_orders WHERE id = :orderId")
    suspend fun deleteTradeOrderById(orderId: Long)

    // ==================== CASH TRANSACTIONS (NHẬN / CHUYỂN TIỀN) ====================
    @Query(
        """
        SELECT 
            ct.id AS id,
            ct.flow_type AS flow_type,
            ct.contact_id AS contact_id,
            c.full_name AS contact_name,
            c.phone AS contact_phone,
            ct.trade_order_id AS trade_order_id,
            o.description AS trade_order_description,
            ct.is_initial_payment AS is_initial_payment,
            ct.amount AS amount,
            ct.transaction_date AS transaction_date,
            ct.payment_method AS payment_method,
            ct.short_content AS short_content,
            ct.note AS note,
            ct.created_at AS created_at
        FROM cash_transactions ct
        INNER JOIN contacts c ON c.id = ct.contact_id
        LEFT JOIN trade_orders o ON o.id = ct.trade_order_id
        ORDER BY ct.transaction_date DESC, ct.id DESC
        """
    )
    fun observeAllCashTransactions(): Flow<List<CashTransactionQueryProjection>>

    @Query("SELECT * FROM cash_transactions WHERE id = :transactionId LIMIT 1")
    suspend fun getRawCashTransactionById(transactionId: Long): CashTransactionEntity?

    @Query(
        """
        SELECT * FROM cash_transactions 
        WHERE trade_order_id = :orderId AND is_initial_payment = 1 
        LIMIT 1
        """
    )
    suspend fun getInitialPaymentForOrder(orderId: Long): CashTransactionEntity?

    @Query(
        """
        SELECT COALESCE(SUM(amount), 0) 
        FROM cash_transactions 
        WHERE trade_order_id = :orderId AND (:excludeTransactionId IS NULL OR id != :excludeTransactionId)
        """
    )
    suspend fun sumPaidAmountForOrder(orderId: Long, excludeTransactionId: Long? = null): Long

    @Query("SELECT * FROM cash_transactions ORDER BY id ASC")
    suspend fun getAllCashTransactionsSnapshot(): List<CashTransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCashTransaction(transaction: CashTransactionEntity): Long

    @Update
    suspend fun updateCashTransaction(transaction: CashTransactionEntity)

    @Query("DELETE FROM cash_transactions WHERE id = :transactionId")
    suspend fun deleteCashTransactionById(transactionId: Long)

    // ==================== ATTACHMENTS ====================
    @Query("SELECT * FROM attachments ORDER BY id ASC")
    fun observeAllAttachments(): Flow<List<AttachmentEntity>>

    @Query("SELECT * FROM attachments ORDER BY id ASC")
    suspend fun getAllAttachmentsSnapshot(): List<AttachmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachments(attachments: List<AttachmentEntity>)

    @Query("DELETE FROM attachments WHERE trade_order_id = :orderId")
    suspend fun deleteAttachmentsForOrder(orderId: Long)

    @Query("DELETE FROM attachments WHERE cash_transaction_id = :transactionId")
    suspend fun deleteAttachmentsForCashTransaction(transactionId: Long)

    // ==================== BACKUP & RESTORE ATOMIC CLEAR ====================
    @Query("DELETE FROM attachments")
    suspend fun clearAllAttachments()

    @Query("DELETE FROM cash_transactions")
    suspend fun clearAllCashTransactions()

    @Query("DELETE FROM trade_orders")
    suspend fun clearAllTradeOrders()

    @Query("DELETE FROM contacts")
    suspend fun clearAllContacts()

    @Transaction
    suspend fun replaceAllDataInTransaction(
        contacts: List<ContactEntity>,
        orders: List<TradeOrderEntity>,
        cashTransactions: List<CashTransactionEntity>,
        attachments: List<AttachmentEntity>
    ) {
        clearAllAttachments()
        clearAllCashTransactions()
        clearAllTradeOrders()
        clearAllContacts()

        contacts.forEach { insertContact(it) }
        orders.forEach { insertTradeOrder(it) }
        cashTransactions.forEach { insertCashTransaction(it) }
        if (attachments.isNotEmpty()) {
            insertAttachments(attachments)
        }
    }
}
