package it.polito.tdp.poweroutages.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutage;

public class PowerOutageDAO {

	public List<Nerc> getNercList(Map<Integer, Nerc> nmap) {
		String sql = "SELECT id, value "
				+ "FROM nerc";
		List<Nerc> result = new ArrayList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nmap.put(n.getId(), n);
				result.add(n);
			}
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public List<PowerOutage> getPowerOutagesList(Nerc nerc) {
		String sql = "SELECT * " + 
				"FROM poweroutages p " + 
				"WHERE p.nerc_id = ?";
		List<PowerOutage> result = new ArrayList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, nerc.getId());
			ResultSet res = st.executeQuery();
			while (res.next())
				result.add(new PowerOutage(res.getInt("id"),
						res.getInt("event_type_id"),
						res.getInt("tag_id"),
						res.getInt("area_id"),
						res.getInt("nerc_id"),
						res.getInt("responsible_id"),
						res.getInt("customers_affected"),
						res.getTimestamp("date_event_began").toLocalDateTime(),
						res.getTimestamp("date_event_finished").toLocalDateTime(),
						res.getInt("demand_loss")));
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

}
