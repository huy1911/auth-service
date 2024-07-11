package com.lpb.mid.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "PERMISION")
@Data
public class Permision implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private String id;
    @Column(name = "ROLE_ID")
    private String roleId;
    @Column(name = "ROLE_NAME")
    private String roleName;
    @Column(name = "INPUTTER")

    private String inputter;
    @Column(name = "status")

    private String status;
    @Column(name = "CHECKER")

    private String checker;
    @Column(name = "CREATE_DATE")

    private Instant createDate;
    @Column(name = "MODIFIER_DATE")

    private Instant  modifierDate;

    public Permision() {
    }


    @Override
    public String getAuthority() {
        return null;
    }
}
