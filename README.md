# Sistema de Associados — Documentação Técnica da API

Documentação dos endpoints, regras de negócio e padrões de comunicação da API.

---

## Sumário

# C+C API — Sistema de Visibilidade e Perfis

Documentação técnica do sistema de controle de acesso, visibilidade de campos e guia de testes da API C+C.

---

## Sumário

- [Perfis de Acesso](#perfis-de-acesso)
- [Tabela de Visibilidade por Campo](#tabela-de-visibilidade-por-campo)
- [Regras Especiais](#regras-especiais)
- [Como Executar os Testes](#como-executar-os-testes)
- [Guia Postman](#guia-postman)
  - [Configuração do Environment](#1-configuração-do-environment)
  - [Autenticação](#2-autenticação--post-authlogin)
  - [Criar Associado](#3-criar-associado--post-associados)
  - [Perfil Filtrado](#4-perfil-filtrado-por-perfil--get-associadoscpfperfil)
  - [Editar Campo](#5-editar-campo-individual--put-associadoscpfcamponomecampo)
  - [Toggle de Visibilidade](#6-toggle-de-visibilidade--post-associadoscpfpermissaocampo)
  - [Confirmar Cadastro](#7-confirmar-cadastro-preativo--ativo--patch-idconfirmar-cadastro)
  - [Renovar Anuidade](#8-renovar-anuidade--patch-idrenovar-anuidade)
  - [Fluxo Completo de Teste](#9-fluxo-completo-de-teste-no-postman)
- [Cobertura de Testes](#cobertura-de-testes)

---

## Perfis de Acesso

O sistema define quatro perfis hierárquicos:

| Perfil | Nível | Descrição |
|---|---|---|
| `ADM_CC` | 1 | Acesso total. Único perfil que insere e edita dados administrativos. |
| `DIRETOR` | 2 | Acesso a dados estruturais de equipe. |
| `ASSOCIADO` | 3 | Acesso ao próprio perfil e campos públicos da rede. |
| `REDE_CC` | — | Representa toda a rede. Campos públicos visíveis a qualquer associado ativo. |

---

## Tabela de Visibilidade por Campo

Cada campo da entidade `Associado` possui controle independente de leitura, inserção e edição.

| Campo | Leitura | Inserção | Edição |
|---|---|---|---|
| `nomeCompleto` | ADM_CC, ASSOCIADO | ADM_CC | ADM_CC |
| `cpf` | ADM_CC, ASSOCIADO | ADM_CC | ADM_CC |
| `dataNascimento` | ADM_CC, ASSOCIADO | ADM_CC | ADM_CC, ASSOCIADO |
| `aniversarioDiaMes` | **Condicional ★** | ADM_CC | ADM_CC, ASSOCIADO |
| `email` | ADM_CC, ASSOCIADO | ADM_CC | ADM_CC, ASSOCIADO |
| `telefone` | ADM_CC, ASSOCIADO | ADM_CC | ADM_CC, ASSOCIADO |
| `enderecoResidencial` | ADM_CC, ASSOCIADO | ADM_CC | ADM_CC, ASSOCIADO |
| `cnpj` | ADM_CC, ASSOCIADO | ADM_CC | ADM_CC |
| `razaoSocial` | ADM_CC, ASSOCIADO | ADM_CC | ADM_CC, ASSOCIADO |
| `enderecoComercial` | **Condicional ★** | ADM_CC | ADM_CC, ASSOCIADO |
| `dataPrimeiroPagamento` | ADM_CC | ADM_CC | ADM_CC |
| `dataIngresso` | REDE_CC | ADM_CC | ADM_CC |
| `dataVencimento` | REDE_CC | Automático | ADM_CC (só renovação) |
| `cpfPadrinho` | ADM_CC | ADM_CC | ADM_CC |
| `equipeOrigem` | ADM_CC | ADM_CC | ADM_CC |
| `equipeAtual` | REDE_CC | ADM_CC | ADM_CC |
| `status` | Varia por status ▼ | ADM_CC | ADM_CC |
| `cluster` | REDE_CC | ADM_CC | ADM_CC |
| `atuacaoEspecifica` | REDE_CC | ADM_CC + ASSOCIADO | ADM_CC (pós-ativação) |
| `cargoLideranca` (nome) | REDE_CC | ADM_CC | ADM_CC |
| `cargoLideranca` (tipo) | ADM_CC | ADM_CC | ADM_CC |
| `grupamento` | REDE_CC | ADM_CC | ADM_CC, DIRETOR |

> **★ Condicional:** o associado controla via boolean `exibirNaRede`. ADM_CC e o próprio associado sempre enxergam, independentemente do valor do boolean.

> **▼ Visibilidade do campo `status` por valor:**
> - `PREATIVO`, `ATIVO`, `INATIVO_PAUSA` → visível para `REDE_CC`
> - `INATIVO_DESISTENCIA`, `INATIVO_FALECIMENTO`, `INATIVO_DESLIGADO` → visível apenas para `ADM_CC`

---

## Regras Especiais

**E-mail único:** duplicidade é rejeitada com `409 Conflict` e mensagem `"E-mail já cadastrado na base de dados"`.

**Status nunca muda automaticamente:** toda alteração de status deve ser feita exclusivamente por `ADM_CC` via endpoint explícito.

**Alerta de falecimento:** ao alterar o status para `INATIVO_FALECIMENTO`, o sistema dispara automaticamente o evento `AlertaSeguroFuneralEvent` com o CPF do associado.

**Data de vencimento calculada:** nunca recebida do cliente. Calculada como o dia 01 do mês seguinte ao mês de ingresso, do ano seguinte.
> Exemplo: ingresso em `10/02/2026` → vencimento em `01/03/2027`

**Renovação:** ao acionar o endpoint de renovação (somente `ADM_CC`), o sistema soma exatamente 12 meses ao vencimento atual e registra log.

**Cargo de liderança com vigência:** toda designação possui `dataInicio` e `dataFim` (null = indeterminado). O campo `tipo` (NORMAL/ISENTO) é invisível para `ASSOCIADO` e `REDE_CC`.

---

## Como Executar os Testes

```bash
# Todos os testes (na raiz do projeto)
mvn test

# Classe específica
mvn test -Dtest=VisibilidadeFiltroServiceTest
mvn test -Dtest=AssociadoServiceTest
mvn test -Dtest=TokenServiceTest
mvn test -Dtest=AssociadosControllerTest
```

---

## Guia Postman

### 1. Configuração do Environment

Crie um environment chamado **C+C Local** com as seguintes variáveis:

| Variável | Valor inicial |
|---|---|
| `base_url` | `http://localhost:8080/api/v1` |
| `token` | *(deixe vazio — preenchido automaticamente)* |

---

### 2. Autenticação — POST /auth/login

```http
POST {{base_url}}/auth/login
Content-Type: application/json

{
  "email": "admin@hotmail.com",
  "senha": "coti@password"
}
```

**Resposta esperada (200):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Script de Test (cole na aba Tests do Postman para salvar o token automaticamente):**

```javascript
const json = pm.response.json();
pm.environment.set("token", json.token);
```

A partir daqui, todas as requisições utilizam:

```http
Authorization: Bearer {{token}}
```

---

### 3. Criar Associado — POST /associados

> Requer perfil: **ADM_CC**

```http
POST {{base_url}}/associados
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "nomeCompleto": "Maria Oliveira",
  "cpf": "98765432100",
  "emailPrincipal": "maria@empresa.com",
  "telefonePrincipal": "11987654321",
  "dataNascimento": "1992-08-20",
  "dataIngresso": "2026-01-10",
  "tipoOrigemEquipe": "ORIGINAL",
  "statusAssociado": "PREATIVO",
  "idEquipe": 1,
  "idCluster": 1,
  "idAtuacaoEspecifica": 1,
  "idPadrinho": 1,
  "idEquipeOrigem": 1,
  "idCargoLideranca": 1,
  "dataInicioCargo": "2026-01-10",
  "exibirAniversario": true,
  "rua": "Rua das Flores",
  "numero": "42",
  "bairro": "Centro",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01310100"
}
```

**Respostas possíveis:**

| Código | Situação |
|---|---|
| `201` | Cadastro realizado com sucesso |
| `409` | CPF ou e-mail já cadastrado |
| `422` | Regra de negócio violada |
| `400` | Campo obrigatório ausente |

---

### 4. Perfil filtrado por perfil — GET /associados/{cpf}/perfil

Cada perfil recebe apenas os campos para os quais tem permissão de leitura.

```http
GET {{base_url}}/associados/98765432100/perfil
Authorization: Bearer {{token}}
```

**Resposta para `ADM_CC` (todos os campos):**

```json
{
  "nomeCompleto": "Maria Oliveira",
  "cpf": "98765432100",
  "email": "maria@empresa.com",
  "dataNascimento": "1992-08-20",
  "dataIngresso": "2026-01-10",
  "dataVencimento": "2027-02-01",
  "equipeAtual": "C+C Alpha",
  "status": "PREATIVO",
  "cpfPadrinho": "12345678901",
  "cargoLiderancaTipo": "NORMAL"
}
```

**Resposta para `ASSOCIADO` alheio (somente campos públicos):**

```json
{
  "equipeAtual": "C+C Alpha",
  "dataIngresso": "2026-01-10",
  "dataVencimento": "2027-02-01",
  "status": "PREATIVO",
  "cluster": "Tecnologia",
  "cargoLiderancaNome": "Membro Fundador"
}
```

> Campos condicionais (`aniversarioDiaMes`, `enderecoComercial`) só aparecem se o próprio associado os tiver ativado via endpoint de permissão.

---

### 5. Editar campo individual — PUT /associados/{cpf}/campo/{nomeCampo}

Cada campo possui seu próprio controle de permissão de edição.

```http
PUT {{base_url}}/associados/98765432100/campo/email
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "valorAnterior": "maria@empresa.com",
  "valorNovo": "maria.novo@empresa.com"
}
```

**Permissões de edição por campo:**

| Campo | ADM_CC | ASSOCIADO (próprio) | DIRETOR |
|---|---|---|---|
| `nomeCompleto` | ✅ | ❌ | ❌ |
| `email` | ✅ | ✅ | ❌ |
| `dataNascimento` | ✅ | ✅ | ❌ |
| `equipeAtual` | ✅ | ❌ | ❌ |
| `grupamento` | ✅ | ❌ | ✅ |
| `status` | ✅ | ❌ | ❌ |

**Respostas possíveis:**

| Código | Situação |
|---|---|
| `200` | Campo atualizado com sucesso |
| `403` | Perfil sem permissão para editar este campo |
| `409` | E-mail ou CPF duplicado |

---

### 6. Toggle de visibilidade — POST /associados/{cpf}/permissao/{campo}

Alterna se o campo condicional aparece para a rede.

```http
POST {{base_url}}/associados/98765432100/permissao/aniversarioDiaMes
Authorization: Bearer {{token}}
```

> Sem body. Campos aceitos: `aniversarioDiaMes` · `enderecoComercial`

**Respostas possíveis:**

| Código | Situação |
|---|---|
| `204` | Toggle realizado com sucesso |
| `403` | Associado tentando alterar perfil alheio |
| `422` | Campo não reconhecido |

---

### 7. Confirmar cadastro (PREATIVO → ATIVO) — PATCH /{id}/confirmar-cadastro

> Requer perfil: **ADM_CC**

```http
PATCH {{base_url}}/associados/1/confirmar-cadastro
Authorization: Bearer {{token}}
```

---

### 8. Renovar anuidade — PATCH /{id}/renovar-anuidade

> Requer perfil: **ADM_CC**. Soma 12 meses ao vencimento atual e registra log.

```http
PATCH {{base_url}}/associados/1/renovar-anuidade
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "dataPagamento": "2026-03-26"
}
```

---

### 9. Fluxo completo de teste no Postman

Execute na ordem abaixo para validar o ciclo completo do sistema:

```
1. POST  /auth/login                              → salva {{token}} via script de test
2. POST  /associados                              → cria o associado (anote o id retornado)
3. PATCH /{id}/confirmar-cadastro                 → ativa o associado
4. GET   /{cpf}/perfil                            → verifique os campos visíveis ao seu perfil
5. POST  /{cpf}/permissao/aniversarioDiaMes       → habilita o campo condicional
6. GET   /{cpf}/perfil                            → aniversarioDiaMes agora aparece para a rede
7. PUT   /{cpf}/campo/email                       → edita o e-mail
8. PATCH /{id}/renovar-anuidade                   → renova a anuidade
```

---

## Cobertura de Testes

| Arquivo | Testes | O que cobre |
|---|---|---|
| `VisibilidadeFiltroServiceTest` | 22 | Todas as combinações perfil × campo, campos condicionais, status restrito |
| `AssociadoServiceTest` | 14 | Cálculo de vencimento, renovação, status, evento funeral, toggle |
| `TokenServiceTest` | 9 | Geração, validação e extração de perfil do JWT |
| `AssociadosControllerTest` | 10 | Delegação, HTTP responses, autenticação, toggle 403 |
| **Total** | **55** | |

- [Autenticação e Cabeçalhos](#autenticação-e-cabeçalhos)
- [Endpoints](#endpoints)
  - [1. Edição do Endereço Comercial](#1-edição-do-endereço-comercial)
  - [2. Renovação da Anuidade](#2-renovação-da-anuidade)
  - [3. Confirmar Cadastro](#3-confirmar-cadastro)
  - [4. Cadastro de Associado](#4-cadastro-de-associado)
  - [5. Cargos de Liderança](#5-cargos-de-liderança)
- [Configurações Gerais](#configurações-gerais)

---

## Autenticação e Cabeçalhos

Todos os requests autenticados devem obrigatoriamente enviar os headers abaixo:

| Header          | Valor                    |
|-----------------|--------------------------|
| `Authorization` | `Bearer {{seu_token_jwt}}` |
| `Content-Type`  | `application/json`       |

---

## Endpoints

### 1. Edição do Endereço Comercial

Atualiza os dados de uma empresa. O sistema permite enviar apenas os dados básicos ou incluir o endereço para criação/atualização simultânea.

```
PUT /api/v1/empresas/{idEmpresa}
```

**A. Apenas dados da empresa**

```json
{
  "idAssociado": 1,
  "razaoSocial": "Empresa Teste LTDA",
  "cnpj": "12345678000190",
  "nomeFantasia": "Empresa Teste"
}
```

**B. Empresa com endereço (atualiza ou cria)**

```json
{
  "idAssociado": 1,
  "razaoSocial": "Empresa Teste LTDA",
  "cnpj": "12345678000190",
  "nomeFantasia": "Empresa Teste",
  "rua": "Rua das Flores",
  "numero": "100",
  "complemento": "Sala 201",
  "bairro": "Centro",
  "cidade": "Belo Horizonte",
  "estado": "MG",
  "cep": "30130000"
}
```

---

### 2. Renovação da Anuidade

Estende a vigência da anuidade de um associado.

```
PATCH /api/v1/associados/{idAssociado}/renovar-anuidade
```

> 🔒 **Role requerida:** `ADM`

**Corpo da Requisição:**

```json
{
  "dataPagamento": "25-03-2026"
}
```

**Regras de Negócio:**

- **Status:** o associado deve estar `ATIVO` (não pode ser `PREATIVO`).
- **Isenção:** associado não pode possuir cargo do tipo `ISENTO`.
- **Cálculo de data:**
  - *Dentro do prazo:* se `dataVencimento` ainda não chegou, estende +1 ano a partir do vencimento atual.
  - *Vencido:* se já venceu, o novo vencimento é calculado a partir da `dataPagamento` informada.

---

### 3. Confirmar Cadastro

Realiza a transição de status do associado recém-criado.

```
PATCH /api/v1/associados/{idAssociado}/confirmar-cadastro
```

> 🔒 **Role requerida:** `ADM` | Body: vazio.

**Fluxo de Status:**

| Etapa        | Endpoint                          | Status resultante |
|--------------|-----------------------------------|-------------------|
| Criação      | `POST /api/v1/associados`         | `PREATIVO`        |
| Confirmação  | `PATCH .../confirmar-cadastro`    | `ATIVO`           |

---

### 4. Cadastro de Associado

Endpoint principal de cadastro. O sistema possui validações automáticas baseadas no cargo.

```
POST /api/v1/associados
```

**Corpo da Requisição:**

```json
{
  "nomeCompleto": "Joao da Silva",
  "cpf": "12345678901",
  "emailPrincipal": "joao@email.com",
  "telefonePrincipal": "31999998888",
  "dataNascimento": "15-05-1990",
  "dataIngresso": "25-03-2026",
  "tipoOrigemEquipe": "INTERNO",
  "statusAssociado": "PREATIVO",
  "idEquipe": 1,
  "idCluster": 1,
  "idAtuacaoEspecifica": 1,
  "idPadrinho": 2,
  "idEquipeOrigem": 1,
  "idCargoLideranca": 5,
  "dataInicioCargo": "25-03-2026",
  "dataPagamentoPrimeiraAnuidade": null,
  "exibirAniversario": true,
  "rua": "Rua das Acácias",
  "numero": "42",
  "complemento": "",
  "bairro": "Pampulha",
  "cidade": "Belo Horizonte",
  "estado": "MG",
  "cep": "31270000"
}
```

> ⚠️ **Notas importantes:**
> - O campo `statusAssociado` no corpo é **ignorado**. O sistema sempre força o status inicial como `PREATIVO`.
> - Se o `idCargoLideranca` for um cargo com classificação financeira `ISENTO`, a `dataVencimento` será salva automaticamente como `null`.

---

### 5. Cargos de Liderança

Endpoints para gerenciamento de cargos. Todos exigem role `ADM`; caso contrário, retornam `403 Forbidden`.

| Método | Endpoint                                    | Descrição                              |
|--------|---------------------------------------------|----------------------------------------|
| `GET`  | `/api/v1/associados-cargos/{id}`            | Busca detalhe por ID do vínculo        |
| `GET`  | `/api/v1/associados-cargos/associado/{id}`  | Busca cargos de um associado específico |
| `GET`  | `/api/v1/associados-cargos/cargo/{id}`      | Busca todos os associados em um cargo  |
| `POST` | `/api/v1/associados-cargos`                 | Cria um novo vínculo de cargo          |
| `PUT`  | `/api/v1/associados-cargos/{id}`            | Atualiza um vínculo existente          |

**Exemplo de Payload (POST / PUT):**

```json
{
  "idAssociado": 1,
  "idCargoLideranca": 2,
  "dataInicio": "25-03-2026",
  "dataFim": null,
  "ativo": true
}
```

---

## Configurações Gerais

### Formato de Datas

Todas as datas trafegadas devem seguir o padrão configurado no `JacksonConfig`:

| Formato      | Exemplo      |
|--------------|--------------|
| `dd-MM-yyyy` | `25-03-2026` |

### Como Rodar a Aplicação

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

### Swagger UI

Documentação interativa disponível em:

```
http://localhost:8080/swagger-ui.html
```
