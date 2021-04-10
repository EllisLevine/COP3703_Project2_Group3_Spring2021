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
    	
    	
    	
    	// check if new release

    	
    	
    	//System.out.println(date);
    	java.util.Date date = new java.util.Date();
    	java.sql.Date sqlDate = new java.sql.Date(date.getTime());
    	java.sql.Date sqlDate1 = null;
    	
    	
    	if (rob.equals("rent")) {
    		Statement st3 = conn.createStatement();
    		String q5 = "select * from group3.book where Book_ID='"+bookid+"'";
    		ResultSet rset4 = st3.executeQuery(q5);
    		String tem = "";
    		while (rset4.next()) {
    			tem = rset4.getString("NewRelease");
    		}
    		if (tem.equals("0")) {
    			sqlDate1 = new java.sql.Date(date.getTime() + 5l*24l*60l*60l*1000l);
    		}
    		else {
    			sqlDate1 = new java.sql.Date(date.getTime() + 4l*24l*60l*60l*1000l);
    		}
    	} 
    	else {
    		sqlDate1 = new java.sql.Date(date.getTime() + 5l*24l*60l*60l*1000l);
    	}
    	
    	//date.toString();
    	
    	ps.setDate(1, sqlDate);
    	ps.setDate(2, sqlDate1);
    	
    	ps.executeUpdate();
    	
    	
    	Statement st3 = conn.createStatement();
    	String q5 = "select MAX(Transaction_ID) from group3.transaction";
    	ResultSet rset4 = st3.executeQuery(q5);
    	String maxv = "";
    	while (rset4.next()) {
    		maxv = rset4.getString("max(Transaction_ID)");
    	}
    	
    	Statement st4 = conn.createStatement();
    	String q6 = "select * from group3.book where Book_ID='"+bookid+"'";
    	ResultSet rset5 = st4.executeQuery(q6);
    	String bookprice = "";
    	while (rset5.next()) {
    		bookprice = rset5.getString("Price");
    	}
    	
    	Statement st5 = conn.createStatement();
    	String q7 = "select * from group3.bill where bill.Customer_ID='"+custID+"'";
    	ResultSet rset6 = st5.executeQuery(q7);
    	String bal = "";
    	while (rset6.next ()) {
            bal = rset6.getString("Total");
    	}
    	System.out.println(" Your current total is $" + bal);
    	
    	
    	float newtotal = Float.parseFloat(bookprice) + Float.parseFloat(bal);
    	// Update bill section
    	int maxi = Integer.parseInt(maxv);
    	PreparedStatement ps1 = conn.prepareStatement("INSERT INTO group3.bill (Tax, LateFee, Total, Customer_ID, Transaction_ID) "
    			+ "VALUES(7 , 7,"+newtotal+", '"+custID +"' ,'" + maxi + "')");
    	
    	
    	ps1.executeUpdate();
    	

    	//st.setTimestamp(1, date);
    	
    	//int insert = 
    	
    	
    } // end choice 2
 
    
    if(choice == 3) {
        System.out.println("Please enter the transaction ID from your book recipet");
        int transactionID = reader.nextInt();
        //need some way to make sure that the transactionID int exists and to repeat the statement if not
        
        java.util.Date date = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        
        String qEndDate = "select * from group3.transaction where transaction.Transaction_ID='" + transactionID + "'";
        Statement sEndDate = conn.createStatement();
        ResultSet rEndDate = sEndDate.executeQuery(qEndDate);
        
        rEndDate.next();
        java.sql.Date endDate = rEndDate.getDate("ReturnBy");
        System.out.println("End date = " + endDate);
        
        String q3 = "UPDATE `group3`.`transaction` SET `ReturnDate` = '" + sqlDate + "' WHERE (`Transaction_ID` = '" + transactionID + "')";
        PreparedStatement update = conn.prepareStatement(q3);
        update.executeUpdate();
        String q3_1 = "select * from group3.transaction where transaction.Transaction_ID='" + transactionID + "'";
        System.out.println("query: " + q3_1);
        
        if(sqlDate.compareTo(endDate) > 0) {
            System.out.println("This is late!");
            //need to add late fee here
        }
    }

    if (choice == 4) {
    	
    	Statement st = conn.createStatement();
    	String q2 = "select * from group3.bill where bill.Customer_ID='"+custID+"'";
    	ResultSet rset1 = st.executeQuery(q2);
    	String bal = "";
    	while (rset1.next ()) {
            bal = rset1.getString("Total");
    	}
    	System.out.println(" Your current total is $" + bal);
    }
    
    if (choice == 5) {
    	
    	Statement st = conn.createStatement();
    	String q2 = "select * from group3.bill where bill.Customer_ID='"+custID+"'";
    	ResultSet rset1 = st.executeQuery(q2);
    	String bal = "";
    	String late = "";
    	float pay = 0;
    	while (rset1.next ()) {
            bal = rset1.getString("Total");
            late = rset1.getString("LateFee");
    	}
    	System.out.println(" What will you be paying today?");
    	System.out.println(" 1. Late Fees");
    	System.out.println(" 2. Balance");
    	System.out.print("> ");
    	int lob = reader.nextInt();
    	
    	if (lob == 1) {
    		System.out.println(" Your current late fee balance is "+late+" how much would you like to pay?");
    		System.out.print("> ");
    		pay = reader.nextInt();
    		float ilate = Float.parseFloat(late);
    		ilate = ilate - pay;
    		System.out.println(" New late fee balance is " + ilate);
    		String q3 = "UPDATE `group3`.`bill` SET `LateFee` = '" + ilate + "' WHERE (`Customer_ID` = '" + custID + "')";
            PreparedStatement update = conn.prepareStatement(q3);
            update.executeUpdate();
            
    		
    	}
    	else {
    		System.out.println(" Your current balance is "+bal+ " how much would you like to pay off");
    		System.out.print("> ");
    		pay = reader.nextInt();
    		float ilate = Float.parseFloat(bal);
    		ilate = ilate - pay;
    		System.out.println(" New late fee balance is " + ilate);
    		String q3 = "UPDATE `group3`.`bill` SET `Total` = '" + ilate + "' WHERE (`Customer_ID` = '" + custID + "')";
            PreparedStatement update = conn.prepareStatement(q3);
            update.executeUpdate();
    	}
    	
   	
    	
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

 private static void where(boolean next) {
	// TODO Auto-generated method stub
	
}

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












