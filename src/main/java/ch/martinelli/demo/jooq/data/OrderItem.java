package ch.martinelli.demo.jooq.data;

public record OrderItem(Long id, int quantity, Product product) {
}