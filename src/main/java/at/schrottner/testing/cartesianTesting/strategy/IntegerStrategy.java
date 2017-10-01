package at.schrottner.testing.cartesianTesting.strategy;

public class IntegerStrategy extends AbstractStrategy<Integer> {
    public IntegerStrategy() {
        super(Integer.class);
    }

    @Override
    public String addToStringInternal(Integer input) {
        return input.toString();
    }
}
