package logica;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    private static SecretKeySpec secretKey;

    public Crypto() {
        final String b64PrivateKey = System.getenv("SECRET_KEY");
		final byte[] decodedPrivateKey = Base64.getDecoder().decode(b64PrivateKey);
		secretKey = new SecretKeySpec(decodedPrivateKey, 0, decodedPrivateKey.length, "AES");
    }

    public byte[] encryptImage(byte[] image) throws Exception {
		// AES Cipher Instance
		Cipher cipherEncrypt = Cipher.getInstance("AES");
		cipherEncrypt.init(Cipher.ENCRYPT_MODE, secretKey);
		
		//Start encryption
		byte[] encryptedImage = cipherEncrypt.doFinal(image);

		return encryptedImage;
	}

	public byte[] decryptImage(byte[] encryptedImage) throws Exception{
		// AES Cipher Instance
		Cipher cipherDecrypt = Cipher.getInstance("AES");
		cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKey);

		byte[] decryptedImage = cipherDecrypt.doFinal(encryptedImage);

		return decryptedImage;
	}
}
