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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 *
 * @author Strannica
 */
@SuppressWarnings("WeakerAccess")
@Entity
public class AccountEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    @Digits(integer=6, fraction=0)
    private Long clientId;

    @Pattern(regexp = "[\\p{L} 0-9]+", message = "Only letters numbers and space")
    @NotNull
    private String name;

    @NotNull
    @Min(value = 0L, message = "The value must be positive")
//    @Digits(integer=6, fraction=2)
    private BigDecimal balance;

//    @EnumValidator(enumClazz=Currency.class,message="Is not a valid currency")
    private Currency currency;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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
