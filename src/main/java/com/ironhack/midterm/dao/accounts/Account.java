package com.ironhack.midterm.dao.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.midterm.dao.Constants;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.transactions.Transaction;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="account_type",
        discriminatorType = DiscriminatorType.STRING)
public class Account {

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
    protected AccountHolder primaryOwner;


    @ManyToOne
    @JoinColumn(name = "secondary_owner_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected AccountHolder secondaryOwner;

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

    public Account(Money balance) {
        this.balance = balance;
        this.penaltyFee = new Money(Constants.PENALTY_FEE, Currency.getInstance("GBP"));
        this.creationDate = LocalDate.now();
        this.status = Status.ACTIVE;
        this.paymentTransactions = new ArrayList<>();
        this.receivingTransactions = new ArrayList<>();
    }

    public Account(Money balance, AccountHolder primaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.penaltyFee = new Money(Constants.PENALTY_FEE, Currency.getInstance("GBP"));
        this.creationDate = LocalDate.now();
        this.status = Status.ACTIVE;
        this.paymentTransactions = new ArrayList<>();
        this.receivingTransactions = new ArrayList<>();
    }

    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.penaltyFee = new Money(Constants.PENALTY_FEE, Currency.getInstance("GBP"));
        this.creationDate = LocalDate.now();
        this.status = Status.ACTIVE;
        this.paymentTransactions = new ArrayList<>();
        this.receivingTransactions = new ArrayList<>();
    }
}
