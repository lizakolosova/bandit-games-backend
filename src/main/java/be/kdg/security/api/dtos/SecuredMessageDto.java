package be.kdg.security.api.dtos;

/**
 * DTO for demo
 */
public class SecuredMessageDto extends UnsecuredMessageDto {

    private String subjectid;
    private String email;
    private String firstName;
    private String lastName;

    private String message;


    private SecuredMessageDto(Builder builder) {
        subjectid = builder.subjectid;
        email = builder.email;
        firstName = builder.firstName;
        lastName = builder.lastName;
        message = builder.message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public static final Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String subjectid;
        private String email;
        private String firstName;
        private String lastName;
        private String message;

        private Builder() {
        }


        public Builder subjectid(String val) {
            subjectid = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder firstName(String val) {
            firstName = val;
            return this;
        }

        public Builder lastName(String val) {
            lastName = val;
            return this;
        }

        public Builder message(String val) {
            message = val;
            return this;
        }

        public SecuredMessageDto build() {
            return new SecuredMessageDto(this);
        }
    }
}
