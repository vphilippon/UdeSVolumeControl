package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.postgresql.geometric.PGcircle;

import message.GetUserConfigsReply;
import message.GetUserConfigsRequest;
import model.Config;

public class GetUserConfigsHandler extends DBHandler{
	private static final String SELECT_STMT = 
			"SELECT \"configId\", \"configName\", \"zone\", \"volumeRingtone\", \"volumeNotification\" " +
			"FROM \"Config\" WHERE \"userId\" = ?;";
	
	public GetUserConfigsReply handle(GetUserConfigsRequest request) {
		
		GetUserConfigsReply reply = new GetUserConfigsReply(false, null);
		
		Connection conn = null;
		try {
			conn = getConnection();
			
			PreparedStatement ps = conn.prepareStatement(SELECT_STMT);
			ps.setString(1, request.getUserId());
			
			ResultSet rs = ps.executeQuery();
			ArrayList<Config> configs = new ArrayList<>();
			while(rs.next()) {
				Integer confId = rs.getInt("configId");
				String confName = rs.getString("configName");
				PGcircle zone = (PGcircle)rs.getObject("zone");
				Integer volumeRingtone = rs.getInt("volumeRingtone");
				Integer volumeNotification = rs.getInt("volumeNotification");
				configs.add(new Config(
						confId, confName, 
						zone.center.x, zone.center.y, (int)zone.radius,
						volumeRingtone, volumeNotification
						));
			}
			
			reply.setConfigs(configs);
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
