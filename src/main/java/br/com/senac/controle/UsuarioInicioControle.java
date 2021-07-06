package br.com.senac.controle;



import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import org.hibernate.Session;
import br.com.senac.dao.UsuarioDao;

import br.com.senac.entidade.Usuario;


@ManagedBean(name = "usuarioInicioC")
@ViewScoped
public class UsuarioInicioControle {
	
	private Usuario usuario;
	private UsuarioDao usuarioDao;
	private Session sessao;
	private int aba;
       

	
	public int getAba() {
		return aba;
	}
}
