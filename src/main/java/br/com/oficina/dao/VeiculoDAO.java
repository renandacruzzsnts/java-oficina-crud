package br.com.oficina.dao;

import br.com.oficina.model.Veiculo;
import br.com.oficina.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class VeiculoDAO {

    public void salvar(Veiculo veiculo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(veiculo);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Veiculo atualizar(Veiculo veiculo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Veiculo merged = em.merge(veiculo);
            em.getTransaction().commit();
            return merged;
        } finally {
            em.close();
        }
    }

    public Veiculo buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Veiculo.class, id);
        } finally {
            em.close();
        }
    }

    public List<Veiculo> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Veiculo> q = em.createQuery("SELECT v FROM Veiculo v", Veiculo.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public void excluir(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Veiculo v = em.find(Veiculo.class, id);
            if (v != null) {
                em.remove(v);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Veiculo> listarPorClienteId(Long clienteId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Veiculo> q = em.createQuery("SELECT v FROM Veiculo v WHERE v.cliente.id = :cid", Veiculo.class);
            q.setParameter("cid", clienteId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
