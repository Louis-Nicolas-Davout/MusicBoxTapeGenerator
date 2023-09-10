package priv.louisnicolasdavout.musicpapertape.musicalscorecompiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*
 * 这个类将整个音乐文件“编译”成一个整数数组，这个数组使用整数的后30比特存储数据。
 * 每一个被设为1的比特都对应于纸带上的一个孔。
 * 方便起见，这个类中获取节点的方法使用了固定的命名空间前缀“lnd”，
 * 因此在用户编写个人的纸带xml时也必须采用lnd前缀，否则将无法识别。
 */
public class MusicalScoreCompiler {
	private static Pitch[] pitches;
	/*
	 * 这个表中存储了每个音符相对于中音DO的升（降）半音数。
	 */
	private static Map<String, Integer> relativePitches;
	static {
		MusicalScoreCompiler.pitches = Pitch.values();
		MusicalScoreCompiler.relativePitches = new HashMap<String, Integer>();
		MusicalScoreCompiler.relativePitches.put("1..", -24);
		MusicalScoreCompiler.relativePitches.put("#1..", -23);
		MusicalScoreCompiler.relativePitches.put("2..", -22);
		MusicalScoreCompiler.relativePitches.put("#2..", -21);
		MusicalScoreCompiler.relativePitches.put("3..", -20);
		MusicalScoreCompiler.relativePitches.put("4..", -19);
		MusicalScoreCompiler.relativePitches.put("#4..", -18);
		MusicalScoreCompiler.relativePitches.put("5..", -17);
		MusicalScoreCompiler.relativePitches.put("#5..", -16);
		MusicalScoreCompiler.relativePitches.put("6..", -15);
		MusicalScoreCompiler.relativePitches.put("#6..", -14);
		MusicalScoreCompiler.relativePitches.put("7..", -13);

		MusicalScoreCompiler.relativePitches.put("1.", -12);
		MusicalScoreCompiler.relativePitches.put("#1.", -11);
		MusicalScoreCompiler.relativePitches.put("2.", -10);
		MusicalScoreCompiler.relativePitches.put("#2.", -9);
		MusicalScoreCompiler.relativePitches.put("3.", -8);
		MusicalScoreCompiler.relativePitches.put("4.", -7);
		MusicalScoreCompiler.relativePitches.put("#4.", -6);
		MusicalScoreCompiler.relativePitches.put("5.", -5);
		MusicalScoreCompiler.relativePitches.put("#5.", -4);
		MusicalScoreCompiler.relativePitches.put("6.", -3);
		MusicalScoreCompiler.relativePitches.put("#6.", -2);
		MusicalScoreCompiler.relativePitches.put("7.", -1);

		MusicalScoreCompiler.relativePitches.put("1", 0);
		MusicalScoreCompiler.relativePitches.put("#1", 1);
		MusicalScoreCompiler.relativePitches.put("2", 2);
		MusicalScoreCompiler.relativePitches.put("#2", 3);
		MusicalScoreCompiler.relativePitches.put("3", 4);
		MusicalScoreCompiler.relativePitches.put("4", 5);
		MusicalScoreCompiler.relativePitches.put("#4", 6);
		MusicalScoreCompiler.relativePitches.put("5", 7);
		MusicalScoreCompiler.relativePitches.put("#5", 8);
		MusicalScoreCompiler.relativePitches.put("6", 9);
		MusicalScoreCompiler.relativePitches.put("#6", 10);
		MusicalScoreCompiler.relativePitches.put("7", 11);

		MusicalScoreCompiler.relativePitches.put("1^", 12);
		MusicalScoreCompiler.relativePitches.put("#1^", 13);
		MusicalScoreCompiler.relativePitches.put("2^", 14);
		MusicalScoreCompiler.relativePitches.put("#2^", 15);
		MusicalScoreCompiler.relativePitches.put("3^", 16);
		MusicalScoreCompiler.relativePitches.put("4^", 17);
		MusicalScoreCompiler.relativePitches.put("#4^", 18);
		MusicalScoreCompiler.relativePitches.put("5^", 19);
		MusicalScoreCompiler.relativePitches.put("#5^", 20);
		MusicalScoreCompiler.relativePitches.put("6^", 21);
		MusicalScoreCompiler.relativePitches.put("#6^", 22);
		MusicalScoreCompiler.relativePitches.put("7^", 23);

		MusicalScoreCompiler.relativePitches.put("1^^", 24);
		MusicalScoreCompiler.relativePitches.put("#1^^", 25);
		MusicalScoreCompiler.relativePitches.put("2^^", 26);
		MusicalScoreCompiler.relativePitches.put("#2^^", 27);
		MusicalScoreCompiler.relativePitches.put("3^^", 28);
		MusicalScoreCompiler.relativePitches.put("4^^", 29);
		MusicalScoreCompiler.relativePitches.put("#4^^", 30);
		MusicalScoreCompiler.relativePitches.put("5^^", 31);
		MusicalScoreCompiler.relativePitches.put("#5^^", 32);
		MusicalScoreCompiler.relativePitches.put("6^^", 33);
		MusicalScoreCompiler.relativePitches.put("#6^^", 34);
		MusicalScoreCompiler.relativePitches.put("7^^", 35);
	}
	private final Document document;

