package colossus.utilities;

/**
 * Vigenere Class - Represents a Vigenere substitution square with 256x256 matrix
 * @author Colossus Group
 *
 */
public class Vigenere {
	
	byte[][] matrix;
	
	/**
	 * Construct a new Vigenere Square with 256 rows and columns
	 */
	public Vigenere(){
		/*
		 * Fill up matrix
		 */
		matrix = new byte[256][256]; 
		for(int i=0;i<256;i++){
			byte[] temp = new byte[256];
			for(int b=0;b<256;b++){
				temp[b] = (byte)(((b+i)%256)-128);
			}
			matrix[i] = temp;
		}
	}
	
	/**
	 * Substitute using plaintext and a key
	 * @param key One byte of the key
	 * @param plain One byte of the plaintext
	 * @return One substituted byte
	 */
	public byte substitute(byte key, byte plain){
		return matrix[plain+128][key+128];
	}
	
	/**
	 * De-Substitute using ciphertext and a key
	 * @param key One byte of the key
	 * @param cipher One byte of the cipher
	 * @return One de-substituted byte
	 */
	public byte desubstitute(byte key, byte cipher) {
		byte result = (byte)((((cipher+128)-(key+128)+256)%256)-128);
		return (result);
	}

}
