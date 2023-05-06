package cn.miniants.framework.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthUserDetails implements UserDetails{

    private List<?> users;
    private String userId;
    private String username;
    private String password;
    private ArrayList<String> roles;
    private String clientId;
    private Map<String, String> headers;

    public AuthUserDetails(List<?> users, String userId, String username, String password, ArrayList<String> roles, String clientId, Map<String, String> headers) {
        this.users = users;
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.clientId = clientId;
        this.headers = headers;
    }
    public AuthUserDetails(String userId, String username, String password, ArrayList<String> roles, String clientId,Map<String, String> headers) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.clientId = clientId;
        this.headers = headers;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles==null? new ArrayList<>():this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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

    public String getClientId() {
        return this.clientId;
    }

    public Object getUserId() {
        return this.userId;
    }

    public List<?> getUsers() {
        return this.users;
    }

    public Map<String, String> getHeaders() {

        return this.headers;
    }
}
