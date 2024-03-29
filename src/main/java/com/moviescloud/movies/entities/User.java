package com.moviescloud.movies.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email
    @NotBlank
    private String email;

    @JsonIgnore
    @NotBlank
    @Size(max = 1024)
    private String password;

    @NotBlank
    @Size(max = 256)
    private String firstName;

    @NotBlank
    @Size(max = 256)
    private String lastName;

    @PastOrPresent
    private Date birthDay;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Privilege> privileges;

    public User(String email, String password, String firstName, String lastName, Date birthDay, List<Privilege> privileges) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.privileges = privileges;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return mapToGrantedAuthorities(this.privileges);
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return false;
    }

    private List<GrantedAuthority> mapToGrantedAuthorities(List<Privilege> userPrivileges) {
        return userPrivileges.stream().map(privilege -> new SimpleGrantedAuthority(privilege.getPrivilegeName())).collect(Collectors.toList());
    }
}
