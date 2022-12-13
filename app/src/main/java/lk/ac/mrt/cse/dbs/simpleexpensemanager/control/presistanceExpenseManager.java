package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.presistanceAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.presistanceTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class presistanceExpenseManager extends ExpenseManager{

    Context contxt;
    // constructor
    public presistanceExpenseManager(Context context) {

        contxt=context;
        setup();
    }

    @Override
    public void setup() {
        /*** Setup the persistent storage implementation ***/
        //DataBaseHelper help = new DataBaseHelper(contxt);
        //SQLiteDatabase sqLItedatabase = help.getWritableDatabase();

        TransactionDAO presistanceTransactionDAO = new presistanceTransactionDAO(contxt);
        setTransactionsDAO(presistanceTransactionDAO);

        AccountDAO presistanceAccountDAO = new presistanceAccountDAO(contxt);
        setAccountsDAO(presistanceAccountDAO);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);


        /*** End ***/
    }
}
