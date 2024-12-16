package backend;

import backend.model.Figure;
import backend.model.Point;

import java.util.*;

public class Layer implements Iterable<Figure> {
    private boolean hidden = false;

    private final LinkedList<Figure> figures = new LinkedList<>();

    public void show(){ this.hidden = false; }

    public void hide(){ this.hidden = true;}

    public boolean isHidden() { return this.hidden;}

    public Iterator<Figure> iterator(){ return figures.listIterator(); }
    public LinkedList<Figure> getFigures() { return figures; }

    public void addFigureLast(Figure figure) {figures.addLast(figure);}


    public void removeFigure(Figure figure) {
        if(!figures.contains(figure))
            throw new FigureNotFoundException();
        figures.remove(figure);
    }

    public Pair<Figure, Figure> divideFigure(Figure figure) {
        if(!figures.contains(figure))
           throw new FigureNotFoundException();

        Point leftCenter = figure.getCenterPoint().substractX(figure.getWidth() / 4);
        Point rightCenter = leftCenter.addX(figure.getWidth() / 2);

        figure.resize(figure.getWidth() / 2, figure.getHeight() / 2);
        figure.move(leftCenter);
        Figure dupl = figure.duplicate(0).move(rightCenter);
        addFigureLast(dupl);
        return new Pair<>(figure, dupl);
    }

    public void sendToBack(Figure figure) {
        removeFigure(figure);
        figures.addFirst(figure);
    }
    public void pushToFront(Figure figure) {
        removeFigure(figure);
        figures.addLast(figure);
    }
}
