package service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import dao.AccountDAO;
import pojo.Account;

public class AccountService implements AccountDAO{
	private static String driverpath="";
	private static String databaseurl="";
	private static String databasename="";
	private static String username="";
	private static String password="";
	private static String file_name="db_info.properties";
	private Connection connection=null;
	private PreparedStatement preparedStatement=null;
	private Statement statement=null;
	private ResultSet resultSet=null;
	
	@Override
	public void createAccount(Account account) {
		String query="insert into account(accountNo,name,balance,pin)values(?,?,?,?)";
		try {
			openConnection();
			int row=0;
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setLong(1, account.getAccountNo());
			preparedStatement.setString(2, account.getName());
			preparedStatement.setDouble(3, account.getBalance());
			preparedStatement.setInt(4, account.getPin());
			row=preparedStatement.executeUpdate();
			if(row>0) {
				System.out.println("Account Created Successfully...");
			}else {
				System.out.println("failed to create Account..");
			}
		}catch(SQLException e) {
			System.out.println(e);
		}finally {
			closeConnection();
		}
	}
	
	@Override
	public List<Account> getAccountinfo() {
		List<Account>Accounts=new ArrayList<Account>();
		String query="select * from account";
		try {
			openConnection();
			preparedStatement=connection.prepareStatement(query);
			resultSet=preparedStatement.executeQuery();
			while(resultSet.next()) {
				Account account=new Account();
				account.setAccountNo(resultSet.getLong(1));
				account.setName(resultSet.getString(2));
				account.setBalance(resultSet.getDouble(3));
				account.setPin(resultSet.getInt(4));
				Accounts.add(account);
			}
		}catch(SQLException e) {
			System.out.println(e);
		}finally {
			closeConnection();
		}
		return Accounts;
	}
	
	@Override
	public List<Account> getByAccountno(long accountno) {
		List<Account>Accounts=new ArrayList<Account>();
		String query="select * from account where accountNo=?";
		try {
			openConnection();
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setLong(1, accountno);
			resultSet=preparedStatement.executeQuery();
			if(resultSet.next()){	
				Account account=new Account();
				account.setAccountNo(resultSet.getLong(1));
				account.setName(resultSet.getString(2));
				account.setBalance(resultSet.getDouble(3));
				account.setPin(resultSet.getInt(4));
				Accounts.add(account);	
			}else {
				System.out.println("Account not exits..");
			}
		}catch(SQLException e) {
			System.out.println(e);
		}finally {
			closeConnection();
		}
		return Accounts;
	}
	
	@Override
	public void deposit(long accountno, double amount) {
		String query="update account set balance=? where accountNo=?";
		double balance=fetchBalance(accountno);
		balance+=amount;
		int row=0;
		if(amount>0) {
			try {
				openConnection();
				preparedStatement=connection.prepareStatement(query);
				preparedStatement.setDouble(1, balance);
				preparedStatement.setLong(2, accountno);
				row=preparedStatement.executeUpdate();
				if(row>0) {
					System.out.println("Deposite Successfull..");
				}else {
					System.out.println("failed to deposite...");
				}
			}catch(SQLException e) {
				System.out.println(e);
			}finally {
				closeConnection();
			}
		}else {
			System.out.println("enter valid amount...");
		}
	}
	
	@Override
	public void withdraw(long accountno, double amount) {
		double balance=fetchBalance(accountno);
		String query="update account set balance=? where accountNo=?";
		int row=0;
		if((balance-amount)>500 && amount>0) {
			balance-=amount;
			try {
				openConnection();
				preparedStatement=connection.prepareStatement(query);
				preparedStatement.setDouble(1, balance);
				preparedStatement.setLong(2, accountno);
				row=preparedStatement.executeUpdate();
				if(row>0){
					System.out.println("Withdraw Successfull...");
				}else {
					System.out.println("Failed to withdraw..");
				}
			}catch(SQLException e) {
				System.out.println(e);
			}finally {
				closeConnection();
			}
		}else if(amount<=0) {
			System.out.println("enter valid amount..");
		}else {
			System.out.println("Insuficient balance,Cannot Proceed Maintain minimum balance of 500.");
		}
	}
	
	@Override
	public void deleteAccount(long accountno, int pin) {
		String query="delete from account where accountNo=? and pin=?";
		try {
			openConnection();
			int row=0;
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setLong(1, accountno);
			preparedStatement.setInt(2, pin);
			row=preparedStatement.executeUpdate();
			if(row>0) {
				System.out.println("Account deleted Successfully...");
			}else {
				System.out.println("failed to delete..");
			}
		}catch(SQLException e) {
			System.out.println(e);
		}finally {
			closeConnection();
		}
	}
	
	@Override
	public double fetchBalance(long accountno) {
		double balance=0;
		String query="select balance from account where accountNo=?";
		try {
			openConnection();
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setLong(1, accountno);
			resultSet=preparedStatement.executeQuery();
			if(resultSet.next()) {
				balance=resultSet.getDouble(1);
			}else {
				System.out.println("Account not exits..");
			}
		}catch(SQLException e) {
			System.out.println(e);
		}finally {
			closeConnection();
		}
		return balance;
	}
	
	public void closeConnection() {
		try {
			if(resultSet!=null) {
				resultSet.close();
				resultSet=null;
			}
			if(statement!=null) {
				statement.close();
				statement=null;
			}
			if(preparedStatement!=null) {
				preparedStatement.close();
				preparedStatement=null;
			}
			if(connection!=null) {
				connection.close();
				connection=null;
			}
//			System.out.println("connection closed...");
		}catch(SQLException e) {
			System.out.println(e);
		}
	}
	public void openConnection() {
		readDB();
		try {
			Class.forName(driverpath);
			connection= DriverManager.getConnection(databaseurl+databasename, username, password);
//			System.out.println("connection established...");
		}catch(ClassNotFoundException e) {
			System.out.println(e);
		}catch(SQLException e) {
			System.out.println(e);
		}
	}
	public void readDB() {
		Properties properties=new Properties();
		FileInputStream fileInputStream=null;
		try {
			fileInputStream=new FileInputStream(file_name);
			properties.load(fileInputStream);
			driverpath=properties.getProperty("driverpath");
			databaseurl=properties.getProperty("databaseurl");
			databasename=properties.getProperty("databasename");
			username=properties.getProperty("username");
			password=properties.getProperty("password");
		}catch(FileNotFoundException e) {
			System.out.println(e);
		}catch(IOException e) {
			System.out.println(e);
		}
		
	}
}
