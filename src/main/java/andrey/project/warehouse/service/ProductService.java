package andrey.project.warehouse.service;

import andrey.project.warehouse.entity.Product;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addEntity(String name, String article, String description, Integer purchasePrice,
                             Integer salePrice, String unit) {
        validateProduct(name, article, description, purchasePrice, salePrice, unit);
        validateUniqueness(name, article);

        Product product = new Product();
        product.setName(name);
        product.setArticle(article);
        product.setDescription(description);
        product.setPurchasePrice(purchasePrice);
        product.setSalePrice(salePrice);
        product.setUnit(unit);

        return productRepository.saveAndFlush(product);
    }

    public Product findById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product with this id was not found."));
    }

    public Product findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Product name must not be empty");
        }

        return productRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Product with this name was not found."));
    }

    public Product findByArticle(String article) {
        if (article == null || article.isBlank()) {
            throw new ValidationException("Product article must not be empty");
        }

        return productRepository.findByArticle(article)
                .orElseThrow(() -> new IllegalArgumentException("Product with this article was not found."));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    private void validateProduct(String name, String article, String description, Integer purchasePrice,
                                 Integer salePrice, String unit) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Product name must not be empty");
        }

        if (article == null || article.isBlank()) {
            throw new ValidationException("Product article must not be empty");
        }

        if (description == null || description.isBlank()) {
            throw new ValidationException("Product description must not be empty");
        }

        if (purchasePrice == null || purchasePrice < 0) {
            throw new ValidationException("Product purchase price must be greater than or equal to 0");
        }

        if (salePrice == null || salePrice < 0) {
            throw new ValidationException("Product sale price must be greater than or equal to 0");
        }

        if (unit == null || unit.isBlank()) {
            throw new ValidationException("Product unit must not be empty");
        }
    }

    private void validateUniqueness(String name, String article) {
        if (productRepository.existsByName(name)) {
            throw new ValidationException("Product name must be unique");
        }

        if (productRepository.existsByArticle(article)) {
            throw new ValidationException("Product article must be unique");
        }
    }
}
