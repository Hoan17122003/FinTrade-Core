# Domain Model Và Kiến Trúc High-Level FinTradeCore

## Domain model sơ bộ

### Core entities

| Entity | Mô tả |
| --- | --- |
| `User` | Tài khoản và hồ sơ người dùng cuối |
| `IdentityVerification` | Trạng thái KYC/eKYC và tham chiếu tới provider |
| `AccountStatus` | Active, locked, restricted, frozen |
| `WalletAccount` | Ví tiền hoặc cash sub-account của user |
| `IssuerCompany` | Công ty phát hành cổ phiếu, chứa hồ sơ doanh nghiệp và trạng thái duyệt |
| `ListingApplication` | Hồ sơ đề nghị niêm yết/đưa cổ phiếu lên platform |
| `ListingCriteria` | Bộ tiêu chí và kết quả đánh giá cho listing |
| `Stock` | Mã cổ phiếu được tạo sau khi công ty được duyệt thành công |
| `StockClassification` | Phân loại cổ phiếu theo ngành, chỉ số, chủ đề, vốn hóa |
| `IndexBasket` | Rổ/chỉ số tham chiếu như VN30, VN100, ngành công nghệ |
| `Order` | Ý định mua/bán do investor tạo |
| `OrderExecution` | Các sự kiện khớp lệnh / fill cho một order |
| `Position` | Tài sản nắm giữ được suy ra theo user và instrument |
| `Portfolio` | Góc nhìn tổng hợp danh mục đầu tư của user |
| `PortfolioSnapshot` | Ảnh chụp giá trị danh mục tại một thời điểm hoặc cuối ngày |
| `Watchlist` | Danh sách cổ phiếu user theo dõi |
| `CashLedgerEntry` | Bản ghi ledger tiền bất biến |
| `SecuritiesLedgerEntry` | Bản ghi ledger chứng khoán bất biến |
| `FeeEntry` | Bản ghi thu phí |
| `PaymentTransaction` | Bản ghi vòng đời nạp/rút tiền |
| `RiskRule` | Định nghĩa rule rủi ro có version |
| `RiskAlert` | Cảnh báo sinh ra từ rule evaluation |
| `ApprovalCase` | Workflow phê duyệt cho giao dịch lớn hoặc exception |
| `ComplaintCase` | Hồ sơ khiếu nại / tranh chấp |
| `AuditLogEntry` | Nhật ký bất biến về actor/action |
| `ReconciliationBatch` | Một lần chạy đối soát internal/external |
| `DailyStatement` | Sao kê người dùng cuối ngày |
| `CloseOfDayBatch` | Một lần chạy quy trình đóng sổ ngày |
| `FailedEvent` | Bản ghi lỗi xử lý event bất đồng bộ |

## Quan hệ domain

- `IssuerCompany` có thể tạo một hoặc nhiều `ListingApplication`.
- `ListingApplication` được đánh giá theo `ListingCriteria`; khi được duyệt thành công thì hệ thống tạo `Stock`.
- `Stock` thuộc về đúng một `IssuerCompany` nhưng có thể gắn với nhiều `StockClassification` và nhiều `IndexBasket`.
- `User` sở hữu một hoặc nhiều `WalletAccount`, có thể có nhiều `Position`, một `Portfolio`, và có thể có một hoặc nhiều `Watchlist`.
- `User` tạo `Order` trên một `Stock`.
- `Order` phát sinh `OrderExecution`, `FeeEntry`, `CashLedgerEntry`, và `SecuritiesLedgerEntry`.
- `PaymentTransaction` phát sinh `CashLedgerEntry` và có thể cần `ApprovalCase`.
- `Portfolio` được suy diễn từ `Position`, `WalletAccount`, `Stock`, và market prices.
- Việc đánh giá `RiskRule` tạo ra `RiskAlert` và có thể cập nhật `AccountStatus` thông qua các hành động được kiểm soát.
- `AuditLogEntry` ghi nhận mọi hành động đặc quyền và mọi thay đổi trạng thái tài chính.
- `CloseOfDayBatch` sử dụng dữ liệu giao dịch, giá, và ledger để tạo báo cáo và statement.

## Domain con quanh cổ phiếu

### Issuer và listing

- `IssuerCompany` chứa tên pháp lý, mã doanh nghiệp, ngành chính, trạng thái hoạt động, hồ sơ tài chính tóm tắt, và trạng thái đủ điều kiện listing.
- `ListingApplication` lưu lần gửi hồ sơ, bộ tài liệu, người duyệt, lý do duyệt/từ chối, và các mốc trạng thái như `draft`, `submitted`, `under_review`, `approved`, `rejected`, `activated`.
- `ListingCriteria` biểu diễn checklist hoặc bộ rule tối thiểu để đánh giá công ty, ví dụ:
  - hồ sơ pháp lý đầy đủ
  - đủ dữ liệu tài chính
  - đạt ngưỡng vốn hóa hoặc số lượng cổ phần tối thiểu
  - không thuộc danh sách hạn chế nội bộ

### Stock master

- `Stock` là thực thể giao dịch chính, gồm các thuộc tính tiêu biểu:
  - `ticker`
  - `display_name`
  - `issuer_company_id`
  - `listing_status`
  - `par_value`
  - `lot_size`
  - `currency`
  - `market`
  - `listing_date`
  - `trading_status`
- `Stock` nên có lifecycle rõ: `draft` -> `approved` -> `active` -> `suspended` -> `delisted`.

### Phân loại cổ phiếu

- `StockClassification` hỗ trợ nhiều chiều phân loại thay vì chỉ một nhãn duy nhất.
- Nên hỗ trợ tối thiểu các loại classification:
  - `sector`: công nghệ, ngân hàng, bất động sản, năng lượng
  - `index`: VN30, VN100, HNX30
  - `theme`: tăng trưởng, cổ tức, blue-chip
  - `market_cap_band`: large-cap, mid-cap, small-cap
