package andrey.project.warehouse.repository;

import andrey.project.warehouse.entity.InvoiceItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Integer> {

    List<InvoiceItem> findByInvoiceId(Integer invoiceId);
}
