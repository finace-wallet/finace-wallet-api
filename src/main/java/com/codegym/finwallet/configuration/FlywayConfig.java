//package com.codegym.finwallet.configuration;
//
//import org.flywaydb.core.Flyway;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class FlywayConfig {
//
//    @Value("${spring.flyway.locations}")
//    private String[] flywayLocations;
//
//    @Value("${spring.datasource.url}")
//    private String flywayUrl;
//
//    @Value("${spring.datasource.username}")
//    private String flywayUsername;
//
//    @Value("${spring.datasource.password}")
//    private String flywayPassword;
//
//    @Bean
//    public Flyway flyway(DataSource dataSource) {
//        Flyway flyway = Flyway.configure()
//                .dataSource(dataSource)
//                .locations(flywayLocations)
//                .baselineOnMigrate(true)
//                .baselineVersion("0")
//                .load();
//        flyway.migrate();
//        return flyway;
//    }
//
//    @Bean
//    public DataSource flywayDataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUrl(flywayUrl);
//        dataSource.setUsername(flywayUsername);
//        dataSource.setPassword(flywayPassword);
//        return dataSource;
//    }
//
//}
