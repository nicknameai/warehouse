package andrey.project.warehouse;

import andrey.project.warehouse.entity.Product;
import andrey.project.warehouse.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
	CommandLineRunner seedProduct(ProductRepository repository) {
		return args -> {
			Product product = new Product();
			product.setName("Test product");
			product.setArticle("ART-001");
			product.setDescription("Product created automatically on application startup");
			product.setPrice(new BigDecimal("199.99"));
			product.setQuantity(25);
			product.setUnit("pcs");
			product.setCreatedAt(LocalDateTime.now());
			product.setUpdatedAt(LocalDateTime.now());

			repository.saveAndFlush(product);
		};
	}

}
