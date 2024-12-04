package frontend;

import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
    Class in charge of converting backend color format to front end color format and vice versa
 */
public class ColorHandler {

    public javafx.scene.paint.Color awtColorToFxColor(java.awt.Color c){
        return new  javafx.scene.paint.Color(
                (double) c.getRed() / 255,
                (double) c.getGreen() / 255,
                (double)c.getBlue() / 255,
                (double)c.getAlpha() / 255);
    }
    public java.awt.Color fxColorToAwtColor(javafx.scene.paint.Color c){
        return new java.awt.Color(
                (float)c.getRed(),
                (float)c.getGreen(),
                (float)c.getBlue(),
                (float)c.getOpacity());
    }

    public RadialGradient radialGradientFromColorList(java.util.List<Color> colors){
        return new RadialGradient(0, 0, 0.5, 0.5, 0.5, true,
                CycleMethod.NO_CYCLE,
                createStopsFromColorList(colors));
    }
    public LinearGradient linearGradientFromColorList(List<Color> colors) {
        return new LinearGradient(0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE,
                createStopsFromColorList(colors));
    }
    private List<Stop> createStopsFromColorList(List<java.awt.Color> list){
        List<Stop> stops = new ArrayList<>();
        for(java.awt.Color c : list){
            double travelPercentage = (double)stops.size() / (list.size() - 1);
            stops.add(new Stop(travelPercentage, this.awtColorToFxColor(c)));
        }
        return stops;
    }


}
