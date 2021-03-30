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
    String url = "jdbc:mysql://cisvm-cop3855.unfcsd.unf.edu:3306/group3";
    
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

    System.out.println("\nEnter vendor code, \nThen product code, " +
                       "description, price, and quantity on hand " +
                       "will be displayed\n");

    int v = 1;

    while (v != 0) {
      // read vendor code
      System.out.print("Vendor Code (enter 0 for exit): ");
      v = getInt();

      // Select the P_CODE, P_DESCRIPT, P_PRICE, and P_ONHAND columns from the 
      // PRODUCT table

      String q = "select P_CODE, P_DESCRIPT, P_PRICE, P_ONHAND " +
               "from PRODUCT " +
               "where V_CODE= " + v;

      ResultSet rset = stmt.executeQuery(q);

      System.out.println("\n");

      // Iterate through the result
      while (rset.next ()) {
        String pcode = rset.getString("P_CODE");
        String pdescript = rset.getString("P_DESCRIPT");
        float pprice = rset.getFloat("P_PRICE");
        int ponhand = rset.getInt("P_ONHAND");        
        System.out.println (pcode + ":" + pdescript + ":" + 
                            pprice + ":" + ponhand);
      } // while rset

      System.out.println("\n");
    } // while v

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












