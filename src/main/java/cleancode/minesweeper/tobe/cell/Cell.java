package cleancode.minesweeper.tobe.cell;

public interface Cell {
    final String FLAG_SIGN = "⚑";
    final String UNCHECKED_SIGN = "□";

    boolean hasLandMineCount();

    String getSign();

    boolean isLandMine();

    public void flag();

    public void open();

    public boolean isChecked();

    public boolean isOpened();
}
