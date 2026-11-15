# 02. PHẠM VI SẢN PHẨM VÀ DANH MỤC CHỨC NĂNG (SCOPE & FEATURES)

## 1. Phạm vi triển khai (In-Scope Features)

Ứng dụng tập trung giải quyết trọn vẹn bài toán quản lý dòng tiền, hoạt động mua bán trâu bò, theo dõi công nợ hai chiều và bảo mật sổ sách bằng mật khẩu 6 số cho hộ kinh doanh nhỏ.

### 1.0. Bảo mật Khóa Mật khẩu 6 số & Sinh trắc học (6-Digit PIN & Biometric Lock)
- **Bắt buộc ngay từ lần đầu mở ứng dụng:**
  - Lần đầu tiên mở ứng dụng (chưa có mật khẩu): Hiển thị màn hình **Thiết lập mật khẩu 6 số** (nhập 6 chữ số lần 1 -> nhập lại 6 chữ số lần 2 để xác nhận).
  - Các lần mở ứng dụng tiếp theo: Hiển thị màn hình **Nhập mật khẩu mở sổ (6 chữ số)** bằng bàn phím số lớn, đồng thời hỗ trợ **Mở khóa bằng vân tay / khuôn mặt (`BiometricPrompt`, tùy phần cứng từng máy)** nếu đã kích hoạt trên thiết bị và bật trong cài đặt bảo mật.
- **Đổi mật khẩu 6 số, Bật/tắt sinh trắc học & Khóa sổ ngay:**
  - Cho phép chủ hộ đổi mật khẩu 6 số (cần nhập đúng mật khẩu 6 số hiện tại), bật/tắt mở khóa vân tay/khuôn mặt, hoặc bấm **"Khóa sổ ngay"** trước khi đưa điện thoại cho người khác mượn.
- **Lưu trữ bảo mật phần cứng:**
  - Mật khẩu 6 số được băm một chiều `SHA-256` kèm `Salt` ngẫu nhiên 16 bytes và mã hóa bằng khóa phần cứng `AES-256-GCM` trong **Android Keystore** (hoạt động 100% offline, không cần tài khoản Cloud).

### 1.1. Màn hình Tổng quan (Dashboard)
- **Thao tác nhanh (Quick Actions):** 4 nút bấm lớn đặt ngay đầu màn hình: **Nhận tiền**, **Chuyển tiền**, **Mua hàng**, **Bán hàng**.
- **Khu vực 1. MUA BÁN TRÂU BÒ (Dòng tiền, Vốn & Lời lý thuyết):**
  - 3 chỉ số tổng hợp nổi bật (phân màu Xanh lá / Đỏ theo trạng thái dương/âm):
    1. **Tiền thực có** (`Tổng đã nhận - Tổng đã chuyển`).
    2. **Lời lý thuyết** (`Tổng bán hàng - Tổng mua hàng`).
    3. **Vốn ròng dự kiến** (Tổng hợp dòng tiền thực + công nợ phải thu - công nợ phải trả).
  - 6 chỉ số tách biệt:
    1. Tổng tiền đã nhận (Dòng tiền thu thực tế).
    2. Tổng tiền đã chuyển (Dòng tiền chi thực tế).
    3. Tổng tiền mua hàng (Tổng giá trị các đợt mua trâu bò).
    4. Tổng tiền bán hàng (Tổng giá trị các đợt bán trâu bò).
    5. Tổng tiền người khác còn nợ mình (Phải thu).
    6. Tổng tiền mình còn nợ người khác (Phải trả).
  - *Tương tác:* Nhấn vào bất kỳ thẻ chỉ số nào sẽ mở thẳng màn hình danh sách/chi tiết tương ứng.
- **Khu vực 2. ĐỔI BATH THÁI:**
  - Hiển thị tổng **Mình chuyển Bath** (`฿` & quy đổi `đ`) và **Họ chuyển Bath** (`฿` & quy đổi `đ`), chạm để mở thẳng tab **Đổi Bath**.
- **Khu vực Công nợ nổi bật (Ưu tiên hiển thị):**
  - Chia thành 2 bảng/thẻ rõ ràng: **Người khác còn nợ mình** (Xanh lá) và **Mình còn nợ người khác** (Cam).
  - Hiển thị tổng số tiền nợ, tổng số người đang nợ, danh sách top những người nợ nhiều nhất (tên + số tiền) và nút **"Xem tất cả"**.