	public MusicalScoreCompiler(File file) throws SAXException, IOException, ParserConfigurationException {
		this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		this.document.normalize();
	}

	public int[] compile(boolean 
						pitchRangeCheckEnabled) {
		Pitch tone = this.getTone();
		double barDuration = this.getBarDuration();
		NodeList bars = this.document.getElementsByTagName("lnd:musical_bar");
		int barNotesCount = (int) Math.round(bars.getLength() * 4 * barDuration);
		int[] a = new int[barNotesCount];
		for (int i = 0; i < bars.getLength(); i++) {
			try {
				MusicalScoreCompiler.loadBarToIntArray(a, (Element) bars.item(i), tone, i, barDuration,
						pitchRangeCheckEnabled);
			} catch (RuntimeException ex) {
				throw new RuntimeException("处理第" + i + "小节时发生错误", ex);
			}
		}
		return a;
	}

	private Pitch getTone() {
		Element elem = (Element) this.document.getElementsByTagName("lnd:musical_score").item(0);
		String toneString = elem.getAttribute("musical_tone");
		toneString = toneString.replace("#", "SHARP_");
		return Pitch.valueOf(toneString);
	}

	private double getBarDuration() {
		Element elem = (Element) this.document.getElementsByTagName("lnd:musical_score").item(0);
		return Double.parseDouble(elem.getAttribute("bar_duration"));
	}

	private static void loadBarToIntArray(int[] arrayToLoad, Element element, Pitch tone, int currentBar,
			double barDuration, boolean pitchRangeCheckEnabled) {
		List<Element> partNodes = MusicalScoreCompiler.getChildNodesByTagName(element, "lnd:musical_part");
		for (Element partNode : partNodes) {
			MusicalScoreCompiler.loadPartToIntArray(arrayToLoad, partNode, tone, currentBar, barDuration,pitchRangeCheckEnabled);
		}
	}

	private static void loadPartToIntArray(int[] arrayToLoad, Element element, Pitch tone, int currentBar,
			double barDuration, boolean pitchRangeCheckEnabled) {
		List<Element> noteNodes = MusicalScoreCompiler.getChildNodesByTagName(element, "lnd:musical_note");
		double currentTime = currentBar * barDuration;
		for (Element noteNode : noteNodes) {
			MusicalScoreCompiler.Note note = MusicalScoreCompiler.parseNote(noteNode, tone, pitchRangeCheckEnabled);
			int index = (int) Math.round(currentTime * 4);
			// 显然，如果遇到了休止符，我们不希望它写入任何一个音。
			Integer maskObj = (note.pitch == null) ? Integer.valueOf(0) : note.pitch.getMask();
			int mask;
			if (maskObj == null) {
				System.out.println("小节" + currentBar + "中有不支持的音高");
				mask = 0;
			} else {
				mask = maskObj;
			}
			arrayToLoad[index] = arrayToLoad[index] | mask;
			currentTime += note.duration;
		}
	}

	private static MusicalScoreCompiler.Note parseNote(Element element, Pitch tone, boolean pitchRangeCheckEnabled) {
		String relativePitchString = element.getAttribute("pitch");
		String durationString = element.getAttribute("duration");
		double duration = Double.parseDouble(durationString);
		// 处理休止符
		if (relativePitchString.equals("0")) {
			return new MusicalScoreCompiler.Note(null, duration);
		}
		int relativePitch = MusicalScoreCompiler.relativePitches.get(relativePitchString);
		Pitch pitch = null;
		try {
			pitch = MusicalScoreCompiler.pitches[tone.ordinal() + relativePitch];
		} catch (ArrayIndexOutOfBoundsException ex) {
			if(pitchRangeCheckEnabled) {
			throw new RuntimeException("所使用的音不在音域内", ex);
			}
		}
		return new MusicalScoreCompiler.Note(pitch, duration);
	}

	private static class Note {
		public final Pitch pitch;
		public final double duration;

		public Note(Pitch pitch, double duration) {
			this.pitch = pitch;
			this.duration = duration;
		}
	}

	/**
	 * @param element 节点
	 * @param tagName 子节点标签名
	 * @return 子节点列表
	 */
	public static List<Element> getChildNodesByTagName(Element element, String tagName) {
		NodeList children = element.getChildNodes();
		List<Element> list = new ArrayList<Element>();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			Element elem;
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				elem = (Element) node;
			} else {
				continue;
			}
			if (elem.getTagName().equals(tagName)) {
				list.add(elem);
			}
		}
		return list;
	}
}
