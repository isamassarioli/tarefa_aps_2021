package cgd;

import cdp.Professor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO {
    private Connection conexao;
    private PreparedStatement pstmt;
    private String sql;
    private Statement comando;
    private ResultSet rs;

    public ProfessorDAO() {
        Conexao driver = new Conexao("meu_exemplo", "postgres", "postgre");
        conexao = driver.getConnection();
    }

    public int save(Professor professor) {
        int cod = -1;
        sql = "INSERT INTO pessoa (cpf, nome, data_nascimento) VALUES (?, ?, ?)";
        try {
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, professor.getCPF());
            pstmt.setString(2, professor.getNome());
            java.sql.Date sqlDate = new java.sql.Date(professor.getDataNascimento().getTime());
            pstmt.setDate(3, sqlDate);
            cod = pstmt.executeUpdate();

            // Inserir na tabela professor
            if (cod > 0) {
                sql = "INSERT INTO professor (cpf, titulacao) VALUES (?, ?)";
                pstmt = conexao.prepareStatement(sql);
                pstmt.setLong(1, professor.getCPF());
                pstmt.setString(2, professor.getTitulacao());
                cod = pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao salvar professor: " + e.getMessage());
            cod = -1;
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

    public Professor get(long cpf) {
        Professor professor = null;
        sql = "SELECT p.cpf, p.nome, p.data_nascimento, pr.titulacao FROM pessoa p " +
              "JOIN professor pr ON p.cpf = pr.cpf WHERE p.cpf = ?";
        try {
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, cpf);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                java.sql.Date sqlDate = rs.getDate("data_nascimento");
                java.util.Date dataAtual = new java.util.Date(sqlDate.getTime());
                String titulacao = rs.getString("titulacao");
                professor = new Professor(nome, dataAtual, cpf, titulacao);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar professor: " + e.getMessage());
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
        return professor;
    }

    public List<Professor> getAll() {
        List<Professor> professores = new ArrayList<>();
        sql = "SELECT p.cpf, p.nome, p.data_nascimento, pr.titulacao FROM pessoa p " +
              "JOIN professor pr ON p.cpf = pr.cpf";
        try {
            comando = conexao.createStatement();
            rs = comando.executeQuery(sql);
            while (rs.next()) {
                long cpf = rs.getLong("cpf");
                String nome = rs.getString("nome");
                java.sql.Date sqlDate = rs.getDate("data_nascimento");
                java.util.Date dataAtual = new java.util.Date(sqlDate.getTime());
                String titulacao = rs.getString("titulacao");
                professores.add(new Professor(nome, dataAtual, cpf, titulacao));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar professores: " + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar ResultSet: " + e.getMessage());
                }
            }
            if (comando != null) {
                try {
                    comando.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar Statement: " + e.getMessage());
                }
            }
        }
        return professores;
    }

    public int update(Professor professor) {
        int cod = -1;
        sql = "UPDATE pessoa SET nome = ?, data_nascimento = ? WHERE cpf = ?";
        try {
            pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, professor.getNome());
            java.sql.Date sqlDate = new java.sql.Date(professor.getDataNascimento().getTime());
            pstmt.setDate(2, sqlDate);
            pstmt.setLong(3, professor.getCPF());
            cod = pstmt.executeUpdate();

            if (cod > 0) {
                sql = "UPDATE professor SET titulacao = ? WHERE cpf = ?";
                pstmt = conexao.prepareStatement(sql);
                pstmt.setString(1, professor.getTitulacao());
                pstmt.setLong(2, professor.getCPF());
                cod = pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar professor: " + e.getMessage());
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

    public int delete(long cpf) {
        int cod = -1;
        sql = "DELETE FROM professor WHERE cpf = ?";
        try {
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, cpf);
            cod = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar professor: " + e.getMessage());
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
}
