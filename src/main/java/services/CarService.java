package services;

import models.entity.Engine;
import models.service.CarServiceModel;

import java.util.List;

public interface CarService {

    List<CarServiceModel> getAll();

    void createCar(String brand, String model, String year, String engine, String username);
}
