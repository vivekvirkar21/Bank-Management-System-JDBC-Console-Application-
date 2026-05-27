package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dao.AccountDAO;
import pojo.Account;

public class AccountMain {
	static Scanner sc=new Scanner(System.in);
	AccountDAO service=new AccountService();
			
		public static void main(String[] args) {
		AccountMain accountMain=new AccountMain();
		int a;
		do {
			System.out.println("-------Bank Management System-------");
			System.out.println("1. Add Account");
			System.out.println("2. Get All Account info");
			System.out.println("3. Get info. by Account no.");
			System.out.println("4. Fetch Balance");
			System.out.println("5. Deposite");
			System.out.println("6. Withdraw");
			System.out.println("7. delete/close Account");
			System.out.println("8. Exit");
			
			System.out.println("\nEnter Choice: ");
			a=sc.nextInt();
			
			switch(a) {
				case 1:
					accountMain.createAccount();
				break;
				case 2:
					accountMain.getAll();
				break;
				case 3:
					accountMain.getByAccount();
				break;
				case 4:
					System.out.println("enter Acc.no.: ");
					long acc=sc.nextLong();
					accountMain.fetchBalance(acc);
				break;
				case 5:
					accountMain.deposit();
				break;
				case 6:
					accountMain.withdraw();
				break;
				case 7:
					accountMain.deleteAccount();
				break;
				case 8:
					System.out.println("Thank's for Use.");
				break;
				default:
					System.out.println("enter valid choice!");
				break;
			}
		}while(a!=8);
		
	}
	
	public void withdraw() {
		System.out.println("enter Acc.no.:");
		long acc=sc.nextLong();
		System.out.println("amount:");
		double amount=sc.nextDouble();
		service.withdraw(acc, amount);
		fetchBalance(acc);
	}
		
	public void deposit() {
		System.out.println("enter Acc.no.:");
		long acc=sc.nextLong();
		System.out.println("amount:");
		double amount=sc.nextDouble();
		service.deposit(acc, amount);
		fetchBalance(acc);
	}
	
	public void fetchBalance(long acc) {
		double balance=service.fetchBalance(acc);
		System.out.println("Current Balance : "+balance);
	}
	
	public void getByAccount() {
		List<Account>Accounts=new ArrayList<Account>();
		System.out.println("enter Account no.: ");
		long acc=sc.nextLong();
		Accounts=service.getByAccountno(acc);
		for(Account a:Accounts) {
			System.out.println(a);
		}
		System.out.println();
	}
	
	public void getAll() {
		List<Account>Accounts=new ArrayList<Account>();
		Accounts=service.getAccountinfo();
		for(Account a:Accounts) {
			System.out.println(a);
		}
		System.out.println();
	}
	
	public void createAccount() {
		Account account=new Account();
		System.out.println("-------enter data-------");
		System.out.println("Acc.no: ");
		account.setAccountNo(sc.nextLong());
		sc.nextLine();
		System.out.println("Name: ");
		account.setName(sc.nextLine());
		System.out.println("Amount to Deposite: ");
		account.setBalance(sc.nextDouble());
		System.out.println("Pin: ");
		account.setPin(sc.nextInt());
		service.createAccount(account);
	}
	
	public void deleteAccount() {
		System.out.println("Account info to delete");
		System.out.println("Acc.no:");
		long Acc=sc.nextLong();
		sc.nextLine();
		System.out.println("Pin:");
		int pin=sc.nextInt();
		service.deleteAccount(Acc, pin);
	}
}
