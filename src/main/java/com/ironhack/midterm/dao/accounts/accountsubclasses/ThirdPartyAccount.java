package com.ironhack.midterm.dao.accounts.accountsubclasses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.midterm.dao.Constants;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.Transaction;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import com.ironhack.midterm.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ThirdPartyAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "amount", column = @Column(name = "balance")),
            @AttributeOverride( name = "currency", column = @Column(name = "balance_currency"))
    })
    protected Money balance;

    @ManyToOne
    @JoinColumn(name = "primary_owner_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected ThirdParty primaryOwner;


    @ManyToOne
    @JoinColumn(name = "secondary_owner_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected ThirdParty secondaryOwner;

    @NotNull
    @Column(name = "penalty_fee")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "amount", column = @Column(name = "penalty_fee")),
            @AttributeOverride( name = "currency", column = @Column(name = "penalty_fee_currency"))
    })
    protected Money penaltyFee;

    @NotNull
    protected LocalDate creationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    protected Status status;

    @OneToMany(
            mappedBy = "transferAccount",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    protected List<Transaction> paymentTransactions;

    @OneToMany(
            mappedBy = "receivingAccount",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    protected List<Transaction> receivingTransactions;

    @NotNull
    private String hashedKey;

    @NotNull
    private String name;

    public ThirdPartyAccount(Money balance, ThirdParty primaryOwner, String hashedKey, String name) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.penaltyFee = new Money(Constants.PENALTY_FEE, Currency.getInstance("GBP"));
        this.creationDate = LocalDate.now();
        this.status = Status.ACTIVE;
        this.paymentTransactions = new ArrayList<>();
        this.receivingTransactions = new ArrayList<>();
        this.hashedKey = hashedKey;
        this.name = name;
    }

    public ThirdPartyAccount(Money balance, ThirdParty primaryOwner, ThirdParty secondaryOwner, String hashedKey, String name) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.penaltyFee = new Money(Constants.PENALTY_FEE, Currency.getInstance("GBP"));
        this.creationDate = LocalDate.now();
        this.status = Status.ACTIVE;
        this.paymentTransactions = new ArrayList<>();
        this.receivingTransactions = new ArrayList<>();
        this.hashedKey = hashedKey;
        this.name = name;
    }

    public String generateHashedKey(String secretKey) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        secretKey = encoder.encode(secretKey);
        return secretKey;
    }
}
