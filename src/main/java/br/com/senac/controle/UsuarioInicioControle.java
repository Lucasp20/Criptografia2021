package br.com.senac.controle;



import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;


@ManagedBean(name = "usuarioInicioC")
@ViewScoped
public class UsuarioInicioControle {

	private int aba;
	
	public int getAba() {
		return aba;
	}
}
