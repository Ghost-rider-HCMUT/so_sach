# 05. HƯỚNG DẪN THIẾT KẾ GIAO DIỆN VÀ TRẢI NGHIỆM NGƯỜI DÙNG (UI/UX GUIDELINES)

Tài liệu này quy định hệ thống thiết kế (Design System) dựa trên **Material Design 3 (Material You)** nhưng được tinh chỉnh đặc biệt cho đối tượng **chủ hộ kinh doanh trâu bò phổ thông tại Việt Nam**: chữ to, tương phản cao để nhìn rõ ngoài trời nắng, vùng chạm rộng, và 100% tiếng Việt tự nhiên.

---

## 1. Quy tắc Ngôn ngữ: 100% Tiếng Việt Tuyệt Đối

Tuyệt đối không để sót bất kỳ từ tiếng Anh nào trên giao diện người dùng, kể cả các từ viết tắt phổ biến trong lập trình.

| Thuật ngữ tuyệt đối CẤM dùng | Thuật ngữ Tiếng Việt BẮT BUỘC dùng |
|---|---|
| Dashboard / Home | **Tổng quan** |
| Transaction / Cash flow | **Giao dịch** |
| Buy / Purchase / Import | **Mua hàng** |
| Sell / Sale / Export | **Bán hàng** |
| Cash In / Receive / Income | **Nhận tiền** |
| Cash Out / Transfer / Expense | **Chuyển tiền** |
| Debt / Receivable / Payable | **Công nợ** / **Người khác nợ mình** / **Mình nợ người khác** |
| Contact / Customer / Supplier | **Người liên quan** (hoặc **Danh bạ**) / **Người mua** / **Người bán** |
| OK / Cancel / Submit / Save | **Đồng ý** / **Hủy** / **Lưu lại** |
| Filter / Sort / Search | **Bộ lọc** / **Sắp xếp** / **Tìm kiếm** |
| Loading... / Empty / Error | **Đang tải dữ liệu...** / **Chưa có dữ liệu** / **Đã xảy ra lỗi** |
| Backup / Restore / Export PDF | **Sao lưu dữ liệu** / **Khôi phục dữ liệu** / **Xuất báo cáo PDF** |
| Cash / Bank Transfer | **Tiền mặt** / **Chuyển khoản** |

---

## 2. Hệ thống Màu sắc Ngữ nghĩa — Chế độ Tối Chuẩn M3 (Dark Theme Best Practices)

Ứng dụng sử dụng **Chế độ Tối hiện đại (Dark Theme Best Practices)** làm giao diện chủ đạo nhằm:
- Giảm mỏi mắt và tiết kiệm pin tối đa trên màn hình OLED/AMOLED.
- **Tránh nền đen tuyền (`#000000`)** gây hiện tượng nhòe chữ (Halation); sử dụng nền **Slate Dark (`#0B0F17`)** kết hợp các thẻ nổi có độ sáng phân tầng (**Tonal Elevation Surfaces**: `#151C28` -> `#1E293B`).
- Các màu ngữ nghĩa được điều chỉnh độ bão hòa dịu mắt nhưng đạt chuẩn tương phản cao **WCAG AAA** trên nền tối.

**Nguyên tắc vàng:** Không bao giờ chỉ dùng màu sắc để phân biệt; mọi con số và trạng thái luôn phải đi kèm **Nhãn chữ tiếng Việt + Biểu tượng (Icon) + Dấu hoặc Nhãn phụ**.

