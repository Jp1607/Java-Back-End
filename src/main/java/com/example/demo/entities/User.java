package com.example.demo.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")

public class User extends DefaultEntities implements UserDetails {

    public User() {

    }

    public User(Long idUser) {
        this.id = idUser;
    }

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles> roles;

    @Column(name = "token")
    private String token;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.roles == null) {
            return null;
        }
        return this.roles.stream().map(e -> new SimpleGrantedAuthority(e.getName())).collect(Collectors.toList());
    }
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return this.name; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
    public void setPassword(String password) { this.password = password; }
    public Set<Roles> getRole() { return roles; }
    public void setRoles(Set<Roles> roles) { this.roles = roles; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}