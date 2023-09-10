package priv.louisnicolasdavout.musicpapertape.player;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/*
 * 这个类负责播放由"编译器"生成的数组
 */
public class MusicalScorePlayer {
	private static final Pitch[] pitches = Pitch.values();
	private final int[] lines;

	public MusicalScorePlayer(int[] lines) {
		this.lines = lines;
	}

	public void play(float bpm) throws MidiUnavailableException, InvalidMidiDataException {
		try (Sequencer sequencer = MidiSystem.getSequencer(); Scanner sc = new Scanner(System.in)) {
			sequencer.open();
			Sequence sequence = new Sequence(Sequence.PPQ, 4);
			Track track = sequence.createTrack();
			sequencer.setTempoInBPM(bpm);
			for (int i = 0; i < this.lines.length; i++) {
				for (MidiEvent event : MusicalScorePlayer.parseLine(this.lines[i], i)) {
					track.add(event);
				}
			}
			sequencer.setSequence(sequence);
			System.out.print("按下回车开始播放：");
			sc.nextLine();
			sequencer.start();
			System.out.print("按下回车停止播放：");
			sc.nextLine();
			sequencer.stop();
		}
	}

	private static List<MidiEvent> parseLine(final int line, final int lineIndex) throws InvalidMidiDataException {
		List<MidiEvent> events = new LinkedList<MidiEvent>();
		for (int i = 0; i < 30; i++) {
			if (((1 << i) & line) != 0) {
				events.add(MusicalScorePlayer.makeEvent(144, 1, MusicalScorePlayer.pitches[i].getId(), 100,
						lineIndex + 1));
				events.add(MusicalScorePlayer.makeEvent(128, 1, MusicalScorePlayer.pitches[i].getId(), 100,
						lineIndex + 2));
			}
		}
		return events;
	}

	private static MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) throws InvalidMidiDataException {
		ShortMessage a = new ShortMessage();
		a.setMessage(comd, chan, one, two);
		return new MidiEvent(a, tick);
	}
}
