package posmy.interview.boot.config;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


public class JwtAuthenticationFilter extends OncePerRequestFilter{

		@Value("${jwt.header.string}")
	    public String HEADER_STRING;

	    @Value("${jwt.token.prefix}")
	    public String TOKEN_PREFIX;

	    @Resource(name = "userService")
	    private UserDetailsService userDetailsService;

	    @Autowired
	    private TokenProvider jwtTokenUtil;

	    
	    @Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
			try {
				String jwt = parseJwt(request);
				if (jwt != null && jwtTokenUtil.validateJwtToken(jwt)) {
					String username = jwtTokenUtil.getUserNameFromJwtToken(jwt);

					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} catch (Exception e) {
				logger.error("Cannot set user authentication: {}", e);
			}

			filterChain.doFilter(request, response);
		}

		private String parseJwt(HttpServletRequest request) {
			String headerAuth = request.getHeader("Authorization");

			if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
				return headerAuth.substring(7, headerAuth.length());
			}

			return null;
		}
	
	
}