| Vai trò ngữ nghĩa | Màu sắc (Dark Theme) | Mã màu Chữ/Nhấn | Mã màu nền hộp (Container) | Biểu tượng đi kèm | Áp dụng cho |
|---|---|---|---|---|---|
| **Chủ đạo (Primary)** | **Xanh dương sáng dịu** | `#60A5FA` | `#1E3A5F` | Biểu tượng ứng dụng, Mua/Bán | Thanh tiêu đề, nút chính, tổng quan mua/bán, điều hướng đang chọn. |
| **Tiền thu & Nợ phải thu (Positive / Receivable)** | **Xanh ngọc lục bảo (Emerald)** | `#34D399` | `#064E3B` | Mũi tên hướng vào `↙` / Dấu cộng `+` | **Nhận tiền**, **Tổng tiền đã nhận**, **Người khác còn nợ mình**, trạng thái **Đã thu đủ**. |
| **Tiền chi (Cash Out / Outflow)** | **Đỏ san hô (Coral Red)** | `#F87171` | `#5C1919` | Mũi tên hướng ra `↗` / Dấu trừ `-` | **Chuyển tiền**, **Tổng tiền đã chuyển**, nút Xóa giao dịch. |
| **Nợ phải trả (Payable / Warning)** | **Cam hổ phách (Warm Amber)** | `#FB923C` | `#5A270B` | Biểu tượng cảnh báo công nợ `⏳` | **Mình còn nợ người khác**, số tiền còn phải trả trong đơn mua hàng. |
| **Bề mặt & Chữ (Dark Surfaces)** | **Slate Dark / Off-White** | `#F1F5F9` (Chữ chính)<br>`#94A3B8` (Chữ phụ) | `#0B0F17` (Nền chính)<br>`#151C28` (Thẻ Card) | Biểu tượng lịch, ghi chú, SĐT | Nền màn hình, bề mặt thẻ, ngày tháng, số điện thoại, ghi chú. |

> [!TIP]
> **Độ tương phản ngoài trời (Sunlight Readability):**
> Toàn bộ mã màu chữ chính trên nền sáng đạt tỷ lệ tương phản tối thiểu **7:1 (Chuẩn WCAG AAA)** để chủ hộ đứng ngoài bãi bò dưới trời nắng gắt vẫn đọc rõ từng con số.

---

## 3. Hệ thống Chữ viết (Typography) & Định dạng Dữ liệu

Sử dụng phông chữ hệ thống **Roboto / Noto Sans** hỗ trợ hoàn hảo dấu tiếng Việt, tăng kích thước cơ sở lớn hơn mức mặc định 1–2sp cho người trung niên:

- **Số tiền lớn trên Dashboard / Chi tiết công nợ:** `22sp – 26sp`, **In đậm (`Bold / 700`)**, dùng thuộc tính `fontFeatureSettings = "tnum"` (Tabular Numbers) để các chữ số thẳng cột tuyệt đối khi so sánh trong bảng.
- **Số tiền trong danh sách (List Item):** `17sp – 18sp`, **In đậm (`Bold`)**.
- **Tên người liên quan:** `17sp`, **Bán đậm (`SemiBold / 600`)**, màu đen đậm `#111827`.
- **Tiêu đề màn hình (Top App Bar):** `20sp`, **In đậm (`Bold`)**.
- **Nội dung thường / Nhãn biểu mẫu:** `16sp`, `Regular / Medium`.
- **Thông tin phụ (Ngày tháng, ghi chú phụ):** Tối thiểu `14sp` (không bao giờ dùng chữ nhỏ hơn `13sp`).

### Quy chuẩn Định dạng (Formatting Standards)
1. **Định dạng Tiền tệ:**
   - Luôn phân cách hàng nghìn bằng dấu chấm (`.`) và kết thúc bằng khoảng trắng + `đ`.
   - Ví dụ chuẩn: `30.000.000 đ`, `8.500.000 đ`, `0 đ`.
   - Khi đang gõ trong ô nhập tiền: Tự động thêm dấu chấm phân cách ngay khi người dùng gõ (VisualTransformation) và hiển thị chữ đọc số tiền bằng tiếng Việt ngay bên dưới ô nhập (Ví dụ: *"Ba mươi triệu đồng"*) để chống nhập thừa/thiếu số `0`!
2. **Định dạng Ngày tháng:**
   - Luôn hiển thị dạng `dd/MM/yyyy` (Ví dụ: `14/11/2026`).
   - Có các nút chọn nhanh ngày trên biểu mẫu: **Hôm nay**, **Hôm qua**, hoặc nhấn vào biểu tượng lịch để mở hộp thoại chọn ngày tiếng Việt.
3. **Định dạng Số điện thoại Việt Nam:**
   - Hiển thị tách nhóm dễ đọc: `0912 345 678` hoặc `0987 654 321` (khi tìm kiếm thì tự động bỏ khoảng trắng để khớp cả `0912345678` lẫn `0912 345 678`).

---

## 4. Bố cục, Vùng chạm và Thành phần Giao diện (Layout & Components)

