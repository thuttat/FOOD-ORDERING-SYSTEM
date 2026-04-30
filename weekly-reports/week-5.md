# Báo cáo Tuần 5

**Tuần:** 5 (30/03/2026 - 05/04/2026)  
**Nhóm:** 9  
**Đề tài:** A1 - Hệ thống đặt đồ ăn trực tuyến (Food Ordering)  
**Nhóm trưởng:** Trịnh Thị Anh Thư - 2351010205  

---

## 1. Công việc đã hoàn thành

| Thành viên | MSSV | Công việc | Link Commit/PR |
|------------|------|-----------|----------------|
| **Trịnh Thị Anh Thư** | 2351010205 | Code hoàn thiện các API Backend (Spring Boot) cho luồng Khách hàng: API lấy danh sách nhà hàng/thực đơn, API Quản lý Giỏ hàng (UC-1) và API Đặt hàng | [e](https://github.com/thuttat/FOOD-ORDERING-SYSTEM/commit/cf9cb66a6aa13260c6f86faf63d87c691e70c462) |
| **Lê Hoàng Bảo Trân** | 2351010214 | Code API Backend (Spring Boot) cho luồng Admin: API lấy danh sách nhà hàng chờ duyệt, API duyệt/khóa tài khoản, API thống kê hệ thống. | [e](https://github.com/thuttat/FOOD-ORDERING-SYSTEM/commit/59f002f71f373048f3bcd3b5d710e4ef584c444a)|
| **Nguyễn Triệu Duy** | 2351010036 | CCode API Backend cho luồng Nhà hàng: Quản lý Menu (thêm/sửa/xóa món), Cập nhật trạng thái đơn hàng. Tích hợp thử RabbitMQ cho chức năng thông báo đơn hàng mới | [e](https://github.com/thuttat/FOOD-ORDERING-SYSTEM/commit/231e81045243e40e418484135023c273e49e503c) | 

---

## 2. Tiến độ tổng thể

| Hạng mục | Trạng thái | % |
|----------|------------|---|
| Phân tích yêu cầu | Đã hoàn thành | 100% |
| Thiết kế kiến trúc | Đã hoàn thành | 100% |
| Backend API | Cơ bản hoàn thành | 80% |
| Frontend UI | Tạm hoãn sang Tuần 6 | 0% |
| Docker & Tích hợp | Bắt đầu (RabbitMQ) | 10% |
| Testing | Chưa bắt đầu | 0% |

**Tổng tiến độ: ~50%**

---

## 3. Kế hoạch tuần tới (Tuần 6)

| Thành viên | Công việc dự kiến |
|------------|-------------------|
| **Trịnh Thị Anh Thư** | Khởi tạo project ReactJS. Dựng giao diện UI luồng Khách hàng: Trang chủ, Trang chi tiết quán ăn, Giỏ hàng và Checkout. Ghép nối API và xử lý lỗi CORS. |
| **Lê Hoàng Bảo Trân** | Dựng giao diện UI luồng Admin: Dashboard thống kê, Bảng duyệt nhà hàng và quản lý đánh giá vi phạm. |
| **Nguyễn Triệu Duy** | Dựng giao diện UI luồng Nhà hàng: Màn hình quản lý thực đơn, Màn hình Receive real-time orders (lắng nghe từ RabbitMQ). |

---

## 4. Khó khăn / Cần hỗ trợ

- **Khó khăn tuần này:** Các luồng logic Backend khá phức tạp, đặc biệt là việc đảm bảo tính nhất quán (ACID) khi xử lý API Đặt hàng (vừa tạo đơn, vừa tính tiền, vừa gửi thông báo qua RabbitMQ). Do đó, nhóm quyết định dồn toàn bộ thời gian tuần này để code và test kỹ Backend, **tạm hoãn việc dựng UI Frontend sang Tuần 6**.
- **Khó khăn tuần tới:** Bắt đầu dựng UI ReactJS và gọi API từ Spring Boot sang. Dự kiến sẽ gặp khó khăn với lỗi bảo mật CORS (Cross-Origin Resource Sharing) và quản lý State giỏ hàng trên ứng dụng.

---
*Ngày nộp: 05/04/2026*  
*Xác nhận của Nhóm trưởng: Trịnh Thị Anh Thư*  
