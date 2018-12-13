package MyGame;

import javax.swing.*;

/**
 * Класс - точка входа в программу. Настраивает окно.
 *
 * @author Igor Reitz on 08.12.2018
 */
public class Main {
    /**
     * Точка входа
     *
     * @param args параметры командной строки
     */
    public static void main(String[] args){
        Model model = new Model();
        Controller controller = new Controller(model);
        JFrame game = new JFrame();

        game.setTitle("2048");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(450, 510);
        game.setResizable(false);

        game.add(controller.getView());

        game.setLocationRelativeTo(null);
        game.setVisible(true);

        JOptionPane.showMessageDialog(game, "Управления стрелками UP, DOWN, RIGHT, LEFT. Z - отмена хода, A - автоматический ход, R - случайный ход, N - новая игра.");
    }
}
