package jop_shop.dao;

import jop_shop.model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDao extends BaseDao {
	/**
	 * Add a department
	 * @param departmentData
	 * @return true if success, otherwise false
	 */
	public boolean add(String departmentData) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			StringBuilder sql = new StringBuilder("INSERT INTO department(department_data) VALUES (?)");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, departmentData);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return false;
	}

	/**
	 * Find all departments
	 * @return List departments
	 */
	public List<Department> findAll() {
		Connection connection = null;
		PreparedStatement ps = null;
		List<Department> departmentList = new ArrayList<>();
		try {
			connection = getConnection();
			StringBuilder sql = new StringBuilder("SELECT * FROM department");
			ps = connection.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int departmentId = rs.getInt("department_id");
				String departmentData = rs.getString("department_data");
				Department department = new Department(departmentId, departmentData);
				departmentList.add(department);
			}
			return departmentList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return departmentList;
	}
}
