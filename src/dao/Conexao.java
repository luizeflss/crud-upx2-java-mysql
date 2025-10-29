package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/smartvan?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "uPx2@097381*";
    
    public static Connection getConecction(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e){
            throw new RuntimeException ("Driver JDBC não encontrado: " + e.getMessage());
        }catch (SQLException e){
            throw new RuntimeException("Erro na conexão: " + e.getMessage());
        }
    }
}
