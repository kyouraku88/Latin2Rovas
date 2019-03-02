package hu.bsido.rovas.common;

import javafx.scene.paint.Paint;

public class LRResources {

	public enum SlideDirection {
		UP, DOWN, LEFT, RIGHT
	}
	
	public enum FileOutput {
		txt, pdf 
	}
	
	public enum AppColor {
		BLUE("#428bca"), GREEN("#3cbc53"), RED("#c30808"),
		LIGHT_GREEN("#ffde03"), BLACK("#000");
		public final String value;
		private AppColor(String value) {
			this.value = value;
		}
		public Paint paint() {
			return Paint.valueOf(value);
		}
	}
	
	public static final String[][] ROVASOK = {
			{"\u00E1","A"},		// hosszu a
			{"\u00E9","E"},		// hosszu e
			{"\u00F3","O"},		// hosszu o
			{"\u00ED","I"},		// hosszu i
			{"\u00FA","U"},		// hosszu u

//			{"q","\u00AB"},
//			{"w","\u005B"},
			{"w","v"},

			// TODO AS 0046 nagy F
			// TODO K (veg) + AK -> 004B nagy K 

			{"\u0301","X"},		// ekezet
			{"aX","A"},			// mer a hosszu a-t aX-re forditja
			{"eX","E"},
			{"oX","O"},
			{"iX","I"},
			{"uX","U"},

			{"\u00F6","\u0071"},		//OE
			{"\u00FC","\u0077"},		//UE
			{"\u0151","\u0051"},		//OEE
			{"\u0171","\u0057"},		//UEE

			{"\u030B","Q"},		// kettos ekezet
			{"oQ","\u0051"},
			{"uQ","\u0057"},

			{"\u0308","Y"},		// ketto pont
			{"oY","\u0071"},
			{"uY","\u0077"},

			{"ssz","\u0053\u0053"},
			{"tty","\u0054\u0054"},
			{"ccs","\u0043\u0043"},
			{"ggy","\u0047\u0047"},
			{"lly","\u004C\u004C"},
			{"nny","\u004E\u004E"},
			{"zzs","\u005A\u005A"},
			{"sz","\u0053"},
			{"ty","\u0054"},
			{"cs","\u0043"},
			{"dzs","\u00A6"},
			{"dz","\u0060"},		
			{"gy","\u0047"},
			{"ly","\u004C"},		
			{"ny","\u004E"},
			{"zs","\u005A"},
			{"x","k\u0053"},	// ksz
			{"y","i"}
		};
	
}
