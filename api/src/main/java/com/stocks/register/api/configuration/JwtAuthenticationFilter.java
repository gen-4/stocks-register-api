package com.stocks.register.api.configuration;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.stocks.register.api.models.user.User;
import com.stocks.register.api.repositories.user.UserRepository;

import lombok.RequiredArgsConstructor;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;






@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserRepository UserRepository;

    @ExceptionHandler()
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7); // "Bearer " has 7 chars and we need the rest
        try {
            JwtInfo info = jwtService.getInfo(jwt);
            request.setAttribute("userId", info.getUserId());
            request.setAttribute("token", jwt);
            request.setAttribute("roles", info.getRoles());

            Optional<User> user = UserRepository.findById(info.getUserId());
            if (!user.isPresent()) {
                handleException(response, "User could not be found", HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            if (user.get().isBanned()) {
                handleException(response, "User is banned", HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                    info.getUsername(), 
                    null,
                    info.getRoles().stream()
                        .map( role ->
                            new SimpleGrantedAuthority("ROLE_" + role.name())
                        )
                        .collect(Collectors.toSet())
                )
            );
            
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
