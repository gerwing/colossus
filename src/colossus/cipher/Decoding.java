package colossus.cipher;

import javax.xml.bind.DatatypeConverter;

import colossus.utilities.Vigenere;

/**
 * Decode cipher text using this class and the decode method
 * @author Colossus Group
 *
 */
public class Decoding {
	private String cipherText;
	private String password;
	private Object[] pArray;
	
	/**
	 * Encode a String using the Colossus cipher
	 * @param plain Plaintext to be encoded
	 * @param pass String of variable length to be used as a password
	 * @return Ciphertext
	 */
	public String decode(String cipher, String pass){
		//initialize plaintext and password
		cipherText = cipher;
		password = pass;
		
		//generate P array from password
		KeyGenerator kg = new KeyGenerator(password);
		pArray = kg.getPArray();
		
		//make array of 64bit pieces (8byte) for cipherText
		byte[] cipherBytes = DatatypeConverter.parseBase64Binary(cipherText);
		byte[][] cipher64Array = new byte[cipherBytes.length/8][8];
		for(int i=0;i<cipherBytes.length/8;i++)
		{
			byte[] tmp = new byte[8];
			for(int b=0;b<8;b++)
			{
				tmp[b] = cipherBytes[b+(8*i)]; 
			}
			cipher64Array[i] = tmp;
		}
		
		//decode 128bit at a time
		byte[][] resultArray = new byte[cipherBytes.length/8][8];
		for(int i=0;i<cipher64Array.length/2;i++)//for every 128bit
		{
			//first 64bit feistel
			byte [] temp = cipher64Array[i*2];
			for(int a=14;a>=0;a-=2)
			{
				temp = feistel((byte[])pArray[a],temp);
			}
			resultArray[i*2] = temp;
			
			//second 64bit feistel
			temp = cipher64Array[i*2+1];
			for(int a=15;a>=0;a-=2)
			{
				temp = feistel((byte[])pArray[a],temp);
			}
			resultArray[i*2+1] = temp;
		}
		
		//put bytes back in one byte array
		byte[] plainBytes = new byte[cipherBytes.length];
		for(int i=0;i<resultArray.length;i++)
		{
			for(int b=0;b<8;b++)
			{
				plainBytes[b+(i*8)] = resultArray[i][b];
			}
		}
		
		//Remove zeros that were added during encoding
		//first count amount of zeros
		int zeroCount = 0;
		for(byte b:plainBytes)
		{
			if(b == 0)
				zeroCount++;
		}
		//secondly remove them
		byte[] temp = new byte[plainBytes.length-zeroCount];
		for(int i=0;i<plainBytes.length-zeroCount;i++)
		{
			temp[i] = plainBytes[i];
		}
		plainBytes = temp;
		
		return new String(plainBytes);
	}
	
	/*
	 * Feistel method, decodes a 64bit block
	 */
	private byte[] feistel(byte[] subkey, byte[] cipher) {
		
		byte[] result = new byte[8];
		//De-Substitute
		Vigenere vn = new Vigenere();
		for(int i=0;i<8;i++)
		{
			result[i] = vn.desubstitute(subkey[i], cipher[i]);
		}
		//XOR
		for(int i=0;i<8;i++)
		{
			result[i] = (byte)(result[i] ^ subkey[i]);
		}
		return result;
	}
}
