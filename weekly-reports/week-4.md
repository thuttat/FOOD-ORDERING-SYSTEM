# Báo cáo Tuần 4

**Tuần:** 4 (23/03/2026 - 29/03/2026)  
**Nhóm:** 9  
**Đề tài:** A1 - Hệ thống đặt đồ ăn trực tuyến (Food Ordering)  
**Nhóm trưởng:** Trịnh Thị Anh Thư - 2351010205  

---

## 1. Công việc đã hoàn thành

| Thành viên | MSSV | Công việc | Link Commit/PR |
|------------|------|-----------|----------------|
| **Trịnh Thị Anh Thư** | 2351010205 | Vẽ sơ đồ Database Schema (ERD) cho toàn bộ hệ thống. Tạo thư mục `db/` và viết file script `init.sql` để khởi tạo các bảng (Users, Restaurants, Categories, Menu, Orders) và insert dữ liệu mẫu, hoàn thiện entity| [4ab5d28](https://github.com/thuttat/FOOD-ORDERING-SYSTEM/commit/4ab5d289b28c0c1fe5fa86dcadf4535efeb0584a) |
| **Lê Hoàng Bảo Trân** | 2351010214 | Thiết kế bộ tài liệu RESTful API (API Design). Định nghĩa rõ HTTP Method, Endpoints và cấu trúc JSON Response cho các chức năng. Thiết lập quy chuẩn Error Handling (các mã lỗi 400, 401, 403, 404, 500),tạo môi trường frontend và UI. | [f4df051](https://github.com/thuttat/FOOD-ORDERING-SYSTEM/commit/f4df05196850c4f76d414d28d7a333fd680bf681) |
| **Nguyễn Triệu Duy** | 2351010036 | Khởi tạo thành công project Backend bằng Spring Boot, cấu hình kết nối với MySQL. Code class Entity User ánh xạ với Database và hoàn thành luồng logic cơ bản (Controller, Service, Repository) cho phần Đăng nhập/Đăng ký. | [c48ddcd](https://github.com/thuttat/FOOD-ORDERING-SYSTEM/commit/c48ddcd47b43d7cd82641f87dc3e27c4c4ab0c08) | 

---

## 2. Tiến độ tổng thể

| Hạng mục | Trạng thái | % |
|----------|------------|---|
| Phân tích yêu cầu | Đã hoàn thành | 100% |
| Thiết kế kiến trúc | Đã hoàn thành | 100% |
| Backend API | Đang thực hiện | 30% |
| Frontend UI | Chưa bắt đầu | 10% |
| Docker | Chưa bắt đầu | 0% |
| Testing | Chưa bắt đầu | 0% |

**Tổng tiến độ: ~35%** 

---

## 3. Kế hoạch tuần tới (Tuần 5)

*Giai đoạn này nhóm sẽ đẩy mạnh hoàn thiện API Backend và bắt đầu khởi tạo Frontend (ReactJS).*

| Thành viên | Công việc dự kiến |
|------------|-------------------|
| **Trịnh Thị Anh Thư** | Code API Backend (Spring Boot) cho luồng Khách hàng: Lấy danh sách nhà hàng/món ăn, Quản lý Giỏ hàng, Tạo đơn đặt hàng (Checkout). Bắt đầu dựng giao diện UI (ReactJS) cho Trang chủ. |
| **Lê Hoàng Bảo Trân** | Code API Backend (Spring Boot) cho luồng Admin: API lấy danh sách nhà hàng chờ duyệt, API duyệt/khóa tài khoản, API thống kê hệ thống. Bắt đầu dựng giao diện UI Dashboard cho Admin. |
| **Nguyễn Triệu Duy** | Code API Backend cho luồng Nhà hàng: Quản lý Menu (thêm/sửa/xóa món), Cập nhật trạng thái đơn hàng. Tích hợp thử RabbitMQ cho chức năng thông báo đơn hàng mới. Bắt đầu dựng UI trang Quản lý thực đơn. |

---

## 4. Khó khăn / Cần hỗ trợ

- **Đã giải quyết:** Nhóm đã giải quyết xong việc setup môi trường Backend (Java, Spring Boot, MySQL) và thống nhất được chuẩn thiết kế API chung cũng như cấu trúc thư mục dự án.
- **Khó khăn tuần tới:** Tuần sau nhóm sẽ bắt đầu code giao diện Frontend bằng ReactJS và tiến hành gọi API từ Backend. Nhóm dự kiến sẽ gặp khó khăn trong việc xử lý lỗi CORS (Cross-Origin Resource Sharing) giữa 2 cổng mạng khác nhau (Cổng 3000 của React và Cổng 8080 của Spring Boot) và việc quản lý State trên React. Ngoài ra, việc cấu hình RabbitMQ cũng là một kiến thức mới cần thời gian nghiên cứu.

---
*Ngày nộp: 29/03/2026*  
*Xác nhận của Nhóm trưởng: Trịnh Thị Anh Thư*  
