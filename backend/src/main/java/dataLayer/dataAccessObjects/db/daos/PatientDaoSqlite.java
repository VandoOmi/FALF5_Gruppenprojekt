package dataLayer.dataAccessObjects.db.daos;

import dataLayer.exceptions.DaoException;
import models.Patient;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PatientDaoSqlite extends AbstactDaoSqlite<Patient, Long> {

	public PatientDaoSqlite(String databaseFilePath) {
		super(databaseFilePath, "patients");
	}

	@Override
	protected String createTableSql() {
		return "CREATE TABLE IF NOT EXISTS patients ("
				+ "id INTEGER PRIMARY KEY,"
				+ "vorname TEXT NOT NULL,"
				+ "nachname TEXT NOT NULL,"
				+ "geburtsdatum TEXT,"
				+ "pflegegrad INTEGER NOT NULL,"
				+ "zimmer TEXT NOT NULL,"
				+ "vermoegen REAL NOT NULL"
				+ ")";
	}

	@Override
	protected String insertSql() {
		return "INSERT INTO patients (id, vorname, nachname, geburtsdatum, pflegegrad, zimmer, vermoegen) VALUES (?, ?, ?, ?, ?, ?, ?)";
	}

	@Override
	protected String findByIdSql() {
		return "SELECT * FROM patients WHERE id = ?";
	}

	@Override
	protected String updateSql() {
		return "UPDATE patients SET vorname = ?, nachname = ?, geburtsdatum = ?, pflegegrad = ?, zimmer = ?, vermoegen = ? WHERE id = ?";
	}

	@Override
	protected String deleteByIdSql() {
		return "DELETE FROM patients WHERE id = ?";
	}

	@Override
	protected Patient mapRow(ResultSet resultSet) throws SQLException {
		String birthDateRaw = resultSet.getString("geburtsdatum");
		LocalDate birthDate = birthDateRaw == null || birthDateRaw.isBlank() ? null : LocalDate.parse(birthDateRaw);

		return instantiatePatient(
				resultSet.getLong("id"),
				resultSet.getString("vorname"),
				resultSet.getString("nachname"),
				birthDate,
				resultSet.getInt("pflegegrad"),
				resultSet.getString("zimmer"),
				resultSet.getDouble("vermoegen")
		);
	}

	@Override
	protected void bindInsert(PreparedStatement statement, Patient entity) throws SQLException {
		statement.setLong(1, entity.getId());
		statement.setString(2, entity.getVorname());
		statement.setString(3, entity.getNachname());
		statement.setString(4, entity.getGeburtsdatum() == null ? null : entity.getGeburtsdatum().toString());
		statement.setInt(5, entity.getPflegegrad());
		statement.setString(6, entity.getZimmer());
		statement.setDouble(7, entity.getVermoegen());
	}

	@Override
	protected void bindFindById(PreparedStatement statement, Long id) throws SQLException {
		statement.setLong(1, id);
	}

	@Override
	protected void bindUpdate(PreparedStatement statement, Patient entity) throws SQLException {
		statement.setString(1, entity.getVorname());
		statement.setString(2, entity.getNachname());
		statement.setString(3, entity.getGeburtsdatum() == null ? null : entity.getGeburtsdatum().toString());
		statement.setInt(4, entity.getPflegegrad());
		statement.setString(5, entity.getZimmer());
		statement.setDouble(6, entity.getVermoegen());
		statement.setLong(7, entity.getId());
	}

	@Override
	protected void bindDeleteById(PreparedStatement statement, Long id) throws SQLException {
		statement.setLong(1, id);
	}

	private Patient instantiatePatient(long id,
									   String vorname,
									   String nachname,
									   LocalDate geburtsdatum,
									   int pflegegrad,
									   String zimmer,
									   double vermoegen) {
		try {
			Constructor<Patient> constructor = Patient.class.getDeclaredConstructor(
					long.class,
					String.class,
					String.class,
					LocalDate.class,
					int.class,
					String.class,
					double.class
			);
			constructor.setAccessible(true);
			return constructor.newInstance(id, vorname, nachname, geburtsdatum, pflegegrad, zimmer, vermoegen);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
			throw new DaoException("Could not instantiate Patient model: " + ex.getMessage());
		}
	}
}
