package edu.iis.mto.coffee;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import edu.iis.mto.coffee.machine.*;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class CoffeeMachineTest {

    @Mock
    private CoffeeGrinder coffeeGrinder;
    @Mock
    private MilkProvider milkProvider;
    @Mock
    private CoffeeReceipes coffeeReceipes;

    private CoffeeMachine coffeeMachine;
    private Map<CoffeeSize, Integer> waterAmounts= new HashMap<>(){{
        put(CoffeeSize.SMALL,5);
        put(CoffeeSize.STANDARD,10);
        put(CoffeeSize.DOUBLE,20);
    }};
    @BeforeEach
    void setUp() throws Exception {
        //coffeeMachine = new CoffeeMachine(coffeeGrinder, milkProvider, coffeeReceipes);

    }

    @Test
    void methodMakeShouldThrowCoffeeMachineExceptionWhenThereIsNoRecipe() {
        coffeeMachine = new CoffeeMachine(coffeeGrinder, milkProvider, coffeeReceipes);
        //when(coffeeReceipes.getReceipe(any(CoffeeType.class))).thenReturn(null);
        CoffeeOrder coffeeOrder = CoffeeOrder.builder()
                .withSize(CoffeeSize.SMALL)
                .withType(CoffeeType.ESPRESSO)
                .build();
        Assertions.assertThrows(CoffeeMachineException.class,()->coffeeMachine.make(coffeeOrder));
    }

    @Test
    void shouldThrowCoffeeMachineExceptionWhenThereOrderedCoffeeSizeIsNonSpecified() {
        coffeeMachine = new CoffeeMachine(coffeeGrinder, milkProvider, coffeeReceipes);
        CoffeeOrder coffeeOrder = CoffeeOrder.builder()
                .withSize(CoffeeSize.SMALL)
                .withType(CoffeeType.ESPRESSO)
                .build();

        CoffeeReceipe coffeeReceipe = CoffeeReceipe.builder()
                .withMilkAmount(10)
                .withWaterAmounts(waterAmounts)
                .build();

        when(coffeeReceipes.getReceipe(any(CoffeeType.class))).thenReturn(coffeeReceipe);
        Assertions.assertThrows(CoffeeMachineException.class,()->coffeeMachine.make(coffeeOrder));
    }
}
