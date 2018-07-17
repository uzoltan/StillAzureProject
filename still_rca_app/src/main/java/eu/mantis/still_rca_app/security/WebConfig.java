package eu.mantis.still_rca_app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  /*@Bean
  SessionManager getSessionManager() {
    return new SessionManager();
  }*/

  @Autowired
  SessionManager manager;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(manager).addPathPatterns("/**").excludePathPatterns("/resources/**", "/login");
    // assuming you put your serve your static files with /resources/ mapping
    // and the pre login page is served with /login mapping
  }

}
