document.addEventListener("DOMContentLoaded", () => {
    const invoiceTypeSelect = document.querySelector("[data-invoice-type]");
    const supplierField = document.querySelector('[data-party-field="supplier"]');
    const clientField = document.querySelector('[data-party-field="client"]');
    const lineItems = document.querySelector("[data-line-items]");
    const addButton = document.querySelector("[data-add-item]");

    const togglePartyFields = () => {
        if (!invoiceTypeSelect || !supplierField || !clientField) {
            return;
        }

        const isIncoming = invoiceTypeSelect.value === "INCOMING";
        supplierField.hidden = !isIncoming;
        clientField.hidden = isIncoming;
    };

    const reindexRows = () => {
        const rows = lineItems?.querySelectorAll(".line-item") ?? [];
        rows.forEach((row, index) => {
            row.querySelectorAll("select, input").forEach((field) => {
                const currentName = field.getAttribute("name");
                if (currentName) {
                    field.setAttribute("name", currentName.replace(/items\[\d+]/, `items[${index}]`));
                }

                const currentId = field.getAttribute("id");
                if (currentId) {
                    field.setAttribute("id", currentId.replace(/items\d+/, `items${index}`));
                }
            });
        });
    };

    const buildRow = (index) => {
        const firstRow = lineItems?.querySelector(".line-item");
        if (!firstRow) {
            return null;
        }

        const clone = firstRow.cloneNode(true);
        clone.querySelectorAll("input").forEach((field) => {
            field.value = "";
        });
        clone.querySelectorAll("select").forEach((field) => {
            field.selectedIndex = 0;
        });
        lineItems.appendChild(clone);
        reindexRows();
        const newRow = lineItems.querySelectorAll(".line-item")[index];
        return newRow;
    };

    invoiceTypeSelect?.addEventListener("change", togglePartyFields);
    togglePartyFields();

    addButton?.addEventListener("click", () => {
        const rowCount = lineItems?.querySelectorAll(".line-item").length ?? 0;
        buildRow(rowCount);
    });

    lineItems?.addEventListener("click", (event) => {
        const button = event.target.closest("[data-remove-item]");
        if (!button) {
            return;
        }

        const rows = lineItems.querySelectorAll(".line-item");
        if (rows.length === 1) {
            rows[0].querySelectorAll("input").forEach((field) => {
                field.value = "";
            });
            rows[0].querySelectorAll("select").forEach((field) => {
                field.selectedIndex = 0;
            });
            return;
        }

        button.closest(".line-item")?.remove();
        reindexRows();
    });
});
