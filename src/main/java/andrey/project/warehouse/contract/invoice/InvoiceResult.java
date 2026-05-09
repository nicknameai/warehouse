package andrey.project.warehouse.contract.invoice;

import andrey.project.warehouse.entity.InvoiceType;
import java.time.LocalDate;
import java.util.List;

public class InvoiceResult {

    private Integer id;
    private LocalDate invoiceDate;
    private InvoiceType invoiceType;
    private Integer supplierId;
    private Integer clientId;
    private String comment;
    private List<InvoiceItemResult> items;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
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

    public List<InvoiceItemResult> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItemResult> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "InvoiceResult{" +
                "id=" + id +
                ", invoiceDate=" + invoiceDate +
                ", invoiceType=" + invoiceType +
                ", supplierId=" + supplierId +
                ", clientId=" + clientId +
                ", comment='" + comment + '\'' +
                ", items=" + items +
                '}';
    }
}
