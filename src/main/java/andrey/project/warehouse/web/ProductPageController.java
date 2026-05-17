package andrey.project.warehouse.web;

import andrey.project.warehouse.entity.Product;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.service.ProductService;
import andrey.project.warehouse.web.form.ProductForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/products")
public class ProductPageController {

    private final ProductService productService;

    public ProductPageController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showPage(@RequestParam(required = false) Integer id,
                           @RequestParam(required = false) String name,
                           @RequestParam(required = false) String article,
                           Model model) {
        preparePage(model, new ProductForm(), id, name, article);

        if (id != null) {
            try {
                model.addAttribute("searchResult", productService.findById(id));
                model.addAttribute("searchLabel", "Результат поиска по ID");
            } catch (IllegalArgumentException ex) {
                model.addAttribute("searchError", ex.getMessage());
            }
        } else if (name != null && !name.isBlank()) {
            try {
                model.addAttribute("searchResult", productService.findByName(name));
                model.addAttribute("searchLabel", "Результат поиска по названию");
            } catch (ValidationException | IllegalArgumentException ex) {
                model.addAttribute("searchError", ex.getMessage());
            }
        } else if (article != null && !article.isBlank()) {
            try {
                model.addAttribute("searchResult", productService.findByArticle(article));
                model.addAttribute("searchLabel", "Результат поиска по артикулу");
            } catch (ValidationException | IllegalArgumentException ex) {
                model.addAttribute("searchError", ex.getMessage());
            }
        }

        return "products";
    }

    @PostMapping
    public String createProduct(@ModelAttribute("productForm") ProductForm form, Model model) {
        preparePage(model, form, null, null, null);

        try {
            Product product = productService.addEntity(
                    form.getName(),
                    form.getArticle(),
                    form.getDescription(),
                    form.getPurchasePrice(),
                    form.getSalePrice(),
                    form.getUnit()
            );
            model.addAttribute("successMessage", "Товар добавлен: ID " + product.getId());
            model.addAttribute("productForm", new ProductForm());
        } catch (ValidationException | IllegalArgumentException ex) {
            model.addAttribute("formError", ex.getMessage());
        }

        return "products";
    }

    private void preparePage(Model model, ProductForm form, Integer id, String name, String article) {
        model.addAttribute("pageTitle", "Товары");
        model.addAttribute("activePage", "products");
        model.addAttribute("productForm", form);
        model.addAttribute("products", productService.findAll());
        model.addAttribute("searchId", id);
        model.addAttribute("searchName", name);
        model.addAttribute("searchArticle", article);
    }
}
