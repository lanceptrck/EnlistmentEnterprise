package com.orangeandbronze.enlistment.dao.jdbc;

import java.sql.Connection;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;

public class AdminDaoJdbcIT {
	private DataSource dataSource;
	Connection jdbcConnection;
	private IDataSet dataSet;
	FlatXmlDataSetBuilder builder;
	IDatabaseConnection dbUnitConnection;

	@Before
	public void init() throws Exception {
		dataSource = DataSourceManager.getDataSource();
		jdbcConnection = dataSource.getConnection();
		jdbcConnection.createStatement().execute("SET CONSTRAINTS ALL DEFERRED");

		builder = new FlatXmlDataSetBuilder();
		dataSet = builder.build(getClass().getClassLoader().getResourceAsStream("DefaultDataset.xml"));
		dbUnitConnection = new DatabaseConnection(jdbcConnection);
		builder.setDtdMetadata(false);

		try {
			DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataSet);
		} finally {
			dbUnitConnection.close(); // don't forget to close the connection!
		}
	}

}
