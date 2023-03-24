package com.example.homework;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarApi {
    private List<Car> carList;

    public CarApi() {
        this.carList = new ArrayList<>();
        carList.add(new Car(1L, "Skoda", "Fabia", "silver"));
        carList.add(new Car(2L, "Seat", "Leon", "yellow"));
        carList.add(new Car(3L, "Opel", "Astra", "black"));
    }

    @GetMapping
    public ResponseEntity<List<Car>> getCars(@RequestParam(required = false) String color) {
        if (color == null) {
            return new ResponseEntity<>(carList, HttpStatus.OK);
        }

        List<Car> cars = carList.stream().filter(car -> car.getColor().equals(color)).toList();
        if (cars.size() > 0) {
            return new ResponseEntity<>(cars, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable long id) {
        Optional<Car> carOptional = carList.stream().filter(car -> car.getId() == id).findFirst();

        if (carOptional.isPresent()) {
            return new ResponseEntity<>(carOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity addCar(@RequestBody Car car) {
        boolean isAdded = carList.add(car);

        if (isAdded) {
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping()
    public ResponseEntity updateCar(@RequestBody Car newCar) {
        Optional<Car> carOptional = carList.stream().filter(car -> car.getId() == newCar.getId()).findFirst();

        if (carOptional.isPresent()) {
            carList.remove(carOptional.get());
            carList.add(newCar);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity modifyCar(@PathVariable long id, @RequestBody Map<String, Object> values) {
        Optional<Car> carOptional = carList.stream().filter(car -> car.getId() == id).findFirst();

        if (carOptional.isPresent()) {
            Car foundCar = carOptional.get();
            carList.remove(foundCar);

            if (values.containsKey("brand")) {
                foundCar.setBrand((String) values.get("brand"));
            }
            if (values.containsKey("model")) {
                foundCar.setModel((String) values.get("model"));
            }
            if (values.containsKey("color")) {
                foundCar.setColor((String) values.get("color"));
            }
            carList.add(foundCar);

            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeCar(long id) {
        Optional<Car> carOptional = carList.stream().filter(car -> car.getId() == id).findFirst();

        if (carOptional.isPresent()) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
