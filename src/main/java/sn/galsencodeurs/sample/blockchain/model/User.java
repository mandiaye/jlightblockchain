package sn.galsencodeurs.sample.blockchain.model;

import java.math.BigDecimal;

import javax.validation.constraints.Email;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private int id;

    @Email
    private String email;

    @NonNull
    private String password;

    private BigDecimal solde;
    private String payload;
    private KeyPair keyPair;

    public User(@Email String email, @NonNull String password, BigDecimal solde, String payload) {
        this.email = email;
        this.password = password;
        this.solde = solde;
        this.payload = payload;
        this.id = RandomUtils.nextInt();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", solde=" + solde + "CFA" +
                '}';
    }
}
