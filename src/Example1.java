/* Example1.java
 * This sample shows how to list codes, descriptions, prices, and quantities on hand
 * of all products from the PRODUCT table, using statement.
 * 
 * It uses the JDBC THIN driver.
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;
//import java.util.Properties;
import java.io.*;  

class Example1
{
		
	
	
  public static void main (String args [])
       throws SQLException, ClassNotFoundException
  {
	//Properties prop = new Properties();
	//InputStream input = null;
	  
    System.out.print("userid: ");
    String uid = getString();

    System.out.print("password: ");
    String pword = getString();
    String url = "jdbc:mysql://cisvm-winsrv-mysql1.unfcsd.unf.edu:3306/group3";
    
//	try {
//
//		input = new FileInputStream("config.properties");
//
//		// load a properties file
//		prop.load(input);
//
//		// get the property value and print it out
//		 url = prop.getProperty("database");
//		 //uid = prop.getProperty("dbuser");
//		 //pword = prop.getProperty("dbpassword");
//
//
//	} catch (IOException ex) {
//		ex.printStackTrace();
//	} finally {
//		if (input != null) {
//			try {
//				input.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
    

    
    // Load the Oracle JDBC driver
    //DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

    // Connect to the database
   // Class.forName("com.mysql.jdbc.Driver");  
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection conn=DriverManager.getConnection(  url, uid, pword);
    

    // Create a Statement
    Statement stmt = conn.createStatement ();
    
    String q = "select * from group3.isbn";
    
    ResultSet rset = stmt.executeQuery(q);
    
    while (rset.next ()) {
        String isbn = rset.getString("ISBN");
               
        System.out.println(isbn);
    } // while rset
    
  } // main 

 public static String getString() {
	try {
	    StringBuffer buffer = new StringBuffer();
        int c = System.in.read();
        while (c != '\n' && c != -1) {
    	  buffer.append((char)c);
          c = System.in.read();
          }
        return buffer.toString().trim();
        }
        catch (IOException e){return "";}
    }

  public static int getInt() 

  {
      String s= getString();
      return Integer.parseInt(s);
  }

} // ex1












