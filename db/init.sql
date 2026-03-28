-- =================================================================
-- KHỞI TẠO CƠ SỞ DỮ LIỆU: FOOD ORDERING SYSTEM
-- =================================================================
CREATE DATABASE IF NOT EXISTS food_ordering_db;
USE food_ordering_db;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    role ENUM('CUSTOMER', 'RESTAURANT', 'ADMIN') NOT NULL,
    status ENUM('ACTIVE', 'LOCKED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE restaurants (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    description TEXT,
    is_open BOOLEAN DEFAULT TRUE,
    approval_status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

CREATE TABLE menu_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(255),
    is_available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    restaurant_id INT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    delivery_fee DECIMAL(10, 2) DEFAULT 0.00,
    status ENUM('PENDING', 'CONFIRMED', 'PREPARING', 'DELIVERING', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    delivery_address VARCHAR(255) NOT NULL,
    customer_note TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    menu_item_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- =================================================================
-- DỮ LIỆU MẪU (MOCK DATA)
-- =================================================================

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
(2, 1, 125000.00, 15000.00, 'COMPLETED', '456 Lê Lợi, Quận 1');

INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price) VALUES
(1, 1, 1, 65000.00), -- 1 Whopper
(1, 2, 1, 45000.00), -- 1 Cheeseburger
(1, 3, 1, 15000.00); -- 1 Coca