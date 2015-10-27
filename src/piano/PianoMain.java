package piano;

import javax.sound.midi.MidiUnavailableException;

public class PianoMain 
{
	 public static void main (String[] args) throws MidiUnavailableException 
	    {
		 	NotePlayer notePlayer = new NotePlayer();
	        Keyboard keyboard = new Keyboard(notePlayer);
	    }
}
