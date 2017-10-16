package util.jdbc;

/**
 *
 * @author a1364987
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco{
	String driverClassName = "com.mysql.jdbc.Driver";
	//String connectionUrl = "jdbc:postgresql://200.134.10.32:5432/1364987_LucasF";String dbUser = "m0n0p0ly";String dbPwd = "#n0m0n3y#";
	String connectionUrl = "jdbc:postgresql://localhost:5432/wikipedia";
	String dbUser = "postgres";
	String dbPwd = "postgres";
	

	private static ConexaoBanco conexaoBanco = null;

	private ConexaoBanco() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws SQLException {
		Connection conn = null;
		conn = DriverManager.getConnection(connectionUrl, dbUser, dbPwd);
		return conn;
	}

	public static ConexaoBanco getInstance() {
		if (conexaoBanco == null) {
			conexaoBanco = new ConexaoBanco();
		}
		return conexaoBanco;
	}
}