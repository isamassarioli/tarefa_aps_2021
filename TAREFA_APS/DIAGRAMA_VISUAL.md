# DIAGRAMA VISUAL — Transformação (ANTES → DEPOIS)

Este arquivo traz uma visualização direta da transformação do modelo (antes: tabela `matricula`; depois: associação N:N `turma_alunos`).

## 1) Visão ASCII - ANTES

```
                      MODELO ANTIGO

                      MODELO DE DADOS
┌─────────────┐
│   Pessoa    │
├─────────────┤
│ nome        │
│ cpf (PK)    │
│ dataNasc    │
└──────┬──────┘
       │
  ┌────▼────┐   ┌────────┐
  │ Aluno   │   │Professor│
  └─────────┘   └─────────┘

    Curso         Turma
  ┌───────┐      ┌────────┐
  │ id    │◄──┐  │ id     │
  │ nome  │   │  │ horario│
  └───────┘   └──┤ curso_id(FK)
                 └────────┘

        MATRICULA (com atributos: nota, data)
      ┌────────────────────────────────┐
      │ aluno_cpf (PK,FK)  |  turma_id │
      │ nota               |  data     │
      └────────────────────────────────┘
```

## 2) Visão ASCII - DEPOIS

```
                      MODELO NOVO

                      MODELO DE DADOS
┌─────────────┐
│   Pessoa    │
├─────────────┤
│ nome        │
│ cpf (PK)    │
│ dataNasc    │
└──────┬──────┘
       │
  ┌────▼────┐   ┌────────┐
  │ Aluno   │   │Professor│
  └─────────┘   └─────────┘

    Curso         Turma
  ┌───────┐      ┌────────┐
  │ id    │◄──┐  │ id     │
  │ nome  │   │  │ horario│
  └───────┘   └──┤ curso_id(FK)
                 └────────┘

        TURMA_ALUNOS (tabela de junção N:N, sem atributos)
      ┌──────────────────────────────┐
      │ turma_id (PK,FK) | aluno_cpf (PK,FK)
      └──────────────────────────────┘
```

## 3) Diagrama PlantUML (embutido)

Se você usa a extensão PlantUML no VS Code, este bloco pode ser renderizado diretamente.

```plantuml
@startuml
skinparam classAttributeIconSize 0
class Pessoa { +String nome\n+String cpf <<PK>>\n+Date dataNascimento }
class Aluno
class Professor { +String titulacao }
class Curso { +Long id <<PK>>\n+String nome\n+int cargaHoraria }
class Turma { +Long id <<PK>>\n+String horario\n+int limiteAlunos\n+boolean fechada\n+Date dataInicio\n+Date dataFim\n+Long curso_id <<FK>>\n+String professor_cpf <<FK>> }
Pessoa <|-- Aluno
Pessoa <|-- Professor
Curso "1" -- "0..*" Turma : possui
Professor "1" -- "0..*" Turma : leciona
Turma "*" -- "*" Aluno : alunos
note "Join table: turma_alunos (turma_id INT FK -> turma.id, aluno_cpf BIGINT FK -> pessoa.cpf)" as N
N .. Turma
N .. Aluno
@enduml
```

## 4) Imagem pronta

Se você deseja ver uma imagem gerada, há um arquivo `diagrama.png` no diretório do projeto — abra-o ou renderize o PlantUML acima.

---

Se quiser, eu insiro este `DIAGRAMA_VISUAL.md` no `README.md` (como seção) em vez de ter um arquivo separado — me diga sua preferência e eu faço a alteração.
