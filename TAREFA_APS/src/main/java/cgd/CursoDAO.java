package cgd;

import cdp.Curso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CursoDAO {
    public CursoDAO() {
    }

    public int save(Curso curso) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(curso);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Erro ao salvar curso: " + e.getMessage());
            return -1;
        } finally {
            em.close();
        }
    }

    public Curso get(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Curso.class, id);
        } finally {
            em.close();
        }
    }

    public List<Curso> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c", Curso.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public int update(Curso curso) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(curso);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Erro ao atualizar curso: " + e.getMessage());
            return -1;
        } finally {
            em.close();
        }
    }

    public int delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Curso curso = em.find(Curso.class, id);
            if (curso == null) return 0;
            em.getTransaction().begin();
            em.remove(curso);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Erro ao deletar curso: " + e.getMessage());
            return -1;
        } finally {
            em.close();
        }
    }
}

















