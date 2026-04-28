# Bộ Tài Liệu FinTradeCore

## Danh sách file đề xuất

| File | Mục đích |
| --- | --- |
| `docs/architecture/01-discovery-summary.md` | Tóm tắt discovery: bài toán, mục tiêu, out of scope, giả định, open issues |
| `docs/architecture/02-system-overview.md` | Tổng quan hệ thống, actor, use case, ranh giới phạm vi |
| `docs/architecture/03-requirements-catalog.md` | Danh mục functional requirements, non-functional requirements, business rules, priority và difficulty |
| `docs/architecture/04-domain-and-architecture.md` | Domain model sơ bộ và kiến trúc high-level được khuyến nghị |
| `docs/architecture/05-e2e-flows.md` | Các luồng nghiệp vụ và vận hành end-to-end chính |
| `docs/architecture/06-ticketization.md` | Phân rã initiative thành epic -> feature -> ticket kèm dependency và acceptance criteria |
| `docs/architecture/07-requirement-feature-ticket-mapping.md` | Ma trận truy vết requirement -> feature -> ticket |
| `docs/architecture/08-open-issues-tracker.md` | Danh sách câu hỏi mở và khoảng trống cần chốt với business/architect |
| `docs/architecture/09-delivery-transition.md` | Cách chuyển từ tài liệu architecture sang luồng delivery theo SDD |
| `docs/changes/FTC-ARCH-001/spec-pack.md` | Spec pack đầu tiên được khuyến nghị cần đặc tả chi tiết trước khi implementation |

## Ghi chú

- Bộ tài liệu này tách rõ `Facts`, `Assumptions`, và `Open Issues`.
- Ticketization được chia pha có chủ đích. Những vùng còn mơ hồ sẽ được biểu diễn bằng ticket discovery/spec trước khi tạo ticket implementation.
- Kiến trúc khuyến nghị là modular monolith với append-only ledger và audit có khả năng phát hiện chỉnh sửa, không phải blockchain hoàn chỉnh ngay từ đầu.
