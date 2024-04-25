    package com.codegym.finwallet.entity;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.CascadeType;
    import jakarta.persistence.Entity;
    import jakarta.persistence.FetchType;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.JoinTable;
    import jakarta.persistence.ManyToMany;
    import jakarta.persistence.ManyToOne;
    import jakarta.persistence.Table;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.util.ArrayList;
    import java.util.List;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Entity
    @Builder
    @Table(name = "wallet")
    public class Wallet {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private String icon;
        private float amount;
        private String currentType;
        private String description;
        private float transactionBudget;
        private boolean isDelete;


        @ManyToMany(cascade = CascadeType.ALL)
        @JoinTable(name = "user_wallets",
                joinColumns = @JoinColumn(name = "wallet_id"),
                inverseJoinColumns = @JoinColumn(name = "appUser_id")
        )
        @JsonIgnore
        private List<AppUser> users;

        @ManyToMany(cascade = CascadeType.ALL)
        @JoinTable(name = "wallet_transaction_type",
                joinColumns = @JoinColumn(name = "wallet_id"),
                inverseJoinColumns = @JoinColumn(name = "tracsaction_type_id")
        )
        @JsonIgnore
        private List<TransactionType> transactionTypes;

        @ManyToMany(cascade = CascadeType.ALL)
        @JoinTable(name = "wallet_transaction_history",
                joinColumns = @JoinColumn(name = "wallet_id"),
                inverseJoinColumns = @JoinColumn(name = "transaction_history_id")
        )
        @JsonIgnore
        private List<TransactionHistory> transactionHistories;

    }