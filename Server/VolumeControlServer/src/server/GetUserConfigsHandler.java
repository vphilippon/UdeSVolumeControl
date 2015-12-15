package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.postgresql.geometric.PGcircle;

import message.GetUserConfigsReply;
import message.GetUserConfigsRequest;
import model.VolumeConfig;

public class GetUserConfigsHandler extends DBHandler{
	private static final String SELECT_STMT = 
			"SELECT \"id\", \"name\", \"zone\", \"profile\" " +
			"FROM \"VolumeConfig\" WHERE \"userId\" = ?;";
	
	public GetUserConfigsReply handle(GetUserConfigsRequest request) {
		
		GetUserConfigsReply reply = new GetUserConfigsReply(false, null);
		
		Connection conn = null;
		try {
			conn = getConnection();
			
			PreparedStatement ps = conn.prepareStatement(SELECT_STMT);
			ps.setString(1, request.getUserId());
			
			ResultSet rs = ps.executeQuery();
			ArrayList<VolumeConfig> volumeConfigs = new ArrayList<>();
			while(rs.next()) {
				Integer confId = rs.getInt("id");
				String confName = rs.getString("name");
				PGcircle zone = (PGcircle)rs.getObject("zone");
				Integer profile = rs.getInt("profile");
				volumeConfigs.add(new VolumeConfig(
						confId, confName, 
						zone.center.x, zone.center.y, (int)zone.radius,
						profile));
			}
			
			reply.setConfigs(volumeConfigs);
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
