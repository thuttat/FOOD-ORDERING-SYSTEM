# Báo cáo Tuần 7

**Tuần:** 7 (13/04/2026 - 19/04/2026)  
**Nhóm:** 9  
**Đề tài:** A1 - Hệ thống đặt đồ ăn trực tuyến (Food Ordering)  
**Nhóm trưởng:** Trịnh Thị Anh Thư - 2351010205  

---

## 1. Công việc đã hoàn thành

Tuần này nhóm tập trung vào việc giải quyết xung đột mã nguồn sau khi merge các phân hệ và hoàn thiện luồng nghiệp vụ cốt lõi: **Chọn món -> Giỏ hàng -> Thanh toán (VNPay/MoMo).**

| Thành viên | MSSV | Công việc | Trạng thái |
|------------|------|-----------|------------|
| **Trịnh Thị Anh Thư** | 2351010205 | Hoàn thiện UI/Logic trang RestaurantDetail và Cart . Xử lý triệt để lỗi Infinite Redirect Loop trong `AxiosClient` và `PrivateRoute`. Hoàn thiện thanh toán. | [e](https://github.com/thuttat/FOOD-ORDERING-SYSTEM/commit/57f8d95ee31b380c0e3e6745bb6da4114eabb7a2) |
| **Lê Hoàng Bảo Trân** | 2351010214 | Hoàn thiện UI Dashboard Admin (biểu đồ doanh thu). Ghép nối API duyệt nhà hàng. | [e](https://github.com/thuttat/FOOD-ORDERING-SYSTEM/commit/c2573029278e06512f2d357871476c70a0030574) |
| **Nguyễn Triệu Duy** | 2351010036 | Hoàn thiện UI Quản lý Menu. Xử lý hiển thị thông báo Real-time cho Nhà hàng khi có đơn mới. | [e] | 

---

## 2. Tiến độ tổng thể

| Hạng mục | Trạng thái | % |
|----------|------------|---|
| Phân tích yêu cầu & ERD | Đã hoàn thành (Cập nhật bản cuối) | 100% |
| Backend API | Đã hoàn thành | 100% |
| Frontend UI (Khách hàng) | Hoàn thiện luồng đặt hàng & thanh toán | 90% |
| Frontend UI (Admin & Nhà hàng) | Đã tích hợp và chuẩn hóa UI | 80% |
| Tích hợp Hệ thống (Docker/RabbitMQ) | Đang thực hiện | 50% |

**Tổng tiến độ: ~80%**

---

## 3. Kế hoạch tuần tới (Tuần 8 - Giai đoạn hoàn thiện)

| Thành viên | Công việc dự kiến |
|------------|-------------------|
| **Trịnh Thị Anh Thư** | Phát triển trang Lịch sử đơn hàng (Order History) và trang Cá nhân (Profile). Bắt đầu viết Dockerfile cho Frontend và Backend. |
| **Lê Hoàng Bảo Trân** | Tiến hành kiểm thử toàn diện (End-to-End Testing) từ lúc đăng ký đến lúc thanh toán. Viết tài liệu hướng dẫn sử dụng hệ thống. |
| **Nguyễn Triệu Duy** | Tối ưu hóa hiệu suất thông báo Real-time qua WebSocket. Cấu hình Docker Compose để triển khai hệ thống toàn cục. |

---

## 4. Khó khăn / Ghi chú

- **Đã giải quyết:** Xử lý thành công lỗi xung đột thư viện Vite và lỗi 401 Unauthorized gây reload trang liên tục. Thống nhất sử dụng duy nhất key `accessToken` để quản lý phiên đăng nhập.
- **Ghi chú:** Cấu trúc thư mục hiện tại đã được tổ chức lại theo dạng đóng gói (Encapsulation) để phục vụ cho việc bảo trì và mở rộng sau này.
- **Kỹ thuật:** Đã tích hợp thành công Redirect URL từ Backend cho các cổng thanh toán điện tử.

---
*Ngày nộp: 19/04/2026* **Xác nhận của Nhóm trưởng:** Trịnh Thị Anh Thư
