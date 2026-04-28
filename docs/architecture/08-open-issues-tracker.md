# Open Issues Tracker FinTradeCore

Đây là file tracking làm việc cho các quyết định sản phẩm và kiến trúc còn thiếu hoặc chưa chốt.

| ID | Area | Câu hỏi / khoảng trống | Vì sao quan trọng | Suggested owner | Status |
| --- | --- | --- | --- | --- | --- |
| `OI-001` | Trading model | Phase 1 là simulated trading, internal matching, hay real exchange connectivity? | Quyết định order lifecycle, settlement, reconciliation, và risk design | Business + Architect | Open |
| `OI-002` | Regulatory posture | Đây là learning/demo platform, internal training platform, hay lõi brokerage tiền production? | Làm thay đổi nghĩa vụ compliance, KYC, audit, và data retention | Business | Open |
| `OI-003` | Payment rail | `banknet` cụ thể là provider/protocol nào? | Cần để thiết kế callback model, failure handling, và reconciliation | Business + Architect | Open |
| `OI-004` | KYC | Những trường dữ liệu KYC và chứng từ nào phải lưu nội bộ? | Ảnh hưởng tới privacy, security, và contract với provider | Business + Security | Open |
| `OI-005` | Order cancellation | User có được hủy order đã accepted không? | Ảnh hưởng trực tiếp tới state machine và correction policy theo append-only | Business | Open |
| `OI-006` | Fee policy | Fee áp dụng cho trade và withdrawal theo mô hình nào? | Cần cho ledger postings và độ chính xác của P&L | Business + Finance | Open |
| `OI-007` | Large transaction rule | Threshold hoặc dynamic rule nào kích hoạt approval? | Đang chặn thiết kế approval workflow | Risk | Open |
| `OI-008` | Market data | Nguồn market prices là gì và tần suất cập nhật bao nhiêu? | Bắt buộc cho valuation, P&L, và EOD close | Architect | Open |
| `OI-009` | Security listing | "Approve stock listing" được hiểu chính xác thế nào trong ngữ cảnh platform này? | Làm rõ đây là security onboarding hay exchange listing thật | Business | Open |
| `OI-010` | Wallet semantics | Cash được reserve lúc order accepted hay chỉ move lúc execution? | Cực kỳ quan trọng cho available balance calculation | Architect | Open |
| `OI-011` | Holdings semantics | Partial fills và settlement delays có nằm trong scope không? | Ảnh hưởng positions, statements, và reconciliation | Architect | Open |
| `OI-012` | Audit transparency | Ai được xem phần nào của audit và ledger data? | Cần cho RBAC và privacy control | Security + Compliance | Open |
| `OI-013` | Complaint workflow | Xử lý khiếu nại chỉ là triage vận hành hay là dispute process chính thức? | Ảnh hưởng artifact retention và case model | Operations | Open |
| `OI-014` | Close of day | Business day boundary và timezone policy là gì? | Cần cho statement và batch cut-off | Operations | Open |
| `OI-015` | Data immutability | Append-only áp dụng cho mọi bảng hay chủ yếu cho financial truth tables? | Tránh over-constrain thiết kế dữ liệu vận hành | Architect | Open |
| `OI-016` | Listing criteria | Bộ tiêu chí duyệt công ty và tạo `Stock` gồm những điều kiện nào? | Chặn thiết kế issuer/listing workflow | Business + Architect | Open |
| `OI-017` | Stock taxonomy | Taxonomy cổ phiếu có bao nhiêu chiều: ngành, chỉ số, theme, vốn hóa? | Ảnh hưởng domain classification và portfolio analytics | Business | Open |
| `OI-018` | Index membership | Membership như `VN30` là dữ liệu nhập tay hay đồng bộ từ nguồn ngoài? | Tác động tới stock classification và market data design | Business + Architect | Open |
| `OI-019` | Portfolio scope | Portfolio chỉ để xem tổng hợp hay cần benchmark/rebalancing/performance chart? | Ảnh hưởng đáng kể tới holdings-pnl module | Business | Open |

## Thứ tự review tiếp theo được khuyến nghị

1. `OI-001`, `OI-003`, `OI-010`, `OI-015`
2. `OI-004`, `OI-006`, `OI-007`, `OI-008`
3. `OI-005`, `OI-009`, `OI-011`, `OI-014`
4. `OI-012`, `OI-013`
