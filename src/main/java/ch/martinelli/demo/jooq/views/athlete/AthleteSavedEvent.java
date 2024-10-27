package ch.martinelli.demo.jooq.views.athlete;

import ch.martinelli.demo.jooq.db.tables.records.AthleteRecord;
import com.vaadin.flow.component.ComponentEvent;

public final class AthleteSavedEvent extends ComponentEvent<AthleteDialog> {

    private final transient AthleteRecord athlete;

    public AthleteSavedEvent(AthleteDialog source, AthleteRecord athlete) {
        super(source, false);
        this.athlete = athlete;
    }

    public AthleteRecord getAthlete() {
        return athlete;
    }
}
