package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.List;

public class LayersContent implements Comparable<LayersContent>{

    final private String layerName;
    final private List<Figure> figures = new ArrayList<>();
    private boolean isVisible = true;


    public LayersContent(String layerName) {
        this.layerName = layerName;
    }

    public void addFigure(Figure figure) { figures.add(figure); }

    public void deleteFigure(Figure figure) { figures.remove(figure); }

    public void divideFigure(Figure figure){
        if(!figures.contains(figure))
            throw new RuntimeException("Attempting to divide nonexistent figure");
        //list.remove(figure);
        figure.move(figure.getCenterPoint().substractX(figure.getWidth() / 4));
        figure.resize(figure.getWidth() / 2, figure.getHeight() / 2);

        Figure dupl = figure.duplicate();
        dupl.move(figure.getCenterPoint().addX(figure.getWidth()));
        dupl.setColors(new ArrayList<>(figure.getColors()));

        figures.add(dupl);
        //   list.add(figure);
    }

    public String getLayerName() {
        return layerName;
    }

    public List<Figure> getFigures() {
        return figures;
    }

    public boolean isVisible() { return isVisible; }

    public void show() { isVisible = true; }

    public void hide() { isVisible = false; }


    public int compareTo(LayersContent other) {
        return this.getLayerName().compareTo(other.getLayerName());
    }
}

