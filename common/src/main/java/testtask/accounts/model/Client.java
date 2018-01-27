
package testtask.accounts.model;


import java.util.Date;
import java.util.List;

/**
 *
 * @author Strannica
 */

public class Client {

    private Long id;

    private String firstName;
    private String middleName;
    private String lastName;
    private Date birthday;
    private List<Account> accounts;

    public Client() {
    }

    
    public Client(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

//    @Override
//    public boolean equals(Object obj) {
//    
//        if(obj instanceof Client == false)
//            return false;
//        ((Client) obj).getId() 
//    }
    
    
}
