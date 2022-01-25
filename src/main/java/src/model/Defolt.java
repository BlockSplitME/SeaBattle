package src.model;
import java.io.Serializable;

public enum Defolt implements Serializable {
    
    EMPTY,
    HIDE,
    DEAD,
    BANGMISS,BANGHIT,
    SHIP,
    BANG11,BANG21,BANG31,BANG41,BANG42,BANG51;

    public static final int COLUMNS = 10; //кол-во столбцов
    public static final int ROWS = 10; //кол-во строк
    public static final int IMAGE_SIZE = 40; //размер стороны картинки в писелах

    
}