package com.example.demo.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.demo.model.Users;

import lombok.Data;

@Data
public class UserPrincipal implements UserDetails {
    private int id;
    private String username;
    private String password;
    private GrantedAuthority authority;


    private UserPrincipal(int id, String username, String password, GrantedAuthority authority) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authority = authority;
    }

    public static UserPrincipal create(Users user) {

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                authority
                );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(authority);
    }

    @Override
    public String getPassword() {
        return password; // Return the hashed password
    }

    @Override
    public String getUsername() {
        return username; // Email is used as the username for authentication
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Or base on your user.getAccountExpired() logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Or base on your user.getAccountLocked() logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Or base on your user.getCredentialsExpired() logic
    }


    // --- equals() and hashCode() for proper comparison ---
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
