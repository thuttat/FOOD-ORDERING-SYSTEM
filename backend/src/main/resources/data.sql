ALTER TABLE users ALTER COLUMN id RESTART WITH 10;
ALTER TABLE restaurants ALTER COLUMN id RESTART WITH 10;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 10;
ALTER TABLE menu_items ALTER COLUMN id RESTART WITH 10;
ALTER TABLE orders ALTER COLUMN id RESTART WITH 10;
ALTER TABLE order_items ALTER COLUMN id RESTART WITH 10;
ALTER TABLE carts ALTER COLUMN id RESTART WITH 10;

INSERT INTO users (id, fullname, username, email, password, role, status, phone) VALUES
                                                                                     (1, 'Quản Trị Viên', 'admin', 'admin@food.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'ADMIN', 'ACTIVE', '0901234567'),
                                                                                     (2, 'Nguyễn Văn Khách', 'khachhang1', 'khach1@gmail.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'USER', 'ACTIVE', '0912345678'),
                                                                                     (3, 'Chủ Quán Burger', 'nhahang1', 'contact@burgerking.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'RESTAURANT', 'ACTIVE', '0923456789');

INSERT INTO restaurants (id, user_id, name, address, phone_number, is_open, status, description) VALUES
    (1, 3, 'Burger King Quận 1', '123 Nguyễn Huệ, Quận 1', '0923456789', true, 'ACTIVE', 'Hương vị Burger nướng lửa truyền thống từ Mỹ');

INSERT INTO categories (id, restaurant_id, name) VALUES
                                                     (1, 1, 'Burger Best Seller'),
                                                     (2, 1, 'Đồ uống & Tráng miệng');

INSERT INTO menu_items (id, category_id, name, description, price, is_available, image_url) VALUES
                                                                                                (1, 1, 'Whopper Burger', 'Burger bò nướng lửa kèm rau tươi và sốt đặc trưng', 89000, true, 'https://example.com/whopper.jpg'),
                                                                                                (2, 1, 'Cheese Burger', 'Burger phô mai truyền thống', 45000, true, 'https://example.com/cheese.jpg'),
                                                                                                (3, 2, 'Coca Cola', 'Lon 330ml', 20000, true, 'https://example.com/coca.jpg'),
                                                                                                (4, 2, 'Khoai tây chiên', 'Size L, giòn rụm', 35000, true, 'https://example.com/fries.jpg');

INSERT INTO carts (id, customer_id) VALUES (1, 2);

INSERT INTO notifications (id, user_id, message, is_read, type, created_at) VALUES
    (1, 1, 'Hệ thống đã sẵn sàng vận hành.', false, 'SYSTEM', CURRENT_TIMESTAMP);