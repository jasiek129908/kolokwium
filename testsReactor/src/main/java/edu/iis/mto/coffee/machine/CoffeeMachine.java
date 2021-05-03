package edu.iis.mto.coffee.machine;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import edu.iis.mto.coffee.CoffeeReceipe;
import edu.iis.mto.coffee.CoffeeReceipes;
import edu.iis.mto.coffee.CoffeeSize;
import edu.iis.mto.coffee.CoffeeType;

public class CoffeeMachine {

    private final Grinder grinder;
    private final MilkProvider milkProvider;
    private final CoffeeReceipes receipes;

    public CoffeeMachine(Grinder grinder, MilkProvider milkProvider, CoffeeReceipes receipes) {
        this.grinder = requireNonNull(grinder, "ginder == null");
        this.milkProvider = requireNonNull(milkProvider, "milkProvider == null");
        this.receipes = requireNonNull(receipes, "receipes == null");
    }

    public Coffee make(CoffeeOrder order) {
        if (isNull(receipes.getReceipe(order.getType()))) {
            throw new IllegalArgumentException("unknown for order " + order);
        }
        grindCoffee(order.getSize());
        Coffee coffee = create(order);
        addMilk(order, coffee);
        return coffee;
    }

    private void addMilk(CoffeeOrder order, Coffee coffee) {
        if (isMilkCoffee(order.getType())) {
            int milkAmount = receipes.getReceipe(order.getType())
                                     .getMilkAmount();
            milkProvider.heat();
            int poured = milkProvider.pour(milkAmount);
            coffee.setMilkAmout(poured);
        }
    }

    private boolean isMilkCoffee(CoffeeType type) {
        return receipes.getReceipe(type)
                       .withMilk();
    }

    private void grindCoffee(CoffeeSize coffeeSize) {
        if (!grinder.grind(coffeeSize)) {
            throw new NoCoffeeBeansException();
        }
    }

    private Coffee create(CoffeeOrder order) {
        Coffee coffee = new Coffee();
        CoffeeReceipe receipe = receipes.getReceipe(order.getType());
        Integer waterAmount = receipe.getWaterAmount(order.getSize());
        coffee.setWaterAmount(waterAmount);
        return coffee;
    }
}
