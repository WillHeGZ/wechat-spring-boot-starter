package xyz.ttooc.spring.starter.wechat.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;

/**
 * @Auther: Alan
 * @Date: 2019-06-18 15:08
 * @Description: AES加密
 */
@Slf4j
public class AES {
    public static boolean initialized = false;

    /**
     * AES解密
     *
     * @param content 密文
     * @return
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchProviderException
     */
    public byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidParameterSpecException {
        initialize();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            Key sKeySpec = new SecretKeySpec(keyByte, "AES");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (NoSuchAlgorithmException e) {
            log.error("decrypt error : {}", e);
            throw e;
        } catch (NoSuchPaddingException e) {
            log.error("decrypt error : {}", e);
            throw e;
        } catch (InvalidKeyException e) {
            log.error("decrypt error : {}", e);
            throw e;
        } catch (IllegalBlockSizeException e) {
            log.error("decrypt error : {}", e);
            throw e;
        } catch (BadPaddingException e) {
            log.error("decrypt error : {}", e);
            throw e;
        } catch (NoSuchProviderException e) {
            log.error("decrypt error : {}", e);
            // TODO Auto-generated catch block
            throw e;
        } catch (RuntimeException e) {
            log.error("decrypt error : {}", e);
            // TODO Auto-generated catch block
            throw e;
        } catch (InvalidAlgorithmParameterException e) {
            log.error("decrypt error : {}", e);
            // TODO Auto-generated catch block
            throw e;
        } catch (InvalidParameterSpecException e) {
            log.error("decrypt error : {}", e);
            // TODO Auto-generated catch block
            throw e;
        }
    }

    public static void initialize() {
        if (initialized)
            return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

    // 生成iv
    public static AlgorithmParameters generateIV(byte[] iv) throws NoSuchAlgorithmException, InvalidParameterSpecException {
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;

    }
}
