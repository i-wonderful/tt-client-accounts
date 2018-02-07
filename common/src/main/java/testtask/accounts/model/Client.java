package testtask.accounts.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Strannica
 */
public class Client extends BaseModel{

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

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj instanceof Client == false) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        Client cl = (Client) obj;

        return Objects.equals(id, cl.id) && Objects.equals(firstName, cl.firstName)
                && Objects.equals(lastName, cl.lastName)
                && Objects.equals(middleName, cl.middleName)
                && Objects.equals(birthday, cl.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, middleName, lastName, birthday);
    }

}
