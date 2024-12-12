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
    public List<Figure> figures() { return figures; }

    public void addFigure(Figure figure) { figures.add(figure); }
    public void removeFigure(Figure figure)
    {
        if(!figures.contains(figure))
            throw new FigureNotFoundException();
        figures.remove(figure);
    }
    public void divideFigure(Figure figure)
    {
        if(!figures.contains(figure))
           throw new FigureNotFoundException();

        Point leftCenter = figure.getCenterPoint().substractX(figure.getWidth() / 4);
        Point rightCenter = leftCenter.addX(figure.getWidth() / 2);

        figure.resize(figure.getWidth() / 2, figure.getHeight() / 2);
        figure.move(leftCenter);
        addFigure(figure.duplicate().move(rightCenter));
    }

    public void sendToBack(Figure figure)
    {
        removeFigure(figure);
        figures.addFirst(figure);
    }
    public void pushToFront(Figure figure)
    {
        removeFigure(figure);
        figures.addLast(figure);
    }
}
