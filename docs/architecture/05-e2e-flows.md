# Các Luồng End-to-End Chính Của FinTradeCore

## Flow 1: Đăng ký và kích hoạt investor

1. User đăng ký tài khoản.
2. Hệ thống tạo hồ sơ ở trạng thái pending.
3. User gửi thông tin KYC.
4. Hệ thống gửi yêu cầu xác minh sang eKYC provider.
5. Hệ thống ghi immutable KYC status events.
6. Khi xác minh thành công, tài khoản chuyển sang active để có thể nạp tiền và giao dịch.

## Flow 2: Nạp tiền

1. User tạo deposit intent.
2. Hệ thống tạo payment transaction ở trạng thái pending.
3. Payment rail ngoài hệ thống trả về kết quả success hoặc failure.
4. Nếu thành công, hệ thống append một cash ledger credit entry.
5. Read model cập nhật wallet balance.
6. Audit log ghi lại request, callback, và kết quả.
7. Reconciliation ở giai đoạn sau sẽ đối chiếu payment rail với internal ledger.

## Flow 3: Rút tiền

1. User gửi yêu cầu rút tiền.
2. Hệ thống kiểm tra KYC, account status, và available cash.
3. Risk rules đánh giá amount, frequency, và suspicious signals.
4. Nếu vượt ngưỡng, hệ thống tạo approval case trước khi payout.
5. Khi được duyệt và payment thành công, hệ thống append cash ledger debit entry.
6. Các failure sẽ đi vào exception handling và có thể retry an toàn.

## Flow 4: Đặt buy order

1. User gửi buy order.
2. Hệ thống kiểm tra user status, instrument status, price guardrails, và available funds.
3. Hệ thống reserve hoặc earmark cash theo đúng thiết kế được chọn.
4. Order vào trạng thái accepted.
5. Execution được xử lý theo mô hình simulated/internal cho đến khi có quyết định rõ về kết nối thị trường thật.
6. Khi order được fill, hệ thống append:
   - cash debit
   - securities credit
   - fee entry
   - audit record
7. Holdings và P&L read models được refresh.

## Flow 4A: Duyệt công ty và tạo cổ phiếu

1. Admin/Ops tạo hoặc tiếp nhận hồ sơ `IssuerCompany`.
2. Hệ thống tạo `ListingApplication` cho công ty.
3. Bộ tiêu chí `ListingCriteria` được áp dụng để kiểm tra điều kiện niêm yết nội bộ.
4. Người duyệt xem hồ sơ pháp lý, dữ liệu tài chính, và các điều kiện domain bắt buộc.
5. Nếu đạt yêu cầu, hồ sơ chuyển sang `approved`.
6. Hệ thống tạo entity `Stock`, gắn với công ty phát hành.
7. Admin gán classification cho `Stock`, ví dụ ngành công nghệ, VN30, blue-chip.
8. Khi `Stock` được chuyển sang `active`, user mới có thể xem và đặt lệnh giao dịch.

## Flow 5: Đặt sell order

1. User gửi sell order.
2. Hệ thống kiểm tra available holdings và các restrictions.
3. Order được accepted và chuyển sang execution model.
4. Khi order được fill, hệ thống append:
   - securities debit
   - cash credit
   - fee entry
   - audit record
5. Statements, history, và P&L có thể được query từ các derived views.

## Flow 6: Cảnh báo rủi ro và khóa giao dịch

1. Một transaction hoặc order event được phát ra.
2. Risk engine đánh giá theo rule set đang active.
3. Nếu nghi ngờ, hệ thống tạo risk alert.
4. Nếu mức độ nghiêm trọng đủ cao hoặc rule cho phép auto-enforce, account trading status chuyển sang restricted/frozen qua một governed state change.
5. Compliance officer review evidence rồi quyết định release hoặc escalate case.

## Flow 7: Retry failed event

1. Một async consumer xử lý event thất bại.
2. Hệ thống lưu failure metadata và điều kiện retry.
3. Ops review failed event queue.
4. Ops kích hoạt retry.
5. Consumer xử lý lại theo cơ chế idempotent.
6. Hệ thống ghi kết quả retry vào audit log.

## Flow 8: Đóng sổ cuối ngày

1. Việc cập nhật market prices được chốt cho business day hiện tại.
2. Hệ thống hoàn tất valuation và P&L calculation còn pending.
3. Reconciliation đối chiếu internal ledger với payment/execution sources.
4. Các exception được ghi nhận để review.
5. Báo cáo cuối ngày và daily statements được tạo ra.
6. Batch đóng sổ được đánh dấu hoàn tất cùng immutable batch evidence.

## Flow 9: Xem portfolio đầu tư

1. User mở màn hình portfolio.
2. Hệ thống tổng hợp cash balance, positions, market prices, và classification của từng `Stock`.
3. Hệ thống tính tổng tài sản, allocation theo mã, allocation theo ngành, allocation theo rổ chỉ số, và P&L tổng.
4. User xem được bức tranh đầu tư gần với trải nghiệm của sàn chứng khoán thật.
