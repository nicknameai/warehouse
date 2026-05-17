package andrey.project.warehouse.web;

import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.service.WarehouseStockService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/stock")
public class StockPageController {

    private final WarehouseStockService warehouseStockService;

    public StockPageController(WarehouseStockService warehouseStockService) {
        this.warehouseStockService = warehouseStockService;
    }

    @GetMapping
    public String showPage(@RequestParam(required = false) Integer productId, Model model) {
        model.addAttribute("pageTitle", "Склад");
        model.addAttribute("activePage", "stock");
        model.addAttribute("stockItems", warehouseStockService.findAll());
        model.addAttribute("searchProductId", productId);

        if (productId != null) {
            try {
                model.addAttribute("searchResult", warehouseStockService.findByProductId(productId));
            } catch (ValidationException | IllegalArgumentException ex) {
                model.addAttribute("searchError", ex.getMessage());
            }
        }

        return "stock";
    }
}
