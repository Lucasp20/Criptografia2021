package br.com.senac.dao;

import java.io.DataOutput;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EnviaMsgUsuario extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
           
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet EnviaMsgUsuario</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EnviaMsgUsuario at " + request.getContextPath() + "</h1>");
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
       if( request.getSession().getAttribute("login")==null){
            response.setStatus(500);
            response.getOutputStream().print("This funciton requires authentication...");
           return;
       }
       
        
        try {
            String userId=request.getParameter("login");
            String message=request.getParameter("message");
            
            DataSourceMySQL ds = new DataSourceMySQL();
            String sql = "SELECT chave_publica FROM usuario where login=?";
            Connection con = ds.getCon();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.valueOf(userId));
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
                    
                    
                    //store in database
                    long enviada=(long)request.getSession().getAttribute("user_id");
                    long recebida=Integer.valueOf(userId);
                    String content=new String(messageCyphered);
                    
                    ds = new DataSourceMySQL();
                    sql = "insert into messages (`from`,`to`,content) values(?,?,?)";
                    con = ds.getCon();
                    ps = con.prepareStatement(sql);
                    ps.setLong(1, enviada);
                    ps.setLong(2, recebida);
                   
                    ps.setBytes(3, messageCyphered);
                    boolean result = ps.execute();
                    if(result){
                         response.setStatus(200);
                         response.getOutputStream().print("Message sent");
                    }else{
                     
                    }
                    
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(EnviaMsgUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EnviaMsgUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
