ALTER TABLE users AUTO_INCREMENT = 50;
ALTER TABLE restaurants AUTO_INCREMENT = 20;
ALTER TABLE categories AUTO_INCREMENT = 20;
ALTER TABLE menu_items AUTO_INCREMENT = 50;
ALTER TABLE carts AUTO_INCREMENT = 20;
ALTER TABLE cart_items AUTO_INCREMENT = 20;
ALTER TABLE notifications AUTO_INCREMENT = 20;

ALTER TABLE orders AUTO_INCREMENT = 10000;
ALTER TABLE order_items AUTO_INCREMENT = 20000;

INSERT IGNORE INTO users (id, fullname, username, email, password, role, status, phone, created_at, updated_at) VALUES
(1, 'Quản Trị Viên', 'admin', 'admin@food.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'ADMIN', 'ACTIVE', '0901234567', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Nguyễn Văn Khách', 'khachhang1', 'khach1@gmail.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'USER', 'ACTIVE', '0912345678', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Chủ Quán Burger', 'nhahang1', 'contact@burgerking.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'RESTAURANT', 'ACTIVE', '0923456789', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Chủ Quán Pizza', 'nhahang2', 'contact@pizza4ps.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'RESTAURANT', 'ACTIVE', '0934567890', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Trần Văn C', 'khachhang3', 'khach3@gmail.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'USER', 'ACTIVE', '0999999999', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Phạm Thị D', 'khachhang4', 'khach4@gmail.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'USER', 'ACTIVE', '0977777777', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT IGNORE INTO restaurants (id, owner_id, name, address, phone, is_open, status, description, image_url, created_at, updated_at) VALUES
(1, 3, 'Burger King Quận 1', '123 Nguyễn Huệ, Quận 1', '0923456789', true, 'ACTIVE', 'Hương vị Burger nướng lửa truyền thống từ Mỹ', 'https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 4, 'Pizza 4P''s Bến Thành', '8 Thủ Khoa Huân, Quận 1', '0934567890', true, 'ACTIVE', 'Pizza thủ công nướng củi phong cách Nhật - Ý', 'https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT IGNORE INTO categories (id, restaurant_id, name, created_at, updated_at) VALUES
(1, 1, 'Burger Best Seller', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 'Đồ uống & Tráng miệng', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 2, 'Pizza Signature', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 2, 'Mì Ý & Salad', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT IGNORE INTO menu_items (id, category_id, name, description, price, is_available, image_url, created_at, updated_at) VALUES
(1, 1, 'Whopper Burger', 'Burger bò nướng lửa kèm rau tươi và sốt đặc trưng', 89000.00, true, 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 'Cheese Burger', 'Burger phô mai bò nướng lửa', 55000.00, true, 'https://images.unsplash.com/photo-1550547660-d9450f859349?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 2, 'Coca Cola', 'Lon 330ml', 20000.00, true, 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 2, 'Khoai tây chiên', 'Size L, giòn rụm', 35000.00, true, 'https://images.unsplash.com/photo-1576107232684-1279f390859f?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 3, 'Burrata Pizza', 'Pizza kèm phô mai Burrata tươi và thịt nguội Ý', 290000.00, true, 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 3, 'Margherita Pizza', 'Pizza truyền thống phong cách Ý', 150000.00, true, 'https://images.unsplash.com/photo-1604068549290-dea0e4a305ca?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT IGNORE INTO carts (id, customer_id, created_at, updated_at) VALUES (1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT IGNORE INTO cart_items (id, cart_id, menu_item_id, quantity, created_at, updated_at) VALUES (1, 1, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT IGNORE INTO notifications (id, user_id, message, is_read, type, created_at, updated_at) VALUES (1, 1, 'Hệ thống đã sẵn sàng vận hành.', false, 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT IGNORE INTO orders (id, customer_id, restaurant_id, status, total_amount, delivery_fee, delivery_address, created_at) VALUES
(10, 2, 1, 'DELIVERED', 450000, 15000, '123 Quận 1', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY)),
(11, 5, 1, 'DELIVERED', 120000, 15000, '456 Quận 3', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY)),
(12, 6, 1, 'DELIVERED', 890000, 15000, '789 Bình Thạnh', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY)),
(14, 2, 1, 'DELIVERED', 1250000, 15000, '123 Quận 1', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY)),
(15, 5, 1, 'DELIVERED', 980000, 15000, '456 Quận 3', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY)),
(16, 6, 1, 'DELIVERED', 2100000, 15000, '789 Bình Thạnh', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY)),
(24, 5, 1, 'PREPARING', 240000, 15000, '789 Cách Mạng Tháng 8', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 MINUTE)),
(25, 6, 1, 'PENDING', 360000, 15000, '321 Nam Kỳ Khởi Nghĩa', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MINUTE)),
(32, 5, 1, 'CANCELLED', 45000, 15000, 'Hẻm 123 Võ Văn Tần, Quận 3', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 MINUTE));

INSERT IGNORE INTO order_items (order_id, menu_item_id, quantity, unit_price) VALUES
(10, 1, 3, 89000), (10, 3, 5, 20000),
(14, 1, 5, 89000), (15, 3, 8, 20000),
(24, 1, 2, 89000), (25, 6, 2, 150000);