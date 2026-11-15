# 10. THIẾT KẾ BÁO CÁO, THỐNG KÊ VÀ XUẤT TỆP PDF (REPORTING)

Tài liệu này mô tả hệ thống thống kê kinh doanh, lọc dữ liệu theo thời gian và tính năng xuất báo cáo PDF 100% tiếng Việt để chủ hộ đối chiếu hoặc gửi cho đối tác qua Zalo.

---

## 1. Các Bộ lọc Thời gian Chuẩn (Time Period Filters)

Người dùng có thể xem thống kê trên Dashboard hoặc trong màn hình Báo cáo theo 5 mốc thời gian:

1. **Hôm nay:** Từ `00:00:00` đến `23:59:59` của ngày hiện tại.
2. **Tuần này:** Từ Thứ Hai đầu tuần đến Chủ Nhật cuối tuần hiện tại (theo chuẩn lịch Việt Nam, tuần bắt đầu từ Thứ Hai).
3. **Tháng này:** Từ ngày `01` đến ngày cuối cùng của tháng hiện tại.
4. **Năm nay:** Từ ngày `01/01/yyyy` đến `31/12/yyyy`.
5. **Khoảng thời gian tùy chọn:** Người dùng chọn **Từ ngày (`dd/MM/yyyy`)** đến **Đến ngày (`dd/MM/yyyy`)** qua hộp thoại chọn ngày tiếng Việt.

---

## 2. Các Chỉ số Báo cáo Tổng hợp

Trong khoảng thời gian được chọn $[T_{start}, T_{end}]$:

| Chỉ số báo cáo | Cách tính trong kỳ $[T_{start}, T_{end}]$ | Màu sắc hiển thị |
|---|---|---|
| **1. Tổng tiền đã nhận** | Tổng `amount` của các giao dịch `CASH_IN` có `transaction_date` nằm trong kỳ. | Xanh lá (`#1B873F`) |
| **2. Tổng tiền đã chuyển** | Tổng `amount` của các giao dịch `CASH_OUT` có `transaction_date` nằm trong kỳ. | Đỏ (`#C62828`) |
| **3. Tổng tiền mua hàng** | Tổng `total_amount` của các đơn `PURCHASE` có `transaction_date` nằm trong kỳ (kèm tổng số con trâu/bò đã mua). | Xanh dương (`#1565C0`) |
| **4. Tổng tiền bán hàng** | Tổng `total_amount` của các đơn `SALE` có `transaction_date` nằm trong kỳ (kèm tổng số con trâu/bò đã bán). | Xanh dương đậm (`#0D47A1`) |
| **5. Người khác còn nợ mình** | Tổng dư nợ phải thu tính đến thời điểm hiện tại (kèm danh sách chi tiết từng người đang nợ). | Xanh lá (`#1B873F`) |
| **6. Mình còn nợ người khác** | Tổng dư nợ phải trả tính đến thời điểm hiện tại (kèm danh sách chi tiết từng người mình nợ). | Cam (`#E65100`) |

---

## 3. Thiết kế Biểu đồ Trực quan (Simple Visual Charts)

Sử dụng biểu đồ cột (Bar Chart) vẽ trực tiếp bằng Jetpack Compose `Canvas` (nhẹ, mượt, không phụ thuộc thư viện bên ngoài nặng nề):
- **Không dùng quá 4 màu sắc.**
- Có nhãn số tiền rút gọn dễ đọc ngay trên đầu mỗi cột (Ví dụ: `184 tr`, `228 tr`, `185 tr`, `142 tr`).
- Chú thích dưới chân cột 100% tiếng Việt: **Mua hàng**, **Bán hàng**, **Tiền nhận**, **Tiền chuyển**.
- Khi chạm vào một cột, hiển thị số tiền đầy đủ chính xác đến từng đồng (Ví dụ: `228.000.000 đ`).

---

## 4. Xuất Báo cáo PDF Tiếng Việt (PDF Generation)

