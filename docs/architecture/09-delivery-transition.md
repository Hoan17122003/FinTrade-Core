# Chuyển Giao Sang Delivery Theo SDD

## Phần nào nên nằm trong `docs/architecture/`

Các nội dung ở cấp hệ thống, dùng chung nhiều ticket, nên được giữ trong `docs/architecture/`:

- Discovery summary
- Ranh giới phạm vi
- Actor/use case model
- Danh mục functional và non-functional requirements
- Business rules và constraints
- Domain model
- High-level architecture được khuyến nghị
- Các end-to-end flows xuyên module
- Ticketization ở cấp initiative
- Requirement traceability
- Open issues tracker

## Phần nào nên nằm trong `docs/changes/<TICKET>/spec-pack.md`

Các nội dung sau nên đi vào spec pack của từng ticket:

- Problem statement chính xác cho một ticket
- Scope và non-scope cụ thể
- Flow/state machine chi tiết của ticket đó
- Data contract changes
- Chi tiết integration contracts
- Business rule decisions riêng của ticket
- Edge cases và failure modes
- Acceptance criteria cho implementation
- Phác thảo test strategy
- Những câu hỏi chỉ đang chặn riêng ticket đó

## Ticket đầu tiên nên đặc tả chi tiết

Khuyến nghị `FTC-ARCH-001 Define financial truth and append-only model`.

Lý do:

- Gần như mọi capability còn lại đều phụ thuộc vào ticket này.
- Ràng buộc append-only là yêu cầu kiến trúc mạnh nhất trong input.
- Nếu phần này sai, balances, holdings, fees, auditability, reconciliation, và risk evidence đều sẽ sai theo.

## Rủi ro lớn nhất nếu triển khai ngay

- Xây trading/payment features trước khi chốt financial system of record
- Hiểu "blockchain-like" như một ràng buộc công nghệ thay vì một yêu cầu về trust/audit
- Trộn mutable operational state với immutable financial truth
- Đặc tả idempotency không đủ chặt, gây duplicate deposit, withdrawal, hoặc ledger postings
- Bắt đầu P&L và reporting khi price source và execution assumptions chưa được khóa
- Tạo implementation tickets cho các workflow còn mơ hồ như approve security listing và approve large transaction
- Tạo implementation tickets trước khi chốt issuer/listing criteria, stock taxonomy, và portfolio scope

## Lộ trình SDD được khuyến nghị

1. Chốt các open issues có ảnh hưởng trực tiếp tới system-of-record.
2. Đặc tả chi tiết `FTC-ARCH-001`.
3. Đặc tả tiếp `FTC-ARCH-002`, `FTC-ARCH-003`, `FTC-ARCH-004`.
4. Chỉ sau đó mới tách tiếp thành implementation tickets.
