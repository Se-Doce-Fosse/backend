DROP TABLE IF EXISTS "historico_compra" CASCADE;
DROP TABLE IF EXISTS "avaliacao" CASCADE;
DROP TABLE IF EXISTS "pedido_item" CASCADE;
DROP TABLE IF EXISTS "pedido" CASCADE;
DROP TABLE IF EXISTS "cupom_produto" CASCADE;
DROP TABLE IF EXISTS "cupom" CASCADE;
DROP TABLE IF EXISTS "produto_ingrediente" CASCADE;
DROP TABLE IF EXISTS "produto" CASCADE;
DROP TABLE IF EXISTS "estoque_insumos" CASCADE;
DROP TABLE IF EXISTS "unidade" CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
drop table if exists "admin" cascade;

CREATE TABLE "unidade" (
"id" BIGSERIAL PRIMARY KEY,
"nome" varchar,
"base_preco_compra" numeric
);

CREATE TABLE "estoque_insumos" (
"id" BIGSERIAL PRIMARY KEY,
"nome" varchar,
"id_unidade" int,
"quantidade" numeric,
"preco_compra" numeric,
"ponto_reposicao" numeric,
"embalagem" boolean
);

CREATE TABLE "produto" (
"sku" varchar PRIMARY KEY,
"nome" varchar,
"descricao" varchar,
"valor" numeric,
"imagem_url" varchar,
"ativo" boolean
);

CREATE TABLE "produto_ingrediente" (
"produto_sku" varchar,
"ingrediente_id" int,
"quantidade_utilizada" numeric,
PRIMARY KEY ("produto_sku", "ingrediente_id")
);

CREATE TABLE "cupom" (
"id" BIGSERIAL PRIMARY KEY,
"codigo" varchar UNIQUE,
"valor_desc" numeric,
"validade" date,
"ativo" boolean,
"unico" boolean
);

CREATE TABLE "cupom_produto" (
"cupom_id" int,
"produto_sku" varchar,
PRIMARY KEY ("cupom_id", "produto_sku")
);

CREATE TABLE "user" (
"id" varchar PRIMARY KEY,
"nome" varchar,
"telefone" varchar
);

CREATE TABLE "pedido" (
"id" BIGSERIAL PRIMARY KEY,
"cliente_id" varchar,
"data_criacao" timestamp,
"valor_total" numeric,
"status" varchar,
"cupom_id" int
);

CREATE TABLE "pedido_item" (
"pedido_id" int,
"produto_sku" varchar,
"quantidade" int,
"valor_unitario" numeric,
PRIMARY KEY ("pedido_id", "produto_sku")
);

CREATE TABLE "admin" (
"id" BIGSERIAL PRIMARY KEY,
"username" VARCHAR(255) NOT NULL UNIQUE,
"email" VARCHAR(255) NOT NULL UNIQUE,
"password" VARCHAR(255) NOT NULL,
"role" VARCHAR(50)
);

CREATE TABLE "avaliacao" (
"id" BIGSERIAL PRIMARY KEY,
"pedido_id" int,
"cliente_id" varchar,
"nota" int,
"descricao" text,
"nome_exibicao" varchar
);

CREATE TABLE "historico_compra" (
"id" BIGSERIAL PRIMARY KEY,
"id_insumo" int,
"id_unidade" int,
"quantidade" DOUBLE PRECISION,
"peco_unidade" DOUBLE PRECISION
);

