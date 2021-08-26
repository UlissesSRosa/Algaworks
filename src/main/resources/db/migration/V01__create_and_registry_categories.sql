CREATE TABLE categories(
    code BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO categories (name) values ('Lazer');
INSERT INTO categories (name) values ('Alimentação');
INSERT INTO categories (name) values ('Supermercado');
INSERT INTO categories (name) values ('Farmácia');
INSERT INTO categories (name) values ('Outros');