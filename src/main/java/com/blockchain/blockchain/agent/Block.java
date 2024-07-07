package com.blockchain.blockchain.agent;


/*
 * Classe que representa um bloco da blockchain. Nele são armazenadas informações como índice, timestamp, hash, hash do bloco anterior, criador e nonce.
 */

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Map;

public class Block implements Serializable { // classe que representa um bloco da blockchain e implementa a interface Serializable para que os objetos possam ser serializados e desserializados

    private static final long serialVersionUID = 1L;  // número de versão da classe

    protected int index; // índice do bloco na blockchain
    protected Long timestamp;  // timestamp do bloco (data e hora em que o bloco foi criado)
    protected String hash;  // hash do bloco (hash dos dados do bloco)
    protected String previousHash;  // hash do bloco anterior (hash dos dados do bloco anterior)
    protected String creator;  // criador do bloco (endereço do agente que criou o bloco)

    protected Integer nonce;  // nonce do bloco (número que é incrementado até que o hash do bloco seja menor que o alvo da mineração)

    protected String data;  // dados do bloco

    private ProofOfWork proofOfWork;  // objeto que realiza a mineração do bloco (ProofOfWork é a classe responsável por realizar a mineração de um bloco)

    public Block(){}

    @Override
    public String toString() {
        return "Block{" +
                "index=" + index +
                ", timestamp=" + timestamp +
                ", creator='" + creator + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o){  // método que verifica se dois objetos são iguais.
        if (this == o) return true;  // verifica se os objetos são o mesmo objeto

        if (o == null || getClass() != o.getClass()) return false;  // verifica se o objeto é nulo ou se é de uma classe diferente

        final Block block = (Block) o;  // converte o objeto para a classe Block

        return index == block.index  // verifica se os campos dos objetos são iguais
                && timestamp.equals(block.timestamp)
                && hash.equals(block.hash)
                && previousHash.equals(block.previousHash)
                && creator.equals(block.creator);
    }

    @Override
    public int hashCode(){  // método que gera um código hash para o objeto com base nos seus campos
        int result = index;  // inicializa o resultado com o índice do bloco
        result = 31 * result + timestamp.hashCode();    // multiplica o resultado por 31 e adiciona o hash do timestamp o 31 serve para evitar colisões
        result = 31 * result + hash.hashCode();
        result = 31 * result + previousHash.hashCode();
        result = 31 * result + creator.hashCode();
        return result;
    }


    public Block(int index, String preHash, String creator){
        this.index = index;
        this.previousHash = preHash;
        this.creator = creator;
        this.timestamp = System.currentTimeMillis();
        hash = Utils.hash256(String.valueOf(index) + previousHash + String.valueOf(timestamp));  // calcula o hash do bloco com base no índice, hash do bloco anterior e timestamp

        proofOfWork = new ProofOfWork(this);  // inicializa o objeto proofOfWork com o bloco atual para realizar a mineração
        Map<String, String> map = proofOfWork.run();  // realiza a mineração do bloco e armazena os dados do bloco minerado em um mapa

        if(map != null){  // verifica se o mapa não é nulo
            nonce = Integer.parseInt(map.get("nonce"));  // armazena o nonce do bloco minerado
            hash = map.get("hash");  // armazena o hash do bloco minerado no bloco atual

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // inicializa um objeto SimpleDateFormat para formatar a data e hora

            System.out.println("Block mined: " + df.format(timestamp) + " " + creator + " " + hash); // imprime a data e hora, o criador e o hash do bloco minerado
            System.out.println("Index: " + index);
            System.out.println("Nonce: " + nonce);
            System.out.println("Timestamp: " + this.timestamp);
            System.out.println("Data: " + this.data);
            System.out.println("Previous hash: " + this.previousHash);
            System.out.println("Hash: " + this.hash);
        }
    }

    public String getCreator() {
        return creator;
    }

    public int getIndex() {
        return index;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getNonce() {
        return nonce;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }
}