- **Khu vực Thống kê kinh doanh:**
  - Bộ lọc thời gian dạng **Dropdown** gọn gàng: *Hôm nay, Tuần này, Tháng này, Năm nay, Tất cả*.
  - Hiển thị 4 chỉ số trong kỳ (Tổng tiền mua hàng, Tổng tiền bán hàng, Tổng tiền nhận, Tổng tiền chuyển) kèm biểu đồ cột đơn giản, chú thích 100% tiếng Việt.

### 1.2. Quản lý Nhận tiền (Thu tiền thực tế)
- Ghi nhận các khoản tiền thực tế thu vào (tiền khách trả nợ mua bò, tiền thu lẻ, tiền cọc...).
- Trường thông tin:
  - Người đưa tiền (Chọn từ danh bạ hoặc thêm mới ngay).
  - Số tiền nhận (`Long`, > 0).
  - Ngày nhận (Mặc định hôm nay, định dạng `dd/MM/yyyy`).
  - Hình thức nhận: **Tiền mặt** hoặc **Chuyển khoản**.
  - Giao dịch bán hàng liên quan (Tùy chọn: Liên kết với một đơn bán hàng mà người đó đang còn nợ để trừ nợ cho đơn đó; hoặc để trống nếu là khoản nhận tiền độc lập).
  - Nội dung ngắn (Ví dụ: *"Trả thêm tiền bò chuyến 10/11"*).
  - Ghi chú chi tiết.
  - Ảnh chứng từ đính kèm (Chụp ảnh mới hoặc chọn từ thư viện, tối đa 5 ảnh).
- Cho phép xem chi tiết, chỉnh sửa và xóa giao dịch (tự động tính lại công nợ khi sửa/xóa).

### 1.3. Quản lý Chuyển tiền (Chi tiền thực tế)
- Ghi nhận các khoản tiền thực tế chi ra (trả nợ tiền mua bò cho người bán, chi trả cọc, chi phí khác...).
- Trường thông tin:
  - Người nhận tiền (Chọn từ danh bạ hoặc thêm mới ngay).
  - Số tiền chuyển (`Long`, > 0).
  - Ngày chuyển (Mặc định hôm nay, định dạng `dd/MM/yyyy`).
  - Hình thức chuyển: **Tiền mặt** hoặc **Chuyển khoản**.
  - Giao dịch mua hàng liên quan (Tùy chọn: Liên kết với một đơn mua hàng mà mình đang còn nợ người đó để trừ nợ; hoặc để trống nếu là khoản chuyển tiền độc lập).
  - Nội dung ngắn.
  - Ghi chú chi tiết.
  - Ảnh chứng từ đính kèm.
- Cho phép xem chi tiết, chỉnh sửa và xóa giao dịch (tự động tính lại công nợ khi sửa/xóa).

### 1.4. Quản lý Mua hàng (Mua trâu bò)
- Ghi nhận mỗi lần/chuyến mua trâu bò từ người bán.
- Trường thông tin:
  - Người bán (Chọn từ danh bạ hoặc thêm mới ngay).
  - Ngày mua (`dd/MM/yyyy`).
  - Số lượng (Số con trâu/bò, số nguyên dương, mặc định 1).
  - Mô tả trâu/bò hoặc lô hàng (Ví dụ: *"3 bò đực BBB + 2 bò cái vàng"*).
  - Tổng tiền mua (`Long`, > 0).
  - Số tiền trả ngay lúc mua (`Long`, >= 0 và <= Tổng tiền mua) + Hình thức trả ngay (**Tiền mặt** / **Chuyển khoản**).
  - Số tiền còn nợ (Tự động tính = `Tổng tiền mua - Tổng đã trả`).
  - Ghi chú.
  - Ảnh đính kèm (Ảnh trâu bò, giấy tờ mua bán).
- Theo dõi danh sách các lần trả tiền tiếp theo gắn với đơn mua này.

### 1.5. Quản lý Bán hàng (Bán trâu bò)
- Ghi nhận mỗi lần/chuyến bán trâu bò cho khách.
- Trường thông tin:
  - Người mua (Chọn từ danh bạ hoặc thêm mới ngay).
  - Ngày bán (`dd/MM/yyyy`).
  - Số lượng (Số con trâu/bò, số nguyên dương, mặc định 1).
  - Mô tả trâu/bò hoặc lô hàng.
  - Tổng tiền bán (`Long`, > 0).
  - Số tiền nhận ngay lúc bán (`Long`, >= 0 và <= Tổng tiền bán) + Hình thức nhận ngay (**Tiền mặt** / **Chuyển khoản**).
  - Số tiền khách còn nợ (Tự động tính = `Tổng tiền bán - Tổng đã nhận`).
  - Ghi chú.
  - Ảnh đính kèm.
