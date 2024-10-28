package exam.myBank.security;

public interface JwtProperties {
    String SECRET = "asdfghjkl";
    int EXPIRATION_TIME = 864000000;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
