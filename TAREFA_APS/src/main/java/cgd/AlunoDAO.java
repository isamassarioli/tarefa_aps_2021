package cgd;

import cdp.Aluno;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class AlunoDAO {
    public AlunoDAO() {
    }

    public int save(Aluno aluno) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(aluno);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Erro ao salvar aluno: " + e.getMessage());
            return -1;
        } finally {
            em.close();
        }
    }

    public Aluno get(long cpf) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Aluno.class, cpf);
        } finally {
            em.close();
        }
    }

    public List<Aluno> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Aluno> query = em.createQuery("SELECT a FROM Aluno a", Aluno.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public int update(Aluno aluno) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(aluno);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Erro ao atualizar aluno: " + e.getMessage());
            return -1;
        } finally {
            em.close();
        }
    }

    public int delete(long cpf) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Aluno aluno = em.find(Aluno.class, cpf);
            if (aluno == null) return 0;
            em.getTransaction().begin();
            em.remove(aluno);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Erro ao deletar aluno: " + e.getMessage());
            return -1;
        } finally {
            em.close();
        }
    }
}
