package ch.martinelli.demo.jooq.data.dto;

import org.jooq.impl.AbstractConverter;

public enum Gender {
    MALE("m", "Male"), FEMALE("f", "Female");

    private final String code;
    private final String text;

    Gender(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static class GenderConverter extends AbstractConverter<String, Gender> {

        public GenderConverter() {
            super(String.class, Gender.class);
        }

        @Override
        public Gender from(String code) {
            return switch (code) {
                case "m" -> MALE;
                case "f" -> FEMALE;
                default -> throw new IllegalStateException("Unexpected value: " + code);
            };
        }

        @Override
        public String to(Gender gender) {
            return gender.getCode();
        }
    }
}
