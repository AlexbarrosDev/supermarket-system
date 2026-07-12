CREATE TABLE tb_address (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        street VARCHAR(100),
        number VARCHAR(100),
        city VARCHAR(100),
        state VARCHAR(100),
        zip VARCHAR(100)
);

ALTER TABLE tb_client
    ADD COLUMN address_id BIGINT NOT NULL;

ALTER TABLE tb_client
    ADD CONSTRAINT fk_client_address
        FOREIGN KEY (address_id)
            REFERENCES tb_address(id);