# 04. SƠ ĐỒ LUỒNG NGHIỆP VỤ VÀ LUỒNG NGƯỜI DÙNG (USER FLOWS)

Tài liệu này mô tả chi tiết các luồng tương tác của người dùng (User Flows) và luồng xử lý nghiệp vụ bằng sơ đồ trực quan (Mermaid Flowchart & Sequence Diagram). Toàn bộ nhãn và thông báo đều bằng tiếng Việt 100%.

---

## 1. Sơ đồ Tổng quan Điều hướng Ứng dụng & Khóa Mật khẩu 6 Số (App Navigation & PIN Flow)

```mermaid
flowchart TD
    Start(["Mở ứng dụng Sổ Trâu Bò"]) --> CheckPin{"Đã thiết lập\nmật khẩu 6 số chưa?"}

    CheckPin -->|"Chưa (Lần đầu mở app)"| SetupPin1["Nhập mật khẩu 6 số mới"]
    SetupPin1 --> SetupPin2["Xác nhận lại mật khẩu 6 số"]
    SetupPin2 -->|"Khớp 6 số"| SavePin["Lưu băm SHA-256 + AES-256-GCM Keystore"]
    SavePin --> BottomNav["Thanh điều hướng dưới cùng (5 mục)"]

    CheckPin -->|"Đã có mật khẩu"| EnterPin["Màn hình Nhập mật khẩu 6 số"]
    EnterPin -->|"Sai mật khẩu"| PinError["Báo lỗi: 'Mật khẩu 6 số không đúng. Vui lòng nhập lại.'"]
    PinError --> EnterPin
    EnterPin -->|"Đúng 6 số"| BottomNav

    BottomNav --> Tab1["1. Tổng quan (Dashboard)"]
    BottomNav --> Tab2["2. Giao dịch (Nhận/Chuyển tiền)"]
    BottomNav --> Tab3["3. Mua bán (Mua/Bán trâu bò)"]
    BottomNav --> Tab4["4. Công nợ (2 chiều)"]
    BottomNav --> Tab5["5. Người liên quan (Danh bạ)"]

    %% Quick Actions from Dashboard
    Tab1 --> QA_CashIn["Nút nhanh: Nhận tiền"]
    Tab1 --> QA_CashOut["Nút nhanh: Chuyển tiền"]
    Tab1 --> QA_Buy["Nút nhanh: Mua hàng"]
    Tab1 --> QA_Sell["Nút nhanh: Bán hàng"]

    %% Dashboard drill-downs
    Tab1 -->|"Nhấn thẻ Công nợ / Xem tất cả"| Tab4
    Tab1 -->|"Nhấn Xem tất cả giao dịch"| Tab2
    Tab1 -->|"Biểu tượng Cài đặt & Sao lưu"| BackupScreen["Màn hình Sao lưu & Báo cáo"]

    %% Debt Detail
    Tab4 -->|"Tab 1: Người khác nợ mình"| DebtDetail["Màn hình Chi tiết Công nợ 1 người"]
    Tab4 -->|"Tab 2: Mình nợ người khác"| DebtDetail

    %% Contact Detail
    Tab5 -->|"Nhấn vào 1 người"| ContactDetail["Màn hình Hồ sơ Người liên quan"]
    ContactDetail -->|"Xem chi tiết công nợ"| DebtDetail
```

---

## 2. Luồng Chọn Người liên quan hoặc Thêm Người mới ngay trong Biểu mẫu

Đây là luồng cốt lõi xuất hiện trong cả 4 biểu mẫu (**Nhận tiền**, **Chuyển tiền**, **Mua hàng**, **Bán hàng**), giúp người dùng không bao giờ phải nhập lại tên thủ công và đảm bảo mỗi người chỉ có duy nhất 1 hồ sơ.

