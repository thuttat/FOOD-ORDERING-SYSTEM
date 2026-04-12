# Báo cáo Tuần 6

**Tuần:** 6 (06/04/2026 - 12/04/2026)  
**Nhóm:** 9  
**Đề tài:** A1 - Hệ thống đặt đồ ăn trực tuyến (Food Ordering)  
**Nhóm trưởng:** Trịnh Thị Anh Thư - 2351010205  

---

## 1. Công việc đã hoàn thành

Tuần này, nhóm đã hoàn thành việc đồng bộ hóa và đóng gói toàn bộ mã nguồn Backend, giải quyết triệt để các xung đột (conflict) giữa các luồng chức năng.

| Thành viên | MSSV | Công việc | Trạng thái |
|------------|------|-----------|------------|
| **Trịnh Thị Anh Thư** | 2351010205 | Hoàn thiện luồng logic Giỏ hàng (Cart) và Đặt hàng (Order). Tái cấu trúc Mapper/DTO. Bắt đầu dựng UI ReactJS (Trang chủ, Detail quán). |[e](https://github.com/thuttat/FOOD-ORDERING-SYSTEM/commit/47e8dead7c882fd7d047cbbdb9d59aab7bd949df) |
| **Lê Hoàng Bảo Trân** | 2351010214 | Hoàn thiện API Thống kê Dashboard. Cấu trúc lại Repository cho Admin. Dựng UI Frontend luồng Admin. | [e](https://github.com/thuttat/FOOD-ORDERING-SYSTEM/commit/c1888bdd216f416602cacf7a69af012715aaa2bb) |
| **Nguyễn Triệu Duy** | 2351010036 | Hoàn thiện logic thông báo (Notification) qua RabbitMQ. Đồng bộ trạng thái MenuItem. Dựng UI Frontend luồng Nhà hàng. | [e](https://github.com/thuttat/FOOD-ORDERING-SYSTEM/commit/5bc59876dcde2aa4a721776ac386121eabfa1e41) |

---

## 2. Tiến độ tổng thể

| Hạng mục | Trạng thái | % |
|----------|------------|---|
| Phân tích yêu cầu & ERD | Đã hoàn thành (Cập nhật bản cuối) | 100% |
| Backend API | **Đã hoàn thành 100%** | 100% |
| Frontend UI (Khách hàng) | Đang thực hiện  | 40% |
| Frontend UI (Admin & Nhà hàng) | Đang thực hiện | 50% |
| Tích hợp hệ thống & Docker | Đang thực hiện | 30% |

**Tổng tiến độ: ~65%**

---

## 3. Kế hoạch tuần tới (Tuần 7)


| Thành viên | Công việc dự kiến |
|------------|-------------------|
| **Trịnh Thị Anh Thư** | Hoàn thiện UI Giỏ hàng và Checkout. Xử lý logic State Management cho giỏ hàng. Ghép nối API Thanh toán (Mock Payment). |
| **Lê Hoàng Bảo Trân** | Hoàn thiện UI Dashboard Admin (biểu đồ doanh thu). Ghép nối API duyệt nhà hàng. |
| **Nguyễn Triệu Duy** | Hoàn thiện UI Quản lý Menu. Xử lý hiển thị thông báo Real-time cho Nhà hàng khi có đơn mới. |

---

## 4. Khó khăn / Ghi chú

- **Tình trạng:** Nhóm đã hoàn thành xuất sắc việc "chốt" cấu trúc Database và logic Backend (Service/Repository/Mapper). Tuy nhiên, do tốn nhiều thời gian vào việc refactor code Backend để đảm bảo tính nhất quán (ACID) cho luồng Giỏ hàng -> Đặt món, nên phần **UI cho Giỏ hàng và Đặt món vẫn chưa thực hiện xong** trong tuần này.
- **Giải pháp:** Trong tuần 7, Nhóm sẽ tập trung toàn lực vào việc xử lý UI luồng Checkout và quản lý trạng thái (State) của giỏ hàng trên ReactJS để kịp tiến độ.

---
*Ngày nộp: 12/04/2026* *Xác nhận của Nhóm trưởng: Trịnh Thị Anh Thư*
