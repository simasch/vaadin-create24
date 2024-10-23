package ch.martinelli.demo.jooq.views.product;

import ch.martinelli.demo.jooq.db.tables.records.ProductRecord;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class ProductDialog extends Dialog {

    private final Binder<ProductRecord> binder = new Binder<>(ProductRecord.class);

    public ProductDialog() {
        setHeaderTitle("Edit Product");

        FormLayout form = new FormLayout();
        add(form);

        TextField name = new TextField("Name");
        binder.forField(name)
                .asRequired("Name is required")
                .bind(ProductRecord::getName, ProductRecord::setName);

        NumberField price = new NumberField("Price");
        binder.forField(price)
                .asRequired("Price is required")
                .bind(ProductRecord::getPrice, ProductRecord::setPrice);

        form.add(name, price);

        Button cancel = new Button("Cancel", event -> close());
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button save = new Button("Save", event -> {
            if (binder.validate().isOk()) {
                fireEvent(new ProductSavedEvent(this, binder.getBean()));
                close();
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.setAutofocus(true);
        getFooter().add(cancel, save);
    }

    public void addSaveListener(ComponentEventListener<ProductSavedEvent> listener) {
        addListener(ProductSavedEvent.class, listener);
    }

    public void open(ProductRecord product) {
        setHeaderTitle("Edit Product " + product.getId());

        super.open();
        binder.setBean(product);
    }

    public static class ProductSavedEvent extends ComponentEvent<ProductDialog> {

        private final transient ProductRecord product;

        public ProductSavedEvent(ProductDialog source, ProductRecord product) {
            super(source, false);
            this.product = product;
        }

        public ProductRecord getProduct() {
            return product;
        }
    }
}
