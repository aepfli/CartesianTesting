package at.schrottner.testing.cartesianTesting.strategy;

public class StringStrategy extends AbstractStrategy<String> {
    public StringStrategy() {
        super(String.class);
    }

    @Override
    public String addToStringInternal(String input) {
        return input;
    }
}
