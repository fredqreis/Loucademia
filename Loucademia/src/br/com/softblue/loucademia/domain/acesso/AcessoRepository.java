package br.com.softblue.loucademia.domain.acesso;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import br.com.softblue.loucademia.domain.aluno.Aluno;

@Stateless
public class AcessoRepository {

	@PersistenceContext
	private EntityManager em;
	
	public void store(Acesso acesso) {
		this.em.persist(acesso);
	}
	
	public Acesso findUltimoAcesso(Aluno aluno) {
		try {
			return em.createQuery(
					"select a from Acesso a where a.aluno.matricula = :matricula order by a.id desc", Acesso.class)
					.setParameter("matricula", aluno.getMatricula())
					.setMaxResults(1)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
