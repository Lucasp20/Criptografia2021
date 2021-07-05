package br.com.senac.controle;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@ManagedBean(name = "usuarioInicioC")
@ViewScoped
public class UsuarioInicioControle {

	private int aba;
	
	public class sendMessageToUser extends HttpServlet {

	    
	    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        response.setContentType("text/html;charset=UTF-8");
	        try ( PrintWriter out = response.getWriter()) {
	            /* TODO output your page here. You may use following sample code. */
	            out.println("<!DOCTYPE html>");
	            out.println("<html>");
	            out.println("<head>");
	            out.println("<title>Servlet sendMessageToUser</title>");            
	            out.println("</head>");
	            out.println("<body>");
	            out.println("<h1>Servlet sendMessageToUser at " + request.getContextPath() + "</h1>");
	            out.println("</body>");
	            out.println("</html>");
	        }
	    }


	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        processRequest(request, response);
	    }


	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	       
	        try {
	            String login=request.getParameter("login");
	            String message=request.getParameter("message");
	            
	            DataSourceMySQL ds = new DataSourceMySQL();
	            String sql = "SELECT chave_publica FROM user where login=?";
	            Connection con = ds.getCon();
	            PreparedStatement ps = con.prepareStatement(sql);
	            ps.setInt(1, Integer.valueOf(login));
	            ResultSet rs = ps.executeQuery();
	            String publicKeyPath=null;
	            while (rs.next()) {
	                publicKeyPath=rs.getString(1);
	            }       
	            if(publicKeyPath!=null){
	                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(publicKeyPath));
	                try {
	                    final PublicKey publicKey = (PublicKey) inputStream.readObject();
	                    byte[] messageCyphered=RSA.encrypt(message.getBytes(), publicKey);
	                    File f= new File("message_cyphered");
	                    FileOutputStream fos= new FileOutputStream(f);
	                    DataOutputStream dos= new DataOutputStream(fos);
	                    dos.write(messageCyphered);
	                    dos.close();
	                    fos.close();
	                    response.getOutputStream().print(f.getAbsolutePath());
	                } catch (ClassNotFoundException ex) {
	                    Logger.getLogger(sendMessageToUser.class.getName()).log(Level.SEVERE, null, ex);
	                }
	            }
	        } catch (SQLException ex) {
	            Logger.getLogger(sendMessageToUser.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }
	}
	public int getAba() {
		return aba;
	}
}
