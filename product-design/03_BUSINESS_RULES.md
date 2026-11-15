# 03. QUY TẮC NGHIỆP VỤ TÀI CHÍNH VÀ CÔNG NỢ (BUSINESS RULES)

Tài liệu này định nghĩa các quy tắc nghiệp vụ cốt lõi, công thức tính toán và ràng buộc toàn vẹn dữ liệu. Mọi tầng xử lý (Database, Repository, ViewModel, UI) đều phải tuân thủ tuyệt đối các quy tắc này.

---

## 1. Nguyên tắc nền tảng: Tách biệt "Mua/Bán" và "Dòng tiền"

Trong ứng dụng này, dữ liệu kinh doanh được chia làm **2 nhóm nghiệp vụ độc lập nhưng có liên kết chặt chẽ**:

1. **Nghiệp vụ Mua bán (Trade Orders):**
   - Bao gồm **Mua hàng** (`PURCHASE`) và **Bán hàng** (`SALE`).
   - Phản ánh giá trị hàng hóa (trâu bò) giao dịch và nghĩa vụ thanh toán phát sinh từ chuyến hàng đó.
2. **Nghiệp vụ Dòng tiền thực tế (Cash Flow Transactions):**
   - Bao gồm **Nhận tiền** (`CASH_IN`) và **Chuyển tiền** (`CASH_OUT`).
   - Phản ánh số tiền thực tế chảy vào hoặc chảy ra khỏi túi chủ hộ (bằng Tiền mặt hoặc Chuyển khoản).

### Quy tắc bất biến số 1 (Rule BR-01): Không cộng gộp Doanh số và Dòng tiền
- **Tổng tiền bán hàng** $\neq$ **Tổng tiền đã nhận**. Tuyệt đối không cộng hai con số này lại với nhau.
- **Tổng tiền mua hàng** $\neq$ **Tổng tiền đã chuyển**. Tuyệt đối không cộng hai con số này lại với nhau.
- Một giao dịch **Nhận tiền** không mặc định là doanh thu bán hàng (có thể là khách trả nợ cũ, hoặc nhận tiền cọc/tiền khác).
- Một giao dịch **Chuyển tiền** không mặc định là một lần mua hàng (có thể là trả nợ tiền mua bò chuyến trước, hoặc chi tiền khác).

---

## 2. Cơ chế Ghi nhận Mua/Bán và Thanh toán (Chống ghi trùng 100%)

Để đảm bảo **không bao giờ ghi nhận trùng tiền** và **sửa/xóa giao dịch luôn tính lại công nợ chính xác**, hệ thống áp dụng quy tắc liên kết giữa Đơn Mua/Bán và Giao dịch Nhận/Chuyển tiền như sau:

### 2.1. Quy tắc khi Mua hàng (Rule BR-02)
Giả sử người dùng tạo một đơn **Mua hàng** từ người bán $P$:
- **Tổng tiền mua:** $M_{mua}$ (Ví dụ: `30.000.000 đ`).
- **Số tiền trả ngay lúc mua:** $T_{ngay}$ (Ví dụ: `20.000.000 đ`, với $0 \le T_{ngay} \le M_{mua}$).
- **Cách hệ thống lưu trữ và tính toán:**
  1. Lưu bản ghi đơn **Mua hàng** với `total_amount = 30.000.000`.
  2. Nếu $T_{ngay} > 0$ (ví dụ `20.000.000`), hệ thống **tự động tạo kèm một bản ghi Chuyển tiền (`CASH_OUT`)** trị giá `20.000.000 đ` được gắn cờ `is_initial_payment = true` và liên kết trực tiếp (`trade_order_id`) tới đơn Mua hàng vừa tạo.
  3. Tổng số tiền **Đã trả** của đơn Mua hàng này luôn được tính bằng tổng các khoản **Chuyển tiền** liên kết với đơn đó:
     $$\text{Đã trả của đơn mua} = \sum \text{Số tiền các giao dịch Chuyển tiền liên kết với đơn mua}$$
  4. Số tiền **Còn nợ** của đơn Mua hàng:
     $$\text{Còn nợ của đơn mua} = \text{Tổng tiền mua} - \text{Đã trả của đơn mua}$$
     *(Trong ví dụ: $30.000.000 - 20.000.000 = 10.000.000\text{ đ}$)*.

