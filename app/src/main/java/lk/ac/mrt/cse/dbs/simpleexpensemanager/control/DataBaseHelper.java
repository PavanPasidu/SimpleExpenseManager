package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

// this is a helper class which help to access DB easily
public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context) {

        super(context, "200375F.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase embed_DB) {
        embed_DB.execSQL(
                "CREATE TABLE AccountDetails (" +
                "Account_No TEXT primary key, " +
                        "Bank TEXT, " +
                "Account_Holder TEXT, " +
                "Initial_Balance NUMERIC(10,2))"
        );

        embed_DB.execSQL(
                "CREATE TABLE TransactionDetails (" +
                        "Date TEXT , " +
                        "Account_No TEXT, " +
                        "Expense_Type TEXT, " +
                        "amount REAL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase embed_DB, int i, int i1) {
        embed_DB.execSQL("drop Table if exists AccountDetails");
    }

    // here we insert account details supply by user
    public Boolean insertAccountDetails(String Account_No, String Bank , String Account_Holder, double Initial_Balance)
    {
        SQLiteDatabase embed_DB = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("Account_No", Account_No);
        content.put("Bank", Bank);
        content.put("Account_Holder", Account_Holder);
        content.put("Initial_Balance", Initial_Balance);

        long res_DataBase = embed_DB.insert("AccountDetails", null, content);

        if(res_DataBase == -1)
        {
            return false;
        }else{return true;}
    }

    //  here we update the balance after some transaction
    public Boolean updateAccountDetails(String Account_No, double Initial_Balance)
    {
        SQLiteDatabase embed_DB = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("Initial_Balance", Initial_Balance);
        Cursor cursor1 = embed_DB.rawQuery("select * from AccountDetails where Account_No = ?", new String[]{Account_No});
        if(cursor1.getCount() > 0) {
            long res_DataBase = embed_DB.update("AccountDetails", content, "Account_No=?", new String[]{Account_No});

            if (res_DataBase == -1) {
                return false;
            } else {
                return true;
            }
        }else{return false;}
    }

    // here we remove accounts
    public Boolean deleteAccountDetails(String Account_No)
    {
        SQLiteDatabase embed_DB = this.getWritableDatabase();
        Cursor cursor0 = embed_DB.rawQuery("select * from AccountDetails where Account_No = ?", new String[]{Account_No});

        if(cursor0.getCount() > 0) {
            long res_DataBase = embed_DB.delete("AccountDetails", "Account_No=?", new String[]{Account_No});

            if (res_DataBase == -1) {
                return false;
            } else {
                return true;
            }
        }else{return false;}
    }

//    public Cursor getCursordata()
//    {
//        SQLiteDatabase embed_DB = this.getWritableDatabase();
//        Cursor cursor1 = embed_DB.rawQuery("select * from AccountDetails", null);
//
//        return cursor1;
//    }

    public List<String> createAccountNum_Lis(DataBaseHelper d){
        List ACC_NUM_Lis = new ArrayList<>();
        SQLiteDatabase DB1 = d.getReadableDatabase();
        String [] col = {"Account_No"};
        Cursor cursor1 = DB1.query("Accounts", col,
                null,null,null,null,null);

        while(cursor1.moveToNext()) {
            String itemId = cursor1.getString(
                    cursor1.getColumnIndexOrThrow("accountNo"));
            ACC_NUM_Lis.add(itemId);
        }

        cursor1.close();
        return ACC_NUM_Lis;
    }

    public List<Account> createACC_Lis(DataBaseHelper db_h) {

        List ACC_lis = new ArrayList<>();
        String [] col = {"Account_No","Bank_Name", "AccountHolder_Name","Balance"};
        SQLiteDatabase db = db_h.getReadableDatabase();
        Cursor cursor2 = db.query("Accounts", col, null,null,null,null,null);

        while(cursor2.moveToNext()) {

            String ACC_No = cursor2.getString(cursor2.getColumnIndexOrThrow("accountNo"));
            String Bank_Name = cursor2.getString(cursor2.getColumnIndexOrThrow("bankName"));
            String ACCHolderName = cursor2.getString(cursor2.getColumnIndexOrThrow("accountHolderName"));
            double Balance = cursor2.getDouble(cursor2.getColumnIndexOrThrow("balance"));

            Account myacc = new Account(ACC_No, Bank_Name, ACCHolderName, Balance);

            ACC_lis.add(myacc);
        }

        cursor2.close();
        return ACC_lis;
    }

    public Account setACCDetails( Cursor cursor3){


        Account TheACC = null;

        while(cursor3.moveToNext()) {

            String ACC_No = cursor3.getString(cursor3.getColumnIndexOrThrow("ACC_No"));
            String Bank_Name = cursor3.getString(cursor3.getColumnIndexOrThrow("Bank_Name"));
            String ACCHolderName = cursor3.getString(cursor3.getColumnIndexOrThrow("ACCHolderName"));
            double BalanceOfACC = cursor3.getDouble(cursor3.getColumnIndexOrThrow("BalanceOfACC"));

            TheACC = new Account(ACC_No, Bank_Name, ACCHolderName, BalanceOfACC);
        }

        cursor3.close();
        return TheACC;
    }

    public void AddTransaction(String date, String ACC_No,double AMOUNT, String expnseMode){
        ContentValues contnt    =   new ContentValues();
        SQLiteDatabase DBSql    =   this.getWritableDatabase();

        contnt.put("Date", date);
        contnt.put("ACC_No", ACC_No);
        contnt.put("AMOUNT", AMOUNT);
        contnt.put("MODE", expnseMode);

        DBSql.insert("TransactionDetails",null, contnt);
        DBSql.close();
    }


    public List<Transaction> getPagTransaction_logs(int limit, SQLiteDatabase DBsql, long cntn){

        String [] col = {"Date","ACC_No", "expenceMode","amount"};
        Cursor crsr = DBsql.query("TransactionDetails", col, null,null,null,null,null);

        List transactionsLis = new ArrayList<>();
        int count = 0;

        while(crsr.moveToNext() && count< limit) {

            String Date = crsr.getString(crsr.getColumnIndexOrThrow("Date"));
            String ACC_No = crsr.getString(crsr.getColumnIndexOrThrow("ACC_No"));
            String expenceMode = crsr.getString(crsr.getColumnIndexOrThrow("expenceMode"));
            double Amount = crsr.getDouble(crsr.getColumnIndexOrThrow("Amount"));

            ExpenseType x = null;

            if (expenceMode.equals("EXPENSE")) {
                x = ExpenseType.EXPENSE;
            } else {
                x = ExpenseType.INCOME;
            }

            Date date1 = null;
            try {
                date1 = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy").parse(Date);

            } catch (ParseException e) {

                e.printStackTrace();
            }

            Transaction TheACC = new Transaction(date1, ACC_No, x, Amount);
            transactionsLis.add(TheACC);
            count++;
        }
        crsr.close();
        return transactionsLis;

    }


}
