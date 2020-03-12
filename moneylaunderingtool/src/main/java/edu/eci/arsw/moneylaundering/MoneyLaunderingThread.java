package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MoneyLaunderingThread extends Thread{
    private List<File> transactionFiles;
    private MoneyLaundering moneyLaundering;
    private TransactionReader transactionReader;
    private TransactionAnalyzer transactionAnalyzer;

    public MoneyLaunderingThread(MoneyLaundering moneyLaundering,TransactionAnalyzer tA ){
        this.moneyLaundering = moneyLaundering;
        this.transactionAnalyzer = tA;
        this.transactionReader = new TransactionReader();
        this.transactionFiles = new ArrayList<>();
    }

    public void addFile(File file){
        this.transactionFiles.add(file);
    }

    @Override
    public synchronized void run(){
        int i = 0;
        while (i < this.transactionFiles.size()){
                while (this.moneyLaundering.isPaused()) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MoneyLaunderingThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }   
            File transactionFile = this.transactionFiles.get(i);
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFile);
            for (Transaction transaction : transactions){
                transactionAnalyzer.addTransaction(transaction);
            }
            moneyLaundering.incrementAmountOfFilesProcessed();
            i++;
            notifyAll();
        }
    }
}