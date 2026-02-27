# 🏗️ Decisões Arquiteturais - Monolito Modular (1 projeto)

## 📋 Por que **NÃO** multi-módulo Maven agora?

### 🎯 Objetivo Principal

Criar uma arquitetura **monolito modular** que possa evoluir facilmente para **microsserviços** sem grandes refatorações, mas **mantendo 1 único app executável** (mais simples para começar).

### ✅ Vantagens da Estrutura Escolhida

#### 1. **Separação de Responsabilidades (por pacotes)**

Cada módulo é um **Bounded Context** isolado por **pacote** dentro de `src/main/java`:

```
src/main/java/com/ecommerce/
 ├── shared/     → Classes genéricas (BaseEntity, DomainEvent)
 ├── product/    → Todo código relacionado a produtos
 ├── customer/   → Todo código relacionado a clientes
 ├── order/      → Todo código relacionado a pedidos
 └── payment/    → Todo código relacionado a pagamentos
```

**Por quê?**
- Cada módulo pode evoluir independentemente
- Facilita extração futura para microsserviços
- Reduz acoplamento entre contextos

#### 2. **Ponto de entrada único (no próprio projeto)**

**Decisão**: manter `@SpringBootApplication` em `src/main/java/com/ecommerce/EcommerceApplication.java`.

**Por quê?**
- **Execução simples**: `mvn spring-boot:run`
- **Menos fricção** no início (um repositório, um build, um deploy)
- Continua modular no código (pacotes + DDD)

#### 3. **Migrations no próprio projeto**

**Decisão**: Colocar todas as migrations em `src/main/resources/db/migration/v1/`

**Por quê?**

✅ **Responsabilidade única**
- Aplicação é responsável por configurar o banco
- Migrations são parte da configuração da aplicação

✅ **Versionamento claro**
- Pasta `v1/` indica versão 1 do schema
- Futuras versões: `v2/`, `v3/`, etc.

✅ **Facilita evolução**
- Quando extrair para microsserviços, cada serviço terá suas próprias migrations
- Migrations ficam junto com o código que as usa

#### 4. **Módulo `shared/` Mínimo**

**Decisão**: Apenas classes **genéricas** no shared.

**Por quê?**

✅ **Evita acoplamento**
- Shared não depende de nenhum módulo
- Módulos dependem do shared (direção correta)

✅ **Facilita extração**
- Quando extrair para microsserviços, shared pode virar uma biblioteca comum
- Ou cada serviço pode ter sua própria versão do shared

## 🔄 Fluxo de Execução

### Como Funciona

```
1. Maven compila **um único app**
2. Spring Boot inicia e escaneia `com.ecommerce.*`
3. Flyway executa migrations em `src/main/resources/db/migration/v1/`
4. Aplicação sobe com todos os módulos (por pacote)
```

### Comando de Execução

```bash
mvn clean install
mvn spring-boot:run
```

## 🚀 Evolução para Microsserviços

### Cenário Atual (Monolito Modular)

```
app/
 ├── depende de product
 ├── depende de customer
 ├── depende de order
 └── depende de payment
```

### Cenário Futuro (Microsserviços)

**Opção 1: Cada módulo vira um serviço**

```
product-service/
 ├── app/ (Spring Boot Application)
 └── product/ (código do módulo)

customer-service/
 ├── app/
 └── customer/

order-service/
 ├── app/
 └── order/

payment-service/
 ├── app/
 └── payment/
```

**Opção 2: API Gateway**

```
api-gateway/
 └── app/ (Spring Cloud Gateway)

product-service/
 ├── app/
 └── product/

... (outros serviços)
```

## 📊 Comparação: Monolito vs Multi-Módulo

| Aspecto | Monolito Simples | Multi-Módulo (Nossa Escolha) |
|---------|------------------|------------------------------|
| **Estrutura** | Tudo em `src/` | Módulos separados |
| **Compilação** | 1 JAR | Múltiplos JARs |
| **Deploy** | 1 aplicação | 1 aplicação (agora) |
| **Evolução** | Refatoração grande | Extração simples |
| **Testes** | Testes integrados | Testes por módulo |
| **Acoplamento** | Alto | Baixo (por módulo) |

## 🎯 Decisões Específicas

### 1. Por que não colocar `@SpringBootApplication` em cada módulo?

❌ **Problema**: Se cada módulo tivesse sua própria aplicação:
- Não conseguiríamos executar tudo junto facilmente
- Dificultaria testes integrados
- Mais complexo para deploy inicial

✅ **Solução**: Módulo `app/` centralizado
- Executa tudo junto
- Facilita evolução futura
- Mantém simplicidade inicial

### 2. Por que migrations no `app/` e não em cada módulo?

❌ **Problema**: Se cada módulo tivesse suas próprias migrations:
- Flyway precisaria escanear múltiplos classpaths
- Mais complexo de configurar
- Dificulta versionamento global

✅ **Solução**: Migrations centralizadas no `app/`
- Flyway escaneia um único local
- Versionamento claro (`v1/`, `v2/`, etc.)
- Facilita evolução (quando extrair, cada serviço terá suas migrations)

### 3. Por que POM parent?

✅ **Vantagens**:
- Versões centralizadas (Spring Boot, Java, etc.)
- Dependências gerenciadas em um só lugar
- Facilita manutenção

## 📝 Resumo das Decisões

| Decisão | Motivo |
|---------|--------|
| **Multi-módulo Maven** | Separação de responsabilidades, facilita extração |
| **Módulo `app/` separado** | Ponto de entrada único, dependências explícitas |
| **Migrations no `app/`** | Responsabilidade única, versionamento claro |
| **`shared/` mínimo** | Evita acoplamento, facilita extração |
| **POM parent** | Centraliza versões e dependências |

## 🔮 Próximos Passos

1. ✅ Estrutura multi-módulo criada
2. ⏳ Testar compilação e execução
3. ⏳ Adicionar testes por módulo
4. ⏳ Implementar eventos de domínio
5. ⏳ Preparar para extração (API Gateway, Service Discovery)