1. **Kích thước vùng chạm (Touch Targets):**
   - Mọi nút bấm, dòng danh sách, thẻ chỉ số, ô chọn đều có chiều cao tối thiểu **52dp – 56dp** (vượt chuẩn 48dp của Android Accessibility) để người dùng có ngón tay lớn thao tác chính xác, không bấm nhầm.
2. **Bàn phím thông minh (Keyboard Optimization):**
   - Ô nhập **Số tiền**: Tích hợp **bàn phím máy tính custom** trực tiếp trong ứng dụng (`+`, `-`, `×`, `÷`, `000`, `00`, `=`, xóa lùi `⌫`, xóa hết `C`), cho phép nhân giá theo số con ngay khi nhập (ví dụ `17.500.000 × 3 = 52.500.000 đ`).
   - Ô nhập **Số điện thoại**: Mở bàn phím điện thoại (`KeyboardType.Phone`) và tự động lọc bỏ chữ cái/ký tự đặc biệt (`sanitizePhoneInput`), chỉ giữ chữ số, dấu `+` đầu và khoảng trắng phân cách.
   - Ô nhập **Họ tên**: Tự động viết hoa chữ cái đầu mỗi từ (`KeyboardCapitalization.Words`).
3. **Trạng thái Giao diện Đầy đủ (Empty, Loading, Error States):**
   - **Trạng thái Trống (Empty State):** Có hình minh họa nhẹ nhàng, câu giải thích rõ bằng tiếng Việt và nút hành động ngay bên dưới.
     - *Ví dụ tại Tab Người khác nợ mình khi không ai nợ:* *"Hiện không có ai đang nợ tiền bạn."*
     - *Ví dụ khi tìm kiếm không có kết quả:* *"Không tìm thấy người nào khớp với 'nguyen van x'. Bạn có muốn thêm người mới không?"* kèm nút **"+ Thêm người mới"**.
   - **Trạng thái Đang tải (Loading State):** Hiển thị khung xương mờ (Skeleton/Shimmer) hoặc vòng xoay kèm chữ *"Đang tải dữ liệu..."*.
   - **Trạng thái Lỗi (Error State):** Hiển thị hộp thông báo tiếng Việt thân thiện, giải thích rõ nguyên nhân và cách khắc phục (Ví dụ: *"Vui lòng chọn người bán trước khi lưu đơn mua hàng"*).

---

## 5. Thiết kế Điều hướng Dưới cùng (Bottom Navigation Bar)

Thanh điều hướng dưới cùng cố định gồm **5 mục trực quan**, giúp chủ hộ chuyển đổi giữa các nghiệp vụ chỉ bằng 1 chạm ngón cái:

| Vị trí | Nhãn hiển thị | Biểu tượng | Vai trò chính |
|---:|---|---|---|
| **1** | **Tổng quan** | 🏠 Ngôi nhà | Màn hình mặc định khi mở app: `Tiền thực có`, `Lời lý thuyết`, `Vốn ròng dự kiến`, 6 chỉ số tài chính, khu vực `Đổi Bath Thái`, Công nợ nổi bật, Thao tác nhanh, Thống kê. |
| **2** | **Mua bán** | 🛒 Giỏ hàng | Sổ thống nhất gồm **Mua hàng**, **Bán hàng**, **Nhận tiền**, **Chuyển tiền**, lọc theo Loại & Thời gian dạng Dropdown. |
| **3** | **Đổi Bath** | 💱 Đổi tiền | Sổ theo dõi giao dịch **Mình chuyển Bath** và **Họ chuyển Bath** (kèm tỷ giá / quy đổi VND). |
| **4** | **Công nợ** | 📒 Sổ công nợ | Chọn **Người khác nợ mình** / **Mình nợ người khác** qua Dropdown, tìm kiếm nhanh không dấu, sắp xếp và xem chi tiết nợ. |
| **5** | **Thêm** | 🎛️ Lưới tiện ích | Hub mở rộng: **Danh bạ đối tác**, **Mua hàng hóa**, **Chi phí gia đình**, **Sao lưu & Báo cáo PDF**. |

> [!NOTE]
> **Nút Cài đặt, Sao lưu & Báo cáo PDF:** Được đặt ở góc trên bên phải của thanh tiêu đề màn hình **Tổng quan** (biểu tượng **Sao lưu / Báo cáo** kèm nhãn rõ ràng) để không làm chật thanh điều hướng dưới cùng nhưng vẫn mở được ngay từ màn hình chính.
