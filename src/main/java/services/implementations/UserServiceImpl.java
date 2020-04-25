package services.implementations;

import models.entity.User;
import models.service.UserLoginServiceModel;
import org.modelmapper.ModelMapper;
import services.HashingService;
import services.UserService;
import services.UserValidationService;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class UserServiceImpl implements UserService {

    private final EntityManager entityManager;
    private final HashingService hashingService;
    private final ModelMapper mapper;
    private final UserValidationService userValidationService;

    @Inject
    public UserServiceImpl(EntityManager entityManager,
                           HashingService hashingService,
                           ModelMapper mapper,
                           UserValidationService userValidationService) {
        this.entityManager = entityManager;
        this.hashingService = hashingService;
        this.mapper = mapper;
        this.userValidationService = userValidationService;
    }

    @Override
    public void register(String username, String email, String password, String confirmPassword) {

        userValidationService.canCreateUser(username, email, password, confirmPassword);

        entityManager.getTransaction().begin();

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashingService.hash(password));

        entityManager.persist(user);
        entityManager.getTransaction().commit();
    }

    @Override
    public UserLoginServiceModel login(String username, String password) {
        User user = entityManager.createQuery("SELECT  u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();

        if (user == null) {
            return null;
        }

        if (! user.getPassword().equals(hashingService.hash(password))) {
            return null;
        }

        return mapper.map(user, UserLoginServiceModel.class);
    }

    @Override
    public User getAuthUser(String username) {
        return entityManager.createQuery("SELECT  u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
