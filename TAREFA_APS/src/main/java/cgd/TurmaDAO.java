package cgd;

import cdp.Turma;
import cdp.Curso;
import cdp.Professor;
import cdp.Aluno;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TurmaDAO {
    private Connection conexao;
    private PreparedStatement pstmt;
    private String sql;
    private Statement comando;
    private ResultSet rs;

    public TurmaDAO() {
        Conexao driver = new Conexao("meu_exemplo", "postgres", "isadora");
        conexao = driver.getConnection();
    }

    public int save(Turma turma) {
        int cod = -1;
        sql = "INSERT INTO turma (horario, limite_alunos, fechada, data_inicio, data_fim, curso_id, professor_cpf) " +
              "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, turma.getHorario());
            pstmt.setInt(2, turma.getLimiteAlunos());
            pstmt.setBoolean(3, turma.isFechada());
            pstmt.setDate(4, new java.sql.Date(turma.getDataInicio().getTime()));
            pstmt.setDate(5, new java.sql.Date(turma.getDataFim().getTime()));
            pstmt.setInt(6, turma.getCurso().getId());
            pstmt.setLong(7, turma.getProfessor().getCPF());
            cod = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao salvar turma: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar PreparedStatement: " + e.getMessage());
                }
            }
        }
        return cod;
    }

    public Turma get(int id) {
        Turma turma = null;
        sql = "SELECT t.*, c.nome as curso_nome, c.carga_horaria, p.nome as prof_nome, " +
              "p.data_nascimento, p.cpf, pr.titulacao FROM turma t " +
              "JOIN curso c ON t.curso_id = c.id " +
              "JOIN professor pr ON t.professor_cpf = pr.cpf " +
              "JOIN pessoa p ON pr.cpf = p.cpf WHERE t.id = ?";
        try {
            pstmt = conexao.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                String horario = rs.getString("horario");
                int limite = rs.getInt("limite_alunos");
                boolean fechada = rs.getBoolean("fechada");
                java.sql.Date dataInicio = rs.getDate("data_inicio");
                java.sql.Date dataFim = rs.getDate("data_fim");

                Curso curso = new Curso(rs.getInt("curso_id"), rs.getString("curso_nome"), rs.getInt("carga_horaria"));
                
                java.util.Date dataNascimento = new java.util.Date(rs.getDate("data_nascimento").getTime());
                Professor professor = new Professor(rs.getString("prof_nome"), dataNascimento, rs.getLong("cpf"), rs.getString("titulacao"));

                turma = new Turma(horario, limite, fechada, new java.util.Date(dataInicio.getTime()), new java.util.Date(dataFim.getTime()), curso, professor);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar turma: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar PreparedStatement: " + e.getMessage());
                }
            }
        }
        return turma;
    }

    public List<Turma> getAll() {
        List<Turma> turmas = new ArrayList<>();
        sql = "SELECT t.*, c.nome as curso_nome, c.carga_horaria, p.nome as prof_nome, " +
              "p.data_nascimento, p.cpf, pr.titulacao FROM turma t " +
              "JOIN curso c ON t.curso_id = c.id " +
              "JOIN professor pr ON t.professor_cpf = pr.cpf " +
              "JOIN pessoa p ON pr.cpf = p.cpf";
        try {
            comando = conexao.createStatement();
            rs = comando.executeQuery(sql);
            while (rs.next()) {
                String horario = rs.getString("horario");
                int limite = rs.getInt("limite_alunos");
                boolean fechada = rs.getBoolean("fechada");
                java.sql.Date dataInicio = rs.getDate("data_inicio");
                java.sql.Date dataFim = rs.getDate("data_fim");

                Curso curso = new Curso(rs.getInt("curso_id"), rs.getString("curso_nome"), rs.getInt("carga_horaria"));
                
                java.util.Date dataNascimento = new java.util.Date(rs.getDate("data_nascimento").getTime());
                Professor professor = new Professor(rs.getString("prof_nome"), dataNascimento, rs.getLong("cpf"), rs.getString("titulacao"));

                turmas.add(new Turma(horario, limite, fechada, new java.util.Date(dataInicio.getTime()), new java.util.Date(dataFim.getTime()), curso, professor));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar turmas: " + e.getMessage());
        } finally {
            if (comando != null) {
                try {
                    comando.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar Statement: " + e.getMessage());
                }
            }
        }
        return turmas;
    }

    public int update(Turma turma, int id) {
        int cod = -1;
        sql = "UPDATE turma SET horario = ?, limite_alunos = ?, fechada = ?, data_inicio = ?, data_fim = ?, curso_id = ?, professor_cpf = ? WHERE id = ?";
        try {
            pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, turma.getHorario());
            pstmt.setInt(2, turma.getLimiteAlunos());
            pstmt.setBoolean(3, turma.isFechada());
            pstmt.setDate(4, new java.sql.Date(turma.getDataInicio().getTime()));
            pstmt.setDate(5, new java.sql.Date(turma.getDataFim().getTime()));
            pstmt.setInt(6, turma.getCurso().getId());
            pstmt.setLong(7, turma.getProfessor().getCPF());
            pstmt.setInt(8, id);
            cod = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar turma: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar PreparedStatement: " + e.getMessage());
                }
            }
        }
        return cod;
    }

    public int delete(int id) {
        int cod = -1;
        sql = "DELETE FROM turma WHERE id = ?";
        try {
            pstmt = conexao.prepareStatement(sql);
            pstmt.setInt(1, id);
            cod = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar turma: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar PreparedStatement: " + e.getMessage());
                }
            }
        }
        return cod;
    }

    // Métodos para gerenciar alunos na turma
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
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar PreparedStatement: " + e.getMessage());
                }
            }
        }
        return cod;
    }

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
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar PreparedStatement: " + e.getMessage());
                }
            }
        }
        return cod;
    }

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
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar ResultSet: " + e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar PreparedStatement: " + e.getMessage());
                }
            }
        }
        return alunos;
    }
}
