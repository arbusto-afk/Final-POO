package backend;

import backend.model.Figure;
import backend.model.Point;

import java.util.ArrayList;
import java.util.List;

public class CanvasState {

    private final List<Figure> list = new ArrayList<>();

    public void addFigure(Figure figure) {
        list.add(figure);
    }

    public void deleteFigure(Figure figure) {
        list.remove(figure);
    }

    public Iterable<Figure> figures() {
        return new ArrayList<>(list);
    }

    public Iterable<Figure> figuresAtPoint(Point p){
        List<Figure> returnIterable = new ArrayList<>();
        for(Figure fig : list){
            if(fig.pointBelongs(p))
                returnIterable.add(fig);
        }
        return returnIterable;
    }

}
