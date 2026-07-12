CREATE TABLE tb_group
(
    id   BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,

    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE tb_category
(
    id   BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,

    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE tb_client
(
    id     BIGINT NOT NULL AUTO_INCREMENT,
    cpf    VARCHAR(255) NOT NULL,
    name   VARCHAR(255) NOT NULL,
    phone  VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    registration_date DATETIME(6) NOT NULL,

    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE tb_product
(
    id            BIGINT NOT NULL AUTO_INCREMENT,
    name          VARCHAR(255) NOT NULL,
    current_price DECIMAL(19,2) NOT NULL,
    category_id   BIGINT NOT NULL,
    group_id       BIGINT NOT NULL,

    PRIMARY KEY (id),

    KEY FK_tb_product_category (category_id),
    KEY FK_tb_product_group (group_id),

    CONSTRAINT FK_tb_product_category
        FOREIGN KEY (category_id)
            REFERENCES tb_category (id),

    CONSTRAINT FK_tb_product_group
        FOREIGN KEY (group_id)
            REFERENCES tb_group (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE tb_sale
(
    id             BIGINT NOT NULL AUTO_INCREMENT,
    client_id      BIGINT NOT NULL,
    sale_date      DATETIME(6) NOT NULL,

    PRIMARY KEY (id),

    KEY FK_tb_sale_client (client_id),

    CONSTRAINT FK_tb_sale_client
        FOREIGN KEY (client_id)
            REFERENCES tb_client (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE tb_item_sold
(
    id          BIGINT NOT NULL AUTO_INCREMENT,
    sale_id     BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    quantity    INT NOT NULL,
    unit_price  DECIMAL(19,2) NOT NULL,

    PRIMARY KEY (id),

    KEY FK_tb_item_sold_sale (sale_id),
    KEY FK_tb_item_sold_product (product_id),

    CONSTRAINT FK_tb_item_sold_sale
        FOREIGN KEY (sale_id)
            REFERENCES tb_sale (id),

    CONSTRAINT FK_tb_item_sold_product
        FOREIGN KEY (product_id)
            REFERENCES tb_product (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;