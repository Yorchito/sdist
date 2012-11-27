/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Utiles.ConexionDB;
import Utiles.PasswordService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ernesto
 */
@WebServlet(name = "newUser", urlPatterns = {"/newUser"})
public class newUser extends HttpServlet {
    private String hash;

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Registrando nuevo miembro...</title>");            
            out.println("</head>");
            out.println("<body>");
            String nom, apPaterno, apMaterno, email, nickname, password;
            
            nom = request.getParameter("txtNombre").toUpperCase();
            apPaterno = request.getParameter("txtApPaterno").toUpperCase();
            apMaterno = request.getParameter("txtApMaterno").toUpperCase();
            email = request.getParameter("txteMail").toUpperCase();
            nickname = request.getParameter("txtNick");
            password = request.getParameter("txtPassword");
            
            if(!nom.equals("") && !apPaterno.equals("")&& !apMaterno.equals("") && !email.equals("") && !nickname.equals("") && !password.equals("")){
                String qry;
               ConexionDB laConexion = new ConexionDB();
                boolean lngth = false;
            boolean num = false;
            boolean lw = false;
            boolean up = false;
            boolean sp = false;
            char[] specialCh = {'!','@',']','#','$','%','^','&','*'};
            
                
                if(password.length() >=8){
                    lngth = true;
                    for(int i = 0; i<10;i++){
                        if(password.contains(Integer.toString(i))){
                            num = true;
                        }
                    }
                    for(int i=0; i< password.length();i++){
                        if(Character.isLowerCase(password.charAt(i))){
                            lw = true;
                        }else if(Character.isUpperCase(password.charAt(i))){
                            up = true;
                        }
                    }
                    for(int i=0; i< specialCh.length;i++){
                        if(password.contains(String.valueOf(specialCh[i]))){
                            sp = true;
                        }
                    }
                }else{
                    out.println("La longitud de la contraseña es menor a al permitida que es de 8 caracteres  <br />");
                }
                
                try{
                 hash =  PasswordService.getInstance().encrypt(password);
                //out.println(hash);
                }catch(Exception ex){
                    System.out.println(ex);
                }
                
                if(lngth && num && lw && up && sp){
               qry = "INSERT INTO usuario (nombre , apPaterno , apMaterno , email, nickname , password , perfil) VALUES ("+laConexion.comillas(nom) +","+laConexion.comillas(apPaterno) +","+laConexion.comillas(apMaterno) +","+laConexion.comillas(email) +","+laConexion.comillas(nickname)+","+laConexion.comillas(hash) +",1)";
               laConexion.open();
               laConexion.ejecutaQuery(qry);
               laConexion.close();
               response.sendRedirect("index.xhtml?id=regSuccess");
                }else{
                    out.println("La contraseña no cumple con los requermimientos de un número, una letra minúscula, una mayúscula y un caracter especial. Además de que debe tener una longitud mayor o igual a ocho caracteres");
                }
                
            }else{
                out.println("Lo sentimos, tu registro no se puede completar puesto que falta información, haz click <a href=\"newUser.xhtml\">aqui</a> para regresar.");
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
        processRequest(request, response);
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
        processRequest(request, response);
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
