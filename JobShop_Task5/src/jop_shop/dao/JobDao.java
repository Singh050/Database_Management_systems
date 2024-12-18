package jop_shop.dao;

import jop_shop.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDao extends BaseDao {
	private TransactionDao transactionDao = new TransactionDao();

	/**
	 * Add a job
	 * @param job
	 * @return true if success, otherwise false
	 */
	public boolean add(Job job) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
			StringBuilder sql = new StringBuilder("INSERT INTO job(completed_date, commenced_date, assembly_id, process_id, labor_time) VALUES (?,?,?,?,?)");
			ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);

			ps.setDate(1, job.getCompletedDate());
			ps.setDate(2, job.getCommencedDate());
			ps.setInt(3, job.getAssemblyId());
			ps.setInt(4, job.getProcessId());
			ps.setInt(5, job.getLaborTime());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				int jobNumber = rs.getInt(1);
				if (job instanceof FitJob) {
					sql = new StringBuilder("INSERT INTO fit_job(job_number) VALUES (?)");
					ps = connection.prepareStatement(sql.toString());
					ps.setInt(1, jobNumber);
					ps.executeUpdate();
				} else if (job instanceof CutJob) {
					CutJob cutJob = (CutJob) job;
					sql = new StringBuilder("INSERT INTO cut_job(job_number, machine_type, machine_used_time, material_used) VALUES (?,?,?,?)");
					ps = connection.prepareStatement(sql.toString());
					ps.setInt(1, jobNumber);
					ps.setString(2, cutJob.getMachineType());
					ps.setInt(3, cutJob.getMachineUsedTime());
					ps.setString(4, cutJob.getMaterialUsed());
					ps.executeUpdate();
				} else {
					PaintJob paintJob = (PaintJob) job;
					sql = new StringBuilder("INSERT INTO paint_job(job_number, color, volume) VALUES (?,?,?)");
					ps = connection.prepareStatement(sql.toString());
					ps.setInt(1, jobNumber);
					ps.setString(2, paintJob.getColor());
					ps.setInt(3, paintJob.getVolume());
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
	 * Find all jobs
	 * @return list jobs
	 */
	public List<Job> findAll() {
		Connection connection = null;
		PreparedStatement ps = null;
		List<Job> jobList = new ArrayList<>();
		try {
			connection = getConnection();
			StringBuilder sql = new StringBuilder("SELECT a.job_number, a.completed_date, a.commenced_date, a.assembly_id, a.process_id, a.labor_time, '' as extra1, 0 as extra2, '' as extra3, '0' as type FROM job a JOIN fit_job b ON a.job_number = b.job_number ");
			sql.append("UNION ALL SELECT a.job_number, a.completed_date, a.commenced_date, a.assembly_id, a.process_id, a.labor_time, b.machine_type as extra1, b.machine_used_time as extra2, b.material_used as extra3, '1' as type FROM job a JOIN cut_job b ON a.job_number = b.job_number ");
			sql.append("UNION ALL SELECT a.job_number, a.completed_date, a.commenced_date, a.assembly_id, a.process_id, a.labor_time, b.color as extra1, b.volume as extra2, '' as extra3, '2' as type FROM job a JOIN paint_job b ON a.job_number = b.job_number ");
			ps = connection.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int jobNumber = rs.getInt("job_number");
				Date completedDate = rs.getDate("completed_date");
				Date commencedDate = rs.getDate("commenced_date");
				int assemblyId = rs.getInt("assembly_id");
				int processId = rs.getInt("process_id");
				int laborTime = rs.getInt("labor_time");
				String extra1 = rs.getString("extra1");
				int extra2 = rs.getInt("extra2");
				String extra3 = rs.getString("extra3");
				String type = rs.getString("type");
				Job job;
				if ("0".equals(type)) {
					job = new FitJob(jobNumber, completedDate, commencedDate, assemblyId, processId, laborTime);
				} else if ("1".equals(type)) {
					job = new CutJob(jobNumber, completedDate, commencedDate, assemblyId, processId, laborTime, extra1, extra2, extra3);
				} else {
					job = new PaintJob(jobNumber, completedDate, commencedDate, assemblyId, processId, laborTime, extra1, extra2);
				}
				jobList.add(job);
			}
			return jobList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return jobList;
	}

	/**
	 * Update completed_date job and insert a transaction
	 * @param jobNumber
	 * @param completedDate
	 * @param transaction
	 * @return true if success, otherwise false
	 */
	public boolean updateCompletedDateAndInsertTran(int jobNumber, Date completedDate, Transaction transaction) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
			StringBuilder sql = new StringBuilder("UPDATE job SET completed_date = ? WHERE job_number = ?");
			ps = connection.prepareStatement(sql.toString());
			ps.setDate(1, completedDate);
			ps.setInt(2, jobNumber);
			if (ps.executeUpdate() > 0) {
				boolean res = transactionDao.add(connection, transaction);
				if (res) {
					connection.commit();
					return true;
				} else {
					connection.rollback();
				}
			}
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException sqlException) {

			}
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return false;
	}

	/**
	 * Get total labor time by department and completed_date
	 * @param departmentId
	 * @param completedDate
	 * @return totalLaborTime
	 */
	public int getTotalLaborTime(int departmentId, Date completedDate) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			StringBuilder sql = new StringBuilder("SELECT c.department_Id, a.completed_date, SUM(a.labor_time) as totalLaborTime FROM job a ");
			sql.append("JOIN process b ON a.process_id = b.process_id ");
			sql.append("JOIN department c ON b.department_id = c.department_id ");
			sql.append("WHERE c.department_id = ? AND a.completed_date = ? ");
			sql.append("GROUP BY c.department_Id, a.completed_date");
			ps = connection.prepareStatement(sql.toString());
			ps.setInt(1, departmentId);
			ps.setDate(2, completedDate);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int totalLaborTime = rs.getInt("totalLaborTime");
				return totalLaborTime;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return 0;
	}

	/**
	 * Delete jobs with the range
	 * @param jobNumber1
	 * @param jobNumber2
	 * @return true if success, otherwise false
	 */
	public boolean deleteJobWithRange(int jobNumber1, int jobNumber2) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
			StringBuilder sql = new StringBuilder("DELETE FROM cut_job FROM cut_job a JOIN job b ON a.job_number = b.job_number JOIN [transaction] c ON a.job_number = c.job_number WHERE a.job_number BETWEEN ? AND ? ");
			ps = connection.prepareStatement(sql.toString());
			ps.setInt(1, jobNumber1);
			ps.setInt(2, jobNumber2);
			ps.executeUpdate();

			sql = new StringBuilder("DELETE FROM [transaction] FROM [transaction] a JOIN job b ON a.job_number = b.job_number JOIN cut_job c ON a.job_number = c.job_number WHERE a.job_number BETWEEN ? AND ? ");
			ps = connection.prepareStatement(sql.toString());
			ps.setInt(1, jobNumber1);
			ps.setInt(2, jobNumber2);
			ps.executeUpdate();

			sql = new StringBuilder("DELETE FROM job FROM job a JOIN cut_job b ON a.job_number = b.job_number JOIN [transaction] c ON a.job_number = c.job_number WHERE a.job_number BETWEEN ? AND ?");
//			sql = new StringBuilder("DELETE FROM job FROM job WHERE job_number BETWEEN ? AND ?");
			ps = connection.prepareStatement(sql.toString());
			ps.setInt(1, jobNumber1);
			ps.setInt(2, jobNumber2);
			ps.executeUpdate();
			connection.commit();
			return true;
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
	 * Update the paint job color
	 * @param jobNumber
	 * @param color
	 * @return true if success, otherwise false
	 */
	public boolean updatePaintJobColor(int jobNumber, String color) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			StringBuilder sql = new StringBuilder("UPDATE paint_job SET color = ? WHERE job_number = ?");
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, color);
			ps.setInt(2, jobNumber);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection, ps, null);
		}
		return false;
	}
}
