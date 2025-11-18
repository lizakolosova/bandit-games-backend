package be.kdg.security.api.dtos;

/**
 * DTO for demo
 */
public class UnsecuredMessageDto {

    private static final String message = "I'm unsecured";

    public String getMessage() {
        return message;
    }
}