Ứng dụng sử dụng `android.graphics.pdf.PdfDocument` kết hợp phông chữ tiếng Việt nhúng sẵn (`Roboto-Regular.ttf` / `Roboto-Bold.ttf`) để tạo tệp PDF sắc nét, chuẩn khổ A4, hỗ trợ 2 mẫu báo cáo thực tế nhất:

### Mẫu PDF 1: Phiếu Đối Chiếu Công Nợ & Lịch Sử Giao Dịch Theo Người Liên Quan
Dùng để gửi trực tiếp qua Zalo cho khách mua hoặc người bán khi cần đối chiếu công nợ:

```text
=====================================================================
                  PHIẾU ĐỐI CHIẾU CÔNG NỢ & GIAO DỊCH
                      Ngày xuất báo cáo: 14/11/2026
=====================================================================
Họ và tên đối tác : Nguyễn Văn A
Số điện thoại     : 0912 345 678
Địa chỉ           : Xã Tân Lập, Huyện Đan Phượng
---------------------------------------------------------------------
I. TỔNG HỢP CÔNG NỢ HIỆN TẠI (TÁCH BIỆT 2 CHIỀU)
  1. Nguyễn Văn A còn nợ lại (Phải thu) : 20.000.000 đ
     (Tổng bán: 50.000.000 đ  -  Đã nhận: 30.000.000 đ)
  2. Mình còn nợ Nguyễn Văn A (Phải trả): 15.000.000 đ
     (Tổng mua: 35.000.000 đ  -  Đã trả:  20.000.000 đ)
---------------------------------------------------------------------
II. CHI TIẾT CÁC ĐƠN MUA / BÁN TRÂU BÒ
Ngày       | Loại     | Mô tả lô hàng      | Tổng tiền    | Đã TT       | Còn nợ
10/11/2026 | Bán hàng | 2 bò thịt BBB      | 50.000.000 đ | 30.000.000 đ| 20.000.000 đ
05/11/2026 | Mua hàng | 1 trâu đực lớn     | 35.000.000 đ | 20.000.000 đ| 15.000.000 đ
---------------------------------------------------------------------
III. CHI TIẾT CÁC LẦN NHẬN TIỀN / CHUYỂN TIỀN
Ngày       | Loại giao dịch | Hình thức    | Nội dung                | Số tiền
14/11/2026 | Nhận tiền      | Chuyển khoản | Trả thêm tiền bò 10/11  | +10.000.000 đ
10/11/2026 | Nhận tiền      | Tiền mặt     | Nhận ngay lúc bán bò    | +20.000.000 đ
05/11/2026 | Chuyển tiền    | Chuyển khoản | Trả ngay lúc mua trâu   | -20.000.000 đ
=====================================================================
```

### Mẫu PDF 2: Báo Cáo Quản Lý Theo Danh Mục & Thời Gian (Cập nhật mới nhất)
Trong màn hình **Sao lưu & Báo cáo PDF**, người dùng chọn **Loại báo cáo** (Dropdown) và **Thời gian** (Dropdown + bộ chọn ngày/tháng/năm):
1. **Loại báo cáo (`PdfReportCategory`):**
   - **Buôn bán hàng trâu bò (`CATTLE_TRADING`):** Tổng hợp tiền mua/bán (kèm tổng số con), tiền đã nhận/chuyển, danh sách công nợ phải thu & phải trả.
   - **Đổi Bath Thái (`BATH_EXCHANGE`):** Tổng hợp `Mình chuyển Bath` (`฿` & quy đổi `đ`), `Họ chuyển Bath` (`฿` & quy đổi `đ`), và bảng kê chi tiết từng giao dịch Bath (hỗ trợ cả trường hợp có và không có tỷ giá).
   - **Tất cả (`ALL`):** Gộp cả Phần I (Buôn bán hàng trâu bò & Công nợ) và Phần II (Đổi Bath Thái) trong cùng một tệp PDF.
2. **Phạm vi thời gian (`TimeFilterMode`):**
   - Hỗ trợ xuất **Theo ngày** (mặc định hôm nay), **Theo tháng**, **Theo năm**, hoặc **Tất cả**.
