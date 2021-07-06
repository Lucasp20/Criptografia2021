package br.com.senac.dao;

import java.io.IOException;
import java.io.PrintWriter;
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

public class autenticacao extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
           
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet authentication</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet authentication at " + request.getContextPath() + "</h1>");
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
            String senha=request.getParameter("senha");
            long id=-1;
            DataSourceMySQL ds = new DataSourceMySQL();
            String sql = "select login from usuario where login=? and senha=sha2(?,512)";
            Connection con = ds.getCon();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, login);
            ps.setString(2, senha);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                id=rs.getInt("login");
            }       
            if(id==-1){
                response.getOutputStream().print("Error: Credentials not valid.");
                response.setStatus(500);
            }else{
                response.getOutputStream().print("Sucess: Credentials are valid.");
                response.setStatus(200);
                request.getSession().setAttribute("login", login);
            }
        } catch (SQLException ex) {
            Logger.getLogger(autenticacao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
