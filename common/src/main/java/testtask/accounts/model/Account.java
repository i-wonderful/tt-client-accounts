/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts.model;


import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 *
 * @author Strannica
 */

public class Account {

    private Long id;

    @NotNull
    @Digits(integer=6, fraction=0)
    private Long clientId;

    @Pattern(regexp = "[\\p{L} 0-9]+", message = "Only letters numbers and space")
    @NotNull
    private String name;

    @NotNull
    @Min(value = 0L, message = "The value must be positive")
    @Digits(integer=10, fraction=2)
    private BigDecimal balance;

    private Currency currency;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", currency=" + currency +
                '}';
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
