ALTER TABLE users ALTER COLUMN id RESTART WITH 10;
ALTER TABLE restaurants ALTER COLUMN id RESTART WITH 10;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 10;
ALTER TABLE menu_items ALTER COLUMN id RESTART WITH 20;
ALTER TABLE carts ALTER COLUMN id RESTART WITH 10;
ALTER TABLE cart_items ALTER COLUMN id RESTART WITH 10;
ALTER TABLE notifications ALTER COLUMN id RESTART WITH 10;
ALTER TABLE orders ALTER COLUMN id RESTART WITH 1;
ALTER TABLE order_items ALTER COLUMN id RESTART WITH 1;

INSERT INTO users (id, fullname, username, email, password, role, status, phone, created_at, updated_at) VALUES
                                                                                                             (1, 'Quản Trị Viên', 'admin', 'admin@food.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'ADMIN', 'ACTIVE', '0901234567', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                             (2, 'Nguyễn Văn Khách', 'khachhang1', 'khach1@gmail.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'USER', 'ACTIVE', '0912345678', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                             (3, 'Chủ Quán Burger', 'nhahang1', 'contact@burgerking.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'RESTAURANT', 'ACTIVE', '0923456789', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                             (4, 'Chủ Quán Pizza', 'nhahang2', 'contact@pizza4ps.com', '$2a$10$8WJmth6KBCIu3G9.3Zls7Osh06p7Vsh.A7mPrZ0S5H.K.fK6pI3Gu', 'RESTAURANT', 'ACTIVE', '0934567890', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO restaurants (id, owner_id, name, address, phone, is_open, status, description, image_url, created_at, updated_at) VALUES
                                                                                                                                  (1, 3, 'Burger King Quận 1', '123 Nguyễn Huệ, Quận 1', '0923456789', true, 'ACTIVE', 'Hương vị Burger nướng lửa truyền thống từ Mỹ', 'https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                  (2, 4, 'Pizza 4P''s Bến Thành', '8 Thủ Khoa Huân, Quận 1', '0934567890', true, 'ACTIVE', 'Pizza thủ công nướng củi phong cách Nhật - Ý', 'https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO categories (id, restaurant_id, name, created_at, updated_at) VALUES
                                                                             (1, 1, 'Burger Best Seller', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                             (2, 1, 'Đồ uống & Tráng miệng', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                             (3, 2, 'Pizza Signature', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                             (4, 2, 'Mì Ý & Salad', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO menu_items (id, category_id, name, description, price, is_available, image_url, created_at, updated_at) VALUES
                                                                                                                        (1, 1, 'Whopper Burger', 'Burger bò nướng lửa kèm rau tươi và sốt đặc trưng', 89000.00, true, 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                        (2, 1, 'Cheese Burger', 'Burger phô mai bò nướng lửa', 55000.00, true, 'https://images.unsplash.com/photo-1550547660-d9450f859349?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                        (3, 2, 'Coca Cola', 'Lon 330ml', 20000.00, true, 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                        (4, 2, 'Khoai tây chiên', 'Size L, giòn rụm', 35000.00, true, 'https://images.unsplash.com/photo-1576107232684-1279f390859f?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                        (5, 3, 'Burrata Parma Ham Pizza', 'Pizza kèm phô mai Burrata tươi mềm mịn và thịt nguội Ý', 290000.00, true, 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                        (6, 3, 'Margherita Pizza', 'Pizza truyền thống với sốt cà chua, phô mai Mozzarella và lá húng quế', 150000.00, true, 'https://images.unsplash.com/photo-1604068549290-dea0e4a305ca?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                        (7, 4, 'Crab Tomato Cream Spaghetti', 'Mì Ý sốt kem cà chua thịt cua đậm đà', 220000.00, true, 'https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=500&q=80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO carts (id, customer_id, created_at, updated_at) VALUES (1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO cart_items (id, cart_id, menu_item_id, quantity, created_at, updated_at) VALUES
                                                                                         (1, 1, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                         (2, 1, 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO notifications (id, user_id, message, is_read, type, created_at, updated_at) VALUES
    (1, 1, 'Hệ thống đã sẵn sàng vận hành.', false, 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);