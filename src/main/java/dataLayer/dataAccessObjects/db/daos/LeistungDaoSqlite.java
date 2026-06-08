package dataLayer.dataAccessObjects.db.daos;

import dataLayer.exceptions.DaoException;
import models.Leistung;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeistungDaoSqlite extends AbstactDaoSqlite<Leistung, String> {

	public LeistungDaoSqlite(String databaseFilePath) {
		super(databaseFilePath, "leistungen");
	}

	@Override
	protected String createTableSql() {
		return "CREATE TABLE IF NOT EXISTS leistungen ("
				+ "lk_nr TEXT PRIMARY KEY,"
				+ "bezeichnung TEXT NOT NULL,"
				+ "beschreibung TEXT NOT NULL"
				+ ")";
	}

	@Override
	protected String insertSql() {
		return "INSERT INTO leistungen (lk_nr, bezeichnung, beschreibung) VALUES (?, ?, ?)";
	}

	@Override
	protected String findByIdSql() {
		return "SELECT * FROM leistungen WHERE lk_nr = ?";
	}

	@Override
	protected String updateSql() {
		return "UPDATE leistungen SET bezeichnung = ?, beschreibung = ? WHERE lk_nr = ?";
	}

	@Override
	protected String deleteByIdSql() {
		return "DELETE FROM leistungen WHERE lk_nr = ?";
	}

	@Override
	protected Leistung mapRow(ResultSet resultSet) throws SQLException {
		return instantiateLeistung(
				resultSet.getString("lk_nr"),
				resultSet.getString("bezeichnung"),
				resultSet.getString("beschreibung")
		);
	}

	@Override
	protected void bindInsert(PreparedStatement statement, Leistung entity) throws SQLException {
		statement.setString(1, entity.getLkNr());
		statement.setString(2, entity.getBezeichnung());
		statement.setString(3, entity.getBeschreibung());
	}

	@Override
	protected void bindFindById(PreparedStatement statement, String id) throws SQLException {
		statement.setString(1, id);
	}

	@Override
	protected void bindUpdate(PreparedStatement statement, Leistung entity) throws SQLException {
		statement.setString(1, entity.getBezeichnung());
		statement.setString(2, entity.getBeschreibung());
		statement.setString(3, entity.getLkNr());
	}

	@Override
	protected void bindDeleteById(PreparedStatement statement, String id) throws SQLException {
		statement.setString(1, id);
	}

	private Leistung instantiateLeistung(String lkNr, String bezeichnung, String beschreibung) {
		try {
			Constructor<Leistung> constructor = Leistung.class.getDeclaredConstructor(String.class, String.class, String.class);
			constructor.setAccessible(true);
			return constructor.newInstance(lkNr, bezeichnung, beschreibung);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
			throw new DaoException("Could not instantiate Leistung model: " + ex.getMessage());
		}
	}
}
