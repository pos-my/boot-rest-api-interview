package posmy.interview.boot.controllers.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import posmy.interview.boot.services.UserDetailsServiceImpl;
import posmy.interview.boot.utils.JWTUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseToken(request);
            if (token != null && jwtUtils.validateToken(token)) {
                UserDetails usrDtl = userDetailsService.loadUserByUsername(jwtUtils.getUserNameFromToken(token));
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(usrDtl, null, usrDtl.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            log.error("Authentication Error.", e);
        }
        filterChain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromCookies(request);
        if(token == null)
            token = jwtUtils.getJwtFromHeader(request);
        return token;
    }
}
