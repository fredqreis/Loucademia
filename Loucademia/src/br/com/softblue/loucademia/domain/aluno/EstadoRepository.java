package br.com.softblue.loucademia.domain.aluno;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EstadoRepository {

	@PersistenceContext
	private EntityManager em;
	
	public List<Estado> listEstados() {
		return em.createQuery("select e from Estado e order by e.nome", 
				Estado.class).getResultList();
	}
}
