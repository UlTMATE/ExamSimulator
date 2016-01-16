
/**
 *
 * @author UlTMATE
 */
import java.sql.*;
import java.util.*;

public class Database {

    static ResultSet rst;

    public Database() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/?", "root", "root");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS examSimulator");
            stmt.close();
            conn.close();
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/examSimulator", "root", "root");
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS questions(Qno int AUTO_INCREMENT, question varchar(100), A varchar(50), B varchar(50), "
                    + "C varchar(50), D varchar(50), answer varchar(10), multiple varchar(10), CHECK (multiple in('true','false')), PRIMARY KEY (QNo))");
            stmt.close();
            stmt = conn.createStatement();
            rst = stmt.executeQuery("Select * from questions");
            if (!rst.next()) {
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO questions(question, A, B, C, D, answer, multiple) values(?,?,?,?,?,?,?)");
                for (int i = 1; i <= 100; i++) {
                    pstmt.setString(1, "Answer of question " + i + " is ?");
                    pstmt.setString(2, "Choose Me");
                    pstmt.setString(3, "What About Me");
                    pstmt.setString(4, "Don't Leave Me");
                    pstmt.setString(5, "Not Selecting Me?");
                    if (i % 2 == 0) {
                        pstmt.setString(6, "A C");
                        pstmt.setString(7, "true");
                    } else {
                        pstmt.setString(6, "D");
                        pstmt.setString(7, "false");
                    }
                    pstmt.executeUpdate();
                }
                pstmt.close();
                pstmt = conn.prepareStatement("UPDATE questions set answer=?, multiple=? where Qno=?");
                Random rand = new Random();
                for (int i = 0; i < 20; i++) {
                    int qNum = rand.nextInt(101);
                    if (qNum != 0) {
                        pstmt.setString(1, "B");
                        pstmt.setString(2, "false");
                        pstmt.setInt(3, qNum);
                        pstmt.executeUpdate();
                    }
                }
                for (int i = 0; i < 20; i++) {
                    int qNum = rand.nextInt(101);
                    if (qNum != 0) {
                        pstmt.setString(1, "B D");
                        pstmt.setString(2, "true");
                        pstmt.setInt(3, qNum);
                        pstmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException excep) {
            excep.printStackTrace();
        }
    }

    public static ResultSet getData(String quesNum) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/examSimulator", "root", "root");
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM questions WHERE QNo=?");
            pstmt.setInt(1, Integer.parseInt(quesNum));
            rst = pstmt.executeQuery();
        } catch (SQLException excep) {
            excep.printStackTrace();
        }
        return rst;
    }
}
