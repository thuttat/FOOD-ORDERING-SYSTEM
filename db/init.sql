-- DỮ LIỆU MẪU (MOCK DATA)
INSERT INTO users (username, password_hash, email, phone, role) VALUES
('admin', 'hashed_pw_1', 'admin@food.com', '0901234567', 'ADMIN'),
('khachhang1', 'hashed_pw_2', 'khach1@gmail.com', '0912345678', 'CUSTOMER'),
('nhahang1', 'hashed_pw_3', 'contact@burgerking.com', '0923456789', 'RESTAURANT');

INSERT INTO restaurants (user_id, name, address, description, approval_status) VALUES
(3, 'Burger King Quận 1', '123 Nguyễn Huệ, Quận 1', 'Chuyên các loại Burger và đồ ăn nhanh', 'APPROVED');

INSERT INTO categories (restaurant_id, name) VALUES
(1, 'Burger Best Seller'),
(1, 'Đồ uống');

INSERT INTO menu_items (category_id, name, description, price) VALUES
(1, 'Whopper Burger', 'Burger bò nướng lửa hồng', 65000.00),
(1, 'Cheeseburger', 'Burger bò phô mai', 45000.00),
(2, 'Coca Cola', 'Ly lớn', 15000.00);

INSERT INTO orders (customer_id, restaurant_id, total_amount, delivery_fee, status, delivery_address) VALUES
(2, 1, 125000.00, 15000.00, 'PENDING', '456 Lê Lợi, Quận 1'); 

INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price) VALUES
(1, 1, 1, 65000.00), 
(1, 2, 1, 45000.00), 
(1, 3, 1, 15000.00);