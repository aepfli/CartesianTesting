/*********************************************************************
 * The Initial Developer of the content of this file is NETCONOMY.
 * All portions of the code written by NETCONOMY are property of
 * NETCONOMY. All Rights Reserved.
 *
 * NETCONOMY Software & Consulting GmbH
 * Hilmgasse 4, A-8010 Graz (Austria)
 * FN 204360 f, Landesgericht fuer ZRS Graz
 * Tel: +43 (316) 815 544
 * Fax: +43 (316) 815544-99
 * www.netconomy.net
 *
 * (c) 2017 by NETCONOMY Software & Consulting GmbH
 *********************************************************************/

package at.schrottner.testing.cartesianTesting.strategy;

public abstract class AbstractStrategy<T extends Object> {

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
}
