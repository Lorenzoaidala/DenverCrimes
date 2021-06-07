package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getVertici(String category_id, int mese) {
		String sql ="SELECT DISTINCT offense_type_id "
				+ "FROM EVENTS "
				+ "WHERE offense_category_id = ? "
				+ "AND MONTH(reported_date) = ?";
		List<String> result = new LinkedList<String>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, category_id);
			st.setInt(2, mese);
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				result.add(rs.getString("offense_type_id"));
			}
			st.close();
			rs.close();
			conn.close();
			return result;
			
		} catch(SQLException e) {
			throw new RuntimeException("Error Connection Database");
		}
	}
		
		public List<Adiacenza> getArchi(String category_id, int mese) {
			String sql ="SELECT e1.offense_type_id as id1, e2.offense_type_id as id2, COUNT(DISTINCT(e1.neighborhood_id)) AS peso "
					+ "FROM events e1, events e2 "
					+ "WHERE  e1.offense_category_id=? AND e1.offense_category_id=e2.offense_category_id "
					+ "AND e1.offense_type_id>e2.offense_type_id "
					+ "AND MONTH(e1.reported_date)=? AND MONTH(e1.reported_date)=MONTH(e2.reported_date) "
					+ "AND e1.neighborhood_id=e2.neighborhood_id "
					+ "GROUP BY e1.offense_type_id, e2.offense_type_id";
			List<Adiacenza> result = new LinkedList<Adiacenza>();
			
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, category_id);
				st.setInt(2, mese);
				ResultSet rs = st.executeQuery();
				
				while(rs.next()) {
					
						result.add(new Adiacenza(rs.getString("id1"), rs.getString("id2"), rs.getInt("peso")));
					
				}
					st.close();
					rs.close();
					conn.close();
				
				return result;
			} catch(SQLException e) {
				throw new RuntimeException("Error Connection Database");
			}
		}
	

}
