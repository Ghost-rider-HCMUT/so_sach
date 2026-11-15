# 11. KỊCH BẢN KIỂM THỬ BẮT BUỘC (MANDATORY TEST CASES)

Tài liệu này định nghĩa chi tiết **15 kịch bản kiểm thử toàn diện** (bao gồm đầy đủ 13 nhóm kịch bản bắt buộc và 2 kịch bản an toàn biên) để xác minh tính chính xác tuyệt đối của nghiệp vụ tài chính, công nợ hai chiều, tìm kiếm tiếng Việt và giao diện.

---

## Danh mục Kịch bản Kiểm thử

### TC-01: Mua hàng trả một phần (Partial Payment on Purchase)
- **Điều kiện ban đầu:** Cơ sở dữ liệu trống. Tạo người bán `Phạm Văn D`.
- **Các bước thực hiện:**
  1. Mở biểu mẫu **Mua hàng**.
  2. Chọn người bán: `Phạm Văn D`.
  3. Nhập số lượng: `2 con`, Mô tả: `"2 bò lai Sind"`.
  4. Nhập Tổng tiền mua: `30.000.000 đ`.
  5. Nhập Số tiền đã trả ngay: `20.000.000 đ` (Hình thức: `Tiền mặt`).
  6. Nhấn **Lưu đơn mua hàng**.
- **Kết quả kỳ vọng:**
  - Đơn mua hiển thị: Tổng tiền mua = `30.000.000 đ`, Đã trả = `20.000.000 đ`, Còn nợ = `10.000.000 đ`.
  - Trên Dashboard:
    - Tổng tiền mua hàng = `30.000.000 đ`.
    - Tổng tiền đã chuyển = `20.000.000 đ`.
    - Mình còn nợ người khác = `10.000.000 đ` (Phạm Văn D: `10.000.000 đ`).

---

### TC-02: Mua hàng trả nhiều lần (Multiple Subsequent Payments on Purchase)
- **Điều kiện ban đầu:** Tiếp nối kết quả của **TC-01** (Đơn mua của `Phạm Văn D` tổng `30.000.000 đ`, đã trả `20.000.000 đ`, còn nợ `10.000.000 đ`).
- **Các bước thực hiện:**
  1. Mở biểu mẫu **Chuyển tiền**.
  2. Chọn người nhận: `Phạm Văn D`.
  3. Chọn liên kết với đơn mua `"2 bò lai Sind"` ngày hôm nay (đang nợ `10.000.000 đ`).
  4. Nhập số tiền chuyển: `5.000.000 đ` (Hình thức: `Chuyển khoản`).
  5. Nhấn **Lưu giao dịch**.
- **Kết quả kỳ vọng:**
  - **Không tạo ra đơn mua hàng mới.**
  - Đơn mua ban đầu cập nhật: Tổng tiền mua = `30.000.000 đ`, Đã trả = `25.000.000 đ`, Còn nợ = `5.000.000 đ`.
  - Trên Dashboard:
    - Tổng tiền mua hàng giữ nguyên = `30.000.000 đ`.
    - Tổng tiền đã chuyển tăng lên = `25.000.000 đ`.
    - Mình còn nợ người khác giảm còn = `5.000.000 đ`.

---

### TC-03: Bán hàng thu một phần (Partial Receipt on Sale)
- **Điều kiện ban đầu:** Tạo người mua `Nguyễn Văn A`.
- **Các bước thực hiện:**
  1. Mở biểu mẫu **Bán hàng**.
  2. Chọn người mua: `Nguyễn Văn A`.
  3. Nhập số lượng: `2 con`, Mô tả: `"2 bò thịt BBB"`.
  4. Nhập Tổng tiền bán: `50.000.000 đ`.
  5. Nhập Đã nhận ngay: `30.000.000 đ` (Hình thức: `Chuyển khoản`).
  6. Nhấn **Lưu đơn bán hàng**.
- **Kết quả kỳ vọng:**
  - Đơn bán hiển thị: Tổng tiền bán = `50.000.000 đ`, Đã nhận = `30.000.000 đ`, Khách còn nợ = `20.000.000 đ`.
  - Trên Dashboard:
    - Tổng tiền bán hàng = `50.000.000 đ`.
    - Tổng tiền đã nhận = `30.000.000 đ` (**Không cộng gộp với 50.000.000 đ**).
    - Người khác còn nợ mình = `20.000.000 đ` (Nguyễn Văn A: `20.000.000 đ`).

---

