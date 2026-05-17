package andrey.project.warehouse.web;

import andrey.project.warehouse.contract.invoice.CreateIncomingInvoiceCommand;
import andrey.project.warehouse.contract.invoice.CreateOutgoingInvoiceCommand;
import andrey.project.warehouse.contract.invoice.InvoiceItemCommand;
import andrey.project.warehouse.contract.invoice.InvoiceResult;
import andrey.project.warehouse.contract.invoice.InvoiceItemResult;
import andrey.project.warehouse.entity.InvoiceType;
import andrey.project.warehouse.entity.Product;
import andrey.project.warehouse.exception.NotEnoughStockException;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.service.ClientService;
import andrey.project.warehouse.service.InvoiceService;
import andrey.project.warehouse.service.ProductService;
import andrey.project.warehouse.service.SupplierService;
import andrey.project.warehouse.web.form.InvoiceForm;
import andrey.project.warehouse.web.form.InvoiceItemForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/invoices")
public class InvoicePageController {

    private final InvoiceService invoiceService;
    private final SupplierService supplierService;
    private final ClientService clientService;
    private final ProductService productService;

    public InvoicePageController(InvoiceService invoiceService,
                                 SupplierService supplierService,
                                 ClientService clientService,
                                 ProductService productService) {
        this.invoiceService = invoiceService;
        this.supplierService = supplierService;
        this.clientService = clientService;
        this.productService = productService;
    }

    @GetMapping
    public String showPage(@RequestParam(required = false) Integer invoiceId, Model model) {
        preparePage(model, new InvoiceForm(), invoiceId);

        if (invoiceId != null) {
            try {
                InvoiceResult result = invoiceService.findById(invoiceId);
                enrichInvoiceDetails(model, result);
            } catch (ValidationException | IllegalArgumentException ex) {
                model.addAttribute("invoiceSearchError", ex.getMessage());
            }
        }

        return "invoices";
    }

    @PostMapping
    public String createInvoice(@ModelAttribute("invoiceForm") InvoiceForm form, Model model) {
        ensureAtLeastOneItem(form);
        preparePage(model, form, null);

        try {
            InvoiceResult result = saveInvoice(form);
            model.addAttribute("invoiceSuccessMessage", "Накладная создана: ID " + result.getId());
            enrichInvoiceDetails(model, result);
            model.addAttribute("invoiceForm", new InvoiceForm());
        } catch (NotEnoughStockException | ValidationException | IllegalArgumentException ex) {
            model.addAttribute("invoiceFormError", ex.getMessage());
        }

        return "invoices";
    }

    private void preparePage(Model model, InvoiceForm form, Integer invoiceId) {
        ensureAtLeastOneItem(form);
        model.addAttribute("pageTitle", "Накладные");
        model.addAttribute("activePage", "invoices");
        model.addAttribute("invoiceForm", form);
        model.addAttribute("products", productService.findAll());
        model.addAttribute("suppliers", supplierService.findAll());
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("invoiceSearchId", invoiceId);
    }

    private void ensureAtLeastOneItem(InvoiceForm form) {
        if (form.getItems() == null || form.getItems().isEmpty()) {
            List<InvoiceItemForm> items = new ArrayList<>();
            items.add(new InvoiceItemForm());
            form.setItems(items);
        }
    }

    private InvoiceResult saveInvoice(InvoiceForm form) {
        if (InvoiceType.OUTGOING.name().equals(form.getInvoiceType())) {
            CreateOutgoingInvoiceCommand command = new CreateOutgoingInvoiceCommand();
            command.setClientId(form.getClientId());
            command.setComment(form.getComment());
            command.setItems(mapItems(form.getItems()));
            return invoiceService.createOutgoingInvoice(command);
        }

        CreateIncomingInvoiceCommand command = new CreateIncomingInvoiceCommand();
        command.setSupplierId(form.getSupplierId());
        command.setComment(form.getComment());
        command.setItems(mapItems(form.getItems()));
        return invoiceService.createIncomingInvoice(command);
    }

    private List<InvoiceItemCommand> mapItems(List<InvoiceItemForm> forms) {
        List<InvoiceItemCommand> commands = new ArrayList<>();

        for (InvoiceItemForm form : forms) {
            if (form.getProductId() == null && form.getQuantity() == null && form.getPrice() == null) {
                continue;
            }

            InvoiceItemCommand command = new InvoiceItemCommand();
            command.setProductId(form.getProductId());
            command.setQuantity(form.getQuantity());
            command.setPrice(form.getPrice());
            commands.add(command);
        }

        return commands;
    }

    private void enrichInvoiceDetails(Model model, InvoiceResult result) {
        model.addAttribute("invoiceDetails", result);
        model.addAttribute("invoicePartyName", resolvePartyName(result));
        model.addAttribute("invoiceProductsById", resolveProducts(result));
    }

    private String resolvePartyName(InvoiceResult result) {
        if (result.getInvoiceType() == InvoiceType.INCOMING && result.getSupplierId() != null) {
            return supplierService.findById(result.getSupplierId()).getName();
        }

        if (result.getInvoiceType() == InvoiceType.OUTGOING && result.getClientId() != null) {
            return clientService.findById(result.getClientId()).getName();
        }

        return "Не указано";
    }

    private Map<Integer, Product> resolveProducts(InvoiceResult result) {
        Map<Integer, Product> productsById = new HashMap<>();

        for (InvoiceItemResult item : result.getItems()) {
            productsById.put(item.getProductId(), productService.findById(item.getProductId()));
        }

        return productsById;
    }
}
