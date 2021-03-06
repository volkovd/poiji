package com.poiji.deserialize;

import com.poiji.deserialize.model.Car;
import com.poiji.deserialize.model.Vehicle;
import com.poiji.exception.InvalidExcelFileExtension;
import com.poiji.bind.Poiji;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by hakan on 17/01/2017.
 */
@RunWith(Parameterized.class)
public class InheritanceTest {

    private String path;
    private List<Car> expectedCars;
    private Class<?> expectedException;

    public InheritanceTest(String path, List<Car> expectedCars, Class<?> expectedException) {
        this.path = path;
        this.expectedCars = expectedCars;
        this.expectedException = expectedException;
    }

    @Parameterized.Parameters(name = "{index}: ({0})={1}")
    public static Iterable<Object[]> queries() throws Exception {
        return Arrays.asList(new Object[][]{
                {"src/test/resources/cars.xlsx", unmarshalling(), null},
                {"src/test/resources/cars.xls", unmarshalling(), null},
                {"src/test/resources/cars.xl", unmarshalling(), InvalidExcelFileExtension.class},
        });
    }

    @Test
    public void shouldMapExcelToJava() {

        try {
            List<Car> actualCars = Poiji.fromExcel(new File(path), Car.class);

            assertThat(actualCars, notNullValue());
            assertThat(actualCars.size(), not(0));
            assertThat(actualCars.size(), is(expectedCars.size()));

            Vehicle vehicle1 = actualCars.get(0);
            assertThat(vehicle1, instanceOf(Car.class));

            Car actualCar1 = (Car) vehicle1;
            Car actualCar2 = actualCars.get(1);

            Car expectedCar1 = expectedCars.get(0);
            Car expectedCar2 = expectedCars.get(1);

            assertThat(actualCar1.getnOfSeats(), is(expectedCar1.getnOfSeats()));
            assertThat(actualCar1.getName(), is(expectedCar1.getName()));
            assertThat(actualCar1.getYear(), is(expectedCar1.getYear()));

            assertThat(actualCar2.getnOfSeats(), is(expectedCar2.getnOfSeats()));
            assertThat(actualCar2.getName(), is(expectedCar2.getName()));
            assertThat(actualCar2.getYear(), is(expectedCar2.getYear()));

        } catch (Exception e) {
            if (expectedException == null) {
                e.printStackTrace();
                fail(e.getMessage());
            } else {
                assertThat(e, instanceOf(expectedException));
            }
        }
    }

    private static List<Car> unmarshalling() {
        List<Car> cars = new ArrayList<Car>(2);

        Car car1 = new Car();
        car1.setName("Honda Civic");
        car1.setYear(2017);
        car1.setnOfSeats(4);
        cars.add(car1);

        Car car2 = new Car();
        car2.setName("Chevrolet Corvette");
        car2.setYear(2016);
        car2.setnOfSeats(2);
        cars.add(car2);
        return cars;
    }
}
