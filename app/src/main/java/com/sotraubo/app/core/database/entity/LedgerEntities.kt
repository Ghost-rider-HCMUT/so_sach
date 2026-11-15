package com.sotraubo.app.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** Hàng hóa mua (cám, thuốc thú y, cỏ, dụng cụ...). */
@Entity(
    tableName = "goods_purchases",
    indices = [Index(value = ["purchase_date"])]
)
data class GoodsPurchaseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "purchase_date")
    val purchaseDate: Long,

    @ColumnInfo(name = "item_name")
    val itemName: String,

    /** Số lượng nhân 100 để lưu số thập phân (vd 2,5 kg = 250). */
    @ColumnInfo(name = "quantity_x100")
    val quantityX100: Long,

    @ColumnInfo(name = "unit")
    val unit: String,

    @ColumnInfo(name = "total_amount")
    val totalAmount: Long,

    @ColumnInfo(name = "supplier")
    val supplier: String = "",

    @ColumnInfo(name = "contact_id")
    val contactId: Long? = null,

    @ColumnInfo(name = "note")
    val note: String = "",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

/** Đổi tiền (VND <-> Baht Thái). */
@Entity(
    tableName = "currency_exchanges",
    indices = [Index(value = ["exchange_date"])]
)
data class CurrencyExchangeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "exchange_date")
    val exchangeDate: Long,

    /** "VND_TO_THB" (đổi VND lấy Bath) | "THB_TO_VND" (đổi Bath lấy VND). */
    @ColumnInfo(name = "direction")
    val direction: String,

    @ColumnInfo(name = "baht_amount")
    val bahtAmount: Long,

    /** Tỷ giá bán / cần thu: số VND cho 1 Bath. */
    @ColumnInfo(name = "rate")
    val rate: Long,

    @ColumnInfo(name = "vnd_amount")
    val vndAmount: Long,

    @ColumnInfo(name = "theoretical_vnd_amount", defaultValue = "0")
    val theoreticalVndAmount: Long = 0L,

    /** Tỷ giá vốn / giá Bath mình mua hoặc chuyển đi (đ / 1 Bath). */
    @ColumnInfo(name = "cost_rate", defaultValue = "0")
    val costRate: Long = 0L,

    /** Số tiền Việt vốn đã chuyển đi cho giao dịch Đổi Bath (đ). */
    @ColumnInfo(name = "cost_vnd_amount", defaultValue = "0")
    val costVndAmount: Long = 0L,

    @ColumnInfo(name = "partner_name", defaultValue = "")
    val partnerName: String = "",

    @ColumnInfo(name = "contact_id")
    val contactId: Long? = null,

    @ColumnInfo(name = "note")
    val note: String = "",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

/** Chi tiêu gia đình. */
@Entity(
    tableName = "family_expenses",
    indices = [Index(value = ["expense_date"])]
)
data class FamilyExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "expense_date")
    val expenseDate: Long,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "amount")
    val amount: Long,

    @ColumnInfo(name = "partner_name", defaultValue = "")
    val partnerName: String = "",

    @ColumnInfo(name = "contact_id")
    val contactId: Long? = null,

    @ColumnInfo(name = "note")
    val note: String = "",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

/** Thu phí quota qua biên giới (chỉ thu, không có chi). */
@Entity(
    tableName = "quota_fees",
    indices = [Index(value = ["fee_date"])]
)
data class QuotaFeeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "fee_date")
    val feeDate: Long,

    @ColumnInfo(name = "payer_name", defaultValue = "")
    val payerName: String = "",

    @ColumnInfo(name = "contact_id")
    val contactId: Long? = null,

    @ColumnInfo(name = "cattle_quantity", defaultValue = "0")
    val cattleQuantity: Int = 0,

    @ColumnInfo(name = "amount")
    val amount: Long,

    @ColumnInfo(name = "note", defaultValue = "")
    val note: String = "",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
