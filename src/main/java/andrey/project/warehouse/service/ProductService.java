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
                .orElseThrow(() -> new IllegalArgumentException("Товар с таким ID не найден."));
    }

    public Product findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Название товара не должно быть пустым.");
        }

        return productRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Товар с таким названием не найден."));
    }

    public Product findByArticle(String article) {
        if (article == null || article.isBlank()) {
            throw new ValidationException("Артикул товара не должен быть пустым.");
        }

        return productRepository.findByArticle(article)
                .orElseThrow(() -> new IllegalArgumentException("Товар с таким артикулом не найден."));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    private void validateProduct(String name, String article, String description, Integer purchasePrice,
                                 Integer salePrice, String unit) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Название товара не должно быть пустым.");
        }

        if (article == null || article.isBlank()) {
            throw new ValidationException("Артикул товара не должен быть пустым.");
        }

        if (description == null || description.isBlank()) {
            throw new ValidationException("Описание товара не должно быть пустым.");
        }

        if (purchasePrice == null || purchasePrice < 0) {
            throw new ValidationException("Закупочная цена товара должна быть больше или равна 0.");
        }

        if (salePrice == null || salePrice < 0) {
            throw new ValidationException("Цена продажи товара должна быть больше или равна 0.");
        }

        if (unit == null || unit.isBlank()) {
            throw new ValidationException("Единица измерения товара не должна быть пустой.");
        }
    }

    private void validateUniqueness(String name, String article) {
        if (productRepository.existsByName(name)) {
            throw new ValidationException("Название товара должно быть уникальным.");
        }

        if (productRepository.existsByArticle(article)) {
            throw new ValidationException("Артикул товара должен быть уникальным.");
        }
    }
}
