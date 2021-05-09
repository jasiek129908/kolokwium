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
            throw new CoffeeMachineException("unknown receipe for order " + order);
        }
        grindCoffee(order.getSize());
        Coffee coffee = create(order);
        if (isMilkCoffee(order.getType())) {
            addMilk(order, coffee);
        }
        return coffee;
    }

    private void addMilk(CoffeeOrder order, Coffee coffee) {
        int milkAmount = receipes.getReceipe(order.getType())
                                 .getMilkAmount();
        try {
            milkProvider.heat();
            int poured = milkProvider.pour(milkAmount);
            if (poured > milkAmount) {
                throw new CoffeeMachineException("milk overfill");
            }
            coffee.setMilkAmout(poured);
        } catch (Exception e) {
            coffee.setMilkAmout(0);
        }
    }

    private boolean isMilkCoffee(CoffeeType type) {
        return receipes.getReceipe(type)
                       .withMilk();
    }

    private void grindCoffee(CoffeeSize coffeeSize) {
        if (!grinder.grind(coffeeSize)) {
            throw new CoffeeMachineException("no coffee beans available");
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
