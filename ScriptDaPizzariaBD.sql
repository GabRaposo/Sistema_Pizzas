-- Schema do banco "pizzariadb" (ver ConnectionManager.java).
-- Reúne todas as tabelas que os DAOs já esperam encontrar no banco.
-- Rode este script inteiro antes de abrir o app pela primeira vez.

CREATE DATABASE IF NOT EXISTS pizzariadb;
USE pizzariadb;

-- Dono e Funcionário são salvos na mesma tabela (tipo = 'Dono' ou 'Funcionario')
CREATE TABLE tb_usuario (
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome   VARCHAR(120) NOT NULL,
    email  VARCHAR(160) NOT NULL,
    senha  VARCHAR(120) NOT NULL,
    tipo   VARCHAR(20)  NOT NULL
);

CREATE TABLE tb_cliente (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome     VARCHAR(120) NOT NULL,
    cpf      VARCHAR(14)  NOT NULL UNIQUE,
    endereco VARCHAR(200) NOT NULL
);

CREATE TABLE tb_pizza (
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo  VARCHAR(60) NOT NULL,
    valor DOUBLE NOT NULL
);

CREATE TABLE tb_adicional (
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome  VARCHAR(60) NOT NULL,
    valor DOUBLE NOT NULL
);

CREATE TABLE tb_estoque (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    adicional_id BIGINT NOT NULL,
    quantidade   INT NOT NULL,
    FOREIGN KEY (adicional_id) REFERENCES tb_adicional(id)
);

-- cabeçalho do pedido (ver Pedido.java / PedidoDAO.java)
CREATE TABLE tb_pedido (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id      BIGINT NOT NULL,
    estado          VARCHAR(20) NOT NULL,
    modo_entrega    VARCHAR(20) NOT NULL,
    forma_pagamento VARCHAR(20) NOT NULL,
    data_hora       DATETIME NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES tb_cliente(id)
);

-- cada linha de pizza dentro de um pedido (ver ItemPedido.java) — é isso
-- que permite várias pizzas (com tamanhos diferentes) no mesmo pedido
CREATE TABLE tb_item_pedido (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id  BIGINT NOT NULL,
    pizza_id   BIGINT NOT NULL,
    tamanho    VARCHAR(20) NOT NULL,
    quantidade INT NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES tb_pedido(id),
    FOREIGN KEY (pizza_id)  REFERENCES tb_pizza(id)
);

-- adicionais de uma linha específica do pedido (junção; linha repetida = quantidade)
CREATE TABLE tb_item_pedido_adicional (
    item_pedido_id BIGINT NOT NULL,
    adicional_id   BIGINT NOT NULL,
    FOREIGN KEY (item_pedido_id) REFERENCES tb_item_pedido(id),
    FOREIGN KEY (adicional_id)   REFERENCES tb_adicional(id)
);
