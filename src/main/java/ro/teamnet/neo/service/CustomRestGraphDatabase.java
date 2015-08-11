package ro.teamnet.neo.service;

import org.neo4j.graphdb.Transaction;
import org.neo4j.rest.graphdb.BatchTransaction;
import org.neo4j.rest.graphdb.BatchTransactionManager;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;

import javax.transaction.TransactionManager;

/**
 * Created by Oana.Mihai on 8/11/2015.
 */
public class CustomRestGraphDatabase extends SpringRestGraphDatabase {
    public CustomRestGraphDatabase(String uri, String user, String password) {
        super(uri, user, password);
    }

    public Transaction beginTx() {
        return getRestAPI().beginTx();
    }


    public TransactionManager getTxManager() {
        return new BatchTransactionManager(getRestAPI());
    }


    public TransactionManager getTransactionManager() {
        return new BatchTransactionManager(getRestAPI());
    }


    public boolean transactionIsRunning() {
        return BatchTransaction.current() != null;
    }

    static {
        System.setProperty("org.neo4j.rest.batch_transaction", "true");
    }
}
