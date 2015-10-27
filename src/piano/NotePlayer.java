package piano;

import javax.swing.*;
import javax.sound.midi.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class NotePlayer implements ActionListener 
{	
    private Sequencer sequencer;
	
	private ArrayList<String> notes;
    private int instrumentChoice;
    private int octaveChoice;
    
    private static final int CHANNEL = 1;
    private static final int QUARTER_NOTE_DURATION = 8;
    private static final int VELOCITY = 127;
    private static final int WHITE_KEY_WIDTH = 60;
    
    private int keyPressedCount;//this keeps track of the number of times a key is pressed. For every multiple of 25 it reaches, the sequencer is closed to preserve

    public NotePlayer() throws MidiUnavailableException
    {	
		sequencer = MidiSystem.getSequencer();
		sequencer.open();
    	
    	notes = new ArrayList<String>(Arrays.asList("C","C#","D","D#","E","F","F#","G","G#","A","A#","B"));
    	instrumentChoice = 1;
    	octaveChoice = 1;
    	
    	keyPressedCount = 0;
    }

    public ArrayList<String> getNotes() {
		return notes;
	}

	public int getInstrumentChoice() {
		return instrumentChoice;
	}

	public int getOctaveChoice() {
		return octaveChoice;
	}

	public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() instanceof JButton) 
        {
        	JButton but = (JButton) e.getSource();
        	int tempOctave = octaveChoice;
        	if(but.getX() < WHITE_KEY_WIDTH*7)
        		tempOctave--;
        	if(but.getX() >= WHITE_KEY_WIDTH*14)
        		tempOctave++;
            int note = ((tempOctave+1) * 12) + notes.indexOf(but.getText());
			playNote(note);
        }
        else if(e.getSource() instanceof JComboBox)
        {
        	JComboBox box = (JComboBox) e.getSource();
        	if(box.getItemCount() > 20)
        		instrumentChoice = Integer.parseInt((String) box.getSelectedItem());
        	else
        		octaveChoice = Integer.parseInt((String) box.getSelectedItem());
        }
    }

    public void playNote(int note)
    {
    	try 
    	{
    		Sequence sequence = new Sequence(Sequence.PPQ, 4);
    		Track track = sequence.createTrack();

    		MidiEvent event = null;//TODO - is this necessary?

    		//Track adds a MidiEvent that determines the instrument
    		ShortMessage instrumentMessage = new ShortMessage();
    		instrumentMessage.setMessage(192, CHANNEL, instrumentChoice-1, 0);//192 indicates that the message is an instrument change in Channel 1
    		MidiEvent changeInstrument = new MidiEvent(instrumentMessage, 1);
    		track.add(changeInstrument);

    		//Track adds a MidiEvent that turns the note on
    		ShortMessage noteOnMessage = new ShortMessage();
    		noteOnMessage.setMessage(144, CHANNEL, note, VELOCITY);//144 indicates that the message is to turn a note on in Channel 1, 100 is the velocity
    		MidiEvent noteOn = new MidiEvent(noteOnMessage, 1);
    		track.add(noteOn);

    		//Track adds a MidiEvent that turns the note off after a predetermined duration
    		ShortMessage noteOffMessage = new ShortMessage();
    		noteOffMessage.setMessage(128, CHANNEL, note, VELOCITY);//128 indicates that the message is to turn a note off in Channel 1, 100 is the velocity
    		MidiEvent noteOff = new MidiEvent(noteOffMessage, QUARTER_NOTE_DURATION);
    		track.add(noteOff);

    		sequencer.setSequence(sequence);
    		sequencer.start();
    	} catch (InvalidMidiDataException e) {

			e.printStackTrace();
		};
    }
}