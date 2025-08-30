CREATE TABLE "estoque_insumos" (
  "id" int PRIMARY KEY,
  "nome" varchar,
  "id_unidade" int,
  "quantidade" numeric,
  "preco_compra" numeric,
  "ponto_reposicao" numeric
);

CREATE TABLE "unidade" (
  "id" int PRIMARY KEY,
  "nome" varchar,
  "base_preco_compra" numeric
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
  "quantidade" double,
  "peco_unidade" double
);

CREATE INDEX ON "estoque_insumos" ("id", "id_unidade");

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
