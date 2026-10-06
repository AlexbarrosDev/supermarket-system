CREATE TABLE tb_user (

    id          BIGINT NOT NULL AUTO_INCREMENT,
    login       VARCHAR(255) UNIQUE NOT NULL,
    password    VARCHAR(255),
    role        VARCHAR(20),

    PRIMARY KEY (id)
)