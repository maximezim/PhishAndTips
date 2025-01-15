package com.learnandphish.authentication.user;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 * @author Robin Lafontaine
 */
@Data
@Entity
public class UserData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;

    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Roles role;

    private String position;

    private Boolean changePassword;
}
