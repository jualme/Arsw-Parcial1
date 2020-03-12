package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.persistence.MoneyLaunderingNotFoundException;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping( value = "/fraud-bank-accounts")
public class MoneyLaunderingController{

    @Autowired
    MoneyLaunderingService moneyLaunderingService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> offendingAccounts() {
        try {
            return new ResponseEntity<>(moneyLaunderingService.getSuspectAccounts(),HttpStatus.OK);
        } catch (MoneyLaunderingNotFoundException ex) {
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> AddOffendingAccounts(@RequestBody SuspectAccount newAs){
        try {
            moneyLaunderingService.createAccount(newAs);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MoneyLaunderingNotFoundException ex) {
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    
    @RequestMapping(value = "/{accountId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAccountStatus(@PathVariable String accountId) {
        try {
            return new ResponseEntity<>(moneyLaunderingService.getAccountStatus(accountId), HttpStatus.OK);
        } catch (MoneyLaunderingNotFoundException ex) {
            Logger.getLogger(MoneyLaunderingNotFoundException.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }
    
    @RequestMapping(path = "/{accountId}",method = RequestMethod.PUT)	
    public ResponseEntity<?> updateAccountStatus(@PathVariable ("accountId") String author, @RequestBody SuspectAccount newSa ){
        try {
            moneyLaunderingService.updateAccountStatus(newSa);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MoneyLaunderingNotFoundException ex) {
            Logger.getLogger(MoneyLaunderingNotFoundException.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }
      
    
}
