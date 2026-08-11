package esprit.tn.foyer_bi10.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Force l'écriture du cookie XSRF-TOKEN sur chaque requête.
 *
 * En Spring Security 6, CookieCsrfTokenRepository utilise un handler "différé"
 * (deferred token) : le cookie n'est écrit que si csrfToken.getToken() est appelé
 * explicitement pendant le cycle de la requête. Ce filtre fait cet appel, ce qui
 * garantit que le cookie est présent dès la première visite de login.html.
 */
public class CsrfCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        // Appeler getToken() déclenche l'écriture du cookie XSRF-TOKEN
        if (csrfToken != null) {
            csrfToken.getToken();
        }
        filterChain.doFilter(request, response);
    }
}
