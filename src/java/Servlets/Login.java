/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Utiles.ConexionDB;
import Utiles.PasswordService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ernesto
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    private String hash;
    private HttpSession ses;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            String usuario = request.getParameter("txtUsuario");
            String pwd = request.getParameter("txtPassword");
            ConexionDB laConexion = new ConexionDB();
            List[] resultado = null;
            String qry = "";
            String[] columnas = new String[2];
            out.println("<title>Procesando Login...</title>");
            out.println("</head>");
            out.println("<body>");
            if (usuario != null && pwd != null) {
                laConexion.open();
                try {
                    hash = PasswordService.getInstance().encrypt(pwd);
                } catch (Exception ex) {


                    System.out.println(ex);
                }
                // out.println(hashito);
                columnas[0] = "idUsuario";
                columnas[1] = "perfil";
                qry = "SELECT idUsuario,perfil FROM usuario WHERE nickname =  " + laConexion.comillas(usuario) + " AND password =" + laConexion.comillas(hash);
                resultado = laConexion.ejecutaQuerySelect(qry, columnas);
                if (resultado[0].size() > 0 && !resultado[0].get(0).equals("")) {
                    ses = request.getSession(true);
                    //ses.setAttribute("privilegios", resultado[0].get(0));
                    ses.setAttribute("idUsuario", resultado[0].get(0));
                    ses.setAttribute("ip", resultado[1].get(0));
                    //response.sendRedirect("HelpDesk");
                    qry = "UPDATE usuario SET sesId = " + laConexion.comillas(ses.getId())
                            + ", ip= " + laConexion.comillas(PasswordService.getInstance().encrypt(request.getRemoteAddr().toString()))
                            + " WHERE idUsuario = " + ses.getAttribute("idUsuario").toString();
                    laConexion.ejecutaQuery(qry);
                    out.println("Login válido :) <br/>");
                    if ("1".equals(resultado[1].get(0).toString())) {
                        out.println("debo redirigir a panel usuario");

                    } else if ("2".equals(resultado[1].get(0).toString())) {
                        out.println("debo redirigir a panel administrativo");
                    }

                } else {
                    response.sendRedirect("index.xhtml");
                }

                laConexion.close();
            } else {
                response.sendRedirect("index.xhtml");
            }
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
