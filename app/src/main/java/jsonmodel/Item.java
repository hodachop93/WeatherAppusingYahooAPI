package jsonmodel;

import java.util.List;


/**
 * Created by Hop on 08/05/2015.
 */
public class Item {
    private Condition condition;
    private List<Forecast> forecast;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }
}
