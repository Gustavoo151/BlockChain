package com.blockchain.blockchain.agent;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Classe de utilidades para o projeto.
 */
public class Utils {

    public static String hash256(String text){  // método que gera um hash SHA-256 de uma string
        MessageDigest digest;  // inicializa o objeto digest para calcular o hash

        try {
            digest = MessageDigest.getInstance("SHA-256"); // inicializa o objeto digest com o algoritmo SHA-256
        }catch (NoSuchAlgorithmException e){
            return "HASH_ERROR"; // retorna "HASH_ERROR" se o algoritmo não for encontrado
        }

        final byte bytes[] = digest.digest(text.getBytes()); // calcula o hash da string
        final StringBuilder hexString = new StringBuilder(); // inicializa uma string para armazenar o hash em hexadecimal

        for (final byte b : bytes){
            String hex = Integer.toHexString(0xff & b); // converte o byte para um número inteiro em hexadecimal. Oxff é usado para converter o byte para um número positivo.
            if (hex.length() == 1) {
                hexString.append('0'); // adiciona um zero à esquerda se o número for menor que 16
            }
            hexString.append(hex); // adiciona o número hexadecimal à string
        }
        return hexString.toString();
    }
}
