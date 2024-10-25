package ch.martinelli.demo.jooq.data;

public record Customer(Long id, String firstName, String lastName, String street, String postalCode, String city) {
}
