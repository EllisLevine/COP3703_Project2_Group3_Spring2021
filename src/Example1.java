/* Example1.java
 * This sample shows how to list codes, descriptions, prices, and quantities on hand
 * of all products from the PRODUCT table, using statement.
 * 
 * It uses the JDBC THIN driver.
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection conn=DriverManager.getConnection(  url, uid, pword);
    
    
    // BEGIN LOGIN
    System.out.print("Enter Customer Number to login: ");
    String user = reader.next();
    String custID = "";
    Boolean hasAdmin = false;
    
    Statement stmt = conn.createStatement();
	String q1 = "select * from group3.customer where customer.Customer_ID='" + user+"'";
	ResultSet rset = stmt.executeQuery(q1);
	
	while (rset.next ()) {
        custID = rset.getString("Customer_ID");
        String custf = rset.getString("Admin");
        if (custf.equals("1")) {
        	hasAdmin = true;
        }
        
        System.out.println("cust " + custID);
        System.out.println("admin " + custf);
    }
	// END LOGIN
    
    System.out.println("\n User Options ");
    System.out.println("----------------------------");
    System.out.println("1. Search");
    System.out.println("2. Checkout (rent) / buy");
    System.out.println("3. Return");
    System.out.println("4. Check Balance");
    System.out.println("5. Pay Balance\n");
    
    if (hasAdmin) {
	    System.out.println("\n Admin Options ");
	    System.out.println("----------------------------");
	    System.out.println("6. Locate Title");
	    System.out.println("7. Update Inventory (add/delete/update titles");
	    System.out.println("8. Check user balance and apply late fees");
	    System.out.println("9. Generate Reports\n");
    }
    
    
    System.out.print("> "); int choice = reader.nextInt();
    System.out.println("");

    
    // Load the Oracle JDBC driver
    //DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

    // Connect to the database
   // Class.forName("com.mysql.jdbc.Driver");  
   
    
    if (choice == 1)
    {
  
    	System.out.print("> ");
    	String title = reader.next();
    	title += reader.nextLine();
    	Statement stmt1 = conn.createStatement();
    	String q2 = "select * from group3.isbn, group3.book where isbn.Title='" + title+"' and isbn.isbn = book.isbn";
        ResultSet rset1 = stmt1.executeQuery(q2);
        
        System.out.println("");
        System.out.println(q2);
        System.out.println("");
        
        while (rset1.next ()) {
          String isbn = rset1.getString("ISBN");
          String author = rset1.getString("AuthorName");
          String category = rset1.getString("Category");
          String publisher = rset1.getString("Publisher");
          String format = rset1.getString("Format");
          String condition = rset1.getString("Condition");
          boolean rent = rset1.getBoolean("Rent");
          boolean buy = rset1.getBoolean("Buy");
          float price = rset1.getFloat("Price");
          System.out.println("ISBN: " + isbn + "\n" 
                                  + "Author: " + author + "\n" 
                                  + "Category: " + category + "\n"
                                  + "Publisher: " + publisher + "\n"
                                  + "Price: $" + price + "\n"
                                  + "Condition: " + condition + "\n"
                                  + "Format: " + format + "\n"
                                  + "Rentable: " + rent + "\n"
                                  + "Buyable: " + buy + "\n");
      }
    	
    } // End Choice 1
    
    else if (choice == 2) {
    	
    	System.out.println(" Will you be renting or buying?");
    	System.out.println("1. Renting");
    	System.out.println("2. Buying");
    	System.out.print("> ");
    	int rentOrBuy = reader.nextInt();
    	String rob = "";
    
    	if (rentOrBuy == 1) {
    		System.out.println(" Enter book to checkout");
    		rob = "rent";
    	}
    	else if(rentOrBuy == 2) {
    		System.out.println(" Enter a book to buy");
    		rob = "buy";
    	}
    	else {
    		System.out.println(" Neither option selected, terminating program"); // maybe change later
    		System.exit(0);
    	}
    	System.out.print("> ");
    	String title = reader.next();
    	title += reader.nextLine();
    	
    	Statement st = conn.createStatement();
    	String q2 = "select * from group3.isbn where isbn.Title='" + title+"'";
    	ResultSet rset1 = st.executeQuery(q2);
    	String isbn = "";
    	while (rset1.next ()) {
            isbn = rset1.getString("ISBN");
    	}
    	Statement st1 = conn.createStatement();
    	String q3 = "select * from group3.book where book.ISBN='"+isbn+"'";
    	ResultSet rset2 = st1.executeQuery(q3);
    	String bookid = "";
    	while (rset2.next ()) {
    	    bookid = rset2.getString("Book_ID");
    	}
    	Statement st2 = conn.createStatement();
    	String q4 = "select * from group3.payment where payment.Customer_ID='"+custID+"'";
    	ResultSet rset3 = st2.executeQuery(q4);
    	String pid = "";
    	while (rset3.next()) {
    		pid = rset3.getString("Payment_ID");
    	}
    	
    	//rset2 = st1.getGeneratedKeys();
    	
    	//int ipid = Integer.parseInt(pid);
    	
    	PreparedStatement ps = conn.prepareStatement("INSERT INTO group3.transaction (Transaction_ID, Book_ID, TransDate, ReturnBy, ReturnDate, BoughtOrRent, Payment_ID) "
    			+ "VALUES(null , "+bookid+", ?, ?, null, '"+rob +"' ,'" + pid + "')");
    	
    	System.out.println("book id = " + bookid);
    	
    	//System.out.println(date);
    	java.util.Date date = new java.util.Date();
    	java.sql.Date sqlDate = new java.sql.Date(date.getTime());
    	java.sql.Date sqlDate1 = new java.sql.Date(date.getTime() + 5l*24l*60l*60l*1000l);
    	//date.toString();
    	
    	ps.setDate(1, sqlDate);
    	ps.setDate(2, sqlDate1);
    	
    	ps.executeUpdate();
    	
    	ps.close();
    	conn.close();
    	
    	

    	//st.setTimestamp(1, date);
    	
    	//int insert = 
    	
    	
    	
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












