# TAREFA_APS — Resumo da Refatoração

Este repositório contém uma refatoração do sistema de matrícula onde a tabela/entidade `Matricula` foi removida e substituída por uma associação direta N:N entre `Turma` e `Aluno` via a tabela de junção `turma_alunos`.

## Visão Rápida

- Objetivo: Simplificar o modelo de dados removendo a tabela `matricula` (com atributos) e passando para uma associação direta Turma↔Aluno.
- Principais mudanças:
  - `Turma` agora tem uma lista de `Aluno` (ManyToMany via `turma_alunos`).
  - `Matricula.java` e `MatriculaDAO.java` foram removidos.
  - `TurmaDAO` recebeu métodos para adicionar/remover/listar alunos em uma turma.
  - Mapeamento JPA/Hibernate adicionado às entidades e DAOs migrados para EntityManager.

## Arquivos importantes

- `src/main/java/cdp/` — Entidades: `Pessoa`, `Aluno`, `Professor`, `Curso`, `Turma` (anotadas com JPA).
- `src/main/java/cgd/` — DAOs atualizados: `AlunoDAO`, `ProfessorDAO`, `CursoDAO`, `TurmaDAO`, `JPAUtil`.
- `src/main/resources/META-INF/persistence.xml` — Configuração JPA (Hibernate).
- `src/main/plantuml/transformacao.puml` — Diagrama PlantUML do modelo "DEPOIS".
- `POSTGRES_SETUP.md` — Instruções para criar o banco e executar o script SQL.
- `INSTRUCOES_TESTE.md` — Passos para testar a aplicação localmente.

## Como executar (resumo)

1. Preparar banco de dados (veja `POSTGRES_SETUP.md`).
2. Rodar o script SQL `src/main/java/cgd/meu_exemplo.sql` ou executar os comandos listados em `INSTRUCOES_TESTE.md`.
3. Compilar com Maven:

```powershell
cd "D:\Users\2024122760199\Documents\GitHub\tarefa_aps_2021\TAREFA_APS"
mvn clean compile
```

4. Executar a aplicação (main):

```powershell
mvn exec:java -Dexec.mainClass="cci.ControladorPrincipal"
```

Observação: o projeto agora usa JPA/Hibernate; verifique `persistence.xml` para credenciais e ajuste conforme necessário.

## Diagrama

O diagrama PlantUML está em `src/main/plantuml/transformacao.puml`. Para renderizar localmente use a extensão PlantUML no VS Code ou o jar do PlantUML.

## Diagrama visual (README link)

Adicionei um diagrama mais legível em: `DIAGRAMA_VISUAL.md` — ele contém:

- Visões ASCII "ANTES" e "DEPOIS";
- Bloco PlantUML embutido que a extensão do VS Code pode renderizar;
- Referência à imagem pronta `diagrama.png`.

Abra `DIAGRAMA_VISUAL.md` para uma visualização rápida ou renderize `src/main/plantuml/transformacao.puml` para gerar uma imagem.

## Testes manuais recomendados

- Criar Curso, Professor e Alunos
- Criar Turma e adicionar alunos
- Verificar `turma_alunos` no banco
- Listar alunos por turma na UI

## Changelog resumido

- Matricula: removida
- Nova tabela: `turma_alunos` criada no script SQL
- DAOs migrados para JPA: `AlunoDAO`, `ProfessorDAO`, `CursoDAO`, `TurmaDAO`

---

Se algo importante faltar no README consolidado que você quer manter, diga e eu incluo (por exemplo logs de commits, checklist detalhado ou passos de rollback).
