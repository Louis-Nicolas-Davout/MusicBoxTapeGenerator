package priv.louisnicolasdavout.musicpapertape.tapegenerator;

import java.util.LinkedList;
import java.util.List;

/*
 * 这个类负责将“编译器”生成的整数数组转换成纸带字符画
 */
public class TapeGenerator {
	public static final String HEAD = "　　　　　　　　　＃　＃　＃　　　　　　　＃　＃　＃　　　　\n　Ｄ　Ａ　Ｃ　Ｅ　Ｆ　Ｇ　Ａ　Ｃ　Ｄ　Ｅ　Ｆ　Ｇ　Ａ　Ｃ　Ｅ\n　　　　　１　１　１　１　１　２　２　２　２　２　２　３　３\n　　　　　　　　　　　　　　　　＃　＃　　　　　　　　　　　\nＣ　Ｇ　Ｂ　Ｄ　Ｆ　Ｇ　Ａ　Ｂ　Ｃ　Ｄ　Ｆ　Ｇ　Ａ　Ｂ　Ｄ　\n　　　　　　１　１　１　１　１　２　２　２　２　２　２　３　";
	private final int[] lines;

	public TapeGenerator(int[] lines) {
		this.lines = lines;
	}

	public List<String> getStrings(int lineNumberGap) {
		List<String> strings = new LinkedList<String>();
		strings.add(TapeGenerator.HEAD);
		for (int lineIndex = 0; lineIndex < this.lines.length; lineIndex++) {
			StringBuilder stringBuilder = new StringBuilder(30);
			for (int i = 0; i < 30; i++) {
				if ((this.lines[lineIndex] & (1 << (29 - i))) == 0) {
					stringBuilder.append(TapeGenerator.getString(lineIndex, i));
				} else {
					stringBuilder.append("●");
				}
			}
			if ((lineIndex % lineNumberGap) == 0) {
				stringBuilder.append(Integer.toString(lineIndex / lineNumberGap));
			}
			strings.add(stringBuilder.toString());
		}
		return strings;
	}

	private static String getString(int row, int column) {
		// 两端是特例
		if (column == 0) {
			return ((row % 2) == 0) ? "├" : "│";
		} else if (column == 29) {
			return ((row % 2) == 0) ? "┤" : "│";
		}
		switch (row % 4) {
		case 0:
			return "┼";
		case 1:
			return "│";
		case 2:
			switch (column % 5) {
			case 0:
				return "├";
			case 1:
				return "┼";
			case 2:
				return "│";
			case 3:
				return "┼";
			case 4:
				return "┤";
			}
			break;
		case 3:
			return "│";
		}
		return null;
	}
}