- Một `Stock` có thể thuộc nhiều classification cùng lúc.
- `IndexBasket` là aggregate riêng để quản lý membership, hiệu lực theo thời gian, và metadata của rổ/chỉ số.

### Portfolio

- `Portfolio` không phải nguồn ghi nhận tài chính gốc mà là aggregate/view ở mức investor.
- `Portfolio` nên phản ánh:
  - tổng giá trị tài sản
  - tỷ trọng tiền mặt so với cổ phiếu
  - phân bổ theo mã cổ phiếu
  - phân bổ theo ngành
  - phân bổ theo rổ chỉ số
  - realized/unrealized P&L
  - top gainers / top losers trong danh mục
- `PortfolioSnapshot` phục vụ sao kê ngày, chart hiệu quả đầu tư, và so sánh theo thời gian.

## Các phương án kiến trúc đã cân nhắc

### Phương án A: Modular monolith với immutable ledger và async jobs

- Một ứng dụng deploy duy nhất
- Các module nội bộ tách bạch rõ
- Dùng chung relational database
- Ghi transactional cho các financial events lõi
- Xử lý async qua outbox/event table và workers

Ưu điểm:

- Độ phức tạp delivery thấp hơn
- Phù hợp với một engineer hoặc team nhỏ
- Dễ debug và giữ nhất quán dữ liệu ở giai đoạn đầu
- Giá trị học tập tốt cho domain modeling

Nhược điểm:

- Khả năng scale độc lập thấp hơn
- Cần kỷ luật để giữ boundary giữa các module

### Phương án B: Microservices ngay từ đầu

- Tách riêng identity, trading, wallet, risk, reporting services

Ưu điểm:

- Mô hình ownership dịch vụ rõ hơn
- Có tiềm năng deploy độc lập

Nhược điểm:

- Quá nhiều overhead vận hành và nhất quán dữ liệu so với độ trưởng thành hiện tại
- Khó đảm bảo các invariant tài chính append-only ở giai đoạn đầu

### Phương án C: Ledger dựa hoàn toàn trên blockchain

Ưu điểm:

- Câu chuyện tamper resistance mạnh

Nhược điểm:

- Không phù hợp với learning scope hiện tại
- Độ phức tạp cao nhưng giá trị nghiệp vụ tăng thêm hạn chế ở phase 1
- Làm chậm delivery các workflow brokerage cốt lõi

## Kiến trúc khuyến nghị

Khuyến nghị chọn `Phương án A: modular monolith với append-only ledger, audit log, và hash/tamper-evident design`.

Lý do:

- Phù hợp nhất với mục tiêu quy mô nhỏ và học tập đã nêu.
- Giữ được integrity tài chính mạnh mà không phải trả chi phí phân tán quá sớm.
- Cho phép thực thi nguyên tắc append-only rõ ràng cho financial truth.
- Vẫn mở đường để tách service sau này nếu boundary giữa các module đủ ổn định.

## Các module đề xuất

| Module | Trách nhiệm |
| --- | --- |
| `identity-access` | đăng ký, đăng nhập, RBAC, account state |
| `kyc-integration` | điều phối eKYC và đồng bộ trạng thái |
| `wallet-payments` | nạp tiền, rút tiền, cash balance, payment adapter |
| `issuer-listing` | quản lý company, listing application, listing criteria, stock activation |
| `stock-master` | stock metadata, phân loại cổ phiếu, index basket, trạng thái giao dịch |
| `trading-order` | tiếp nhận lệnh, validation, lifecycle, execution |
| `market-data` | cập nhật giá và reference data |
| `holdings-pnl` | positions, portfolio, valuation, realized/unrealized P&L |
| `risk-compliance` | rule engine, alerts, approvals, restrictions |
| `ledger-accounting` | cash/securities ledger bất biến, fees, reconciliation |
| `operations-control` | failed events, close-of-day, statements, exception handling |
| `audit-evidence` | audit logs, hash chain, evidence export |

## Nguyên tắc thiết kế dữ liệu

- Dùng relational storage cho authoritative records.
- Xem ledger entries và audit records là immutable facts.
- Tách rõ authoritative master data của `IssuerCompany`, `Stock`, `StockClassification`, và `IndexBasket` khỏi các derived portfolio views.
- Xây read models / materialized views cho balances, holdings, và dashboards.
- Áp dụng hash-chain theo stream hoặc batch để cung cấp khả năng phát hiện chỉnh sửa.
- Dùng outbox pattern để publish event đáng tin cậy từ các transactional changes.

## Tích hợp bên ngoài

| Tích hợp | Mục đích | Quan điểm ban đầu |
| --- | --- | --- |
| `eKYC Provider` | xác minh danh tính | bắt buộc trước khi cho phép live money movement |
| `Banknet/Payment Rail` | luồng nạp và rút tiền | trừu tượng hóa qua adapter interface |
| `Market Data Provider` | cập nhật giá | bắt buộc cho valuation và P&L |
| `Notification Provider` | gửi cảnh báo và statement | có thể để optional ở thin slice đầu tiên |

## Rủi ro kiến trúc chính

- Execution model của trade còn mơ hồ sẽ ảnh hưởng trực tiếp tới order lifecycle và thiết kế ledger.
- Yêu cầu append-only dễ bị hiểu sai nếu không tách rõ dữ liệu vận hành mutable với financial truth immutable.
- Payment và event idempotency là bắt buộc; thiết kế yếu ở đây sẽ làm sai số dư.
- Risk rule configuration có thể bị over-engineer nếu không giới hạn ban đầu vào một rule DSL tối thiểu.
