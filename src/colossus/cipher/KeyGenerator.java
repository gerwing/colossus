package colossus.cipher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import colossus.utilities.PI;
import colossus.utilities.Vigenere;

/**
 * Keygenerator Class
 * Can be used for generating a random strong password (44 chars)
 * And for generating a P array from a given password
 * @author Colossus Group
 *
 */
public class KeyGenerator {

	private String key;
	private byte[] finalKey;
	
	/**
	 * Constructor construct a new KeyGenerator object
	 */
	public KeyGenerator(){
		key = "";
		hashKey();
	}
	
	/** Constructor construct a new KeyGenerator object
	 * @param key The key to be used as a String. The key can be of any length but preferably at least 40 characters
	 */
	public KeyGenerator(String key){
		this.key = key;
		hashKey();
	}
	
	/**
	 * Set the key to be used for the Key generation
	 * @param key String of any amount of characters but preferably at least 40
	 */
	public void setKey(String key){
		this.key = key;
		hashKey();
	}
	
	/**
	 * This method generates and returns the P Array containing 16 x 64 bit subkeys to be used in the Feistel Scheme
	 * @return P-Array containing 16 subkeys
	 */
	public Object[] getPArray(){
		//resulting pArray
		Object[] result = new Object[16];
		
		//initial P array derived from PI decimals
		PI pi = new PI();
		Object[] pArray = pi.getPiByteArray();
		
		//Split key into 4 x 8 byte array
		Object[] keyArray = new Object[4];
		for(int i=0;i<4;i++)
		{
			byte[] b = new byte[8];
			for(int a=0;a<8;a++)
			{
				b[a] = finalKey[a+i*8];
			}
			keyArray[i] = b;
		}
		
		//Do feistel scheme for each 64 bit part in the keyArray
		byte[] temp = (byte[])keyArray[0];
		for(int i=0;i<8;i+=2)
		{
			temp = feistel((byte[])pArray[i],temp);
			result[i] = temp;
		}
		temp = (byte[])keyArray[1];
		for(int i=1;i<8;i+=2)
		{
			temp = feistel((byte[])pArray[i],temp);
			result[i] = temp;
		}
		temp = (byte[])keyArray[2];
		for(int i=8;i<16;i+=2)
		{
			temp = feistel((byte[])pArray[i],temp);
			result[i] = temp;
		}
		temp = (byte[])keyArray[3];
		for(int i=9;i<16;i+=2)
		{
			temp = feistel((byte[])pArray[i],temp);
			result[i] = temp;
		}
		
		return result;
	}

	/**
	 * This method generates a random key with a length of 44 characters
	 * The time needed to force attack this key is the same as a 256bit key
	 * @return
	 */
	public static String getRandomKey(){
		final String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String result = "";
		Random rand = new Random();
		for(int i = 0;i<44;i++)
		{
			result += alphabet.charAt(rand.nextInt(alphabet.length()));
		}
		return result;
	}
	
	/*
	 * Custom feistel scheme for Key generation
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
	
	/*
	 * Hash the given key of variable length into a 256 bit value that will be used for the Key generation
	 */
	private void hashKey(){
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(key.getBytes());
			finalKey = md.digest();
		} 
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
