CREATE SEQUENCE order_no_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE item (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    price NUMERIC(19, 2)
);

CREATE TABLE inventory (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    item_id BIGINT NOT NULL,
    qty INT NOT NULL,
    type VARCHAR(255),
    CONSTRAINT fk_item_inventory FOREIGN KEY (item_id) REFERENCES item (id)
);

CREATE TABLE orders (
                         order_no VARCHAR(255) NOT NULL PRIMARY KEY,
                         qty INT NOT NULL,
                         price NUMERIC(19, 2),
                         item_id BIGINT,
                         item_id_snapshot BIGINT
);

