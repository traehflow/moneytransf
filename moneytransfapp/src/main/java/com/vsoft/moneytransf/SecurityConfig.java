package com.vsoft.moneytransf;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.context.WebApplicationContext;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationSuccessHandler customLoginSuccessHandler) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/**").hasAnyRole(new String[]{Roles.MERCHANT, Roles.ADMIN})
                                .requestMatchers(HttpMethod.POST, "/import/merchants").hasAnyRole(new String[]{Roles.ADMIN, Roles.MERCHANT})

                )

                .formLogin( x -> {
                    x.defaultSuccessUrl("/transactionform.html", true);

                    x.loginPage("/login.html").permitAll();
                    x.loginProcessingUrl("/perform_login");
                    x.successHandler(customLoginSuccessHandler);
                    //x.defaultSuccessUrl("/transactionform.html", true);
                    x.failureUrl("/login.html?error=true");


                })
                //.successHandler(customLoginSuccessHandler)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles(Roles.ADMIN)
                .build();

        UserDetails merchant1 = User.withDefaultPasswordEncoder()
                .username("johnwill@merchant.com")
                .password("password")
                .roles(Roles.MERCHANT)
                .build();

        UserDetails merchant2 = User.withDefaultPasswordEncoder()
                .username("petersecada@merchant.com")
                .password("password")
                .roles(Roles.MERCHANT)
                .build();

        return new InMemoryUserDetailsManager(admin, merchant1, merchant2);
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UserProfile getUser(HttpServletRequest request) {
        UserProfile userProfile = new UserProfile();
        var principal = request.getUserPrincipal();
        if (principal instanceof UsernamePasswordAuthenticationToken token) {
            userProfile.setAdmin(token.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals(Roles.ROLE_PREFIX + Roles.ADMIN)));
            userProfile.setMerchant(token.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals(Roles.ROLE_PREFIX + Roles.MERCHANT)));
        }

        userProfile.setUserName(principal.getName());
        return userProfile;
    }
}

