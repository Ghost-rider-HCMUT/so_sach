package com.sotraubo.app.feature.ledger

/**
 * Kết quả kiểm tra và quy đổi dữ liệu nhập cho giao dịch Đổi Bath Thái.
 */
sealed interface BathExchangeResolution {
    data class Valid(
        val resolvedVndAmount: Long,
        val resolvedTheoreticalVndAmount: Long,
        val resolvedCostVndAmount: Long = 0L
    ) : BathExchangeResolution

    data class Invalid(val errorMessage: String) : BathExchangeResolution
}

/**
 * Bộ xử lý quy tắc nhập liệu linh động cho Đổi Bath Thái:
 * - Nếu chưa nhập tiền Việt: tự nhân `bahtAmount * rate` (nếu thiếu 1 trong 2 thì báo lỗi rõ ràng).
 * - Nếu đã nhập tiền Việt: không bắt buộc nhập Bath hay Tỉ giá.
 * - Cho phép nhập cả Bath và tiền Việt mà không cần nhập Tỉ giá.
 */
object BathExchangeInputResolver {

    fun resolve(
        directionCode: String,
        bahtAmount: Long,
        rate: Long,
        vndAmount: Long,
        theoreticalVndAmount: Long,
        costRate: Long = 0L,
        costVndAmount: Long = 0L
    ): BathExchangeResolution {
        if (directionCode == "VND_TO_THB") {
            val resolvedVnd = when {
                vndAmount > 0L -> vndAmount
                bahtAmount > 0L && rate > 0L -> bahtAmount * rate
                else -> 0L
            }
            if (resolvedVnd <= 0L) {
                val error = when {
                    bahtAmount > 0L && rate <= 0L ->
                        "Chưa nhập tiền Việt: hãy nhập thêm Tỉ giá để nhân hoặc nhập thẳng Số tiền Việt."
                    rate > 0L && bahtAmount <= 0L ->
                        "Chưa nhập tiền Việt: hãy nhập thêm Số Bath để nhân hoặc nhập thẳng Số tiền Việt."
                    else ->
                        "Vui lòng nhập Số tiền Việt, hoặc nhập đủ Số Bath và Tỉ giá."
                }
                return BathExchangeResolution.Invalid(error)
            }
            return BathExchangeResolution.Valid(
                resolvedVndAmount = resolvedVnd,
                resolvedTheoreticalVndAmount = resolvedVnd,
                resolvedCostVndAmount = resolvedVnd
            )
        }

        // Chiều THB_TO_VND (Khách chuyển Việt)
        val resolvedCost = when {
            costVndAmount > 0L -> costVndAmount
            bahtAmount > 0L && costRate > 0L -> bahtAmount * costRate
            else -> 0L
        }
        val resolvedTheoretical = when {
            theoreticalVndAmount > 0L -> theoreticalVndAmount
            bahtAmount > 0L && rate > 0L -> bahtAmount * rate
            vndAmount > 0L -> vndAmount
            else -> 0L
        }
        if (vndAmount <= 0L && resolvedTheoretical <= 0L) {
            val error = when {
                bahtAmount > 0L && rate <= 0L ->
                    "Chưa nhập tiền Việt: hãy nhập thêm Giá bán để nhân hoặc nhập thẳng Tiền Việt cần thu."
                rate > 0L && bahtAmount <= 0L ->
                    "Chưa nhập tiền Việt: hãy nhập thêm Số Bath để nhân hoặc nhập thẳng Tiền Việt cần thu."
                else ->
                    "Vui lòng nhập Số tiền Việt (đã thu hoặc cần thu), hoặc nhập đủ Số Bath và Giá bán."
            }
            return BathExchangeResolution.Invalid(error)
        }
        return BathExchangeResolution.Valid(
            resolvedVndAmount = vndAmount,
            resolvedTheoreticalVndAmount = resolvedTheoretical,
            resolvedCostVndAmount = resolvedCost
        )
    }
}
