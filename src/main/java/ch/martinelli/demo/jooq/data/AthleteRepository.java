package ch.martinelli.demo.jooq.data;

import ch.martinelli.demo.jooq.db.tables.records.AthleteRecord;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.martinelli.demo.jooq.db.tables.Athlete.ATHLETE;

@Repository
public class AthleteRepository {

    private final DSLContext dslContext;

    public AthleteRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public List<AthleteRecord> findAll(int offset, int limit, List<OrderField<?>> orderBy) {
        return dslContext.selectFrom(ATHLETE)
                .orderBy(orderBy)
                .offset(offset)
                .limit(limit)
                .fetch();
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