package com.group3.MockProject.security;

import com.group3.MockProject.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {
    
    /**
     * Serial version UID for serialization
     */
    private static final long serialVersionUID = 1L;

    /**
     * User ID
     */
    private String id;

    /**
     * Username for authentication
     */
    private String username;

    /**
     * User's email address
     */
    private String email;

    /**
     * User's full name
     */
    private String fullname;

    /**
     * User's password hash
     */
    private String password;

    /**
     * User's authorities/roles
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor for UserDetailsImpl
     *
     * @param id the user ID
     * @param username the username
     * @param email the email address
     * @param fullname the full name
     * @param password the password hash
     * @param authorities the user authorities
     */
    public UserDetailsImpl(String id, String username, String email, String fullname, 
                          String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Creates a UserDetailsImpl from a User entity
     * <p>
     * Static factory method that converts a User entity into a UserDetailsImpl
     * with appropriate authorities based on the user's role.
     * </p>
     *
     * @param user the User entity to convert
     * @return UserDetailsImpl the converted user details
     */
    public static UserDetailsImpl build(User user) {
        // Create authority from user role
        GrantedAuthority authority = new SimpleGrantedAuthority(
            user.getRole() != null ? "ROLE_" + user.getRole().getRoleId() : "ROLE_USER"
        );
        
        List<GrantedAuthority> authorities = List.of(authority);

        return new UserDetailsImpl(
                user.getUsername(),
                user.getUsername(),
                null, // email removed from User entity
                user.getFullName(),
                user.getPasswordHash(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Gets the user ID
     *
     * @return String the user ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the user's email
     *
     * @return String the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the user's full name
     *
     * @return String the full name
     */
    public String getFullname() {
        return fullname;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
} 