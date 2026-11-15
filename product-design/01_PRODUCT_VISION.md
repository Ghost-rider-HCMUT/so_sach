# 01. TẦM NHÌN SẢN PHẨM (PRODUCT VISION)

## 1. Tổng quan sản phẩm

**Tên sản phẩm đề xuất:** **Sổ Trâu Bò** (Ứng dụng Quản lý Kinh doanh & Công nợ Trâu Bò)  
**Nền tảng:** Android (Offline-first, hoạt động 100% trên thiết bị, không cần kết nối mạng)  
**Ngôn ngữ giao diện:** 100% Tiếng Việt tự nhiên, gần gũi với thương lái và hộ kinh doanh.

### Tuyên bố tầm nhìn (Vision Statement)
> Xây dựng một cuốn **"Sổ tay tài chính và mua bán trâu bò điện tử"** cực kỳ đơn giản, chính xác và trực quan dành riêng cho các hộ kinh doanh, thương lái trâu bò nhỏ lẻ tại Việt Nam. Ngay khi mở ứng dụng, chủ hộ nắm bắt được tức thì: **Đã thu bao nhiêu, đã chi bao nhiêu, mua bán thế nào, ai đang nợ mình và mình đang nợ ai** — loại bỏ hoàn toàn tình trạng thất thoát, nhầm lẫn hoặc quên nợ khi ghi chép bằng sổ giấy truyền thống.

---

## 2. Chân dung người dùng mục tiêu (Target Personas)

### 2.1. Chân dung chính: Chủ hộ / Thương lái trâu bò (Anh Hùng, 38–55 tuổi)
- **Đặc điểm công việc:**
  - Mua trâu bò từ các hộ chăn nuôi nhỏ lẻ hoặc thương lái khác, sau đó bán lại cho lò mổ, thương lái vùng khác hoặc người nuôi vỗ béo.
  - Giao dịch diễn ra tại chuồng trại, bãi tập kết, chợ gia súc; thường xuyên di chuyển, tay có thể bẩn hoặc đang đứng ngoài trời nắng.
  - Giá trị mỗi giao dịch lớn (từ vài chục triệu đến hàng trăm triệu đồng mỗi chuyến/lô).
  - Rất phổ biến hình thức **mua nợ / bán chịu một phần** (đặt cọc hoặc trả trước một phần, hẹn vài ngày/tuần sau thanh toán nốt qua chuyển khoản hoặc tiền mặt).
  - Một đối tác (ví dụ: một thương lái cùng vùng) có thể **vừa bán bò cho mình, vừa mua bò của mình** vào các thời điểm khác nhau.
- **Thói quen sử dụng công nghệ:**
  - Sử dụng điện thoại Android phổ thông hoặc tầm trung.
  - Quen dùng Zalo, ứng dụng ngân hàng để chụp ảnh chuyển khoản, nhưng ngại các phần mềm kế toán nhiều thuật ngữ ("Nợ/Có", "Định khoản", "Bút toán", "SKU", "Khấu hao").
  - Cần chữ to, rõ ràng, nút bấm lớn, màu sắc phân biệt rạch ròi, thao tác ít bước nhất có thể.

### 2.2. Nỗi đau hiện tại (Pain Points)
1. **Ghi sổ giấy dễ nhầm lẫn và thất thoát:** Một người mua/bán nhiều chuyến, trả góp nhiều lần (lần 10 triệu, lần 15 triệu). Khi cộng sổ giấy rất dễ cộng thiếu, cộng trùng hoặc quên ghi.
2. **Không tách bạch được "Mua/Bán" và "Dòng tiền thực tế":** Không rõ tháng này tổng tiền hàng mua vào là bao nhiêu so với tổng tiền mặt/chuyển khoản thực tế đã chi ra.
3. **Rối công nợ hai chiều:** Với bạn buôn vừa mua của mình vừa bán cho mình, sổ giấy thường gạch xóa chồng chéo khiến hai bên dễ tranh cãi.
4. **Mất thời gian tra cứu:** Khi khách gọi điện hỏi *"Tôi còn nợ anh bao nhiêu?"*, phải lật từng trang sổ giấy từ tháng trước để cộng tay.
5. **Thất lạc chứng từ:** Ảnh chụp màn hình chuyển khoản hoặc ảnh chụp con bò/lô bò nằm lẫn lộn trong thư viện ảnh điện thoại, không gắn liền với giao dịch cụ thể.

---

## 3. Giá trị cốt lõi mang lại (Core Value Propositions)

