package MyGame;

/**
 * Класс для описания эффективности хода
 *
 * @author Igor Reitz on 11.12.2018
 */
public class MoveEfficiency implements Comparable<MoveEfficiency> {
    private int numberOfEmptyTiles;
    private int score;
    private Move move;

    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {
        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    @Override
    public int compareTo(MoveEfficiency o) {
        if (this.numberOfEmptyTiles != o.numberOfEmptyTiles)
            return this.numberOfEmptyTiles > o.numberOfEmptyTiles ? 1 : -1;
        else
            return Integer.compare(this.score, o.score);
    }
}