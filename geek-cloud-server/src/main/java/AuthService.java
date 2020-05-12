
import java.sql.*;

public class AuthService {
    private int PORT = 8989;
    private static Connection connection;
    private static Statement stmt;
    String url = "jdbc:MySQL://localhost:3306/main?Unicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode-fasle&serverTimezone=Europe/Moscow";
    String userName = "root";
    String password = "x545ee199";


    public void setConnection(){
        connect();
    }
    private void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection(url,userName,password);
            System.out.println("connection succesfull");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("connection failed");
        }
    }
    public static String getNicknameByLoginAndPass(String login, String password){
        String sql = String.format("SELECT nickname FROM users WHERE login = '%s' and password = '%s'",login,password);
        try {
            ResultSet resultSet = stmt.executeQuery(sql);
            if (resultSet.next()){
                System.out.println("Access is Allowed");
                return resultSet.getString("nickname");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Access Denied ");
        return null;
    }



}
