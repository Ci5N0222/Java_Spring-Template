package com.sion.config.filters;

import com.sion.commons.util.JwtUtil;
import com.sion.members.service.MembersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwt;

    @Autowired
    private MembersService membersService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /**
         *  Spring Security
         *  - SecurityContextHolder에 Authenticaion 객체의 존재 유무로 인증 여부를 판단함
         */

        String token = "";

        // Token value parsing from Request Header
        String auth = request.getHeader("Authorization");
        if(auth != null && auth.startsWith("Bearer ")) {
            token = auth.substring(7);
        }

        System.out.println("token ===== " + token);

        // Token is valid
        if(token != null && jwt.isVerified(token)) {
            String id = jwt.getSubject(token);
            UserDetails user = membersService.loadUserByUsername(id);

            // Save Authenticated User Information ( Spring Security )
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
