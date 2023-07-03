package com.mammadli.ecommerce.jwt;

import com.mammadli.ecommerce.config.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;


    private final MyUserDetailsService userDetailsService;

    @Override
    // The doFilter method of the Filter is called by the container each time a request/response pair is passed through the chain due to a client request for a resource at the end of the chain.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Tokens are sent with a prefix of 7 chars = "Bearer", which we do not need that is why we are substring the first 7 characters of the token
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        // The user can be authenticated and does not has any authorities (not authenticated yet)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Getting the overall userDetails
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // Validating the token
            if (jwtService.validateToken(token, userDetails)) {
                // UsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities)
                // We are passing the userDetails,  credentials are not none yet (null), then we get the authorities from the userDetails
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,  null, userDetails.getAuthorities());
                // AuthenticationDetailsSource builds the details object from an HttpServletRequest object, creating a WebAuthenticationDetails .
                // .buildDetails is called by a class when it wishes a new authentication details instance to be created.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // SecurityContextHolder.getContext().setAuthentication is used to set the authenticated principal (the authenticated entity) to the context.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // doFilter(ServletRequest request, ServletResponse response) Causes the next filter in the chain to be invoked, or if the calling filter is the last filter in the chain, causes the resource at the end of the chain to be invoked.
        filterChain.doFilter(request, response);
    }

}
