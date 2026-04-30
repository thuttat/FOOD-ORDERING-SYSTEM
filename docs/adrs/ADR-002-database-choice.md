# ADR-002: Lựa chọn MySQL làm cơ sở dữ liệu chính
## Trạng thái: 
Accepted

## 1. Bối cảnh
Chúng tôi cần chọn một hệ quản trị cơ sở dữ liệu phù hợp cho hệ thống đặt đồ ăn trực tuyến HappyFood.
Dữ liệu của hệ thống có tính liên kết quan hệ rất chặt chẽ (Người dùng - Đơn hàng - Nhà hàng - Món ăn). Nhóm gồm 3 thành viên, cần một giải pháp lưu trữ quen thuộc, ổn định và đảm bảo an toàn tuyệt đối cho các giao dịch thanh toán.

## 2. Quyết định
Sử dụng MySQL làm cơ sở dữ liệu chính. Phía Backend (Spring Boot) sẽ giao tiếp với cơ sở dữ liệu thông qua Spring Data JPA (Hibernate ORM).

## 3. Lý do
1. Đảm bảo thuộc tính ACID, an toàn cho các nghiệp vụ chốt đơn và thanh toán.
2. Hỗ trợ tốt các ràng buộc khóa ngoại (Foreign Keys) để bảo vệ tính toàn vẹn dữ liệu.
3. Dễ dàng thực hiện các câu truy vấn phức tạp để thống kê doanh thu cho Dashboard.
4. Tích hợp trơn tru với Spring Boot và dễ dàng đóng gói bằng Docker.

## 4. Hệ quả
- Tích cực: Dữ liệu luôn nhất quán, dễ dàng thiết kế cấu trúc và truy vấn dữ liệu quan hệ.
- Tiêu cực: Yêu cầu thiết kế lược đồ (Schema) thật kỹ từ đầu, khó thay đổi cấu trúc bảng sau này.

## Ngày quyết định
2026-03-23