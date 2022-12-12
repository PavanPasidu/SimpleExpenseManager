package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class presistanceTransactionDAO implements TransactionDAO {
    private DataBaseHelper DBhelper;


    public presistanceTransactionDAO(Context cntxt)
    {
        DBhelper    =   new DataBaseHelper(cntxt);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        DBhelper.AddTransaction(date.toString(),accountNo, amount , expenseType.toString());

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase DBsql = DBhelper.getReadableDatabase();
        String [] col = {"Date","ACC_No", "expence_MODE","Amount"};
        // create cursor
        Cursor cursor1 = DBsql.query("TransactionDetails", col, null,null,null,null,null);

        List transactionsList = new ArrayList<>();

        while(cursor1.moveToNext()) {

            String Date = cursor1.getString(cursor1.getColumnIndexOrThrow("Date"));
            String ACC_No = cursor1.getString(cursor1.getColumnIndexOrThrow("ACC_No"));
            String expence_MODE = cursor1.getString(cursor1.getColumnIndexOrThrow("expence_MODE"));
            double Amount = cursor1.getDouble(cursor1.getColumnIndexOrThrow("Amount"));

            ExpenseType x = null;

            if(expence_MODE.equals("EXPENSE")){
                x = ExpenseType.EXPENSE;
            }
            else{
                x = ExpenseType.INCOME;
            }

            Date date1= null;
            try {
                date1 = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy").parse(Date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Transaction TheAcc = new Transaction(date1, ACC_No, x, Amount);
            transactionsList.add(TheAcc);

        }
        cursor1.close();
        return transactionsList;

    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase DBsql    =   DBhelper.getWritableDatabase();
        long cntn   = DatabaseUtils.queryNumEntries(DBsql, "TransactionDetails");

        List Transaction_Lis ;
        if(limit<=cntn){
            return getAllTransactionLogs();
        }else{
            Transaction_Lis = DBhelper.getPagTransaction_logs(limit,DBsql,cntn);
            return Transaction_Lis;
        }

    }


}
