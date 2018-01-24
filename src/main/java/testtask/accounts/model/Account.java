/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts.model;

import java.math.BigDecimal;

/**
 *
 * @author Strannica
 */
public class Account {
    
    private BigDecimal amount;

    public Account(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    
}
