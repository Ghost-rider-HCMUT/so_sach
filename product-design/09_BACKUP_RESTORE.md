# 09. THIẾT KẾ SAO LƯU VÀ KHÔI PHỤC DỮ LIỆU (BACKUP & RESTORE)

Vì ứng dụng hoạt động hoàn toàn offline trên điện thoại của chủ hộ (không cần tài khoản, không đẩy dữ liệu lên máy chủ trung gian), tính năng **Sao lưu và Khôi phục** là chốt chặn an toàn quan trọng nhất giúp người dùng không bị mất sổ sách khi đổi điện thoại mới hoặc lỡ tay xóa ứng dụng.

---

## 1. Cấu trúc Tệp Sao lưu (Backup Package Format)

Mỗi bản sao lưu được đóng gói thành một tệp nén duy nhất có phần mở rộng `.zip` (hoặc `.stb.zip`) với tên tệp tự động rõ ràng bằng tiếng Việt không dấu:
`SoTrauBo_SaoLuu_14-11-2026_2130.zip`

Bên trong tệp `.zip` gồm 3 thành phần:

```text
SoTrauBo_SaoLuu_14-11-2026_2130.zip
├── manifest.json            # Thông tin kiểm tra tính hợp lệ, phiên bản, chữ ký băm SHA-256
├── database_export.json     # Toàn bộ dữ liệu 4 bảng (contacts, trade_orders, cash_transactions, attachments)
└── attachments/             # Thư mục chứa toàn bộ tệp ảnh chứng từ và ảnh trâu bò đính kèm
    ├── img_1731591000_1.jpg
    └── img_1731591200_2.jpg
```

### Cấu trúc `manifest.json`
```json
{
  "app_identifier": "com.sotraubo.app",
  "schema_version": 1,
  "created_at_epoch_ms": 1794666600000,
  "created_at_display": "14/11/2026 21:30",
  "counts": {
    "contacts": 25,
    "trade_orders": 68,
    "cash_transactions": 142,
    "attachments": 34
  },
  "summary": {
    "total_receivable_debt": 43000000,
    "total_payable_debt": 42000000
  },
  "data_sha256_checksum": "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
}
```

---

## 2. Quy trình Xuất Sao lưu (Export Backup)

1. Người dùng vào màn hình **Sao lưu & Khôi phục** -> Nhấn nút **"Xuất tệp sao lưu"**.
2. Hệ thống đọc toàn bộ dữ liệu hiện tại trong một giao dịch đọc nhất quán (`@Transaction`), kết xuất ra `database_export.json`, tính mã kiểm tra `SHA-256`, tạo `manifest.json` và nén cùng thư mục ảnh `attachments/` thành tệp `.zip`.
3. Cho phép người dùng chọn 1 trong 2 cách lưu:
   - **Lưu vào bộ nhớ điện thoại** (thông qua `ActivityResultContracts.CreateDocument` chuẩn Scoped Storage của Android — không cần xin quyền truy cập toàn bộ bộ nhớ nguy hiểm).
   - **Chia sẻ tệp sao lưu** (gửi vào "Cloud của tôi" trên Zalo, Google Drive cá nhân, hoặc gửi sang máy mới qua Bluetooth/Quick Share).
4. Lưu lại thời điểm sao lưu gần nhất để hiển thị trên màn hình (Ví dụ: *"Lần sao lưu gần nhất: 14/11/2026 lúc 21:30"*).

---

## 3. Quy trình Kiểm tra Hợp lệ trước khi Khôi phục (Pre-Restore Validation)

Khi người dùng chọn một tệp từ điện thoại để khôi phục, hệ thống **CHƯA ghi đè ngay** mà thực hiện 5 bước kiểm tra nghiêm ngặt trong thư mục tạm (`cacheDir/restore_staging/`):

