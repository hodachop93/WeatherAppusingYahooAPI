package jsonmodel;

import java.util.ArrayList;


/**
 * Created by Hop on 08/05/2015.
 */
public class Item {
    private Condition condition;
    private ArrayList<Forecast> forecasts;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public ArrayList<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(ArrayList<Forecast> forecasts) {
        this.forecasts = forecasts;
    }
}
