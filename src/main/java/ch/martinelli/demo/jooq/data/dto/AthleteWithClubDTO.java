package ch.martinelli.demo.jooq.data.dto;

public record AthleteWithClubDTO(Long id, String firstName, String lastName, ClubDTO club) {
}
