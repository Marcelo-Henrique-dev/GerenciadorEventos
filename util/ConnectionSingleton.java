package util;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionSingleton {
    private static ConnectionSingleton instance;
    public Connection conexao = null;
    private ConnectionSingleton() throws SQLException, InterruptedException{
        String url = "jdbc:mysql://localhost:3306/eventos_bd?useTimezone=true&serverTimezone=UTC";
        String user = "root";
        String pass = "125896";
        this.conexao = DriverManager.getConnection(url, user, pass);
    }

    public static ConnectionSingleton getInstance() throws SQLException, InterruptedException{
        if(instance==null||instance.conexao.isClosed()){
            instance = new ConnectionSingleton();
            System.out.println("Novo Objeto");
        }else{
            System.out.println("Reuso de Conexão");
        }
        return instance;
    }
}
