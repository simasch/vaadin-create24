package ch.martinelli.demo.jooq.data;

import java.util.Objects;

public record AthleteDTO(Long id, String firstName, String lastName, String clubName) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AthleteDTO that = (AthleteDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
