package site.viosmash.english.util.enums;

public interface JwtClaims {
    String ID_TOKEN = "id_token";
    String SUB = "sub";
    String PREFERRED_NAME = "preferred_name";
    String FIRST_NAME = "first_name";
    String LAST_NAME = "last_name";
    String EMAIL = "email";
    String EXPIRED = "expired";
    String REFRESH_TOKEN = "refresh_token";
    String ROLE_TYPE = "role_type";
}