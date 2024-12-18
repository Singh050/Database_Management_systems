package jop_shop.dao;

import jop_shop.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssemblyDao extends BaseDao {
	/**
	 * Add a assembly
	 * @param assembly
	 * @return true if success, otherwise false
	 */
	public int add(Assembly assembly) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			StringBuilder sql = new StringBuilder("INSERT INTO assembly(date_ordered, assembly_detail, customer_id) VALUES (?,?,?)");
			ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			ps.setDate(1, assembly.getDateOrdered());
			ps.setString(2, assembly.getAssemblyDetail());
			ps.setInt(3, assembly.getCustomerId());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			};
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return 0;
	}

	/**
	 * Find all the assemblies
	 * @return List assemblies
	 */
	public List<Assembly> findAll() {
		Connection connection = null;
		PreparedStatement ps = null;
		List<Assembly> assemblyList = new ArrayList<>();
		try {
			connection = getConnection();
			StringBuilder sql = new StringBuilder("SELECT * FROM assembly");
			ps = connection.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int assemblyId = rs.getInt("assembly_id");
				Date dateOrdered = rs.getDate("date_ordered");
				String assemblyDetail = rs.getString("assembly_detail");
				int customerId = rs.getInt("customer_id");
				Assembly assembly = new Assembly(assemblyId, dateOrdered, assemblyDetail, customerId);
				assemblyList.add(assembly);
			}
			return assemblyList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return assemblyList;
	}
}
