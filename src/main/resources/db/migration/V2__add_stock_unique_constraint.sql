ALTER TABLE online_shop.stocks
    ADD CONSTRAINT uq_stocks_product_location UNIQUE (product_id, location_id);
