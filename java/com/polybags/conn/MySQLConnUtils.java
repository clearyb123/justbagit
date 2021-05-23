package com.polybags.conn;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
  
public class MySQLConnUtils {
  
 public static Connection getMySQLConnection()
         throws ClassNotFoundException, SQLException {
     // Note: Change the connection parameters accordingly.
     /*String hostName = "localhost";
     String dbName = "PolybagsQAS";
     String userName = "root";
     String password = "Z1mbabwe";
     String timezone = "";*/
     String hostName = "localhost";
     String dbName = "justbagi_websql";
     String userName = "justbagi_ben";
     String password = "Bellab00!2";
     String timezone = "?seUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
     return getMySQLConnection(hostName, dbName, userName, password,timezone);
 }
  
 public static Connection getMySQLConnection(String hostName, String dbName,
         String userName, String password, String timezone) throws SQLException,
         ClassNotFoundException {
    
     Class.forName("com.mysql.jdbc.Driver");
  
     // URL Connection for MySQL:
     // Example: 
     // jdbc:mysql://localhost:3306/simplehr
     String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName + timezone;
    //jdbc:mysql://localhost/db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC

     Connection conn = DriverManager.getConnection(connectionURL, userName,
             password);
     return conn;
 }
}