package br.com.senac.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import java.util.List;
import org.hibernate.*;
import br.com.senac.entidade.Usuario;

public interface UsuarioDao extends BaseDao<Usuario, Long> {

	List<Usuario> pesquisarPorNome(String nome, Session sessao) throws HibernateException;

	List<Usuario> pesquisarPorLogin(String login, Session sessao) throws HibernateException;

	public class UsuarioDAO {

		private EntityManagerFactory factory = Persistence.createEntityManagerFactory("usuarios");
		private EntityManager em = factory.createEntityManager();

	public Usuario getUsuario(String login, String senha) {

      try {
        Usuario usuario = (Usuario) em
         .createQuery(
             "SELECT u from Usuario u where u.nomeUsuario = :name and u.senha = :senha").setParameter("login", login).setParameter("senha", senha).getSingleResult();
        return usuario;
      } catch (NoResultException e) {
            return null;
      }
    }
	}
}