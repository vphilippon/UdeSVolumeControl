package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import message.PostNewUserReply;
import message.PostNewUserRequest;

public class PostNewUserHandler extends DBHandler {
	private static final String INSERT_STMT = "INSERT INTO \"User\" VALUES (?);";

	public PostNewUserReply handle(PostNewUserRequest request) {
		PostNewUserReply reply = new PostNewUserReply(false);
		
		Connection conn = null;
		try {
			conn = getConnection();
			
			PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
			ps.setString(1, request.getUserId());
			ps.executeUpdate();
			
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
