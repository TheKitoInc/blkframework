
import BLK.System.Utils.Crypto.RSA;
import java.math.BigInteger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author andresrg
 */
public class DemoRSA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        RSA rsa = new RSA(2048);
        
        System.out.println(rsa.getN());
        System.out.println(rsa.getD());
        System.out.println(rsa.getE()); 
        
        
        
        BigInteger[] data = rsa.encrypt(new String("Hola que tal").getBytes());
        
        for(BigInteger bi : data)
            System.out.println(bi.toString());
        
        System.out.println(new String(rsa.decrypt(data)));
    }
}
