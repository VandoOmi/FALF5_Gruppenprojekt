package dataLayer.dataAccessObjects.db.daos;

import models.Pflegekraft;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PflegekraftDaoSqlite extends AbstactDaoSqlite<Pflegekraft, Long> {

	public PflegekraftDaoSqlite(String databaseFilePath) {
		super(databaseFilePath, "pflegekraefte");
	}

	@Override
	protected String createTableSql() {
		return "CREATE TABLE IF NOT EXISTS pflegekraefte ("
				+ "id INTEGER PRIMARY KEY,"
				+ "vorname TEXT NOT NULL,"
				+ "nachname TEXT NOT NULL,"
				+ "telefon TEXT NOT NULL"
				+ ")";
	}

	@Override
	protected String insertSql() {
		return "INSERT INTO pflegekraefte (id, vorname, nachname, telefon) VALUES (?, ?, ?, ?)";
	}

	@Override
	protected String findByIdSql() {
		return "SELECT * FROM pflegekraefte WHERE id = ?";
	}

	@Override
	protected String updateSql() {
		return "UPDATE pflegekraefte SET vorname = ?, nachname = ?, telefon = ? WHERE id = ?";
	}

	@Override
	protected String deleteByIdSql() {
		return "DELETE FROM pflegekraefte WHERE id = ?";
	}

	@Override
	protected Pflegekraft mapRow(ResultSet resultSet) throws SQLException {
		return new Pflegekraft(
				resultSet.getLong("id"),
				resultSet.getString("vorname"),
				resultSet.getString("nachname"),
				resultSet.getString("telefon")
		);
	}

	@Override
	protected void bindInsert(PreparedStatement statement, Pflegekraft entity) throws SQLException {
		statement.setLong(1, entity.getId());
		statement.setString(2, entity.getVorname());
		statement.setString(3, entity.getNachname());
		statement.setString(4, entity.getTelefon());
	}

	@Override
	protected void bindFindById(PreparedStatement statement, Long id) throws SQLException {
		statement.setLong(1, id);
	}

	@Override
	protected void bindUpdate(PreparedStatement statement, Pflegekraft entity) throws SQLException {
		statement.setString(1, entity.getVorname());
		statement.setString(2, entity.getNachname());
		statement.setString(3, entity.getTelefon());
		statement.setLong(4, entity.getId());
	}

	@Override
	protected void bindDeleteById(PreparedStatement statement, Long id) throws SQLException {
		statement.setLong(1, id);
	}
}
