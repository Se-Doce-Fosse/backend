# Sistema de Hooks Git - Backend Spring Boot

Este projeto utiliza um sistema automatizado de validação e qualidade de código através de hooks Git configurados com Husky.

## 🎯 Hooks Implementados

### 1. **pre-commit** (`.husky/pre-commit`)
Executa **antes de cada commit**:
- ✅ Validação do nome da branch

### 2. **pre-push** (`.husky/pre-push`)
Executa **antes de cada push**:
- ✅ Validação do nome da branch
- ✅ Verificação de formatação de código
- ✅ Execução dos testes com cobertura (JaCoCo)
- ✅ Compilação do projeto

## 📋 Validação de Nomes de Branches

### Padrão Obrigatório
```
<tipo>/<número-ticket>[-descrição-opcional]
```

### Tipos Permitidos
- **`feat/`** - Para novas funcionalidades
- **`bugfix/`** - Para correções de bugs  
- **`chore/`** - Para tarefas de manutenção

### Exemplos Válidos
```
feat/123
feat/456-adicionar-sistema-login
bugfix/789
bugfix/101-corrigir-validacao-email
chore/42
chore/999-atualizar-dependencias-spring
```

### Exceções Permitidas
- **`main`** - Branch principal
- **`develop`** - Branch de desenvolvimento

## 🛠️ Scripts NPM Disponíveis

```bash
# Instalar hooks Git
npm run prepare

# Executar testes
npm run test

# Executar testes com cobertura
npm run test:coverage

# Compilar projeto
npm run build

# Verificar formatação de código
npm run format:check

# Executar validação de branch manualmente
sh .husky/validate-branch-name.sh
```

## ⚠️ O que acontece se a validação falhar?

### Branch Inválida
Se o nome da branch não seguir o padrão, você receberá uma mensagem como:

```
❌ Nome da branch 'minha-branch' é inválido!

📋 Padrão esperado:
   • feat/número-ticket[-descrição] (ex: feat/123, feat/456-adicionar-login)
   • bugfix/número-ticket[-descrição] (ex: bugfix/789, bugfix/101-corrigir-validacao)
   • chore/número-ticket[-descrição] (ex: chore/42, chore/999-atualizar-dependencias)

🚀 Exceções permitidas:
   • main (branch principal)
   • develop (branch de desenvolvimento)

🔧 Renomeie sua branch usando:
   git branch -m nome-atual-da-branch <novo-nome-valido>
```

### Testes Falhando
Se os testes falharem durante o `pre-push`, o push será bloqueado até que os testes passem.

### Compilação Falhando
Se a compilação falhar durante o `pre-push`, o push será bloqueado até que o código compile corretamente.

## 🔧 Como Renomear uma Branch

```bash
# Renomear branch atual
git branch -m nome-atual-da-branch <novo-nome-valido>

# Exemplo
git branch -m minha-branch feat/123-adicionar-login
```

## 📁 Estrutura de Arquivos

```
.husky/
├── _/                          # Arquivos do Husky
├── pre-commit                  # Hook executado antes do commit
├── pre-push                    # Hook executado antes do push
└── validate-branch-name.sh     # Script de validação de branches

package.json                    # Scripts NPM e dependências
pom.xml                        # Configuração Maven com JaCoCo
```

## 🆘 Solução de Problemas

### 1. Hooks não estão funcionando
```bash
# Reinstalar hooks
npm run prepare
```

### 2. Validação de branch falhando
```bash
# Verificar branch atual
git branch

# Executar validação manualmente
sh .husky/validate-branch-name.sh

# Renomear branch se necessário
git branch -m nome-atual feat/123-nova-funcionalidade
```

### 3. Testes falhando
```bash
# Executar testes localmente
npm run test

# Verificar cobertura
npm run test:coverage
```

### 4. Compilação falhando
```bash
# Verificar se tem JDK instalado
java -version

# Tentar compilar
npm run build
```

## 📝 Dicas

1. **Sempre crie branches a partir da branch principal** (`main` ou `develop`)
2. **Use números de ticket reais** do seu sistema de gerenciamento de projetos
3. **Mantenha as descrições curtas e descritivas**
4. **Use hífens para separar palavras** na descrição
5. **Execute os testes localmente** antes de fazer push
6. **Verifique se o código compila** antes de fazer push

## 🔄 Fluxo de Trabalho Recomendado

1. **Criar branch** com nome válido: `git checkout -b feat/123-nova-funcionalidade`
2. **Desenvolver** a funcionalidade
3. **Fazer commit**: `git commit -m "feat: implementar nova funcionalidade"`
4. **Executar testes**: `npm run test`
5. **Fazer push**: `git push origin feat/123-nova-funcionalidade`

O sistema de hooks garantirá que:
- ✅ O nome da branch seja válido
- ✅ Os testes passem
- ✅ O código compile corretamente
- ✅ A cobertura de testes seja gerada
