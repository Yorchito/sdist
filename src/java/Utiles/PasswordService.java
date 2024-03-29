/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utiles;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Ernesto
 */
public final class PasswordService
{
  private static PasswordService instance;

  private PasswordService()
  {
  }

  public synchronized String encrypt(String plaintext) throws Exception
  {
    MessageDigest md = null;
    try
    {
      md = MessageDigest.getInstance("SHA"); //step 2
    }
    catch(Exception e)
    {
     // throw new SystemUnavailableException(e.getMessage());
        System.out.println(e);
    }
    try
    {
      md.update(plaintext.getBytes("UTF-8")); //step 3
    }
    catch(Exception e)
    {
     // throw new SystemUnavailableException(e.getMessage());
        System.out.println(e);
    }

    byte raw[] = md.digest(); //step 4
    String hash = (new BASE64Encoder()).encode(raw); //step 5
    return hash; //step 6
  }
  
  public static synchronized PasswordService getInstance() //step 1
  {
    if(instance == null)
    {
       instance = new PasswordService(); 
    } 
    return instance;
  }
}
