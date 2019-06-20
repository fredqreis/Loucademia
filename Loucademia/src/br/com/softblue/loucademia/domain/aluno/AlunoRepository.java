package br.com.softblue.loucademia.domain.aluno;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.softblue.loucademia.application.util.StringUtils;
import br.com.softblue.loucademia.domain.acesso.Acesso;
import br.com.softblue.loucademia.domain.aluno.Aluno.Situacao;

@Stateless
public class AlunoRepository {
	
	@PersistenceContext
	public EntityManager em;
	
	public void store(Aluno aluno) {
		this.em.persist(aluno);
	}
	
	public void update(Aluno aluno) {
		this.em.merge(aluno);
	}
	
	public void delete(String matricula) {
		Aluno aluno = this.findByMatricula(matricula);
		
		if (aluno != null) {
			this.em.remove(aluno);
		}
	}
	
	public Aluno findByMatricula(String matricula) {
		return this.em.find(Aluno.class, matricula);
	}
	
	public Aluno findByRg(Integer rg) {
		try {
			return this.em.createQuery("select a from Aluno a where a.rg = :rg", Aluno.class)
					.setParameter("rg", rg)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public String getMaxMatriculaAno() {
		return em.createQuery("select max(a.matricula) from Aluno a where a.matricula like :ano", String.class)
				.setParameter("ano", Year.now() + "%")
				.getSingleResult();
	}
	
	public List<Aluno> listAlunos(String matricula, String nome, Integer rg, Integer telefone) {
		StringBuilder jpql = new StringBuilder("select a from Aluno a where ");
		
		if (!StringUtils.isEmpty(matricula)) {
			jpql.append("a.matricula = :matricula and ");
		}
		
		if (!StringUtils.isEmpty(nome)) {
			jpql.append("a.nome like :nome and ");
		}
		
		if (rg != null) {
			jpql.append("a.rg = :rg and ");
		}
		
		if (telefone != null) {
			jpql.append("(a.telefone.numeroCelular like :celular or a.telefone.numeroFixo like :fixo) and ");
		}
		
		jpql.append("1 = 1");
		
		TypedQuery<Aluno> query = em.createQuery(jpql.toString(), Aluno.class);
		
		if (!StringUtils.isEmpty(matricula)) {
			query.setParameter("matricula", matricula);
		}
		
		if (!StringUtils.isEmpty(nome)) {
			query.setParameter("nome", "%" + nome + "%");
		}
		
		if (rg != null) {
			query.setParameter("rg", rg);
		}
		
		if (telefone != null) {
			query.setParameter("celular", telefone);
			query.setParameter("fixo", telefone);
		}
		
		return query.getResultList();
	}
	
	public List<Aluno> listSituacoesAlunos(Situacao situacao) {
		return em.createQuery("select a from Aluno a where a.situacao = :situacao order by a.nome", Aluno.class)
				.setParameter("situacao", situacao)
				.getResultList();
	}
	
	public List<Acesso> listAcessosAlunos(String matricula, LocalDate dataInicial, LocalDate dataFinal) {
		StringBuilder jpql= new StringBuilder("select a from Acesso a where ");
		
		if (!StringUtils.isEmpty(matricula)) { // a de Acesso
			jpql.append("a.aluno.matricula = :matricula and ");
		}
		
		if (dataInicial != null) {
			jpql.append("a.entrada >= :entradaInicio and ");
		}
		
		if (dataFinal != null) {
			jpql.append("a.saida <= :saidaFim and ");
		}
		
		jpql.append("1 = 1 order by a.entrada");
		
		TypedQuery<Acesso> query = em.createQuery(jpql.toString(), Acesso.class);
		
		if (!StringUtils.isEmpty(matricula)) {
			query.setParameter("matricula", matricula);
		}
		
		if (dataInicial != null) {
			LocalDateTime localDateTime = LocalDateTime.of(dataInicial, LocalTime.of(0, 0, 0));
			query.setParameter("entradaInicio", localDateTime);
		}
		
		if (dataFinal != null) {
			LocalDateTime localDateTime = LocalDateTime.of(dataFinal, LocalTime.of(23, 59, 59));
			query.setParameter("saidaFim", localDateTime);
		}
		
		return query.getResultList();
	}
}
