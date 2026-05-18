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
            Turma turma = em.find(Turma.class, (long) turmaId);
            Aluno aluno = em.find(Aluno.class, alunoCpf);
            if (turma == null || aluno == null) {
                em.getTransaction().rollback();
                return 0;
            }
            turma.getAlunos().add(aluno);
            em.merge(turma);
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
            Turma turma = em.find(Turma.class, (long) turmaId);
            Aluno aluno = em.find(Aluno.class, alunoCpf);
            if (turma == null || aluno == null) {
                em.getTransaction().rollback();
                return 0;
            }
            turma.getAlunos().remove(aluno);
            em.merge(turma);
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
            Turma turma = em.find(Turma.class, (long) turmaId);
            if (turma == null) return new java.util.ArrayList<>();
            // força inicialização da coleção, caso LAZY
            turma.getAlunos().size();
            return turma.getAlunos();
        } finally {
            em.close();
        }
    }
}
