package ch.martinelli.demo.jooq.views.athlete;

import ch.martinelli.demo.jooq.db.tables.records.AthleteRecord;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;

public class AthleteDialog extends Dialog {

    private final Binder<AthleteRecord> binder = new Binder<>(AthleteRecord.class);

    public AthleteDialog() {
        FormLayout form = new FormLayout();
        add(form);

        TextField firstName = new TextField("First Name");
        firstName.setAutofocus(true);
        binder.forField(firstName)
                .asRequired("First name is required")
                .bind(AthleteRecord::getFirstName, AthleteRecord::setFirstName);

        TextField lastName = new TextField("Last Name");
        binder.forField(lastName)
                .asRequired("Last name is required")
                .bind(AthleteRecord::getLastName, AthleteRecord::setLastName);

        Select<String> gender = new Select<>();
        gender.setLabel("Gender");
        gender.setItems(List.of("f", "m"));
        binder.forField(gender)
                .asRequired("Gender is required")
                .bind(AthleteRecord::getGender, AthleteRecord::setGender);

        IntegerField yearOfBirth = new IntegerField("Year of Birth");
        binder.forField(yearOfBirth)
                .asRequired("Year of birth is required")
                .bind(AthleteRecord::getYearOfBirth, AthleteRecord::setYearOfBirth);

        form.add(firstName, lastName, gender, yearOfBirth);

        Button cancel = new Button("Cancel", event -> close());
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button save = new Button("Save", event -> {
            if (binder.validate().isOk()) {
                fireEvent(new AthleteSavedEvent(this, binder.getBean()));
                close();
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.setAutofocus(true);
        getFooter().add(cancel, save);
    }

    public void addSaveListener(ComponentEventListener<AthleteSavedEvent> listener) {
        addListener(AthleteSavedEvent.class, listener);
    }

    public void open(AthleteRecord athlete) {
        setHeaderTitle(athlete.getId() != null ? "Edit Athlete " + athlete.getId() : "Create Athlete");
        binder.setBean(athlete);
        super.open();
    }

    public static class AthleteSavedEvent extends ComponentEvent<AthleteDialog> {

        private final transient AthleteRecord athlete;

        public AthleteSavedEvent(AthleteDialog source, AthleteRecord athlete) {
            super(source, false);
            this.athlete = athlete;
        }

        public AthleteRecord getAthlete() {
            return athlete;
        }
    }
}
