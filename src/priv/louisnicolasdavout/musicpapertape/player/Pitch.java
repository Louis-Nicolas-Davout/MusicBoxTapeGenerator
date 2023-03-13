package priv.louisnicolasdavout.musicpapertape.player;

/*
 * 这个枚举存储了纸带上支持的每个音和他们的MIDI音符码。
 */
enum Pitch {
	E3(64), D3(62), C3(60), B2(59), SHARP_A2(58),

	A2(57), SHARP_G2(56), G2(55), SHARP_F2(54), F2(53),

	E2(52), SHARP_D2(51), D2(50), SHARP_C2(49), C2(48),

	B1(47), SHARP_A1(46), A1(45), SHARP_G1(44), G1(43),

	SHARP_F1(42), F1(41), E1(40), D1(38), C1(36),

	B(35), A(33), G(31), D(26), C(24);

	private int midiId;

	private Pitch(int midiId) {
		this.midiId = midiId;
	}

	public int getId() {
		return this.midiId;
	}
}
