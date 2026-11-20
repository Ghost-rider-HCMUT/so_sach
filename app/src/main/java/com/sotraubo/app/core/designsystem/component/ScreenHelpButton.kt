package com.sotraubo.app.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sotraubo.app.core.designsystem.theme.BlueContainer
import com.sotraubo.app.core.designsystem.theme.BluePrimary
import com.sotraubo.app.core.designsystem.theme.DividerDark
import com.sotraubo.app.core.designsystem.theme.GreenReceive
import com.sotraubo.app.core.designsystem.theme.GreenReceiveContainer
import com.sotraubo.app.core.designsystem.theme.SurfaceDarkCard

data class ScreenHelpStep(
    val title: String,
    val detail: String
)

data class ScreenHelpGuide(
    val screenTitle: String,
    val summary: String,
    val steps: List<ScreenHelpStep>,
    val quickTip: String
)

/**
 * Nút nhỏ "?" đặt trên thanh tiêu đề (TopAppBar) của từng màn hình.
 * Khi bấm sẽ mở hộp thoại hướng dẫn chi tiết cách thao tác riêng cho màn hình đó.
 */
@Composable
fun ScreenHelpButton(
    guide: ScreenHelpGuide,
    modifier: Modifier = Modifier,
    onColoredBar: Boolean = false
) {
    var showHelp by rememberSaveable { mutableStateOf(false) }

    val badgeBg = if (onColoredBar) Color.White.copy(alpha = 0.22f) else BlueContainer
    val badgeBorder = if (onColoredBar) Color.White.copy(alpha = 0.85f) else BluePrimary
    val badgeText = if (onColoredBar) Color.White else BluePrimary

    Box(
        modifier = modifier
            .padding(start = 4.dp, end = 10.dp)
            .size(32.dp)
            .clip(CircleShape)
            .background(badgeBg)
            .clickable { showHelp = true }
            .semantics {
                contentDescription = "Hướng dẫn màn hình"
                role = Role.Button
            },
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.size(32.dp),
            shape = CircleShape,
            color = Color.Transparent,
            border = BorderStroke(1.5.dp, badgeBorder)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "?",
                    color = badgeText,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
            }
        }
    }

    if (showHelp) {
        Dialog(
            onDismissRequest = { showHelp = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 24.dp),
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                border = BorderStroke(1.5.dp, BluePrimary.copy(alpha = 0.65f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                modifier = Modifier.size(30.dp),
                                shape = CircleShape,
                                color = BlueContainer,
                                border = BorderStroke(1.2.dp, BluePrimary)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "?",
                                        color = BluePrimary,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "HƯỚNG DẪN SỬ DỤNG",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = BluePrimary
                                )
                                Text(
                                    text = guide.screenTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        IconButton(
                            onClick = { showHelp = false },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Đóng hướng dẫn"
                            )
                        }
                    }

                    Text(
                        text = guide.summary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    HorizontalDivider(thickness = 1.dp, color = DividerDark)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 420.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        guide.steps.forEachIndexed { index, step ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = SurfaceDarkCard,
                                border = BorderStroke(1.dp, BluePrimary.copy(alpha = 0.28f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(BluePrimary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${index + 1}",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = step.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = step.detail,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = GreenReceiveContainer,
                            border = BorderStroke(1.dp, GreenReceive.copy(alpha = 0.55f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Mẹo nhanh",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenReceive
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = guide.quickTip,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    HorizontalDivider(thickness = 1.dp, color = DividerDark)

                    Button(
                        onClick = { showHelp = false },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                    ) {
                        Text(
                            text = "Đã hiểu",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

object ScreenHelpGuides {

    val DASHBOARD = ScreenHelpGuide(
        screenTitle = "Tổng quan tài chính",
        summary = "Màn hình theo dõi nhanh dòng tiền đang có, thống kê lãi/lỗ theo kỳ và các khoản công nợ nổi bật.",
        steps = listOf(
            ScreenHelpStep(
                title = "4 lối tắt ghi sổ nhanh ở trên cùng",
                detail = "Chạm vào Hàng hóa (Mua/Bán trâu bò), Đổi Bath, Phí Quota hoặc Sinh hoạt để mở ngay biểu mẫu ghi chép tương ứng."
            ),
            ScreenHelpStep(
                title = "Chọn kỳ xem thống kê (Năm / Tháng / Tuần / Ngày)",
                detail = "Chọn loại kỳ và dùng 2 mũi tên < > để lùi hoặc tiến kỳ cần xem."
            ),
            ScreenHelpStep(
                title = "Tình hình tiền bạc & Vốn ban đầu",
                detail = "Bấm mở rộng mục 'TÌNH HÌNH TIỀN BẠC' để xem Tiền đang có, Khách còn nợ mình, Mình còn nợ người bán và khai báo Vốn ban đầu."
            ),
            ScreenHelpStep(
                title = "Sao lưu dữ liệu",
                detail = "Bấm nút 'Sao lưu' ở góc phải trên cùng để xuất file sao lưu hoặc xuất báo cáo PDF."
            )
        ),
        quickTip = "Bạn có thể chạm trực tiếp vào từng thẻ công nợ nổi bật ở cuối trang để mở chi tiết đối tác hoặc ghi thu/trả nợ nhanh."
    )

    val MUA_BAN = ScreenHelpGuide(
        screenTitle = "Sổ Mua Bán & Thu Chi",
        summary = "Danh sách toàn bộ đơn mua/bán trâu bò và các phiếu thu/chi tiền mặt hoặc chuyển khoản.",
        steps = listOf(
            ScreenHelpStep(
                title = "Thêm giao dịch mới",
                detail = "Bấm nút '+ Ghi sổ' ở góc trên bên phải để chọn: Mua trâu bò, Bán trâu bò, Thu tiền hoặc Chi tiền."
            ),
            ScreenHelpStep(
                title = "Lọc theo loại giao dịch & thời gian",
                detail = "Dùng các thẻ lọc (Tất cả, Mua hàng, Bán hàng, Thu tiền, Chi tiền) kết hợp thanh lọc thời gian (Tháng/Ngày/Hôm nay) để tra cứu nhanh."
            ),
            ScreenHelpStep(
                title = "Xem hoặc sửa đơn đã ghi",
                detail = "Chạm vào bất kỳ dòng đơn hàng hoặc phiếu thu/chi nào để mở lại biểu mẫu chỉnh sửa hoặc ghi trả thêm đợt tiếp theo."
            )
        ),
        quickTip = "Mỗi đơn mua/bán còn nợ đều hiển thị rõ số tiền còn nợ ngay trên thẻ để bạn kiểm soát dễ dàng."
    )

    fun tradeOrderForm(isPurchase: Boolean, isEditing: Boolean): ScreenHelpGuide {
        val actionWord = if (isPurchase) "mua trâu bò" else "bán trâu bò"
        return ScreenHelpGuide(
            screenTitle = if (isEditing) "Sửa đơn $actionWord" else "Tạo đơn $actionWord",
            summary = "Biểu mẫu ghi nhận đơn $actionWord, tự động tính số tiền còn nợ theo tổng tiền và số đã thanh toán.",
            steps = listOf(
                ScreenHelpStep(
                    title = "Chọn hoặc thêm mới đối tác",
                    detail = "Bấm vào mục số 1 để chọn người trong danh bạ hoặc tạo người mới ngay tại chỗ mà không cần thoát màn hình."
                ),
                ScreenHelpStep(
                    title = "Ngày, số lượng & mô tả lô hàng",
                    detail = "Dùng nút - / + để chỉnh số lượng con và nhập mô tả đặc điểm (ví dụ: 2 bò đực BBB + 1 bò cái vàng)."
                ),
                ScreenHelpStep(
                    title = "Nhập Tổng tiền & Đã thanh toán bằng máy tính tiền",
                    detail = "Chạm vào ô tiền để mở bảng máy tính (+, -, ×, ÷, 000). Bạn gõ số đến đâu ô tiền tự cập nhật ngay đến đó, hoặc bấm gợi ý 'Trả đủ / Thu đủ'."
                ),
                ScreenHelpStep(
                    title = "Ghi trả góp thêm nhiều đợt (khi sửa đơn)",
                    detail = "Khi mở lại đơn cũ còn nợ, bấm nút 'Ghi thêm đợt trả tiền' để cộng dồn số tiền trả và tự động lưu lịch sử vào ghi chú đơn."
                )
            ),
            quickTip = "Trên bảng máy tính tiền, bạn có thể tính phép nhân trực tiếp (ví dụ: 35.000.000 × 5) để tính tổng tiền nhiều con cực nhanh."
        )
    }

    fun cashTransactionForm(isCashIn: Boolean, isEditing: Boolean): ScreenHelpGuide {
        val label = if (isCashIn) "phiếu Thu tiền" else "phiếu Chi tiền"
        return ScreenHelpGuide(
            screenTitle = if (isEditing) "Sửa $label" else "Ghi $label",
            summary = "Ghi nhận khoản tiền thực tế thu vào hoặc chi ra, có thể gắn vào từng đơn cụ thể hoặc tự động trừ nợ đơn cũ nhất.",
            steps = listOf(
                ScreenHelpStep(
                    title = "Chọn người liên quan",
                    detail = "Chọn đối tác giao dịch để hệ thống tự động liệt kê các đơn còn nợ của người đó."
                ),
                ScreenHelpStep(
                    title = "Chọn cách trừ công nợ",
                    detail = "Bạn có thể chọn 'Tự động trừ vào đơn nợ cũ nhất' hoặc chỉ định trả cho một đơn hàng cụ thể."
                ),
                ScreenHelpStep(
                    title = "Nhập số tiền & hình thức thanh toán",
                    detail = "Chạm vào ô số tiền để nhập bằng máy tính hoặc bấm nút gợi ý 'Điền đủ nợ', sau đó chọn Tiền mặt hoặc Chuyển khoản."
                )
            ),
            quickTip = "Khi bấm gợi ý 'Điền đủ nợ', hệ thống sẽ tự điền chính xác tổng dư nợ hiện tại của đối tác để không bị nhập lệch."
        )
    }

    val DEBT_OVERVIEW = ScreenHelpGuide(
        screenTitle = "Quản lý Công nợ",
        summary = "Theo dõi tách biệt 2 sổ: Khách còn nợ mình (Phải thu) và Mình còn nợ người bán (Phải trả), bao gồm cả công nợ đổi Bath.",
        steps = listOf(
            ScreenHelpStep(
                title = "Chuyển giữa 2 tab Phải thu & Phải trả",
                detail = "Chạm vào thẻ 'Khách nợ mình' (màu xanh) hoặc 'Mình nợ người ta' (màu cam) để xem danh sách từng nhóm."
            ),
            ScreenHelpStep(
                title = "Tìm kiếm & sắp xếp đối tác",
                detail = "Gõ tên hoặc số điện thoại (không cần dấu) vào ô tìm kiếm, và chọn sắp xếp theo Nợ nhiều nhất hoặc Giao dịch gần nhất."
            ),
            ScreenHelpStep(
                title = "Thu nợ / Trả nợ nhanh",
                detail = "Bấm nút 'Thu nợ' hoặc 'Trả nợ' ngay trên thẻ của từng người để lập phiếu thu/chi gắn sẵn tên người đó."
            )
        ),
        quickTip = "Chạm vào tên đối tác để mở trang Đối chiếu công nợ hai chiều và xuất phiếu PDF gửi qua Zalo."
    )

    val PERSON_DEBT_DETAIL = ScreenHelpGuide(
        screenTitle = "Chi tiết công nợ đối tác",
        summary = "Bảng đối chiếu toàn bộ lịch sử mua bán, thu chi hai chiều và số dư nợ ròng với từng đối tác.",
        steps = listOf(
            ScreenHelpStep(
                title = "Xem tổng kết đối chiếu 2 chiều",
                detail = "Khung trên cùng tổng hợp rõ: Tổng mua, Tổng bán, Đã thu, Đã trả và kết luận ai còn nợ ai bao nhiêu tiền."
            ),
            ScreenHelpStep(
                title = "Ghi thu/trả trực tiếp trên từng đơn",
                detail = "Với các đơn còn nợ, bấm nút 'Thu thêm' hoặc 'Trả thêm' ngay dưới đơn đó để ghi nhận đợt thanh toán mới."
            ),
            ScreenHelpStep(
                title = "Xuất phiếu đối chiếu PDF",
                detail = "Bấm biểu tượng PDF ở góc trên bên phải để xuất file đối chiếu công nợ rõ ràng, gửi cho đối tác kiểm tra."
            )
        ),
        quickTip = "Bạn có thể chạm vào bất kỳ đơn hàng hoặc phiếu thu/chi nào trong danh sách để mở ra xem hoặc chỉnh sửa."
    )

    val CONTACT_LIST = ScreenHelpGuide(
        screenTitle = "Danh bạ Đối tác",
        summary = "Quản lý thông tin liên hệ và tóm tắt tình trạng nợ của tất cả lái buôn, khách mua, người bán.",
        steps = listOf(
            ScreenHelpStep(
                title = "Thêm người liên quan mới",
                detail = "Bấm nút '+ Thêm mới' ở góc phải trên cùng để lưu Họ tên, Số điện thoại, Địa chỉ và Ghi chú."
            ),
            ScreenHelpStep(
                title = "Tìm kiếm thông minh không dấu",
                detail = "Gõ bất kỳ từ nào trong tên hoặc số điện thoại để lọc danh sách tức thì."
            ),
            ScreenHelpStep(
                title = "Xem hồ sơ chi tiết",
                detail = "Chạm vào thẻ đối tác để xem toàn bộ lịch sử giao dịch, sửa thông tin hoặc xuất PDF công nợ của người đó."
            )
        ),
        quickTip = "Trên mỗi thẻ đối tác đều có nhãn màu báo ngay người đó đang nợ mình, mình nợ họ hay đã hết nợ."
    )

    val CONTACT_DETAIL = ScreenHelpGuide(
        screenTitle = "Hồ sơ đối tác",
        summary = "Trang thông tin chi tiết, tổng hợp 6 chỉ số giao dịch và lịch sử đơn hàng của một đối tác.",
        steps = listOf(
            ScreenHelpStep(
                title = "Xem 6 chỉ số tổng hợp",
                detail = "Kiểm tra nhanh Tổng tiền mua, Tổng tiền bán, Đã nhận, Đã chuyển và số nợ còn lại của riêng đối tác này."
            ),
            ScreenHelpStep(
                title = "Sửa thông tin hoặc Xuất PDF",
                detail = "Dùng các biểu tượng ở góc phải trên cùng để xuất báo cáo PDF, chỉnh sửa tên/SĐT/địa chỉ hoặc xóa hồ sơ."
            )
        ),
        quickTip = "Bấm 'Xem chi tiết đối chiếu công nợ' để mở bảng đối chiếu chuyên sâu và ghi thu/trả nợ nhanh."
    )

    val MORE_HUB = ScreenHelpGuide(
        screenTitle = "Sổ phụ & Tiện ích",
        summary = "Trung tâm truy cập nhanh các sổ phụ: Đổi Bath Thái, Thu phí Quota biên giới, Mua bán hàng hóa, Chi tiêu gia đình và Danh bạ.",
        steps = listOf(
            ScreenHelpStep(
                title = "Chọn sổ phụ cần ghi chép",
                detail = "Chạm vào từng ô chức năng để mở sổ tương ứng. Mỗi ô đều hiển thị sẵn số lượng bản ghi hiện có."
            )
        ),
        quickTip = "Bạn cũng có thể mở nhanh các sổ này từ nút 'Thu chi' ở thanh điều hướng dưới cùng hoặc từ 4 lối tắt ở màn Tổng quan."
    )

    val BACKUP_AND_REPORT = ScreenHelpGuide(
        screenTitle = "Báo cáo PDF & Sao lưu",
        summary = "Xuất báo cáo PDF theo kỳ, sao lưu toàn bộ dữ liệu ra file an toàn và cài đặt khóa bảo mật 6 số.",
        steps = listOf(
            ScreenHelpStep(
                title = "Xuất báo cáo PDF theo danh mục & kỳ",
                detail = "Chọn loại báo cáo (Tổng hợp, Mua bán trâu bò, Đổi Bath, Phí Quota, Chi gia đình), chọn kỳ thời gian rồi bấm 'Xuất file PDF'."
            ),
            ScreenHelpStep(
                title = "Sao lưu & Khôi phục dữ liệu",
                detail = "Bấm 'Xuất file sao lưu (.json)' để lưu dữ liệu ra máy/Google Drive. Khi đổi máy mới, chọn 'Khôi phục từ file sao lưu' để nạp lại toàn bộ sổ."
            ),
            ScreenHelpStep(
                title = "Đổi mật khẩu PIN 6 số & Vân tay",
                detail = "Cuộn xuống mục Bảo mật để đổi mã PIN 6 số, bật/tắt mở khóa sinh trắc học hoặc khóa sổ ngay lập tức."
            )
        ),
        quickTip = "Hãy xuất file sao lưu định kỳ mỗi tuần hoặc trước khi gỡ/cài lại ứng dụng để dữ liệu luôn an toàn tuyệt đối."
    )

    fun forLedgerList(title: String): ScreenHelpGuide = when {
        title.contains("Bath", ignoreCase = true) -> ScreenHelpGuide(
            screenTitle = "Sổ Đổi Bath Thái",
            summary = "Theo dõi các giao dịch đổi tiền Việt - Bath Thái (2 chiều: Mình chuyển Bath và Khách chuyển Việt), tự động tính lời/lỗ chênh lệch tỉ giá.",
            steps = listOf(
                ScreenHelpStep(
                    title = "Tạo giao dịch đổi Bath",
                    detail = "Bấm nút '+ Tạo giao dịch' ở góc trên bên phải để mở biểu mẫu nhập Số Bath, Tỉ giá và Số tiền Việt."
                ),
                ScreenHelpStep(
                    title = "Lọc theo chiều giao dịch & thời gian",
                    detail = "Lọc xem riêng chiều 'Chuyển (Mình chuyển Bath)' hoặc 'Nhận (Khách chuyển Việt)' và xem tổng lời/lỗ chênh lệch ngay ở khung tóm tắt."
                ),
                ScreenHelpStep(
                    title = "Ghi thu/chuyển thêm nhiều đợt",
                    detail = "Chạm vào giao dịch đã lưu để chỉnh sửa hoặc bấm 'Ghi thêm đợt khách chuyển tiền' khi khách trả làm nhiều lần."
                )
            ),
            quickTip = "Các khoản khách còn thiếu tiền đổi Bath cũng được tự động tổng hợp sang màn hình Quản lý Công nợ."
        )
        title.contains("Quota", ignoreCase = true) -> ScreenHelpGuide(
            screenTitle = "Sổ Thu phí Quota",
            summary = "Ghi chép các khoản thu phí Quota qua biên giới theo người nộp, số lượng trâu bò và ngày thu.",
            steps = listOf(
                ScreenHelpStep(
                    title = "Ghi phiếu thu phí mới",
                    detail = "Bấm '+ Ghi phí Quota' ở góc trên bên phải, chọn người nộp, nhập số tiền phí và số lượng con."
                ),
                ScreenHelpStep(
                    title = "Xem tổng thu theo tháng/ngày",
                    detail = "Dùng thanh lọc thời gian để xem tổng số phiếu, tổng số con và tổng tiền phí thu được trong kỳ."
                )
            ),
            quickTip = "Chạm vào bất kỳ phiếu thu nào trong danh sách để chỉnh sửa lại số tiền hoặc ghi chú khi cần."
        )
        title.contains("Chi tiêu", ignoreCase = true) -> ScreenHelpGuide(
            screenTitle = "Sổ Chi tiêu gia đình",
            summary = "Ghi lại các khoản chi sinh hoạt hằng ngày để quản lý tách biệt hoặc đối chiếu với dòng tiền chung.",
            steps = listOf(
                ScreenHelpStep(
                    title = "Tạo khoản chi mới",
                    detail = "Bấm '+ Tạo khoản chi', nhập số tiền, lý do chi (tiền chợ, tiền điện...) và người nhận (nếu có)."
                ),
                ScreenHelpStep(
                    title = "Theo dõi tổng chi theo kỳ",
                    detail = "Chọn lọc theo Tháng, Ngày hoặc Hôm nay để biết tổng số tiền đã chi tiêu trong kỳ."
                )
            ),
            quickTip = "Số liệu chi gia đình cũng được tổng hợp tự động vào bảng Thống kê kinh doanh ở màn Tổng quan."
        )
        else -> ScreenHelpGuide(
            screenTitle = title,
            summary = "Quản lý danh sách ghi chép theo thời gian, tự động cộng tổng theo ngày và theo kỳ lọc.",
            steps = listOf(
                ScreenHelpStep(
                    title = "Thêm bản ghi mới",
                    detail = "Bấm nút '+' ở góc trên bên phải để mở biểu mẫu nhập liệu."
                ),
                ScreenHelpStep(
                    title = "Lọc & chỉnh sửa",
                    detail = "Dùng thanh lọc thời gian để tra cứu theo kỳ, hoặc chạm vào từng dòng để mở lại biểu mẫu chỉnh sửa."
                )
            ),
            quickTip = "Mọi ô số tiền đều tích hợp sẵn bàn phím máy tính tự động lưu số ngay khi bạn gõ."
        )
    }

    fun forLedgerForm(title: String): ScreenHelpGuide = when {
        title.contains("Bath", ignoreCase = true) -> ScreenHelpGuide(
            screenTitle = title,
            summary = "Biểu mẫu tính tự động tiền đổi Bath Thái theo 2 chiều giao dịch.",
            steps = listOf(
                ScreenHelpStep(
                    title = "Chọn chiều giao dịch (Chuyển / Nhận)",
                    detail = "• 'Chuyển (Mình chuyển Bath)': Nhập Số Bath và Tỉ giá (hoặc Số tiền Việt), máy tự tính ô còn lại.\n• 'Nhận (Khách chuyển Việt)': Nhập Số Bath, Giá Bath vốn và Giá bán cho khách để máy tự tính Tiền lời chênh lệch và Số tiền khách còn thiếu."
                ),
                ScreenHelpStep(
                    title = "Chọn người giao dịch",
                    detail = "Chọn đối tác từ danh bạ hoặc thêm người mới để theo dõi công nợ đổi Bath chính xác."
                ),
                ScreenHelpStep(
                    title = "Nhập số bằng máy tính tiền",
                    detail = "Chạm vào ô Số Bath, Tỉ giá hoặc Số tiền Việt để mở máy tính. Gõ số đến đâu ô nhập tự cập nhật ngay đến đó."
                )
            ),
            quickTip = "Bạn chỉ cần nhập 2 trong 3 thông số (ví dụ Số Bath + Tỉ giá, hoặc Số Bath + Số tiền Việt), ứng dụng sẽ tự suy ra thông số thứ 3 khi bấm Lưu."
        )
        title.contains("Quota", ignoreCase = true) -> ScreenHelpGuide(
            screenTitle = title,
            summary = "Biểu mẫu ghi nhận khoản thu phí Quota qua biên giới.",
            steps = listOf(
                ScreenHelpStep(
                    title = "Chọn người nộp phí",
                    detail = "Chọn đối tác từ danh bạ hoặc tạo người mới ngay trong hộp chọn."
                ),
                ScreenHelpStep(
                    title = "Nhập số tiền phí & số lượng trâu bò",
                    detail = "Chạm vào ô số tiền để nhập bằng máy tính, sau đó chỉnh số lượng trâu bò (con) và ngày thu."
                )
            ),
            quickTip = "Nút Lưu ở cuối trang sẽ sáng lên ngay khi bạn nhập Số tiền phí Quota lớn hơn 0."
        )
        title.contains("khoản chi", ignoreCase = true) || title.contains("Chi tiêu", ignoreCase = true) -> ScreenHelpGuide(
            screenTitle = title,
            summary = "Biểu mẫu ghi nhận khoản chi tiêu sinh hoạt gia đình.",
            steps = listOf(
                ScreenHelpStep(
                    title = "Nhập số tiền & lý do chi (*)",
                    detail = "Chạm vào ô Số tiền để gõ trên bảng máy tính và nhập Lý do chi (bắt buộc, ví dụ: Tiền chợ, đóng học...)."
                ),
                ScreenHelpStep(
                    title = "Chọn người nhận & ngày chi",
                    detail = "Có thể chọn thêm người nhận tiền từ danh bạ và điều chỉnh ngày chi trước khi bấm Lưu."
                )
            ),
            quickTip = "Khi sửa lại một khoản chi cũ, bạn có thể bấm biểu tượng thùng rác ở góc trên bên phải nếu muốn xóa."
        )
        else -> ScreenHelpGuide(
            screenTitle = title,
            summary = "Biểu mẫu nhập thông tin chi tiết và tự động định dạng số tiền.",
            steps = listOf(
                ScreenHelpStep(
                    title = "Điền các mục có dấu (*)",
                    detail = "Nhập đầy đủ các thông tin bắt buộc và dùng bàn phím máy tính tích hợp cho các ô số tiền."
                ),
                ScreenHelpStep(
                    title = "Lưu hoặc xóa bản ghi",
                    detail = "Bấm nút Lưu màu xanh ở cuối biểu mẫu để hoàn tất."
                )
            ),
            quickTip = "Khi gõ số trên bảng máy tính tiền, số sẽ tự động lưu trực tiếp vào ô nhập ngay khi bạn bấm từng phím."
        )
    }
}
