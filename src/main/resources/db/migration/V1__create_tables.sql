CREATE TABLE product_categories (
    id          UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE products (
    id          UUID             PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255)     NOT NULL,
    description TEXT,
    price       NUMERIC(19, 2)   NOT NULL,
    weight      DOUBLE PRECISION NOT NULL,
    image_url   VARCHAR(500),
    category_id UUID             NOT NULL,
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES product_categories (id)
);

CREATE INDEX idx_products_category_id ON products (category_id);

CREATE TABLE locations (
    id                     UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    name                   VARCHAR(255) NOT NULL,
    address_country        VARCHAR(100),
    address_city           VARCHAR(100),
    address_county         VARCHAR(100),
    address_street_address VARCHAR(255)
);

CREATE TABLE users (
    id            UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    username      VARCHAR(100) NOT NULL,
    password      VARCHAR(255) NOT NULL,
    email_address VARCHAR(255) NOT NULL,
    role          VARCHAR(50)  NOT NULL,
    CONSTRAINT uq_users_username      UNIQUE (username),
    CONSTRAINT uq_users_email_address UNIQUE (email_address),
    CONSTRAINT chk_users_role         CHECK (role IN ('ADMIN', 'USER'))
);

CREATE TABLE orders (
    id                     UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id                UUID        NOT NULL,
    created_at             TIMESTAMPTZ NOT NULL DEFAULT now(),
    address_country        VARCHAR(100),
    address_city           VARCHAR(100),
    address_county         VARCHAR(100),
    address_street_address VARCHAR(255),
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_orders_user_id ON orders (user_id);

CREATE TABLE stocks (
    id          UUID    PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id  UUID    NOT NULL,
    location_id UUID    NOT NULL,
    quantity    INTEGER NOT NULL,
    CONSTRAINT fk_stocks_product  FOREIGN KEY (product_id)  REFERENCES products (id),
    CONSTRAINT fk_stocks_location FOREIGN KEY (location_id) REFERENCES locations (id),
    CONSTRAINT chk_stocks_quantity CHECK (quantity > 0)
);

CREATE TABLE order_details (
    id                       UUID    PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id                 UUID    NOT NULL,
    product_id               UUID    NOT NULL,
    shipped_from_location_id UUID,
    quantity                 INTEGER NOT NULL,
    CONSTRAINT fk_order_details_order         FOREIGN KEY (order_id)                 REFERENCES orders (id),
    CONSTRAINT fk_order_details_product       FOREIGN KEY (product_id)               REFERENCES products (id),
    CONSTRAINT fk_order_details_shipped_from  FOREIGN KEY (shipped_from_location_id) REFERENCES locations (id),
    CONSTRAINT chk_order_details_quantity     CHECK (quantity > 0)
);

CREATE INDEX idx_order_details_product_id           ON order_details (product_id);
CREATE INDEX idx_order_details_shipped_from_location ON order_details (shipped_from_location_id);
