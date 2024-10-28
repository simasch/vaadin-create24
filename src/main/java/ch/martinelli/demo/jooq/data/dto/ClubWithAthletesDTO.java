package ch.martinelli.demo.jooq.data.dto;

import java.util.List;

public record ClubWithAthletesDTO(Long id, String abbreviation, String name, List<AthleteDTO> athletes) {
}
