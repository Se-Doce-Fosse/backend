# Padrão de Nomenclatura de Branches

Este projeto utiliza um sistema automatizado de validação de nomes de branches para manter a consistência e organização do código.

## 📋 Padrão Obrigatório

Todas as branches devem seguir o padrão:

```
<tipo>/<número-ticket>[-descrição-opcional]
```

### Tipos Permitidos

- **`feat/`** - Para novas funcionalidades
- **`bugfix/`** - Para correções de bugs
- **`chore/`** - Para tarefas de manutenção, atualizações de dependências, etc.

### Exemplos Válidos

```
feat/123
feat/456-adicionar-sistema-login
bugfix/789
bugfix/101-corrigir-validacao-email
chore/42
chore/999-atualizar-dependencias-spring
```

## 🚀 Exceções Permitidas

As seguintes branches são permitidas sem seguir o padrão:

- **`main`** - Branch principal
- **`develop`** - Branch de desenvolvimento

## ⚠️ Validação Automática

O sistema de validação é executado automaticamente nos seguintes momentos:

- **Antes de cada commit** (`pre-commit` hook)
- **Antes de cada push** (`pre-push` hook)

### O que acontece se a validação falhar?

Se o nome da branch não seguir o padrão, você receberá uma mensagem de erro e a operação será bloqueada.

## 🔧 Como Renomear uma Branch

Se sua branch atual não segue o padrão, você pode renomeá-la usando:

```bash
git branch -m nome-atual-da-branch <novo-nome-valido>
```

### Exemplo

```bash
git branch -m minha-branch feat/123-adicionar-login
```

## 🛠️ Desenvolvimento

### Script de Validação

O script de validação está localizado em `.husky/validate-branch-name.sh` e pode ser executado manualmente:

```bash
.husky/validate-branch-name.sh
```

### Hooks Git

Os hooks estão configurados usando Husky e estão localizados na pasta `.husky/`:

- `.husky/pre-commit` - Executa antes de cada commit
- `.husky/pre-push` - Executa antes de cada push

## 📝 Dicas

1. **Sempre crie branches a partir da branch principal** (`main` ou `develop`)
2. **Use números de ticket reais** do seu sistema de gerenciamento de projetos
3. **Mantenha as descrições curtas e descritivas**
4. **Use hífens para separar palavras** na descrição (não espaços ou underscores)

## 🆘 Solução de Problemas

Se você encontrar problemas com a validação:

1. Verifique se está na branch correta: `git branch`
2. Execute o script manualmente para ver a mensagem de erro
3. Renomeie a branch se necessário
4. Se o problema persistir, verifique se o Husky está instalado: `npm run prepare`
