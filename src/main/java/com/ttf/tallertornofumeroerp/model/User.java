package com.ttf.tallertornofumeroerp.model;

import com.ttf.tallertornofumeroerp.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "TTFUSER")
public class User {

    @Id
    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private UserStatus userStatus;
}
