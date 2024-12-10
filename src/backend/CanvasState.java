package backend;

import backend.model.Figure;
import backend.model.Point;

import java.util.*;

public class CanvasState {

    private String workingLayer;
    private int nextLayerNumber = 1;


    private final SequencedMap<String, LayersContent> figuresLayers = new LinkedHashMap<>();


    public void addFigure(Figure figure) {
        checkLayers(workingLayer);
        figuresLayers.get(workingLayer).addFigure(figure);
    }

    public void deleteFigure(Figure figure) {
        checkLayers(workingLayer);
        figuresLayers.get(workingLayer).deleteFigure(figure);
    }

    public void divideFigure(Figure figure){
        checkLayers(workingLayer);
        figuresLayers.get(workingLayer).divideFigure(figure);
    }

    public Iterable<Figure> figures() {
        List<Figure> returnIterable = new ArrayList<>();
        for(LayersContent arr : figuresLayers.values()){
            if(arr.isVisible())
                returnIterable.addAll(arr.getFigures());
        }
        return returnIterable;
    }

    public Iterable<Figure> figuresAtPoint(Point p){
        List<Figure> returnIterable = new ArrayList<>();
        for(LayersContent arr : figuresLayers.values()){
            if(arr.isVisible()){
                for (Figure fig : arr.getFigures()) {
                    if (fig.pointBelongs(p))
                        returnIterable.add(fig);
                }
            }
        }
        return returnIterable;
    }

    public void addLayer(String newLayerName) {
        figuresLayers.putLast(newLayerName, new LayersContent(newLayerName));
    }

    public void deleteLayer(String layerToDelete) {
        figuresLayers.remove(layerToDelete);
    }


    public String getWorkingLayer() {return workingLayer;}

    public int getNextLayerNumber() {return nextLayerNumber;}

    public void increaseNextLayerNumber() {++nextLayerNumber;}


    //Si existe la layer, entonces la cambia y retorna true, si no, retorna false.
    public void changeLayer(String newLayer){
        if(figuresLayers.containsKey(newLayer)){
            workingLayer = newLayer;
        }
    }

    public void showLayer(){
        checkLayers(workingLayer);
        figuresLayers.get(workingLayer).show();
    }

    public void hideLayer(){
        checkLayers(workingLayer);
        figuresLayers.get(workingLayer).hide();
    }

    public boolean isVisible(){
        checkLayers(workingLayer);
        return figuresLayers.get(workingLayer).isVisible();
    }

    public void pushForward(){
        checkLayers(workingLayer);
        figuresLayers.putLast(workingLayer, push());
    }

    public void pushToBottom(){
        checkLayers(workingLayer);
        figuresLayers.putFirst(workingLayer, push());
    }

    private LayersContent push(){
        checkLayers(workingLayer);
        LayersContent aux = figuresLayers.get(workingLayer);
        figuresLayers.remove(workingLayer);
        return aux;
    }


    private void checkLayers(String layerName){
        if(!figuresLayers.containsKey(layerName)){
            throw new RuntimeException("Attempting to access nonexistent layer");
        }
    }

}

