package edu.iis.mto.coffee;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import edu.iis.mto.coffee.machine.*;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
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
        CoffeeOrder coffeeOrder = CoffeeOrder.builder()
                .withSize(CoffeeSize.SMALL)
                .withType(CoffeeType.ESPRESSO)
                .build();

        assertThrows(CoffeeMachineException.class,()->coffeeMachine.make(coffeeOrder));
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
        assertThrows(CoffeeMachineException.class,()->coffeeMachine.make(coffeeOrder));
    }

    @Test
    void shouldCallMilkProviderHeatAndThenPourIfCoffeeIsWithMilk() throws HeaterException {
        coffeeMachine = new CoffeeMachine(coffeeGrinder, milkProvider, coffeeReceipes);
        CoffeeOrder coffeeOrder = CoffeeOrder.builder()
                .withSize(CoffeeSize.SMALL)
                .withType(CoffeeType.ESPRESSO)
                .build();

        CoffeeReceipe coffeeReceipe = CoffeeReceipe.builder()
                .withMilkAmount(10)
                .withWaterAmounts(waterAmounts)
                .build();

        when(coffeeReceipes.getReceipe(CoffeeType.ESPRESSO)).thenReturn(coffeeReceipe);
        when(coffeeGrinder.grind(any(CoffeeSize.class))).thenReturn(true);

        coffeeMachine.make(coffeeOrder);
        InOrder in = inOrder(milkProvider);
        in.verify(milkProvider).heat();
        in.verify(milkProvider).pour(any(Integer.class));
    }

    @Test
    void shouldThrowCoffeeMachineExceptionWhenThereIsAProblemWithMilkProvider() throws HeaterException {
        coffeeMachine = new CoffeeMachine(coffeeGrinder, milkProvider, coffeeReceipes);
        CoffeeOrder coffeeOrder = CoffeeOrder.builder()
                .withSize(CoffeeSize.SMALL)
                .withType(CoffeeType.ESPRESSO)
                .build();

        CoffeeReceipe coffeeReceipe = CoffeeReceipe.builder()
                .withMilkAmount(10)
                .withWaterAmounts(waterAmounts)
                .build();

        when(coffeeReceipes.getReceipe(CoffeeType.ESPRESSO)).thenReturn(coffeeReceipe);
        when(coffeeGrinder.grind(any(CoffeeSize.class))).thenReturn(true);
        doThrow(HeaterException.class).when(milkProvider).heat();
        assertThrows(CoffeeMachineException.class,()->coffeeMachine.make(coffeeOrder));
    }

    @Test
    void onSuccessShouldReturnCoffee() throws HeaterException {
        coffeeMachine = new CoffeeMachine(coffeeGrinder, milkProvider, coffeeReceipes);
        CoffeeOrder coffeeOrder = CoffeeOrder.builder()
                .withSize(CoffeeSize.SMALL)
                .withType(CoffeeType.ESPRESSO)
                .build();

        CoffeeReceipe coffeeReceipe = CoffeeReceipe.builder()
                .withMilkAmount(10)
                .withWaterAmounts(waterAmounts)
                .build();

        when(coffeeReceipes.getReceipe(CoffeeType.ESPRESSO)).thenReturn(coffeeReceipe);
        when(coffeeGrinder.grind(any(CoffeeSize.class))).thenReturn(true);
        Coffee coffee = coffeeMachine.make(coffeeOrder);
        assertNotNull(coffee);
    }

    @Test
    void shouldPourProperAmountOfMilkToACoffee() {
        coffeeMachine = new CoffeeMachine(coffeeGrinder, milkProvider, coffeeReceipes);
        CoffeeOrder coffeeOrder = CoffeeOrder.builder()
                .withSize(CoffeeSize.SMALL)
                .withType(CoffeeType.ESPRESSO)
                .build();

        CoffeeReceipe coffeeReceipe = CoffeeReceipe.builder()
                .withMilkAmount(10)
                .withWaterAmounts(waterAmounts)
                .build();

        when(coffeeReceipes.getReceipe(CoffeeType.ESPRESSO)).thenReturn(coffeeReceipe);
        when(coffeeGrinder.grind(any(CoffeeSize.class))).thenReturn(true);
        when(milkProvider.pour(coffeeReceipe.getMilkAmount())).thenReturn(coffeeReceipe.getMilkAmount());
        Coffee make = coffeeMachine.make(coffeeOrder);
        assertEquals(10,make.getMilkAmout());
    }

}