```mermaid
flowchart TD
    FormOpen["Đang ở Biểu mẫu (Nhận tiền / Chuyển tiền / Mua hàng / Bán hàng)"] --> ClickPerson["Nhấn vào trường 'Người liên quan'"]
    ClickPerson --> OpenSheet["Mở Bảng chọn Người liên quan (Bottom Sheet)"]
    OpenSheet --> ShowSearch["Hiển thị Ô tìm kiếm ở trên cùng + Danh sách người đã lưu"]

    ShowSearch --> UserType["Người dùng nhập Tên (có dấu/không dấu) hoặc Số điện thoại"]
    UserType --> FilterRealtime["Lọc danh sách tức thì (Không phân biệt hoa/thường & dấu)"]

    FilterRealtime --> HasResult{"Có người cần tìm?"}
    HasResult -->|"Có"| SelectExisting["Chạm chọn người trong danh sách"]
    SelectExisting --> ReturnForm["Đóng bảng chọn & Điền thông tin người đó vào Biểu mẫu"]

    HasResult -->|"Chưa có trong danh bạ"| ClickAddNew["Nhấn nút '+ Thêm người mới' (Gợi ý sẵn tên/SĐT vừa gõ)"]
    ClickAddNew --> OpenQuickAdd["Mở hộp thoại 'Thêm người liên quan mới'"]
    OpenQuickAdd --> InputNewInfo["Nhập Họ tên (bắt buộc), Số điện thoại, Địa chỉ, Ghi chú"]
    InputNewInfo --> CheckDuplicate{"Trùng SĐT hoặc Tên?"}

    CheckDuplicate -->|"Trùng SĐT đã có"| WarnDup["Cảnh báo: 'Số điện thoại này đã thuộc về [Tên]. Bạn muốn chọn người đã có hay vẫn lưu mới?'"]
    WarnDup -->|"Chọn người đã có"| ReturnForm
    WarnDup -->|"Vẫn lưu mới"| SaveNewContact["Lưu Hồ sơ mới vào Cơ sở dữ liệu"]

    CheckDuplicate -->|"Không trùng"| SaveNewContact
    SaveNewContact --> AutoSelect["Tự động chọn người vừa tạo cho giao dịch hiện tại"]
    AutoSelect --> ReturnForm
```

---

## 3. Luồng Nghiệp vụ Mua hàng và Trả nợ thêm sau đó

Ví dụ minh họa: Mua bò `30.000.000 đ`, trả ngay `20.000.000 đ` (còn nợ `10.000.000 đ`), sau đó chuyển thêm `5.000.000 đ` (còn nợ `5.000.000 đ`).

