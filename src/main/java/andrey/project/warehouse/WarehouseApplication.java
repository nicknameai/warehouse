package andrey.project.warehouse;

import andrey.project.warehouse.entity.Product;
import andrey.project.warehouse.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import andrey.project.warehouse.service.ProductService;
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
	CommandLineRunner seedProduct(ProductService productService) {
		return args -> {
			productService.addEntity("Стул","ыы", "описание", new BigDecimal(1000),10, "SDSS", LocalDateTime.now(), LocalDateTime.now() );
		};
	}

}
