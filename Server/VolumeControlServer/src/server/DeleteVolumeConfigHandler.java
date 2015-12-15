package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import message.DeleteVolumeConfigReply;
import message.DeleteVolumeConfigRequest;

public class DeleteVolumeConfigHandler extends DBHandler {
	private static final String DELETE_STMT = 
			"DELETE FROM  \"VolumeConfig\" WHERE \"id\" = ?;";
	
	public DeleteVolumeConfigReply handle(DeleteVolumeConfigRequest request) {
		
		DeleteVolumeConfigReply reply = new DeleteVolumeConfigReply(false);
		
		Connection conn = null;
		try {
			conn = getConnection();
			
			PreparedStatement ps = conn.prepareStatement(DELETE_STMT);
			ps.setInt(1, request.getVolumeConfigId());
			ps.executeUpdate();

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
