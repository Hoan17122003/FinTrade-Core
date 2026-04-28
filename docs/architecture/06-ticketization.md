# Ticketization FinTradeCore

## Nguyên tắc sequencing

Không nên tạo ticket implementation cho trading, payments, hoặc P&L khi system-of-record và các giả định execution vẫn còn mơ hồ. Làn sóng đầu tiên nên là các ticket architecture/specification để loại bỏ ambiguity.

## Epics

| Epic | Objective | Priority |
| --- | --- | --- |
| `FTC-E01` | Nền tảng kiến trúc và financial truth | Must |
| `FTC-E02` | Identity, access, và onboarding KYC | Must |
| `FTC-E03` | Wallet và payment operations | Must |
| `FTC-E04` | Trading và holdings | Must |
| `FTC-E05` | Risk và compliance controls | Must |
| `FTC-E06` | Ledger, finance, và reporting | Must |
| `FTC-E07` | Resilience vận hành và observability | Should |

## Features và tickets

### Epic `FTC-E01` Nền tảng kiến trúc và financial truth

| Ticket Key | Title | Objective | Scope | Dependencies | Acceptance criteria | Required artifacts before implementation |
| --- | --- | --- | --- | --- | --- | --- |
| `FTC-ARCH-001` | Define financial truth and append-only model | Chốt authoritative data model và correction strategy | ledger facts, read models, compensating entries, state derivation | none | reviewed spec định nghĩa rõ immutable record strategy, correction model, ownership của balances/positions | discovery summary, domain model, open issues list |
| `FTC-ARCH-002` | Define trading execution model | Quyết định phase 1 dùng simulated fills, internal matching, hay external execution | order states, fill source, reserve/settlement assumptions | `FTC-ARCH-001` | reviewed spec định nghĩa execution lifecycle và order state transitions | `FTC-ARCH-001` |
| `FTC-ARCH-003` | Define integration contracts | Thiết lập contract cho eKYC, payment rail, market data, notifications | request/response, callback, idempotency, failure handling | `FTC-ARCH-001` | adapter contracts và sequence diagrams được duyệt | `FTC-ARCH-001`, `FTC-ARCH-002` |
| `FTC-ARCH-004` | Define security and RBAC model | Làm rõ quyền của actor và privileged actions | roles, action matrix, audit obligations | `FTC-ARCH-001` | role matrix và danh sách privileged actions được duyệt | discovery summary |

### Epic `FTC-E02` Identity, access, và onboarding KYC

| Ticket Key | Title | Objective | Scope | Dependencies | Acceptance criteria | Required artifacts before implementation |
| --- | --- | --- | --- | --- | --- | --- |
| `FTC-ID-001` | User registration and login spec | Đặc tả auth flows và account states | registration, login, password/session assumptions | `FTC-ARCH-004` | flow, states, và edge cases được tài liệu hóa | RBAC model |
| `FTC-ID-002` | eKYC onboarding spec | Định nghĩa vòng đời gửi KYC và nhận kết quả verify | provider callbacks, manual review, failure states | `FTC-ARCH-003`, `FTC-ID-001` | KYC lifecycle và local data model được duyệt | integration contract, account states |
| `FTC-ID-003` | Account restriction controls | Đặc tả hành vi lock/unlock/freeze | admin action, risk action, audit trace | `FTC-ARCH-004` | restriction matrix được duyệt | RBAC model, risk assumptions |

### Epic `FTC-E03` Wallet và payment operations

| Ticket Key | Title | Objective | Scope | Dependencies | Acceptance criteria | Required artifacts before implementation |
| --- | --- | --- | --- | --- | --- | --- |
| `FTC-PAY-001` | Deposit flow spec | Định nghĩa deposit intent, confirmation, ledgering, và reconciliation hooks | payment lifecycle, idempotency, exception states | `FTC-ARCH-001`, `FTC-ARCH-003` | deposit state machine và ledger effect được duyệt | append-only model, payment contract |
| `FTC-PAY-002` | Withdrawal flow spec | Định nghĩa validation, approval, payout, và retry handling của withdrawal | funds check, approval step, payment statuses | `FTC-PAY-001`, `FTC-RISK-002` | withdrawal policy và failure handling được duyệt | deposit flow, risk thresholds |
| `FTC-PAY-003` | Wallet balance read model | Định nghĩa cách suy ra và expose balances | balance derivation, reserved cash, available cash | `FTC-ARCH-001`, `FTC-ARCH-002` | read model equations được duyệt | financial truth model, execution model |

### Epic `FTC-E04` Trading và holdings

