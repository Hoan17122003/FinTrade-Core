# Tổng Quan Hệ Thống FinTradeCore

## Mục đích hệ thống

FinTradeCore là lõi của một nền tảng kiểu brokerage, quản lý:

- Onboarding và truy cập người dùng
- Dòng tiền nạp và rút
- Tiếp nhận và xử lý vòng đời lệnh giao dịch chứng khoán
- Hiển thị holdings và P&L
- Giám sát risk/compliance
- Ledger tài chính và đối soát
- Các kiểm soát vận hành cuối ngày

## Ranh giới hệ thống được khuyến nghị

Ranh giới phase 1 được đề xuất là:

- Trong scope:
  - API cho investor portal
  - API backoffice cho admin/operations/risk
- Quản lý lệnh
- Quản lý công ty phát hành, cổ phiếu, và phân loại cổ phiếu
- Wallet / cash ledger
- Securities holdings ledger
  - Risk rule engine
  - Báo cáo và quy trình đóng sổ cuối ngày
  - Adapter tích hợp cho eKYC, payment, market data
- Ngoài scope:
  - Quy trình thành viên sở giao dịch thật
  - Tích hợp trung tâm lưu ký
  - Nền tảng AML case management ở cấp production

## Actors và use cases chính

### Retail Investor / End User

- Đăng ký và hoàn tất KYC
- Đăng nhập và quản lý truy cập
- Nạp tiền
- Rút tiền
- Xem số dư ví và tài sản nắm giữ
- Đặt lệnh mua/bán
- Xem trạng thái lệnh và lịch sử giao dịch
- Xem thông tin chi tiết cổ phiếu, công ty phát hành, phân loại ngành/chỉ số
- Xem portfolio đầu tư tổng hợp
- Xem P&L và daily statement
- Nhận cảnh báo rủi ro hoặc cảnh báo tài khoản
- Gửi khiếu nại / tranh chấp

### Admin / Operations Staff

- Xem danh sách user và thông tin user
- Khóa/mở khóa tài khoản
- Review giao dịch lỗi
- Theo dõi luồng payment thất bại
- Xử lý khiếu nại
- Retry failed asynchronous events
- Chạy reconciliation và close-of-day jobs
- Phê duyệt onboarding/listing của mã chứng khoán
- Quản lý taxonomy cổ phiếu như ngành, rổ chỉ số, thematic group

### Compliance / Risk Officer

- Cấu hình risk rules
- Theo dõi giao dịch đáng ngờ
- Theo dõi lệnh bất thường
- Phê duyệt hoặc từ chối giao dịch lớn
- Khóa giao dịch khi vi phạm rule
- Xem risk reports và audit logs

## Nhóm use case

| Domain | Use case chính |
| --- | --- |
| Identity & Access | đăng ký, đăng nhập, KYC verification, kiểm soát trạng thái tài khoản |
| Money Movement | nạp tiền, rút tiền, theo dõi trạng thái payment, số dư platform |
| Stock Master & Listing | quản lý công ty phát hành, tạo stock, phân loại stock, listing approval |
| Trading | đặt lệnh, vòng đời execution, holdings, portfolio, cập nhật giá |
| Ledger & Finance | ledger entries, fees, doanh thu, reconciliation, end-of-day |
| Risk & Compliance | alerts, suspicious activity scan, rule configuration, approvals |
| Operations | retry failed event, xử lý khiếu nại, tạo daily statement |

## Mô hình vận hành được khuyến nghị

Ở iteration đầu tiên, nên dùng modular monolith với các module nội bộ tách bạch rõ và domain events bất đồng bộ khi cần. Cách này giữ hệ thống đủ nhỏ cho một team nhỏ hoặc một người triển khai, đồng thời vẫn bảo toàn các ranh giới cần thiết để sau này tách dịch vụ nếu cần.
