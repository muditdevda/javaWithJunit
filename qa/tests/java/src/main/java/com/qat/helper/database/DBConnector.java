package com.dfs.helper.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.dfs.helper.properties.PropertiesHelper;
import com.dfs.model.DeliveryDatesSumatory;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.awaitility.Awaitility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfs.model.Request;


public class DBConnector {
	private static Logger logger = LoggerFactory.getLogger(DBConnector.class);

	private final String url;
	private final String username;
	private final String passwd;
	private String dbname;
	private final String identifierPrefixContact;
	private final String identifierPrefixCustomer;
	private final String identifierPrefixProvider;

	public DBConnector() {
		PropertiesHelper ph = new PropertiesHelper();
		dbname = "\"" + ph.getFlowDBName() + "\"";

		if (dbname.equals("tboc_Flow")){
			dbname = "Tboc_Flow";
		}



		logger.info ("database to use is {}" , dbname);
		identifierPrefixContact = ph.getIdentifierPrefixContact();
		identifierPrefixCustomer = ph.getIdentifierPrefixCustomer();
		identifierPrefixProvider = ph.getIdentifierPrefixProvider();

		url = "jdbc:sqlserver://" + ph.getFlowDBHost() + ":" + ph.getFlowDBPort();
		username = ph.getFlowDBUser();
		passwd = ph.getFlowDBPasswd();

	}

	private <T> T query(String sql, Class<T> resultClass, Object... params) throws SQLException {
		try (Connection conn = DriverManager.getConnection(url, username, passwd)) {
			return new QueryRunner().query(conn, sql, new BeanHandler<T>(resultClass), params);
		}
	}



	public List<DeliveryDatesSumatory> getAllDeliveryDatesForProviderByName(String providerName, String dueDate) throws SQLException {
		List<DeliveryDatesSumatory> deliveryDatesList;
		String duedates = "DATEADD(DAY," +dueDate+ ",GETDATE())";
		String sql="select" +dbname+ ".dbo.task.delivery_date, "+dbname+".dbo.level_of_effort.provider_effort pay from "+dbname +".dbo.task inner join " +
				dbname+".dbo.provider on " + dbname+ ".dbo.task.provider_id = provider.oid " +
				"inner join "+dbname +".dbo.level_of_effort on "+dbname+ ".dbo.task.oid = level_of_effort.task_id " +
				"where first_name  = ? "; //+


		logger.info("THE QUERY IS : " + sql);


		try (Connection conn = DriverManager.getConnection(url, username, passwd)) {
			deliveryDatesList= new QueryRunner().query(conn, sql,new BeanListHandler<DeliveryDatesSumatory>(DeliveryDatesSumatory.class) ,providerName);

		}
		return deliveryDatesList;
	}

    public int updateService(String service, String value) throws SQLException {

        String updateString = "update "+dbname+".dbo.service set service.unique_identifier ='"+value+ "' where "+dbname+ ".dbo.service.oid in( select s.oid from "+dbname+".dbo.service s " +
                "inner join "+ dbname+ ".dbo.label_hash_label lhl on (s.name_label_id = lhl.label_id ) where lhl.elmnt = '"+ service +"' and lhl.mapkey = 'en')";
        return update(updateString);

    }




	private int update(String sql, Object... params) throws SQLException {
		logger.debug("Executing {} ", sql);
		try (Connection conn = DriverManager.getConnection(url, username, passwd)) {
			return new QueryRunner().update(conn, sql, params);
		}
	}







	public Request getProject(String oid) throws SQLException {
		String sql = "SELECT request_no FROM " + dbname + ".dbo.Request where oid=?";
		return query(sql, Request.class, oid);
	}

	public String getProjectIdFromProjectNumber(String projectNumber) throws SQLException {
		Awaitility.await().with().pollInterval(10,TimeUnit.SECONDS).atMost(120, TimeUnit.SECONDS).until(() -> projectExists(projectNumber));
		Request r = getRequestForProjectname(projectNumber);
		return "1250936094" + "-" + r.oid();
	}

	private Request getRequestForProjectname(String projectNumber) throws SQLException {
		String sql = "SELECT oid FROM " + dbname + ".dbo.Request where request_no =?";
		logger.debug("Executing {} param: {}", sql, projectNumber);
		return query(sql, Request.class, projectNumber);
	}

	private boolean projectExists(String projectNumber) throws SQLException {
		return getRequestForProjectname(projectNumber) != null;
	}

	public int updateVle(String name, String value) throws SQLException {
		return update("update " + dbname + ".dbo.param set vle=? where name='" + name + "'", value);
	}

	public int updateProviderEmail(String provider, String email) throws SQLException {
		String sql = "update " + dbname + ".dbo.provider set notification_email='" + email
				+ "' where usr_id in(select oid from " + dbname + ".dbo.usr where username='" + provider + "')";
		logger.info(sql);
		return update(sql);
	}

	public int clientClientAceesApi(String userName) throws SQLException {

		String sql = "INSERT INTO " + dbname + ".dbo.user_modules (usr_id, modules_mdle_id)SELECT " + dbname
				+ ".dbo.usr.oid, 'web_service_api' AS module_id\n" +

				"FROM " + dbname + ".dbo.usr " + "\n" + "WHERE usr.username = '" + userName
				+ "' AND NOT EXISTS (SELECT 1 FROM " + dbname
				+ ".dbo.user_modules um2 WHERE um2.usr_id = usr.oid AND um2.modules_mdle_id = 'web_service_api')";
		logger.info("query to update  to access restAPi {}" , sql);
		return update(sql);

	}




	
	public String quotationRequiredForOid(String oid) throws SQLException {
		String sql = "SELECT request_no,quotation_required FROM " + dbname + ".dbo.Request where oid =?";
		logger.info("Executing {} param: {}", sql, oid);
		return query(sql, Request.class, oid).getQuotation_required();
	}
	
	
	public String clientRefReqNumberForOid(String oid) throws SQLException {
		String sql = "SELECT request_no,client_ref_request_no FROM " + dbname + ".dbo.Request where oid =?";
		logger.info("Executing {} param: {}", sql, oid);
		return query(sql, Request.class, oid).getClient_ref_request_no();
	}
	
	public String priorityForOid(String oid) throws SQLException {
		String sql = "SELECT request_no,priority_id FROM " + dbname + ".dbo.Request where oid =?";
		logger.info("Executing {} param: {}", sql, oid);
		return query(sql, Request.class, oid).getPriority_id();
	}


}
