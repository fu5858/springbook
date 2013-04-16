package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class JdbcContext {

	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = dataSource.getConnection();
			
			pstmt = stmt.makePreparedStatement(conn);
			
			pstmt.executeUpdate();
		}catch(SQLException e){
			throw e;
		}finally{
			if(pstmt != null){
				try{ pstmt.close(); }catch(SQLException e){}
			}
			if(conn != null){
				try{ conn.close(); }catch(SQLException e){}
			}
		}
	}
	
	public void workWithStatementStrategy(StatementStrategy stmt, String... args) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = dataSource.getConnection();
			
			pstmt = stmt.makePreparedStatement(conn);
			int index = 1;
			for(String name : args){
				pstmt.setString(index, name);
				index++;
			}
			
			pstmt.executeUpdate();
		}catch(SQLException e){
			throw e;
		}finally{
			if(pstmt != null){
				try{ pstmt.close(); }catch(SQLException e){}
			}
			if(conn != null){
				try{ conn.close(); }catch(SQLException e){}
			}
		}
	}
	
	public void executeSQL(final String query) throws SQLException {
		workWithStatementStrategy(
			new StatementStrategy() {
				
				public PreparedStatement makePreparedStatement(Connection conn)
						throws SQLException {
					return conn.prepareStatement(query);
				}
			}
		);
	}
	
	public void executeSQL(final String query, final String... args) throws SQLException {
		workWithStatementStrategy(
			new StatementStrategy() {
				
				public PreparedStatement makePreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement pstmt = conn.prepareStatement(query);
					int index = 1;
					for(String name : args){
						pstmt.setString(index, name);
						index++;
					}
					return pstmt;
				}
			}
		);
	}
	
}
