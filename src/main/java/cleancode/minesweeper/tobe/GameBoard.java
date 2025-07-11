package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;

public class GameBoard {
    private static final int LAND_MINE_COUNT = 10;

    private final Cell[][] board;

    public GameBoard(int rowSize, int colSize) {
        board = new Cell[rowSize][colSize];
    }

    public void initializeGame() {
        // 비어있는 셀을 만든다.
        int rowSize = board.length;
        int colSize = board[0].length;

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                board[row][col] = Cell.create();
            }
        }

        // 셀에 지뢰를 할당한다.
        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int landMineCol = new Random().nextInt(rowSize);
            int landMineRow = new Random().nextInt(colSize);
            Cell cell = findCell(landMineRow, landMineCol);
            cell.turnOnLandMine();
        }

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                // 지뢰 셀인 경우, 패스 (근처에 지뢰가 있는지 확인할 필요 없음)
                if (isLandMineCell(row, col)) {
                    continue;
                }
                int count = countNearbyLandMines(row, col);
                Cell cell = findCell(row, col);
                cell.updateNearbyLandMineCount(count);
            }
        }
    }

    public boolean isLandMineCell(int selectedRowIndex, int selectedColumnIndex) {
        Cell cell = findCell(selectedRowIndex, selectedColumnIndex);
        return cell.isLandMine();
    }

    public void flag(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        cell.flag();
    }

    public void open(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        cell.open();
    }

    public void openSurroundedCells(int row, int col) {
        // 범위를 벗어난 경우, 더 이상 열지 않는다.
        if (row < 0 || row >= getRowSize() || col < 0 || col >= getColSize()) {
            return;
        }

        // 이미 열려있거나 지뢰가 있는 셀은 더 이상 열지 않는다.
        if (isOpenedCell(row, col)) {
            return;
        }

        // 지뢰가 있는 셀은 더 이상 열지 않는다.
        if (isLandMineCell(row, col)) {
            return;
        }

        open(row, col);

        // 주변에 지뢰가 있는 경우, 더 이상 열지 않는다.
        if (doesCellHaveLandMineCount(row, col)) {
            return;
        }

        openSurroundedCells(row - 1, col - 1);
        openSurroundedCells(row - 1, col);
        openSurroundedCells(row - 1, col + 1);
        openSurroundedCells(row, col - 1);
        openSurroundedCells(row, col + 1);
        openSurroundedCells(row + 1, col - 1);
        openSurroundedCells(row + 1, col);
        openSurroundedCells(row + 1, col + 1);
    }

    public boolean isAllCellChecked() {
        return Arrays.stream(board)
                .flatMap(Arrays::stream)
                .allMatch(Cell::isChecked);
    }

    private boolean doesCellHaveLandMineCount(int row, int col) {
        return findCell(row, col).hasLandMineCount();
    }

    private boolean isOpenedCell(int row, int col) {
        return findCell(row, col).isOpened();
    }

    public String getSign(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        return cell.getSign();
    }

    public int getRowSize() {
        return board.length;
    }

    public int getColSize() {
        return board[0].length;
    }

    private int countNearbyLandMines(int rowIndex, int colIndex) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        int count = 0;

        if (rowIndex - 1 >= 0 && colIndex - 1 >= 0 && isLandMineCell(rowIndex - 1, colIndex - 1)) {
            count++;
        }
        if (rowIndex - 1 >= 0 && isLandMineCell(rowIndex - 1, colIndex)) {
            count++;
        }
        if (rowIndex - 1 >= 0 && colIndex + 1 < colSize && isLandMineCell(rowIndex - 1, colIndex + 1)) {
            count++;
        }
        if (colIndex - 1 >= 0 && isLandMineCell(rowIndex, colIndex - 1)) {
            count++;
        }
        if (colIndex + 1 < colSize && isLandMineCell(rowIndex, colIndex + 1)) {
            count++;
        }
        if (rowIndex + 1 < rowSize && colIndex - 1 >= 0 && isLandMineCell(rowIndex + 1, colIndex - 1)) {
            count++;
        }
        if (rowIndex + 1 < rowSize && isLandMineCell(rowIndex + 1, colIndex)) {
            count++;
        }
        if (rowIndex + 1 < rowSize && colIndex + 1 < colSize && isLandMineCell(rowIndex + 1, colIndex + 1)) {
            count++;
        }
        return count;
    }

    private Cell findCell(int row, int col) {
        Cell cell = board[row][col];
        return cell;
    }
}
