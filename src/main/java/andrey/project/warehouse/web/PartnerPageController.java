package andrey.project.warehouse.web;

import andrey.project.warehouse.entity.Client;
import andrey.project.warehouse.entity.Supplier;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.service.ClientService;
import andrey.project.warehouse.service.SupplierService;
import andrey.project.warehouse.web.form.ClientForm;
import andrey.project.warehouse.web.form.SupplierForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/partners")
public class PartnerPageController {

    private final SupplierService supplierService;
    private final ClientService clientService;

    public PartnerPageController(SupplierService supplierService, ClientService clientService) {
        this.supplierService = supplierService;
        this.clientService = clientService;
    }

    @GetMapping
    public String showPage(@RequestParam(defaultValue = "suppliers") String tab,
                           @RequestParam(required = false) Integer supplierId,
                           @RequestParam(required = false) String supplierName,
                           @RequestParam(required = false) Integer clientId,
                           @RequestParam(required = false) String clientName,
                           Model model) {
        preparePage(model, tab, new SupplierForm(), new ClientForm());

        if ("suppliers".equals(tab)) {
            handleSupplierSearch(model, supplierId, supplierName);
        } else {
            handleClientSearch(model, clientId, clientName);
        }

        model.addAttribute("supplierSearchId", supplierId);
        model.addAttribute("supplierSearchName", supplierName);
        model.addAttribute("clientSearchId", clientId);
        model.addAttribute("clientSearchName", clientName);
        return "partners";
    }

    @PostMapping("/suppliers")
    public String createSupplier(@RequestParam(defaultValue = "suppliers") String tab,
                                 @ModelAttribute("supplierForm") SupplierForm form,
                                 Model model) {
        preparePage(model, tab, form, new ClientForm());

        try {
            Supplier supplier = supplierService.addEntity(
                    form.getName(),
                    form.getPhone(),
                    form.getEmail(),
                    form.getAddress(),
                    form.getDescription()
            );
            model.addAttribute("supplierSuccessMessage", "Поставщик добавлен: ID " + supplier.getId());
            model.addAttribute("supplierForm", new SupplierForm());
        } catch (ValidationException | IllegalArgumentException ex) {
            model.addAttribute("supplierFormError", ex.getMessage());
        }

        return "partners";
    }

    @PostMapping("/clients")
    public String createClient(@RequestParam(defaultValue = "clients") String tab,
                               @ModelAttribute("clientForm") ClientForm form,
                               Model model) {
        preparePage(model, tab, new SupplierForm(), form);

        try {
            Client client = clientService.addEntity(
                    form.getName(),
                    form.getPhone(),
                    form.getEmail(),
                    form.getAddress(),
                    form.getDescription()
            );
            model.addAttribute("clientSuccessMessage", "Клиент добавлен: ID " + client.getId());
            model.addAttribute("clientForm", new ClientForm());
        } catch (ValidationException | IllegalArgumentException ex) {
            model.addAttribute("clientFormError", ex.getMessage());
        }

        return "partners";
    }

    private void preparePage(Model model, String tab, SupplierForm supplierForm, ClientForm clientForm) {
        model.addAttribute("pageTitle", "Поставщики и клиенты");
        model.addAttribute("activePage", "partners");
        model.addAttribute("activeTab", "clients".equals(tab) ? "clients" : "suppliers");
        model.addAttribute("supplierForm", supplierForm);
        model.addAttribute("clientForm", clientForm);
        model.addAttribute("suppliers", supplierService.findAll());
        model.addAttribute("clients", clientService.findAll());
    }

    private void handleSupplierSearch(Model model, Integer supplierId, String supplierName) {
        if (supplierId != null) {
            try {
                model.addAttribute("supplierSearchResult", supplierService.findById(supplierId));
            } catch (IllegalArgumentException ex) {
                model.addAttribute("supplierSearchError", ex.getMessage());
            }
        } else if (supplierName != null && !supplierName.isBlank()) {
            try {
                model.addAttribute("supplierSearchResult", supplierService.findByName(supplierName));
            } catch (ValidationException | IllegalArgumentException ex) {
                model.addAttribute("supplierSearchError", ex.getMessage());
            }
        }
    }

    private void handleClientSearch(Model model, Integer clientId, String clientName) {
        if (clientId != null) {
            try {
                model.addAttribute("clientSearchResult", clientService.findById(clientId));
            } catch (IllegalArgumentException ex) {
                model.addAttribute("clientSearchError", ex.getMessage());
            }
        } else if (clientName != null && !clientName.isBlank()) {
            try {
                model.addAttribute("clientSearchResult", clientService.findByName(clientName));
            } catch (ValidationException | IllegalArgumentException ex) {
                model.addAttribute("clientSearchError", ex.getMessage());
            }
        }
    }
}
