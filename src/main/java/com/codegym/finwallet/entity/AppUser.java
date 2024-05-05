package com.codegym.finwallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Builder
@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    private String email;
    private boolean isDelete;
    private boolean isActive;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonManagedReference
    private List<Role> roles;

//    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
////    @JsonIgnore
////    private List<Wallet> wallets;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<WalletOwnership> walletOwnerships;

    @JsonIgnore
    @OneToOne(mappedBy = "appUser",cascade = CascadeType.ALL)
    private Profile profile;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private TokenBlackList tokenBlackList;
}
