# INSTRUÇÕES PARA TESTAR A REFATORAÇÃO

## 📋 PRÉ-REQUISITOS

- PostgreSQL instalado e rodando
- Java 21 (ou compatível)
- Maven instalado
- VS Code com suporte a Java

---

## 🔧 PASSO 1: ATUALIZAR BANCO DE DADOS

### 1.1 Conectar ao PostgreSQL
```bash
psql -U postgres
```

### 1.2 Conectar ao banco de dados do projeto
```sql
\c meu_exemplo
```

### 1.3 Verificar tabelas atuais
```sql
\dt
```

### 1.4 Executar script de migração
```sql
-- Remover tabela antiga de matrícula
DROP TABLE IF EXISTS matricula CASCADE;

-- Criar nova tabela de relacionamento
CREATE TABLE turma_alunos (
    turma_id INT NOT NULL,
    aluno_cpf BIGINT NOT NULL,
    PRIMARY KEY (turma_id, aluno_cpf),
    FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE CASCADE,
    FOREIGN KEY (aluno_cpf) REFERENCES aluno(cpf) ON DELETE CASCADE
);

-- Criar índices
CREATE INDEX idx_turma_alunos_turma ON turma_alunos(turma_id);
CREATE INDEX idx_turma_alunos_aluno ON turma_alunos(aluno_cpf);
```

### 1.5 Verificar resultado
```sql
-- Ver estrutura da nova tabela
\d turma_alunos

-- Ver todas as tabelas
\dt
```

---

## 🛠️ PASSO 2: COMPILAR PROJETO

### 2.1 Navegar até o diretório do projeto
```bash
cd "D:\Users\2024122760199\Documents\GitHub\tarefa_aps_2021\TAREFA_APS"
```

### 2.2 Limpar build anterior
```bash
mvn clean
```

### 2.3 Compilar o projeto
```bash
mvn compile
```

**Resultado esperado**: BUILD SUCCESS ✅

---

## 🧪 PASSO 3: TESTAR FUNCIONALIDADES

### 3.1 Iniciar a aplicação
```bash
mvn exec:java -Dexec.mainClass="cci.ControladorPrincipal"
```

### 3.2 Testar Fluxo Completo

#### 3.2.1 Criar um Curso
1. Abrir janela principal
2. Menu → Cadastrar → Novo Curso
3. Preencher:
   - ID: 1
   - Nome: Inglês Básico
   - Carga Horária: 40
4. Clicar em "Salvar"
5. **Verificar**: Mensagem "Curso criado com sucesso"

#### 3.2.2 Criar um Professor
1. Menu → Cadastrar → Novo Professor
2. Preencher:
   - Nome: João Silva
   - Data de Nascimento: 01/01/1980
   - CPF: 12345678901
   - Titulação: Mestrado
3. Clicar em "Salvar"
4. **Verificar**: Mensagem "Professor cadastrado com sucesso"

#### 3.2.3 Criar Alunos
1. Menu → Cadastrar → Novo Aluno
2. Preencher com vários alunos:
   - Nome: Maria Santos | CPF: 11111111111
   - Nome: Pedro Costa | CPF: 22222222222
   - Nome: Ana Silva | CPF: 33333333333
3. **Verificar**: Cada um registrado com sucesso

#### 3.2.4 Criar Turma (COM ALUNOS)
1. Menu → Cadastrar → Nova Turma
2. Preencher:
   - Horário: 14:00-16:00
   - Limite de Alunos: 30
   - Fechada: NÃO (desmarcado)
   - Data Início: 01/06/2026
   - Data Fim: 30/08/2026
3. Selecionar Alunos (novo fluxo):
   - [ ] Maria Santos
   - [ ] Pedro Costa
   - [ ] Ana Silva
4. Clicar em "Salvar"
5. **Verificar**: Mensagem "Turma criada com sucesso"

#### 3.2.5 Listar Alunos da Turma
1. Menu → Consultas → Listar Turmas (se disponível)
2. Selecionar turma criada
3. **Verificar**: Aparecem 3 alunos adicionados
   - Maria Santos
   - Pedro Costa
   - Ana Silva

---

## ✅ VERIFICAÇÕES DE BANCO DE DADOS

### 3.3 Consultar dados diretamente no PostgreSQL

#### Ver alunos em uma turma
```sql
SELECT 
    t.id AS turma_id,
    t.horario,
    p.nome AS aluno_nome,
    p.cpf
FROM turma t
JOIN turma_alunos ta ON t.id = ta.turma_id
JOIN aluno a ON ta.aluno_cpf = a.cpf
JOIN pessoa p ON a.cpf = p.cpf
ORDER BY t.id, p.nome;
```

#### Ver estatísticas
```sql
SELECT 
    COUNT(DISTINCT turma_id) AS total_turmas,
    COUNT(DISTINCT aluno_cpf) AS total_alunos_inscritos,
    AVG(alunos_por_turma) AS media_alunos_por_turma
FROM (
    SELECT turma_id, COUNT(aluno_cpf) AS alunos_por_turma
    FROM turma_alunos
    GROUP BY turma_id
) subquery;
```

#### Verificar se Matrícula foi removida
```sql
-- Isso deve retornar: ERROR (tabela não existe)
SELECT * FROM matricula;
```

**Resultado esperado**: 
```
ERROR: relation "matricula" does not exist
```

---

## 🔍 TROUBLESHOOTING

### Problema: Erro na compilação
```
[ERROR] COMPILATION ERROR
```
**Solução**: 
- Verifique se Maven está instalado: `mvn -v`
- Verifique se Java 21+ está instalado: `java -version`
- Execute: `mvn clean compile` novamente

### Problema: Erro de conexão com banco de dados
```
org.postgresql.util.PSQLException: Connection refused
```
**Solução**:
- Verifique se PostgreSQL está rodando
- Verifique credenciais em `cgd/Conexao.java`
- Verifique se banco de dados `meu_exemplo` existe

### Problema: Tabela turma_alunos não existe
```
ERROR: relation "turma_alunos" does not exist
```
**Solução**:
- Execute o script SQL novamente
- Verifique se o script foi executado na base correta

### Problema: Erro de chave estrangeira
```
ERROR: foreign key constraint violation
```
**Solução**:
- Verifique se Turma existe antes de adicionar Aluno
- Verifique se Aluno existe antes de adicioná-lo à Turma

---

## 📊 RESULTADO ESPERADO

```
✅ Banco de Dados
   - Tabela turma_alunos criada
   - Tabela matricula removida
   - Índices criados

✅ Compilação
   - BUILD SUCCESS
   - Sem erros de compilação
   - Sem referências a Matrícula

✅ Funcionalidades
   - Criar Cursos
   - Criar Turmas
   - Criar Alunos
   - Adicionar Alunos a Turma
   - Listar Alunos por Turma
   - Remover Alunos de Turma

✅ Integridade
   - Sem erros de chave estrangeira
   - Relacionamentos funcionando
   - Cascata de delete funcionando
```

---

## 📞 SUPORTE

Se encontrar problemas:

1. Verifique o arquivo `MUDANCAS_REALIZADAS.md`
2. Verifique o arquivo `RESUMO_EXECUTIVO.md`
3. Revise os logs de erro
4. Consulte a documentação do PostgreSQL

---

**Última atualização**: 6 de maio de 2026
