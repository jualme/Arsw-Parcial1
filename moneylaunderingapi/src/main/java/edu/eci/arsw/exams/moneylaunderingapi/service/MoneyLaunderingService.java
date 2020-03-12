package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.persistence.MoneyLaunderingNotFoundException;

import java.util.List;

public interface MoneyLaunderingService {
    void updateAccountStatus(SuspectAccount suspectAccount) throws MoneyLaunderingNotFoundException;
    SuspectAccount getAccountStatus(String accountId) throws MoneyLaunderingNotFoundException;
    List<SuspectAccount> getSuspectAccounts() throws MoneyLaunderingNotFoundException;
    void createAccount(SuspectAccount suspectAccount) throws MoneyLaunderingNotFoundException;
}
