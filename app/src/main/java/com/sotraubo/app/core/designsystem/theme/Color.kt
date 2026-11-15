package com.sotraubo.app.core.designsystem.theme

import androidx.compose.ui.graphics.Color

// ==================== DARK THEME BEST PRACTICES (MATERIAL 3) ====================
// Tránh dùng màu đen tuyền (#000000) gây lóa mắt (Halation); sử dụng Slate Dark (#0B0F17)
// và các lớp thẻ phân cấp độ sáng (Elevation Tonal Surfaces).

// Màu nền & Bề mặt thẻ (Dark Surfaces - Tách lớp rõ nét)
val BackgroundDark = Color(0xFF090D14)
val SurfaceDarkCard = Color(0xFF172030)
val SurfaceElevatedDark = Color(0xFF222E44)
val SurfaceTopBarDark = Color(0xFF101622)

// Màu chữ & Đường viền (Độ tương phản cao chuẩn WCAG AAA)
val TextPrimaryLight = Color(0xFFF8FAFC)
val TextSecondaryLight = Color(0xFFB4C2D6)
val DividerDark = Color(0xFF3B4C6B)

// Màu chủ đạo (Xanh dương nét): Thông tin chính, Mua bán, Điều hướng
val BluePrimary = Color(0xFF3B9EFF)
val BluePrimaryDark = Color(0xFF38BDF8)
val BlueContainer = Color(0xFF16355C)
val OnBlueContainer = Color(0xFFE0F2FE)

// Màu Xanh lá (Emerald nét): Tiền nhận (Thu tiền), Người khác còn nợ mình (Phải thu)
val GreenReceive = Color(0xFF22E58A)
val GreenReceiveContainer = Color(0xFF074632)
val OnGreenReceiveContainer = Color(0xFFD1FAE5)

// Màu Đỏ (Coral Red nét): Tiền chuyển (Chi tiền), Cảnh báo xóa
val RedTransfer = Color(0xFFFF6B6B)
val RedTransferContainer = Color(0xFF5C1919)
val OnRedTransferContainer = Color(0xFFFEE2E2)

// Màu Cam (Warm Amber nét): Mình còn nợ người khác (Phải trả), Nhắc công nợ
val OrangePayable = Color(0xFFFFA133)
val OrangePayableContainer = Color(0xFF592708)
val OnOrangePayableContainer = Color(0xFFFFEDD5)
