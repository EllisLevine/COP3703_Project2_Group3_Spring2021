/* Example1.java
 * This sample shows how to list codes, descriptions, prices, and quantities on hand
 * of all products from the PRODUCT table, using statement.
 * 
 * It uses the JDBC THIN driver.
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;
import java.util.Scanner;
//import java.util.Properties;
import java.io.*;  

class Example1
{
		
	
	
  public static void main (String args [])
       throws SQLException, ClassNotFoundException
  {
	//Properties prop = new Properties();
	//InputStream input = null;
	  
    //System.out.print("userid: ");
	Scanner reader = new Scanner(System.in); 
	  
    String uid = "n01362563";

    //System.out.print("password: ");
    String pword = "Spring20212563";
    String url = "jdbc:mysql://cisvm-winsrv-mysql1.unfcsd.unf.edu:3306/group3";
    
    System.out.println("\n User Options ");
    System.out.println("----------------------------");
    System.out.println("1. Search");
    System.out.println("2. Checkout (rent) / buy");
    System.out.println("3. Return");
    System.out.println("4. Check Balance");
    System.out.println("5. Pay Balance\n");
    
    System.out.print("> "); int choice = reader.nextInt();
    System.out.println("");

    
    // Load the Oracle JDBC driver
    //DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

    // Connect to the database
   // Class.forName("com.mysql.jdbc.Driver");  
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection conn=DriverManager.getConnection(  url, uid, pword);
    
    if (choice == 1)
    {
  
    	System.out.print("> ");
    	String title = reader.next();
    	title += reader.nextLine();
    	Statement stmt = conn.createStatement();
    	String q1 = "select * from group3.isbn where isbn.Title='" + title+"'";
    	ResultSet rset = stmt.executeQuery(q1);
    	
    	System.out.println("");
    	System.out.println(q1);
    	System.out.println("");
    	
    	while (rset.next ()) {
          String isbn = rset.getString("ISBN");
          String author = rset.getString("AuthorName");
          String category = rset.getString("Category");
          String publisher = rset.getString("Publisher");
          String format = rset.getString("Format");
          System.out.println("ISBN: " + isbn + "\n" 
        		  				+ "Author: " + author + "\n" 
        		  				+ "Category: " + category + "\n"
        		  				+ "Publisher: " + publisher + "\n"
        		  				+ "Format: " + format);

      } // while rset
    	
    }

    // Create a Statement
//    Statement stmt = conn.createStatement ();
//    
//    String q = "select * from group3.isbn";
//    
//    ResultSet rset = stmt.executeQuery(q);
    
//    while (rset.next ()) {
//        String isbn = rset.getString("ISBN");
//               
//        System.out.println(isbn);
//    } // while rset
    
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












