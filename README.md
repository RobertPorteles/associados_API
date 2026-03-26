# Sistema de Associados — Documentação Técnica da API

Documentação dos endpoints, regras de negócio e padrões de comunicação da API.

---

## Sumário

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