### TC-04: Bán hàng thu nhiều lần (Multiple Subsequent Receipts on Sale)
- **Điều kiện ban đầu:** Tiếp nối kết quả của **TC-03** (`Nguyễn Văn A` còn nợ `20.000.000 đ` ở đơn bán `50.000.000 đ`).
- **Các bước thực hiện:**
  1. Mở biểu mẫu **Nhận tiền**.
  2. Chọn người đưa tiền: `Nguyễn Văn A`.
  3. Chọn liên kết với đơn bán `"2 bò thịt BBB"` (khách còn nợ `20.000.000 đ`).
  4. Nhập số tiền nhận: `10.000.000 đ`.
  5. Nhấn **Lưu giao dịch**.
- **Kết quả kỳ vọng:**
  - **Không tạo đơn bán hàng mới.**
  - Đơn bán cập nhật: Tổng tiền bán = `50.000.000 đ`, Đã nhận = `40.000.000 đ`, Khách còn nợ = `10.000.000 đ`.
  - Trên Dashboard:
    - Tổng tiền bán hàng giữ nguyên = `50.000.000 đ`.
    - Tổng tiền đã nhận tăng lên = `40.000.000 đ`.
    - Người khác còn nợ mình giảm còn = `10.000.000 đ`.

---

### TC-05: Một người có công nợ hai chiều (Bidirectional Debt Without Offsetting)
- **Điều kiện ban đầu:** `Nguyễn Văn A` đang nợ mình `10.000.000 đ` (từ **TC-04**).
- **Các bước thực hiện:**
  1. Tạo thêm một đơn **Mua hàng** từ chính `Nguyễn Văn A` (không tạo hồ sơ mới, chọn lại `Nguyễn Văn A` đã có):
     - Tổng tiền mua: `35.000.000 đ`.
     - Đã trả ngay: `20.000.000 đ`.
     - Mình còn nợ lại `Nguyễn Văn A`: `15.000.000 đ`.
  2. Mở màn hình **Công nợ** (kiểm tra cả 2 Tab) và nhấn vào `Nguyễn Văn A` để xem **Chi tiết công nợ**.
- **Kết quả kỳ vọng:**
  - **Tuyệt đối không tự động bù trừ** (`15.000.000 - 10.000.000 = 5.000.000` là SAI).
  - Ở Tab 1 (*Người khác nợ mình*): Hiển thị `Nguyễn Văn A` nợ `10.000.000 đ`.
  - Ở Tab 2 (*Mình nợ người khác*): Hiển thị `Nguyễn Văn A` với số tiền mình nợ `15.000.000 đ`.
  - Trong màn hình **Chi tiết công nợ của Nguyễn Văn A**: Hiển thị rõ 2 phần tách biệt:
    - Phần 1: Nguyễn Văn A còn nợ mình = `10.000.000 đ`.
    - Phần 2: Mình còn nợ Nguyễn Văn A = `15.000.000 đ`.

---

### TC-06: Giao dịch Nhận tiền độc lập (Standalone Cash In)
- **Điều kiện ban đầu:** Đang có dữ liệu từ các bước trên.
- **Các bước thực hiện:**
  1. Mở biểu mẫu **Nhận tiền**, chọn `Nguyễn Văn A`.
  2. Chọn mục liên kết: **"Không liên kết — Nhận tiền độc lập"**.
  3. Nhập số tiền nhận: `2.000.000 đ`, Nội dung: `"Tiền gửi nhờ cước xe chở bò"`.
  4. Nhấn **Lưu giao dịch**.
- **Kết quả kỳ vọng:**
  - **Tổng tiền đã nhận** trên Dashboard và trong hồ sơ `Nguyễn Văn A` tăng thêm `2.000.000 đ`.
  - **Tổng tiền bán hàng** không đổi.
  - Số tiền `Nguyễn Văn A` còn nợ mình ở đơn bán bò vẫn giữ nguyên là `10.000.000 đ`.

---

### TC-07: Giao dịch Chuyển tiền độc lập (Standalone Cash Out)
- **Điều kiện ban đầu:** Đang có dữ liệu từ các bước trên.
- **Các bước thực hiện:**
  1. Mở biểu mẫu **Chuyển tiền**, chọn `Phạm Văn D`.
  2. Chọn mục liên kết: **"Không liên kết — Chuyển tiền độc lập"**.
  3. Nhập số tiền chuyển: `1.500.000 đ`, Nội dung: `"Tiền hỗ trợ thuốc thú y"`.
  4. Nhấn **Lưu giao dịch**.
