package com.vsoft.moneytransf.security;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.vsoft.moneytransf.jpl.UserRepository;
import com.vsoft.moneytransf.jpl.entity.UserData;
import jakarta.annotation.PostConstruct;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void postConstruct() {
        userRepository.save(new UserData("johnwill@merchant.com",
                "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                Set.of("MERCHANT")));
        userRepository.save(new UserData("petersecada@merchant.com",
                "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                Set.of("MERCHANT")));
        userRepository.save(new UserData("admin",
                "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                Set.of("ADMIN")));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
        var MERCHANT = new SimpleGrantedAuthority("ROLE_MERCHANT");
        Map<String, SimpleGrantedAuthority> roleMap = Map.of("ADMIN",  new SimpleGrantedAuthority("ROLE_ADMIN"),
                "MERCHANT", new SimpleGrantedAuthority("ROLE_MERCHANT"));
        UserData user = userRepository.loadUser(username);
        if(user != null) {
            return new User(
                    user.getUsername(),
                    user.getPassword(),
                    CollectionUtils.emptyIfNull(user.getRole().stream().map(roleMap::get).filter(Objects::nonNull).collect(Collectors.toSet())));
        }
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
            throw new UsernameNotFoundException("UserData not found with username: " + username);
        }

    }
}