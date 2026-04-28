# Tóm Tắt Discovery FinTradeCore

## Tóm tắt bài toán

FinTradeCore được định hướng là một nền tảng giao dịch chứng khoán quy mô nhỏ phục vụ học tập và thực hành delivery. Hệ thống cần hỗ trợ giao dịch minh bạch, mua/bán cổ phiếu, nạp/rút tiền, giám sát rủi ro, kiểm soát vận hành, đối soát, báo cáo, và ghi nhận giao dịch theo mô hình append-only.

## Facts

- Tên initiative / sản phẩm: `FinTradeCore`
- Actor chính: `Retail Investor / End User`, `Admin / Operations Staff`, `Compliance / Risk Officer`
- Các khả năng nghiệp vụ được nêu rõ trong input:
  - Đăng ký và đăng nhập
  - Nạp tiền / rút tiền / xem số dư ví
  - Đặt lệnh mua/bán
  - Xem tài sản nắm giữ, lịch sử giao dịch, P&L
  - Nhận cảnh báo rủi ro
  - Quản lý user, xử lý giao dịch lỗi, khóa/mở khóa tài khoản
  - Xử lý khiếu nại
  - Theo dõi order bất thường
  - Theo dõi payment thất bại
  - Xem audit log
  - Cấu hình risk rule
  - Theo dõi giao dịch đáng ngờ
  - Phê duyệt giao dịch lớn
  - Xem báo cáo rủi ro
  - Khóa giao dịch nếu user vi phạm rule
  - Ledger entries, dòng tiền nạp/rút, phí giao dịch, doanh thu phí, số dư platform
  - Báo cáo cuối ngày, reconciliation, cập nhật giá thị trường, đóng sổ cuối ngày, daily statement, tính lại P&L
  - Quét giao dịch bất thường, retry failed event
  - Duyệt xử lý cổ phiếu lên sàn của một công ty
  - Ghi dữ liệu transaction vào ledger
- Người dùng bổ sung thêm định hướng domain:
  - Có entity `Stock` rõ ràng để tạo cổ phiếu cho một công ty sau khi được duyệt thành công
  - Có các domain xung quanh cổ phiếu như `Portfolio`
  - Có phân loại cổ phiếu theo nhóm/ngành/chỉ số như công nghệ, ngân hàng, `VN30`
  - Hệ thống nên gần với cách vận hành của một sàn chứng khoán thật hơn
- Ràng buộc phi chức năng được nêu trong input: dữ liệu giao dịch phải minh bạch; toàn bộ giao dịch chỉ được thêm và đọc, không được xóa/sửa phá hủy lịch sử
- Tích hợp dự kiến từ input:
  - eKYC cho xác thực định danh
  - mã hóa toàn bộ giao dịch, "tương tự blockchain"
- Người dùng cũng yêu cầu bổ sung các capability còn thiếu nhưng cần thiết

## Mục tiêu nghiệp vụ

- Xây dựng một nền tảng giao dịch kiểu brokerage ở quy mô nhỏ nhưng đủ thực tế để học và nâng cấp năng lực triển khai.
- Đảm bảo độ tin cậy thông qua ghi nhận giao dịch minh bạch, auditability, và kiểm soát vận hành rõ ràng.
- Cho phép business, operations, và risk cùng làm việc trên một nguồn dữ liệu đáng tin cậy.
- Tạo bộ tài liệu có thể bàn giao sang quy trình delivery theo SDD.
- Mô hình hóa được domain chứng khoán đủ sát thực tế để học được cách vận hành issuer, stock listing, phân nhóm cổ phiếu, portfolio, và luồng đầu tư của nhà đầu tư.

## Out of scope cho bản phát hành đầu tiên

- Tích hợp trực tiếp với sở giao dịch hoặc trung tâm lưu ký thật
- Hạ tầng giao dịch tần suất cao hoặc độ trễ siêu thấp
- Chứng nhận pháp lý đầy đủ cho một công ty chứng khoán production
- Corporate actions vượt quá mức tối thiểu giữ chỗ
- Margin trading, options, derivatives, short selling
- Hỗ trợ thuế và pháp lý đa quốc gia
- Ứng dụng mobile native
- Blockchain network hoàn chỉnh, tokenization, hoặc smart contract runtime

## Giả định chính

- Phạm vi ban đầu là nền tảng học tập / productization, chưa phải brokerage được cấp phép đầy đủ.
- Giao dịch ở giai đoạn đầu sẽ bắt đầu bằng mô hình quản lý lệnh và khớp lệnh nội bộ hoặc mô phỏng, cho đến khi có yêu cầu rõ ràng về kết nối sàn thật.
- "Minh bạch kiểu blockchain" trước hết sẽ được hiện thực bằng append-only ledger có hash-chain, audit log mạnh, và cơ chế kiểm chứng mật mã, vì cách này phù hợp và đơn giản hơn nhiều so với việc đưa blockchain phân tán vào phase 1.
- Hệ thống sẽ dùng corrective entries và state derivation thay vì update/delete lên transaction tài chính.
- Tiền của người dùng sẽ được biểu diễn bằng ví nội bộ / cash ledger, còn nạp-rút ngoài hệ thống đi qua adapter payment kiểu banknet.
- "Duyệt cổ phiếu lên sàn" được hiểu là workflow nội bộ để duyệt mã chứng khoán được phép giao dịch trên platform, không phải quy trình niêm yết thật trên sở giao dịch.
- Mỗi cổ phiếu sẽ gắn với một công ty phát hành và chỉ được tạo/active sau khi vượt qua bộ tiêu chí niêm yết nội bộ của platform.
- Portfolio của user là lớp domain riêng, được cấu thành từ holdings, cash allocation, watchlist, và các chỉ số hiệu quả đầu tư.

## Open issues

- Bản phát hành đầu tiên là simulated trading, internal matching, hay real market execution?
- Hệ thống đang nhắm tới learning/demo platform, internal training tool, hay lõi fintech tiền production?
- `banknet` ở đây là provider cụ thể, switch ngân hàng, hay chỉ là payment rail tổng quát?
- Chính sách nào định nghĩa một `large transaction`?
- Dữ liệu KYC nào phải lưu nội bộ và dữ liệu nào chỉ giữ ở provider eKYC?
- Holdings được dựa trên custody record thật hay chỉ là bookkeeping nội bộ ở phase 1?
- User có được hủy lệnh không? Nếu có thì phải tương thích thế nào với nguyên tắc append-only?
- Fee là fee cố định, theo phần trăm, theo tier, hay phụ thuộc thị trường?
- Ngoài cổ phiếu phổ thông, còn loại market/instrument nào nằm trong scope?
- Complaint handling chỉ là ticket vận hành hay cần cả evidence package phục vụ pháp lý?
- Bộ tiêu chí nào quyết định một công ty đủ điều kiện để tạo `Stock` và đưa vào giao dịch?
- Phân loại cổ phiếu là taxonomy tĩnh do admin cấu hình, hay cần hỗ trợ nhiều chiều như ngành, chỉ số, thematic tag, mức vốn hóa?
- Portfolio chỉ là view tổng hợp cho investor hay còn cần rebalancing, performance attribution, benchmark comparison?
