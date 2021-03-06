package at.schrottner.testing.cartesianTesting.mockito;

import at.schrottner.testing.cartesianTesting.service.SomeService;
import at.schrottner.testing.cartesianTesting.strategy.AbstractStrategy;
import at.schrottner.testing.cartesianTesting.strategy.IntegerStrategy;
import at.schrottner.testing.cartesianTesting.strategy.StringStrategy;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.jukito.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Junit5ParametrisedTest {

    @Mock
    SomeService someService;

    @BeforeEach
    void setUp() {
        when(someService.someMethod()).thenReturn("mockedMethod");
    }

    @DisplayName("Testing if the Strategy is applicable")
    @ParameterizedTest(name = "{index} ==> strategy=''{0}'', input={1}")
    @ArgumentsSource(CartesianProductProvider.class)
    void isApplicable(AbstractStrategy strategy,
                      TestObject testObject) throws Exception {

        assertThat(strategy.isApplicable(testObject.input))
                .isEqualTo(strategy.getType().equals(testObject.input.getClass()));
        assertThat(strategy.isApplicable(null)).isEqualTo(false);
    }


    @DisplayName("Testing if addedToString is working")
    @ParameterizedTest(name = "{index} ==> strategy=''{0}'', input={1}")
    @ArgumentsSource(CartesianProductProvider.class)
    void addToString(AbstractStrategy strategy,
                     TestObject testObject) throws Exception {
        strategy.setSomeService(someService);

        if (strategy.isApplicable(testObject.input)) {
            assertThat(strategy.addToString(testObject.input))
                    .satisfies(testObject.check);
        } else {
            assertThat(strategy.addToString(testObject.input))
                    .isEqualTo("");
        }

        verify(someService, times(1)).someMethod();
    }

    static class CartesianProductProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return getStrategies().flatMap(strategy ->
                    getData().map(xTestData -> Arguments.of(strategy, xTestData)));
        }

        Stream<AbstractStrategy> getStrategies() {
            return Stream.of(new StringStrategy(),
                    new IntegerStrategy());
        }

        Stream<TestObject> getData() {
            return Stream.of(
                    new TestObject("Test", s -> assertThat(s).isEqualTo("Test")),
                    new TestObject(Integer.valueOf("2"), s -> assertThat(s).isEqualTo("2")),
                    new TestObject(Float.valueOf("3"))
            );
        }
    }

    static class TestObject {
        Object input;
        Consumer<String> check;

        TestObject(Object input) {
            this.input = input;
        }

        TestObject(Object input, Consumer<String> check) {
            this.input = input;
            this.check = check;
        }

        @Override
        public String toString() {
            return input.toString();
        }
    }
}