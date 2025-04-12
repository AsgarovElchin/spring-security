package elchinasgarov.plantly_backend.config;

import elchinasgarov.plantly_backend.service.JWTService;
import elchinasgarov.plantly_backend.service.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final JWTService jwtService;
    private final MyUserDetailService myUserDetailService;

    public JwtFilter(JWTService jwtService, MyUserDetailService myUserDetailService) {
        this.jwtService = jwtService;
        this.myUserDetailService = myUserDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        logger.info("Incoming request path: {}", path);

        if (path.equals("/login") || path.equals("/register") || path.equals("/refresh")) {
            logger.info("Skipping filter for public endpoint: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            logger.info("Token found in Authorization header");

            try {
                username = jwtService.extractUserName(token, false);
                logger.info("Extracted username from token: {}", username);
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                logger.warn("Token has expired: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expired");
                return;
            } catch (Exception e) {
                logger.error("Invalid token: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return;
            }
        } else {
            logger.warn("No Bearer token found in Authorization header");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("Loading user details for: {}", username);
            UserDetails userDetails = myUserDetailService.loadUserByUsername(username);

            if (jwtService.validateAccessToken(token, userDetails)) {
                logger.info("Token is valid for user: {}", username);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                logger.warn("Token validation failed for user: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}
