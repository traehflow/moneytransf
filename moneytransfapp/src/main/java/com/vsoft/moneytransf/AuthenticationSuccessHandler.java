package com.vsoft.moneytransf;

import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.security.JwtUserDetailsService;
import com.vsoft.moneytransf.security.TokenManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private final MerchantRepository merchantRepository;

    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private TokenManager tokenManager;

    public AuthenticationSuccessHandler(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        org.springframework.security.web.authentication.AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        final String jwtToken = tokenManager.generateJwtToken(userDetails);
        Cookie cookie = new Cookie("auth-token", jwtToken);
        response.addCookie(cookie);

        if(authentication.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals(("ROLE_MERCHANT")))){
            Merchant merchant = merchantRepository.getByEmail(authentication.getName());
            if(merchant == null) {
                merchant = new Merchant();
                merchant.setName(authentication.getName());
                merchant.setEmail(authentication.getName());
                merchant.setDescription("Automatically generated");
                merchant.setStatus(MerchantStatus.ENABLED);
                merchantRepository.save(merchant);
            }
            response.sendRedirect("/transactionform.html");

        }

        if(authentication.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals(("ROLE_ADMIN")))){
            response.sendRedirect("/adminpage.html");

        }


    }
}
