package br.com.senac.dao;

import java.util.List;
import org.hibernate.*;
import br.com.senac.entidade.Usuario;

public interface UsuarioDao extends BaseDao<Usuario, Long> {

	List<Usuario> pesquisarPorNome(String nome, Session sessao) throws HibernateException;
	
	List<Usuario> pesquisarPorLogin(String login, Session sessao) throws HibernateException;
	
	
}
