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

INSERT INTO "produto" VALUES ('SKU001', 'COOKIE CHOCOLATE', 'COOKIE DE CHOCOLATE BELGA PREMIUM', 35.9, '-', True);
INSERT INTO "produto" VALUES ('SKU002', 'COOKIE AVELA', 'COOKIE DE AVELA COM PEDACOS CROCANTES', 38.5, '-', False);
INSERT INTO "produto" VALUES ('SKU003', 'COOKIE RED VELVET', 'COOKIE SABOR RED VELVET CREMOSO', 42.0, '-', True);
INSERT INTO "produto" VALUES ('SKU004', 'COOKIE OREO', 'COOKIE COM PEDACOS DE BISCOITO OREO', 37.9, '-', False);
INSERT INTO "produto" VALUES ('SKU005', 'COOKIE NUTELLA', 'COOKIE RECHEADO COM NUTELLA', 45.0, '-', True);
INSERT INTO "produto" VALUES ('SKU006', 'COOKIE LEITE NINHO', 'COOKIE SABOR LEITE EM PO NINHO', 36.5, '-', True);
INSERT INTO "produto" VALUES ('SKU007', 'COOKIE AMENDOIM', 'COOKIE DE AMENDOIM COM MEL', 32.9, '-', False);
INSERT INTO "produto" VALUES ('SKU008', 'COOKIE BAUNILHA', 'COOKIE CLASSICO DE BAUNILHA', 30.0, '-', True);
INSERT INTO "produto" VALUES ('SKU009', 'COOKIE CANELA', 'COOKIE AROMATIZADO COM CANELA', 33.5, '-', False);
INSERT INTO "produto" VALUES ('SKU010', 'COOKIE MMS', 'COOKIE COLORIDO COM MMS', 39.9, '-', True);
INSERT INTO "produto" VALUES ('SKU011', 'COOKIE LIMÃO', 'COOKIE CÍTRICO SABOR LIMÃO SICILIANO', 34.0, '-', True);
INSERT INTO "produto" VALUES ('SKU012', 'COOKIE COCO', 'COOKIE DE COCO QUEIMADO', 31.9, '-', False);
INSERT INTO "produto" VALUES ('SKU013', 'COOKIE CAFÉ', 'COOKIE SABOR CAFÉ EXPRESSO', 36.0, '-', True);
INSERT INTO "produto" VALUES ('SKU014', 'COOKIE BRIGADEIRO', 'COOKIE SABOR BRIGADEIRO GOURMET', 40.5, '-', False);
INSERT INTO "produto" VALUES ('SKU015', 'COOKIE AMERICANO', 'COOKIE ESTILO AMERICANO TRADICIONAL', 35.0, '-', True);
INSERT INTO "produto" VALUES ('SKU016', 'COOKIE CASTANHA', 'COOKIE DE CASTANHA-DO-PARÁ', 43.9, '-', True);
INSERT INTO "produto" VALUES ('SKU017', 'COOKIE MARACUJÁ', 'COOKIE COM RECHEIO DE MARACUJÁ', 37.0, '-', False);
INSERT INTO "produto" VALUES ('SKU018', 'COOKIE LEITE CONDENSADO', 'COOKIE SABOR LEITE CONDENSADO CASEIRO', 38.0, '-', True);
INSERT INTO "produto" VALUES ('SKU019', 'COOKIE AMENDOAS', 'COOKIE COM AMENDOAS LAMINADAS', 41.5, '-', True);
INSERT INTO "produto" VALUES ('SKU020', 'COOKIE MORANGO', 'COOKIE SABOR MORANGO COM CHOCOLATE', 36.9, '-', False);
INSERT INTO "produto" VALUES ('SKU021', 'COOKIE NOZES', 'COOKIE COM NOZES PICADAS', 39.5, '-', True);
INSERT INTO "produto" VALUES ('SKU022', 'COOKIE BRANCO', 'COOKIE DE CHOCOLATE BRANCO', 35.9, '-', False);
INSERT INTO "produto" VALUES ('SKU023', 'COOKIE AVELA CROCANTE', 'COOKIE COM AVELA E TEXTURA CROCANTE', 40.0, '-', True);
INSERT INTO "produto" VALUES ('SKU024', 'COOKIE MAÇA CANELA', 'COOKIE SABOR MAÇÃ COM CANELA', 34.5, '-', True);
INSERT INTO "produto" VALUES ('SKU025', 'COOKIE DARK CHOCOLATE', 'COOKIE DE CHOCOLATE 70% CACAU', 42.5, '-', False);
INSERT INTO "produto" VALUES ('SKU026', 'COOKIE AMENDOIM CARAMELO', 'COOKIE DE AMENDOIM COM CARAMELO', 37.5, '-', True);
INSERT INTO "produto" VALUES ('SKU027', 'COOKIE BANANA', 'COOKIE SABOR BANANA COM CANELA', 33.0, '-', True);
INSERT INTO "produto" VALUES ('SKU028', 'COOKIE LARANJA', 'COOKIE CÍTRICO SABOR LARANJA', 32.5, '-', False);
INSERT INTO "produto" VALUES ('SKU029', 'COOKIE MENTA CHOCOLATE', 'COOKIE SABOR MENTA COM CHOCOLATE', 39.0, '-', True);
INSERT INTO "produto" VALUES ('SKU030', 'COOKIE TRIPLO CHOCOLATE', 'COOKIE COM TRÊS TIPOS DE CHOCOLATE', 44.9, '-', True);