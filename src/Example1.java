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
       throws SQLException, ClassNotFoundException, SQLSyntaxErrorException
  {
	//Properties prop = new Properties();
	//InputStream input = null;
	  
    //System.out.print("userid: ");

	System.out.println("");
	System.out.println("|---------------------|");
	System.out.println("|                     |");
	System.out.println("| Welcome To UNFBooks |");
	System.out.println("|                     |");
	System.out.println("|---------------------|");
	System.out.println("\n");
	  
	  
	Scanner reader = new Scanner(System.in); 
	  
	System.out.println(" Please enter username below");
    String uid = reader.nextLine();
    
    

    //System.out.print("password: ");
    System.out.println(" Please enter password below");
    String pword = reader.nextLine();
    
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
        
//        System.out.println("cust " + custID);
//        System.out.println("admin " + custf);
    }
	
	if (custID.equals("")) {
		System.out.println("");
		System.out.println(" Please enter a correct user id");
		System.out.println("");
		System.exit(0);
	}
	// END LOGIN
	
	while (true) {
    
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
    
    System.out.println(" Enter -1 to exit the program");
    System.out.println("");
    
    System.out.print("> "); 
    while(!reader.hasNextInt()) {
    	System.out.println("You didn't enter a valid number, please try again");
    	reader.next();
    }
    int choice = reader.nextInt();
    System.out.println("");

    
    // Load the Oracle JDBC driver
    //DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

    // Connect to the database
   // Class.forName("com.mysql.jdbc.Driver");  
   
    
    if (choice == 1)
    {
    	System.out.println(" Enter the name of the book you are searching for\n");
    	System.out.print("> ");
    	String title = reader.next();
    	title += reader.nextLine();
    	Statement stmt1 = conn.createStatement();
    	String q2 = "select * from group3.isbn, group3.book where isbn.Title='" + title+"' and isbn.isbn = book.isbn";
        ResultSet rset1 = stmt1.executeQuery(q2);
        System.out.println("");
        String isbn = "";
        
        while (rset1.next ()) {
          isbn = rset1.getString("ISBN");
          String author = rset1.getString("AuthorName");
          String category = rset1.getString("Category");
          String publisher = rset1.getString("Publisher");
          String format = rset1.getString("Format");
          String condition = rset1.getString("BookCondition");
          boolean rent = rset1.getBoolean("Rent");
          boolean buy = rset1.getBoolean("Buy");
          float price = rset1.getFloat("Price");
          System.out.println("ISBN: " + isbn + "\n" 
                                  + "Author: " + author + "\n" 
                                  + "Category: " + category + "\n"
                                  + "Publisher: " + publisher + "\n"
                                  + "Price: $" + price + "\n"
                                  + "BookCondition: " + condition + "\n"
                                  + "Format: " + format + "\n"
                                  + "Rentable: " + rent + "\n"
                                  + "Buyable: " + buy + "\n");
      }
      
        if (isbn.equals("")) {
        	System.out.println(" Nothing was found returning to main menu");
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
    	
    	if (isbn.equals("")) {
    		System.out.println("Book not located, returning to menu");
    		continue;
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
    	
    	System.out.println("");
    	
    	
    	
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
    	
    	String numCopies = "";
    	Statement st4 = conn.createStatement();
    	String q6 = "select * from group3.book where Book_ID='"+bookid+"'";
    	ResultSet rset5 = st4.executeQuery(q6);
    	String bookprice = "";
    	while (rset5.next()) {
    		bookprice = rset5.getString("Price");
    		numCopies = rset5.getString("NumCopies");
    	}
    	
    	Statement st5 = conn.createStatement();
    	String q7 = "select * from group3.bill where bill.Customer_ID='"+custID+"'";
    	ResultSet rset6 = st5.executeQuery(q7);
    	String bal = "";
    	while (rset6.next ()) {
            bal = rset6.getString("Total");
            
    	}
    	System.out.println(" Your current total is $" + bal);
    	
    	float taxval = Float.parseFloat(bookprice) * Float.parseFloat(".07");
    	
    	float newtotal = Float.parseFloat(bookprice) + taxval;
    	
    	
    	// Update bill section
    	int maxi = Integer.parseInt(maxv);
    	PreparedStatement ps1 = conn.prepareStatement("INSERT INTO group3.bill (Tax, LateFee, Total, Customer_ID, Transaction_ID) "
    			+ "VALUES("+taxval+" , null,"+newtotal+", '"+custID +"' ,'" + maxi + "')");
    	
    	int ncopies = Integer.parseInt(numCopies) - 1;
    	PreparedStatement ps2 = conn.prepareStatement("update group3.book set NumCopies = "+ncopies+" where Book_ID = "+bookid);
    	
    	
    	ps1.executeUpdate();
    	ps2.executeUpdate();
    	
   
    	System.out.println(" Transaction Sucesssful, your transaction number for this bill is " +maxi);

    	//st.setTimestamp(1, date);
    	
    	//int insert = 
    	
    	
    	
    } // end choice 2
 
    
    if(choice == 3) {
        System.out.println("Please enter the transaction ID from your book recipet");
        System.out.println("");
        System.out.print("> ");
        
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
        System.out.println("");
        
        if(sqlDate.compareTo(endDate) > 0) {
            System.out.println("This is late!");
            //need to add late fee here
        }
    }

    if (choice == 4) {
    	
    	Statement st = conn.createStatement();
    	String q2 = "select sum(Total) from group3.bill where bill.Customer_ID='"+custID+"'";
    	ResultSet rset1 = st.executeQuery(q2);
    	String bal = "";
    	while (rset1.next ()) {
            bal = rset1.getString("sum(Total)");
    	}
    	System.out.println(" Your current total is $" + bal);
    	System.out.println("");
    }
    
    if (choice == 5) {
    	
    	System.out.println(" What is the transaction ID of what you will be paying");
    	System.out.print("> ");
    	int tranid = reader.nextInt();
    	
    	Statement st = conn.createStatement();
    	String q2 = "select * from group3.bill where bill.Customer_ID='"+custID+"' and bill.Transaction_ID="+tranid;
    	ResultSet rset1 = st.executeQuery(q2);
    	String bal = "";
    	String late = "";
    	float pay = 0;
    	while (rset1.next ()) {
            bal = rset1.getString("Total");
            late = rset1.getString("LateFee");
    	}
    	
    	System.out.println("");
    	System.out.println(" What will you be paying today?");
    	System.out.println(" 1. Late Fees");
    	System.out.println(" 2. Balance");
    	System.out.println("");
    	System.out.print("> ");
    	int lob = reader.nextInt();
    	
    	if (lob == 1) {
    		System.out.println(" Your current late fee balance is "+late+" how much would you like to pay?");
    		System.out.print("> ");
    		pay = reader.nextFloat();
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
    		pay = reader.nextFloat();
    		float ilate = Float.parseFloat(bal);
    		ilate = ilate - pay;
    		System.out.println(" New balance is " + ilate);
    		String q3 = "UPDATE `group3`.`bill` SET `Total` = '" + ilate + "' WHERE (`Customer_ID` = '" + custID + "')";
            PreparedStatement update = conn.prepareStatement(q3);
            update.executeUpdate();
    	}
    	
    }
    
    else if (choice == 6) {
    	
    	if (hasAdmin) {
    		
    		System.out.println(" Enter the name of the book you are searching for\n");
        	System.out.print("> ");
        	String title = reader.next();
        	title += reader.nextLine();
        	Statement stmt1 = conn.createStatement();
        	String q2 = "select * from group3.isbn, group3.book where isbn.Title='" + title+"' and isbn.isbn = book.isbn";
            ResultSet rset1 = stmt1.executeQuery(q2);
            System.out.println("");
            String isbn = "";
            
            while (rset1.next ()) {
              isbn = rset1.getString("ISBN");
              String author = rset1.getString("AuthorName");
              String category = rset1.getString("Category");
              String publisher = rset1.getString("Publisher");
              String format = rset1.getString("Format");
              String condition = rset1.getString("BookCondition");
              boolean rent = rset1.getBoolean("Rent");
              boolean buy = rset1.getBoolean("Buy");
              float price = rset1.getFloat("Price");
              System.out.println("ISBN: " + isbn + "\n" 
                                      + "Author: " + author + "\n" 
                                      + "Category: " + category + "\n"
                                      + "Publisher: " + publisher + "\n"
                                      + "Price: $" + price + "\n"
                                      + "BookCondition: " + condition + "\n"
                                      + "Format: " + format + "\n"
                                      + "Rentable: " + rent + "\n"
                                      + "Buyable: " + buy + "\n");
          }
          
            if (isbn.equals("")) {
            	System.out.println(" Nothing was found returning to main menu");
            }
          
    	}
    	else {
    		System.out.println(" You do not have access to this option");
    		//break;	
    	}
    	
    }
    
    else if (choice == 7) {
    	
    	if (hasAdmin) {
    		
    		System.out.println(" Which of the following actions would you like to perform");
    		System.out.println("1. Add Title");
    		System.out.println("2. Delete Title");
    		System.out.println("3. Update Title");
    		System.out.println(" Select any other number to quit");
    		System.out.println("");
    		System.out.print("> ");
    		int achoice = reader.nextInt();
    		
    		if (achoice == 1) {
    	
    			Scanner reader1 = new Scanner(System.in);
    			//System.out.print("> ");
    			System.out.println(" Enter the name of the book to be added");
    			String bookkkkk = reader1.nextLine();

    			//System.out.println(book);
    			
//    			try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
    			
    			//System.out.print("> ");
    			
    			System.out.println(" Enter the authorname");
    			String author = reader1.nextLine();
    			
    			
    			//System.out.print("> ");
    			System.out.println(" Enter the category");
    			String cat = reader1.nextLine();
    			
    			
    			//System.out.print("> ");
    			System.out.println(" Enter the publisher");
    			String pub = reader1.nextLine();
    			
    			
    			//System.out.print("> ");
    			System.out.println(" Enter the format");
    			String forma = reader1.nextLine();
    			
    			
    			//System.out.print("> ");
    			System.out.println(" Enter the ISBN");
    			String isbn = reader1.nextLine();
    			
    			
    			System.out.println(" Enter the condition");
    			String condition = reader1.nextLine();
    			
    			
    			//System.out.print("> ");
    			System.out.println(" Enter the number of copies");
    			int copies = reader1.nextInt();
    			
    			
    			//System.out.print("> ");
    			System.out.println(" Enter the Price");
    			float price = reader1.nextFloat();
    			
    			
    			
    			
    			//System.out.print("> ");
    			System.out.println(" Is the book rentable? Type 1 for yes or 2 for no");
    			short rentable = reader1.nextShort();
    			
    			
    			//System.out.print("> ");
    			System.out.println(" Is the book Buyable? Type 1 for yes or 2 for no");
    			short buyable = reader1.nextShort();
    			
    			
    			//System.out.print("> ");
    			System.out.println(" Is the book a new release? Type 1 for yes or 2 for no");
    			short newrelease = reader1.nextShort();
    			
    			
    			Statement st3 = conn.createStatement();
    	    	String q5 = "select MAX(Book_ID) from group3.book";
    	    	ResultSet rset4 = st3.executeQuery(q5);
    	    	String maxv = "";
    	    	while (rset4.next()) {
    	    		maxv = rset4.getString("max(Book_ID)");
    	    	}
    			int imax = Integer.parseInt(maxv) + 1;
    			
    			PreparedStatement ps1 = conn.prepareStatement("INSERT INTO group3.book (Book_ID, ISBN, NumCopies, Price, BookCondition, Rent, Buy, NewRelease) "
    	    			+ "VALUES("+imax+", '"+isbn+"', "+copies+", "+price+", '"+condition+"', "+rentable+", "+buyable+", "+newrelease+")") ;
    	    	
    			PreparedStatement ps2 = conn.prepareStatement("INSERT INTO group3.isbn (ISBN, Title, AuthorName, Category, Publisher, Format) "
    	    			+ "VALUES('"+isbn+"', '"+bookkkkk+"', '"+author+"', '"+cat+"', '"+pub+"', '"+forma+"') ") ;
    	    	
    			ps2.executeUpdate();
    	    	ps1.executeUpdate();
    	    	
    			
    			
    		}
    		else if (achoice == 2) {
    			System.out.println(" Enter the name of the book you would like to delete");
    			System.out.print("> ");
    			String delbook = reader.nextLine();
    			
    			Statement st = conn.createStatement();
    	    	String q2 = "select * from group3.isbn where isbn.Title='"+delbook+"'";
    	    	ResultSet rset1 = st.executeQuery(q2);
    	    	String nisbn= "";
    	    	while (rset1.next ()) {
    	            nisbn = rset1.getString("ISBN");
    	    	}
    	    	
    	    	if (nisbn.equals("")) {
    	    		System.out.println("Book not located, returning to menu");
    	    		continue;
    	    	}
    			
    			PreparedStatement p1 = conn.prepareStatement("delete from group3.isbn where isbn.Title='"+delbook+"'");
    			PreparedStatement p2 = conn.prepareStatement("delete from group3.book where book.isbn='"+nisbn+"'");
    			
    			p1.executeUpdate();
    			p2.executeUpdate();
    			
    		}
    		else if (achoice == 3) {
    			
    			Scanner reader2 = new Scanner(System.in);
    			
    			System.out.println(" Enter the ISBN of the Title you would like to update");
    			System.out.print("");
    			System.out.print("> ");
    			String isbnn = reader2.next();
    			System.out.println(" What do you want to update about this ISBN ");
    			System.out.println("1. Title");
    			System.out.println("2. Authorname");
    			System.out.println("3. Category");
    			System.out.println("4. Publisher");
    			System.out.println("5. Format");
    			System.out.println("6. Number of Copies");
    			System.out.println("7. Price");
    			System.out.println("8. Condition");
    			System.out.println("9. Rentable Status");
    			System.out.println("10. Buyable Status");
    			System.out.println("11. New Release Status");
    			System.out.println(" Enter a number greater than 11 to cancel");
    			int choiceu = reader2.nextInt();
    			
    			String temp = "";
    			String query = "";
    			if (choiceu == 1) temp = "Title";
    			if (choiceu == 2) temp = "AuthorName";
    			if (choiceu == 3) temp = "Category";
    			if (choiceu == 4) temp = "Publisher";
    			if (choiceu == 5) temp = "Format";
    			if (choiceu == 6) temp = "NumCopies";
    			if (choiceu == 7) temp = "Price";
    			if (choiceu == 8) temp = "BookCondition";
    			if (choiceu == 9) temp = "Rent";
    			if (choiceu == 10) temp = "Buy";
    			if (choiceu == 11) temp = "NewRelease";
    			
    		
    			if (choiceu >= 1 && choiceu <= 6) {
    				System.out.println(" Enter what you want the updated value to be");
    				String newval = reader2.nextLine();
    				query = "update group3.isbn set "+temp+"='"+newval+"' where isbn.ISBN='"+isbnn+"'";
    			}
    			else if (choiceu > 6 && choiceu < 11) {
    				System.out.println(" Enter what you want the updated value to be");
    				int newval = reader2.nextInt();
    				query = "update group3.isbn set "+temp+"="+newval+"where isbn.ISBN='"+isbnn+"'";
    			}
    			else {
    				System.out.println("Cancelling update...");
    				//break;
    			}
    		
    			PreparedStatement st1 = conn.prepareStatement(query);
    			
    			st1.executeUpdate();
    			
    			st1.close();
    			
    			
    		}
    		else {
    			System.out.println("No valid choice entered, returning to main menu");
    			//break;
    		}
    		
    	}
    	else {
    		System.out.println(" You do not have access to this option");
    		//break;
    	}
    	
    }
    
    if (choice == 8) {
    	
    	if (hasAdmin) {
    		
    		System.out.println(" Which of the following features would you like to access");
    		System.out.println("1. Check user balance");
    		System.out.println("2. Apply late fees");
    		System.out.println("");
    		System.out.print("> ");
    		int choicen = reader.nextInt();
    		
    		if (choicen == 1) {
    			System.out.println(" Enter the Customer ID of the user who's balance you want to check");
    			System.out.print("> ");
    			String usr = reader.next();
    			System.out.println(" ");
    			Statement st = conn.createStatement();
    	    	String q2 = "select sum(Total) from group3.bill where bill.Customer_ID="+usr;
    	    	ResultSet rset1 = st.executeQuery(q2);
    	    	String bal = "";
    	    	while (rset1.next ()) {
    	            bal = rset1.getString("sum(Total)");
    	    	}
    	    	System.out.println(" This users current total is $" + bal);
    		}
    		else if (choicen == 2) {
    			System.out.println(" Enter the Customer ID in which you will apply late fee's too");
    			System.out.print("> ");
    			int usr = reader.nextInt();
    			System.out.println(" Enter the transaction ID in which the late fee's will be applied");
    			System.out.print("> ");
    			int late = reader.nextInt();
    			System.out.println(" ");
    			Statement st = conn.createStatement();
    	    	String q2 = "select * from group3.bill where bill.Customer_ID="+usr+" and bill.Transaction_ID="+late;
    	    	ResultSet rset1 = st.executeQuery(q2);
    	    	String bal = "";
    	    	while (rset1.next ()) {
    	            bal = rset1.getString("LateFee");
    	    	}
    	    	float latefee = Float.parseFloat(bal);
    	    	System.out.println(" This users current late fee total is $" + latefee +" how much shall be added?");
    	    	float nlate = reader.nextFloat();
    	    	latefee = latefee + nlate;
    	    	System.out.println(" users new late fee is " + latefee);
    	    	PreparedStatement p1 = conn.prepareStatement("update group3.bill set LateFee="+latefee+" where bill.Customer_ID="+usr+" and bill.Transaction_ID="+late);
    	    	p1.executeUpdate();
    		}
    	    	
    		
    	}
    	else {
    		System.out.println(" You do not have access to this option");
    		//break;
    	}
    }
    
    if (choice == 9) {
    	
    	if (hasAdmin) {
    		System.out.println(" What would you like to generate reports by? ");
    		System.out.println("1. Title");
    		System.out.println("2. Genre");
    		System.out.print("> ");
    		int cu = reader.nextInt();
    		
    		if (cu == 1) {
    			
    			Statement st4 = conn.createStatement();
    	    	String q6 = "select distinct isbn.Title, isbn.Category, payment.Payment_ID, transaction.BoughtOrRent, book.Price\n" + 
    	    			"from            isbn, book, payment, transaction\n" + 
    	    			"where            isbn.ISBN = book.ISBN\n" + 
    	    			"order by        isbn.Title, isbn.Category;";
    	    	ResultSet rset5 = st4.executeQuery(q6);
    	    	String bookprice = "";
    	    	while (rset5.next()) {
    	    		System.out.print(rset5.getString("Title") + ", ");
    	    		System.out.print(rset5.getString("Category") + ", ");
    	    		System.out.print(rset5.getString("Payment_ID") + ", ");
    	    		System.out.print(rset5.getString("BoughtOrRent") + ", ");
    	    		System.out.print(rset5.getString("Price")+" \n");
    	    	}
    	    	
    		}
    		
    		if (cu == 2) {
    			
    			Statement st4 = conn.createStatement();
    	    	String q6 = "select distinct    transaction.TransDate, isbn.Title, book.NumCopies, book.Price, book.Rent, book.Buy\n" + 
    	    			"from             transaction, isbn, book\n" + 
    	    			"where            isbn.ISBN = book.ISBN\n" + 
    	    			"order by        transaction.TransDate;";
    	    	ResultSet rset5 = st4.executeQuery(q6);
    	    	String bookprice = "";
    	    	while (rset5.next()) {
    	    		System.out.print(rset5.getString("TransDate") + ", ");
    	    		System.out.print(rset5.getString("Title") + ", ");
    	    		System.out.print(rset5.getString("NumCopies") + ", ");
    	    		System.out.print(rset5.getString("Price") + ", ");
    	    		System.out.print(rset5.getString("Rent")+", ");
    	    		System.out.print(rset5.getString("Buy")+" \n");
    	    	}
    		}
    		
    	}
    	else {
    		System.out.println(" You do not have access to this option");
    		//break;
    	}
    	
    }
    
    if (choice < 0) {
    	
    	if (choice == -1) {
    		System.out.println(" -1 Entered.. Exiting program");
    		System.exit(0);
    	}
    	else {
    		System.out.println(" Negative number entered, Would you like to exit the program?");
    		System.out.println("1. Yes");
    		System.out.println("2. No");
    		int rx = reader.nextInt();
    		
    		if(rx == 1) {
    			System.exit(0);
    		}
    		
    	}    	
    }
	}
    
    
  
    
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












