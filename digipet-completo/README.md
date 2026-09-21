# DigiPet Care

MVP do backend para gestão multi-tenant de clínicas e petshops, conforme o documento de system design recebido.

## Executar

Defina `DB_PASSWORD` e inicie o PostgreSQL: `docker compose up -d`. Em seguida execute `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`). O Flyway cria o schema automaticamente.

> Este ambiente de sandbox não tem acesso ao Maven Central, então não foi possível rodar `mvn compile`/`mvn test` aqui. O código foi revisado manualmente (getters/setters Lombok e assinaturas de repositório conferidos um a um), mas rode `./mvnw verify` no seu ambiente antes de subir para produção.

## Endpoints

Todas as rotas de recursos "filhos" de uma clínica são escopadas por `clinicaId`. **A rota por clínica na URL é provisória**: antes de produção, o `clinicaId` deve ser obtido do JWT/sessão autenticada, conforme o desenho de segurança do projeto — hoje qualquer chamador pode informar qualquer `clinicaId` na URL.

### Clínicas
- `GET /api/v1/clinicas`
- `GET /api/v1/clinicas/{id}`
- `POST /api/v1/clinicas`
- `PUT /api/v1/clinicas/{id}`

### Usuários (profissionais, atendentes, admins de clínica)
- `GET /api/v1/clinicas/{clinicaId}/usuarios`
- `GET /api/v1/clinicas/{clinicaId}/usuarios/{id}`
- `POST /api/v1/clinicas/{clinicaId}/usuarios`
- `PUT /api/v1/clinicas/{clinicaId}/usuarios/{id}`
- `PATCH /api/v1/clinicas/{clinicaId}/usuarios/{id}/inativar`
- `PATCH /api/v1/clinicas/{clinicaId}/usuarios/{id}/reativar`

### Donos e Pets
- `GET|POST /api/v1/clinicas/{clinicaId}/donos`
- `GET|PUT|DELETE /api/v1/clinicas/{clinicaId}/donos/{id}`
- `GET|POST /api/v1/clinicas/{clinicaId}/donos/{donoId}/pets`
- `GET|PUT|DELETE /api/v1/clinicas/{clinicaId}/donos/{donoId}/pets/{id}`

### Serviços
- `GET /api/v1/clinicas/{clinicaId}/servicos` (aceita `?apenasAtivos=true`)
- `GET|POST /api/v1/clinicas/{clinicaId}/servicos`
- `PUT /api/v1/clinicas/{clinicaId}/servicos/{id}`
- `PATCH /api/v1/clinicas/{clinicaId}/servicos/{id}/inativar`
- `PATCH /api/v1/clinicas/{clinicaId}/servicos/{id}/reativar`

### Escalas do profissional
- `GET|POST /api/v1/clinicas/{clinicaId}/profissionais/{profissionalId}/escalas`
- `GET|PUT /api/v1/clinicas/{clinicaId}/profissionais/{profissionalId}/escalas/{id}`
- `PATCH .../escalas/{id}/inativar` e `.../reativar`

### Habilitação de profissional para um serviço
- `GET|POST /api/v1/clinicas/{clinicaId}/profissionais/{profissionalId}/servicos`
- `DELETE /api/v1/clinicas/{clinicaId}/profissionais/{profissionalId}/servicos/{servicoId}` (desabilita)

### Agendamentos
- `GET /api/v1/clinicas/{clinicaId}/agendamentos` (aceita `?profissionalId=`)
- `GET /api/v1/clinicas/{clinicaId}/agendamentos/{id}`
- `POST /api/v1/clinicas/{clinicaId}/agendamentos`
- `PATCH /api/v1/clinicas/{clinicaId}/agendamentos/{id}/confirmar` (PENDENTE → CONFIRMADO)
- `PATCH /api/v1/clinicas/{clinicaId}/agendamentos/{id}/concluir` (CONFIRMADO → CONCLUIDO)
- `PATCH /api/v1/clinicas/{clinicaId}/agendamentos/{id}/cancelar` (a partir de PENDENTE ou CONFIRMADO)

Exemplo de corpo do `POST` de agendamento:

```json
{"petId": 1, "servicoId": 1, "profissionalId": 1, "dataHora": "2026-09-02T10:00:00", "observacoes": "Primeira consulta"}
```

O serviço valida tenant, pet, serviço ativo, profissional habilitado, escala e sobreposição de horários.
