 /* To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ernesto Valdés Curiel
 */
public class ConexionDB {

    private String host;
    private String usuarioDB;
    private String passDB;
    private String DB;
    private Connection conDB;

    public ConexionDB() {
        this.host = "localhost";
        this.usuarioDB = "root";
        this.passDB = "10.3.1.107";
        this.DB = "stumbleupon";
    }

    /**
     *
     * @return Regresa el estado de la conexión con el servidor de basde de
     * datos en caso de que la conexión sea exitosa regresa true y en caso
     * contrario false.
     */
    public boolean open() {
        String url = "";
        try {
            url = "jdbc:mysql://" + host + "/" + DB;
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            Connection con = DriverManager.getConnection(url, usuarioDB, passDB);
//System.out.println(" Database connection established to " + url+ ".");

            conDB = con;

            return true;

        } catch (java.sql.SQLException e) {
//System.out.println(" Connection couldn't be established to "+ url);
//throw (e);
            System.out.println(e);
            return false;
        }

    }

    /**
     *
     * @return
     */
    public boolean close() {
        try {
            if (conDB == null) {
                return false;
            } else if (conDB.isValid(0)) {
                conDB.close();
                return true;
            } else {
                return false;
            }

        } catch (java.sql.SQLException e) {
            // System.out.println(e.toString());
            return false;
        }
    }

    private void isActive() {
        try {
            if (!conDB.isValid(0)) {
                open();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param query
     * @return
     */
    public int ejecutaQuery(String query) {
        int count = 0;

        isActive();

        try {
            PreparedStatement ps = conDB.prepareStatement(query);
            ps.execute();
            count = ps.getUpdateCount();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }

        return count;
    }

    /**
     *
     * @param query
     * @param columnas
     * @return
     */
    public List[] ejecutaQuerySelect(String query, String[] columnas) {
        ResultSet rs;
        List<String> columna;
        List[] columns;

        isActive();

        try {
            PreparedStatement ps = conDB.prepareStatement(query);
            rs = ps.executeQuery();
            columns = new List[columnas.length];
            for (int i = 0; i < columnas.length; i++) {
                columna = openResultSet(rs, columnas[i]);
                columns[i] = columna;
            }
            rs.close();
            ps.close();
            return columns;
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private List<String> openResultSet(ResultSet rs, String columna) {
        List<String> result = new ArrayList<String>();
        try {
            while (rs.next()) {
                result.add(rs.getString(columna));
            }
            rs.beforeFirst();
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     *
     * @param str
     * @return
     */
    public String comillas(String str) {
        return '"' + str + '"';
    }
}
