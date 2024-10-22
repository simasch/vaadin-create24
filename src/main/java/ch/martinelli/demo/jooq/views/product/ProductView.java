package ch.martinelli.demo.jooq.views.product;

import ch.martinelli.demo.jooq.data.ProductDao;
import ch.martinelli.demo.jooq.db.tables.records.ProductRecord;
import ch.martinelli.demo.jooq.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.seventytwo.vaadinjooq.util.VaadinJooqUtil;

import static ch.martinelli.demo.jooq.db.tables.Product.PRODUCT;

@PageTitle("Products")
@Route(value = "products", layout = MainLayout.class)
public class ProductView extends VerticalLayout {

    public ProductView(ProductDao productDao) {
        setSizeFull();
        
        Grid<ProductRecord> grid = new Grid<>();
        grid.setSizeFull();

        grid.addColumn(ProductRecord::getId).setHeader("ID").setSortable(true).setSortProperty(PRODUCT.ID.getName());
        grid.addColumn(ProductRecord::getName).setHeader("Name").setSortable(true).setSortProperty(PRODUCT.NAME.getName());
        grid.addColumn(ProductRecord::getPrice).setHeader("Price").setSortable(true).setSortProperty(PRODUCT.PRICE.getName());

        grid.addComponentColumn(product ->
                new Button("Edit", event -> {
                    ProductDialog dialog = new ProductDialog(p -> {
                        productDao.save(p);
                        grid.getDataProvider().refreshAll();
                    });
                    dialog.open(product);
                }));

        add(grid);

        grid.setItems(q -> productDao.findAll(
                q.getOffset(), q.getLimit(),
                VaadinJooqUtil.orderFields(PRODUCT, q)).stream());
    }

}