> [!IMPORTANT]
> **Tại sao thiết kế khoản trả ngay lúc mua thành một bản ghi Chuyển tiền liên kết (`is_initial_payment = true`)?**
> - Khi người dùng mua bò 30 triệu và trả ngay 20 triệu, thực tế họ đã chi ra 20 triệu. Nhờ bản ghi Chuyển tiền liên kết này:
>   - Chỉ số **"Tổng tiền mua hàng"** tăng 30 triệu.
>   - Chỉ số **"Tổng tiền đã chuyển"** tăng đúng 20 triệu.
>   - Chỉ số **"Mình còn nợ người khác"** tăng đúng 10 triệu.
> - Mọi khoản trả tiền (dù trả ngay lúc mua hay trả thêm vài ngày sau) đều nằm chung trong bảng Dòng tiền (`cash_transactions`) có gắn `trade_order_id`. Không bao giờ xảy ra lỗi cộng sót hay cộng trùng giữa "tiền trả ban đầu" và "tiền chuyển thêm"!

### 2.2. Quy tắc khi Trả thêm tiền cho đơn Mua hàng (Rule BR-03)
Tiếp tục ví dụ trên (Đơn mua 30 triệu, đã trả 20 triệu, còn nợ 10 triệu):
- Vài ngày sau, người dùng tạo giao dịch **Chuyển tiền** `5.000.000 đ` cho người bán $P$ và chọn liên kết với đơn Mua hàng trên:
  - **Tổng tiền mua** của đơn hàng: Vẫn giữ nguyên là `30.000.000 đ` (không tạo đơn mua mới).
  - **Đã trả** của đơn hàng: $20.000.000 + 5.000.000 = 25.000.000\text{ đ}$.
  - **Còn nợ** của đơn hàng: $30.000.000 - 25.000.000 = 5.000.000\text{ đ}$.
  - Trên Dashboard: **Tổng tiền đã chuyển** tăng thêm `5.000.000 đ`; **Tổng tiền mình còn nợ người khác** giảm đi `5.000.000 đ`.

### 2.3. Quy tắc khi Bán hàng (Rule BR-04)
Giả sử người dùng tạo một đơn **Bán hàng** cho người mua $C$:
- **Tổng tiền bán:** $M_{ban}$ (Ví dụ: `50.000.000 đ`).
- **Số tiền nhận ngay lúc bán:** $N_{ngay}$ (Ví dụ: `30.000.000 đ`, với $0 \le N_{ngay} \le M_{ban}$).
- **Cách hệ thống lưu trữ và tính toán:**
  1. Lưu bản ghi đơn **Bán hàng** với `total_amount = 50.000.000`.
  2. Nếu $N_{ngay} > 0$ (ví dụ `30.000.000`), hệ thống **tự động tạo kèm một bản ghi Nhận tiền (`CASH_IN`)** trị giá `30.000.000 đ` gắn cờ `is_initial_payment = true` và liên kết (`trade_order_id`) tới đơn Bán hàng đó.
  3. Tổng số tiền **Đã nhận** của đơn Bán hàng:
     $$\text{Đã nhận của đơn bán} = \sum \text{Số tiền các giao dịch Nhận tiền liên kết với đơn bán}$$
  4. Số tiền **Khách còn nợ** của đơn Bán hàng:
     $$\text{Khách còn nợ của đơn bán} = \text{Tổng tiền bán} - \text{Đã nhận của đơn bán}$$
     *(Trong ví dụ: $50.000.000 - 30.000.000 = 20.000.000\text{ đ}$)*.

