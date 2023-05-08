package com.dfs.helper.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DBMangler {
	private static final Logger LOGGER = LoggerFactory.getLogger(DBMangler.class);
	private static final String DEFAULT_MACHINE_NAME = "kkk";

	public static void main(String[] args) throws Exception {
		String machineName = System.getProperty("machineName") != null ? System.getProperty("machineName"):DEFAULT_MACHINE_NAME;

		// TODO Move all this configuration to a properties file
		try (Connection conn = DriverManager
				.getConnection("jdbc:sqlserver://qa48-2012R2.global.sdl.corp:1433;user=sa2;password=Password123");
				Statement statement = conn.createStatement();) {
			final String[] dbs = {  machineName + "_Flow", machineName + "_Reports" };
			for (String db : dbs) {
				LOGGER.info("Re-creating {}", db);
				dropTableIfExists(statement, db);
				statement.execute("create DATABASE" + "\"" + db + "\"");
			}

		} catch (SQLException e) {
			LOGGER.error("Exception while updating db", e);
		}

	}

	private static void dropTableIfExists(Statement statement, String db) {
		try {
			//statement.execute("ALTER DATABASE " + "\"" + db + "\"" + "SET AUTO_CLOSE OFF");SET OFFLINE


			statement.execute("ALTER DATABASE " + "\"" + db + "\"" + "set single_user With rollback IMMEDIATE");
			statement.execute("DROP DATABASE " + "\"" + db + "\"");
		} catch (SQLException e) {
			LOGGER.error("Could not alter/drop DB {}", db, e);
		}
		LOGGER.info("{} db dropped",db);
	}
}