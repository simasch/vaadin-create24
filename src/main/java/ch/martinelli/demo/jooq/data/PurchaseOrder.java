package ch.martinelli.demo.jooq.data;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseOrder(Long id, LocalDateTime orderDate,
                     Customer customer, List<OrderItem> items) {
}