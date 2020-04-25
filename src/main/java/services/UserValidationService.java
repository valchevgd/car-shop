package services;

public interface UserValidationService {

    boolean canCreateUser(String username, String email, String password, String confirmPassword);
}
