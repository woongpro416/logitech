CREATE TABLE IF NOT EXISTS member (
    member_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    login_id VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50),
    address TEXT,
    create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    CONSTRAINT ck_member_role CHECK (role IN ('USER', 'ADMIN'))
);

CREATE TABLE IF NOT EXISTS items (
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

CREATE TABLE IF NOT EXISTS carts (
    cart_id BIGSERIAL PRIMARY KEY,
    member_id BIGINT NOT NULL REFERENCES member(member_id) ON DELETE CASCADE,
    item_id BIGINT REFERENCES items(item_id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity INTEGER NOT NULL DEFAULT 1,
    CONSTRAINT ck_carts_quantity CHECK (quantity >= 1)
);

CREATE TABLE IF NOT EXISTS orders (
    order_id BIGSERIAL PRIMARY KEY,
    member_id BIGINT NOT NULL REFERENCES member(member_id) ON DELETE CASCADE,
    address TEXT,
    payment VARCHAR(30) NOT NULL,
    amount INTEGER NOT NULL,
    item_ids TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ORDER',
    CONSTRAINT ck_orders_amount CHECK (amount >= 0),
    CONSTRAINT ck_orders_payment CHECK (payment IN ('card', 'bank')),
    CONSTRAINT ck_orders_status CHECK (status IN ('ORDER', 'SHIPPING', 'COMPLETE', 'CANCEL'))
);

CREATE TABLE IF NOT EXISTS qna (
    id BIGSERIAL PRIMARY KEY,
    level INTEGER NOT NULL,
    parent_id BIGINT,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    writer VARCHAR(100) NOT NULL,
    view_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_qna_level CHECK (level IN (1, 2)),
    CONSTRAINT ck_qna_view_count CHECK (view_count >= 0)
);

CREATE TABLE IF NOT EXISTS reviews (
    review_id BIGSERIAL PRIMARY KEY,
    member_id BIGINT NOT NULL REFERENCES member(member_id) ON DELETE CASCADE,
    order_id BIGINT NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    item_id BIGINT NOT NULL REFERENCES items(item_id) ON DELETE CASCADE,
    rating INTEGER NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_reviews_rating CHECK (rating BETWEEN 1 AND 5),
    CONSTRAINT uk_reviews_order_item UNIQUE (order_id, item_id)
);

CREATE INDEX IF NOT EXISTS idx_items_category ON items(category);
CREATE INDEX IF NOT EXISTS idx_orders_member_id ON orders(member_id);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_qna_parent_id ON qna(parent_id);
CREATE INDEX IF NOT EXISTS idx_reviews_item_id ON reviews(item_id);

INSERT INTO member (name, login_id, password, email, phone, address, role)
VALUES
('Admin', 'admin1', '$2a$10$rJZ.laG9gT67N79n94f8EeL5CkCKjC3YH.fsm7HZLOkB0Ody/O326', 'admin@example.com', '010-0000-1111', 'Incheon', 'ADMIN'),
('이용자100', 'user100', '$2a$10$rJZ.laG9gT67N79n94f8EeL5CkCKjC3YH.fsm7HZLOkB0Ody/O326', 'user100@example.com', '010-2222-3333', 'Seoul', 'USER')
ON CONFLICT (login_id) DO NOTHING;

INSERT INTO items (item_name, img_path, price, stock, category)
VALUES
('G102', '/images/items/g102.png', 25000, 50, 'BEST'),
('G304', '/images/items/g304.png', 32000, 35, 'BEST'),
('G502', '/images/items/g502.png', 59000, 20, 'RECOMMEND'),
('Lift Vertical', '/images/items/lift_vertical.png', 45000, 18, 'RECOMMEND'),
('M240', '/images/items/m240.png', 24000, 40, 'NEW'),
('M331', '/images/items/m331.png', 28000, 25, 'NEW'),
('M650', '/images/items/m650.png', 42000, 22, 'NEW'),
('MX Anywhere', '/images/items/mx_anywhere.png', 89000, 14, 'RECOMMEND'),
('MX Master', '/images/items/mx_master4.png', 129000, 12, 'RECOMMEND'),
('PRO X SUPERLIGHT', '/images/items/pro_x_superlight.png', 169000, 8, 'BEST'),
('PRO X 2', '/images/items/pro_x2.png', 279000, 6, 'RECOMMEND')
ON CONFLICT DO NOTHING;

INSERT INTO carts (member_id, item_id, quantity)
SELECT m.member_id, i.item_id, 1
FROM member m
JOIN items i ON i.item_name = 'G304'
WHERE m.login_id = 'user100'
ON CONFLICT DO NOTHING;

INSERT INTO orders (member_id, address, payment, amount, item_ids, status)
SELECT m.member_id, m.address, 'card', 57000, '1,2', 'ORDER'
FROM member m
WHERE m.login_id = 'user100'
ON CONFLICT DO NOTHING;

INSERT INTO qna (level, parent_id, title, content, writer, view_count)
VALUES
(1, NULL, 'G304 wireless connection issue', 'The receiver sometimes disconnects. How can I troubleshoot it?', '이용자100', 4),
(1, NULL, 'Is Lift Vertical good for long work sessions?', 'I want to know whether it helps with wrist strain.', '이용자100', 2)
ON CONFLICT DO NOTHING;

UPDATE qna SET parent_id = id WHERE level = 1 AND parent_id IS NULL;

INSERT INTO qna (level, parent_id, title, content, writer, view_count)
SELECT 2, q.id, 'RE: ' || q.title, 'Please check the battery, receiver port, and Logitech Options settings first.', 'Admin', 0
FROM qna q
WHERE q.level = 1 AND q.title = 'G304 wireless connection issue'
ON CONFLICT DO NOTHING;
