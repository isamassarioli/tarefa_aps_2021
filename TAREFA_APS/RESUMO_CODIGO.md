# Arquivo consolidado

Este conteúdo foi consolidado no `README.md` principal. Abra `README.md` para a documentação atualizada.

ANTES:
`java
public class Turma {
    private String horario;
    private int limiteAlunos;
    private boolean fechada;
    private Date dataInicio;
    private Date dataFim;
    private Curso curso;
    private Professor professor;
    
    // Construtores
    // Getters e Setters
}
`

DEPOIS:
`java
public class Turma {
    private String horario;
    private int limiteAlunos;
    private boolean fechada;
    private Date dataInicio;
    private Date dataFim;
    private Curso curso;
    private Professor professor;
    private List<Aluno> alunos;  //  NOVO
    
    public Turma(...) {
        // ... código existente
        this.alunos = new ArrayList<>();  //  NOVO
    }
    
    //  NOVOS MÉTODOS:
    public List<Aluno> getAlunos() {
        return alunos;
    }
    
    public void adicionarAluno(Aluno aluno) {
        if (alunos.size() < limiteAlunos && !alunos.contains(aluno)) {
            alunos.add(aluno);
        }
    }
    
    public void removerAluno(Aluno aluno) {
        alunos.remove(aluno);
    }
}
`

---

## 2 TURMADAO.JAVA - NOVOS MÉTODOS

ADICIONADOS:

`java
//  NOVO MÉTODO 1
public int adicionarAlunoTurma(int turmaId, long alunoCpf) {
    int cod = -1;
    sql = "INSERT INTO turma_alunos (turma_id, aluno_cpf) VALUES (?, ?)";
    try {
        pstmt = conexao.prepareStatement(sql);
        pstmt.setInt(1, turmaId);
        pstmt.setLong(2, alunoCpf);
        cod = pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Erro ao adicionar aluno à turma: " + e.getMessage());
    } finally {
        if (pstmt != null) {
            try { pstmt.close(); } catch (SQLException e) {}
        }
    }
    return cod;
}

//  NOVO MÉTODO 2
public int removerAlunoTurma(int turmaId, long alunoCpf) {
    int cod = -1;
    sql = "DELETE FROM turma_alunos WHERE turma_id = ? AND aluno_cpf = ?";
    try {
        pstmt = conexao.prepareStatement(sql);
        pstmt.setInt(1, turmaId);
        pstmt.setLong(2, alunoCpf);
        cod = pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Erro ao remover aluno da turma: " + e.getMessage());
    } finally {
        if (pstmt != null) {
            try { pstmt.close(); } catch (SQLException e) {}
        }
    }
    return cod;
}

//  NOVO MÉTODO 3
public List<Aluno> listarAlunosTurma(int turmaId) {
    List<Aluno> alunos = new ArrayList<>();
    sql = "SELECT p.cpf, p.nome, p.data_nascimento FROM turma_alunos ta " +
          "JOIN aluno a ON ta.aluno_cpf = a.cpf " +
          "JOIN pessoa p ON a.cpf = p.cpf WHERE ta.turma_id = ?";
    try {
        pstmt = conexao.prepareStatement(sql);
        pstmt.setInt(1, turmaId);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            Aluno aluno = new Aluno(
                rs.getString("nome"),
                rs.getDate("data_nascimento"),
                rs.getLong("cpf")
            );
            alunos.add(aluno);
        }
    } catch (SQLException e) {
        System.out.println("Erro ao listar alunos da turma: " + e.getMessage());
    } finally {
        if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
        if (pstmt != null) { try { pstmt.close(); } catch (SQLException e) {} }
    }
    return alunos;
}
`

---

## 3 MEU_EXEMPLO.SQL - MUDANÇAS

REMOVIDO:
`sql
--  TABELA DELETADA
DROP TABLE IF EXISTS matricula;
`

ADICIONADO:
`sql
--  NOVA TABELA
CREATE TABLE turma_alunos (
    turma_id INT NOT NULL,
    aluno_cpf BIGINT NOT NULL,
    PRIMARY KEY (turma_id, aluno_cpf),
    FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE CASCADE,
    FOREIGN KEY (aluno_cpf) REFERENCES aluno(cpf) ON DELETE CASCADE
);

--  ÍNDICES PARA PERFORMANCE
CREATE INDEX idx_turma_alunos_turma ON turma_alunos(turma_id);
CREATE INDEX idx_turma_alunos_aluno ON turma_alunos(aluno_cpf);
`

---

## 4 ARQUIVOS DELETADOS 

`
Matricula.java 
MatriculaDAO.java 
`

---

## 5 OUTROS ARQUIVOS

 REVISADOS E INTACTOS:
- ControladorPrincipal.java
- JanPrincipal.java
- AplGerenciarCurso.java
- AplGerenciarPessoas.java
- Todas as outras classes

---

##  IMPACTO NO CÓDIGO

Adições:
- 2 imports (ArrayList, List)
- 1 novo atributo
- 3 novos métodos em Turma
- 3 novos métodos em TurmaDAO
Total: ~100 linhas

Remoções:
- 1 classe completa (Matricula)
- 1 DAO completo (MatriculaDAO)
Total: ~200 linhas

Net: -100 linhas (simplificação!)

---

PRONTO PARA TESTAR! 
