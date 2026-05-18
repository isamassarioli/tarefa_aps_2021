package cgd;

import cdp.Professor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ProfessorDAO {
    public ProfessorDAO() {
    }

    public int save(Professor professor) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(professor);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Erro ao salvar professor: " + e.getMessage());
            return -1;
        } finally {
            em.close();
        }
    }

    public Professor get(long cpf) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Professor.class, cpf);
        } finally {
            em.close();
        }
    }

    public List<Professor> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Professor> query = em.createQuery("SELECT p FROM Professor p", Professor.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public int update(Professor professor) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(professor);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Erro ao atualizar professor: " + e.getMessage());
            return -1;
        } finally {
            em.close();
        }
    }

    public int delete(long cpf) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Professor professor = em.find(Professor.class, cpf);
            if (professor == null) return 0;
            em.getTransaction().begin();
            em.remove(professor);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Erro ao deletar professor: " + e.getMessage());
            return -1;
        } finally {
            em.close();
        }
    }
}
