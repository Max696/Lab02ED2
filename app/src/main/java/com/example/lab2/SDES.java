/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.lab2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class SDES {
       
   static int P10(int key){
        //permutacion de 10
        return permutacion(key, 4,2,1,9,0,6,3,8,5,7);
    }

    static int LI(int key) {
        //lado izquierdo de la key 
        return permutacion(key, 4,0,1,2,3,9,5,6,7,8);
    }

    static int P8(int key) {
        //permutacion de 8
        return permutacion(key, 1,0,5,2,6,3,7,4);
    }
   
    public static String encrypt(String text, int key) {
        int[] keys = getK12(key); // generamos las dos llaves 
        StringBuilder add = new StringBuilder(text.length());
        
        for (int i = 0; i < text.length(); i++)
        {
              add.append((char)fullEncriptar(text.charAt(i), keys));
              // ciframos letra por letra
        }         

        return add.toString();
    }

    
 
        public static String decrypt(String msg, int key) {
            int[] keys = getK12(key);
            StringBuilder builder = new StringBuilder(msg.length());
            for (int i = 0; i < msg.length(); i++)
                builder.append((char)fullDecencriptar(msg.charAt(i), keys));

        return builder.toString();
    }

       

    static int fullEncriptar(int c, int[] keys) {
        int result = bitCreation(IP(c), keys[0]);
          result = (result << 28) >>> 24 | (result >>> 4);
        result = bitCreation    (result, keys[1]);
        return IPtranspuesta(result);
    }

    static int fullDecencriptar(int c, int[] keys) {
        int[] rKeys = new int[2];
        rKeys[0] = keys[1];
        rKeys[1] = keys[0];
        return fullEncriptar(c, rKeys);
    }

    static int bitCreation(int text, int subKey) {
       int L = text >>> 4;
       // divide el bit del texto en cuatro
      int R = text << 28 >>> 28;
      // eleva a 28 y luego divide en 28 
        return (L^finite(R, subKey)) << 4 | R;
        //L XOR finite  or R
        
    }

    

    static int[] getK12(int key) {
        int[] result = new int[2];
        int shift = LI(P10(key));
        result[0] = P8(shift);
        shift = LI(shift);
        result[1] = P8(shift);
        return result;
    }

    static int IP(int text) {
        return permutacion(text, 1,3,0,4,7,5,2,6);
    }

    static int IPtranspuesta(int text) {
        return permutacion(text, 2, 0, 6, 1, 3, 5, 7, 4);
    }
/*
    
    int... nos deja introducir los numeros como una secuencia  para no crear diferentes metodos para cada permutacion
    */
    static int permutacion(int bits, int... permutationOrder) {
        int permutatedBits = 0;
        for(int i = 0; i < permutationOrder.length; i++)
            permutatedBits |= ((bits >>> permutationOrder[i]) & 1) << i;
        //operacion or
        return permutatedBits;
    }

      private final static int[][] SBox0 = new int[][] {
            {1,0,3,2},
            {3,2,1,0},
            {0,2,1,3},
            {3,1,3,1}
    };

    private final static int[][] SBox1 = new int[][] {
            {1,1,2,3},
            {2,0,1,3},
            {3,0,1,0},
            {2,1,0,3}
    };

    static int finite(int text, int subKey) {
       
       int permutation = permutacion(text, 3,4,1,2,1,2,9,0);
        permutation ^= subKey;
        //XOR  entre las  OP y key

        int substituted = 0;
        int rows = ((permutation & (1 << 7)) >>> 6) | (permutation & (1 << 4)) >>> 4;
        int columns  = ((permutation & (1 << 6)) >>> 5) | (permutation & (1 << 5)) >>> 5;
            
        substituted |= SBox0[rows][columns] << 2;
        rows = ((permutation & (1 << 3)) >>> 2) | (permutation & 1);
        columns  = ((permutation & (1 << 2)) >>> 1) | (permutation & (1 << 1)) >>> 1;
        substituted |= SBox1[rows][columns];

        return permutacion(substituted, 3,1,0,2);
    }

   
}
