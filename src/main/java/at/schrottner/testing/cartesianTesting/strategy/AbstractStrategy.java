package at.schrottner.testing.cartesianTesting.strategy;

import at.schrottner.testing.cartesianTesting.service.SomeService;

import javax.annotation.Resource;

public abstract class AbstractStrategy<T extends Object> {

    @Resource
    private SomeService someService;

    Class<T> type;

    public AbstractStrategy(Class<T> type) {
        this.type = type;
    }

    public boolean isApplicable(Object object) {
        return object != null && isApplicable(object.getClass());
    }

    public boolean isApplicable(Class clazz) {
        return clazz != null && getType().equals(clazz);
    }

    public Class<T> getType() {
        return type;
    }

    public String addToString(T input) {
        // a Block to verify Dependency Injection
        if(someService != null) {
            someService.someMethod();
        }

        if(isApplicable(input)) {
            return addToStringInternal(input);
        }
        return "";
    }
    
    protected abstract String addToStringInternal(T input);

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public void setSomeService(SomeService someService) {
        this.someService = someService;
    }
}
