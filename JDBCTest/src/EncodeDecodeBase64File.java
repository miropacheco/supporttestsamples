import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;

public class EncodeDecodeBase64File {
	public static void main(String s[]) throws IOException {
		String inputFile = s[0];
		String outputFile = s[1];
		String action = s.length > 2 ? s[2] : "DECODE";
		switch (action.toUpperCase()) {
		case "DECODE": {
			StringBuffer strBuffer = new StringBuffer();

			byte[] b = new byte[1024];

			try {

				BufferedInputStream inStr = new BufferedInputStream(new FileInputStream(inputFile));
				while (true) {
					int read = inStr.read(b, 0, 1024);
					if (read == -1) {
						break;
					} else {
						strBuffer.append(new String(b, 0, read));
					}

				}
				byte[] output = Base64.getDecoder().decode(strBuffer.toString());
				FileOutputStream fos = new FileOutputStream(outputFile);
				fos.write(output);
				inStr.close();
				fos.flush();
				fos.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		}
		case "ENCODE": {
			ByteArrayOutputStream bArray = new ByteArrayOutputStream();

			byte[] b = new byte[1024];

			try {

				BufferedInputStream inStr = new BufferedInputStream(new FileInputStream(inputFile));
				while (true) {
					int read = inStr.read(b, 0, 1024);
					if (read == -1) {
						break;
					} else {
						bArray.write(b, 0,read);
					}

				}
				byte[] output = Base64.getEncoder().encode(bArray.toByteArray());
				FileOutputStream fos = new FileOutputStream(outputFile);
				fos.write(output);
				inStr.close();
				fos.flush();
				fos.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
			

		}
		default:
			System.out.println("Action should be Encode/Decode");

		}

	}

}
