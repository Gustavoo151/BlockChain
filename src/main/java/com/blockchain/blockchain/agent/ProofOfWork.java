package com.blockchain.blockchain.agent;

/*
 * ProofOfWork é a classe responsável por realizar a mineração de um bloco na blockchain.
 * Ela utiliza um algoritmo de prova de trabalho para encontrar um nonce que produza um hash
 * do bloco que satisfaça uma dificuldade pré-definida.
 */
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class ProofOfWork {
    private Block mBlock;  // Bloco a ser minerado
    private BigInteger mTarget;  // Alvo da mineração
    public static int mDifficulty;  // Dificuldade da mineração

    /*
     * Construtor da classe ProofOfWork.
     * Inicializa o bloco a ser minerado e calcula o alvo da mineração com base na dificuldade.
     */
    public ProofOfWork(Block mBlock){
        this.mBlock = mBlock;  // Inicializa o bloco a ser minerado
        mTarget = BigInteger.ONE;  // Inicializa o alvo da mineração com 1
        mTarget = mTarget.shiftLeft(256 - ProofOfWork.mDifficulty);  // Calcula o alvo da mineração com base na dificuldade.
        // O alvo é calculado como 2^(256 - mDifficulty). Isso significa que o alvo é um número que tem 256 - mDifficulty bits 0 no início.
    }

    /*
     * Método privado que prepara os dados para serem minerados.
     * Concatena o hash do bloco anterior, dados do bloco, timestamp, alvo da mineração e nonce.
     */
    private String prepareData(int nonce){
        String result = mBlock.getPreviousHash() +  // Concatena o hash do bloco anterior
                mBlock.getData() +  // Concatena os dados do bloco
                mBlock.getTimestamp() +  // Concatena o timestamp do bloco
                Integer.toHexString(mTarget.intValue()) +  // Concatena o alvo da mineração em hexadecimal
                Integer.toHexString(nonce);  // Concatena o nonce em hexadecimal
        return result;
    }

    /*
     * Método que realiza a mineração do bloco.
     * Retorna um mapa contendo o nonce e o hash do bloco minerado se a mineração for bem-sucedida,
     * caso contrário, retorna null.
     */
    public Map<String, String> run(){
        String hash = "";  // Inicializa o hash do bloco como uma string vazia
        int nonce = 0;  // Inicializa o nonce como 0

        while (true) {  // Loop que incrementa o nonce até encontrar um hash que satisfaça a dificuldade da mineração
            String data = prepareData(nonce);  // Prepara os dados para serem minerados
            hash = Utils.hash256(data);  // Calcula o hash dos dados preparados usando o algoritmo SHA-256
            BigInteger hashInt = new BigInteger(hash, 16);  // Converte o hash para um número inteiro em hexadecimal (base 16)

            if (hashInt.compareTo(mTarget) == -1 ) {  // Verifica se o hash é menor que o alvo da mineração
                break;  // Se o hash satisfaz a dificuldade da mineração, sai do loop
            } else {
                nonce++;  // Incrementa o nonce e tenta novamente
            }
        }

        // Prepara os dados para validação do bloco minerado
        String validation = mBlock.getPreviousHash() +
                mBlock.getTimestamp() +
                Integer.toHexString(mTarget.intValue()) +
                Integer.toHexString(nonce);

        // Verifica se o hash calculado é igual ao hash encontrado
        if (Utils.hash256(validation).equals(hash)){
            // Se o hash calculado for igual ao hash encontrado, cria um mapa para armazenar os dados do bloco minerado
            Map<String, String> map = new HashMap<>();
            map.put("nonce", String.valueOf(nonce));  // Armazena o nonce como uma string no mapa
            map.put("hash", hash);  // Armazena o hash do bloco minerado no mapa

            return map;  // Retorna o mapa com os dados do bloco minerado
        } else {
            return null;  // Se o hash calculado não for igual ao hash encontrado, retorna null
        }
    }
}
