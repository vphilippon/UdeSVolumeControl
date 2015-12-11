package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import message.ExistsUserReply;
import message.ExistsUserRequest;

public class ExistsUserHandler extends DBHandler {
	private static final String SELECT_STMT = 
			"SELECT COUNT(*) AS cnt FROM \"User\" WHERE \"userId\" = ?;";
	
	public ExistsUserReply handle(ExistsUserRequest request) {
		
		ExistsUserReply reply = new ExistsUserReply(false, false);
		
		Connection conn = null;
		try {
			conn = getConnection();
			
			PreparedStatement ps = conn.prepareStatement(SELECT_STMT);
			ps.setString(1, request.getUserId());
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next() && rs.getInt("cnt") > 0) {
				reply.setExists(true);
			}

			reply.setSuccess(true);
		} catch (SQLException e) {
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
