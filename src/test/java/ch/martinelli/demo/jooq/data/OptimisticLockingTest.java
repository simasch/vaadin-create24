package ch.martinelli.demo.jooq.dat;

import ch.martinelli.demo.jooq.TestConfiguration;
import ch.martinelli.demo.jooq.db.tables.records.ProductRecord;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.context.annotation.Import;

import static ch.martinelli.demo.jooq.db.tables.Product.PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@Import(TestConfiguration.class)
@JooqTest
public class OptimisticLockingTest {

    @Autowired
    private DSLContext dsl;

    @Test
    void optimisticLockingWithRecords() {
        ProductRecord productRecord = dsl.selectFrom(PRODUCT)
                .where(PRODUCT.ID.eq(1L))
                .fetchOne();

        assertThat(productRecord).isNotNull();

        productRecord.setName("test");

        assertThatNoException().isThrownBy(productRecord::store);
    }
}
