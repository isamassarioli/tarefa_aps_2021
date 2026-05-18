package cgd;

import cdp.Turma;
import cdp.Aluno;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class TurmaDAO {
    public TurmaDAO() {
    }

    public int save(Turma turma) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(turma);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Erro ao salvar turma: " + e.getMessage());
            return -1;
        } finally {
            em.close();
        }
    }

    public Turma get(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Turma.class, id);
        } finally {
            em.close();
        }
    }

    public List<Turma> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Turma> query = em.createQuery("SELECT t FROM Turma t", Turma.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public int update(Turma turma) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(turma);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Erro ao atualizar turma: " + e.getMessage());
            return -1;
        } finally {
            em.close();
        }
    }

    public int delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Turma turma = em.find(Turma.class, id);
            if (turma == null) return 0;
            em.getTransaction().begin();
            em.remove(turma);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Erro ao deletar turma: " + e.getMessage());
            return -1;
        } finally {
            em.close();
        }
    }

    // Métodos para gerenciar alunos na turma usando native queries na tabela de junção
    public int adicionarAlunoTurma(int turmaId, long alunoCpf) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.createNativeQuery("INSERT INTO turma_alunos (turma_id, aluno_cpf) VALUES (?, ?)")
                    .setParameter(1, turmaId)
                    .setParameter(2, alunoCpf)
                    .executeUpdate();
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Erro ao adicionar aluno à turma: " + e.getMessage());
            return -1;
        } finally {
            em.close();
        }
    }

    public int removerAlunoTurma(int turmaId, long alunoCpf) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.createNativeQuery("DELETE FROM turma_alunos WHERE turma_id = ? AND aluno_cpf = ?")
                    .setParameter(1, turmaId)
                    .setParameter(2, alunoCpf)
                    .executeUpdate();
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Erro ao remover aluno da turma: " + e.getMessage());
            return -1;
        } finally {
            em.close();
        }
    }

    public List<Aluno> listarAlunosTurma(int turmaId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Object[]> rows = em.createNativeQuery(
                    "SELECT ta.aluno_cpf FROM turma_alunos ta WHERE ta.turma_id = ?")
                    .setParameter(1, turmaId)
                    .getResultList();

            List<Aluno> alunos = new java.util.ArrayList<>();
            for (Object row : rows) {
                Number cpfNum = (Number) row;
                long cpf = cpfNum.longValue();
                Aluno aluno = em.find(Aluno.class, cpf);
                if (aluno != null) alunos.add(aluno);
            }
            return alunos;
        } finally {
            em.close();
        }
    }
}
