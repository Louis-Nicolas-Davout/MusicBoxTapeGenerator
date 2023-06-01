package priv.louisnicolasdavout.musicpapertape.main;

import java.io.File;

import priv.louisnicolasdavout.musicpapertape.musicalscorecompiler.MusicalScoreCompiler;
import priv.louisnicolasdavout.musicpapertape.player.MusicalScorePlayer;
import priv.louisnicolasdavout.musicpapertape.tapegenerator.TapeGenerator;

/*
 * 此项目将一份规定格式的xml文档转换为一幅字符画，对应于30音纸带八音盒的纸带。
 * 经过实验，要想得到最为接近的格式，应当将生成的字符画复制到WPS文字中，
 * 设置格式如下：
 * 宋体 5.5
 * 固定行距5.65磅
 * 字符间距 缩放 103%
 * 分为两栏
 * A4打印
 */
public class Launcher {
	/*
	 * 主程序的入口，接受三个命令行参数： 第一个参数：要处理的xml文件的路径 第二个参数：在生成的纸带字符画会有一些行号，第二个参数是每个行号的间隔。
	 * 推荐使用一个小节中16分音符的个数。由于在生成的字符画中每行表示一个16分音符的时间， 这样做会使每个行号对应一个小节。
	 * 第三个参数：在预览时使用的速度，单位为bpm。
	 */
	public static void main(String[] args) {
		String filePath = args[0];
		int lineNumberGap = Integer.parseInt(args[1]);
		float bpm = Float.parseFloat(args[2]);
		boolean pitchRangeCheckEnabled = true;
		if (args.length > 3) {
			pitchRangeCheckEnabled = Boolean.parseBoolean(args[3]);
		}
		try {
			File file = new File(filePath);
			MusicalScoreCompiler compiler = new MusicalScoreCompiler(file);
			int[] a = compiler.compile(pitchRangeCheckEnabled);
			TapeGenerator generator = new TapeGenerator(a);
			for (String str : generator.getStrings(lineNumberGap)) {
				System.out.println(str);
			}
			MusicalScorePlayer player = new MusicalScorePlayer(a);
			player.play(bpm);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
