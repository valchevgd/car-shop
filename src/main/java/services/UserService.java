package services;

import models.entity.User;
import models.service.UserLoginServiceModel;

public interface UserService {


    void register(String username, String email, String password, String confirmPassword);

    UserLoginServiceModel login(String username, String password);

    User getAuthUser(String username);
}
