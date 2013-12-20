package colossus.cipher;

import javax.xml.bind.DatatypeConverter;

import colossus.utilities.Vigenere;

/**
 * Encode Plain text using this class and the encode method
 * @author Colossus Group
 *
 */
public class Encoding {

	private String plainText;
	private String password;
	private Object[] pArray;
	
	/**
	 * Encode a String using the Colossus cipher
	 * @param plain Plaintext to be encoded
	 * @param pass String of variable length to be used as a password
	 * @return Ciphertext
	 */
	public String encode(String plain, String pass){
		//initialize plaintext and password
		plainText = plain;
		password = pass;
		
		//generate P array from password
		KeyGenerator kg = new KeyGenerator(password);
		pArray = kg.getPArray();
		
		//make sure the whole text is in pieces of 16 bytes or 128 bits (the block size)
		byte[] plainBytes = plainText.getBytes();
		int missing = 16-(plainBytes.length%16);
		byte byt = 0;
		byte[] temp = new byte[plainBytes.length+missing];
		for(int i=0;i<plainBytes.length;i++)
		{
			temp[i] = plainBytes[i];
		}
		for(int i=plainBytes.length;i<plainBytes.length+missing;i++)
		{
			temp[i] = byt;
		}
		plainBytes = temp;
		
		//make array of 64bit pieces (8byte) for plaintext
		byte[][] plain64Array = new byte[plainBytes.length/8][8];
		for(int i=0;i<plainBytes.length/8;i++)
		{
			byte[] tmp = new byte[8];
			for(int b=0;b<8;b++)
			{
				tmp[b] = plainBytes[b+(8*i)]; 
			}
			plain64Array[i] = tmp;
		}
		
		//encode 128bit at a time
		byte[][] resultArray = new byte[plainBytes.length/8][8];
		for(int i=0;i<plain64Array.length/2;i++)//for every 128bit
		{
			//first 64bit feistel
			temp = plain64Array[i*2];
			for(int a=0;a<16;a+=2)
			{
				temp = feistel((byte[])pArray[a],temp);
			}
			resultArray[i*2] = temp;
			
			//second 64bit feistel
			temp = plain64Array[i*2+1];
			for(int a=1;a<16;a+=2)
			{
				temp = feistel((byte[])pArray[a],temp);
			}
			resultArray[i*2+1] = temp;
		}
		
		//put bytes back in one byte array
		byte[] cipherBytes = new byte[plainBytes.length];
		for(int i=0;i<resultArray.length;i++)
		{
			for(int b=0;b<8;b++)
			{
				cipherBytes[b+(i*8)] = resultArray[i][b];
			}
		}
		
		return DatatypeConverter.printBase64Binary(cipherBytes);
	}
	
	/*
	 * Feistel method, encodes a 64bit block
	 */
	private byte[] feistel(byte[] subkey, byte[] plain) {
		//XOR
		byte[] result = new byte[8];
		for(int i=0;i<8;i++)
		{
			result[i] = (byte)(plain[i] ^ subkey[i]);
		}
		//Substitute
		Vigenere vn = new Vigenere();
		for(int i=0;i<8;i++)
		{
			result[i] = vn.substitute(subkey[i], result[i]);
		}
		return result;
	}
}
