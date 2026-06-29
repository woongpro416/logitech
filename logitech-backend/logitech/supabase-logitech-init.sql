BEGIN;

DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS qna CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS carts CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS member CASCADE;

CREATE TABLE member (
    member_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    login_id VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    address TEXT,
    create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    CONSTRAINT uk_member_login_id UNIQUE (login_id),
    CONSTRAINT uk_member_email UNIQUE (email),
    CONSTRAINT ck_member_role CHECK (role IN ('USER', 'ADMIN'))
);

CREATE TABLE items (
    item_id BIGSERIAL PRIMARY KEY,
    item_name VARCHAR(50) NOT NULL,
    img_path VARCHAR(150) NOT NULL,
    price INTEGER NOT NULL,
    create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    stock INTEGER NOT NULL DEFAULT 0,
    category VARCHAR(20),
    CONSTRAINT ck_items_price CHECK (price >= 0),
    CONSTRAINT ck_items_stock CHECK (stock >= 0),
    CONSTRAINT ck_items_category CHECK (category IN ('NEW', 'BEST', 'RECOMMEND'))
);

CREATE TABLE carts (
    cart_id BIGSERIAL PRIMARY KEY,
    member_id BIGINT,
    item_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity INTEGER NOT NULL DEFAULT 1,
    CONSTRAINT fk_carts_member FOREIGN KEY (member_id)
        REFERENCES member(member_id) ON DELETE CASCADE,
    CONSTRAINT fk_carts_item FOREIGN KEY (item_id)
        REFERENCES items(item_id) ON DELETE CASCADE,
    CONSTRAINT ck_carts_quantity CHECK (quantity >= 1)
);

CREATE TABLE orders (
    order_id BIGSERIAL PRIMARY KEY,
    member_id BIGINT,
    address TEXT,
    payment VARCHAR(30),
    amount INTEGER NOT NULL,
    item_ids TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ORDER',
    CONSTRAINT fk_orders_member FOREIGN KEY (member_id)
        REFERENCES member(member_id) ON DELETE CASCADE,
    CONSTRAINT ck_orders_amount CHECK (amount >= 0),
    CONSTRAINT ck_orders_status CHECK (status IN ('ORDER', 'SHIPPING', 'COMPLETE', 'CANCEL')),
    CONSTRAINT ck_orders_payment CHECK (payment IS NULL OR payment IN ('card', 'bank'))
);

CREATE TABLE qna (
    id BIGSERIAL PRIMARY KEY,
    level INTEGER NOT NULL,
    parent_id BIGINT,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    writer VARCHAR(100) NOT NULL,
    view_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_qna_level CHECK (level IN (1, 2)),
    CONSTRAINT ck_qna_view_count CHECK (view_count >= 0),
    CONSTRAINT fk_qna_parent FOREIGN KEY (parent_id)
        REFERENCES qna(id) ON DELETE CASCADE
);

CREATE TABLE reviews (
    review_id BIGSERIAL PRIMARY KEY,
    member_id BIGINT,
    order_id BIGINT,
    item_id BIGINT,
    rating INTEGER NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reviews_member FOREIGN KEY (member_id)
        REFERENCES member(member_id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_order FOREIGN KEY (order_id)
        REFERENCES orders(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_item FOREIGN KEY (item_id)
        REFERENCES items(item_id) ON DELETE CASCADE,
    CONSTRAINT ck_reviews_rating CHECK (rating BETWEEN 1 AND 5),
    CONSTRAINT uk_reviews_order_item UNIQUE (order_id, item_id)
);

CREATE INDEX idx_member_name ON member(name);
CREATE INDEX idx_member_role ON member(role);
CREATE INDEX idx_items_name ON items(item_name);
CREATE INDEX idx_items_category ON items(category);
CREATE INDEX idx_items_create_at ON items(create_at DESC);
CREATE INDEX idx_carts_member_id ON carts(member_id);
CREATE INDEX idx_carts_item_id ON carts(item_id);
CREATE INDEX idx_carts_member_item ON carts(member_id, item_id);
CREATE INDEX idx_orders_member_id ON orders(member_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at DESC);
CREATE INDEX idx_orders_member_status ON orders(member_id, status);
CREATE INDEX idx_qna_parent_id ON qna(parent_id);
CREATE INDEX idx_qna_created_at ON qna(created_at DESC);
CREATE INDEX idx_qna_writer ON qna(writer);
CREATE INDEX idx_reviews_item_id ON reviews(item_id);
CREATE INDEX idx_reviews_member_id ON reviews(member_id);
CREATE INDEX idx_reviews_order_id ON reviews(order_id);
CREATE INDEX idx_reviews_created_at ON reviews(created_at DESC);

INSERT INTO member (name, login_id, password, email, phone, address, role)
VALUES
('Admin', 'admin1', '$2a$10$KcsWvMvriP99EcD84.IbeOUcW.Vt70hEQyq50PW0EFLFlFzomhuxq', 'admin@myshop.com', '010-0000-1111', 'Incheon', 'ADMIN'),
('User', 'user100', '$2a$10$KcsWvMvriP99EcD84.IbeOUcW.Vt70hEQyq50PW0EFLFlFzomhuxq', 'user100@myshop.com', '010-2222-3333', 'Seoul', 'USER');

INSERT INTO items (item_name, img_path, price, stock, category)
VALUES
('G102', '/images/items/g102.png', 25000, 50, 'BEST'),
('G304', '/images/items/g304.png', 32000, 35, 'BEST'),
('G502', '/images/items/g502.png', 59000, 20, 'RECOMMEND'),
('Lift Vertical', '/images/items/lift_vertical.png', 45000, 18, 'RECOMMEND'),
('M240', '/images/items/m240.png', 24000, 40, 'NEW'),
('M331', '/images/items/m331.png', 28000, 25, 'NEW'),
('M650', '/images/items/m650.png', 32000, 30, 'NEW'),
('MX Anywhere', '/images/items/mx_anywhere.png', 55000, 24, 'RECOMMEND'),
('MX Master 3S', '/images/items/mx_master4.png', 129000, 12, 'RECOMMEND'),
('PRO X Superlight', '/images/items/pro_x_superlight.png', 149000, 10, 'BEST'),
('PRO X2', '/images/items/pro_x2.png', 179000, 8, 'NEW');

INSERT INTO carts (member_id, item_id, quantity)
VALUES
(2, 1, 2),
(2, 4, 1);

INSERT INTO orders (member_id, address, payment, amount, item_ids, status)
VALUES
(2, 'Seoul', 'card', 95000, '1,4', 'ORDER'),
(2, 'Seoul', 'bank', 32000, '2', 'COMPLETE');

INSERT INTO qna (level, parent_id, title, content, writer, view_count)
VALUES
(1, NULL, 'G304 wireless connection issue', 'The mouse sometimes disconnects. How can I fix it?', 'user100', 12);

UPDATE qna
SET parent_id = id
WHERE level = 1 AND parent_id IS NULL;

INSERT INTO qna (level, parent_id, title, content, writer, view_count)
VALUES
(2, 1, 'RE: G304 wireless connection issue', 'Please check the battery and try another USB port first.', 'admin1', 0);

INSERT INTO reviews (member_id, order_id, item_id, rating, content)
VALUES
(2, 1, 1, 5, 'Good value and comfortable click feeling.'),
(2, 1, 4, 4, 'The vertical mouse is comfortable after a short adjustment period.'),
(2, 2, 2, 5, 'Stable wireless connection and good battery life.');

COMMIT;
