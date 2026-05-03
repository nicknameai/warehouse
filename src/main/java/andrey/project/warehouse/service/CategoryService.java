package andrey.project.warehouse.service;

import andrey.project.warehouse.entity.Category;
import andrey.project.warehouse.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category addEntity(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);

        return categoryRepository.saveAndFlush(category);
    }

    public void removeEntity(Long id) {
        categoryRepository.deleteById(id);
    }
}
