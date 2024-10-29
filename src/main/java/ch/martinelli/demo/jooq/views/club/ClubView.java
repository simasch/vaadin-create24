package ch.martinelli.demo.jooq.views.club;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jooq.DSLContext;

@PageTitle("Clubs")
@Route("clubs")
public class ClubView extends VerticalLayout {

    ClubView(DSLContext dslContext) {
    }
}
