@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()  // Désactive la protection CSRF pour les requêtes POST si nécessaire
            .authorizeRequests()
            .antMatchers("/simuler").permitAll()  // Permet les appels POST à /simuler
            .anyRequest().authenticated();
    }
}
