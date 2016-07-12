package WifiproInterfaces;

public class Utils {
	public static String JdbcDriverName = "org.mariadb.jdbc.Driver";
	public static String JdbcConnectionstring = "jdbc:mariadb://localhost/totwifing?user=wifipro&password=wifipro1234";
	//public static String JdbcConnectionstring = "jdbc:mariadb://10.1.164.144/totwifing?user=wifipro&password=wifipro1234";
	public static String CharacterEncoding = "UTF-8";
	
/*	public static String NMSUrlProjectInsert = "http://localhost:8080/testNMSInterfaces/testProjectInsert.php";
	public static String NMSUrlProjectUpdate = "http://localhost:8080/testNMSInterfaces/testProjectUpdate.php";
	public static String NMSUrlProjectDelete = "http://localhost:8080/testNMSInterfaces/testProjectDelete.php";
	
	public static String NMSUrlSiteInsert = "http://localhost:8080/testNMSInterfaces/testSiteInsert.php";
	public static String NMSUrlSiteUpdate = "http://localhost:8080/testNMSInterfaces/testSiteUpdate.php";
	public static String NMSUrlSiteDelete = "http://localhost:8080/testNMSInterfaces/testSiteDelete.php";
	
	public static String NMSUrlLocationInsert = "http://localhost:8080/testNMSInterfaces/testLocationInsert.php";
	public static String NMSUrlLocationUpdate = "http://localhost:8080/testNMSInterfaces/testLocationUpdate.php";
	public static String NMSUrlLocationDelete = "http://localhost:8080/testNMSInterfaces/testLocationDelete.php";
	
	public static String NMSUrlEquipmentInsert = "http://localhost:8080/testNMSInterfaces/testEquipmentInsert.php";
	public static String NMSUrlEquipmentUpdate = "http://localhost:8080/testNMSInterfaces/testEquipmentUpdate.php";
	public static String NMSUrlEquipmentDelete = "http://localhost:8080/testNMSInterfaces/testEquipmentDelete.php";
*/	
	public static String NMSUrlProjectInsert = "http://10.1.166.32/api/v2/projects";
	public static String NMSUrlProjectUpdate = "http://10.1.166.32/api/v2/projects";
	public static String NMSUrlProjectDelete = "http://10.1.166.32/api/v2/projects";
	
	public static String NMSUrlSiteInsert = "http://10.1.166.32/api/v2/sites";
	public static String NMSUrlSiteUpdate = "http://10.1.166.32/api/v2/sites";
	public static String NMSUrlSiteDelete = "http://10.1.166.32/api/v2/sites";
	
	public static String NMSUrlLocationInsert = "http://10.1.166.32/api/v2/locations";
	public static String NMSUrlLocationUpdate = "http://10.1.166.32/api/v2/locations";
	public static String NMSUrlLocationDelete = "http://10.1.166.32/api/v2/locations";
	
	public static String NMSUrlEquipmentInsert = "http://10.1.166.32/api/v2/nodes";
	public static String NMSUrlEquipmentUpdate = "http://10.1.166.32/api/v2/nodes";
	public static String NMSUrlEquipmentDelete = "http://10.1.166.32/api/v2/nodes";

	public static String DateTimeFormat = "dd/MM/yyyy HH:mm:ss";
}
