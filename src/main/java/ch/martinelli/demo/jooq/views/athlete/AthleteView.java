package ch.martinelli.demo.jooq.views.athlete;

import ch.martinelli.demo.jooq.data.dto.AthleteWithClubNameDTO;
import ch.martinelli.demo.jooq.data.repository.AthleteRepository;
import ch.martinelli.demo.jooq.data.repository.ClubRepository;
import ch.martinelli.demo.jooq.db.tables.records.AthleteRecord;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrderBuilder;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import io.seventytwo.vaadinjooq.util.VaadinJooqUtil;

import static ch.martinelli.demo.jooq.db.tables.Athlete.ATHLETE;

@PageTitle("Athletes")
@Route(value = "athletes")
@RouteAlias(value = "")
public class AthleteView extends VerticalLayout {

    private final AthleteRepository athleteRepository;
    private final AthleteDialog dialog;

    private final Grid<AthleteWithClubNameDTO> grid;

    AthleteView(AthleteRepository athleteRepository, ClubRepository clubRepository) {
        this.athleteRepository = athleteRepository;
        this.dialog = new AthleteDialog(clubRepository);

        setSizeFull();

        grid = new Grid<>();
        grid.setSizeFull();
        add(grid);

        grid.addColumn(AthleteWithClubNameDTO::id).setHeader("ID")
                .setSortable(true).setSortProperty(ATHLETE.ID.getName())
                .setAutoWidth(true);
        var firstName = grid.addColumn(AthleteWithClubNameDTO::firstName).setHeader("First Name")
                .setSortable(true).setSortProperty(ATHLETE.FIRST_NAME.getName())
                .setAutoWidth(true);
        var lastName = grid.addColumn(AthleteWithClubNameDTO::lastName).setHeader("Last Name")
                .setSortable(true).setSortProperty(ATHLETE.LAST_NAME.getName())
                .setAutoWidth(true);
        grid.addColumn(AthleteWithClubNameDTO::clubName).setHeader("Club")
                .setAutoWidth(true);

        grid.addComponentColumn(this::createActions)
                .setHeader(new Button("Add", event -> dialog.open(new AthleteRecord())))
                .setTextAlign(ColumnTextAlign.END);

        grid.setItems(
                q -> athleteRepository.findAll(
                        q.getOffset(), q.getLimit(),
                        VaadinJooqUtil.orderFields(ATHLETE, q)).stream(),
                q -> athleteRepository.count());

        grid.setMultiSort(true);
        grid.sort(new GridSortOrderBuilder<AthleteWithClubNameDTO>()
                .thenAsc(firstName)
                .thenAsc(lastName)
                .build());

        dialog.addSaveListener(event -> {
            athleteRepository.save(event.getAthlete());
            grid.getDataProvider().refreshAll();
        });
    }

    private HorizontalLayout createActions(AthleteWithClubNameDTO athlete) {
        var edit = new Button("Edit", event ->
                athleteRepository.findById(athlete.id()).ifPresent(dialog::open));

        var delete = new Button("Delete", event ->
                new ConfirmDialog(
                        "Delete Athlete",
                        "Are you sure?",
                        "Delete",
                        e -> {
                            athleteRepository.deleteById(athlete.id());
                            grid.getDataProvider().refreshAll();
                        },
                        "Cancel", e -> {
                }
                ).open());
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        var buttons = new HorizontalLayout(edit, delete);
        buttons.setPadding(false);
        buttons.setJustifyContentMode(JustifyContentMode.END);
        return buttons;
    }

}
