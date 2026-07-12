ALTER TABLE tb_group
    ADD CONSTRAINT UK_tb_group_name
        UNIQUE (name);

ALTER TABLE tb_category
    ADD CONSTRAINT UK_tb_category_name
        UNIQUE (name);

ALTER TABLE tb_client
    ADD CONSTRAINT UK_tb_client_cpf
        UNIQUE (cpf);