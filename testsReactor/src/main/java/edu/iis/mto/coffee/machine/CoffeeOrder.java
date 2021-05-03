package edu.iis.mto.coffee.machine;

import static java.util.Objects.requireNonNull;

import edu.iis.mto.coffee.CoffeeSize;
import edu.iis.mto.coffee.CoffeeType;

public class CoffeeOrder {

    private final CoffeeSize size;
    private final CoffeeType type;

    private CoffeeOrder(Builder builder) {
        this.size = requireNonNull(builder.size, "size == null");
        this.type = requireNonNull(builder.type, "type == null");
    }

    public CoffeeSize getSize() {
        return size;
    }

    public CoffeeType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "CoffeReceipe [size=" + size + ", type=" + type + "]";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private CoffeeSize size;
        private CoffeeType type;

        private Builder() {}

        public Builder withSize(CoffeeSize size) {
            this.size = size;
            return this;
        }

        public Builder withType(CoffeeType type) {
            this.type = type;
            return this;
        }

        public CoffeeOrder build() {
            return new CoffeeOrder(this);
        }
    }
}
