package andrey.project.warehouse.contract.invoice;

import java.time.LocalDate;
import java.util.List;

public class CreateOutgoingInvoiceCommand {

    private LocalDate invoiceDate;
    private Integer clientId;
    private String comment;
    private List<InvoiceItemCommand> items;

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<InvoiceItemCommand> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItemCommand> items) {
        this.items = items;
    }
}
