package ch.martinelli.demo.jooq.data.dto;

import java.util.Objects;

public record AthleteWithClubNameDTO(Long id, String firstName, String lastName, String clubName) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (AthleteWithClubNameDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
