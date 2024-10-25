package ch.martinelli.demo.jooq.views.product;

import ch.martinelli.demo.jooq.data.ProductRepository;
import ch.martinelli.demo.jooq.db.tables.records.ProductRecord;
import ch.martinelli.demo.jooq.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.seventytwo.vaadinjooq.util.VaadinJooqUtil;

import static ch.martinelli.demo.jooq.db.tables.Product.PRODUCT;

@PageTitle("Products")
@Route(value = "products", layout = MainLayout.class)
public class ProductView extends VerticalLayout {

    private final ProductDialog dialog = new ProductDialog();

    public ProductView(ProductRepository productRepository) {
        setSizeFull();

        Grid<ProductRecord> grid = new Grid<>();
        grid.setSizeFull();

        Grid.Column<ProductRecord> idColumn = grid.addColumn(ProductRecord::getId).setHeader("ID")
                .setSortable(true).setSortProperty(PRODUCT.ID.getName());
        grid.addColumn(ProductRecord::getName).setHeader("Name")
                .setSortable(true).setSortProperty(PRODUCT.NAME.getName());
        grid.addColumn(ProductRecord::getPrice).setHeader("Price")
                .setSortable(true).setSortProperty(PRODUCT.PRICE.getName());

        grid.addComponentColumn(product ->
                        new Button("Edit", event -> dialog.open(product)))
                .setHeader(new Button("Add", event -> dialog.open(new ProductRecord())))
                .setTextAlign(ColumnTextAlign.END);

        add(grid);

        grid.sort(GridSortOrder.asc(idColumn).build());

        grid.setItems(q -> productRepository.findAll(
                q.getOffset(), q.getLimit(),
                VaadinJooqUtil.orderFields(PRODUCT, q)).stream());

        dialog.addSaveListener(event -> {
            productRepository.save(event.getProduct());
            grid.getDataProvider().refreshAll();
        });
    }

}
