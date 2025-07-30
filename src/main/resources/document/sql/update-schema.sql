CREATE TABLE member
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    nickname      VARCHAR(255) NULL,
    password_hash VARCHAR(255) NULL,
    status        VARCHAR(255) NULL,
    address       LONGTEXT NULL,
    CONSTRAINT pk_member PRIMARY KEY (id)
);

ALTER TABLE member
    ADD CONSTRAINT uc_member_address UNIQUE (address);