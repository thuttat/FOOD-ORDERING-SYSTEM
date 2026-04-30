# ADR-003: Lựa chọn RabbitMQ cho xử lý bất đồng bộ

## Trạng thái 
Accepted 

## 1. Bối cảnh 
Hệ thống HappyFood cần thực hiện các tác vụ tốn thời gian như gửi email/thông báo cho Khách hàng và Chủ nhà hàng khi có đơn hàng mới hoặc trạng thái đơn thay đổi. 
Hệ thống được phát triển bởi team 3 người, cần tối ưu thời gian phản hồi (response time) của các API.

## 2. Quyết định 
Sử dụng RabbitMQ làm Message Broker để tách các tác vụ gửi thông báo ra xử lý bất đồng bộ (Asynchronous processing) thay vì xử lý đồng bộ trực tiếp trên luồng API.

## 3. Lý do
1. Tối ưu hóa tốc độ phản hồi API cho Frontend (không bắt người dùng chờ gửi email xong mới báo đặt hàng thành công).
2. Tách biệt rõ ràng luồng nghiệp vụ chính (đặt hàng, thanh toán) và luồng phụ (thông báo).
3. Đảm bảo không bị mất thông báo nhờ cơ chế lưu trữ hàng đợi của RabbitMQ nếu dịch vụ gửi mail bị lỗi tạm thời.
4. Có sẵn Docker Image, dễ dàng tích hợp vào hệ thống hiện tại.

## 4. Hệ quả
- Tích cực: Trải nghiệm người dùng mượt mà, hệ thống chịu tải tốt hơn khi có nhiều đơn hàng cùng lúc. 
- Tiêu cực: Tăng độ phức tạp khi vận hành (cần quản lý thêm container RabbitMQ) và khó trace bug (theo dõi lỗi) hơn luồng đồng bộ bình thường.

## Ngày quyết định 
2026-03-26