### 2.4. Quy tắc khi Khách trả thêm tiền cho đơn Bán hàng (Rule BR-05)
Tiếp tục ví dụ trên (Đơn bán 50 triệu, đã nhận 30 triệu, khách còn nợ 20 triệu):
- Sau đó khách trả thêm `10.000.000 đ` (người dùng tạo giao dịch **Nhận tiền** `10.000.000 đ` từ khách $C$ và liên kết với đơn Bán hàng trên):
  - **Tổng tiền bán** của đơn hàng: Vẫn là `50.000.000 đ` (không tạo đơn bán mới).
  - **Đã nhận** của đơn hàng: $30.000.000 + 10.000.000 = 40.000.000\text{ đ}$.
  - **Khách còn nợ** của đơn hàng: $50.000.000 - 40.000.000 = 10.000.000\text{ đ}$.
  - Trên Dashboard: **Tổng tiền đã nhận** tăng thêm `10.000.000 đ`; **Tổng tiền người khác còn nợ mình** giảm đi `10.000.000 đ`.

---

## 3. Quy tắc Giao dịch Nhận tiền / Chuyển tiền độc lập (Rule BR-06)

Người dùng có thể tạo một giao dịch **Nhận tiền** hoặc **Chuyển tiền** mà **không chọn liên kết với bất kỳ đơn Mua/Bán cụ thể nào** (`trade_order_id = NULL`).

- **Trường hợp sử dụng thực tế:**
  - Nhận/chuyển các khoản tiền không gắn với một lô bò cụ thể (ví dụ: tiền hỗ trợ vận chuyển, tiền gửi nhờ, hoặc ghi nhận dòng tiền thu/chi chung với người đó).
- **Tác động lên các chỉ số:**
  - Giao dịch **Nhận tiền độc lập** làm tăng **Tổng tiền đã nhận** (của toàn app và trong hồ sơ người liên quan).
  - Giao dịch **Chuyển tiền độc lập** làm tăng **Tổng tiền đã chuyển** (của toàn app và trong hồ sơ người liên quan).
  - **Không làm thay đổi số dư nợ của các đơn Mua/Bán hiện có.**
  - *Gợi ý thông minh trên UI (UX Safeguard):* Khi người dùng chọn một Người liên quan đang có đơn Bán hàng còn nợ (khi Nhận tiền) hoặc đang có đơn Mua hàng còn nợ (khi Chuyển tiền), ứng dụng sẽ hiển thị thông báo gợi ý nổi bật: *"Người này đang có X đơn còn nợ (Tổng: Y đ). Bạn có muốn trừ vào khoản nợ này không?"* kèm danh sách chọn nhanh đơn nợ để tránh việc người dùng quên chọn đơn liên kết khi thu/trả nợ! Nếu người dùng có nhiều đơn nợ và muốn trả gộp 1 lần cho nhiều đơn, hệ thống hỗ trợ tùy chọn **"Tự động trừ vào các đơn nợ cũ nhất"** (phân bổ tự động vào các đơn đang nợ theo thứ tự thời gian từ cũ đến mới) hoặc chọn cụ thể từng đơn.

---

## 4. Quy tắc Công nợ hai chiều (Rule BR-07)

Một người liên quan $X$ chỉ có **duy nhất 1 hồ sơ** trong danh bạ, nhưng có thể đồng thời vừa mua trâu bò của mình, vừa bán trâu bò cho mình.

### 4.1. Công thức tính công nợ của một người $X$
1. **Số tiền người đó còn nợ mình (Phải thu từ $X$):**
   $$\text{Nợ phải thu}(X) = \sum_{i \in \text{Đơn Bán cho } X} \left( \text{Tổng tiền bán}_i - \text{Đã nhận của đơn bán}_i \right)$$
2. **Số tiền mình còn nợ người đó (Phải trả cho $X$):**
   $$\text{Nợ phải trả}(X) = \sum_{j \in \text{Đơn Mua từ } X} \left( \text{Tổng tiền mua}_j - \text{Đã trả của đơn mua}_j \right)$$

