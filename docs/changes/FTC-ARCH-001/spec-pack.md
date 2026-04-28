# FTC-ARCH-001 Spec Pack

## Title

Define financial truth and append-only model

## Objective

Đặc tả authoritative data model cho tiền, holdings, fees, và auditability để các module phía sau có thể được triển khai mà không vi phạm ràng buộc append-only.

## Facts

- Input nêu rõ yêu cầu minh bạch.
- Input nêu rõ transaction data chỉ được thêm và đọc.
- Nhiều capability phía sau phụ thuộc vào balances, holdings, audit, reconciliation, và P&L.

## Assumptions

- Append-only áp dụng chủ yếu cho financial truth và audit records, không nhất thiết cho mọi projection table.
- Read models có thể rebuild từ immutable facts.
- Corrections được biểu diễn bằng compensating entries hoặc reversal events, không phải mutation trực tiếp trên record cũ.

## Open issues

- Balance reservation diễn ra lúc order acceptance hay chỉ khi execution
- Holdings dùng semantics theo trade-date hay settlement-date ở phase 1
- Order cancellation có nằm trong scope phase 1 không
- Nên dùng một ledger thống nhất hay tách riêng cash ledger và securities ledger

## Scope

- Định nghĩa các loại authoritative records
- Định nghĩa cái gì là immutable và cái gì là derived
- Định nghĩa correction/reversal strategy
- Định nghĩa yêu cầu tối thiểu về audit linkage
- Định nghĩa nguyên tắc suy ra balances và positions

## Out of scope

- Database schema cụ thể
- Exchange connectivity
- Định nghĩa chi tiết UI/API payload
- Công thức fee chính xác

## Những quyết định cần chốt trong spec này

1. Ledger model
2. Read model strategy
3. Correction model
4. Balance availability model
5. Audit/tamper-evidence model

## Hướng thiết kế được khuyến nghị

- Tách các immutable fact streams:
  - `cash_ledger_entry`
  - `securities_ledger_entry`
  - `fee_entry`
  - `audit_log_entry`
- Duy trì mutable projections cho:
  - available cash
  - reserved cash
  - current holdings
  - realized/unrealized P&L snapshots
- Dùng compensating entries cho reversal và correction.
- Dùng hash-chain cho immutable entries theo stream hoặc batch để tạo tamper evidence.
- Gắn `correlation_id`, `causation_id`, `actor_id`, và `business_date` cho mọi immutable fact.

## Preliminary acceptance criteria

- Spec định nghĩa được authoritative financial record types và ownership boundaries.
- Spec định nghĩa record nào là immutable và record nào là derived projections.
- Spec định nghĩa deposit, withdrawal, trade fill, fee, và correction tác động thế nào lên ledger facts.
- Spec định nghĩa metadata bắt buộc cho traceability và tamper evidence.
- Spec có ít nhất một ví dụ được duyệt về cách sửa một transaction sai mà không dùng mutation.

## Input artifacts required

- [docs/architecture/01-discovery-summary.md](/D:/20-ProjectPersonal/01-Fintrade/30-Coding/core/docs/architecture/01-discovery-summary.md)
- [docs/architecture/04-domain-and-architecture.md](/D:/20-ProjectPersonal/01-Fintrade/30-Coding/core/docs/architecture/04-domain-and-architecture.md)
- [docs/architecture/08-open-issues-tracker.md](/D:/20-ProjectPersonal/01-Fintrade/30-Coding/core/docs/architecture/08-open-issues-tracker.md)