- **Kết quả kỳ vọng:**
  - **Tổng tiền đã chuyển** trên Dashboard và trong hồ sơ `Phạm Văn D` tăng thêm `1.500.000 đ`.
  - **Tổng tiền mua hàng** không đổi.
  - Số tiền mình còn nợ `Phạm Văn D` ở đơn mua bò vẫn giữ nguyên là `5.000.000 đ`.

---

### TC-08: Tìm kiếm người có dấu, không dấu và số điện thoại (Vietnamese Accent-Insensitive Search)
- **Điều kiện ban đầu:** Trong danh bạ và danh sách công nợ có `"Nguyễn Văn A"` (`0912 345 678`), `"Trần Văn Đạt"` (`0988 776 655`), `"Lê Thị Ánh"`.
- **Các bước thực hiện:**
  1. Tại ô tìm kiếm màn hình Công nợ (và ô tìm kiếm chọn người):
     - Nhập `"nguyen van a"` (chữ thường, không dấu).
     - Nhập `"NGUYỄN VĂN A"` (chữ hoa, có dấu).
     - Nhập `"tran van dat"` (kiểm tra chữ `Đ` -> `d`).
     - Nhập `"0912345"` (tìm theo số điện thoại viết liền).
     - Nhập `"hoang van xyz"` (từ khóa không tồn tại).
- **Kết quả kỳ vọng:**
  - Kết quả lọc cập nhật ngay lập tức khi gõ từng ký tự.
  - `"nguyen van a"` và `"NGUYỄN VĂN A"` đều trả về đúng `"Nguyễn Văn A"`.
  - `"tran van dat"` trả về đúng `"Trần Văn Đạt"`.
  - `"0912345"` trả về đúng `"Nguyễn Văn A"`.
  - `"hoang van xyz"` hiển thị thông báo tiếng Việt: *"Không tìm thấy người nào phù hợp với từ khóa 'hoang van xyz'"*.

---

### TC-09: Chọn lại người đã lưu khi tạo giao dịch (Re-selecting Existing Contact)
- **Các bước thực hiện:**
  1. Mở biểu mẫu **Bán hàng** -> Nhấn vào ô **"Người mua"**.
  2. Gõ `"nguyen"` vào ô tìm kiếm trong bảng chọn người.
  3. Chạm vào `"Nguyễn Văn A"` trong danh sách kết quả.
- **Kết quả kỳ vọng:**
  - Bảng chọn đóng lại, thông tin `"Nguyễn Văn A (0912 345 678)"` được điền vào biểu mẫu Bán hàng.
  - Không tạo thêm bản ghi trùng lặp của `"Nguyễn Văn A"` trong bảng `contacts`.

---

### TC-10: Tạo người mới ngay trong biểu mẫu giao dịch (Inline New Contact Creation)
- **Các bước thực hiện:**
  1. Mở biểu mẫu **Mua hàng** -> Nhấn vào ô **"Người bán"**.
  2. Gõ `"Hoàng Văn E"` vào ô tìm kiếm -> Hệ thống báo chưa có và hiện nút **"+ Thêm người mới ('Hoàng Văn E')"**.
  3. Nhấn nút **"+ Thêm người mới"**, nhập Số điện thoại `0933 998 877`, nhấn **"Lưu & Chọn người này"**.
- **Kết quả kỳ vọng:**
  - Hồ sơ `"Hoàng Văn E"` được lưu vào bảng `contacts`.
  - Biểu mẫu Mua hàng tự động chọn sẵn `"Hoàng Văn E (0933 998 877)"` làm Người bán mà không làm mất các thông tin khác người dùng đã nhập trên biểu mẫu.

---

### TC-11: Thanh toán hết nợ và kiểm tra danh sách công nợ (Full Debt Settlement)
- **Điều kiện ban đầu:** `Phạm Văn D` chỉ có 1 đơn mua còn nợ `5.000.000 đ` (từ **TC-02**).
- **Các bước thực hiện:**
  1. Tạo giao dịch **Chuyển tiền** cho `Phạm Văn D`, liên kết với đơn mua trên, nhập số tiền `5.000.000 đ` (trả hết nợ).
  2. Mở màn hình **Công nợ** -> Tab 2 (*Mình nợ người khác*).
  3. Mở màn hình **Danh bạ (Người liên quan)** và **Lịch sử giao dịch**.