### 4.2. Cấm tự động bù trừ (No Auto-Offsetting)
- Nếu Ông Nguyễn Văn A đang nợ mình `20.000.000 đ` (do mua bò của mình) và mình đang nợ Ông Nguyễn Văn A `15.000.000 đ` (do mình mua bò của ông ấy):
  - Hệ thống **PHẢI giữ nguyên cả hai con số**:
    - Người đó còn nợ mình: `20.000.000 đ`.
    - Mình còn nợ người đó: `15.000.000 đ`.
  - **TUYỆT ĐỐI KHÔNG** tự động trừ nhau thành "Ông A nợ mình 5.000.000 đ".
  - Ông Nguyễn Văn A sẽ xuất hiện ở **cả 2 Tab** trong màn hình Công nợ:
    - Xuất hiện ở Tab *"Người khác nợ mình"* với số tiền `20.000.000 đ`.
    - Xuất hiện ở Tab *"Mình nợ người khác"* với số tiền `15.000.000 đ`.
  - Trong màn hình **Chi tiết công nợ của Ông Nguyễn Văn A**, giao diện hiển thị 2 khối (Card/Section) tách biệt hoàn toàn:
    1. **Khối 1 (Màu xanh lá): Ông Nguyễn Văn A còn nợ mình: 20.000.000 đ** (kèm danh sách các đơn bán và các lần đã thu).
    2. **Khối 2 (Màu cam): Mình còn nợ Ông Nguyễn Văn A: 15.000.000 đ** (kèm danh sách các đơn mua và các lần đã trả).

---

## 5. Quy tắc Ràng buộc Thanh toán vượt số dư nợ (Rule BR-08)

- **Khi tạo/sửa đơn Mua hàng:**
  - Số tiền `Đã trả ngay` không được lớn hơn `Tổng tiền mua` ($0 \le \text{Đã trả} \le \text{Tổng tiền mua}$). Nếu người dùng nhập lớn hơn, hiển thị lỗi tiếng Việt ngay dưới ô nhập: *"Số tiền đã trả không được vượt quá tổng tiền mua (XX.XXX.XXX đ)"*.
- **Khi tạo/sửa đơn Bán hàng:**
  - Số tiền `Đã nhận ngay` không được lớn hơn `Tổng tiền bán` ($0 \le \text{Đã nhận} \le \text{Tổng tiền bán}$). Nếu lớn hơn, báo lỗi: *"Số tiền đã nhận không được vượt quá tổng tiền bán (XX.XXX.XXX đ)"*.
- **Khi tạo/sửa giao dịch Nhận tiền liên kết với một đơn Bán hàng cụ thể:**
  - Số tiền nhận không được vượt quá **Số tiền khách còn nợ hiện tại** của đơn Bán hàng đó.
  - Nếu người dùng nhập số tiền lớn hơn số dư còn nợ của đơn đó, hệ thống chặn lưu và hiển thị thông báo rõ ràng:
    > *"Đơn bán ngày dd/MM/yyyy chỉ còn nợ **10.000.000 đ**. Không thể nhập số tiền trả nợ lớn hơn số tiền còn nợ."*
- **Khi tạo/sửa giao dịch Chuyển tiền liên kết với một đơn Mua hàng cụ thể:**
  - Số tiền chuyển không được vượt quá **Số tiền mình còn nợ hiện tại** của đơn Mua hàng đó.
  - Nếu vượt quá, hệ thống chặn lưu và hiển thị thông báo:
    > *"Đơn mua ngày dd/MM/yyyy chỉ còn nợ **5.000.000 đ**. Không thể nhập số tiền trả nợ lớn hơn số tiền còn nợ."*

---

## 6. Quy tắc Sửa và Xóa giao dịch (Rule BR-09)

Mọi thao tác sửa hoặc xóa đều phải thực hiện trong một **Giao dịch cơ sở dữ liệu nguyên tử (`@Transaction`)** và đảm bảo tính lại công nợ tức thì thông qua `Flow`:

