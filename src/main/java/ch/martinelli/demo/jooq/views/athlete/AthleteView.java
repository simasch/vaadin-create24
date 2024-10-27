package ch.martinelli.demo.jooq.views.athlete;

import ch.martinelli.demo.jooq.data.AthleteRepository;
import ch.martinelli.demo.jooq.db.tables.records.AthleteRecord;
import ch.martinelli.demo.jooq.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import io.seventytwo.vaadinjooq.util.VaadinJooqUtil;

import static ch.martinelli.demo.jooq.db.tables.Athlete.ATHLETE;

@PageTitle("Athletes")
@Route(value = "athletes", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class AthleteView extends VerticalLayout {

    private final AthleteDialog dialog = new AthleteDialog();

    public AthleteView(AthleteRepository athleteRepository) {
        setSizeFull();

        Grid<AthleteRecord> grid = new Grid<>();
        grid.setSizeFull();

        Grid.Column<AthleteRecord> idColumn = grid.addColumn(AthleteRecord::getId).setHeader("ID")
                .setSortable(true).setSortProperty(ATHLETE.ID.getName());
        grid.addColumn(AthleteRecord::getFirstName).setHeader("First Name")
                .setSortable(true).setSortProperty(ATHLETE.FIRST_NAME.getName());
        grid.addColumn(AthleteRecord::getLastName).setHeader("Last Name")
                .setSortable(true).setSortProperty(ATHLETE.LAST_NAME.getName());

        grid.addComponentColumn(athlete ->
                        new Button("Edit", event -> dialog.open(athlete)))
                .setHeader(new Button("Add", event -> dialog.open(new AthleteRecord())))
                .setTextAlign(ColumnTextAlign.END);

        add(grid);

        grid.sort(GridSortOrder.asc(idColumn).build());

        grid.setItems(q -> athleteRepository.findAll(
                q.getOffset(), q.getLimit(),
                VaadinJooqUtil.orderFields(ATHLETE, q)).stream());

        dialog.addSaveListener(event -> {
            athleteRepository.save(event.getAthlete());
            grid.getDataProvider().refreshAll();
        });
    }

}
