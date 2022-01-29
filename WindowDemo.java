import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/*
 *  The main window of the gui.
 *  Notice that it extends JFrame - so we can add our own components.
 *  Notice that it implements ActionListener - so we can handle user input.
 *  This version also implements MouseListener to show equivalent functionality (compare with the other demo).
 *  @author mhatcher
 */
public class WindowDemo extends JFrame implements ActionListener, MouseListener
{
	// gui components that are contained in this frame:
	private JPanel topPanel, bottomPanel;	// top and bottom panels in the main window
	private JLabel instructionLabel;		// a text label to tell the user what to do
	private JLabel infoLabel;            // a text label to show the coordinate of the selected square
        private JButton topButton;			// a 'reset' button to appear in the top panel
	private GridSquare [][] gridSquares;	// squares to appear in grid formation in the bottom panel
	private int rows,columns,player,winPlayer=0,xCoord,yCoord;				// the size of the grid
									
	
	/*
	 *  constructor method takes as input how many rows and columns of gridsquares to create
	 *  it then creates the panels, their subcomponents and puts them all together in the main frame
	 *  it makes sure that action listeners are added to selectable items
	 *  it makes sure that the gui will be visible
	 */
	public WindowDemo(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		this.setSize(600,600);
		
		// first create the panels
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(rows, columns, 10, 10));
		bottomPanel.setSize(500,500);
		
		// then create the components for each panel and add them to it
		
		// for the top panel:
		instructionLabel = new JLabel("Don't select neighbour squares! Click to begin >>");
		topButton = new JButton("New Game");
		topButton.addActionListener(this); // IMPORTANT! Without this, clicking the square does nothing.
		
		topPanel.add(instructionLabel);
		topPanel.add(topButton);
		
	
		// for the bottom panel:	
		// create the squares and add them to the grid
		gridSquares = new GridSquare[rows][columns];
		for ( int x = 0; x < columns; x ++)
		{
			for ( int y = 0; y < rows; y ++)
			{
				gridSquares[x][y] = new GridSquare(x, y);
				gridSquares[x][y].setSize(20, 20);
				gridSquares[x][y].reset();
				
				//gridSquares[x][y].addMouseListener(this);		// AGAIN, don't forget this line!
				
				bottomPanel.add(gridSquares[x][y]);
			}
		}
		
		// now add the top and bottom panels to the main frame
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(bottomPanel, BorderLayout.CENTER);		// needs to be center or will draw too small
		
		// housekeeping : behaviour
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	
	
	/*
	 *  handles actions performed in the gui
	 *  this method must be present to correctly implement the ActionListener interface
	 */
	public void actionPerformed(ActionEvent aevt)
	{
		// get the object that was selected in the gui
		Object selected = aevt.getSource();
				
		// if resetting the squares' colours is requested then do so
		if ( selected.equals(topButton) )
		{
			winPlayer = 0;
			Random rand = new Random();
			player = 1 + rand.nextInt(2);
			instructionLabel.setText("Player " + player + "'s turn");

			for ( int x = 0; x < columns; x ++)
			{
				for ( int y = 0; y < rows; y ++)
				{
					gridSquares [x][y].addMouseListener(this);
					gridSquares [x][y].reset();
				}
			}
		}
	}


	// Mouse Listener events
	public void mouseClicked(MouseEvent mevt)
	{
		// get the object that was selected in the gui
		Object selected = mevt.getSource();
		
		/*
		 * I'm using instanceof here so that I can easily cover the selection of any of the gridsquares
		 * with just one piece of code.
		 * In a real system you'll probably have one piece of action code per selectable item.
		 * Later in the course we'll see that the Command Holder pattern is a much smarter way to handle actions.
		 */
		
		// if a gridsquare is selected then switch its color
		if (selected instanceof GridSquare)
		{
            	    GridSquare square = (GridSquare) selected;
		    if (square.getBackground() == Color.WHITE)
		    {
		        square.switchColor(player);
                        player = (player == 1) ? 2 : 1;
			int xCoord = square.getXcoord();
                        int yCoord = square.getYcoord();
			System.out.println("xCoor = " + xCoord + " yCoor = " + yCoord);
			// Logic for Win/Loss
			for (int x=-1;x<=1;x++)
			{
			    for (int y=-1;y<=1;y++)
			    { 
				System.out.println("x = " + x + " and " + "y = " + y);
				if ((x!=0 || y!=0) && x+xCoord>=0 && y+yCoord>=0 && x+xCoord <= 3 && y+yCoord <= 3) // should be removed if possible
				{
				    GridSquare neighSquare = gridSquares[xCoord + x][yCoord + y];  // neighbouring square of selected square
				    Color currColor = square.getBackground();
				    System.out.println("currColor = " + currColor);
				    Color neighColor = neighSquare.getBackground();
				    System.out.println("neighColor = " + neighColor);

				    if (currColor == neighColor)
				    {
				        winPlayer = player;
				        instructionLabel.setText("Player " + winPlayer + " Wins! Click to play again >>");
					// disabling square clicking
				        for ( int column = 0; column < columns; column ++)
				        {
				            for ( int row = 0; row < rows; row ++)
				 	    {
					        gridSquares [column][row].removeMouseListener(this);
					    }
				        }
				    }
			        }
			    }
		        }
       			if (winPlayer == 0)
			{
			    instructionLabel.setText("Player " + player + "'s turn..."); 
			}
		    }	
	        }
	}
	// not used but must be present to fulfil MouseListener contract
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
}
