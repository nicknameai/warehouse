package andrey.project.warehouse;

import andrey.project.warehouse.console.MainMenu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WarehouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarehouseApplication.class, args);
    }

    @Bean
    CommandLineRunner runMainMenu(MainMenu mainMenu) {
        return args -> mainMenu.run();
    }
}
