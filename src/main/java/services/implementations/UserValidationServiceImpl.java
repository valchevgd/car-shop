package services.implementations;

import models.entity.User;
import services.UserValidationService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidationServiceImpl implements UserValidationService {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final EntityManager entityManager;

    @Inject
    public UserValidationServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean canCreateUser(String username, String email, String password, String confirmPassword) {
        return isPasswordsMatching(password, confirmPassword) &&
                isValidEmail(email) &&
                isUsernameFree(username);
    }

    private boolean isPasswordsMatching(String password, String confirmPassword) {

        if (password.equals(confirmPassword)) {
            return true;
        }

        throw new IllegalArgumentException("Passwords miss match");
    }

    private boolean isValidEmail(String email) {

        if (validate(email)) {
            return true;
        }

        throw new IllegalArgumentException("Email is invalid");
    }

    private static boolean validate(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    private boolean isUsernameFree(String username) {
        List<User> users = entityManager.createQuery("SELECT  u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();

        if (users.isEmpty()) {
            return true;
        }

        throw new IllegalArgumentException("Username is already taken");
    }
}
