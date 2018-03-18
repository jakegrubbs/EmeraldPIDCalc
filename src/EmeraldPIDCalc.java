import java.io.*;
import java.util.*;

public class EmeraldPIDCalc
{
	private FileReader fr;
    private BufferedReader br;
    private int numberOfFrames = 0;
	
    public EmeraldPIDCalc(String fileName) throws IOException
    {
    	System.out.println("How many frames to read? (There are about 850 corruption type 7 frames per 10000)");
    	Scanner sc = new Scanner(System.in);
    	totalFrames(sc.nextInt());
    	
    	fr = new FileReader(fileName);
		br = new BufferedReader(fr);
		
		String line = br.readLine();
		
		int remainingFrames = numberOfFrames;
		
		while (line != null && remainingFrames > 0)
		{
			String[] ar = line.trim().split(" ");
			//for (String s : ar) System.out.println(s);
			long PID = hexToDecimal(ar[2]);
			if (isValidFrame(PID, ar[2].charAt(0)))
			{
				int frame = Integer.valueOf(ar[0]);
				String time = ar[1];
				String nature = ar[3];
				char ability = ar[4].charAt(0);
				byte hp = Byte.valueOf(ar[5]);
				byte attack = Byte.valueOf(ar[6]);
				byte defense = Byte.valueOf(ar[7]);
				byte sAttack = Byte.valueOf(ar[8]);
				byte sDefense = Byte.valueOf(ar[9]);
				byte speed = Byte.valueOf(ar[10]);
				String hiddenPower = ar[11];
				String gender50 = ar[13]; //for 50% F
				String gender12_5 = ar[14]; //for 12.5% F
				if (!isTooEarly(time))
				{
					printFrame(ar[2], frame, time, nature, ability, hp, attack, defense, sAttack, sDefense, speed, hiddenPower, gender50, gender12_5);
					remainingFrames--;
				}
			}
			
			line = br.readLine();
		}
		br.close();
		
		System.out.println("Number of frames printed: " + (numberOfFrames - remainingFrames));
    }
    
    public void printFrame(String PID, int frame, String time, String nature, char ability, byte hp, byte attack, byte defense, byte sAttack, byte sDefense, byte speed, String hiddenPower, String gender50, String gender12_5)
    {
    	System.out.println(
    			"PID: " + PID +
    			"\nFrame: " + frame +
    			"\nTime: " + time +
    			"\nNature: " + nature +
    			"\nAbility: " + ability +
    			"\nIVs: " + hp + "/" + attack + "/" + defense + "/" + sAttack + "/" + sDefense + "/" + speed +
    			"\nHidden Power type: " + hiddenPower +
    			"\nGender (50%/12.5%): " + gender50 + "/" + gender12_5 + "\n"
    			);
    }
    
    public boolean isValidFrame(long PID, char firstChar)
    {
    	switch(firstChar)
    	{
    		case '0': case '1':
    		case '2': case '3':
    		case '8': case '9':
    		case 'A': case 'B':
    			return (PID % 24 == 2 || PID % 24 == 3 || PID % 24 == 12 || PID % 24 == 13);
    		default: break;
    	}
    	return false;
    }
    
    public boolean isTooEarly(String time)
    {
    	//TODO parse time to earliest possible frame: ex. 0:08.00
    	//TODO rework the time calc so it's not bad
    	if (time.charAt(0) == '0' && time.charAt(2) == '0' && time.charAt(3) <= 56)
    	{
    		return true;
    	}
    	else return false;
    }
    
    public void totalFrames(int input)
    {
    	numberOfFrames = input;
    }
    
    public long hexToDecimal(String s)
	{
		String digits = "0123456789ABCDEF";
		s = s.toUpperCase();
		long val = 0;
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			long d = digits.indexOf(c);
			val = 16 * val + d;
		}
		return val;
	}
    
	public static void main(String[] args) throws IOException
	{
		EmeraldPIDCalc calc = new EmeraldPIDCalc("test.txt");
	}
}