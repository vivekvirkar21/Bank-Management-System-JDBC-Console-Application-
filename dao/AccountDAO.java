package dao;

import java.util.List;

import pojo.Account;

public interface AccountDAO {
	public List<Account> getAccountinfo();
	
	public List<Account> getByAccountno(long accountno);
	
	public void createAccount(Account account);
	
	public double fetchBalance(long accountno);
	
	public void withdraw(long accountno,double amount);
	
	public void deposit(long accountno,double amount);
	
	public void deleteAccount(long accountno,int pin);
}
