package piano;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class Keyboard extends JFrame 
{
	private NotePlayer notePlayer;
	private ArrayList<String> notes;
	private ArrayList<String> basicNotes;
	private ArrayList<String> sharpNotes;
	
	private static final int WHITE_KEY_WIDTH = 60;
	private static final int WHITE_KEY_HEIGHT = 240;
	private static final int BLACK_KEY_WIDTH = 48;
	private static final int BLACK_KEY_HEIGHT = 140;

	public Keyboard(NotePlayer givenNotePlayer)
	{
		notePlayer = givenNotePlayer;
		notes = notePlayer.getNotes();
		basicNotes = new ArrayList<String>(Arrays.asList("C", "D", "E", "F", "G", "A", "B"));
		sharpNotes = new ArrayList<String>(Arrays.asList("C#", "D#", "F#", "G#", "A#"));

		setTitle("Virtual Piano");
		JLayeredPane keyPanel = new JLayeredPane();
		JPanel controlPanel = new JPanel();

		addKeys(keyPanel);
		addOptions(controlPanel);
		
		add(keyPanel);
		add(BorderLayout.SOUTH, controlPanel);
		setSize(750, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void addKeys(JLayeredPane keyPanel) 
	{
		//adds 3 separate octaves
		for(int h = 0; h < 3; h++)
		{
			//adds white keys
			for (int i = h*7; i < (h+1)*7; i++)
			{
				JButton key = new JButton(basicNotes.get(i%7));
				key.setBackground(Color.WHITE);
				key.setForeground(Color.WHITE);
				key.setLocation(i * WHITE_KEY_WIDTH, 0);
				key.setSize(WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);
				key.addActionListener(notePlayer);

				keyPanel.add(key, 0, -1);
			}
			//adds black keys
			int sharpNotesIndex = 0;
			for (int i = h*7; i < (h+1)*7; i++) 
			{
				if(!(i == 2+(h*7) || i == 6+(h*7)))
				{
					JButton key = new JButton(sharpNotes.get(sharpNotesIndex));
					key.setBackground(Color.BLACK);
					key.setForeground(Color.BLACK);
					key.setLocation(i*(WHITE_KEY_WIDTH) + (BLACK_KEY_WIDTH*3/4), 0);
					key.setSize(BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
					key.addActionListener(notePlayer);

					keyPanel.add(key, 1, -1);
					sharpNotesIndex++;
				}
			}
		}
	}
	private void addOptions(JPanel controlPanel) 
	{
		String[] instrumentOptions = new String[127];
		for(int i = 1; i <= instrumentOptions.length; i++)
			instrumentOptions[i-1] = ""+i;

		String [] octaveOptions = new String[5];
		for(int i = 2; i <= octaveOptions.length+1; i++)
			octaveOptions[i-2] = "" +i;

		JLabel instrumentChoiceLabel = new JLabel("Instrument: ");
		JComboBox instrumentChoiceBox = new JComboBox(instrumentOptions);
		instrumentChoiceBox.addActionListener(notePlayer);

		JLabel octaveChoiceLabel = new JLabel("Octave: ");
		JComboBox octaveChoiceBox = new JComboBox(octaveOptions);
		octaveChoiceBox.addActionListener(notePlayer);

		//populate controlPanel
		controlPanel.add(instrumentChoiceLabel);
		controlPanel.add(instrumentChoiceBox);
		controlPanel.add(octaveChoiceLabel);
		controlPanel.add(octaveChoiceBox);
	}
}
