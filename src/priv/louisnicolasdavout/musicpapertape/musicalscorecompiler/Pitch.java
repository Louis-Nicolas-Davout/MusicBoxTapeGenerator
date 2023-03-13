package priv.louisnicolasdavout.musicpapertape.musicalscorecompiler;

/*
 * 这个枚举中列举出了music.xsd中支持的所有音高。如果这些音高在纸带上没有对应，
 * 那么它的mask属性为null。反之，它的mask属性为这个音在纸带上对应的位置，
 * 同时也是这个音在“编译”结果中占据的比特。
 */
enum Pitch {
	C2(1 << 29), SHARP_C2(null), D2(1 << 28), SHARP_D2(null), E2(null), F2(null), SHARP_F2(null), G2(1 << 27),
	SHARP_G2(null), A2(1 << 26), SHARP_A2(null), B2(1 << 25),

	C3(1 << 24), SHARP_C3(null), D3(1 << 23), SHARP_D3(null), E3(1 << 22), F3(1 << 21), SHARP_F3(1 << 20), G3(1 << 19),
	SHARP_G3(1 << 18), A3(1 << 17), SHARP_A3(1 << 16), B3(1 << 15),

	C4(1 << 14), SHARP_C4(1 << 13), D4(1 << 12), SHARP_D4(1 << 11), E4(1 << 10), F4(1 << 9), SHARP_F4(1 << 8),
	G4(1 << 7), SHARP_G4(1 << 6), A4(1 << 5), SHARP_A4(1 << 4), B4(1 << 3),

	C5(1 << 2), SHARP_C5(null), D5(1 << 1), SHARP_D5(null), E5(1), F5(null), SHARP_F5(null), G5(null), SHARP_G5(null),
	A5(null), SHARP_A5(null), B5(null);

	private final Integer mask;

	private Pitch(Integer mask) {
		this.mask = mask;
	}

	/**
	 * @return 某个音在纸带上的掩码值。掩码值越大，音越低，只有30个音有掩码，掩码在数字中的位置代表音在纸带上的位置。纸带上不存在的音掩码为null
	 */
	public Integer getMask() {
		return this.mask;
	}
}
