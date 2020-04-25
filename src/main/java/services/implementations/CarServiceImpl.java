package services.implementations;

import models.entity.Car;
import models.entity.Engine;
import models.service.CarServiceModel;
import org.modelmapper.ModelMapper;
import services.CarService;
import services.UserService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class CarServiceImpl implements CarService {

    private final ModelMapper mapper;
    private final EntityManager entityManager;
    private final UserService userService;

    @Inject
    public CarServiceImpl(ModelMapper mapper,
                          EntityManager entityManager,
                          UserService userService) {
        this.mapper = mapper;
        this.entityManager = entityManager;
        this.userService = userService;
    }

    @Override
    public List<CarServiceModel> getAll() {
        entityManager.getTransaction();

        return entityManager.createQuery("select c from Car c", Car.class)
                .getResultList()
                .stream()
                .map(c -> mapper.map(c, CarServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void createCar(String brand, String model, String year, String engine, String username) {

        entityManager.getTransaction().begin();

        Car car = new Car();

        car.setBrand(brand);
        car.setModel(model);
        car.setYear(year);
        car.setEngine(Engine.valueOf(engine));
        car.setUser(userService.getAuthUser(username));

        entityManager.persist(car);
        entityManager.getTransaction().commit();
    }
}