### 6.1. Sửa hoặc Xóa giao dịch Nhận tiền / Chuyển tiền
- **Khi xóa một khoản Nhận tiền (đã liên kết với đơn Bán hàng):**
  - Khoản Nhận tiền bị xóa khỏi lịch sử dòng tiền -> **Tổng tiền đã nhận** giảm tương ứng -> Số tiền **Đã nhận** của đơn Bán giảm đi -> Số tiền **Khách còn nợ** của đơn Bán tự động tăng trở lại -> Nếu đơn Bán trước đó đã trả hết nợ (còn nợ = 0) thì nay có dư nợ > 0 nên người đó tự động xuất hiện trở lại trong danh sách **Người khác nợ mình**.
- **Khi sửa số tiền của một khoản Nhận/Chuyển tiền:**
  - Hệ thống kiểm tra ràng buộc: Tổng các khoản thanh toán của đơn liên kết (sau khi thay đổi số tiền mới) không được vượt quá `Tổng tiền mua/bán` của đơn đó. Nếu hợp lệ, cập nhật bản ghi và mọi số dư công nợ tự động cập nhật theo.

### 6.2. Sửa hoặc Xóa đơn Mua hàng / Bán hàng
- **Khi sửa `Tổng tiền mua` hoặc `Tổng tiền bán` của một đơn hàng:**
  - Kiểm tra ràng buộc: `Tổng tiền mua/bán mới` không được nhỏ hơn `Tổng số tiền đã thanh toán` (tổng các lần trả ngay + trả thêm đã liên kết với đơn đó).
  - Nếu nhỏ hơn, hiển thị thông báo: *"Tổng tiền đơn hàng không thể nhỏ hơn số tiền đã thanh toán (XX.XXX.XXX đ). Vui lòng chỉnh sửa hoặc xóa các lần thanh toán liên kết trước."*
- **Khi xóa một đơn Mua hàng / Bán hàng:**
  - Hiển thị hộp thoại xác nhận bằng tiếng Việt rõ ràng:
    - Nếu đơn hàng chỉ có khoản thanh toán ban đầu: *"Khi xóa đơn mua/bán này, khoản tiền thanh toán ban đầu (XX.XXX.XXX đ) và công nợ liên quan của đơn này cũng sẽ bị xóa. Bạn có chắc chắn muốn xóa?"*
    - Nếu đơn hàng đã có các lần trả nợ thêm sau đó: Cảnh báo rõ số lượng giao dịch Nhận/Chuyển tiền trả nợ liên kết và cho phép xác nhận xóa toàn bộ chuỗi giao dịch của đơn đó để không bao giờ để lại giao dịch trả nợ "mồ côi".

### 6.3. Quy tắc Bảo vệ Hồ sơ Người liên quan (Rule BR-10)
- Khi một người đã thanh toán hết toàn bộ nợ ($\text{Còn nợ} = 0$):
  - **Không xóa** hồ sơ người đó và **không xóa** lịch sử các đơn hàng/giao dịch cũ.
  - Người đó chỉ tự động ẩn khỏi danh sách mặc định trên màn hình **Công nợ** (vì màn hình Công nợ mặc định chỉ hiển thị người có số dư nợ $> 0$).
  - Người đó vẫn hiển thị đầy đủ trong màn hình **Danh bạ (Người liên quan)** và **Lịch sử giao dịch**, cũng như có thể xem lại các đơn đã tất toán trong chi tiết hồ sơ của họ.
- Không cho phép xóa một Người liên quan nếu người đó đã có giao dịch Mua/Bán hoặc Nhận/Chuyển tiền trong lịch sử (trừ khi người dùng đã xóa hết các giao dịch của người đó trước), nhằm bảo toàn lịch sử tài chính.

---

## 7. Bảng tổng hợp Công thức 6 chỉ số trên Dashboard (Rule BR-11)

