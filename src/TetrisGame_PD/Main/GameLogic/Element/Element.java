package TetrisGame_PD.Main.GameLogic.Element;

import org.lwjgl.system.windows.POINT;

import java.util.Arrays;

public class Element {

    public static int noElement = 0;
    protected int xSlPos = 0; //position X in slots not px (column) on grid
    protected int ySlPos = 0; //position Y in slots not px (row) on grid
    protected int blocksWidth;// horizontal size of element blocks
    protected int blocksHeight;// vertical size of element blocks

    protected int size;
    private int[][] data;

    public Element(int[][] data) {
        noElement++;
        this.size = data.length;
        this.data = data;
    }

    public Element(int size) {
        noElement++;
        this.size = size;
        this.data = new int[size][size];
    }

    public Element(TetrisElements elType, int xSlPos, int ySlPos) {
        noElement++;
        this.data = elType.getArray();
        this.size = this.data.length;
        this.xSlPos = xSlPos;
        this.ySlPos = ySlPos;
        updateBlockSize();
    }


    public void rotate90CW() {
        for (int j = 0; j < (size / 2); j++) {
            //int j = 0; //shell depth
            for (int i = j; i < ((size - j) - 1); i++) { //every loop rotates one 'peel' , like next layer of onion.
                int max_idx = size - 1;
                int temp = data[j][i]; //store A
                data[j][i] = data[max_idx - i][j]; //move M on A place
                data[max_idx - i][j] = data[max_idx - j][max_idx - i];// move P on M place
                data[max_idx - j][max_idx - i] = data[i][max_idx - j];//move D on P place
                data[i][max_idx - j] = temp;//move A on D place
            }
        }
        alignTop();
        alignLeft();
        updateBlockSize();
    }

    public void rotate90CCW() {
        for (int j = 0; j < (size / 2); j++) {
            //int j = 0; //shell depth
            for (int i = j; i < ((size - j) - 1); i++) { //every loop rotates one 'peel' , like next layer of onion.
                int max_idx = size - 1;
                int temp = data[j][i]; //store A
                data[j][i] = data[i][max_idx - j]; //move D on A place
                data[i][max_idx - j] = data[max_idx - j][max_idx - i];// move P on D place
                data[max_idx - j][max_idx - i] = data[max_idx - i][j];//move M on P place
                data[max_idx - i][j] = temp;//move A on M place
            }
        }
        alignTop();
        alignLeft();
        updateBlockSize();
    }

    @Override
    public String toString() {
        StringBuilder sbld = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sbld.append(data[i]);
            if ((i + 1) < size)
                sbld.append(",");
        }
        return sbld.toString();
    }

    protected int[] getRow(int rowNo) {
        return data[rowNo];
    }

    private int[] getCol(int colNo) {
        int[] col = new int[size];
        for (int i = 0; i < size; i++) {
            col[i] = data[i][colNo];
        }
        return col;
    }

    private void setCol(int[] colData, int colNo) {
        for (int i = 0; i < colData.length; i++) {
            data[i][colNo] = colData[i];
        }
    }

    private void alignLeft() {
        if (Arrays.stream(getCol(0)).anyMatch(p -> p != 0)) {
            return;//column 0 already has at leas one non-zero element. Is aligned to left.
        } else {
            //all zero collumn is zero
            moveLeft();
            alignLeft();
        }
    }

    private void alignTop() {
        if (Arrays.stream(data[0]).anyMatch(p -> p != 0)) {
            return;//row 0 already has at least one non-zero element. Is alligned to top.
        } else {
            moveUp();
            alignTop();
        }
    }

    private void moveUp() {
        for (int i = 0; i < (size - 1); i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = new int[size];//fill last row with zeros (new row)
    }

    private void moveLeft() {
        for (int i = 0; i < (size - 1); i++) {
            setCol(getCol(i + 1), i);
        }
        setCol(new int[size], size - 1);//fill last coll with zeros (new coll)
    }

    public void prettyPrint() {
        for (int i = 0; i < size; i++) {
            System.out.println(Arrays.toString(getRow(i)));
        }
    }

    public void moveDown(int noSlots) {
        ySlPos += noSlots;
    }

    public void moveToBeginning() {
        ySlPos = 0;
    }

    public int[][] getData() {
        return this.data;
    }

    public int[] getCurrentSlotPos() {
        return new int[]{xSlPos, ySlPos};
    }

    private void updateBlockSize() {
        int xMax = 0, yMax = 0;
        for (int i = 0; i < this.data.length; i++) { //i - rows
            for (int j = 0; j < this.data[0].length; j++) { //j - columns
                if (data[i][j] != 0) {
                    if (i > yMax)
                        yMax = i;
                    if (j > xMax)
                        xMax = j;
                }
            }
        }
        blocksWidth = xMax + 1;
        blocksHeight = yMax + 1;
    }

    public int[] getBlocksSize() {
        return new int[]{blocksHeight, blocksWidth};
    }


    /**
     * Returns index (column no from 0) of first block (on left) in the given row
     *
     * @param rowNo row in with function will return index of first block on the left
     * @return Index of first block on the left (-1 if there is no block in row)
     */
    public int getIndexFirstInRow(int rowNo) {
        for (int i = 0; i <= this.blocksWidth-1; i++) {
            if (data[rowNo][i] != 0) { //block found
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns index (column no from 0) of last block (on right) in the given row
     *
     * @param rowNo row in with function will return index of last block (on the right)
     * @return Index of last block (on the right) (-1 if there is no block in row)
     */
    public int getIndexLastInRow(int rowNo) {
        for (int i = this.blocksWidth-1; i >= 0; i--) {
            if (data[rowNo][i] != 0) { //block found
                return i;
            }
        }
        return -1;
    }


}


