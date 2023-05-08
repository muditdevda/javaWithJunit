package com.dfs.helper.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dfs.helper.properties.PropertiesHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DBConnectorMT {
	private static Logger logger = LoggerFactory.getLogger(DBConnectorMT.class);

	private final String url;
	private final String username;
	private final String passwd;
	private final String dbname;

	public DBConnectorMT() {
		PropertiesHelper ph = new PropertiesHelper();
		dbname = "\"" + ph.getMTDBName() + "\"";

		url = "jdbc:sqlserver://" + ph.getFlowDBHost() + ":" + ph.getFlowDBPort();
		username = ph.getFlowDBUser();
		passwd = ph.getFlowDBPasswd();

	}

	private <T> T query(String sql, Class<T> resultClass, Object... params) throws SQLException {
		try (Connection conn = DriverManager.getConnection(url, username, passwd)) {
			return new QueryRunner().query(conn, sql, new BeanHandler<T>(resultClass), params);
		}
	}


}