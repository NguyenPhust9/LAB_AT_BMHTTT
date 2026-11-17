package week_02;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CryptoTest {
    private static final String TEXT = "Xin chào Lab 2 - Mã hóa hiện đại!";

    @Test void desRoundTripAndRandomIv() throws Exception {
        String first = DESCipher.encrypt(TEXT, "12345678");
        assertEquals(TEXT, DESCipher.decrypt(first, "12345678"));
        assertNotEquals(first, DESCipher.encrypt(TEXT, "12345678"));
        assertThrows(IllegalArgumentException.class, () -> DESCipher.encrypt(TEXT, "1234"));
    }

    @Test void tripleDesRejects23Bytes() throws Exception {
        String key = "123456789012345678901234";
        assertEquals(TEXT, TripleDESCipher.decrypt(TripleDESCipher.encrypt(TEXT, key), key));
        assertThrows(IllegalArgumentException.class, () -> TripleDESCipher.encrypt(TEXT, key.substring(1)));
    }

    @Test void aesAuthenticatesCiphertextAndKey() throws Exception {
        String encrypted = AESCipher.encrypt(TEXT, "MatKhauAES");
        assertEquals(TEXT, AESCipher.decrypt(encrypted, "MatKhauAES"));
        assertThrows(javax.crypto.AEADBadTagException.class, () -> AESCipher.decrypt(encrypted, "SaiKey"));
        byte[] data = CipherUtil.fromB64(encrypted);
        data[data.length - 1] ^= 1;
        assertThrows(javax.crypto.AEADBadTagException.class, () -> AESCipher.decrypt(CipherUtil.b64(data), "MatKhauAES"));
        assertNotEquals(encrypted, AESCipher.encrypt(TEXT, "MatKhauAES"));
    }

    @Test void rc4KnownVector() {
        assertEquals("BBF316E8D940AF0AD3", RC4Cipher.encrypt("Plaintext", "Key"));
        assertEquals(TEXT, RC4Cipher.decrypt(RC4Cipher.encrypt(TEXT, "Key"), "Key"));
        assertThrows(IllegalArgumentException.class, () -> RC4Cipher.encrypt(TEXT, "x".repeat(257)));
    }

    @Test void twofishFileRestoresIv() throws Exception {
        String iv = TwofishCipher.newIv();
        String ciphertext = TwofishCipher.encrypt(TEXT, "Twofish@Lab2", iv);
        String file = TwofishCipher.toFile(iv, ciphertext);
        String[] parts = TwofishCipher.fromFile(file.replace("\n", "\r\n"));
        assertEquals(iv, parts[0]);
        assertEquals(TEXT, TwofishCipher.decrypt(parts[1], "Twofish@Lab2", parts[0]));
        assertFalse(file.contains("Twofish@Lab2"));
        assertThrows(IllegalArgumentException.class, () -> TwofishCipher.fromFile(ciphertext));
    }

    @Test void ctrLongTextHasNoPaddingAndFileRestoresNonce() throws Exception {
        String text = TEXT.repeat(200), key = AESCTRCipher.newKey(), nonce = AESCTRCipher.newNonce();
        String ciphertext = AESCTRCipher.encrypt(text, key, nonce);
        assertEquals(CipherUtil.utf8(text).length, CipherUtil.fromB64(ciphertext).length);
        String[] parts = AESCTRCipher.fromFile(AESCTRCipher.toFile(nonce, ciphertext));
        assertEquals(nonce, parts[0]);
        assertEquals(text, AESCTRCipher.decrypt(parts[1], key, parts[0]));
        assertThrows(IllegalArgumentException.class, () -> AESCTRCipher.encrypt(text, key, CipherUtil.b64(new byte[16])));
    }

    @Test void emptyAndLongMessages() throws Exception {
        for (String text : new String[]{"", "1234567890123456", TEXT.repeat(200)}) {
            assertEquals(text, DESCipher.decrypt(DESCipher.encrypt(text, "12345678"), "12345678"));
            assertEquals(text, AESCipher.decrypt(AESCipher.encrypt(text, "Password"), "Password"));
            String iv = TwofishCipher.newIv();
            assertEquals(text, TwofishCipher.decrypt(TwofishCipher.encrypt(text, "Key", iv), "Key", iv));
        }
    }

    @Test void malformedEncodingsAndCredentialsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> CipherUtil.fromB64("!"));
        assertThrows(IllegalArgumentException.class, () -> CipherUtil.fromHex("FFF"));
        assertThrows(IllegalArgumentException.class, () -> CipherUtil.fromHex("ZZ"));
        assertThrows(IllegalArgumentException.class, () -> UserDao.register("a'--", "12345678".toCharArray()));
        assertThrows(IllegalArgumentException.class, () -> UserDao.register("sinhvien01", "123".toCharArray()));
        assertTrue(UserDao.newTicket().matches("[0-9A-F]{32}"));
    }
}