```mermaid
flowchart TD
    subgraph Step1["BƯỚC 1: TẠO ĐƠN MUA HÀNG (Mua bò 30 triệu, trả trước 20 triệu)"]
        B1["Nhấn 'Mua hàng'"] --> B2["Chọn Người bán (VD: Phạm Văn D)"]
        B2 --> B3["Nhập Số lượng (VD: 2 con) & Mô tả ('2 bò đực lai Sind')"]
        B3 --> B4["Nhập Tổng tiền mua: 30.000.000 đ"]
        B4 --> B5["Nhập Đã trả ngay: 20.000.000 đ & Chọn hình thức (Tiền mặt / Chuyển khoản)"]
        B5 --> B6{"Kiểm tra: Đã trả <= Tổng tiền mua?"}
        B6 -->|"Sai (Đã trả > 30tr)"| B_Err["Báo lỗi: 'Số tiền đã trả không được vượt quá tổng tiền mua'"]
        B_Err --> B5
        B6 -->|"Hợp lệ"| B7["Tự động hiển thị Còn nợ: 10.000.000 đ"]
        B7 --> B8["Nhấn 'Lưu đơn mua' (Ghi DB trong 1 Transaction)"]
        B8 --> B9["Hệ thống lưu:\n1. Đơn Mua hàng (Tổng: 30tr)\n2. Giao dịch Chuyển tiền liên kết (20tr, trả ban đầu)\n=> Còn nợ đơn mua = 10tr"]
    end

    subgraph Step2["BƯỚC 2: TRẢ THÊM TIỀN SAU ĐÓ (Chuyển thêm 5 triệu)"]
        P1["Nhấn 'Chuyển tiền' (hoặc nhấn 'Trả nợ' từ chi tiết công nợ Phạm Văn D)"] --> P2["Chọn Người nhận: Phạm Văn D"]
        P2 --> P3["Hệ thống hiển thị các đơn Mua còn nợ của Phạm Văn D:\n'Đơn mua 14/11 - Còn nợ: 10.000.000 đ'"]
        P3 --> P4["Chọn liên kết với Đơn mua ngày 14/11"]
        P4 --> P5["Nhập Số tiền chuyển: 5.000.000 đ"]
        P5 --> P6{"Kiểm tra: Số tiền chuyển <= Còn nợ (10tr)?"}
        P6 -->|"Vượt quá (> 10tr)"| P_Err["Báo lỗi: 'Đơn mua chỉ còn nợ 10.000.000 đ. Không thể trả vượt số nợ.'"]
        P_Err --> P5
        P6 -->|"Hợp lệ (<= 10tr)"| P7["Nhấn 'Lưu giao dịch'"]
        P7 --> P8["Hệ thống lưu:\n- Thêm 1 bản ghi Chuyển tiền 5tr liên kết với Đơn mua\n- KHÔNG tạo đơn mua mới\n=> Đơn mua giữ nguyên Tổng 30tr, Đã trả = 25tr, Còn nợ = 5tr"]
    end

    B9 --> Step2
```

---

## 4. Luồng Nghiệp vụ Bán hàng và Thu nợ thêm sau đó

Ví dụ minh họa: Bán bò `50.000.000 đ`, nhận ngay `30.000.000 đ` (khách còn nợ `20.000.000 đ`), sau đó khách trả thêm `10.000.000 đ` (khách còn nợ `10.000.000 đ`).

```mermaid
flowchart TD
    subgraph SaleStep1["BƯỚC 1: TẠO ĐƠN BÁN HÀNG (Bán bò 50 triệu, nhận ngay 30 triệu)"]
        S1["Nhấn 'Bán hàng'"] --> S2["Chọn Người mua (VD: Nguyễn Văn A)"]
        S2 --> S3["Nhập Số lượng (VD: 2 con) & Mô tả ('2 bò thịt BBB')"]
        S3 --> S4["Nhập Tổng tiền bán: 50.000.000 đ"]
        S4 --> S5["Nhập Đã nhận ngay: 30.000.000 đ & Hình thức nhận"]
        S5 --> S6{"Kiểm tra: Đã nhận <= Tổng tiền bán?"}
        S6 -->|"Sai (Đã nhận > 50tr)"| S_Err["Báo lỗi: 'Số tiền đã nhận không được vượt quá tổng tiền bán'"]
        S_Err --> S5
        S6 -->|"Hợp lệ"| S7["Tự động hiển thị Khách còn nợ: 20.000.000 đ"]
        S7 --> S8["Nhấn 'Lưu đơn bán' (Ghi DB trong 1 Transaction)"]
        S8 --> S9["Hệ thống lưu:\n1. Đơn Bán hàng (Tổng: 50tr)\n2. Giao dịch Nhận tiền liên kết (30tr, thu ban đầu)\n=> Khách còn nợ = 20tr"]
    end

    subgraph SaleStep2["BƯỚC 2: KHÁCH TRẢ THÊM TIỀN (Nhận thêm 10 triệu)"]
        R1["Nhấn 'Nhận tiền' (hoặc nhấn 'Thu nợ' từ chi tiết công nợ Nguyễn Văn A)"] --> R2["Chọn Người đưa tiền: Nguyễn Văn A"]
        R2 --> R3["Hệ thống hiển thị đơn Bán còn nợ của Nguyễn Văn A:\n'Đơn bán 14/11 - Khách còn nợ: 20.000.000 đ'"]
        R3 --> R4["Chọn liên kết với Đơn bán ngày 14/11"]
        R4 --> R5["Nhập Số tiền nhận: 10.000.000 đ"]
        R5 --> R6{"Kiểm tra: Số tiền nhận <= Khách còn nợ (20tr)?"}
        R6 -->|"Vượt quá (> 20tr)"| R_Err["Báo lỗi: 'Đơn bán chỉ còn nợ 20.000.000 đ. Không thể thu vượt số nợ.'"]
        R_Err --> R5
        R6 -->|"Hợp lệ (<= 20tr)"| R7["Nhấn 'Lưu giao dịch'"]
        R7 --> R8["Hệ thống lưu:\n- Thêm 1 bản ghi Nhận tiền 10tr liên kết với Đơn bán\n- KHÔNG tạo đơn bán mới\n=> Đơn bán giữ nguyên Tổng 50tr, Đã nhận = 40tr, Khách còn nợ = 10tr"]
    end

    S9 --> SaleStep2
```

