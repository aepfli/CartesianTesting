package at.schrottner.testing.cartesianTesting.mockito;

import at.schrottner.testing.cartesianTesting.service.SomeService;
import at.schrottner.testing.cartesianTesting.strategy.AbstractStrategy;
import at.schrottner.testing.cartesianTesting.strategy.IntegerStrategy;
import at.schrottner.testing.cartesianTesting.strategy.StringStrategy;
import com.google.inject.Inject;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
@DisplayName("jukito + mockito Test")
public class JukitoParametrisedTest {

    @Inject
    SomeService someService;

    @DisplayName("Testing if the Strategy is applicable")
    @Test
    public void isApplicable(@All AbstractStrategy strategy,
                             @All TestObject testObject) throws Exception {

        assertThat(strategy.isApplicable(testObject.input))
                .isEqualTo(strategy.getType().equals(testObject.input.getClass()));
        assertThat(strategy.isApplicable(null)).isEqualTo(false);
    }


    @DisplayName("Testing if addedToString is working")
    @Test
    public void addToString(@All AbstractStrategy strategy,
                            @All TestObject testObject) throws Exception {
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

    public static class Module extends JukitoModule {

        @Override
        protected void configureTest() {
            bindMany(AbstractStrategy.class,
                    StringStrategy.class,
                    IntegerStrategy.class);

            bindManyInstances(TestObject.class,
                    new TestObject("Test", s -> assertThat(s).isEqualTo("Test")),
                    new TestObject(Integer.valueOf("2"), s -> assertThat(s).isEqualTo("2")),
                    new TestObject(Float.valueOf("3")));

            bindMock(SomeService.class);
        }
    }
}