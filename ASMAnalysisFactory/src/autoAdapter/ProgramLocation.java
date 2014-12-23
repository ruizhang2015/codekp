package autoAdapter;

public class ProgramLocation {
	public String className;
	public String methodName;
	public String methodDesc;
	public int insnIndex;
	public int sourceLine = -1;
	public static ProgramLocation BuiltinPL = new ProgramLocation("builtinC", "builtinM", "builtinMD", 0, -1);

	public ProgramLocation(String className, String methodName, String methodDesc, int insnIndex, int sourceLine) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.methodDesc = methodDesc;
		this.insnIndex = insnIndex;
		this.sourceLine = sourceLine;
	}

	public String toString() {
		String result = "";
		String consoleLink = getSourceFile();
		consoleLink = consoleLink.replace('/', '.');
		if (consoleLink.indexOf('.') >= 0)
			consoleLink = consoleLink.substring(consoleLink.lastIndexOf('.') + 1);
		consoleLink = " " + className + "." + methodName + "(" + consoleLink + ".java:" + sourceLine + ") ";
		// System.out.println("link= "+consoleLink);
		result = consoleLink + methodDesc + ":" + insnIndex;
		if (sourceLine != -1)
			result += "/" + sourceLine;
		return result;
	}

	private String getSourceFile() {
		String sf = className;
		if (this.className.startsWith("L") && this.className.endsWith(";"))
			sf = sf.substring(0, sf.length() - 2);
		if (sf.indexOf('$') >= 0)
			sf = sf.substring(0, sf.indexOf('$'));
		return sf;

	}

}