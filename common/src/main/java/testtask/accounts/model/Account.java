
package testtask.accounts.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author Strannica
 */
public class Account extends BaseModel{

    @NotNull
    @Digits(integer = 6, fraction = 0)
    private Long clientId;

    @Pattern(regexp = "[\\p{L} 0-9]+", message = "Only letters numbers and space")
    @NotNull
    private String name;

    @NotNull
    @Min(value = 0L, message = "The value must be positive")
    private BigDecimal balance;

    private Currency currency;

    @Override
    public String toString() {
        return "Account{"
                + "id=" + id
                + ", clientId=" + clientId
                + ", name='" + name + '\''
                + ", balance=" + balance
                + ", currency=" + currency
                + '}';
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

    @Override
    public boolean equals(Object obj) {

        if (obj == null || !(obj instanceof Account)) {
            return false;
        }
        
        if (this == obj) {
            return true;
        }

        Account acc = (Account) obj;

        return Objects.equals(this.id, acc.id)
                && Objects.equals(this.name, acc.name)
                && Objects.equals(this.clientId, acc.clientId)
                && Objects.equals(this.currency, acc.currency)
                && Objects.equals(this.balance.doubleValue(), acc.balance.doubleValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, name, balance, currency);
    }

}
