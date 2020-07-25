package db;

public class MySQLDBUtil {
	//db endpoint address
	private static final String INSTANCE = "database-summer2-project.ckj5dcy3ys0d.us-east-2.rds.amazonaws.com";
	private static final String PORT_NUM = "3306";
	private static final String DB_NAME = "jupiter";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "Yyz199632!";
	public static final String URL = "jdbc:mysql://"
			+ INSTANCE + ":" + PORT_NUM + "/" + DB_NAME
			+ "?user=" + USERNAME + "&password=" + PASSWORD
			+ "&autoReconnect=true&serverTimezone=UTC";
}