| Giá trị cốt lõi | Giải pháp trong ứng dụng |
|---|---|
| **Nhìn thấy ngay bức tranh tài chính** | Màn hình **Tổng quan (Dashboard)** hiển thị rõ ràng 6 chỉ số tài chính & mua bán cốt lõi cùng danh sách công nợ nổi bật ngay khi mở máy. |
| **Quản lý công nợ chính xác tuyệt đối** | Tự động tính toán số tiền còn nợ sau mỗi lần mua/bán hoặc trả thêm. Tách biệt hoàn toàn **Người khác nợ mình** và **Mình nợ người khác** (không tự ý bù trừ). |
| **Một người — Một hồ sơ duy nhất** | Mỗi đối tác chỉ lưu 1 lần trong danh bạ. Khi giao dịch chỉ cần gõ vài chữ cái (hỗ trợ cả tiếng Việt không dấu) hoặc số điện thoại để chọn lại ngay, hoặc thêm mới trực tiếp trên biểu mẫu. |
| **Tách biệt Nghiệp vụ Mua/Bán và Dòng tiền** | Không bao giờ cộng gộp "Tổng tiền bán hàng" với "Tổng tiền đã nhận". Khi trả nợ thêm cho một chuyến mua/bán cũ, số tiền được liên kết vào chuyến đó để giảm nợ mà không làm sai lệch doanh số mua/bán. |
| **Lưu trữ chứng từ đi kèm giao dịch** | Đính kèm ảnh chụp biên lai chuyển khoản, giấy viết tay hoặc ảnh trâu bò ngay trong từng giao dịch để đối chiếu bất cứ lúc nào. |
| **An toàn, độc lập, 100% Tiếng Việt** | Chạy hoàn toàn offline trên máy, tốc độ phản hồi tức thì, hỗ trợ xuất/nhập tệp sao lưu an toàn, không dùng bất kỳ từ tiếng Anh nào trên giao diện. |

---

## 4. Triết lý thiết kế sản phẩm (Product Principles)

1. **Thực tế kinh doanh là trên hết (Pragmatism over Accounting Theory):**
   - Không dùng khái niệm kế toán kép phức tạp. Dùng đúng ngôn ngữ đời thường của người buôn trâu bò: *"Nhận tiền"*, *"Chuyển tiền"*, *"Mua hàng"*, *"Bán hàng"*, *"Người khác nợ mình"*, *"Mình nợ người khác"*.
2. **Chống ghi trùng và chống sai số (Single Source of Truth):**
   - Mọi số tiền được lưu trữ bằng số nguyên (`Long` — đơn vị đồng), tuyệt đối không có sai số làm tròn.
   - Tiền trả ngay lúc mua/bán và tiền trả thêm sau đó đều được quản lý nhất quán để khi sửa hoặc xóa bất kỳ giao dịch nào, công nợ cũng được tính lại chính xác 100%.
3. **Thao tác trong 10 giây (10-Second Entry):**
   - Bấm nút thao tác nhanh trên Tổng quan -> Chọn người (hoặc thêm nhanh) -> Nhập số tiền bằng bàn phím số có dấu chấm phân cách hàng nghìn tự động -> Bấm Lưu.
4. **Trực quan bằng màu sắc VÀ chữ viết (Accessible Clarity):**
   - Xanh lá cho Tiền thu vào / Phải thu; Đỏ cho Tiền chi ra; Cam cho Khoản mình nợ người ta; Xanh dương cho thông tin tổng quan. Luôn đi kèm nhãn chữ tiếng Việt và biểu tượng rõ ràng để dùng tốt ngay cả dưới ánh nắng gắt ngoài trời.

---

## 5. Thước đo thành công của sản phẩm (Success Metrics)

- **Tốc độ tra cứu công nợ:** Chủ hộ tìm thấy một người và biết chính xác số dư nợ hai chiều + lịch sử trả tiền trong vòng **dưới 3 giây**.
- **Tốc độ nhập liệu:** Tạo xong một giao dịch Nhận tiền / Chuyển tiền / Mua hàng / Bán hàng trong **dưới 15 giây**.
- **Độ chính xác số liệu:** 100% khớp nhau giữa số liệu trên Dashboard, màn hình Công nợ, Hồ sơ người liên quan và Lịch sử giao dịch trong mọi kịch bản thêm/sửa/xóa.
- **Độ thân thiện ngôn ngữ:** 0% từ tiếng Anh xuất hiện trên giao diện, thông báo lỗi, bộ lọc, biểu đồ và báo cáo.
