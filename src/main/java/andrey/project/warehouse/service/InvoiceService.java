package andrey.project.warehouse.service;

import andrey.project.warehouse.contract.invoice.CreateIncomingInvoiceCommand;
import andrey.project.warehouse.contract.invoice.CreateOutgoingInvoiceCommand;
import andrey.project.warehouse.contract.invoice.InvoiceItemCommand;
import andrey.project.warehouse.contract.invoice.InvoiceItemResult;
import andrey.project.warehouse.contract.invoice.InvoiceResult;
import andrey.project.warehouse.entity.Client;
import andrey.project.warehouse.entity.Invoice;
import andrey.project.warehouse.entity.InvoiceItem;
import andrey.project.warehouse.entity.InvoiceType;
import andrey.project.warehouse.entity.Product;
import andrey.project.warehouse.entity.Supplier;
import andrey.project.warehouse.entity.WarehouseStock;
import andrey.project.warehouse.exception.NotEnoughStockException;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.repository.ClientRepository;
import andrey.project.warehouse.repository.InvoiceItemRepository;
import andrey.project.warehouse.repository.InvoiceRepository;
import andrey.project.warehouse.repository.ProductRepository;
import andrey.project.warehouse.repository.SupplierRepository;
import andrey.project.warehouse.repository.WarehouseStockRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final SupplierRepository supplierRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final WarehouseStockRepository warehouseStockRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          InvoiceItemRepository invoiceItemRepository,
                          SupplierRepository supplierRepository,
                          ClientRepository clientRepository,
                          ProductRepository productRepository,
                          WarehouseStockRepository warehouseStockRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.supplierRepository = supplierRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.warehouseStockRepository = warehouseStockRepository;
    }

    @Transactional
    public InvoiceResult createIncomingInvoice(CreateIncomingInvoiceCommand command) {
        validateIncomingCommand(command);
        validateItems(command.getItems());

        Supplier supplier = supplierRepository.findById(command.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("Supplier with this id was not found."));

        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setInvoiceType(InvoiceType.INCOMING);
        invoice.setSupplier(supplier);
        invoice.setComment(command.getComment());

        Invoice savedInvoice = invoiceRepository.saveAndFlush(invoice);
        List<InvoiceItem> savedItems = saveItems(savedInvoice, command.getItems());
        applyIncomingStock(savedItems);

        return toResult(savedInvoice, savedItems);
    }

    @Transactional
    public InvoiceResult createOutgoingInvoice(CreateOutgoingInvoiceCommand command) {
        validateOutgoingCommand(command);
        validateItems(command.getItems());

        Client client = clientRepository.findById(command.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client with this id was not found."));

        validateOutgoingStock(command.getItems());

        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setInvoiceType(InvoiceType.OUTGOING);
        invoice.setClient(client);
        invoice.setComment(command.getComment());

        Invoice savedInvoice = invoiceRepository.saveAndFlush(invoice);
        List<InvoiceItem> savedItems = saveItems(savedInvoice, command.getItems());
        applyOutgoingStock(savedItems);

        return toResult(savedInvoice, savedItems);
    }

    public InvoiceResult findById(Integer id) {
        if (id == null) {
            throw new ValidationException("Invoice id must not be empty.");
        }

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice with this id was not found."));
        List<InvoiceItem> items = invoiceItemRepository.findByInvoiceId(id);
        return toResult(invoice, items);
    }

    private void validateIncomingCommand(CreateIncomingInvoiceCommand command) {
        if (command == null) {
            throw new ValidationException("Incoming invoice command must not be empty.");
        }

        if (command.getSupplierId() == null) {
            throw new ValidationException("Supplier id must not be empty.");
        }
    }

    private void validateOutgoingCommand(CreateOutgoingInvoiceCommand command) {
        if (command == null) {
            throw new ValidationException("Outgoing invoice command must not be empty.");
        }

        if (command.getClientId() == null) {
            throw new ValidationException("Client id must not be empty.");
        }
    }

    private void validateItems(List<InvoiceItemCommand> items) {
        if (items == null || items.isEmpty()) {
            throw new ValidationException("Invoice must contain at least one item.");
        }

        for (InvoiceItemCommand item : items) {
            if (item.getProductId() == null) {
                throw new ValidationException("Invoice item product id must not be empty.");
            }

            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new ValidationException("Invoice item quantity must be greater than 0.");
            }

            if (item.getPrice() == null || item.getPrice() < 0) {
                throw new ValidationException("Invoice item price must be greater than or equal to 0.");
            }
        }
    }

    private void validateOutgoingStock(List<InvoiceItemCommand> itemCommands) {
        for (InvoiceItemCommand itemCommand : itemCommands) {
            WarehouseStock stock = warehouseStockRepository.findByProductId(itemCommand.getProductId())
                    .orElseThrow(() -> new NotEnoughStockException(
                            "Product with id " + itemCommand.getProductId() + " is not available in warehouse."
                    ));

            if (stock.getQuantity() < itemCommand.getQuantity()) {
                throw new NotEnoughStockException(
                        "Not enough stock for product id " + itemCommand.getProductId() +
                                ". Available: " + stock.getQuantity() +
                                ", requested: " + itemCommand.getQuantity() + "."
                );
            }
        }
    }

    private List<InvoiceItem> saveItems(Invoice invoice, List<InvoiceItemCommand> itemCommands) {
        List<InvoiceItem> savedItems = new ArrayList<>();

        for (InvoiceItemCommand itemCommand : itemCommands) {
            Product product = productRepository.findById(itemCommand.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product with this id was not found."));

            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setInvoice(invoice);
            invoiceItem.setProduct(product);
            invoiceItem.setQuantity(itemCommand.getQuantity());
            invoiceItem.setPrice(itemCommand.getPrice());

            savedItems.add(invoiceItemRepository.saveAndFlush(invoiceItem));
        }

        return savedItems;
    }

    private void applyIncomingStock(List<InvoiceItem> items) {
        for (InvoiceItem item : items) {
            WarehouseStock stock = warehouseStockRepository.findByProductId(item.getProduct().getId())
                    .orElseGet(() -> {
                        WarehouseStock newStock = new WarehouseStock();
                        newStock.setProduct(item.getProduct());
                        newStock.setQuantity(0);
                        return newStock;
                    });

            stock.setQuantity(stock.getQuantity() + item.getQuantity());
            warehouseStockRepository.saveAndFlush(stock);
        }
    }

    private void applyOutgoingStock(List<InvoiceItem> items) {
        for (InvoiceItem item : items) {
            WarehouseStock stock = warehouseStockRepository.findByProductId(item.getProduct().getId())
                    .orElseThrow(() -> new NotEnoughStockException(
                            "Product with id " + item.getProduct().getId() + " is not available in warehouse."
                    ));

            int updatedQuantity = stock.getQuantity() - item.getQuantity();
            if (updatedQuantity > 0) {
                stock.setQuantity(updatedQuantity);
                warehouseStockRepository.saveAndFlush(stock);
            } else {
                warehouseStockRepository.delete(stock);
            }
        }
    }

    private InvoiceResult toResult(Invoice invoice, List<InvoiceItem> items) {
        InvoiceResult result = new InvoiceResult();
        result.setId(invoice.getId());
        result.setInvoiceDate(invoice.getInvoiceDate());
        result.setInvoiceType(invoice.getInvoiceType());
        result.setSupplierId(invoice.getSupplier() != null ? invoice.getSupplier().getId() : null);
        result.setClientId(invoice.getClient() != null ? invoice.getClient().getId() : null);
        result.setComment(invoice.getComment());
        result.setItems(toItemResults(items));
        return result;
    }

    private List<InvoiceItemResult> toItemResults(List<InvoiceItem> items) {
        List<InvoiceItemResult> results = new ArrayList<>();

        for (InvoiceItem item : items) {
            InvoiceItemResult itemResult = new InvoiceItemResult();
            itemResult.setId(item.getId());
            itemResult.setProductId(item.getProduct().getId());
            itemResult.setQuantity(item.getQuantity());
            itemResult.setPrice(item.getPrice());
            results.add(itemResult);
        }

        return results;
    }
}
