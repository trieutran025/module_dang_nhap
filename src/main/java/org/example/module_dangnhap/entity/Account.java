package org.example.module_dangnhap.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Account  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long accountId;
    @Column(nullable = false,unique = true)
     String username;
    @Column(nullable = false,unique = true)
     String password;
    @ManyToMany
    @JoinTable(
            name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles;

    @Column(columnDefinition = "boolean default true")
    boolean isActive;

    public boolean isAccountNonExpired(){
        return true;
    }
    public boolean isAccountNonLocket(){
        return true;
    }
    public boolean isCredentialsNonExpired() {
        return true;
    }


    public boolean isEnabled() {
        return true;
    }

}
