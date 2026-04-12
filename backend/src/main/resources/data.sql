
ALTER TABLE users ALTER COLUMN id RESTART WITH 10;
ALTER TABLE restaurants ALTER COLUMN id RESTART WITH 10;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 10;
ALTER TABLE menu_items ALTER COLUMN id RESTART WITH 10;
ALTER TABLE orders ALTER COLUMN id RESTART WITH 10;
ALTER TABLE order_items ALTER COLUMN id RESTART WITH 10;


-- 1. Bảng User 
INSERT INTO users (id, fullname, username, email, password, role, status, phone) VALUES 
(1, 'Quản Trị Viên', 'admin', 'admin@food.com', '123456', 'ADMIN', 'ACTIVE', '0901234567'), 
(2, 'Nguyễn Văn Khách', 'khachhang1', 'khach1@gmail.com', '123456', 'USER', 'ACTIVE', '0912345678'), 
(3, 'Chủ Quán Burger', 'nhahang1', 'contact@burgerking.com', '123456', 'RESTAURANT', 'ACTIVE', '0923456789');

-- 2. Bảng Restaurant 
INSERT INTO restaurants (id, user_id, name, address, phone_number, is_open, status) VALUES
(1, 3, 'Burger King Quận 1', '123 Nguyễn Huệ, Quận 1', '0923456789', true, 'ACTIVE');

-- 3. Bảng Categories
INSERT INTO categories (id, restaurant_id, name) VALUES
(1, 1, 'Burger Best Seller'),
(2, 1, 'Đồ uống');

-- 4. Bảng Menu Items
INSERT INTO menu_items (id, category_id, name, description, price, is_available) VALUES
(1, 1, 'Whopper Burger', 'Burger bò nướng lửa hồng', 65000.00, true),
(2, 1, 'Cheeseburger', 'Burger bò phô mai', 45000.00, true),
(3, 2, 'Coca Cola', 'Ly lớn', 15000.00, true);

-- 5. Bảng Orders 
INSERT INTO orders (id, customer_id, restaurant_id, total_amount, delivery_fee, status, delivery_address, customer_note) VALUES
(1, 2, 1, 125000.00, 15000.00, 'PENDING', '456 Lê Lợi, Quận 1', 'Vui lòng cho thêm nhiều tương ớt và không hành'); 

-- 6. Bảng Order Items
INSERT INTO order_items (id, order_id, menu_item_id, quantity, unit_price) VALUES
(1, 1, 1, 1, 65000.00), 
(2, 1, 2, 1, 45000.00), 
(3, 1, 3, 1, 15000.00);