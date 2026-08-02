package aston.module4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
        basePackages = "aston.module4",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASPECTJ,
                pattern = "aston.module4.notification..*"
        )
)

public class Module4Application {
    public static void main(String[] args) {
        System.setProperty("server.port", "8080");
        SpringApplication.run(Module4Application.class, args);
    }

}
