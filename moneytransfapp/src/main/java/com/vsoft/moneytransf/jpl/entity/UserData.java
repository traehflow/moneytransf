package com.vsoft.moneytransf.jpl.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;

@Data
@Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "userData")
public class UserData {
    @Id
    private String username;
    private String password;
    private Set<String> role;
}
