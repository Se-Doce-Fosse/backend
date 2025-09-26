db = db.getSiblingDB('db-se-doce-fosse');

// Inserir dados iniciais
db.user.insertMany([
  {
    "nome": "Jhon Doe",
    "email": "j.doe@example.com",
    "telefone": "123456789",
    "carrinho": [
      {
        "produtoID": "SKU1",
        "quantidade": 2,
        "precoProduto": 97.30,
        "imagemProduto": "imagens/cookie1.png",
        "descricaoProduto": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
      }
    ]
  }
]);

db.categorias.insertMany([
  {
    "nome": "Especial",
    "descricao": "Produtos Para Datas Especiais",
    "produtos": ["SKU1", "SKU2", "SKU3"]
  }
]);

print('Dados iniciais inseridos com sucesso!');