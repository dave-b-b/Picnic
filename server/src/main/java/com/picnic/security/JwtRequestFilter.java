package com.picnic.security;

import com.picnic.model.AppUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtRequestFilter extends GenericFilterBean {

    private final JwtConverter converter;

    public JwtRequestFilter(JwtConverter converter) {
        this.converter = converter;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String authorization = ((HttpServletRequest)servletRequest).getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {

            // 3. The value looks okay, confirm it with JwtConverter.
            AppUser user = converter.getUserFromToken(authorization);
            if (user == null) {
                ((HttpServletResponse)servletResponse).setStatus(403); // Forbidden
            } else {

                // 4. Confirmed. Set auth for this single request.
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }

        // 5. Keep the chain going.
        filterChain.doFilter(servletRequest, servletResponse);
    }

//    @Override
//    public void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain chain) throws IOException, ServletException {
//
//        // 2. Read the Authorization value from the request.
//        String authorization = request.getHeader("Authorization");
//        if (authorization != null && authorization.startsWith("Bearer ")) {
//
//            // 3. The value looks okay, confirm it with JwtConverter.
//            AppUser user = converter.getUserFromToken(authorization);
//            if (user == null) {
//                response.setStatus(403); // Forbidden
//            } else {
//
//                // 4. Confirmed. Set auth for this single request.
//                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
//                        user, null, user.getAuthorities());
//
//                SecurityContextHolder.getContext().setAuthentication(token);
//            }
//        }
//
//        // 5. Keep the chain going.
//        chain.doFilter(request, response);
//    }
}
