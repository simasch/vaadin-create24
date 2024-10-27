package ch.martinelli.demo.jooq.views.athlete;

import ch.martinelli.demo.jooq.data.AthleteRepository;
import ch.martinelli.demo.jooq.db.tables.records.AthleteRecord;
import ch.martinelli.demo.jooq.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import io.seventytwo.vaadinjooq.util.VaadinJooqUtil;

import static ch.martinelli.demo.jooq.db.tables.Athlete.ATHLETE;

@PageTitle("Athletes")
@Route(value = "athletes", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class AthleteView extends VerticalLayout {

    private final AthleteRepository athleteRepository;
    private final AthleteDialog dialog = new AthleteDialog();
    private final CallbackDataProvider<AthleteRecord, Void> dataProvider;

    public AthleteView(AthleteRepository athleteRepository) {
        this.athleteRepository = athleteRepository;

        setSizeFull();

        Grid<AthleteRecord> grid = new Grid<>();
        grid.setSizeFull();
        add(grid);

        Grid.Column<AthleteRecord> idColumn = grid.addColumn(AthleteRecord::getId).setHeader("ID")
                .setSortable(true).setSortProperty(ATHLETE.ID.getName());
        grid.addColumn(AthleteRecord::getFirstName).setHeader("First Name")
                .setSortable(true).setSortProperty(ATHLETE.FIRST_NAME.getName());
        grid.addColumn(AthleteRecord::getLastName).setHeader("Last Name")
                .setSortable(true).setSortProperty(ATHLETE.LAST_NAME.getName());

        grid.addComponentColumn(this::createActions)
                .setHeader(new Button("Add", event -> dialog.open(new AthleteRecord())))
                .setTextAlign(ColumnTextAlign.END);

        dataProvider = new CallbackDataProvider<>(
                q -> athleteRepository.findAll(
                        q.getOffset(), q.getLimit(),
                        VaadinJooqUtil.orderFields(ATHLETE, q)).stream(),
                q -> athleteRepository.count(),
                AthleteRecord::getId
        );
        grid.setDataProvider(dataProvider);

        grid.sort(GridSortOrder.asc(idColumn).build());

        dialog.addSaveListener(event -> {
            athleteRepository.save(event.getAthlete());
            dataProvider.refreshAll();
        });
    }

    private HorizontalLayout createActions(AthleteRecord athlete) {
        Button edit = new Button("Edit", event -> dialog.open(athlete));
        Button delete = new Button("Delete", event ->
                new ConfirmDialog("Delete Athlete",
                        "Are you sure?",
                        "Delete", e -> athleteRepository.deleteById(athlete.getId()),
                        "Cancel", e -> {
                })
                        .open());
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        HorizontalLayout buttons = new HorizontalLayout(edit, delete);
        buttons.setPadding(false);
        buttons.setJustifyContentMode(JustifyContentMode.END);
        return buttons;
    }

}
