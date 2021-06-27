package br.com.senac.controle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import org.hibernate.*;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

import br.com.senac.dao.*;
import br.com.senac.entidade.Usuario;
import util.UtilGerador;

@ManagedBean(name = "usuarioC")
@ViewScoped
public class UsuarioControle {

	private Usuario usuario;
	private UsuarioDao usuarioDao;
	private Session sessao;
	private DataModel<Usuario> modelUsuarios;
	private int aba;

	public UsuarioControle() {
		usuarioDao = new UsuarioDaoImpl();
	}

	
	public void salvar() {
		sessao = HibernateUtil.abrirSessao();
		try {
			usuarioDao.salvarOuAlterar(usuario, sessao);
			usuario = null;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário Salvo com Sucesso", null));
			modelUsuarios = null;
		} catch (HibernateException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informação", "Erro ao salvar usuário"));
		} finally {
			sessao.close();
		}
	}

	public void excluir() {
		usuario = modelUsuarios.getRowData();
		sessao = HibernateUtil.abrirSessao();
		try {
			usuarioDao.excluir(usuario, sessao);
			usuario = null;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário excluido com Sucesso", null));
			modelUsuarios = null;
		} catch (HibernateException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir Usuário", ""));
		} finally {
			sessao.close();
		}
	}

	public void prepararAlterar() {
		usuario = modelUsuarios.getRowData();
		aba = 1;
	}
	
	public void onTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Novo"));
		
	}

	public void onTabClose(TabCloseEvent event) {
	}

	public Usuario getUsuario() {
		if (usuario == null) {
			usuario = new Usuario();
		}
		return usuario;
	}

	public void setUsuario(Usuario profissao) {
		this.usuario = profissao;
	}

	public DataModel<Usuario> getModelUsuarios() {
		return modelUsuarios;
	}

	public int getAba() {
		return aba;
	}

}