---

## 5. Luồng Tra cứu và Quản lý Công nợ Hai Chiều

```mermaid
flowchart TD
    OpenDebt["Mở màn hình 'Công nợ' từ Thanh điều hướng hoặc Dashboard"] --> TwoTabs["Hiển thị 2 Tab tách biệt:\n- Tab 1: Người khác nợ mình (Màu xanh lá)\n- Tab 2: Mình nợ người khác (Màu cam)"]

    TwoTabs --> FilterDefault["Mặc định: Chỉ hiển thị người có Số tiền còn nợ > 0"]
    FilterDefault --> SearchOrSort["Người dùng có thể:\n1. Gõ tìm kiếm nhanh (Tên có dấu/không dấu, SĐT)\n2. Sắp xếp: Nợ cao -> thấp / Giao dịch gần nhất"]

    SearchOrSort --> ClickPerson["Nhấn vào 1 người trong danh sách (VD: Ông Nguyễn Văn A)"]
    ClickPerson --> OpenPersonDebt["Mở màn hình 'Chi tiết công nợ' của Ông Nguyễn Văn A"]

    OpenPersonDebt --> CheckBidirectional{"Người này có nợ ở mấy chiều?"}

    CheckBidirectional -->|"Chỉ nợ mình"| ShowReceivableOnly["Hiển thị Phần 1: Người đó còn nợ mình\n(Tổng bán, Đã nhận, Còn nợ + Danh sách đơn bán & lần thu)"]
    CheckBidirectional -->|"Mình chỉ nợ họ"| ShowPayableOnly["Hiển thị Phần 2: Mình còn nợ người đó\n(Tổng mua, Đã trả, Còn nợ + Danh sách đơn mua & lần trả)"]
    CheckBidirectional -->|"Có nợ cả 2 chiều"| ShowBothSeparate["Hiển thị CẢ 2 PHẦN TÁCH BIỆT RÕ RÀNG:\n1. Khối Xanh lá: Người đó còn nợ mình (VD: 20.000.000 đ)\n2. Khối Cam: Mình còn nợ người đó (VD: 15.000.000 đ)\n(Tuyệt đối KHÔNG tự động bù trừ)"]

    ShowReceivableOnly --> ActionCollect["Nhấn nút 'Thu tiền nợ' -> Mở biểu mẫu Nhận tiền (chọn sẵn người & đơn nợ)"]
    ShowPayableOnly --> ActionPay["Nhấn nút 'Trả tiền nợ' -> Mở biểu mẫu Chuyển tiền (chọn sẵn người & đơn nợ)"]
    ShowBothSeparate --> ActionCollect
    ShowBothSeparate --> ActionPay
```

---

## 6. Luồng Sửa / Xóa Giao dịch và Tự động Tính lại Số dư

