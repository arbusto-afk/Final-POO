package backend;

import backend.model.Figure;
import backend.model.Point;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class CanvasState<T> {

    /*
    Cada layer es unica, se mantiene su orden de insercion;
     */
    private final Map<Integer, Layer> layers = new TreeMap<>();
    private final Map<Figure, T> colorMap = new HashMap<>();
    private List<Figure> selectedFigures;

    private final Integer STARTINGLAYERS = 3;

    public void setFigureColor(Figure fig, T color){
        colorMap.put(fig, color);
    }
    public T getFigureColor(Figure fig){
        return colorMap.get(fig);
    }

    private Layer getOrInitializeLayer(Integer layerIndex) {
        if (layers.containsKey(layerIndex))
            return layers.get(layerIndex);
        Layer l = new Layer();
        layers.put(layerIndex, l);
        return l;
    }

    private void initializeInitialLayers(Integer initialLayerCount){
        for(int i = 0; i < initialLayerCount; i++){
            layers.putIfAbsent(i, new Layer());
        }
    }
    public CanvasState(){
        initializeInitialLayers(STARTINGLAYERS);
        selectedFigures = Collections.emptyList();
    }

    public void addFigure(Figure figure, Integer layerIndex) {
        Layer l = getOrInitializeLayer(layerIndex);
        l.addFigure(figure);
    }

    /*
    Borra una figura de la capa seleccionada, si no existe lanza una FigureNotFoundException
     */
    public void deleteLayer(Integer layerIndex){
        layers.remove(layerIndex);
    }
    public void deleteFigure(Figure figure, Integer layerIndex) throws FigureNotFoundException {
        Layer l = getOrInitializeLayer(layerIndex);
        l.removeFigure(figure);
    }
    public void divideFigure(Figure figure, Integer layerIndex) throws FigureNotFoundException {
        Layer l = getOrInitializeLayer(layerIndex);
        l.divideFigure(figure);
    }

    private List<Figure> collectFigures(boolean collectHidden) {
        List<Figure> figures = new ArrayList<>();
        for (Layer layer : layers.values()) {
            if (collectHidden || !layer.isHidden()) {
                figures.addAll(layer.figures());
            }
        }
        return figures;
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

    public List<Figure> selectedFigures() {
        return selectedFigures;
    }
    public void selectFigure(List<Figure> figureList){
        this.selectedFigures = figureList;
    }
    public boolean isSelected(Figure fig) {
        return selectedFigures.contains(fig);
    }
    public void deselectFigures(){
        selectedFigures = Collections.emptyList();
    }

    public void forEachSelectedFigure(Consumer<Figure> action) {
        if (selectedFigures().isEmpty()) {
            return;
        }
        for (Figure fig : selectedFigures()) {
            action.accept(fig);
        }
    }

    public void forEachVisibleFigure(Consumer<Figure> action) {
        for (Figure fig : visibleFigures()) {
            action.accept(fig);
        }
    }

}

