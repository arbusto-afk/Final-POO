package backend;

import backend.model.Figure;
import backend.model.Point;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class CanvasState {

//  Cada layer es unica, se mantiene su orden de insercion;

    private final Map<String, Layer> layers = new TreeMap<>();
    private final Set<Figure> selectedFigures = new HashSet<>();
    private final Set<Set<Figure>> figureGroups = new HashSet<>();

    private final Integer DUPLICATEOFFSET = 15;
    private final Integer STARTINGLAYERS = 3;
    private int getLastLayerNumber = 1;
    private String workingLayerName = "Capa 1";

    public CanvasState() {
        while(getLastLayerNumber <= STARTINGLAYERS) {
            initializeNewLayer();
            System.out.println(getLastLayerNumber);
        }
    }

    public void addFigure(Figure figure) {
        layers.get(workingLayerName).addFigureLast(figure);
    }

    public String getFigureLayer(Figure fig) throws FigureNotFoundException {
        for (Map.Entry<String, Layer> entry : layers.entrySet()) {
            if (entry.getValue().getFigures().contains(fig)) {
                return entry.getKey();
            }
        }
        throw new FigureNotFoundException();
    }

    public String initializeNewLayer() {
        String newLayerName = "Capa " + getLastLayerNumber;
        getLastLayerNumber+=1;
        layers.putIfAbsent(newLayerName, new Layer());
        return newLayerName;
    }

    private boolean LayerCheck(String layerName){
        return layers.containsKey(layerName);
    }

    public List<Figure> getAllVisibleFigures(){
        List<Figure> toReturn = new LinkedList<>();
        for(Layer l : layers.values()){
            if(!l.isHidden())
                toReturn.addAll(l.getFigures());
        }
        return toReturn;
    }

    public void setWorkingLayer(String newLayerName) { workingLayerName = newLayerName; }

    public void deleteLayer(String layerName){
        layers.get(layerName);
        layers.remove(layerName);
    }

    //En base al workingIndex, usa regex
    public void showLayer(String WorkingLayerName) {
        layers.get(WorkingLayerName).show();
    }

    public void hideLayer(String WorkingLayerName) {
        layers.get(WorkingLayerName).hide();
    }

    public boolean isLayerHidden(String WorkingLayerName) {
        return layers.get(WorkingLayerName).isHidden();
    }


//  Borra una figura de la capa seleccionada, si no existe lanza una FigureNotFoundException
    public void deleteFigure(Figure figure, String layerName) throws FigureNotFoundException {
        if(!LayerCheck(layerName)){
            throw new LayerNotFoundException("Layer not found!");
        }
        layers.get(layerName).removeFigure(figure);
    }

    public Pair<Figure, Figure> divideFigure(Figure figure, String layerName) throws FigureNotFoundException {
        if(!LayerCheck(layerName)){
            throw new LayerNotFoundException("Layer not found!");
        }
        return layers.get(layerName).divideFigure(figure);
    }

    public void pushSelectedFigureForward(String workingLayerName) {
        for(Figure f : selectedFigures)
            layers.get(workingLayerName).pushToFront(f);
    }

    public void pushSelectedFigureToBottom(String workingLayerName) {
        for(Figure f : selectedFigures)
            layers.get(workingLayerName).sendToBack(f);
    }

    public Iterable<Figure> visibleFiguresAtPoint(Point p){
        List<Figure> returnIterable = new ArrayList<>();
        for(Figure fig: getAllVisibleFigures()){
            if(fig.pointBelongs(p))
                returnIterable.add(fig);
        }
        return returnIterable;
    }

    public Iterable<Figure> selectedFigures() {
        return selectedFigures;
    }

    public void selectFigure(Collection<Figure> figureList){
        this.selectedFigures.addAll(figureList);
    }

    public void selectFigure(Figure fig) {   this.selectedFigures.add(fig);}

    public boolean isSelected(Figure fig) {
        return selectedFigures.contains(fig);
    }

    public void deselectFigures(){
        selectedFigures.clear();
    }

    public Set<Figure> getFigureGroup(Figure fig){
        for(Set<Figure> s : figureGroups){
            if(s.contains(fig))
                return s;
        }
        return null;
    }
    public void groupFigures(Iterable<Figure> figuresToGroup){
        Set<Figure> group = new HashSet<>();
        for(Figure fig : figuresToGroup){

            Set<Figure> currentGroup = getFigureGroup(fig);
            if(currentGroup != null) {
                figureGroups.remove(currentGroup);
                group.addAll(currentGroup);
            } else {
                group.add(fig);
            }
        }
        figureGroups.add(group);
    }
    public void ungroupFigures(Iterable<Figure> figuresToUngroup){
        for(Figure fig : figuresToUngroup){
            if(getFigureGroup(fig) != null){
                figureGroups.remove(getFigureGroup(fig));
            }
        }
    }

    public Integer getDUPLICATEOFFSET() {
        return DUPLICATEOFFSET;
    }
    public Integer getSelectedFiguresCount(){
        return selectedFigures.size();
    }
    public Figure getSingleSelectedFigure() {
        Figure retFig = null;
        if(selectedFigures.isEmpty())
            throw new RuntimeException("Attempting to get a figure from empty canvasState");
        for (Figure fig : selectedFigures) {
            retFig = fig;
            return fig;
        }
        return retFig;
    }
}

