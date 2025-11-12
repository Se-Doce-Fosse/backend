db = db.getSiblingDB('db-se-doce-fosse');

// Inserir dados iniciais
db.user.insertMany([
  {
    "nome": "Alice Smith",
    "telefone": "987654321",
    "carrinho": [
      {
        "produtoID": "SKU5",
        "quantidade": 1,
        "precoProduto": 125.00,
        "imagemProduto": "imagens/brownie_nutella.png",
        "descricaoProduto": "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium"
      }
    ]
  },
  {
    "nome": "Bob Johnson",
    "telefone": "555123456",
    "carrinho": [
      {
        "produtoID": "SKU12",
        "quantidade": 3,
        "precoProduto": 45.99,
        "imagemProduto": "imagens/bolo_cenoura.png",
        "descricaoProduto": "Totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo"
      },
      {
        "produtoID": "SKU15",
        "quantidade": 1,
        "precoProduto": 220.50,
        "imagemProduto": "imagens/kit_festas.png",
        "descricaoProduto": "Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit"
      }
    ]
  },
  {
    "nome": "Carlos Oliveira",
    "telefone": "11998765432",
    "carrinho": [
      {
        "produtoID": "SKU8",
        "quantidade": 5,
        "precoProduto": 35.75,
        "imagemProduto": "imagens/pao_mel.png",
        "descricaoProduto": "Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet"
      }
    ]
  },
  {
    "nome": "Diana Santos",
    "telefone": "21912345678",
    "carrinho": [
      {
        "produtoID": "SKU2",
        "quantidade": 2,
        "precoProduto": 110.80,
        "imagemProduto": "imagens/cookie_chocolate.png",
        "descricaoProduto": "Consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
      },
      {
        "produtoID": "SKU10",
        "quantidade": 1,
        "precoProduto": 88.00,
        "imagemProduto": "imagens/torta_limao.png",
        "descricaoProduto": "Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam"
      }
    ]
  },
  {
    "nome": "Eduardo Costa",
    "telefone": "31987654321",
    "carrinho": []
  },
  {
    "nome": "Fernanda Lima",
    "email": "f.lima@clientmail.co",
    "telefone": "41991234567",
    "carrinho": [
      {
        "produtoID": "SKU4",
        "quantidade": 4,
        "precoProduto": 65.50,
        "imagemProduto": "imagens/cupcake_redvelvet.png",
        "descricaoProduto": "Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur"
      },
      {
        "produtoID": "SKU7",
        "quantidade": 2,
        "precoProduto": 55.00,
        "imagemProduto": "imagens/brigadeiro_gourmet.png",
        "descricaoProduto": "At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti"
      },
      {
        "produtoID": "SKU9",
        "quantidade": 1,
        "precoProduto": 150.00,
        "imagemProduto": "imagens/bolo_casamento.png",
        "descricaoProduto": "Et harum quidem rerum facilis est et expedita distinctio"
      }
    ]
  },
  {
    "nome": "Gustavo Pereira",
    "telefone": "51988776655",
    "carrinho": [
      {
        "produtoID": "SKU1",
        "quantidade": 1,
        "precoProduto": 97.30,
        "imagemProduto": "imagens/cookie1.png",
        "descricaoProduto": "Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis"
      }
    ]
  },
  {
    "nome": "Helena Rocha",
    "telefone": "61977665544",
    "carrinho": [
      {
        "produtoID": "SKU6",
        "quantidade": 6,
        "precoProduto": 25.90,
        "imagemProduto": "imagens/muffin_blueberry.png",
        "descricaoProduto": "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur"
      }
    ]
  },
  {
    "nome": "Igor Mendes",
    "telefone": "71966554433",
    "carrinho": [
      {
        "produtoID": "SKU3",
        "quantidade": 3,
        "precoProduto": 78.50,
        "imagemProduto": "imagens/donut_glacê.png",
        "descricaoProduto": "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum"
      }
    ]
  },
  {
    "nome": "Julia Nunes",
    "telefone": "81955443322",
    "carrinho": [
      {
        "produtoID": "SKU11",
        "quantidade": 2,
        "precoProduto": 180.00,
        "imagemProduto": "imagens/cheesecake_frutas.png",
        "descricaoProduto": "Minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur"
      },
      {
        "produtoID": "SKU13",
        "quantidade": 1,
        "precoProduto": 99.90,
        "imagemProduto": "imagens/palha_italiana.png",
        "descricaoProduto": "Quis nostrum exercitationem ullam corporis suscipit laboriosam"
      }
    ]
  },
  {
    "nome": "Kleber Dias",
    "telefone": "91944332211",
    "carrinho": [
      {
        "produtoID": "SKU14",
        "quantidade": 1,
        "precoProduto": 300.00,
        "imagemProduto": "imagens/caixa_presente.png",
        "descricaoProduto": "Vel illum qui dolorem eum fugiat quo voluptas nulla pariatur"
      }
    ]
  },
  {
    "nome": "Laura Gomes",
    "telefone": "13933221100",
    "carrinho": [
      {
        "produtoID": "SKU1",
        "quantidade": 2,
        "precoProduto": 97.30,
        "imagemProduto": "imagens/cookie1.png",
        "descricaoProduto": "Reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur"
      },
      {
        "produtoID": "SKU5",
        "quantidade": 1,
        "precoProduto": 125.00,
        "imagemProduto": "imagens/brownie_nutella.png",
        "descricaoProduto": "Excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi"
      }
    ]
  },
  {
    "nome": "Marcelo Vieira",
    "telefone": "19922110099",
    "carrinho": [
      {
        "produtoID": "SKU12",
        "quantidade": 4,
        "precoProduto": 45.99,
        "imagemProduto": "imagens/bolo_cenoura.png",
        "descricaoProduto": "Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus"
      }
    ]
  },
  {
    "nome": "Nina Barreto",
    "telefone": "27911009988",
    "carrinho": [
      {
        "produtoID": "SKU3",
        "quantidade": 1,
        "precoProduto": 78.50,
        "imagemProduto": "imagens/donut_glacê.png",
        "descricaoProduto": "Omnis voluptas assumenda est, omnis dolor repellendus"
      },
      {
        "produtoID": "SKU8",
        "quantidade": 2,
        "precoProduto": 35.75,
        "imagemProduto": "imagens/pao_mel.png",
        "descricaoProduto": "Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae"
      }
    ]
  },
  {
    "nome": "Otávio Sousa",
    "telefone": "34900998877",
    "carrinho": [
      {
        "produtoID": "SKU7",
        "quantidade": 5,
        "precoProduto": 55.00,
        "imagemProduto": "imagens/brigadeiro_gourmet.png",
        "descricaoProduto": "Nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
      }
    ]
  }
]);

db.categorias.insertMany([
  {
    "nome": "Cookies Tradicionais",
    "descricao": "Nossos Clássicos Cookies Crocantes e Macios",
    "produtos": ["CK001", "CK003"]
  },
  {
    "nome": "Cookies Recheados",
    "descricao": "Nossos Clássicos Cookies Crocantes e Macios",
    "produtos": ["CK002", "CK004"]
  },
  {
    "nome": "Bolos",
    "descricao": "Bolos de Aniversário e Ocasiões Especiais",
    "produtos": ["BL001", "BL002", "BL003"]
  }
]);