| Ticket Key | Title | Objective | Scope | Dependencies | Acceptance criteria | Required artifacts before implementation |
| --- | --- | --- | --- | --- | --- | --- |
| `FTC-TRD-001` | Stock master, issuer, và listing workflow spec | Định nghĩa company/stock model và listing approval | issuer profile, listing criteria, stock metadata, activation | `FTC-ARCH-003` | issuer lifecycle, stock lifecycle, và listing workflow được duyệt | integration contracts |
| `FTC-TRD-001A` | Stock classification và index basket spec | Định nghĩa taxonomy cổ phiếu và membership vào các rổ/chỉ số | sector, index, thematic tags, market-cap bands | `FTC-TRD-001` | classification model và membership rules được duyệt | stock master spec |
| `FTC-TRD-002` | Order lifecycle spec | Định nghĩa validation và state transitions cho buy/sell order | states, acceptance/rejection, cancellation policy | `FTC-ARCH-002`, `FTC-ARCH-004` | order state machine được duyệt | execution model, RBAC model |
| `FTC-TRD-003` | Trade ledger impact spec | Định nghĩa ledger effects của fill, fee, và reversal | cash movement, holdings movement, fees | `FTC-ARCH-001`, `FTC-TRD-002` | fill-to-ledger mapping được duyệt | append-only model, order lifecycle |
| `FTC-TRD-004` | Holdings, portfolio, và transaction history spec | Định nghĩa derived views cho holdings/history/portfolio | aggregation, query model, allocation breakdowns, history filters | `FTC-TRD-003`, `FTC-TRD-001A` | derived model cho holdings và portfolio được duyệt | trade ledger impacts, classification model |
| `FTC-TRD-005` | P&L calculation spec | Định nghĩa công thức realized/unrealized P&L và quy tắc batch recalculation | valuation, price source, recalculation semantics | `FTC-TRD-004`, `FTC-FIN-002` | công thức P&L và edge cases được duyệt | holdings model, market data rules |
| `FTC-TRD-006` | Stock detail và watchlist spec | Định nghĩa stock detail view và watchlist của investor | issuer info, classifications, watchlist actions | `FTC-TRD-001`, `FTC-TRD-001A` | stock detail fields và watchlist flow được duyệt | stock master, classification model |

### Epic `FTC-E05` Risk và compliance controls

| Ticket Key | Title | Objective | Scope | Dependencies | Acceptance criteria | Required artifacts before implementation |
| --- | --- | --- | --- | --- | --- | --- |
| `FTC-RISK-001` | Risk rule framework spec | Định nghĩa rule model tối thiểu, versioning, và actions | thresholds, event inputs, alert outputs | `FTC-ARCH-001`, `FTC-ARCH-004` | rule model và versioning được duyệt | financial truth model, RBAC model |
| `FTC-RISK-002` | Large transaction approval spec | Định nghĩa threshold và approval workflow | approval states, SLA, evidence capture | `FTC-RISK-001` | approval workflow được duyệt | risk rule framework |
| `FTC-RISK-003` | Suspicious activity monitoring spec | Định nghĩa scope của unusual order/transaction detection | triggers, signals, cases, freeze actions | `FTC-RISK-001`, `FTC-TRD-002`, `FTC-PAY-001` | suspicious activity policy được duyệt | rule framework, order flow, payment flow |
| `FTC-RISK-004` | Risk reporting and audit evidence spec | Định nghĩa output của risk report và evidence retention | reports, export, audit linkage | `FTC-RISK-001` | report definitions được duyệt | rule framework |

### Epic `FTC-E06` Ledger, finance, và reporting

| Ticket Key | Title | Objective | Scope | Dependencies | Acceptance criteria | Required artifacts before implementation |
| --- | --- | --- | --- | --- | --- | --- |
| `FTC-FIN-001` | Reconciliation model spec | Định nghĩa logic đối soát internal/external | sources, matching, exception handling | `FTC-ARCH-001`, `FTC-ARCH-003` | reconciliation control points được duyệt | append-only model, external contracts |
| `FTC-FIN-002` | Market data and valuation spec | Định nghĩa cập nhật giá, timing valuation, và source rules | price validity, close price, stale data handling | `FTC-ARCH-003` | valuation data policy được duyệt | market data contract |
| `FTC-FIN-003` | Fee model spec | Định nghĩa fee formulas và revenue recognition | charge timing, rounding, reversals | `FTC-TRD-003` | fee model được duyệt | trade ledger impact spec |
| `FTC-FIN-004` | Close-of-day and statement spec | Định nghĩa EOD batches và statement output | batch sequence, evidence, retry, statement content | `FTC-FIN-001`, `FTC-FIN-002`, `FTC-TRD-005` | EOD workflow được duyệt | reconciliation, valuation, P&L specs |

### Epic `FTC-E07` Resilience vận hành và observability

| Ticket Key | Title | Objective | Scope | Dependencies | Acceptance criteria | Required artifacts before implementation |
| --- | --- | --- | --- | --- | --- | --- |
| `FTC-OPS-001` | Failed event handling spec | Định nghĩa capture lỗi event và retry safety | outbox, dead-letter, retry policy | `FTC-ARCH-003` | idempotent retry policy được duyệt | integration contracts |
| `FTC-OPS-002` | Complaint management spec | Định nghĩa workflow khiếu nại tối thiểu | intake, triage, linkage tới transactions | `FTC-ARCH-004`, `FTC-TRD-004`, `FTC-PAY-001` | complaint lifecycle được duyệt | RBAC model, transaction history, payment flow |
| `FTC-OPS-003` | Operational audit log spec | Định nghĩa actor/action logging và evidence export | privileged actions, filters, retention | `FTC-ARCH-001`, `FTC-ARCH-004` | audit taxonomy được duyệt | append-only model, RBAC model |

## Thứ tự thực hiện được khuyến nghị

1. `FTC-ARCH-001`
2. `FTC-ARCH-002`
3. `FTC-ARCH-003`
4. `FTC-ARCH-004`
5. `FTC-RISK-001`
6. `FTC-PAY-001`
7. `FTC-TRD-002`
8. `FTC-TRD-003`
9. `FTC-FIN-001`
10. Các spec còn lại theo dependency chain

Chỉ sau khi các ticket trên ổn định mới nên sinh implementation tickets tương ứng.