| Bước | Nội dung kiểm tra | Thông báo tiếng Việt nếu thất bại |
|---:|---|---|
| **1** | Kiểm tra định dạng tệp `.zip` và sự tồn tại của `manifest.json`, `database_export.json`. | *"Tệp đã chọn không phải là tệp sao lưu của ứng dụng Sổ Trâu Bò."* |
| **2** | Kiểm tra `app_identifier == "com.sotraubo.app"` và `schema_version <= CURRENT_SCHEMA_VERSION`. | *"Phiên bản tệp sao lưu này mới hơn phiên bản ứng dụng hiện tại. Vui lòng cập nhật ứng dụng."* |
| **3** | Tính lại mã băm `SHA-256` của `database_export.json` và đối chiếu với `data_sha256_checksum` trong `manifest.json`. | *"Tệp sao lưu đã bị hỏng hoặc bị thay đổi nội dung. Không thể khôi phục để bảo vệ dữ liệu hiện tại."* |
| **4** | Kiểm tra toàn vẹn khóa ngoại và quy tắc nghiệp vụ (mọi `contact_id`, `trade_order_id` đều tồn tại; số tiền `Long > 0`; không có đơn nào có tổng tiền trả vượt quá tổng tiền đơn). | *"Dữ liệu bên trong tệp sao lưu không đồng nhất. Quá trình khôi phục đã được dừng lại an toàn."* |
| **5** | Hiển thị bảng tóm tắt nội dung tệp sao lưu và **Hộp thoại Cảnh báo Ghi đè**. | *(Chuyển sang bước Xác nhận của người dùng bên dưới).* |

---

## 4. Cảnh báo Ghi đè và Cơ chế Tự phục hồi An toàn (Safe Atomic Restore)

Sau khi tệp vượt qua cả 4 bước kiểm tra kỹ thuật, ứng dụng hiển thị hộp thoại xác nhận bằng tiếng Việt thật rõ ràng:

```text
╔═══════════════════════════════════════════════════════════════════╗
║  ⚠️ CẢNH BÁO TRƯỚC KHI KHÔI PHỤC DỮ LIỆU                          ║
╠═══════════════════════════════════════════════════════════════════╣
║  Bạn đang chuẩn bị khôi phục dữ liệu từ tệp sao lưu:              ║
║  • Ngày tạo bản sao lưu: 14/11/2026 lúc 21:30                     ║
║  • Số người liên quan: 25 người                                   ║
║  • Số đơn mua/bán: 68 đơn  •  Số giao dịch tiền: 142 giao dịch    ║
║  • Người khác nợ mình: 43.000.000 đ | Mình nợ: 42.000.000 đ       ║
║                                                                   ║
║  ❗ LƯU Ý QUAN TRỌNG:                                             ║
║  Toàn bộ dữ liệu hiện tại trên máy sẽ bị GHI ĐÈ và thay thế bằng  ║
║  dữ liệu trong tệp sao lưu này.                                   ║
║                                                                   ║
║  ┌───────────────────────────┐   ┌─────────────────────────────┐  ║
║  │        HỦY THAO TÁC       │   │  ĐỒNG Ý GHI ĐÈ & KHÔI PHỤC  │  ║
║  └───────────────────────────┘   └─────────────────────────────┘  ║
╚═══════════════════════════════════════════════════════════════════╝
```

### Cơ chế Khôi phục Nguyên tử (Atomic Rollback Guarantee)
- Trước khi xóa dữ liệu hiện tại để nạp dữ liệu mới, hệ thống tự động tạo một bản sao lưu nội bộ tạm thời (`pre_restore_snapshot.zip`).
- Toàn bộ việc xóa bảng cũ và chèn dữ liệu mới từ `database_export.json` diễn ra bên trong một `database.withTransaction { ... }` duy nhất.
- Nếu có bất kỳ lỗi đọc/ghi bộ nhớ nào xảy ra giữa chừng (ví dụ điện thoại hết pin hoặc đầy bộ nhớ), Transaction tự động hoàn tác (Rollback) 100%, giữ nguyên dữ liệu cũ của người dùng.
