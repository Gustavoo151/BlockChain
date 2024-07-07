package com.blockchain.blockchain.agent;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/*
 * ProofOfWork é a classe responsável por realizar a mineração
 * de um bloco.
 */

public class ProofOfWork {
    private Block mBlock;  // bloco a ser minerado
    private BigInteger mTarget;  // alvo da mineração
    public static int mDificult;  // dificuldade da mineração

    public ProofOfWork(Block mBlock){ // construtor da classe
        this.mBlock = mBlock;  // inicializa o bloco a ser minerado
        mTarget = BigInteger.ONE;  // inicializa o alvo da mineração com 1
        mTarget = mTarget.shiftLeft(256 - ProofOfWork.mDificult);   // calcula o alvo da mineração com base na dificuldade. Essa dificuldade é definida pela variável mDificult.
        // O alvo é calculado como 2^(256 - mDificult). Isso significa que o alvo é um número que tem 256 - mDificult bits 0 no início.
    }

    private String prePareData(int nonce){  // método que prepara os dados para serem minerados
        String result = mBlock.getPreviousHash() +  // concatena o hash do bloco anterior
                mBlock.getData() +  // concatena os dados do bloco
                mBlock.getTimestamp() +  // concatena o timestamp do bloco
                Integer.toHexString(mTarget.intValue()) +  // concatena o alvo da mineração em hexadecimal
                Integer.toHexString(nonce); // concatena o nonce em hexadecimal (nonce é um número que é incrementado até que o hash do bloco seja menor que o alvo)
        return result;
    }

    public Map<String, String> run(){  // método que realiza a mineração do bloco
        String hash = "";  // inicializa o hash do bloco como uma string vazia
        int nonce = 0; // inicializa o nonce como 0. O nonce é um número que é incrementado até que o hash do bloco seja menor que o alvo. Isso é feito para encontrar um hash que satisfaça a dificuldade da mineração.

        while (true) {  // loop que incrementa o nonce até encontrar um hash que satisfaça a dificuldade da mineração
            String data = prePareData(nonce); // prepara os dados para serem minerados
            hash = Utils.hash256(data); // calcula o hash dos dados preparados com o algoritmo SHA-256
            BigInteger hashInt = new BigInteger(hash, 16); // converte o hash para um número inteiro em hexadecimal (base 16)

            if (hashInt.compareTo(mTarget) == -1 ) { // verifica se o hash é menor que o alvo da mineração (isso significa que o hash satisfaz a dificuldade da mineração)
                break; // se o hash satisfaz a dificuldade da mineração, sai do loop
            }
            else{
                nonce++; // se o hash não satisfaz a dificuldade da mineração, incrementa o nonce e tenta novamente
            }
        }

        String validation = mBlock.getPreviousHash() +  // prepara os dados para validação
                mBlock.getTimestamp() +  // concatena o timestamp do bloco
                Integer.toHexString(mTarget.intValue()) +  // concatena o alvo da mineração em hexadecimal
                Integer.toHexString(nonce);  // concatena o nonce em hexadecimal

        if (Utils.hash256(validation).equals(hash)){  // verifica se o hash calculado é igual ao hash encontrado
            Map<String, String> map = new HashMap<>(100);  // cria um mapa para armazenar os dados do bloco minerado
            map.put("nonce", nonce + "");  // armazena o nonce no mapa como uma string
            map.put("hash", hash);  // armazena o hash no mapa. Esse hash é o hash do bloco minerado.

            return map; // retorna o mapa com os dados do bloco minerado
        }else{
            return null; // se o hash calculado não for igual ao hash encontrado, retorna null
        }
    }
}
