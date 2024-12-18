package jop_shop.dao;

import jop_shop.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao extends BaseDao {
	/**
	 * Find all accounts
	 * @return List accounts
	 */
	public List<Account> findAll() {
		Connection connection = null;
		PreparedStatement ps = null;
		List<Account> accountList = new ArrayList<>();
		try {
			connection = getConnection();
			StringBuilder sql = new StringBuilder("SELECT a.account_number, a.established_date, a.cost, b.assembly_id as extra1, '0' as type FROM account a JOIN assembly_account b ON a.account_number = b.account_number ");
			sql.append("UNION ALL SELECT a.account_number, a.established_date, a.cost, b.department_id as extra1, '1' as type FROM account a JOIN department_account b ON a.account_number = b.account_number ");
			sql.append("UNION ALL SELECT a.account_number, a.established_date, a.cost, b.process_id as extra1, '2' as type FROM account a JOIN process_account b ON a.account_number = b.account_number ");
			ps = connection.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String accountNumber = rs.getString("account_number");
				Date establishedDate = rs.getDate("established_date");
				double cost = rs.getDouble("cost");
				int extra1 = rs.getInt("extra1");
				String type = rs.getString("type");
				Account account;
				if ("0".equals(type)) {
					account = new AssemblyAccount(accountNumber, establishedDate, cost, extra1);
				} else if ("1".equals(type)) {
					account = new DepartmentAccount(accountNumber, establishedDate, cost, extra1);
				} else {
					account = new ProcessAccount(accountNumber, establishedDate, cost, extra1);
				}
				accountList.add(account);
			}
			return accountList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return accountList;
	}

	/**
	 * Check exist accountNo
	 * @param accountNo: accountNo
	 * @return: true if exist, otherwise false
	 */
	public boolean checkExistAccountNo(String accountNo) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			StringBuilder sql = new StringBuilder("SELECT * FROM account WHERE account_number = ?");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, accountNo);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return false;
	}

	/**
	 * Add a account
	 * @param account: Account
	 * @return true if adding successfully, otherwise false
	 */
	public boolean add(Account account) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
			StringBuilder sql = new StringBuilder("INSERT INTO account(account_number, established_date, cost) VALUES (?,?,?)");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, account.getAccountNumber());
			ps.setDate(2, account.getEstablishedDate());
			ps.setDouble(3, account.getCost());
			if (ps.executeUpdate() > 0) {
				String accountNumber = account.getAccountNumber();
				if (account instanceof AssemblyAccount) {
					AssemblyAccount assemblyAccount = (AssemblyAccount) account;
					sql = new StringBuilder("INSERT INTO assembly_account(account_number, assembly_id) VALUES (?,?)");
					ps = connection.prepareStatement(sql.toString());
					ps.setString(1, accountNumber);
					ps.setInt(2, assemblyAccount.getAssemblyId());
					ps.executeUpdate();
				} else if (account instanceof DepartmentAccount) {
					DepartmentAccount departmentAccount = (DepartmentAccount) account;
					sql = new StringBuilder("INSERT INTO department_account(account_number, department_id) VALUES (?,?)");
					ps = connection.prepareStatement(sql.toString());
					ps.setString(1, accountNumber);
					ps.setInt(2, departmentAccount.getDepartmentId());
					ps.executeUpdate();
				} else {
					ProcessAccount processAccount = (ProcessAccount) account;
					sql = new StringBuilder("INSERT INTO process_account(account_number, process_id) VALUES (?,?)");
					ps = connection.prepareStatement(sql.toString());
					ps.setString(1, accountNumber);
					ps.setInt(2, processAccount.getProcessId());
					ps.executeUpdate();
				}
				connection.commit();
				return true;
			}
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {

			}
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return false;
	}

	/**
	 * Update supplied_cost for the account
	 * @param transaction: Transaction account number
	 * @param suppliedCost: Cost
	 * @return true if success, otherwise false
	 */
	public boolean updateSuppliedCost(Transaction transaction, double suppliedCost) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			StringBuilder sql = new StringBuilder("SELECT * FROM account WHERE account_number = ?");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, transaction.getAccountNumber());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				double currentSupCost = rs.getDouble("cost");
				double updateSupCost = currentSupCost + suppliedCost;

				sql = new StringBuilder("UPDATE account SET cost = ? WHERE account_number = ?");
				ps = connection.prepareStatement(sql.toString());
				ps.setDouble(1, updateSupCost);
				ps.setString(2, transaction.getAccountNumber());
				return ps.executeUpdate() > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return false;
	}

	/**
	 * Get total cost by assembly
	 * @param assemblyId Assembly_ID
	 * @return totalCost
	 */
	public double getTotalCostByAssemblyId(int assemblyId) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			StringBuilder sql = new StringBuilder("SELECT c.assembly_Id, SUM(a.Cost) as totalCost FROM account a ");
			sql.append("JOIN assembly_account b ON a.account_number = b.account_number ");
			sql.append("JOIN assembly c ON c.assembly_id = b.assembly_id ");
			sql.append("WHERE c.assembly_id = ? ");
			sql.append("GROUP BY c.assembly_id ");
			ps = connection.prepareStatement(sql.toString());
			ps.setInt(1, assemblyId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				double totalCost = rs.getDouble("totalCost");
				return totalCost;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return 0;
	}
}