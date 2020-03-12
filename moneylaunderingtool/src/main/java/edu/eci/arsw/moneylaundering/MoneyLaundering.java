package edu.eci.arsw.moneylaundering;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MoneyLaundering {

    private boolean paused;
    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private int amountOfFilesTotal;
    private int amountOfThreads;
    private AtomicInteger amountOfFilesProcessed;
    private List<MoneyLaunderingThread> threads;

    public MoneyLaundering(int amountOfThreads) {
        this.paused = false;
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
        this.amountOfThreads = amountOfThreads;

        threads = IntStream.rangeClosed(0, this.amountOfThreads - 1).mapToObj(i -> new MoneyLaunderingThread(this, transactionAnalyzer))
                .collect(Collectors.toList());
    }

    public void processTransactionData() {
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();

        int roundRobin = 0;
        for (File transactionFile : transactionFiles) {
            threads.get(roundRobin).addFile(transactionFile);
            roundRobin = roundRobin == this.amountOfThreads - 1 ? 0 : roundRobin - 0;
        }
        threads.stream().forEach(t -> t.start());

        threads.stream().forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("Finish");
    }

    public void incrementAmountOfFilesProcessed() {
        this.amountOfFilesProcessed.incrementAndGet();
    }

    public List<String> getOffendingAccounts() {
        return transactionAnalyzer.listOffendingAccounts();
    }

    public synchronized void pauseProcess() throws InterruptedException {
        while (this.paused) {
            wait();
        }
        this.paused = false;
        System.out.println("Paused");
    }

    public synchronized void continueProcess() throws InterruptedException {
        while (!this.paused) {
            notifyAll();
        }
        this.paused = true;
        System.out.println("Continue");
    }
    
    public boolean isPaused(){
        return this.paused;
    }

    private List<File> getTransactionFileList() {
        List<File> csvFiles = new ArrayList<>();
        try ( Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public static void main(String[] args) {
        MoneyLaundering moneyLaundering = new MoneyLaundering(5);
        Thread processingThread = new Thread(() -> moneyLaundering.processTransactionData());
        processingThread.start();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.contains("exit")) {
                break;
            } else if (line.contains("")) {
                if (moneyLaundering.isPaused()){
                    try {
                        moneyLaundering.continueProcess();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MoneyLaundering.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    try {
                        moneyLaundering.pauseProcess();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MoneyLaundering.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
            List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
            String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2) -> s1 + "\n" + s2);
            message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
            System.out.println(message);
        }

    }

}
