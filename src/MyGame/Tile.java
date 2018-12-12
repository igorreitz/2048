package MyGame;

import java.awt.*;

/**
 * Класс, описывающий одну плитку
 *
 * @author Igor Reitz on 08.12.2018
 */
public class Tile {
    /**
     * уровень доступа по умолчанию
     */
    int value;

    public Tile(int value) {
        this.value = value;
    }

    public Tile() {
        this.value = 0;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    /**
     * @return true в случае, если значение поля value равно 0, иначе - false
     */
    public boolean isEmpty() {
        return value == 0;
    }

    /**
     * @return новый цвет(объект типа Color) (0x776e65) в случае, если вес плитки меньше 16, иначе - 0xf9f6f2.
     */
    public Color getFontColor() {
        return value < 16 ? new Color(0x776e65) : new Color(0xf9f6f2);
    }

    /**
     * @return цвет плитки в зависимости от ее веса
     */
    public Color getTileColor() {
        Color color;
        switch (value) {
            case 0:
                color = new Color(0xcdc1b4);
                break;
            case 2:
                color = new Color(0xeee4da);
                break;
            case 4:
                color = new Color(0xede0c8);
                break;
            case 8:
                color = new Color(0xf2b179);
                break;
            case 16:
                color = new Color(0xf59563);
                break;
            case 32:
                color = new Color(0xf67c5f);
                break;
            case 64:
                color = new Color(0xf65e3b);
                break;
            case 128:
                color = new Color(0xedcf72);
                break;
            case 256:
                color = new Color(0xedcc61);
                break;
            case 512:
                color = new Color(0xedc850);
                break;
            case 1024:
                color = new Color(0xedc53f);
                break;
            case 2048:
                color = new Color(0xedc22e);
                break;
            default:
                color = new Color(0xff0000);
                break;
        }
        return color;
    }
}
