package br.com.senac.controle;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.hibernate.Session;

import br.com.senac.dao.HibernateUtil;
import br.com.senac.dao.UsuarioDao.UsuarioDAO;
import br.com.senac.entidade.Usuario;

@ManagedBean(name ="loginC")
@ViewScoped
public class LoginControle {

	private Session sessao;
	private int aba;
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	private Usuario usuario = new Usuario();

	  public String envia() {
		sessao = HibernateUtil.abrirSessao();
	    usuario = usuarioDAO.getUsuario(usuario.getLogin(), usuario.getSenha());
	    if (usuario == null) {
	      usuario = new Usuario();
	      FacesContext.getCurrentInstance().addMessage(
	         null,
	         new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário não encontrado!",
	           "Erro no Login!"));
	      return null;
	    } else {
	          return "/main";
	    }


	  }
	public UsuarioDAO getUsuarioDAO() {
		return usuarioDAO;
	}

	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
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
	public Usuario getUsuario() {
		if (usuario == null) {
			usuario = new Usuario();
		}
		return usuario;
	}

	public void setUsuario(Usuario profissao) {
		this.usuario = profissao;
	}



	public int getAba() {
		return aba;
	}

}
