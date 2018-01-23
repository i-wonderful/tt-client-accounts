/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts.dto;

import java.util.List;

/**
 *
 * @author Strannica
 */
public class ClientDto {
    
    private String name;
    
    private String surname;

    private List<AccountDto> accounts;
    
    public ClientDto(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public ClientDto(String name, String surname, List<AccountDto> accounts) {
        this.name = name;
        this.surname = surname;
        this.accounts = accounts;
    }
    
    

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return the accounts
     */
    public List<AccountDto> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(List<AccountDto> accounts) {
        this.accounts = accounts;
    }
    
    
    
}
