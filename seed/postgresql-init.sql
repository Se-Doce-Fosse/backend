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
DROP TABLE IF EXISTS "categoria" CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;

CREATE TABLE "unidade" (
  "id" int PRIMARY KEY,
  "nome" varchar,
  "base_preco_compra" numeric
);

CREATE TABLE "categoria" (
  "id" int PRIMARY KEY,
  "nome" varchar
);

CREATE TABLE "estoque_insumos" (
  "id" int PRIMARY KEY,
  "nome" varchar,
  "id_unidade" int,
  "quantidade" numeric,
  "preco_compra" numeric,
  "ponto_reposicao" numeric
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
  "id" int PRIMARY KEY,
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
  "id" int PRIMARY KEY,
  "cargo" varchar,
  "nome" varchar,
  "email" varchar UNIQUE,
  "senha_hash" varchar,
  "status" varchar,
  "data_criacao" timestamp,
  "ultimo_login" timestamp
);

CREATE TABLE "pedido" (
  "id" int PRIMARY KEY,
  "cliente_id" int,
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

CREATE TABLE "avaliacao" (
  "id" int PRIMARY KEY,
  "pedido_id" int,
  "cliente_id" int,
  "nota" int,
  "descricao" text,
  "nome_exibicao" varchar
);

CREATE TABLE "historico_compra" (
  "id" int PRIMARY KEY,
  "id_insumo" int,
  "id_unidade" int,
  "quantidade" DOUBLE PRECISION,
  "peco_unidade" DOUBLE PRECISION
);

ALTER TABLE "produto" ADD COLUMN "categoria_id" INT;
ALTER TABLE "produto" ADD FOREIGN KEY ("categoria_id") REFERENCES "categoria" ("id");

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
INSERT INTO categoria (id, nome) VALUES (1, 'cookies');
INSERT INTO categoria (id, nome) VALUES (2, 'bolos');

INSERT INTO produto (sku, nome, descricao, valor, imagem_url, ativo, categoria_id)
VALUES ('1', 'Cookie Oreo com Nutella', 'Delicioso cookie com recheio de Nutella e pedaços de Oreo.', 15.00, '/images/cookie-oreo.jpg', true, 1);

INSERT INTO produto (sku, nome, descricao, valor, imagem_url, ativo, categoria_id)
VALUES ('2', 'Cookie Chocolate Branco', 'Cookie macio com gotas de chocolate branco.', 15.00, '/images/cookie-branco.jpg', true, 1);

INSERT INTO produto (sku, nome, descricao, valor, imagem_url, ativo, categoria_id)
VALUES ('10', 'Bolo Red Velvet', 'Clássico bolo Red Velvet com cobertura de cream cheese.', 35.00, '/images/bolo-red-velvet.jpg', true, 2);