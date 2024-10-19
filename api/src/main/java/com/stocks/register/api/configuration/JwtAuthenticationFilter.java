package com.stocks.register.api.configuration;

import java.io.IOException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import com.stocks.register.api.models.user.User;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;






@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserService userDetailsService;

    @ExceptionHandler()
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;
        UsernamePasswordAuthenticationToken authToken;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7); // "Bearer " has 7 chars and we need the rest
        try {
            email = jwtService.extractUserEmail(jwt);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = this.userDetailsService.loadUserByEmail(email);

                request.setAttribute("userId", user.getId());
                request.setAttribute("token", jwt);
                request.setAttribute("roles", user.getAuthorities());

                if (jwtService.isTokenValid(jwt, user)) {
                    authToken = new UsernamePasswordAuthenticationToken(
                        user, 
                        null,
                        user.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } 
        } catch (ExpiredJwtException e) {
            handleException(response, "Token has expired", HttpServletResponse.SC_FORBIDDEN);
            return;
        } catch (JwtException e) {
            handleException(response, "Invalid JWT token", HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (Exception e) {
            handleException(response, "An error occurred during token processing", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, String message, int status) throws IOException{
        response.setStatus(status);
        response.getWriter().write("{\"code\": \"exception.id.forbidden\", \"message\": \"" + message + "\"}");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
    
}
