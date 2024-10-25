package ch.martinelli.demo.jooq;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Theme(value = "vaadin-create24")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public DefaultConfigurationCustomizer jooqConfigurationCustomizer() {
        return (DefaultConfiguration c) -> c.settings().withExecuteWithOptimisticLocking(true);
    }
}
