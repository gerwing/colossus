package colossus.utilities;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 * Use this class to obtain a P array based on decimal values of PI, to be used by a 16 round Feistel scheme with 64bit Block size
 * the getPiBitArray method will return the generated P array
 * @author Colossus Group
 *
 */
public class PI {
	
	//256 decimals of PI in hexadecimal value, each 4 bit, 1024 bits in total
	private String pi = "243F6A8885A308D313198A2E03707344A4093822299F31D0082EFA98EC4E6C89" +
			"452821E638D01377BE5466CF34E90C6CC0AC29B7C97C50DD3F84D5B5B5470917" +
			"9216D5D98979FB1BD1310BA698DFB5AC2FFD72DBD01ADFB7B8E1AFED6A267E96" +
			"BA7C9045F12C7F9924A19947B3916CF70801F2E2858EFC16636920D871574E69" ;
	
	private byte[] piBytes;
	
	/**
	 * Construct new PI object
	 */
	public PI(){
		//turn 256 decimals into 1024 bit array
		HexBinaryAdapter hba = new HexBinaryAdapter();
		piBytes = hba.unmarshal(pi);
	}
	
	/**
	 * This method returns a P-Array containing 16x64bit keys based on 256 decimals of PI. 
	 * This array can be used as an initial P array holding 16 x 64 bit keys (or 16 x 8 byte arrays)
	 * @return 1024 Bit Array, based on decimal values of PI
	 */
	public Object[] getPiByteArray(){
		Object[] pArray = new Object[16];//array to be returned
		//divide piBytes array into 16x8byte array
		for(int i=0;i<16;i++)
		{
			byte[] a = new byte[8];
			for(int b=0;b<8;b++)
			{
				a[b] = piBytes[b+(i*8)];
			}
			pArray[i] = a;
		}
		return pArray;
	}
}
