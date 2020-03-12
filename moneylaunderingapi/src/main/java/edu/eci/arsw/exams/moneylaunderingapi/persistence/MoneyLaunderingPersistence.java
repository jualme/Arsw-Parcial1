/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.exams.moneylaunderingapi.persistence;


import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import java.util.List;
/**
 *
 * @author jualm
 */
public interface MoneyLaunderingPersistence {
    public void saveSuspect(SuspectAccount sa) throws MoneyLaunderingNotFoundException;
    public void createSuspect(SuspectAccount sa) throws MoneyLaunderingNotFoundException;
    public SuspectAccount getAccountStatus(String accountId) throws MoneyLaunderingNotFoundException;
    public List<SuspectAccount> getSuspectAccount() throws MoneyLaunderingNotFoundException;
}
