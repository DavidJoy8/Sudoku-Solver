import java.util.Arrays;

public class Sudoku implements Cloneable {
	private static final short CANBE_INIT = 0x03FE;

	private int[][] numbers;		//what is filled in on the sudoku.  0 represents not filled yet
	private short[][] canBe;
	//9x9, bits in an entry represents whether that square of the sudoku could be a certain number

	public Sudoku() {
		numbers = new int[9][9];
		canBe = new short[9][9];
		for(short[] row:canBe)
			Arrays.fill(row,CANBE_INIT);
	}

	//argument must be a 9x9 array
	public Sudoku(int[][] nums) {
		if(nums.length!=9 || nums[0].length!=9)
			throw new IllegalArgumentException();
		numbers = new int[9][9];
		canBe = new short[9][9];
		for(short[] row:canBe)
			Arrays.fill(row,CANBE_INIT);
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(nums[i][j]!=0)
					addNumber(i,j,nums[i][j]);
			}
		}
	}

	public Sudoku clone() {
		return new Sudoku(numbers);
	}

	private void testArgs(int row, int col, int num) {
		if(row<0 || row>8 || col<0 || col>8 || num<1 || num>9)
			throw new IllegalArgumentException();
	}

	public void addNumber(int row, int col, int num) {
		testArgs(row,col,num);
		if(!couldBe(row,col,num))
			throw new IllegalStateException();
		numbers[row][col]=num;
		short remover = (short)(0xFFFF ^ (1<<num));	//all 1 except for the bit corresponding to num
		//remove number as possibility in row and column:
		for(int i=0;i<9;i++) {
			canBe[row][i] &= remover;
			canBe[i][col] &= remover;
		}
		//remove number as possibility in square:
		for(int i=row-(row%3);i<row-(row%3)+3;i++) {
			for(int j=col-(col%3);j<col-(col%3)+3;j++) {
				canBe[i][j] &= remover;
			}
		}
	}

	public boolean couldBe(int row, int col, int num) {
		testArgs(row,col,num);
		return (canBe[row][col] & (1<<num)) != 0;
	}
}