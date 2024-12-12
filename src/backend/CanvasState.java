package backend;

import backend.model.Figure;
import backend.model.Point;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CanvasState<T> {

    /*
    Cada layer es unica, se mantiene su orden de insercion;
     */
    private final Map<Integer, Layer> layers = new TreeMap<>();

    /*
    Agrega una figura a la capa especificada, si no existe la capa la crea
     */
    public CanvasState(){
        layers.put(1, new Layer());
    }
    public void addFigure(Figure figure) {
        //if(layers.(layerIndex) == null)
         //   layers.put(layerIndex, new Layer());
        //layers.get(layerIndex).addFigure(figure);
        layers.get(1).addFigure(figure);
    }
    /*
    Borra una figura de la capa seleccionada, si no existe no hace nada
     */
    public void deleteFigure(Figure figure) {
        //if(layers.containsValue());
        layers.get(1).removeFigure(figure);
    }
    public void divideFigure(Figure figure){
     //   layers.get(1).divideFigure(figure);
       // if(!list.contains(figure))
        //   throw new RuntimeException("Attempting to divide nonexistent figure");
        //list.remove(figure);
        figure.move(figure.getCenterPoint().substractX(figure.getWidth() / 4));
        figure.resize(figure.getWidth() / 2, figure.getHeight() / 2);
        Figure dupl = figure.duplicate();
        dupl.move(figure.getCenterPoint().addX(figure.getWidth()));
        addFigure(dupl);
    }

    private List<Figure> collectFigures(boolean collectHidden){
        List<Figure> returnIterable = new ArrayList<>();
        for(Layer l : layers.values()){
            if(collectHidden || !l.isHidden())
                returnIterable.addAll(l.figures());
        }
        return returnIterable;
    }

    public Iterable<Figure> figures(){
        return collectFigures(true);
    }
    public Iterable<Figure> visibleFigures(){
        return collectFigures(false);
    }
    public Iterable<Figure> visibleFiguresAtPoint(Point p){
        List<Figure> returnIterable = new ArrayList<>();
        for(Figure fig: collectFigures(false)){
            if(fig.pointBelongs(p))
                returnIterable.add(fig);
        }
        return returnIterable;
    }
}

