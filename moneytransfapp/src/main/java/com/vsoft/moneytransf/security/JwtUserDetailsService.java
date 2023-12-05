package com.vsoft.moneytransf.security;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class JwtUserDetailsService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
        var MERCHANT = new SimpleGrantedAuthority("ROLE_MERCHANT");
        if ("johnwill@merchant.com".equals(username)) {
            return new User("johnwill@merchant.com",
                    "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                    List.of(MERCHANT));
        } else if ("petersecada@merchant.com".equals(username)) {
            return new User("petersecada@merchant.com",
                    "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                    List.of(MERCHANT));
        } else if ("admin".equals(username)) {
            return new User("admin",
                    "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                    List.of(ADMIN));
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

    }
}