ALTER TABLE "estoque_insumos" ADD FOREIGN KEY ("id_unidade") REFERENCES "unidade" ("id");
ALTER TABLE "produto_ingrediente" ADD FOREIGN KEY ("produto_sku") REFERENCES "produto" ("sku");
ALTER TABLE "produto_ingrediente" ADD FOREIGN KEY ("ingrediente_id") REFERENCES "estoque_insumos" ("id");
ALTER TABLE "cupom_produto" ADD FOREIGN KEY ("cupom_id") REFERENCES "cupom" ("id");
ALTER TABLE "cupom_produto" ADD FOREIGN KEY ("produto_sku") REFERENCES "produto" ("sku");
ALTER TABLE "pedido" ADD FOREIGN KEY ("cliente_id") REFERENCES "user" ("id");
ALTER TABLE "pedido" ADD FOREIGN KEY ("cupom_id") REFERENCES "cupom" ("id");
ALTER TABLE "pedido_item" ADD FOREIGN KEY ("pedido_id") REFERENCES "pedido" ("id");
ALTER TABLE "pedido_item" ADD FOREIGN KEY ("produto_sku") REFERENCES "produto" ("sku");
ALTER TABLE "avaliacao" ADD FOREIGN KEY ("pedido_id") REFERENCES "pedido" ("id");
ALTER TABLE "avaliacao" ADD FOREIGN KEY ("cliente_id") REFERENCES "user" ("id");
ALTER TABLE "historico_compra" ADD FOREIGN KEY ("id_insumo") REFERENCES "estoque_insumos" ("id");
ALTER TABLE "historico_compra" ADD FOREIGN KEY ("id_unidade") REFERENCES "unidade" ("id");

-- inserts iniciais
INSERT INTO "unidade" ("nome", "base_preco_compra") VALUES
('KG', 1000.00),
('UN', 1.00),
('L', 1.00);

INSERT INTO "user" ("id","nome", "telefone") VALUES
('1','Alice Smith', '987654321'),
('2','Bob Johnson', '555123456'),
('3','Carlos Oliveira', '11998765432');

INSERT INTO "admin" ("username", "email", "password", "role") VALUES
('admin_master', 'admin@email.com', '$2a$10$CKonyZomgg/CVkYBy.Ex9.stAr8SBerpSE8igTBlk5I..YRniz4ta', 'ROLE_OWNER');

INSERT INTO "cupom" ("codigo", "valor_desc", "validade", "ativo", "unico") VALUES
('DOCE10', 10.00, '2025-12-31', true, false),
('BEMVINDO', 5.00, '2025-10-30', true, true),
('BOLOFESTA', 15.00, '2025-12-30', true, false);

INSERT INTO "estoque_insumos" ("nome", "id_unidade", "quantidade", "preco_compra", "ponto_reposicao", "embalagem") VALUES
('Farinha de Trigo', 1, 50000, 4.50, 10000, false),
('Açúcar Mascavo', 1, 25000, 8.00, 5000, false),
('Manteiga Sem Sal', 1, 20000, 35.00, 4000, false),
('Ovos', 2, 120, 0.80, 30, false),
('Gotas de Chocolate Meio Amargo', 1, 15000, 45.00, 3000, false),
('Essência de Baunilha', 3, 5, 25.00, 1, false),
('Fermento Químico', 1, 2, 1500, 500, false),
('Leite Integral', 3, 10, 5.50, 3, false),
('Chocolate em Pó 50%', 1, 8, 30000, 2000, false),
('Café em Grãos Moído', 1, 5, 50000, 1000, false),
('Embalagem Individual para Cookie', 2, 500, 0.50, 100, true),
('Caixa para Bolo Pequeno', 2, 50, 3.00, 10, true);

