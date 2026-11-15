package com.sotraubo.app.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "contacts",
    indices = [
        Index(value = ["name_normalized"]),
        Index(value = ["phone_normalized"])
    ]
)
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "full_name")
    val fullName: String,

    @ColumnInfo(name = "name_normalized")
    val nameNormalized: String,

    @ColumnInfo(name = "phone")
    val phone: String = "",

    @ColumnInfo(name = "phone_normalized")
    val phoneNormalized: String = "",

    @ColumnInfo(name = "address")
    val address: String = "",

    @ColumnInfo(name = "note")
    val note: String = "",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "trade_orders",
    foreignKeys = [
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = ["id"],
            childColumns = ["contact_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["order_type"]),
        Index(value = ["contact_id"]),
        Index(value = ["transaction_date"])
    ]
)
data class TradeOrderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "order_type")
    val orderType: String, // "PURCHASE" | "SALE"

    @ColumnInfo(name = "contact_id")
    val contactId: Long,

    @ColumnInfo(name = "transaction_date")
    val transactionDate: Long,

    @ColumnInfo(name = "cattle_quantity")
    val cattleQuantity: Int,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "total_amount")
    val totalAmount: Long,

    @ColumnInfo(name = "note")
    val note: String = "",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "cash_transactions",
    foreignKeys = [
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = ["id"],
            childColumns = ["contact_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = TradeOrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["trade_order_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["flow_type"]),
        Index(value = ["contact_id"]),
        Index(value = ["trade_order_id"]),
        Index(value = ["transaction_date"])
    ]
)
data class CashTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "flow_type")
    val flowType: String, // "CASH_IN" | "CASH_OUT"

    @ColumnInfo(name = "contact_id")
    val contactId: Long,

    @ColumnInfo(name = "trade_order_id")
    val tradeOrderId: Long? = null,

    @ColumnInfo(name = "is_initial_payment")
    val isInitialPayment: Boolean = false,

    @ColumnInfo(name = "amount")
    val amount: Long,

    @ColumnInfo(name = "transaction_date")
    val transactionDate: Long,

    @ColumnInfo(name = "payment_method")
    val paymentMethod: String, // "CASH" | "BANK_TRANSFER"

    @ColumnInfo(name = "short_content")
    val shortContent: String,

    @ColumnInfo(name = "note")
    val note: String = "",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "attachments",
    foreignKeys = [
        ForeignKey(
            entity = TradeOrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["trade_order_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CashTransactionEntity::class,
            parentColumns = ["id"],
            childColumns = ["cash_transaction_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["trade_order_id"]),
        Index(value = ["cash_transaction_id"])
    ]
)
data class AttachmentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "trade_order_id")
    val tradeOrderId: Long? = null,

    @ColumnInfo(name = "cash_transaction_id")
    val cashTransactionId: Long? = null,

    @ColumnInfo(name = "local_file_path")
    val localFilePath: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
