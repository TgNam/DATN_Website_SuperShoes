package org.example.datn_website_supershoes.webconfig;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.datn_website_supershoes.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class CustomPreFilter extends OncePerRequestFilter {

    @Autowired
    JWTService jwtService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Pre filter.....");

        String author = request.getHeader("Authorization");
        log.info("Authorization {}", author);

        if (StringUtils.isBlank(author) || !author.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = author.substring("Bearer ".length());
        log.info("token {}", token);

        String email = jwtService.extract(token);
        log.info("email {}", email);
        if (StringUtils.isNotEmpty(email) || SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            if (jwtService.isValid(token, userDetails)) {
                SecurityContext contextHolder = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                contextHolder.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(contextHolder);
            }
        }
        filterChain.doFilter(request, response);
    }
}
