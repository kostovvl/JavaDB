package entities.PaymentSystem;

import entities.BaseEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "bank_accounts")
public class BankAccount extends BaseEntity {

    private String name;
    private String SWIFTCode;
    private Set<CreditCard> creditCards;
    private BillingDetail billingDetail;

    public BankAccount() {
    }

    @Column(name = "name", length = 50, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "SWIFT_code", length = 12, nullable = false)
    public String getSWIFTCode() {
        return SWIFTCode;
    }

    public void setSWIFTCode(String SWIFTCode) {
        this.SWIFTCode = SWIFTCode;
    }

    @OneToMany(mappedBy = "bankAccount", targetEntity = CreditCard.class,
    fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(Set<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    @OneToOne(mappedBy = "bankAccount", targetEntity = BillingDetail.class)
    public BillingDetail getBillingDetail() {
        return billingDetail;
    }

    public void setBillingDetail(BillingDetail billingDetail) {
        this.billingDetail = billingDetail;
    }
}
