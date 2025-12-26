package com.aditya;

import java.util.*;
import java.sql.*;
import com.aditya.*;

public class EnquireTransaction {
	private String UName, StartDate, EndDate;
	private Vector<String> C_Names;
	private Vector<Vector<Object>> Rows;
	public Vector[] FetchTransaction(String Uname,String StartDate,String EndDate) {
		boolean done = !StartDate.equals("") && !EndDate.equals("");
		try {
			Vector<String> C_Names = new Vector<>();
			Vector<Vector<Object>> Rows = new Vector<>();
			if (done)
			{
			  C_Names.addElement("Trans_Number");
			  C_Names.addElement("Trans_Amount");
			  C_Names.addElement("Trans_Time");
			  C_Names.addElement("Trans_Date");
			  C_Names.addElement("From_Acc");
			  C_Names.addElement("To_Acc");
			  C_Names.addElement("Trans_Type");

			  DBConnection ToDB = new DBConnection();
			  Connection DBConn = ToDB.openConn();
			  String sql = "SELECT * FROM Transactions WHERE CustomerID = ? AND TransactionDate BETWEEN ? AND ?";
			  PreparedStatement stmt = DBConn.prepareStatement(sql);
			  stmt.setString(1, Uname);
			  stmt.setString(2, StartDate);
			  stmt.setString(3, EndDate);
	          ResultSet rslt = stmt.executeQuery();
	          ResultSetMetaData rsmd = rslt.getMetaData();
	          while(rslt.next())
	          {
				  DBConnection DC = new DBConnection();
				  Rows.addElement(DC.getNextRow(rslt,rsmd));
				  int CN = rsmd.getColumnCount();
				  for (int i = 0; i<=CN; i++)
				  {
					  C_Names.addElement(rsmd.getColumnName(CN));
				  }
		  	  }
		  	  stmt.close();
		   	  ToDB.closeConn();
			}
		}

	 	 	catch(java.sql.SQLException e)
		 	{done = false;
		 	 System.out.println("SQLException: " + e);
		 	 while (e != null)
		 	 {System.out.println("SQLState: " + e.getSQLState());
		 		System.out.println("Message: " + e.getMessage());
		 		System.out.println("Vendor: " + e.getErrorCode());
		 		e = e.getNextException();
		 		System.out.println("");
		 	 }
		   	}
		 	catch (java.lang.Exception e)
		 	{done = false;
		 	 System.out.println("Exception: " + e);
		 	 e.printStackTrace ();
			}
		Vector[] TransInfo = new Vector[2];
		TransInfo[0] = C_Names;
		TransInfo[1] = Rows;
		return TransInfo;
}
}



