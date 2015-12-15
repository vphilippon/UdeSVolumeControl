package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DBHandler {
	private static final String HOST = "localhost";
	private static final String PORT = "5432";
	private static final String DBNAME = "IFT604_Project";
	private static final String USERNAME = "ift604";
	private static final String PASSWD = "volume";

	protected Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
				String.format("jdbc:postgresql://%1$s:%2$s/%3$s", HOST, PORT, DBNAME), USERNAME, PASSWD);
	}

}
