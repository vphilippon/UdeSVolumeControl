package server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import message.PostNewUserReply;
import message.PostNewUserRequest;

public class PostNewUserHandler extends DBHandler {
	private static final String INSERT_STMT = "INSERT INTO \"User\" (\"userId\") VALUES ('%1$s');";

	public PostNewUserReply handle(PostNewUserRequest request) {
		PostNewUserReply reply = new PostNewUserReply(false);
		Connection conn = null;
		try {
			conn = getConnection();
			Statement statement = conn.createStatement();
			// TODO Sanitize input
			statement.executeUpdate(String.format(INSERT_STMT, request.getUserId()));
			reply.setSuccess(true);
			// Auto-commit will commit
		} catch (SQLException e) {
			// Auto-commit will rollback
			e.printStackTrace();
		}
		finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return reply;
	}
}
