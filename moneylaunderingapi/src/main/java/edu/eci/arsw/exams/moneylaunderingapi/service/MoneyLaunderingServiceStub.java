package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.persistence.MoneyLaunderingNotFoundException;
import edu.eci.arsw.exams.moneylaunderingapi.persistence.MoneyLaunderingPersistence;
import edu.eci.arsw.exams.moneylaunderingapi.service.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoneyLaunderingServiceStub implements MoneyLaunderingService{

    @Autowired
    MoneyLaunderingPersistence mlp;

    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) throws MoneyLaunderingNotFoundException {
        mlp.createSuspect(suspectAccount);        
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) throws MoneyLaunderingNotFoundException { 
        return mlp.getAccountStatus(accountId);
    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() throws MoneyLaunderingNotFoundException{        
        return mlp.getSuspectAccount();        
    }
    
    @Override
    public void createAccount(SuspectAccount suspectAccount) throws MoneyLaunderingNotFoundException{
        mlp.saveSuspect(suspectAccount); 
    }
}