- Theo dõi danh sách các lần khách trả thêm gắn với đơn bán này.

### 1.6. Quản lý Công nợ chi tiết
- **Màn hình Công nợ 2 Tab:**
  - **Tab 1: Người khác nợ mình** (Chỉ liệt kê những người có tổng dư nợ phải thu > 0).
  - **Tab 2: Mình nợ người khác** (Chỉ liệt kê những người có tổng dư nợ phải trả > 0).
- **Tìm kiếm và Sắp xếp công nợ:**
  - Tìm kiếm tức thì theo tên (có dấu và không dấu) hoặc số điện thoại.
  - Sắp xếp theo: *Số tiền nợ từ cao xuống thấp*, *Số tiền nợ từ thấp lên cao*, *Giao dịch gần nhất*, *Giao dịch cũ nhất*.
- **Màn hình Chi tiết công nợ của một người:**
  - Tách biệt hoàn toàn 2 khu vực nếu người đó có công nợ hai chiều (1. Người đó còn nợ mình; 2. Mình còn nợ người đó).
  - Hiển thị Tổng nợ ban đầu, Đã thanh toán, Còn nợ hiện tại, danh sách các đơn Mua/Bán còn nợ hoặc đã tất toán, danh sách các lần Nhận/Chuyển tiền liên kết, và nút thao tác nhanh **"Thu nợ"** / **"Trả nợ"** ngay trên màn hình chi tiết.

### 1.7. Quản lý Người liên quan (Danh bạ đối tác)
- Một hồ sơ duy nhất cho mỗi người (dù vừa mua vừa bán).
- Thông tin: Họ tên (bắt buộc), Số điện thoại, Địa chỉ, Ghi chú.
- Thống kê tổng hợp trong hồ sơ mỗi người:
  1. Tổng tiền mua (mình mua của họ).
  2. Tổng tiền bán (mình bán cho họ).
  3. Tổng tiền đã nhận (từ họ).
  4. Tổng tiền đã chuyển (cho họ).
  5. Tổng tiền người đó còn nợ mình.
  6. Tổng tiền mình còn nợ người đó.
- Lịch sử giao dịch mua/bán và lịch sử nhận/chuyển tiền của riêng người đó.
- Bộ chọn người thông minh (Bottom Sheet / Dialog) có tìm kiếm không dấu và tạo mới nhanh ngay trong luồng nhập giao dịch.

### 1.8. Đổi Bath Thái, Lịch sử Mua bán thống nhất, Báo cáo PDF & Sao lưu / Khôi phục
- **Sổ Đổi Bath Thái (Tab riêng):** Ghi nhận 2 chiều **Mình chuyển Bath** (`VND -> THB`) và **Họ chuyển Bath** (`THB -> VND`), hỗ trợ nhập có hoặc không có tỷ giá quy đổi VND, lọc theo chiều và thời gian dạng Dropdown.
- **Sổ Mua Bán & Thu Chi thống nhất:** Gộp chung toàn bộ **Mua hàng**, **Bán hàng**, **Nhận tiền**, **Chuyển tiền** trong một danh sách gom nhóm theo ngày, lọc theo Loại & Thời gian bằng Dropdown.
- **Báo cáo & Xuất PDF:** Chọn xuất báo cáo PDF theo **Loại** (*Buôn bán hàng trâu bò*, *Đổi Bath Thái*, hoặc *Tất cả*) và theo **Thời gian** (*Theo ngày*, *Theo tháng*, *Theo năm*, *Tất cả*) để gửi qua Zalo hoặc in ấn.
- **Sao lưu & Khôi phục dữ liệu (Backup & Restore):**
  - Đóng gói toàn bộ cơ sở dữ liệu và ảnh đính kèm ra tệp sao lưu (`.zip` chứa JSON/SQLite + thư mục ảnh) lưu vào máy hoặc chia sẻ lưu trữ.
  - Kiểm tra tính toàn vẹn của tệp trước khi khôi phục và hiển thị cảnh báo rõ ràng trước khi ghi đè dữ liệu hiện tại.

---

## 2. Những chức năng KHÔNG triển khai (Out-of-Scope / Non-Goals)

Để giữ cho ứng dụng gọn nhẹ, dễ dùng đúng chất công cụ cho hộ kinh doanh nhỏ, các tính năng sau **tuyệt đối không đưa vào thiết kế và cài đặt**:

