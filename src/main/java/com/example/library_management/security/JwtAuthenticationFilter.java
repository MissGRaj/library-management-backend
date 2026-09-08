package com.example.library_management.security;
import com.example.library_management.service.CustomUserDetailsService;
import com.example.library_management.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        if(authHeader != null && authHeader.startsWith("Bearer ")){

            try {
                String jwt = authHeader.substring(7);

                String username = jwtService.extractUsername(jwt);
                UserDetails userDetails = customUserDetailsService
                        .loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
//                System.out.println("-------Authenticated user: = " + username);
//
//                System.out.println("======Authenticated = "
//                        + SecurityContextHolder.getContext()
//                        .getAuthentication()
//                        .isAuthenticated());
//
//                System.out.println("------Authorities = "
//                        + SecurityContextHolder.getContext()
//                        .getAuthentication()
//                        .getAuthorities());
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
