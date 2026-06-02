package dataLayer.dataAccessObjects.file.daos;

import dataLayer.dataAccessObjects.IDao;
import dataLayer.exceptions.DaoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstactDaoSqlite<T, ID> implements IDao<T, ID> {

	private final String jdbcUrl;
	private final String findAllSql;

	protected AbstactDaoSqlite(String databaseFilePath, String tableName) {
		this.jdbcUrl = "jdbc:sqlite:" + databaseFilePath;
		this.findAllSql = "SELECT * FROM " + tableName;
		ensureTable();
	}

	protected abstract String createTableSql();

	protected abstract String insertSql();

	protected abstract String findByIdSql();

	protected abstract String updateSql();

	protected abstract String deleteByIdSql();

	protected abstract T mapRow(ResultSet resultSet) throws SQLException;

	protected abstract void bindInsert(PreparedStatement statement, T entity) throws SQLException;

	protected abstract void bindFindById(PreparedStatement statement, ID id) throws SQLException;

	protected abstract void bindUpdate(PreparedStatement statement, T entity) throws SQLException;

	protected abstract void bindDeleteById(PreparedStatement statement, ID id) throws SQLException;

	@Override
	public void create(T entity) {
		try (Connection connection = openConnection();
			 PreparedStatement statement = connection.prepareStatement(insertSql())) {
			bindInsert(statement, entity);
			statement.executeUpdate();
		} catch (SQLException ex) {
			throw new DaoException("Error while creating entity in SQLite: " + ex.getMessage());
		}
	}

	@Override
	public Optional<T> findById(ID id) {
		try (Connection connection = openConnection();
			 PreparedStatement statement = connection.prepareStatement(findByIdSql())) {
			bindFindById(statement, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return Optional.empty();
				}
				return Optional.of(mapRow(resultSet));
			}
		} catch (SQLException ex) {
			throw new DaoException("Error while loading entity by id from SQLite: " + ex.getMessage());
		}
	}

	@Override
	public List<T> findAll() {
		List<T> result = new ArrayList<>();

		try (Connection connection = openConnection();
			 PreparedStatement statement = connection.prepareStatement(findAllSql);
			 ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				result.add(mapRow(resultSet));
			}
		} catch (SQLException ex) {
			throw new DaoException("Error while loading entities from SQLite: " + ex.getMessage());
		}

		return result;
	}

	@Override
	public void update(T entity) {
		try (Connection connection = openConnection();
			 PreparedStatement statement = connection.prepareStatement(updateSql())) {
			bindUpdate(statement, entity);
			statement.executeUpdate();
		} catch (SQLException ex) {
			throw new DaoException("Error while updating entity in SQLite: " + ex.getMessage());
		}
	}

	@Override
	public void deleteById(ID id) {
		try (Connection connection = openConnection();
			 PreparedStatement statement = connection.prepareStatement(deleteByIdSql())) {
			bindDeleteById(statement, id);
			statement.executeUpdate();
		} catch (SQLException ex) {
			throw new DaoException("Error while deleting entity in SQLite: " + ex.getMessage());
		}
	}

	protected Connection openConnection() throws SQLException {
		return DriverManager.getConnection(jdbcUrl);
	}

	private void ensureTable() {
		try (Connection connection = openConnection();
			 PreparedStatement statement = connection.prepareStatement(createTableSql())) {
			statement.execute();
		} catch (SQLException ex) {
			throw new DaoException("Error while creating SQLite table: " + ex.getMessage());
		}
	}
}