INSERT INTO "produto" ("sku", "nome", "descricao", "valor", "imagem_url", "ativo") VALUES
('CK001', 'Cookie Clássico com Gotas de Chocolate', 'Massa amanteigada com baunilha e gotas de chocolate meio amargo.', 41.50, 'https://static.ifood-static.com.br/image/upload/t_medium/pratos/96aa4ce9-22d9-4993-8f81-01caf03c2d31/202506161754_8J5A_i.jpg', true),
('CK002', 'Cookie Chocolate Branco', 'Prepare-se para uma verdadeira explosão de sabores com o nosso irresistível Cookie de chocolate branco! ', 15.50, 'https://static.ifood-static.com.br/image/upload/t_medium/pratos/96aa4ce9-22d9-4993-8f81-01caf03c2d31/202502272218_TE6C_i.jpg', true),
('CK003', 'Cookie Dark Chocolate', 'Uma verdadeira explosão de sabores para os chocólatras de plantão! Nossa massa rica e intensa em cacau é a base perfeita para pedaços generosos de chocolate branco, ao leite e meio amargo, que se derretem delicadamente em cada mordida.', 52.50, 'https://static.ifood-static.com.br/image/upload/t_medium/pratos/96aa4ce9-22d9-4993-8f81-01caf03c2d31/202506161756_NB65_i.jpg', true),
('CK004', 'Cookie Nozes com Doce de Leite', 'Mergulhe em um mundo de sabor com o nosso Cookie de Nozes com Doce de Leite, uma verdadeira obra-prima que vai encantar seus sentidos! ', 17.90, 'https://static.ifood-static.com.br/image/upload/t_medium/pratos/96aa4ce9-22d9-4993-8f81-01caf03c2d31/202506161909_5T2Y_i.jpg', true),
('CK005', 'Cookie Romeu e Julieta', 'Uma releitura irresistível do clássico brasileiro!', 15.90, 'https://static.ifood-static.com.br/image/upload/t_medium/pratos/96aa4ce9-22d9-4993-8f81-01caf03c2d31/202506162235_DC6F_i.jpg', true),
('BL001', 'Brownie com Nutella', 'Marmitinha de brownie com um delicioso recheio de Nutella', 20.90, 'https://static.ifood-static.com.br/image/upload/t_medium/pratos/96aa4ce9-22d9-4993-8f81-01caf03c2d31/202502272219_4KDY_i.jpg', true),
('BL002', 'Bolo de Chocolate Intenso (fatia)', 'Fatia generosa de bolo de chocolate com cobertura de brigadeiro.', 35.00, 'https://static.ifood-static.com.br/image/upload/t_medium/pratos/96aa4ce9-22d9-4993-8f81-01caf03c2d31/202508091609_49LG_.jpeg', true),
('BL003', 'Bolo de Cenoura com Chocolate (inteiro)', 'Bolo fofinho de cenoura com cobertura de chocolate. Serve 8 pessoas.', 25.00, 'https://static.ifood-static.com.br/image/upload/t_medium/pratos/96aa4ce9-22d9-4993-8f81-01caf03c2d31/202508091609_49LG_.jpeg', true);

INSERT INTO "produto_ingrediente" ("produto_sku", "ingrediente_id", "quantidade_utilizada") VALUES
('CK001', 1, 0.5),
('CK001', 2, 0.3),
('CK001', 3, 0.250),
('CK001', 5, 0.2),
('CK001', 11, 1),
('BL001', 1, 0.8),
('BL001', 4, 1),
('BL001', 8, 0.6),
('BL001', 9, 0.5),
('BL002', 12, 1);

INSERT INTO "cupom_produto" ("cupom_id", "produto_sku") VALUES
(3, 'BL002');

INSERT INTO "historico_compra" ("id_insumo", "id_unidade", "quantidade", "peco_unidade") VALUES
(1, 1, 25, 4.20),
(5, 1, 5, 42.50);

INSERT INTO "pedido" ("cliente_id", "data_criacao", "valor_total", "status", "cupom_id") VALUES
('1', '2025-09-26 14:00:00', 13.00, 'ENTREGUE', 2),
('1', '2025-09-27 18:10:00', 45.00, 'PAGAMENTO_APROVADO', 3),
('2', NOW(), 22.00, 'EM_PREPARACAO', NULL);

INSERT INTO "pedido_item" ("pedido_id", "produto_sku", "quantidade", "valor_unitario") VALUES
(1, 'CK001', 4, 4.50),
(2, 'BL002', 1, 65.00),
(3, 'BL001', 1, 15.00),
(3, 'CK002', 1, 7.00);

INSERT INTO "avaliacao" ("pedido_id", "cliente_id", "nota", "descricao", "nome_exibicao") VALUES
(1, '1', 5, 'Os melhores cookies que já comi! Chegou quentinho.', 'João S.');