package com.example.lab2;

public class ZigZag {

    public String ZigZag(String texto, int numero){

        String[] lista = new String[numero];
        int contador = 0;
        Boolean abajo = true;
        int numerO = numero *2 -2;

        for (int i = 0; i < lista.length; i++) {
            lista[i] = "";
        }


        while(!(texto.length()% numerO == 0)){
            texto = texto + " ";
        }

        for (int i = 0; i < texto.length(); i++) {
            if (abajo) {
                lista[contador] = lista[contador] + Character.toString(texto.charAt(i));
                contador++;
                if(contador>=lista.length){
                    abajo = false;
                    contador = contador - 2;
                }
            }else{
                lista[contador] = lista[contador] + Character.toString(texto.charAt(i));
                contador--;
                if(contador<0){
                    abajo = true;
                    contador = 1;
                }
            }
        }
        String resultado = "";
        for (int i = 0; i < lista.length; i++) {
            resultado = resultado + lista[i];
        }
        return resultado;
    }
    public String DescifrarZig(String cifrado, int linea){
       int tex = cifrado.length();
        String[] lineazos = new String[linea];
        int elementosOlas = linea * 2 - 2 ;
        int numEle = tex / elementosOlas;
        String descifrado = "";

        String crestas = cifrado.substring(0,numEle);
        String enMedio;
        String base = cifrado.substring(cifrado.length()-numEle,cifrado.length());

        int restante = linea - 2;
        if(restante == 1){
            enMedio = cifrado.substring(numEle,cifrado.length()-numEle);
            lineazos = new String[linea];
            lineazos[0] = crestas;
            lineazos[1] = enMedio;
            lineazos[2] = base;
            int centrinho = 0;

            for (int i = 0; i < lineazos[0].length(); i++) {
                descifrado = descifrado + lineazos[0].charAt(i) + lineazos[1].charAt(centrinho) + lineazos[2].charAt(i) + lineazos[1].charAt(++centrinho);
                centrinho++;
            }
        }else if(restante == 0){
            for (int i = 0; i < crestas.length(); i++) {
                descifrado = descifrado + crestas.charAt(i) + base.charAt(i);
            }
        }else{
            lineazos = new String[linea];
            enMedio = cifrado.substring(numEle,cifrado.length()-numEle);

            for (int i = 0; i < lineazos.length; i++) {
                if(i ==0){
                    lineazos[i] = crestas;
                }else if (i != lineazos.length-1) {
                    lineazos[i] = enMedio.substring(0,numEle*2);
                    enMedio = enMedio.substring(numEle*2,enMedio.length());
                }else{
                    lineazos[i] = base;
                }
            }

            Boolean arriba = false;
            int i = 0;
            int c = 0;
            while(descifrado.length() < cifrado.length()){
                if (!arriba) {//Se debe bajar pero validar que no baje mas de lo que debería
                    descifrado = descifrado + lineazos[i].charAt(c);
                    lineazos[i] = lineazos[i].substring(1,lineazos[i].length());
                    i++;
                    if(i >= lineazos.length){
                        arriba = true;
                        i = i - 2;
                    }
                }else{
                    descifrado = descifrado + lineazos[i].charAt(c);
                    lineazos[i] = lineazos[i].substring(1,lineazos[i].length());
                    i--;
                    if(i==0){
                        arriba = false;
                    }
                }
            }
        }
        descifrado = descifrado.trim();
        return descifrado;
    }
}
