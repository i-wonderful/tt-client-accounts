/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts.dao;

import testtask.accounts.model.Currency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 *
 * @author Strannica
 */
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;
    private BigDecimal balance;
    private Currency currency;

    public Account(BigDecimal amount) {
        this.balance = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    
}
