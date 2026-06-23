
package bio.face;

public class Faceprint {

	public native int jniFAStart(String cmmLibPath);
	public native int jniFAEnd();
	public native int jniFAExtractDB(String confPath, String unicode, byte[] Image, int nImageSize, int nFormat, byte[] DBFeature);
	static {
		System.loadLibrary("Faceprint");
	}
}

