package andrey.project.warehouse.service;

import andrey.project.warehouse.entity.Product;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;



//создание товара
@Service
public class ProductService {


    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }


    public Product addEntity(String name, String article, String description, BigDecimal price,
                             Integer quantity, String unit, LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateProduct(name, article, price, quantity);

        Product product = new Product();
        product.setName(name);
        product.setArticle(article);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setUnit(unit);
        product.setCreatedAt(createdAt);
        product.setUpdatedAt(updatedAt);

        this.productRepository.saveAndFlush(product);

        return product;
    }


    public void removeEntity(Long id) {
        this.productRepository.deleteById(id);
    }


    private void validateProduct(String name, String article, BigDecimal price, Integer quantity) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Product name must not be empty");
        }

        if (article == null || article.isBlank()) {
            throw new ValidationException("Product article must not be empty");
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Product price must be greater than or equal to 0");
        }

        if (quantity == null || quantity < 0) {
            throw new ValidationException("Product quantity must be greater than or equal to 0");
        }
    }

}
