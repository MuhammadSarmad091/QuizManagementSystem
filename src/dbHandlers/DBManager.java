package dbHandlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DBManager 
{
	private static DBManager dbManager = null;
    private String jdbcUrl;
    private String driverClass;

    private DBManager() 
    {
        this.jdbcUrl =  "jdbc:sqlserver://Sarmad\\SQLEXPRESS;databaseName=QuizManagementSystem;IntegratedSecurity=True;trustServerCertificate=true";
        this.driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }
    public static DBManager getDBManager()
	{
		if(dbManager == null)
		{
			dbManager = new DBManager();
			return dbManager;
		}
		else return dbManager;
	}

    public Connection connect() {
        Connection conn = null;
        try {
            if (driverClass != null) {
                Class.forName(driverClass);
            }
            conn = DriverManager.getConnection(jdbcUrl);
        } catch (ClassNotFoundException e) {
            // Handle driver class not found exception
            e.printStackTrace();
        } catch (SQLException e) {
            // Handle SQL connection exception
            e.printStackTrace();
        }
        return conn;
    }


    public String getJdbcUrl() 
    {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }


    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }
}