| Nhóm không triển khai | Chi tiết các tính năng loại bỏ | Lý do loại bỏ |
|---|---|---|
| **Quản lý kho phức tạp** | Mã SKU, mã vạch (Barcode/QR), quản lý tồn kho theo từng cá thể trâu bò, tính giá vốn hàng bán (FIFO/Bình quân gia quyền), theo dõi cân nặng tăng trưởng từng ngày. | Thương lái mua bán theo chuyến/lô hoặc theo con tự mô tả; quản lý tồn kho SKU làm phức tạp hóa việc nhập liệu gấp nhiều lần. |
| **Kế toán chuyên nghiệp** | Kế toán kép (Nợ/Có), bảng cân đối kế toán, báo cáo lưu chuyển tiền tệ chuẩn VAS, khấu hao tài sản, phân bổ chi phí. | Người dùng phổ thông không hiểu nghiệp vụ kế toán, dễ nhập sai. |
| **Thuế & Hóa đơn** | Tính thuế GTGT/TNCN, kết nối hóa đơn điện tử, chữ ký số. | Hộ kinh doanh cá thể mua bán trâu bò trực tiếp không sử dụng hóa đơn điện tử trong sổ tay cá nhân. |
| **Đa người dùng & Chi nhánh** | Quản lý nhân viên, phân quyền (Admin/Staff), chấm công, tính lương, quản lý nhiều chi nhánh/cửa hàng, đăng nhập tài khoản/mật khẩu Cloud. | Ứng dụng dành riêng cho chủ hộ sử dụng trên thiết bị cá nhân, hoạt động 100% offline. |
| **CRM phức tạp** | Phễu bán hàng (Sales Pipeline), chăm sóc khách hàng tự động, gửi SMS/Email marketing, phân hạng thẻ thành viên. | Chỉ cần danh bạ người liên quan đơn giản gắn với lịch sử mua bán và công nợ. |
| **Quản lý đa ví / Chuyển quỹ nội bộ** | Quản lý số dư từng tài khoản ngân hàng riêng biệt, nghiệp vụ rút tiền mặt nhập quỹ hoặc chuyển tiền giữa các ví nội bộ. | Chủ hộ chỉ cần biết giao dịch đó trả bằng *Tiền mặt* hay *Chuyển khoản*, không cần đối chiếu số dư từng thẻ ngân hàng trong app. |
| **Module thu/trả nợ tách rời độc lập** | Tạo màn hình "Thu hồi công nợ" hoặc "Thanh toán công nợ" có cơ chế lưu trữ tách biệt với "Nhận tiền" và "Chuyển tiền". | Mọi khoản thu nợ bản chất là **Nhận tiền** (có liên kết với đơn Bán hàng), mọi khoản trả nợ bản chất là **Chuyển tiền** (có liên kết với đơn Mua hàng). Gộp chung giúp dữ liệu đồng nhất, không bao giờ bị trùng lặp. |
| **Tự động bù trừ công nợ 2 chiều** | Tự động lấy khoản người ta nợ mình trừ đi khoản mình nợ người ta. | Trong buôn bán trâu bò, chuyến nào ra chuyến đó (hoặc bên mua/bán tính riêng). Việc tự động cấn trừ làm mất dấu vết các đơn chưa thanh toán. |

---

## 3. Ma trận ánh xạ Nhóm chức năng — Màn hình điều hướng

Để tối ưu trải nghiệm trên điện thoại Android, thanh điều hướng dưới cùng (Bottom Navigation) gồm 5 mục chính:

1. **Tổng quan:** Dashboard (1. Mua bán trâu bò với Tiền thực có / Lời lý thuyết / Vốn ròng dự kiến; 2. Đổi Bath Thái; Công nợ nổi bật; Thao tác nhanh; Thống kê) + Lối vào Sao lưu/Khôi phục & Xuất báo cáo PDF.
2. **Mua bán:** Sổ thống nhất cho cả hoạt động thương mại (**Mua hàng**, **Bán hàng**) và dòng tiền (**Nhận tiền**, **Chuyển tiền**), lọc theo Loại và Thời gian dạng Dropdown.
3. **Đổi Bath:** Sổ theo dõi giao dịch **Mình chuyển Bath** và **Họ chuyển Bath** kèm quy đổi VND.
4. **Công nợ:** Quản lý chuyên sâu **Người khác nợ mình** và **Mình nợ người khác** (chọn qua Dropdown), tìm kiếm nhanh không dấu, sắp xếp và đi vào Chi tiết công nợ từng người.
5. **Thêm:** Hub mở rộng gồm **Danh bạ người liên quan**, **Chi phí gia đình**, **Mua hàng hóa**, và **Sao lưu & Báo cáo PDF**.
