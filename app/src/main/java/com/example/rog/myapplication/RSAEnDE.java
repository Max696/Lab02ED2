package com.example.rog.myapplication;

import java.math.BigInteger;
import java.util.Random;

public class RSAEnDE {

    private BigInteger p;
    private BigInteger q;
    private BigInteger N;
    private BigInteger phi;
    private BigInteger e;
    private BigInteger d;
    private int bitlength = 1024;
    private Random r;
    public String Key1 = N + "," + e ; //PUBLIC|PRIVATE
    public String Key2 = N + "," + d;

    public RSAEnDE(){}

    public RSAEnDE(BigInteger p, BigInteger q){
        r = new Random();
        N = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(bitlength / 2, r);
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0)
        {
            e.add(BigInteger.ONE);
        }
        d = e.modInverse(phi);
    }

    //TEXTO DEL ARCHIVO.getBytes()
    public byte[] encrypt(byte[] message, BigInteger N, BigInteger e)
    {
        return (new BigInteger(message)).modPow(e, N).toByteArray();
    }

    // Decrypt message
    public byte[] decrypt(byte[] message, BigInteger N, BigInteger d)
    {
        return (new BigInteger(message)).modPow(d, N).toByteArray();
    }
}
