/*
 * Classe de utilidades para operações comuns no projeto blockchain.
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    /*
     * Gera um hash SHA-256 de uma string.
     *
     * @param text Texto a ser convertido em hash.
     * @return String contendo o hash SHA-256 da entrada.
     */
    public static String hash256(String text){
        MessageDigest digest;  // Objeto para cálculo do hash

        try {
            digest = MessageDigest.getInstance("SHA-256"); // Inicializa o objeto digest com o algoritmo SHA-256
        } catch (NoSuchAlgorithmException e) {
            return "HASH_ERROR"; // Retorna "HASH_ERROR" se o algoritmo não for encontrado
        }

        final byte bytes[] = digest.digest(text.getBytes()); // Calcula o hash dos bytes do texto
        final StringBuilder hexString = new StringBuilder(); // Inicializa uma string para armazenar o hash em hexadecimal

        for (final byte b : bytes) {
            String hex = Integer.toHexString(0xff & b); // Converte o byte para um número inteiro em hexadecimal. 0xff é usado para converter o byte para um número positivo.
            if (hex.length() == 1) {
                hexString.append('0'); // Adiciona um zero à esquerda se o número for menor que 16
            }
            hexString.append(hex); // Adiciona o número hexadecimal à string
        }
        return hexString.toString(); // Retorna o hash SHA-256 como uma string em hexadecimal
    }
}
