package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.postgresql.geometric.PGcircle;

import message.PutConfigReply;
import message.PutConfigRequest;
import model.VolumeConfig;

public class PutConfigHandler extends DBHandler {
	private static final String INSERT_STMT = 
			"INSERT INTO \"VolumeConfig\" (\"name\", \"zone\", \"profile\", \"userId\") " +
			"VALUES (?, ?, ?, ?);";
	
	private static final String UPDATE_STMT = 
			"UPDATE \"VolumeConfig\" SET(\"name\", \"zone\", \"profile\") = (?, ?, ?) " +
			"WHERE \"id\" = ?;";
	
	public PutConfigReply handle(PutConfigRequest request) {
		PutConfigReply reply = new PutConfigReply(false);
		
		Connection conn = null;
		try {
			conn = getConnection();
			
			VolumeConfig conf = request.getUpdatedConfig();
			
			// If null, it's new one, else it's an update
			if(conf.getId() == null) {
				PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
				ps.setObject(1, conf.getName());
				PGcircle zone = new PGcircle(conf.getLongitude(), conf.getLattitude(), conf.getRadius());
				ps.setObject(2, zone);
				ps.setInt(3, conf.getProfile());
				ps.setString(4, request.getUserId());
				ps.executeUpdate();
			} else {
				PreparedStatement ps = conn.prepareStatement(UPDATE_STMT);
				ps.setObject(1, conf.getName());
				PGcircle zone = new PGcircle(conf.getLongitude(), conf.getLattitude(), conf.getRadius());
				ps.setObject(2, zone);
				ps.setInt(3, conf.getProfile());
				ps.setInt(4, conf.getId()); // WHERE
				ps.executeUpdate();
			}
			
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
