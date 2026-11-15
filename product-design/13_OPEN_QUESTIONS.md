# 13. CÁC CÂU HỎI MỞ & ĐỀ XUẤT QUYẾT ĐỊNH THIẾT KẾ (OPEN QUESTIONS & DECISIONS)

Trong quá trình phân tích nghiệp vụ thực tế của hộ kinh doanh trâu bò, có **5 điểm chi tiết** có thể phát sinh trong quá trình sử dụng hằng ngày. Dưới đây là phương án thiết kế mặc định đã được lựa chọn tối ưu trong bộ tài liệu này, cùng câu hỏi để bạn xác nhận hoặc điều chỉnh trước khi chuyển sang giai đoạn viết code.

---

## 1. Khi khách trả tiền gộp 1 lần cho nhiều chuyến mua/bán còn nợ
- **Tình huống thực tế:** Khách Nguyễn Văn A mua bò của mình 2 chuyến: Chuyến 1 còn nợ `10.000.000 đ`, Chuyến 2 còn nợ `15.000.000 đ` (Tổng nợ: `25.000.000 đ`). Hôm nay khách chuyển khoản 1 lần `18.000.000 đ` để trả nợ.
- **Giải pháp đã thiết kế (Đề xuất mặc định):**
  - Trên biểu mẫu **Nhận tiền** (hoặc **Chuyển tiền**), ngoài việc chọn 1 đơn cụ thể, nếu người đó có từ 2 đơn đang nợ trở lên, hệ thống cung cấp thêm tùy chọn **"Tự động trừ vào các đơn nợ cũ nhất"** (Hệ thống tự động trả hết `10.000.000 đ` cho Chuyến 1 và trừ tiếp `8.000.000 đ` vào Chuyến 2, đảm bảo tổng tiền nhận vẫn là `18.000.000 đ` và không bao giờ vượt quá tổng nợ `25.000.000 đ`).
- **Câu hỏi xác nhận:** Bạn có đồng ý giữ tính năng hỗ trợ trả nợ gộp tự động theo thứ tự đơn cũ trước này không, hay chỉ cần chọn từng đơn một?

## 2. Xử lý khi xóa một đơn Mua hàng / Bán hàng đã có giao dịch trả nợ
- **Tình huống thực tế:** Người dùng lỡ tạo nhầm một đơn Mua hàng `30.000.000 đ` (đã trả ngay `20.000.000 đ`) và muốn xóa đơn này.
- **Giải pháp đã thiết kế (Đề xuất mặc định):**
  - Khi người dùng nhấn Xóa đơn Mua/Bán, ứng dụng hiển thị hộp thoại cảnh báo rõ bằng tiếng Việt: *"Khi xóa đơn này, các khoản tiền thanh toán đã liên kết với đơn (20.000.000 đ) và số dư nợ của đơn cũng sẽ được xóa cùng để tránh sai lệch sổ sách. Bạn có chắc chắn muốn xóa?"*
- **Câu hỏi xác nhận:** Bạn có đồng ý xóa kèm các khoản thanh toán liên kết của đơn bị xóa (sau khi người dùng xác nhận cảnh báo) không?

## 3. Tên hiển thị của Ứng dụng trên màn hình điện thoại
- **Giải pháp đã thiết kế (Đề xuất mặc định):** **"Sổ Trâu Bò"** (hoặc **"Quản Lý Trâu Bò"**).
- **Câu hỏi xác nhận:** Bạn muốn đặt tên hiển thị chính thức của ứng dụng trên màn hình chính điện thoại là gì?

## 4. Thông tin chủ hộ trên phiếu Báo cáo PDF
- **Giải pháp đã thiết kế (Đề xuất mặc định):** Cho phép chủ hộ nhập tùy chọn *Tên hộ kinh doanh / Chủ hộ* và *Số điện thoại* trong phần Cài đặt (Ví dụ: *"Hộ kinh doanh trâu bò Anh Hùng - SĐT: 09xx..."*) để in lên phần đầu trang khi xuất phiếu PDF gửi cho khách.
- **Câu hỏi xác nhận:** Bạn có muốn đưa mục thông tin chủ hộ tùy chọn này vào phần Xuất PDF không?

## 5. Bảo mật mở ứng dụng (Khóa mật khẩu 6 số bắt buộc khi mở app) — ĐÃ CHỐT
- **Quyết định đã chốt:** Áp dụng **Cách 2 — Bắt buộc mật khẩu 6 chữ số**:
  - Lần đầu tiên mở ứng dụng: Bắt buộc thiết lập mật khẩu 6 chữ số (nhập 6 số và xác nhận lại 6 số).
  - Các lần mở ứng dụng tiếp theo: Luôn hiển thị màn hình nhập mật khẩu 6 số trước khi vào màn hình Tổng quan (Dashboard).
  - Hỗ trợ đổi mật khẩu 6 số và nút "Khóa sổ ngay" trong phần Sao lưu & Bảo mật.
