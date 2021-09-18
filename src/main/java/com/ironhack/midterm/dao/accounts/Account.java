package com.ironhack.midterm.dao.accounts;

import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
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

    @NotBlank
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "primary_account_holder_id")
    protected AccountHolder primaryOwner;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "secondary_account_holder_id")
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
}