```mermaid
sequenceDiagram
    actor User as Chủ hộ kinh doanh
    participant UI as Màn hình Chi tiết Giao dịch
    participant VM as ViewModel
    participant Repo as Repository / Room DB
    participant Dash as Dashboard & Công nợ (Flow)

    User->>UI: Nhấn "Sửa" hoặc "Xóa" một giao dịch
    UI->>User: Hiển thị biểu mẫu chỉnh sửa hoặc Hộp thoại xác nhận xóa (100% Tiếng Việt)
    User->>UI: Xác nhận Lưu thay đổi / Xác nhận Xóa
    UI->>VM: Gửi yêu cầu cập nhật / xóa
    VM->>Repo: Kiểm tra ràng buộc nghiệp vụ (Không âm nợ, không thanh toán vượt tổng đơn)
    alt Vi phạm ràng buộc (VD: Sửa tổng đơn mua nhỏ hơn số tiền đã trả)
        Repo-->>VM: Trả về lỗi nghiệp vụ cụ thể
        VM-->>UI: Hiển thị thông báo lỗi tiếng Việt rõ ràng
    else Hợp lệ
        Repo->>Repo: Thực thi @Transaction cập nhật/xóa bản ghi trong Room
        Repo-->>Dash: Room tự động phát luồng dữ liệu mới qua Flow<T>
        Dash-->>User: Tự động cập nhật tức thì:\n1. Số tiền còn nợ của đơn hàng\n2. Công nợ 2 chiều của người liên quan\n3. 6 chỉ số trên Dashboard
    end
```

---

## 7. Luồng Sao lưu và Khôi phục dữ liệu (Backup & Restore Flow)

```mermaid
flowchart TD
    OpenBackup["Mở màn hình 'Sao lưu & Khôi phục'"] --> ChooseAction{"Chọn thao tác"}

    ChooseAction -->|"Sao lưu dữ liệu"| CreateBackup["Đóng gói dữ liệu SQLite/JSON + Ảnh chứng từ thành tệp .zip"]
    CreateBackup --> SaveFile["Mở trình chọn nơi lưu tệp của Android\n(Tên mặc định: SoTrauBo_SaoLuu_ddMMyyyy_HHmm.zip)"]
    SaveFile --> BackupSuccess["Thông báo: 'Đã sao lưu dữ liệu thành công!' + Nút chia sẻ qua Zalo/Drive"]

    ChooseAction -->|"Khôi phục dữ liệu"| PickFile["Chọn tệp sao lưu (.zip) từ điện thoại"]
    PickFile --> ValidateFile{"Kiểm tra tính hợp lệ của tệp:\n- Đúng định dạng?\n- Đúng phiên bản cấu trúc?\n- Dữ liệu không hỏng?"}
    ValidateFile -->|"Không hợp lệ"| RestoreError["Báo lỗi: 'Tệp sao lưu không hợp lệ hoặc bị hỏng. Dữ liệu hiện tại được giữ nguyên.'"]
    ValidateFile -->|"Hợp lệ"| ShowWarning["Hiển thị Cảnh báo quan trọng:\n'Khôi phục sẽ GHI ĐÈ toàn bộ dữ liệu hiện tại bằng dữ liệu ngày [dd/MM/yyyy] (gồm X người, Y giao dịch). Bạn có chắc chắn muốn tiếp tục?'"]
    ShowWarning -->|"Hủy"| CancelRestore["Giữ nguyên dữ liệu hiện tại"]
    ShowWarning -->|"Đồng ý khôi phục"| AutoSafetyBackup["Tự động tạo 1 bản sao lưu tạm thời của dữ liệu hiện tại (Safety Rollback)"]
    AutoSafetyBackup --> DoRestore["Thay thế dữ liệu & ảnh chứng từ trong Transaction"]
    DoRestore --> RestoreDone["Thông báo: 'Khôi phục dữ liệu thành công!' & Tải lại Dashboard"]
```
