package eu.mantis.still_rul_app;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "akquinetEntityManagerFactory", transactionManagerRef = "akquinetTransactionManager", basePackages
    = {
    "eu.mantis.still_rul_app.repository.akquinet"})
public class AkquinetDbConfig {

  @Bean(name = "akquinetDataSource")
  @ConfigurationProperties(prefix = "secondary.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "akquinetEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean akquinetEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                             @Qualifier("akquinetDataSource") DataSource dataSource) {
    return builder.dataSource(dataSource).packages("eu.mantis.still_rul_app.model.akquinet").persistenceUnit("mantis-akquinet-demo").build();
  }

  @Bean(name = "akquinetTransactionManager")
  public PlatformTransactionManager akquinetTransactionManager(
      @Qualifier("akquinetEntityManagerFactory") EntityManagerFactory akquinetEntityManagerFactory) {
    return new JpaTransactionManager(akquinetEntityManagerFactory);
  }

}