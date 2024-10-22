package ch.martinelli.demo.jooq.views.product;

import ch.martinelli.demo.jooq.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Products")
@Route(value = "products", layout = MainLayout.class)
public class ProductView extends VerticalLayout {

    public ProductView() {
    }

}
