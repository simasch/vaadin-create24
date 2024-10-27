package ch.martinelli.demo.jooq.data;

import ch.martinelli.demo.jooq.db.tables.records.AthleteRecord;
import ch.martinelli.demo.jooq.db.tables.records.AthleteViewRecord;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static ch.martinelli.demo.jooq.db.tables.Athlete.ATHLETE;
import static ch.martinelli.demo.jooq.db.tables.AthleteView.ATHLETE_VIEW;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.field;

@Repository
public class AthleteRepository {

    private final DSLContext dslContext;

    public AthleteRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public List<AthleteDTO> findAll(int offset, int limit, List<OrderField<?>> orderBy) {
        return dslContext
                .select(ATHLETE.ID, ATHLETE.FIRST_NAME, ATHLETE.LAST_NAME,
                        concat(ATHLETE.club().ABBREVIATION, field("' '"), ATHLETE.club().NAME))
                .from(ATHLETE)
                .orderBy(orderBy)
                .offset(offset)
                .limit(limit)
                .fetch(mapping(AthleteDTO::new));
    }

    public List<AthleteViewRecord> findAllFromView(int offset, int limit, List<OrderField<?>> orderBy) {
        return dslContext
                .selectFrom(ATHLETE_VIEW)
                .orderBy(orderBy)
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    public int count() {
        return dslContext.fetchCount(ATHLETE);
    }

    public Optional<AthleteRecord> findById(Long id) {
        return dslContext
                .selectFrom(ATHLETE)
                .where(ATHLETE.ID.eq(id))
                .fetchOptional();
    }

    @Transactional
    public void save(AthleteRecord athlete) {
        dslContext.attach(athlete);
        athlete.store();
    }

    @Transactional
    public void deleteById(Long id) {
        dslContext.deleteFrom(ATHLETE).where(ATHLETE.ID.eq(id)).execute();
    }

}