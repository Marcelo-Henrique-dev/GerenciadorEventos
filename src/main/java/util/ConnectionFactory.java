package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    @SuppressWarnings("finally")
    public static Connection createConnection(){
        Connection conexao = null;
        try{
            String url = "jdbc:mysql://localhost:3306/eventos_bd?useTimezone=true&serverTimezone=UTC";
            String user = "root";
            String pass = "125896";
            
            conexao = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e){
            System.out.printf("Erro na conexão:",e.getMessage());
        } finally {
            return conexao;
        }
    }
}
