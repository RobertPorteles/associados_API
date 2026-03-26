📑 Documentação Técnica da API — Sistema de AssociadosDocumentação dos endpoints, regras de negócio e padrões de comunicação da API.🔐 Autenticação e CabeçalhosTodos os requests autenticados devem obrigatoriamente enviar os headers abaixo:HeaderValorAuthorizationBearer {{seu_token_jwt}}Content-Typeapplication/json🏗️ 1. Edição do Endereço ComercialAtualiza os dados de uma empresa. O sistema permite enviar apenas os dados básicos ou incluir o endereço para criação/atualização simultânea.A. Apenas dados da empresaPUT /api/v1/empresas/{idEmpresa}JSON{
  "idAssociado": 1,
  "razaoSocial": "Empresa Teste LTDA",
  "cnpj": "12345678000190",
  "nomeFantasia": "Empresa Teste"
}
B. Empresa com endereço (Atualiza ou Cria)PUT /api/v1/empresas/{idEmpresa}JSON{
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
💳 2. Renovação da AnuidadeEndpoint para estender a vigência da anuidade de um associado.PATCH /api/v1/associados/{idAssociado}/renovar-anuidade🔒 Role requerida: ADMCorpo da Requisição:JSON{
  "dataPagamento": "25-03-2026"
}
📋 Regras de NegócioStatus: O Associado deve estar ATIVO (não pode ser PREATIVO).Isenção: Associado não pode possuir cargo do tipo ISENTO.Cálculo de Data:Dentro do prazo: Se dataVencimento ainda não chegou, estende +1 ano a partir do vencimento atual.Vencido: Se já venceu, o novo vencimento é calculado a partir da dataPagamento informada.✅ 3. Confirmar CadastroRealiza a transição de status do associado recém-criado.PATCH /api/v1/associados/{idAssociado}/confirmar-cadastro🔒 Role requerida: ADM | 📥 Body: Vazio.Fluxo de StatusCriação: POST /api/v1/associados ⮕ Sempre inicia como PREATIVO.Confirmação: Chamada deste endpoint ⮕ Transiciona para ATIVO.👤 4. Cadastro de Associado (Verificação de Isento)Endpoint principal de cadastro. Note que o sistema possui validações automáticas baseadas no cargo.POST /api/v1/associadosJSON{
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
⚠️ Nota Importante:O campo statusAssociado no corpo é ignorado. O sistema sempre força o status inicial como PREATIVO.Se o idCargoLideranca for um cargo com classificação financeira ISENTO, a dataVencimento será salva automaticamente como null.🎖️ 5. Cargos de LiderançaEndpoints exclusivos para gerenciamento de cargos. Todos exigem role ADM. Caso contrário, retornam 403 Forbidden.MétodoEndpointDescriçãoGET/api/v1/associados-cargos/{id}Busca detalhe por ID do vínculo.GET/api/v1/associados-cargos/associado/{id}Busca cargos de um associado específico.GET/api/v1/associados-cargos/cargo/{id}Busca todos os associados em um cargo.POST/api/v1/associados-cargosCria um novo vínculo de cargo.PUT/api/v1/associados-cargos/{id}Atualiza um vínculo existente.Exemplo de Payload (POST/PUT):JSON{
  "idAssociado": 1,
  "idCargoLideranca": 2,
  "dataInicio": "25-03-2026",
  "dataFim": null,
  "ativo": true
}
⚙️ Configurações GeraisFormato de DatasConforme configuração do JacksonConfig, todas as datas trafegadas devem seguir o padrão:Formato: dd-MM-yyyyExemplo: 25-03-2026Como rodar a aplicaçãoBash./mvnw spring-boot:run
A aplicação estará disponível em: http://localhost:8080.Swagger UIDocumentação interativa disponível em:🔗 http://localhost:8080/swagger-ui.html
