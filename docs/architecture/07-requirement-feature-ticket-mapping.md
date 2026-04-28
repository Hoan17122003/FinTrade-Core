# Mapping Requirement -> Feature -> Ticket

| Requirement | Feature | Ticket chính |
| --- | --- | --- |
| Đăng ký / đăng nhập | Identity và access | `FTC-ID-001` |
| Xác thực eKYC | Onboarding KYC | `FTC-ID-002`, `FTC-ARCH-003` |
| Nạp tiền | Deposit operations | `FTC-PAY-001` |
| Rút tiền | Withdrawal operations | `FTC-PAY-002` |
| Xem số dư ví | Wallet balance model | `FTC-PAY-003` |
| Đặt lệnh mua/bán | Order management | `FTC-ARCH-002`, `FTC-TRD-002` |
| Xem holdings | Holdings view | `FTC-TRD-004` |
| Xem lịch sử giao dịch | History/audit query | `FTC-TRD-004`, `FTC-OPS-003` |
| Xem P&L | Valuation và P&L | `FTC-FIN-002`, `FTC-TRD-005` |
| Nhận risk alerts | Notification và risk alerts | `FTC-RISK-001`, `FTC-RISK-003` |
| Xem danh sách user | Backoffice user management | `FTC-ID-003` |
| Kiểm tra giao dịch lỗi | Exception handling | `FTC-FIN-001`, `FTC-OPS-001` |
| Khóa/mở khóa tài khoản | Account restriction control | `FTC-ID-003` |
| Xử lý khiếu nại | Complaint workflow | `FTC-OPS-002` |
| Theo dõi order bất thường | Trade surveillance | `FTC-RISK-003` |
| Theo dõi payment thất bại | Payment exception monitoring | `FTC-PAY-001`, `FTC-OPS-001` |
| Xem audit log | Audit evidence | `FTC-OPS-003` |
| Cấu hình risk rule | Risk rules engine | `FTC-RISK-001` |
| Theo dõi giao dịch đáng ngờ | AML-lite monitoring | `FTC-RISK-003` |
| Phê duyệt giao dịch lớn | Approval workflow | `FTC-RISK-002` |
| Xem báo cáo rủi ro | Risk reporting | `FTC-RISK-004` |
| Khóa giao dịch nếu vi phạm rule | Restriction enforcement | `FTC-ID-003`, `FTC-RISK-003` |
| Ledger entries | Financial truth foundation | `FTC-ARCH-001`, `FTC-TRD-003` |
| Dòng tiền nạp/rút | Finance reporting | `FTC-PAY-001`, `FTC-PAY-002`, `FTC-FIN-001` |
| Phí giao dịch | Fee model | `FTC-FIN-003` |
| Doanh thu phí | Finance reporting | `FTC-FIN-003`, `FTC-FIN-004` |
| Số dư platform | Treasury/balance reporting | `FTC-FIN-001`, `FTC-PAY-003` |
| Báo cáo cuối ngày | EOD operations | `FTC-FIN-004` |
| Reconciliation | Reconciliation control | `FTC-FIN-001` |
| Cập nhật giá thị trường | Market data ingestion | `FTC-FIN-002` |
| Đóng sổ cuối ngày | EOD close | `FTC-FIN-004` |
| Tạo daily statement | Statement generation | `FTC-FIN-004` |
| Tính lại P&L | P&L batch recalculation | `FTC-TRD-005` |
| Quét giao dịch bất thường | Risk surveillance | `FTC-RISK-003` |
| Retry failed event | Resilience operations | `FTC-OPS-001` |
| Duyệt cổ phiếu lên sàn | Issuer và stock listing workflow | `FTC-TRD-001` |
| Ghi transaction vào ledger | Immutable ledgering | `FTC-ARCH-001`, `FTC-TRD-003` |
| Phân tách RBAC | Access control | `FTC-ARCH-004` |
| Security master | Stock reference data | `FTC-TRD-001` |
| Financial processing idempotent | Resilience và correctness | `FTC-ARCH-003`, `FTC-OPS-001` |
| Quản lý công ty phát hành | Issuer management | `FTC-TRD-001` |
| Tạo entity stock sau khi duyệt | Stock creation and activation | `FTC-TRD-001` |
| Phân loại cổ phiếu theo ngành/chỉ số | Stock classification | `FTC-TRD-001A` |
| Quản lý nhóm như VN30 | Index basket management | `FTC-TRD-001A` |
| Xem chi tiết cổ phiếu | Stock detail | `FTC-TRD-006` |
| Xem portfolio đầu tư | Portfolio analytics | `FTC-TRD-004`, `FTC-TRD-005` |
| Watchlist cổ phiếu | Investor watchlist | `FTC-TRD-006` |