| STT | Tên chỉ số (Tiếng Việt) | Công thức tính chính xác | Ghi chú |
|---:|---|---|---|
| 1 | **Tổng tiền đã nhận** | $\sum \text{Số tiền của tất cả giao dịch Nhận tiền (`CASH_IN`)}$ | Bao gồm cả tiền nhận ngay lúc bán hàng, tiền khách trả nợ sau đó, và tiền nhận độc lập. |
| 2 | **Tổng tiền đã chuyển** | $\sum \text{Số tiền của tất cả giao dịch Chuyển tiền (`CASH_OUT`)}$ | Bao gồm cả tiền trả ngay lúc mua hàng, tiền mình trả nợ sau đó, và tiền chuyển độc lập. |
| 3 | **Tổng tiền mua hàng** | $\sum \text{Tổng tiền mua của tất cả đơn Mua hàng (`PURCHASE`)}$ | Tính theo tổng giá trị đơn mua (kể cả mua chịu hay trả hết). |
| 4 | **Tổng tiền bán hàng** | $\sum \text{Tổng tiền bán của tất cả đơn Bán hàng (`SALE`)}$ | Tính theo tổng giá trị đơn bán (kể cả bán chịu hay thu hết). |
| 5 | **Tổng tiền người khác còn nợ mình** | $\sum_{\text{Đơn Bán}} (\text{Tổng tiền bán} - \text{Đã nhận của đơn bán})$ | Luôn bằng tổng số dư nợ của tất cả người trong Tab "Người khác nợ mình". |
| 6 | **Tổng tiền mình còn nợ người khác** | $\sum_{\text{Đơn Mua}} (\text{Tổng tiền mua} - \text{Đã trả của đơn mua})$ | Luôn bằng tổng số dư nợ của tất cả người trong Tab "Mình nợ người khác". |

---

## 8. Quy tắc Định dạng và Chuẩn hóa dữ liệu (Rule BR-12)

1. **Kiểu dữ liệu tiền tệ:**
   - Bắt buộc dùng kiểu số nguyên 64-bit (`Long`) đại diện cho đơn vị **Đồng (VNĐ)**.
   - Tuyệt đối không dùng `Float` hoặc `Double` ở bất kỳ tầng nào.
   - Hiển thị trên giao diện: Dùng dấu chấm (`.`) phân cách hàng nghìn và hậu tố ` đ` (Ví dụ: `30.000.000 đ`).
2. **Định dạng ngày tháng:**
   - Lưu trong cơ sở dữ liệu dưới dạng `Long` (Epoch Milliseconds hoặc Epoch Day theo múi giờ địa phương Việt Nam `Asia/Ho_Chi_Minh`) để lọc và sắp xếp nhanh.
   - Hiển thị trên toàn bộ giao diện và báo cáo theo định dạng: `dd/MM/yyyy` (Ví dụ: `14/11/2026`).
3. **Chuẩn hóa tìm kiếm tiếng Việt không dấu:**
   - Khi lưu hoặc cập nhật tên Người liên quan (`full_name`), hệ thống tự động sinh một cột chuẩn hóa `search_name_normalized`:
     - Chuyển toàn bộ về chữ thường (`lowercase`).
     - Loại bỏ toàn bộ dấu thanh và dấu mũ tiếng Việt (`Nguyễn Văn Ánh` -> `nguyen van anh`).
     - Thay thế ký tự `đ`/`Đ` thành `d`.
     - Cắt bỏ khoảng trắng thừa ở hai đầu và gộp nhiều khoảng trắng liên tiếp thành 1 khoảng trắng.
   - Khi người dùng nhập từ khóa tìm kiếm, từ khóa cũng được qua hàm chuẩn hóa tương tự trước khi so khớp (`LIKE %keyword%`), giúp người dùng gõ `"nguyen van a"` hay `"Nguyễn Văn A"` hay `"NGUYEN VAN A"` đều ra kết quả tức thì.
