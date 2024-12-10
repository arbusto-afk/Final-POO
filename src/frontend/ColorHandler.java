package frontend;

import javafx.scene.paint.*;

import java.util.ArrayList;
import java.util.List;

public class ColorHandler {

    public RadialGradient radialGradientFromColorList(List<Color> colors){
        return new RadialGradient(0, 0, 0.5, 0.5, 0.5, true,
                CycleMethod.NO_CYCLE,
                createStopsFromColorList(colors));
    }
    public LinearGradient linearGradientFromColorList(List<Color> colors) {
        return new LinearGradient(0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE,
                createStopsFromColorList(colors));
    }
    private List<Stop> createStopsFromColorList(List<Color> list){
        if (list == null) {
            throw new IllegalArgumentException("La lista de colores no puede ser nula");
        }
        List<Stop> stops = new ArrayList<>();
        for(Color c : list){
            double travelPercentage = (double)stops.size() / (list.size() - 1);
            stops.add(new Stop(travelPercentage, c));
        }
        return stops;
    }
}
