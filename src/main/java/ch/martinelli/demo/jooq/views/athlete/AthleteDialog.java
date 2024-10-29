package ch.martinelli.demo.jooq.views.athlete;

import ch.martinelli.demo.jooq.data.dto.Gender;
import ch.martinelli.demo.jooq.data.repository.ClubRepository;
import ch.martinelli.demo.jooq.db.tables.records.AthleteRecord;
import ch.martinelli.demo.jooq.db.tables.records.ClubRecord;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.HashMap;
import java.util.Map;

public class AthleteDialog extends Dialog {

    private final Binder<AthleteRecord> binder = new Binder<>(AthleteRecord.class);
    private final Gender.GenderConverter genderConverter = new Gender.GenderConverter();
    private Map<Long, ClubRecord> clubMap = new HashMap<>();
    private final Select<Long> club;
    private final ClubRepository clubRepository;

    public AthleteDialog(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;

        var form = new FormLayout();
        add(form);

        var firstName = new TextField("First Name");
        firstName.setAutofocus(true);
        binder.forField(firstName)
                .asRequired("First name is required")
                .bind(AthleteRecord::getFirstName, AthleteRecord::setFirstName);

        var lastName = new TextField("Last Name");
        binder.forField(lastName)
                .asRequired("Last name is required")
                .bind(AthleteRecord::getLastName, AthleteRecord::setLastName);

        var gender = new Select<Gender>();
        gender.setLabel("Gender");
        gender.setItems(Gender.values());
        gender.setItemLabelGenerator(Gender::getText);
        binder.forField(gender)
                .asRequired("Gender is required")
                .bind(a -> a.getGender() != null ? genderConverter.from(a.getGender()) : null, (a, g) -> a.setGender(g.getCode()));

        var yearOfBirth = new IntegerField("Year of Birth");
        binder.forField(yearOfBirth)
                .asRequired("Year of birth is required")
                .bind(AthleteRecord::getYearOfBirth, AthleteRecord::setYearOfBirth);

        club = new Select<>();
        club.setLabel("Club");
        club.setItemLabelGenerator(clubId -> {
            ClubRecord clubRecord = clubMap.get(clubId);
            return "%s %s".formatted(clubRecord.getAbbreviation(), clubRecord.getName());
        });
        club.setItems(clubMap.keySet());
        binder.forField(club)
                .bind(AthleteRecord::getClubId, AthleteRecord::setClubId);

        form.add(firstName, lastName, gender, yearOfBirth, club);

        var cancel = new Button("Cancel", event -> close());
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        var save = new Button("Save", event -> {
            if (binder.validate().isOk()) {
                fireEvent(new AthleteSavedEvent(this, binder.getBean()));
                close();
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        getFooter().add(cancel, save);
    }

    public void addSaveListener(ComponentEventListener<AthleteSavedEvent> listener) {
        addListener(AthleteSavedEvent.class, listener);
    }

    public void open(AthleteRecord athlete) {
        this.clubMap = clubRepository.findAll();
        club.setItems(clubMap.keySet());

        binder.setBean(athlete);

        setHeaderTitle(athlete.getId() != null ? "Edit Athlete " + athlete.getId() : "Create Athlete");

        super.open();
    }
}
