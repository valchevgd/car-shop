package web;

import models.view.CarViewModel;
import models.view.ViewModel;
import org.modelmapper.ModelMapper;
import services.CarService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/cars/all")
public class CarsAllServlet extends HttpServlet {

    private final CarService carService;
    private final ModelMapper mapper;

    @Inject
    public CarsAllServlet(CarService carService, ModelMapper mapper) {
        this.carService = carService;
        this.mapper = mapper;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<CarViewModel> cars = carService.getAll()
                .stream()
                .map(car -> mapper.map(car, CarViewModel.class))
                .collect(Collectors.toList());

        req.setAttribute("viewModel", cars);

        req.getRequestDispatcher("/cars-all.jsp")
                .forward(req, resp);
    }
}