- **Kết quả kỳ vọng:**
  - Số tiền còn nợ của đơn mua về `0 đ` (Trạng thái: *Đã trả đủ*).
  - `Phạm Văn D` **không còn xuất hiện** trong danh sách đang nợ mặc định ở Tab 2 màn hình Công nợ và bảng Công nợ nổi bật trên Dashboard.
  - Hồ sơ `Phạm Văn D` và toàn bộ lịch sử đơn mua `30.000.000 đ` + 3 lần chuyển tiền (`20tr`, `5tr`, `5tr`) **vẫn còn nguyên vẹn** trong Danh bạ và Lịch sử giao dịch.

---

### TC-12: Sửa hoặc Xóa giao dịch và kiểm tra tính lại số dư (Edit/Delete Recalculation)
- **Điều kiện ban đầu:** Tiếp nối **TC-11** (Đơn mua của `Phạm Văn D` đã trả đủ `30.000.000 đ`, nợ = `0 đ`).
- **Các bước thực hiện:**
  1. Mở chi tiết khoản **Chuyển tiền** `5.000.000 đ` cuối cùng của `Phạm Văn D`.
  2. Thực hiện **Sửa** số tiền từ `5.000.000 đ` thành `2.000.000 đ` -> Lưu lại.
  3. Kiểm tra số dư nợ của `Phạm Văn D` và Dashboard.
  4. Tiếp tục **Xóa** hẳn khoản Chuyển tiền `2.000.000 đ` đó.
- **Kết quả kỳ vọng:**
  - Sau bước 2 (Sửa `5tr` -> `2tr`): Đã trả của đơn mua = `27.000.000 đ`, Còn nợ = `3.000.000 đ`. `Phạm Văn D` tự động xuất hiện trở lại trong Tab *"Mình nợ người khác"* với số nợ `3.000.000 đ`.
  - Sau bước 4 (Xóa khoản `2tr`): Đã trả của đơn mua = `25.000.000 đ`, Còn nợ = `5.000.000 đ`. Tổng tiền đã chuyển trên Dashboard giảm đúng `2.000.000 đ`.

---

### TC-13: Kiểm tra đối chiếu số liệu Dashboard với dữ liệu giao dịch thực tế (Dashboard Reconciliation)
- **Các bước thực hiện:**
  1. Cộng tổng thủ công toàn bộ các bản ghi trong cơ sở dữ liệu sau chuỗi thao tác từ TC-01 đến TC-12.
  2. Đối chiếu với 6 thẻ chỉ số trên **Dashboard**, 2 bảng **Công nợ nổi bật**, và khu vực **Thống kê kinh doanh**.
- **Kết quả kỳ vọng:**
  - 6 chỉ số trên Dashboard khớp chính xác từng đồng (`100%`) với công thức tại `03_BUSINESS_RULES.md`:
    - `Tổng tiền đã nhận` = Tổng mọi bản ghi `CASH_IN`.
    - `Tổng tiền đã chuyển` = Tổng mọi bản ghi `CASH_OUT`.
    - `Tổng tiền mua hàng` = Tổng `total_amount` mọi bản ghi `PURCHASE`.
    - `Tổng tiền bán hàng` = Tổng `total_amount` mọi bản ghi `SALE`.
    - `Người khác còn nợ mình` = Tổng dư nợ các đơn `SALE`.
    - `Mình còn nợ người khác` = Tổng dư nợ các đơn `PURCHASE`.

---

### TC-14: Kiểm thử chặn thanh toán vượt số dư còn nợ (Overpayment Prevention)
- **Điều kiện ban đầu:** Đơn bán của `Nguyễn Văn A` đang còn nợ `10.000.000 đ`.
- **Các bước thực hiện:**
  1. Mở biểu mẫu **Nhận tiền**, chọn `Nguyễn Văn A` và liên kết với đơn bán đang nợ `10.000.000 đ`.
  2. Nhập số tiền nhận: `12.000.000 đ` (lớn hơn `10.000.000 đ`).
  3. Nhấn **Lưu giao dịch**.
- **Kết quả kỳ vọng:**
  - Hệ thống chặn lưu và hiển thị thông báo lỗi bằng tiếng Việt: *"Đơn bán ngày ... chỉ còn nợ 10.000.000 đ. Không thể nhập số tiền trả nợ lớn hơn số tiền còn nợ."*

---

### TC-15: Kiểm thử Sao lưu và Khôi phục dữ liệu (Backup & Restore Integrity)
- **Các bước thực hiện:**
  1. Thực hiện **Sao lưu dữ liệu** ra tệp `.zip`.
  2. Xóa một vài giao dịch hoặc thêm giao dịch nháp.
  3. Chọn **Khôi phục dữ liệu** từ tệp `.zip` vừa xuất -> Kiểm tra hộp thoại cảnh báo ghi đè -> Xác nhận khôi phục.
  4. Thử chọn một tệp `.zip` bất kỳ không phải của ứng dụng để khôi phục.
