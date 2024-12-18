package jop_shop.dao;

import jop_shop.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao extends BaseDao {
	/**
	 * Add a customer
	 * @param name
	 * @param address
	 * @param category
	 * @return true if success, otherwise false
	 */
	public boolean add(String name, String address, int category) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			StringBuilder sql = new StringBuilder("INSERT INTO customer(name, address, category) VALUES (?, ?, ?)");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, name);
			ps.setString(2, address);
			ps.setInt(3, category);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return false;
	}

	/**
	 * Find all customers
	 * @return list customers
	 */
	public List<Customer> findAll() {
		Connection connection = null;
		PreparedStatement ps = null;
		List<Customer> customerList = new ArrayList<>();
		try {
			connection = getConnection();
			StringBuilder sql = new StringBuilder("SELECT * FROM customer");
			ps = connection.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int customerId = rs.getInt("customer_id");
				String name = rs.getString("name");
				String address = rs.getString("address");
				int category = rs.getInt("category");
				Customer customer = new Customer(customerId, name, address, category);
				customerList.add(customer);
			}
			return customerList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return customerList;
	}

	/**
	 * Find all customers by category
	 * @param categoryId1
	 * @param categoryId2
	 * @return List customers
	 */
	public List<Customer> findAllByCategory(int categoryId1, int categoryId2) {
		Connection connection = null;
		PreparedStatement ps = null;
		List<Customer> customerList = new ArrayList<>();
		try {
			connection = getConnection();
			StringBuilder sql = new StringBuilder("SELECT * FROM customer WHERE category BETWEEN ? AND ? ORDER BY name ASC");
			ps = connection.prepareStatement(sql.toString());
			ps.setInt(1, categoryId1);
			ps.setInt(2, categoryId2);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int customerId = rs.getInt("customer_id");
				String name = rs.getString("name");
				String address = rs.getString("address");
				int category = rs.getInt("category");
				Customer customer = new Customer(customerId, name, address, category);
				customerList.add(customer);
			}
			return customerList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return customerList;
	}
}
