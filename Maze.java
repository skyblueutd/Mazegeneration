import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import javax.swing.*;

public class Maze {
	/**
	 * Creates a maze with ALL walls exist.
	 * 
	 * @param rows number of rows.
	 * @param columns number of columns.
	 */
	private Maze(int rows, int columns) 
	{
		this.rows = rows;
		this.columns = columns;
		this.grid = new int[rows*columns];
		for (int i = 0; i < rows*columns; i++) 
		{
			this.grid[i] = UP | RIGHT | DOWN | LEFT;
		}	
	}
	
	public static class Wall 
	{
		private final int cell;
		private final int direction;

		public Wall(int cell, int direction) 
		{
			this.cell = cell;
			this.direction = direction;
		}
	}

	private static int UP = 1;
	private static int RIGHT = 2;
	private static int DOWN = 4;
	private static int LEFT = 8;
	private int[] grid;
	private int rows;
	private int columns;
	
	public static Maze createMaze(int rows, int columns) 
	{
		Maze maze = new Maze(rows, columns);
		// create all walls
		List<Wall> walls = new ArrayList<Wall>();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) 
			{
				if (row > 0) 
				{
					walls.add(new Wall(row*columns + col, UP));
				}
				if (col > 0) 
				{
					walls.add(new Wall(row*columns + col, LEFT));
				}
			}
		}
		
		// remove all the walls you can
		DisjSets diset = new DisjSets(rows*columns);
		Random rand = new Random();
		int disetsize = rows * columns;
		
		while (disetsize > 1) 
		{
			int wallIndex = rand.nextInt(walls.size());
			int cell1 = walls.get(wallIndex).cell;
			int cell2 = (walls.get(wallIndex).direction == UP) ?
					cell1 - columns :
					cell1 - 1; 
//			if (wallIndex <= columns)
//			{
//				 cell2 = cell1-1;
//			}
//			if (wallIndex > columns)
//			{
//			 cell2 = (walls.get(wallIndex).direction == UP) ?
//					cell1 - columns :
//					cell1 - 1;
//			}
			
			if (diset.find(cell1) != diset.find(cell2)) 
			{
				// we can remove the wall
				if (walls.get(wallIndex).direction == UP) 
				{
					maze.grid[cell1] ^= UP;
					maze.grid[cell2] ^= DOWN;
				} 
				else 
				{
					maze.grid[cell1] ^= LEFT;
					maze.grid[cell2] ^= RIGHT;
				}
				diset.union(diset.find(cell1), diset.find(cell2));
				disetsize--;
			}
			walls.remove(wallIndex);
		}
		return maze;
	}

	
	public void display(Graphics2D g) 
	{
		for (int row = 0; row < rows; row++) 
		{
			for (int col = 0; col < columns; col++) 
			{
				int cell = grid[col + row*columns];
				if (((cell & LEFT) != 0)&&(row!=0||col!=0)) 
				{
					drawLeftWall(g, row, col);
				}
				if (((cell & RIGHT) != 0)&&(row!=rows-1||col!=columns-1)) 
				{
					drawRightWall(g, row, col);
				}
				if (((cell & UP) != 0)&&(row!=0||col!=0)) 
				{
					drawUpperWall(g, row, col);
				}
				if (((cell & DOWN) != 0)&&(row!=rows-1||col!=columns-1)) 
				{
					drawLowerWall(g, row, col);
				}
			}
		}
	}
	
	// graph size parameter
	private int startX = 25;
	private int startY = 40;
	private int size = 24;

	private void drawLeftWall(Graphics2D g, int row, int col) 
	{
		g.drawLine(startX + size*col, startY + size*row,
				   startX + size*col, startY + size*(row+1));		
	}
	private void drawRightWall(Graphics2D g, int row, int col) 
	{
		g.drawLine(startX + size*(col+1), startY + size*row,
				   startX + size*(col+1), startY + size*(row+1));
	}
	private void drawUpperWall(Graphics2D g, int row, int col) 
	{
		g.drawLine(startX + size*col,     startY + size*row,
				   startX + size*(col+1), startY + size*row);
	}
	
	private void drawLowerWall(Graphics2D g, int row, int col) 
	{
		g.drawLine(startX + size*col,     startY + size*(row+1),
				   startX + size*(col+1), startY + size*(row+1));
	}
		

	public static void main(String[] args) 
	{
		System.out.println("Please input the row number of the maze ");
		Scanner sc1 = new Scanner(System.in);
	    int i = sc1.nextInt();
	    System.out.println("Please input the colume number of the maze ");
		Scanner sc2 = new Scanner(System.in);
	    int j = sc2.nextInt();
	    Maze m = createMaze(i, j);
		
		java.awt.EventQueue.invokeLater(new Runnable() 
		{
            public void run() 
            {
                JFrame frame = new JFrame() 
                {
					private static final long serialVersionUID = 1L;

                	public void paint(Graphics g) 
                	{
                		m.display((Graphics2D) g);
                	}
                };
                
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                GroupLayout layout = new GroupLayout(frame.getContentPane());
                frame.getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                		layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                				.addGap(0, 400, Short.MAX_VALUE));
                layout.setVerticalGroup(
                		layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                				.addGap(0, 400, Short.MAX_VALUE));
                frame.pack();
                
                frame.setVisible(true);
            }
        });
	}
	
}
