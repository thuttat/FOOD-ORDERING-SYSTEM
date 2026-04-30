# ADR-001: Lựa chọn Mô hình Kiến trúc Client-Server kết hợp Layered Architecture

## Trạng thái:
Accepted

## 1. Ngữ cảnh: 
Hệ thống Food Ordering có 3 đối tượng người dùng chính (Khách hàng, Nhà hàng, Admin) thao tác trên các giao diện khác nhau nhưng cùng truy xuất và biến đổi một tập dữ liệu tập trung (thông tin đơn hàng, thực đơn, trạng thái thanh toán). Dự án có thời hạn triển khai trong 8 tuần. Chúng ta cần một kiến trúc hệ thống đảm bảo tính phân tách mối quan tâm, dễ dàng phân công công việc trong nhóm, và thuận tiện cho việc bảo trì, mở rộng sau này.

## 2. Quyết định:
Hệ thống sẽ được áp dụng hai phong cách kiến trúc (Architectural Styles) kết hợp:
- Mức hệ thống (System-level): Áp dụng kiến trúc Client-Server. Các ứng dụng người dùng (Web Admin, Web Khách hàng, Web Nhà hàng) đóng vai trò là Client độc lập, giao tiếp với một Server trung tâm thông qua RESTful APIs.

- Mức máy chủ (Server-level/Backend): Áp dụng kiến trúc Layered Architecture (Kiến trúc phân tầng). Mã nguồn backend sẽ được chia thành các tầng chuẩn mực:
+ Presentation/Controller Layer: Xử lý request/response HTTP, định tuyến API.
+ Business Logic/Service Layer: Chứa core logic nghiệp vụ (tính toán giá đơn hàng, kiểm tra tồn kho món ăn, xử lý nghiệp vụ thanh toán).
+ Data Access/Repository Layer: Giao tiếp với cơ sở dữ liệu.

## 3. Lý do:
- Phù hợp với lý thuyết và nền tảng công nghệ: Layered Architecture là pattern kinh điển giúp giảm thiểu rủi ro kỹ thuật. Việc sử dụng Spring Boot cho Backend hỗ trợ cấu trúc phân tầng này một cách tự nhiên thông qua các mô hình chuẩn mực (như @RestController, @Service, @Repository), giúp tổ chức mã nguồn rõ ràng, dễ bảo trì.

- Phân tách trách nhiệm hiệu quả: Mô hình Client-Server cho phép đội ngũ phân chia công việc độc lập. Việc xây dựng giao diện Frontend bằng React.js (cho Client Khách hàng, Nhà hàng và Admin) có thể được tiến hành song song với việc phát triển Backend bằng Spring Boot. Các thành viên sẽ không bị phụ thuộc hay dẫm chân lên nhau.

- Đánh đổi hợp lý so với Microservices: Dù kiến trúc Microservices mang lại khả năng mở rộng (Scalability) lớn, nhưng lại đi kèm với độ phức tạp khổng lồ về quản lý hạ tầng và phân tán dữ liệu (Distributed System Complexity). Với quy mô đồ án môn học và quỹ thời gian giới hạn, việc xây dựng một hệ thống Modular Monolith bằng Spring Boot là giải pháp mang lại hiệu quả cao nhất. Khối backend vẫn được chia module rõ ràng, tạo tiền đề để có thể dễ dàng tách thành các service độc lập trong tương lai nếu dự án mở rộng.

## 4. Hệ quả:
- Ưu điểm: 
1. Tách biệt rõ ràng: Dễ hiểu, dễ quản lý vì mỗi lớp có nhiệm vụ riêng. Giảm sự phụ thuộc.
2. Dễ bảo trì: Thay đổi ở một lớp(VD: Thay đổi UI) ít ảnh hưởng đến lớp Logic hay Database.
3. Dễ kiểm thử: Có thể viết Unit Test độc lập cho từng lớp bằng cách Mock các lớp phụ thuộc.
4. Tái sử dụng: Lớp Business Logic có thể được dùng chung cho cả Web UI và Mobile UI.
5. Dễ tiếp cận: Cấu trúc tự nhiên, phù hợp cho người mới và các dự án nhỏ.

- Nhược điểm:
1. Hiệu suất: Dữ liệu phải đi qua nhiều lớp trung gian gây tăng độ trễ(latency). Overhead do quá trình abstraction.
2. Architecture Sinkhole: Một anti-pattern phổ biến khi các request đơn giản( như lấy dữ liệu hiển thị) vẫn phải đi qua tất cả các lớp mà không có logic xử lý thêm, gây lãng phí code.
3. Độ phức tạp: Có thể là "Over-engineer" đối với các ứng dụng quá đơn giản.
4. Khó mở rộng: Khó scale theo chiều ngang. Thường phải scale toàn bộ ứng dụng nguyên khối(monolith).

## 5. Kế hoạch định hướng tương lai: Lộ trình chuyển đổi sang Microservices
Mặc dù kiến trúc Modular Monolith dựa trên Spring Boot là lựa chọn tối ưu cho giai đoạn hiện tại, hệ thống được thiết kế với tư duy mở (Future-proof) để sẵn sàng chuyển đổi sang Microservices khi ứng dụng đạt đến các giới hạn về quy mô.

1. Các Triggers quyết định việc chuyển đổi: 
Hệ thống sẽ bắt đầu lộ trình chia tách thành Microservices khi gặp một trong các điều kiện sau:

- Khi một tính năng cụ thể chịu tải vượt trội so với phần còn lại. (Ví dụ: Hệ thống tìm kiếm món ăn/nhà hàng hoặc dịch vụ Thông báo (Notification) nhận lượng request khổng lồ vào giờ cao điểm (giờ ăn trưa), đòi hỏi phải scale riêng biệt (scale out) mà không cần nhân bản toàn bộ khối backend.)

- Khi đội ngũ phát triển mở rộng từ một nhóm nhỏ (3-5 người) thành nhiều team chéo. Lúc này, việc chia nhỏ codebase để mỗi team làm chủ một dịch vụ (ví dụ: Team Payment, Team Order, Team Restaurant) sẽ giảm thiểu xung đột khi merge code và tăng tốc độ triển khai (CI/CD).

- Khi một nghiệp vụ mới phát sinh đòi hỏi công nghệ đặc thù. (Ví dụ: Module gợi ý món ăn (Recommendation) cần xử lý dữ liệu lớn nên ưu tiên dùng Python, trong khi Core logic vẫn giữ ở Java/Spring Boot.)

2. Lộ trình và Chiến lược chuyển đổi:
- Giai đoạn 1 - Tách các dịch vụ rìa (Edge Services): Tách các module ít phụ thuộc vào dữ liệu cốt lõi nhất.Ví dụ: Rút module Notification (gửi email, push notification cho nhà hàng) hoặc Search (tìm kiếm món ăn) ra thành một Spring Boot service độc lập.

- Giai đoạn 2 - Chia tách cơ sở dữ liệu (Database Decomposition): Phá vỡ ranh giới Database Monolith. Chuyển từ việc các module JOIN trực tiếp các bảng của nhau sang việc mỗi module sở hữu một Database riêng biệt và giao tiếp qua API hoặc Event/Message Queue.

- Giai đoạn 3 - Tách các dịch vụ cốt lõi (Core Domain Services): Tách tách biệt hoàn toàn Order Service, Payment Service, và User Service. Sử dụng hệ sinh thái Spring Cloud (như API Gateway, Eureka/Consul cho Service Discovery) để điều phối các microservices này.

## Ngày quyết định
2026-03-23