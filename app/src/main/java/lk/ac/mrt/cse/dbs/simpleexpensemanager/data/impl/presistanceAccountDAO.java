package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class presistanceAccountDAO implements AccountDAO{
    //private final Map<String, Account> accounts;
    // lets replace Map with a db
    DataBaseHelper DBhelp;

    public presistanceAccountDAO(Context cntxt) {
        DBhelp  =   new DataBaseHelper(cntxt);
        //we use data base rather than a Array
        // this.accounts = new HashMap<>();
    }

    @Override
    public List<String> getAccountNumbersList() {
        List ACC_NUM_Lis = DBhelp.createAccountNum_Lis(DBhelp);

        return ACC_NUM_Lis;
    }

    @Override
    public List<Account> getAccountsList() {
        List ACCLis = DBhelp.createACC_Lis(DBhelp);
        return ACCLis;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase DBh = DBhelp.getReadableDatabase();
        String [] col = {"ACC_No","Bank_Name", "ACCHolderName","BalanceOfACC"};
        String[] arg = {accountNo};
        Cursor c = DBh.query("AccountDetails", col, "ACC_No = ?",arg,null,null,null);

        Account TheACC = DBhelp.setACCDetails(c);
        return TheACC;
    }

    @Override
    public void addAccount(Account account) {
        Boolean inserted = DBhelp.insertAccountDetails(account.getAccountNo(),account.getBankName(),account.getAccountHolderName(),account.getBalance());
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        Boolean removed = DBhelp.deleteAccountDetails(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account TempACC = getAccount(accountNo);

        double newBalance = 0;

        switch (expenseType) {
            case EXPENSE:
                newBalance = TempACC.getBalance() - amount;
                break;
            case INCOME:
                newBalance = TempACC.getBalance() + amount;
                break;
        }
        DataBaseHelper dbHelp = new DataBaseHelper(null);
        Boolean updated = DBhelp.updateAccountDetails(accountNo , newBalance);


    }

}
