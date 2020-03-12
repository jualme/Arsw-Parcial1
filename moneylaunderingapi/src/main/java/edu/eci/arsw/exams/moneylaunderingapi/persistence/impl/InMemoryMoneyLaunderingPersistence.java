/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.exams.moneylaunderingapi.persistence.impl;


import edu.eci.arsw.exams.moneylaunderingapi.persistence.MoneyLaunderingPersistence;
import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.persistence.MoneyLaunderingNotFoundException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Service;
/**
 *
 * @author jualm
 */

@Service
public class InMemoryMoneyLaunderingPersistence implements  MoneyLaunderingPersistence{
    
    final CopyOnWriteArrayList<SuspectAccount> suspectAccounts = new CopyOnWriteArrayList();;

    public InMemoryMoneyLaunderingPersistence(){
    }
    @Override
    public void saveSuspect(SuspectAccount sa) throws MoneyLaunderingNotFoundException{
        boolean suspectAccount = suspectAccounts.addIfAbsent(sa);
        if (!suspectAccount){
            throw new MoneyLaunderingNotFoundException("The given suspect already exists: "+sa);
        }
    }
    @Override
    public SuspectAccount getAccountStatus(String accountId) throws MoneyLaunderingNotFoundException{
        SuspectAccount sa = null;
        for (SuspectAccount suspect : suspectAccounts ){
            if (accountId.equals(suspect.accountId)){
                sa = suspect;
            }
        }
        if (sa == null){
            throw new MoneyLaunderingNotFoundException("The given suspect doesn`t exist " + accountId);
        }
        return  sa;
    }
    
    @Override
    public List<SuspectAccount> getSuspectAccount() throws MoneyLaunderingNotFoundException{
        if(suspectAccounts.isEmpty()){            
            throw new MoneyLaunderingNotFoundException("Not found Suspect Accounts");
        }
        return suspectAccounts;
    }
    
    @Override
    public void createSuspect(SuspectAccount sa) throws MoneyLaunderingNotFoundException{ 
        for (SuspectAccount suspect : suspectAccounts ){
            if (sa.accountId.equals(suspect.accountId)){
                suspect.amountOfSmallTransactions += sa.amountOfSmallTransactions;
            }
        }
    }
}
    
    

