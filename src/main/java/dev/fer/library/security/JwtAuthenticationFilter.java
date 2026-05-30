package dev.fer.library.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.fer.library.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private JwtService jwtService;
  private CustomUserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    final String token;
    if (containsBearer(authHeader)) {
      token = extractToken(authHeader);

      if (jwtService.validateToken(token)) {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(jwtService.extractSubject(token));
        UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(
            userDetails,null,userDetails.getAuthorities()
          );

          authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
          );

          SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    
    filterChain.doFilter(request, response);
  }

  protected boolean containsBearer(String header) {
    return header != null && header.startsWith("Bearer ");
  }

  protected String extractToken(String header) {
    return header.substring(7);
  } 

}
