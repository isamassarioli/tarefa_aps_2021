package cgd;

import cdp.Aluno;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
    private Connection conexao;
    private PreparedStatement pstmt;
    private String sql;
    private Statement comando;
    private ResultSet rs;

    public AlunoDAO() {
        Conexao driver = new Conexao("meu_exemplo", "postgres", "postgre");
        conexao = driver.getConnection();
    }

    public int save(Aluno aluno) {
        int cod = -1;
        sql = "INSERT INTO pessoa (cpf, nome, data_nascimento) VALUES (?, ?, ?)";
        try {
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, aluno.getCPF());
            pstmt.setString(2, aluno.getNome());
            java.sql.Date sqlDate = new java.sql.Date(aluno.getDataNascimento().getTime());
            pstmt.setDate(3, sqlDate);
            cod = pstmt.executeUpdate();

            // Inserir na tabela aluno
            if (cod > 0) {
                sql = "INSERT INTO aluno (cpf) VALUES (?)";
                pstmt = conexao.prepareStatement(sql);
                pstmt.setLong(1, aluno.getCPF());
                cod = pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao salvar aluno: " + e.getMessage());
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

    public Aluno get(long cpf) {
        Aluno aluno = null;
        sql = "SELECT p.cpf, p.nome, p.data_nascimento FROM pessoa p JOIN aluno a ON p.cpf = a.cpf WHERE p.cpf = ?";
        try {
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, cpf);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                java.sql.Date sqlDate = rs.getDate("data_nascimento");
                java.util.Date dataAtual = new java.util.Date(sqlDate.getTime());
                aluno = new Aluno(nome, dataAtual, cpf);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar aluno: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar PreparedStatement: " + e.getMessage());
                }
            }
        }
        return aluno;
    }

    public List<Aluno> getAll() {
        List<Aluno> alunos = new ArrayList<>();
        sql = "SELECT p.cpf, p.nome, p.data_nascimento FROM pessoa p JOIN aluno a ON p.cpf = a.cpf";
        try {
            comando = conexao.createStatement();
            rs = comando.executeQuery(sql);
            while (rs.next()) {
                long cpf = rs.getLong("cpf");
                String nome = rs.getString("nome");
                java.sql.Date sqlDate = rs.getDate("data_nascimento");
                java.util.Date dataAtual = new java.util.Date(sqlDate.getTime());
                alunos.add(new Aluno(nome, dataAtual, cpf));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar alunos: " + e.getMessage());
        } finally {
            if (comando != null) {
                try {
                    comando.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar Statement: " + e.getMessage());
                }
            }
        }
        return alunos;
    }

    public int update(Aluno aluno) {
        int cod = -1;
        sql = "UPDATE pessoa SET nome = ?, data_nascimento = ? WHERE cpf = ?";
        try {
            pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, aluno.getNome());
            java.sql.Date sqlDate = new java.sql.Date(aluno.getDataNascimento().getTime());
            pstmt.setDate(2, sqlDate);
            pstmt.setLong(3, aluno.getCPF());
            cod = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar aluno: " + e.getMessage());
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
        sql = "DELETE FROM aluno WHERE cpf = ?";
        try {
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, cpf);
            cod = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar aluno: " + e.getMessage());
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
