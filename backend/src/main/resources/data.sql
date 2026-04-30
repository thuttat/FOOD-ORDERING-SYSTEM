


-- 2. DỮ LIỆU NGƯỜI DÙNG (Users)
INSERT INTO users (id, fullname, username, email, password, role, status, phone) VALUES 
(1, 'Quản Trị Viên', 'admin', 'admin@food.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'ADMIN', 'ACTIVE', '0901234567'), 
(2, 'Nguyễn Văn A', 'khachhang1', 'khach1@gmail.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'USER', 'ACTIVE', '0912345678'), 
(3, 'Chủ Quán Burger', 'nhahang1', 'contact@burgerking.com', '$2a$12$fym4WmGb1TOb/uK6.H59aOBn.efDKvzadUjh86Y36qNeE2oH3I7Qq', 'RESTAURANT', 'ACTIVE', '0923456789'),
(4, 'Trần Thị B', 'khachhang2', 'khach2@gmail.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'USER', 'ACTIVE', '0988888888'),
(5, 'Trần Văn C', 'khachhang3', 'khach3@gmail.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'USER', 'ACTIVE', '0999999999'),
(6, 'Phạm Thị D', 'khachhang4', 'khach4@gmail.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'USER', 'ACTIVE', '0977777777');

-- 3. DỮ LIỆU NHÀ HÀNG (Restaurants)
INSERT INTO restaurants (id, user_id, name, address, phone_number, is_open, status, description) VALUES
(1, 3, 'Burger King Quận 1', '123 Nguyễn Huệ, Quận 1', '0923456789', true, 'ACTIVE', 'Hương vị Burger nướng lửa truyền thống từ Mỹ');

-- 4. DỮ LIỆU DANH MỤC & MÓN ĂN (Categories & Menu Items)
INSERT INTO categories (id, restaurant_id, name) VALUES
(1, 1, 'Best Seller Burgers'),
(2, 1, 'Beverages & Desserts');

INSERT INTO menu_items (id, category_id, name, description, price, is_available, image_url) VALUES
(1, 1, 'Whopper Burger', 'Flame-grilled beef patty with fresh vegetables and signature sauce', 89000, true, 'https://example.com/whopper.jpg'),
(2, 1, 'Cheese Burger', 'Classic burger with melted cheddar cheese and pickles', 45000, false, 'https://example.com/cheese.jpg'),
(3, 2, 'Coca Cola', 'Chilled 330ml can', 20000, true, 'https://example.com/coca.jpg'),
(4, 2, 'French Fries', 'Crispy golden fries, Size L', 35000, true, 'https://example.com/fries.jpg');

-- 5. DỮ LIỆU GIỎ HÀNG & THÔNG BÁO (Carts & Notifications)
INSERT INTO carts (id, customer_id) VALUES (1, 2);
INSERT INTO notifications (id, user_id, message, is_read, type, created_at) VALUES
(1, 1, 'The system is ready for operation.', false, 'SYSTEM', CURRENT_TIMESTAMP);

-- 6. DỮ LIỆU ĐƠN HÀNG (Orders) 
-- Đơn hàng tuần trước (DELIVERED)
INSERT INTO orders (id, customer_id, restaurant_id, status, total_amount, delivery_address, created_at) VALUES
(10, 2, 1, 'DELIVERED', 450000, '123 Quận 1', CURRENT_TIMESTAMP - INTERVAL '7' DAY),
(11, 4, 1, 'DELIVERED', 120000, '456 Quận 3', CURRENT_TIMESTAMP - INTERVAL '6' DAY),
(12, 5, 1, 'DELIVERED', 890000, '789 Bình Thạnh', CURRENT_TIMESTAMP - INTERVAL '5' DAY),
(13, 6, 1, 'DELIVERED', 350000, '321 Quận 5', CURRENT_TIMESTAMP - INTERVAL '4' DAY);

-- Đơn hàng tuần này (DELIVERED)
INSERT INTO orders (id, customer_id, restaurant_id, status, total_amount, delivery_address, created_at) VALUES
(14, 2, 1, 'DELIVERED', 1250000, '123 Quận 1', CURRENT_TIMESTAMP - INTERVAL '3' DAY),
(15, 4, 1, 'DELIVERED', 980000, '456 Quận 3', CURRENT_TIMESTAMP - INTERVAL '2' DAY),
(16, 5, 1, 'DELIVERED', 2100000, '789 Bình Thạnh', CURRENT_TIMESTAMP - INTERVAL '1' DAY),
(17, 6, 1, 'DELIVERED', 550000, '321 Quận 5', CURRENT_TIMESTAMP);

-- Đơn hàng giờ cao điểm hôm nay (DELIVERED)
INSERT INTO orders (id, customer_id, restaurant_id, status, total_amount, delivery_address, created_at) VALUES
(18, 2, 1, 'DELIVERED', 150000, 'Q1', TIMESTAMP '2026-04-29 11:30:00'),
(19, 4, 1, 'DELIVERED', 250000, 'Q3', TIMESTAMP '2026-04-29 12:00:00'),
(20, 5, 1, 'DELIVERED', 180000, 'Q1', TIMESTAMP '2026-04-29 12:45:00'),
(21, 6, 1, 'DELIVERED', 300000, 'Q5', TIMESTAMP '2026-04-29 18:30:00'),
(22, 2, 1, 'DELIVERED', 450000, 'Q1', TIMESTAMP '2026-04-29 19:15:00'),
(23, 4, 1, 'DELIVERED', 520000, 'Q3', TIMESTAMP '2026-04-29 20:00:00');

-- Đơn hàng đang xử lý (Real-time)
INSERT INTO orders (id, customer_id, restaurant_id, status, total_amount, delivery_address, created_at) VALUES
(24, 5, 1, 'PREPARING', 240000, '789 Cách Mạng Tháng 8', CURRENT_TIMESTAMP - INTERVAL '8' MINUTE),
(25, 6, 1, 'PENDING', 360000, '321 Nam Kỳ Khởi Nghĩa', CURRENT_TIMESTAMP - INTERVAL '1' MINUTE),
(26, 2, 1, 'OUT_FOR_DELIVERY', 120000, 'Sư Vạn Hạnh, Quận 10', CURRENT_TIMESTAMP - INTERVAL '5' HOUR),
(27, 4, 1, 'PENDING', 178000, '221B Baker Street, Quận 1', CURRENT_TIMESTAMP - INTERVAL '2' MINUTE),
(28, 5, 1, 'CONFIRMED', 255000, ' Landmark 81, Bình Thạnh', CURRENT_TIMESTAMP - INTERVAL '15' MINUTE),
(29, 2, 1, 'PREPARING', 125000, 'Bitexco Tower, Quận 1', CURRENT_TIMESTAMP - INTERVAL '10' MINUTE),
(30, 6, 1, 'READY', 89000, 'Chung cư Safira, TP. Thủ Đức', CURRENT_TIMESTAMP - INTERVAL '5' MINUTE),
(31, 4, 1, 'OUT_FOR_DELIVERY', 215000, 'Đại học Bách Khoa, Quận 10', CURRENT_TIMESTAMP - INTERVAL '3' MINUTE),
(32, 5, 1, 'CANCELLED', 45000, 'Hẻm 123 Võ Văn Tần, Quận 3', CURRENT_TIMESTAMP - INTERVAL '30' MINUTE);


-- 7. DỮ LIỆU CHI TIẾT ĐƠN HÀNG (Order Items)
INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price) VALUES
(10, 1, 3, 89000), (10, 3, 5, 20000),
(14, 1, 5, 89000), (15, 3, 8, 20000),
(16, 1, 10, 89000), (17, 4, 2, 35000),
(22, 1, 4, 89000), (23, 3, 6, 20000),
(12, 4, 4, 35000), (20, 4, 5, 35000),
(11, 3, 2, 20000),
(27, 1, 2, 89000), 
(28, 1, 2, 89000), (28, 4, 2, 35000), 
(29, 4, 3, 35000), (29, 3, 1, 20000), 
(30, 1, 1, 89000), 
(31, 2, 3, 45000), (31, 3, 4, 20000), 
(32, 2, 1, 45000); 


ALTER TABLE users ALTER COLUMN id RESTART WITH 30;
ALTER TABLE restaurants ALTER COLUMN id RESTART WITH 10;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 10;
ALTER TABLE menu_items ALTER COLUMN id RESTART WITH 10;
ALTER TABLE orders ALTER COLUMN id RESTART WITH 50;
ALTER TABLE order_items ALTER COLUMN id RESTART WITH 100;