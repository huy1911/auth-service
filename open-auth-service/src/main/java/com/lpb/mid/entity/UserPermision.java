package com.lpb.mid.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "USER_PERMISION")
public class UserPermision {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String id;
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "ROLE_ID")
    private String roleId;

}