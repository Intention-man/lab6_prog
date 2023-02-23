package movies_classes;

import org.simpleframework.xml.Element;

/**
 *    Coordinates - created specially for Movie
 *    @param  {Integer} coordX
 *    @param  {int} coordY
 * */

public class Coordinates {
    @Element(name="coordX")
    private Integer coordX; //Значение поля должно быть больше -319, Поле не может быть null
    @Element(name="coordY")
    private int coordY;

    public Coordinates(){}

    public Coordinates(@Element(name="coordX") Integer coordX, @Element(name="coordY") int coordY)
    {
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public Integer getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }
}