package backend;

import backend.model.Figure;
import backend.model.Point;

import java.util.*;

public class CanvasState {

    private int workingLayer = 1;
    private int lastLayer = 1;

//    private final SequencedMap<Boolean, LayersContent> figuresLayers = new TreeMap<>();

    private final SortedMap<Integer, List<Figure>> figuresLayers = new TreeMap<>();

    private final SortedMap<Integer, List<Figure>> hiddenLayers = new TreeMap<>();


    public void addFigure(Figure figure) {
        if (!figuresLayers.containsKey(workingLayer)) {
            figuresLayers.put(workingLayer, new ArrayList<>());
        }
        figuresLayers.get(workingLayer).add(figure);
    }

    public void deleteFigure(Figure figure) {
        figuresLayers.get(workingLayer).remove(figure);
    }

    public void divideFigure(Figure figure){
        if(!figuresLayers.get(workingLayer).contains(figure))
            throw new RuntimeException("Attempting to divide nonexistent figure");
        //list.remove(figure);
        figure.move(figure.getCenterPoint().substractX(figure.getWidth() / 4));
        figure.resize(figure.getWidth() / 2, figure.getHeight() / 2);
        Figure dupl = figure.duplicate();
        dupl.move(figure.getCenterPoint().addX(figure.getWidth()));
        figuresLayers.get(workingLayer).add(dupl);
     //   list.add(figure);
    }

    public Iterable<Figure> figures() {
        return new ArrayList<>(figuresLayers.get(workingLayer));
    }

    public Iterable<Figure> figuresAtPoint(Point p){
        List<Figure> returnIterable = new ArrayList<>();
        for(Figure fig : figuresLayers.get(workingLayer)){
            if(fig.pointBelongs(p))
                returnIterable.add(fig);
        }
        return returnIterable;
    }

    public int addLayer() {
        figuresLayers.putIfAbsent(lastLayer, new ArrayList<>());
        lastLayer++;
        return lastLayer-1;
    }

    public void deleteLayer() {
        if(isHidden()){
            hiddenLayers.get(workingLayer).clear();
            hiddenLayers.remove(workingLayer);
        }
        else {
            figuresLayers.get(workingLayer).clear();
            figuresLayers.remove(workingLayer);
        }
    }


    public void clearLayer(SortedMap<Integer, List<Figure>> map){
        map.get(workingLayer).clear();
    }

    public int getWorkingLayer() {return workingLayer;}

    public int getLastUsedLayer(){
        if(figuresLayers.lastKey() < hiddenLayers.lastKey())
            return hiddenLayers.lastKey();
        return figuresLayers.lastKey();
    }


    //Si existe la layer, entonces la cambia y retorna true, si no, retorna false.
    public void changeLayer(int newLayer){
        if(figuresLayers.containsKey(newLayer)){
            workingLayer = newLayer;
        }
    }

    public void showLayer(){
        if(hiddenLayers.containsKey(workingLayer)){
            figuresLayers.get(workingLayer).addAll(hiddenLayers.get(workingLayer));
            clearLayer(hiddenLayers);
        }
    }

    public void hideLayer(){
        if(!hiddenLayers.containsKey(workingLayer)){
            hiddenLayers.put(workingLayer, new ArrayList<>());
        }
        hiddenLayers.get(workingLayer).addAll(figuresLayers.get(workingLayer));
        clearLayer(figuresLayers);
    }

    public boolean isHidden(){
        return hiddenLayers.containsKey(workingLayer);
    }
}

