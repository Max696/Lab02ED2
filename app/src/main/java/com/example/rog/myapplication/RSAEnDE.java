package com.example.rog.myapplication;

import java.math.BigInteger;
import java.util.Random;

public class RSAEnDE { BigInteger p, q, e, d, phi, n;

    public RSAEnDE(){

    }

    public RSAEnDE(BigInteger p, BigInteger q){
        this.p = p;
        this.q = q;
        generateKeys();
    }


    public void generateKeys(){
        n = p.multiply(q);

        BigInteger one = new BigInteger("1");
        phi = p.subtract(one).multiply(q.subtract(one));

        Random random = new Random();
        while(e == null){
            Integer value = random.nextInt(phi.intValue());

            if (validatePrime(value)) {
                e = new BigInteger(value.toString());
            }
        }

        d = e.modInverse(phi);

        int k =0;
    }

    public String getPublicKey(){
        return n.intValue() + "," + e.intValue();
    }

    public String getPrivateKey(){
        return n.intValue() + "," + d.intValue();
    }


    public boolean validatePrime(int num) {
        int cont = 0;
        for (int i = 1; i <= num; i++) {

            if ((num % i) == 0) {

                cont++;
            }
        }
        if (cont <= 2) {
            return true;
        }
        return false;
    }

    /**
     * Metodo para cifrar/descifrar un texto
     * @param key
     * @param text
     * @return
     */
    public String encode(String key, String text){
        String encodeText = "";
        String[] keyValue = key.split(",");
        BigInteger exponent = new BigInteger(keyValue[1]);
        BigInteger mod = new BigInteger(keyValue[0]);
        int cont = 0;

        while(cont < text.length()){
            encodeText += (char) code(exponent, mod, text.charAt(cont)).intValue();
            cont++;
        }
        return encodeText;
    }


    public BigInteger code(BigInteger exponent, BigInteger mod, char character) {

        Integer characterValue = Integer.valueOf(character);
        BigInteger base = new BigInteger(characterValue.toString());
        return base.modPow(exponent, mod);

    }
}
