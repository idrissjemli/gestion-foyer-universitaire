package esprit.tn.foyer_bi10.config;

import esprit.tn.foyer_bi10.services.UtilisateurDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

/**
 * Configuration Spring Security : encodage BCrypt, autorisations par rôle,
 * formLogin sur /login.html personnalisé, logout, CSRF via cookie lisible en JS.
 *
 * Correctif Spring Security 6 : CsrfTokenRequestAttributeHandler avec
 * setCsrfRequestAttributeName(null) force le rendu immédiat du token, et
 * CsrfCookieFilter garantit que le cookie XSRF-TOKEN est écrit dès la
 * première requête (y compris le chargement de login.html).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UtilisateurDetailsService utilisateurDetailsService;

    public SecurityConfig(UtilisateurDetailsService utilisateurDetailsService) {
        this.utilisateurDetailsService = utilisateurDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(utilisateurDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Handler qui force le rendu immédiat du token (pas de lazy loading)
        // setCsrfRequestAttributeName(null) désactive le double-submit pattern
        // et laisse CookieCsrfTokenRepository écrire le cookie à chaque requête.
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName(null);

        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(requestHandler)
                // L'URL de traitement du login est exemptée du CSRF :
                // c'est une requête non authentifiée depuis une page publique,
                // elle ne peut pas être une attaque CSRF (l'attaquant ne connaît pas les identifiants).
                .ignoringRequestMatchers("/login")
            )
            // CsrfCookieFilter doit s'exécuter après BasicAuthenticationFilter,
            // moment où le CsrfToken a déjà été mis en attribut de requête.
            .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                // Pages et ressources publiques
                .requestMatchers("/login.html", "/login", "/css/**", "/js/**").permitAll()
                // Infos utilisateur connecté
                .requestMatchers("/api/me").authenticated()
                // GET /xxx/all : ADMIN et ETUDIANT
                .requestMatchers(HttpMethod.GET,
                        "/foyer/all", "/bloc/all", "/chambre/all",
                        "/etudiant/all", "/reservation/all", "/universite/all")
                        .hasAnyRole("ADMIN", "ETUDIANT")
                // Écriture : ADMIN uniquement
                .requestMatchers(HttpMethod.POST,
                        "/foyer/**", "/bloc/**", "/chambre/**",
                        "/etudiant/**", "/reservation/**", "/universite/**")
                        .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,
                        "/foyer/**", "/bloc/**", "/chambre/**",
                        "/etudiant/**", "/reservation/**", "/universite/**")
                        .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,
                        "/foyer/**", "/bloc/**", "/chambre/**",
                        "/etudiant/**", "/reservation/**", "/universite/**")
                        .hasRole("ADMIN")
                // Gestion des comptes : ADMIN uniquement
                .requestMatchers("/utilisateur/**").hasRole("ADMIN")
                // Tout le reste : authentifié
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/index.html", true)
                .failureUrl("/login.html?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html?logout=true")
                .permitAll()
            );

        return http.build();
    }
}
