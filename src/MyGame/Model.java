package MyGame;

import java.util.*;

/**
 * Класс для описания внутренней логики игры
 *
 * @author Igor Reitz on 08.12.2018
 */
public class Model {
    /**
     * ширина игрового поля
     */
    private static final int FIELD_WIDTH = 4;
    /**
     * массив с плитками
     */
    private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
    /**
     * текущий счет
     */
    int score;
    /**
     * максимальный вес плитки на игровом поле
     */
    int maxTile;
    /**
     * предыдущие состояния игрового поля
     */
    private Stack<Tile[][]> previousStates = new Stack<>();
    /**
     * предыдущие счета
     */
    private Stack<Integer> previousScores = new Stack<>();

    /**
     * Конструктор без параметров инициализирующий игровое поле и заполняющий его пустыми плитками.
     */
    public Model() {
        resetGameTiles();
        this.score = 0;
        this.maxTile = 0;
    }

    /**
     * Метод для сохранения состояния игрового поля
     *
     * @param tile массив с игровым полем
     */
    private void saveState(Tile[][] tile) {
        Tile[][] tempTile = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < tile.length; i++) {
            for (int j = 0; j < tile[i].length; j++) {
                tempTile[i][j] = new Tile(tile[i][j].value);
            }
        }

        previousScores.push(score);
        previousStates.push(tempTile);
    }

    /**
     * Метод для отката к предыдущему состоянию
     */
    void rollback() {
        if (!previousStates.empty() && !previousScores.empty()) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }

    /**
     * @return true если можно сдлать ход, иначе false.
     */
    boolean canMove() {
        //если есть пустые клетки
        if (!getEmptyTiles().isEmpty())
            return true;
        //если есть клетки, которые можно объединить
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 1; j < gameTiles.length; j++) {
                if (gameTiles[i][j].value == gameTiles[i][j - 1].value)
                    return true;
            }
        }
        for (int j = 0; j < gameTiles.length; j++) {
            for (int i = 1; i < gameTiles.length; i++) {
                if (gameTiles[i][j].value == gameTiles[i - 1][j].value)
                    return true;
            }
        }
        return false;
    }

    /**
     * Метод для того, чтобы при необходимости
     * начать новую игру, не приходилось создавать новую модель, а можно было бы просто вернуться в начальное состояние, вызвав его.
     */
    void resetGameTiles() {
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    /**
     * @return список со свободными плитками
     */
    private List<Tile> getEmptyTiles() {
        List<Tile> result = new ArrayList<>();

        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                if (gameTiles[i][j].isEmpty())
                    result.add(gameTiles[i][j]);
            }
        }
        return result;
    }

    /**
     * Метод смотрит, какие плитки пустуют и, если такие имеются,
     * менять вес одной из них, выбранной случайным образом, на 2 или 4 (на 9 двоек должна приходиться 1 четверка).
     */
    private void addTile() {
        List<Tile> emptyTiles = getEmptyTiles();
        if (!emptyTiles.isEmpty())
            emptyTiles.get((int) (emptyTiles.size() * Math.random())).value = Math.random() < 0.9 ? 2 : 4;
    }

    /**
     * @param tiles ряд плиток для сжатия
     * @return менялся ли массив переданных плиток
     */
    private boolean compressTiles(Tile[] tiles) {
        boolean result = false;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length - 1; j++) {
                if (tiles[j].value == 0 && tiles[j + 1].value != 0) {
                    tiles[j].value = tiles[j + 1].value;
                    tiles[j + 1].value = 0;
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Метод объединяет плитки
     *
     * @param tiles ряд плиток
     * @return менялся ли массив переданных методу плиток
     */
    private boolean mergeTiles(Tile[] tiles) {
        boolean result = false;

        for (int j = 0; j < tiles.length - 1; j++) {
            if (tiles[j].value == tiles[j + 1].value && tiles[j].value != 0) {
                tiles[j].value += tiles[j + 1].value;
                score += tiles[j].value;
                tiles[j + 1].value = 0;
                if (tiles[j].value > maxTile)
                    maxTile = tiles[j].value;
                result = true;
            }

        }
        for (int j = 0; j < tiles.length - 1; j++) {
            if (tiles[j].value == 0) {
                tiles[j].value = tiles[j + 1].value;
                tiles[j + 1].value = 0;
            }
        }
        return result;
    }

    Tile[][] getGameTiles() {
        return gameTiles;
    }

    /**
     * Метод вызывает один из методов движения случайным образом
     */
    void randomMove() {
        int n = ((int) (Math.random() * 100)) % 4;
        switch (n) {
            case 0:
                left();
                break;
            case 1:
                right();
                break;
            case 2:
                up();
                break;
            case 3:
                down();
                break;
            default:
                left();
                break;
        }
    }

    /**
     * Метод будет для каждой строки массива gameTiles вызывать методы compressTiles и mergeTiles
     * и добавлять одну плитку с помощью метода addTile в том случае, если это необходимо.
     */
    private void abstractMove() {
        boolean canAddTile = false;
        boolean booleanInString;

        for (int i = 0; i < gameTiles.length; i++) {
            booleanInString = compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i]);
            canAddTile = canAddTile || booleanInString;
        }
        if (canAddTile)
            addTile();
    }

    /**
     * Сдвиг влево
     */
    void left() {
        saveState(gameTiles);
        abstractMove();
    }

    /**
     * Сдвиг вверх
     */
    void up() {
        saveState(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
        abstractMove();
        gameTiles = rotateClockwise(gameTiles);
    }

    /**
     * Сдвиг вправо
     */
    void right() {
        saveState(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
        abstractMove();
        gameTiles = rotateClockwise(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
    }

    /**
     * Сдвиг вниз
     */
    void down() {
        saveState(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
        abstractMove();
        gameTiles = rotateClockwise(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
    }

    /**
     * @return будет возвращать true, в случае, если вес плиток в массиве gameTiles отличается от веса плиток в верхнем массиве стека previousStates.
     */
    private boolean hasBoardChanged() {
        int valueOfGameTiles = 0;
        int valueOfStack = 0;

        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                valueOfGameTiles += gameTiles[i][j].value;
            }
        }

        Tile[][] stackTiles = previousStates.peek(); //прочитать из стэка, но не удалять
        for (int i = 0; i < stackTiles.length; i++) {
            for (int j = 0; j < stackTiles[i].length; j++) {
                valueOfStack += stackTiles[i][j].value;
            }
        }

        return valueOfGameTiles != valueOfStack;
    }

    /**
     * @param move ход
     * @return эффективность хода
     */
    private MoveEfficiency getMoveEfficiency(Move move) {
        move.move();

        if (!hasBoardChanged()) {
            rollback();
            return new MoveEfficiency(-1, 0, move);
        }

        MoveEfficiency moveEfficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);
        rollback();

        return moveEfficiency;
    }

    void autoMove() {
        final int NUMBER_OF_MOVES = 4;
        PriorityQueue<MoveEfficiency> moveEfficiencyPriorityQueue = new PriorityQueue<>(NUMBER_OF_MOVES,Collections.reverseOrder());

        moveEfficiencyPriorityQueue.add(getMoveEfficiency(this::left));
        moveEfficiencyPriorityQueue.add(getMoveEfficiency(this::right));
        moveEfficiencyPriorityQueue.add(getMoveEfficiency(this::up));
        moveEfficiencyPriorityQueue.add(getMoveEfficiency(this::down));

        Move move= moveEfficiencyPriorityQueue.poll().getMove();
        move.move();
    }

    /**
     * Метод для вращения матрицы по часовой стрелке
     *
     * @param matrix исходная матрица
     * @return матрица повернутая на 90 градусов по часовой стрелке
     */
    private static Tile[][] rotateClockwise(Tile[][] matrix) {
        int rowNum = matrix.length;
        int colNum = matrix[0].length;

        Tile[][] result = new Tile[rowNum][colNum];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                result[i][j] = matrix[rowNum - j - 1][i];
            }
        }

        return result;
    }
}