- **Kết quả kỳ vọng:**
  - Khi khôi phục tệp hợp lệ: Hiển thị cảnh báo ghi đè rõ ràng; sau khi đồng ý, toàn bộ 6 chỉ số Dashboard, danh sách người liên quan, công nợ 2 chiều và ảnh chứng từ quay về đúng trạng thái lúc sao lưu.
  - Khi chọn tệp lạ/hỏng: Hệ thống từ chối khôi phục kèm thông báo tiếng Việt rõ ràng và giữ nguyên dữ liệu hiện tại.

---

### TC-16: Kiểm thử Thiết lập, Mở khóa và Đổi Mật khẩu 6 Số bắt buộc khi mở ứng dụng (6-Digit PIN Lock)
- **Các bước thực hiện:**
  1. Mở ứng dụng lần đầu tiên -> Kiểm tra màn hình bắt buộc **"Thiết lập mật khẩu 6 số"**.
  2. Nhập `123456` ở bước 1, sau đó cố tình nhập sai `654321` ở bước xác nhận -> Hệ thống báo không khớp và yêu cầu đặt lại.
  3. Nhập `123456` ở bước 1 và xác nhận đúng `123456` ở bước 2 -> Lưu thành công và mở vào màn hình Tổng quan.
  4. Khóa ứng dụng (hoặc mở lại ứng dụng) -> Nhập sai `000000` -> Hệ thống chặn và báo lỗi tiếng Việt *"Mật khẩu 6 số không đúng. Vui lòng nhập lại."* -> Nhập đúng `123456` -> Mở khóa thành công.
  5. Vào mục **Đổi mật khẩu 6 số** -> Nhập mật khẩu cũ `123456` và mật khẩu mới `888999` -> Xác nhận mật khẩu cũ không còn hiệu lực và mật khẩu mới `888999` hoạt động chính xác.
- **Kết quả kỳ vọng:**
  - Không thể truy cập vào dữ liệu bên trong ứng dụng nếu chưa thiết lập hoặc chưa nhập đúng mật khẩu 6 chữ số.
  - Mật khẩu được băm `SHA-256` kèm `Salt` và mã hóa an toàn.

---

### TC-17: Mở khóa Sinh trắc học (Vân tay / Khuôn mặt) & Lọc đầu vào Số điện thoại
- **Các bước thực hiện:**
  1. Thiết lập xong PIN 6 số, kiểm tra công tắc **"Mở khóa vân tay / khuôn mặt"** trong mục **3. BẢO MẬT MẬT KHẨU 6 SỐ**.
  2. Khóa sổ ngay -> Xác nhận nút **"Mở khóa bằng vân tay / khuôn mặt"** hiển thị (khi máy đã cài đặt sinh trắc học) và cho phép bấm **"Dùng mã PIN"** để quay lại nhập 6 số bất kỳ lúc nào.
  3. Mở hộp thoại thêm/sửa người liên quan, dán chuỗi `"abc0912-345.678!@#"` vào ô **Số điện thoại**.
- **Kết quả kỳ vọng:**
  - Hệ thống mở khóa thành công khi xác thực vân tay/khuôn mặt hợp lệ, và giữ nguyên cơ chế fallback về PIN 6 số.
  - Ô **Số điện thoại** tự động lọc sạch chữ cái/ký tự đặc biệt thành `"0912345678"`.

---

### TC-18: Stress Test 125+ bản ghi & Xuất PDF theo Danh mục / Theo ngày
- **Các bước thực hiện:**
  1. Nạp 125+ bản ghi thật (25 đối tác, 40 đơn mua/bán trâu bò, 30 thu/chi, 30 đổi Bath Thái).
  2. Kiểm tra cuộn và lọc Dropdown trên Tổng quan, Mua bán, Đổi Bath, Công nợ.
  3. Xuất 3 tệp PDF: *Buôn bán hàng trâu bò*, *Đổi Bath Thái*, và *Tất cả theo ngày*.
- **Kết quả kỳ vọng:**
  - Giao diện hiển thị số tiền hàng trăm triệu đến hơn 1 tỷ không tràn chữ, không crash.
  - Cả 3 tệp PDF xuất ra đúng số liệu, bố cục A4 rõ nét, không cắt xén dòng.
