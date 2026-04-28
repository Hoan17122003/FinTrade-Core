# Danh Mục Requirements FinTradeCore

## Functional requirements

| ID | Requirement | Priority | Difficulty | Ghi chú |
| --- | --- | --- | --- | --- |
| `FR-001` | User có thể đăng ký và đăng nhập | Must | Medium | Phụ thuộc chính sách identity và KYC flow |
| `FR-002` | User có thể hoàn tất eKYC verification | Must | High | Có tích hợp ngoài; retention của dữ liệu còn TBD |
| `FR-003` | User có thể nạp tiền | Must | High | Semantics của payment rail còn TBD |
| `FR-004` | User có thể rút tiền | Must | High | Cần giới hạn, approval, anti-fraud checks |
| `FR-005` | User có thể xem số dư tiền trong ví | Must | Low | Được suy ra từ append-only ledger |
| `FR-006` | User có thể đặt lệnh mua/bán | Must | High | Cần định nghĩa rõ order lifecycle |
| `FR-007` | User có thể xem tài sản nắm giữ hiện tại | Must | Medium | Cần cơ chế suy diễn position |
| `FR-008` | User có thể xem lịch sử giao dịch | Must | Medium | Trace append-only của event |
| `FR-009` | User có thể xem P&L | Should | High | Chính sách realized/unrealized còn TBD |
| `FR-010` | User nhận được cảnh báo risk/tài khoản | Should | Medium | Kênh gửi cảnh báo còn TBD |
| `FR-011` | Admin có thể xem danh sách user | Must | Low | Chức năng backoffice |
| `FR-012` | Admin có thể kiểm tra giao dịch lỗi | Must | Medium | Cần phân loại lỗi |
| `FR-013` | Admin có thể khóa/mở khóa tài khoản | Must | Medium | Không được làm sai lệch ledger state |
| `FR-014` | Admin có thể xử lý khiếu nại | Could | Medium | Phạm vi workflow còn TBD |
| `FR-015` | Ops có thể theo dõi order bất thường | Should | High | Cần rules và detection signals |
| `FR-016` | Ops có thể theo dõi payment thất bại | Must | Medium | Cần payment event model |
| `FR-017` | Nhân sự được phân quyền có thể xem audit logs | Must | Medium | Cần tamper evidence |
| `FR-018` | Risk officer có thể cấu hình risk rules | Must | High | Cần versioned rule model |
| `FR-019` | Risk officer có thể theo dõi giao dịch đáng ngờ | Must | High | Giai đoạn đầu là AML-lite |
| `FR-020` | Giao dịch lớn cần approval | Should | High | Ngưỡng và SLA còn TBD |
| `FR-021` | Risk officer có thể xem risk reports | Should | Medium | Phụ thuộc output của rule engine |
| `FR-022` | Hệ thống có thể khóa giao dịch khi user vi phạm rule | Must | High | Phải giữ được auditability |
| `FR-023` | Hệ thống ghi ledger entries cho mọi money/trade events | Must | High | Yêu cầu lõi về integrity |
| `FR-024` | Hệ thống theo dõi dòng tiền nạp/rút | Must | Medium | Phụ thuộc báo cáo tài chính |
| `FR-025` | Hệ thống tính phí giao dịch | Should | Medium | Fee model còn TBD |
| `FR-026` | Hệ thống báo cáo doanh thu phí | Should | Medium | Phụ thuộc fee calculation |
| `FR-027` | Hệ thống hiển thị số dư platform | Should | Medium | Cách hiểu treasury còn TBD |
| `FR-028` | Hệ thống tạo báo cáo cuối ngày | Should | High | Cần quy trình close |
| `FR-029` | Hệ thống thực hiện reconciliation | Must | High | Cần định nghĩa nguồn internal/external |
| `FR-030` | Hệ thống cập nhật giá thị trường | Must | Medium | Market data source còn TBD |
| `FR-031` | Hệ thống thực hiện đóng sổ cuối ngày | Should | High | Close policy phải được đặc tả |
| `FR-032` | Hệ thống tạo daily statement | Should | Medium | Format output còn TBD |
| `FR-033` | Hệ thống tính lại P&L | Could | High | Thường chạy batch và theo corrective semantics |
| `FR-034` | Hệ thống quét giao dịch bất thường | Should | High | Dùng chung rule model với risk module |
| `FR-035` | Ops có thể retry failed events | Must | Medium | Cần event handling idempotent |
| `FR-036` | Admin có thể phê duyệt onboarding chứng khoán | Could | High | Cần làm rõ listing workflow |
| `FR-037` | Hệ thống lưu transaction history trong immutable ledger | Must | High | Cùng nguyên tắc lõi với FR-023 |
| `FR-045` | Admin có thể quản lý công ty phát hành chứng khoán | Should | Medium | Cần issuer profile và trạng thái phê duyệt |
| `FR-046` | Hệ thống có thể tạo entity `Stock` sau khi công ty đáp ứng tiêu chí listing | Must | High | Cần listing criteria và stock lifecycle |
| `FR-047` | Hệ thống quản lý phân loại cổ phiếu theo ngành, chỉ số, nhóm chủ đề | Should | Medium | Ví dụ: Công nghệ, Ngân hàng, VN30 |
| `FR-048` | User có thể xem thông tin chi tiết cổ phiếu và công ty phát hành | Should | Medium | Gần với trải nghiệm sàn chứng khoán thật |
| `FR-049` | User có thể xem portfolio đầu tư tổng hợp | Must | Medium | Bao gồm cash, holdings, allocation, P&L |
| `FR-050` | User có thể theo dõi watchlist cổ phiếu quan tâm | Could | Low | Hữu ích cho trải nghiệm investor |
| `FR-051` | Hệ thống quản lý thành phần rổ/chỉ số tham chiếu như VN30 | Could | Medium | Có giá trị cho phân loại và portfolio analytics |

