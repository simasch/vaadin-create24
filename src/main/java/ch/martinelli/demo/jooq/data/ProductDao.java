package ch.martinelli.demo.jooq.data;

import ch.martinelli.demo.jooq.db.tables.records.ProductRecord;
import org.jooq.OrderField;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.martinelli.demo.jooq.db.tables.Product.PRODUCT;

@Repository
public class ProductDao {

    private final DefaultDSLContext dslContext;

    public ProductDao(DefaultDSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public List<ProductRecord> findAll(int offset, int limit, List<OrderField<?>> orderBy) {
        return dslContext.selectFrom(PRODUCT)
                .orderBy(orderBy)
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Transactional
    public void save(ProductRecord product) {
        dslContext.attach(product);
        product.store();
    }

}
