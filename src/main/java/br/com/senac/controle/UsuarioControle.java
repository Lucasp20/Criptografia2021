package br.com.senac.controle;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

	public void salvar() throws NoSuchAlgorithmException {
		sessao = HibernateUtil.abrirSessao();
		String hash = new String(this.getHash(usuario));

		try {
			usuario.setSenha(hash);
			usuarioDao.salvarOuAlterar(usuario, sessao);
			usuario = null;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário Salvo com Sucesso", null));
			modelUsuarios = null;
		} catch (HibernateException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar usuário", null));
		} finally {
			sessao.close();
		}
	}

	private String getHash(Usuario usuario) throws NoSuchAlgorithmException {
		String senha = new String(usuario.getSenha());
		byte[] digest = sha512(senha);
		String hash = hexaToString(digest);
		return hash;
	}

	public static byte[] sha512(String message) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-512"); // SHA (Secure Hash Algorithm)
		md.update(message.getBytes());
		byte[] digest = md.digest();
		return digest;
	}

	public static String hexaToString(byte[] digest) {
		// Convert digest to a string
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			if ((0xff & digest[i]) < 0x10) {
				hexString.append("0" + Integer.toHexString((0xFF & digest[i])));
			} else {
				hexString.append(Integer.toHexString(0xFF & digest[i]));
			}
		}
		return hexString.toString();
	}

	public void prepararAlterar() {
		usuario = modelUsuarios.getRowData();
		aba = 1;
	}

	public void onTabChange(TabChangeEvent event) {
		if (event.getTab().getTitle().equals("Novo"))
			;

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
