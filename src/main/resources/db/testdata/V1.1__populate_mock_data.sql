-- Product Categories
INSERT INTO product_categories (id, name, description) VALUES
    ('11111111-1111-1111-1111-111111111111', 'Electronics', 'Electronic devices and accessories'),
    ('22222222-2222-2222-2222-222222222222', 'Clothing',    'Apparel and fashion items');

-- Products
INSERT INTO products (id, name, description, price, weight, image_url, category_id) VALUES
    ('33333333-3333-3333-3333-333333333111', 'Laptop Pro 15',   'High-performance laptop',    1299.99, 2.10, 'https://example.com/laptop.jpg',  '11111111-1111-1111-1111-111111111111'),
    ('33333333-3333-3333-3333-333333333222', 'Wireless Mouse',  'Ergonomic wireless mouse',     29.99, 0.15, 'https://example.com/mouse.jpg',   '11111111-1111-1111-1111-111111111111'),
    ('33333333-3333-3333-3333-333333333333', 'Basic T-Shirt',   'Cotton basic t-shirt',         19.99, 0.30, 'https://example.com/tshirt.jpg',  '22222222-2222-2222-2222-222222222222');

-- Locations
INSERT INTO locations (id, name, address_country, address_city, address_county, address_street_address) VALUES
    ('44444444-4444-4444-4444-444444444111', 'Warehouse Cluj',      'Romania', 'Cluj-Napoca', 'Cluj',  'Str. Fabricii 1'),
    ('44444444-4444-4444-4444-444444444222', 'Warehouse Bucharest', 'Romania', 'Bucharest',   'Ilfov', 'Str. Industriilor 5');

-- Users (passwords are plain text placeholders — hash them when security is added)
INSERT INTO users (id, first_name, last_name, username, password, email_address, role) VALUES
    ('55555555-5555-5555-5555-555555555111', 'Admin', 'User',  'admin',   'admin123',    'admin@shop.com',       'ADMIN'),
    ('55555555-5555-5555-5555-555555555222', 'John',  'Doe',   'johndoe', 'password123', 'john.doe@example.com', 'USER');

-- Stocks
INSERT INTO stocks (id, product_id, location_id, quantity) VALUES
    ('66666666-6666-6666-6666-666666666111', '33333333-3333-3333-3333-333333333111', '44444444-4444-4444-4444-444444444111', 10),
    ('66666666-6666-6666-6666-666666666222', '33333333-3333-3333-3333-333333333222', '44444444-4444-4444-4444-444444444111', 50),
    ('66666666-6666-6666-6666-666666666333', '33333333-3333-3333-3333-333333333333', '44444444-4444-4444-4444-444444444222', 100);

-- Orders
INSERT INTO orders (id, user_id, created_at, address_country, address_city, address_county, address_street_address) VALUES
    ('77777777-7777-7777-7777-777777777111', '55555555-5555-5555-5555-555555555222', now(), 'Romania', 'Cluj-Napoca', 'Cluj', 'Str. Memorandumului 10');

-- Order Details
INSERT INTO order_details (id, order_id, product_id, shipped_from_location_id, quantity) VALUES
    ('88888888-8888-8888-8888-888888888111', '77777777-7777-7777-7777-777777777111', '33333333-3333-3333-3333-333333333111', '44444444-4444-4444-4444-444444444111', 1),
    ('88888888-8888-8888-8888-888888888222', '77777777-7777-7777-7777-777777777111', '33333333-3333-3333-3333-333333333222', '44444444-4444-4444-4444-444444444111', 2);