## Additional recommended requirements

| ID | Requirement | Priority | Difficulty | Lý do bổ sung |
| --- | --- | --- | --- | --- |
| `FR-038` | Role-based access control cho user/admin/risk personas | Must | Medium | Cần để tách biệt nhiệm vụ an toàn |
| `FR-039` | Hiển thị order lifecycle: submitted, validated, accepted, rejected, executed, cancelled, settled | Must | High | Không thể thiết kế trading nếu thiếu state model |
| `FR-040` | Idempotency cho payment và event processing | Must | High | Tránh side effect tài chính bị lặp |
| `FR-041` | Security master cho danh mục mã giao dịch | Must | Medium | Cần cho holdings, pricing, order validation |
| `FR-042` | Notification framework cho user và ops alerts | Should | Medium | Cần để alert có tính hành động |
| `FR-043` | Suspense / exception queue cho financial events không khớp | Should | Medium | Quan trọng cho reconciliation và failed payments |
| `FR-044` | Data retention và evidence export cho audit/compliance | Should | Medium | Hỗ trợ điều tra và khiếu nại |
| `FR-045A` | Listing criteria engine/rule set cho việc duyệt công ty và tạo stock | Should | High | Cần để mô hình hóa nghiệp vụ niêm yết nội bộ |
| `FR-046A` | Portfolio analytics cơ bản: allocation theo ngành/mã, tổng giá trị, lãi/lỗ | Should | Medium | Bổ trợ trải nghiệm như sàn thật |

## Non-functional requirements

| ID | Requirement |
| --- | --- |
| `NFR-001` | Mọi giao dịch tài chính phải append-only; không destructive update/delete trên ledger facts |
| `NFR-002` | Derived read models có thể được rebuild từ immutable events/entries |
| `NFR-003` | Dữ liệu nhạy cảm phải được mã hóa khi truyền và khi lưu |
| `NFR-004` | Ledger và audit records phải có khả năng phát hiện chỉnh sửa |
| `NFR-005` | Mọi hành động hệ thống phải truy vết được actor, thời gian, và lý do |
| `NFR-006` | Monetary operations phải idempotent và traceable end-to-end |
| `NFR-007` | Privileged actions phải được phân quyền và audit đầy đủ |
| `NFR-008` | End-of-day jobs phải replayable hoặc compensatable mà không làm sai lệch financial truth |
| `NFR-009` | Thiết kế nên ưu tiên tính module và dễ học hơn là phân tán hóa quá sớm |

## Business rules

- Financial truth được biểu diễn bằng immutable entries; mọi correction phải thực hiện qua compensating entries, không overwrite lịch sử.
- User không được đặt buy order khi không đủ eligible cash balance.
- User không được sell nhiều hơn eligible holdings đang có.
- `Stock` chỉ được phép giao dịch khi ở trạng thái active và công ty phát hành đang hợp lệ.
- Mỗi `Stock` phải thuộc đúng một công ty phát hành nhưng có thể thuộc nhiều nhóm phân loại như ngành, chỉ số, thematic tag.
- Portfolio là thực thể tổng hợp được suy diễn từ holdings, cash balance, market prices, và các quy tắc valuation; không được trở thành nguồn financial truth độc lập.
- User có KYC thất bại, tài khoản bị freeze, hoặc đang vi phạm rule thì không được trade hoặc withdraw.
- Các khoản rút lớn hoặc giao dịch lớn có thể yêu cầu approval bổ sung, chờ định nghĩa ngưỡng cụ thể.
- Mọi accepted order, trade fill, fee, deposit, withdrawal, và adjustment đều phải tạo ledger record và audit record.
- Risk rules phải có version để có thể truy vết alert/decision theo đúng bộ rule đang hiệu lực tại thời điểm đó.
- Chênh lệch reconciliation phải đi vào exception queue để operations review.
- Failed asynchronous processing phải retry an toàn mà không sinh duplicate side effects.
