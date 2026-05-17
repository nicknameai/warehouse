package andrey.project.warehouse.web.form;

import java.util.ArrayList;
import java.util.List;

public class InvoiceForm {

    private String invoiceType = "INCOMING";
    private Integer supplierId;
    private Integer clientId;
    private String comment;
    private List<InvoiceItemForm> items = new ArrayList<>();

    public InvoiceForm() {
        items.add(new InvoiceItemForm());
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
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

    public List<InvoiceItemForm> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItemForm> items) {
        this.items = items;
    }
}
