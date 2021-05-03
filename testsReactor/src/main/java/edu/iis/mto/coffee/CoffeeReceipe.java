package edu.iis.mto.coffee;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Map;

public class CoffeeReceipe {

    private final Map<CoffeeSize, Integer> waterAmounts;
    private final int milkAmount;

    private CoffeeReceipe(Builder builder) {
        this.waterAmounts = requireNonNull(builder.waterAmounts);
        this.milkAmount = builder.milkAmount < 0 ? 0 : builder.milkAmount;
    }

    public int getMilkAmount() {
        return milkAmount;
    }

    public boolean withMilk() {
        return milkAmount > 0;
    }

    public Integer getWaterAmount(CoffeeSize size) {
        return this.waterAmounts.get(size);
    }

    @Override
    public String toString() {
        return "CoffeeReceipe [waterAmounts=" + waterAmounts + ", milkAmount=" + milkAmount + "]";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Map<CoffeeSize, Integer> waterAmounts = Collections.emptyMap();
        private int milkAmount;

        private Builder() {}

        public Builder withWaterAmounts(Map<CoffeeSize, Integer> waterAmounts) {
            this.waterAmounts = waterAmounts;
            return this;
        }

        public Builder withMilkAmount(int milkAmount) {
            this.milkAmount = milkAmount;
            return this;
        }

        public CoffeeReceipe build() {
            return new CoffeeReceipe(this);
        }
    }

}
