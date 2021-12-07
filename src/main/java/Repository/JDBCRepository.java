package Repository;

import java.sql.*;

public class JDBCRepository {
    static final String DB_URL = "jdbc:mysql://localhost:3306/university";
    static final String USER = "root";
    static final String PASS = "password31";
    static final String QUERY = "SELECT * FROM Enrolled";

    public static void main(String[] args){
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY)) {

            // Extract data from result set
            while (rs.next()) {
                // Retrieve by column name
                System.out.print("ID: " + rs.getInt("id"));
                System.out.print(", Age: " + rs.getInt("age"));
                System.out.print(", First: " + rs.getString("first"));
                System.out.println(", Last: " + rs.getString("last"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
