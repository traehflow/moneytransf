package com.vsoft.moneytransf.security;

import com.vsoft.moneytransf.UserProfile;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import com.vsoft.moneytransf.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.reflections.util.ConfigurationBuilder.build;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationSuccessHandler customLoginSuccessHandler) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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
                    x.failureUrl("/login.html?error=true");


                })
                .logout(x -> x.logoutUrl("/perform_logout"))
                .exceptionHandling(h ->h.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(withDefaults())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
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


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}

