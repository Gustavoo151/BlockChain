package com.blockchain.blockchain.agent;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Map;
/*
 * Classe que representa um bloco na blockchain.
 * Armazena informações como índice, timestamp, hash, hash do bloco anterior, criador e nonce.
 */
public class Block implements Serializable {

    private static final long serialVersionUID = 1L; // Número de versão da classe

    protected int index; // Índice do bloco na blockchain
    protected Long timestamp; // Timestamp do bloco (data e hora de criação)
    protected String hash; // Hash do bloco (identificador único gerado a partir dos dados do bloco)
    protected String previousHash; // Hash do bloco anterior
    protected String creator; // Identificação do criador do bloco
    protected Integer nonce; // Número usado em mineração para encontrar um hash válido
    protected String data; // Dados armazenados no bloco

    private ProofOfWork proofOfWork; // Objeto responsável pela mineração do bloco

    /*
     * Construtor da classe Block.
     * Inicializa os atributos do bloco e calcula seu hash.
     */
    public Block(int index, String previousHash, String creator) {
        this.index = index;
        this.previousHash = previousHash;
        this.creator = creator;
        this.timestamp = System.currentTimeMillis();
        this.hash = calculateHash(); // Calcula o hash inicial do bloco

        // Inicia o processo de mineração para encontrar um hash válido
        proofOfWork = new ProofOfWork(this);
        Map<String, String> minedBlockData = proofOfWork.run();

        if (minedBlockData != null) {
            this.nonce = Integer.parseInt(minedBlockData.get("nonce"));
            this.hash = minedBlockData.get("hash");

            // Exibe informações sobre o bloco minerado
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("Block mined: " + dateFormat.format(timestamp) + " " + creator + " " + hash);
            System.out.println("Index: " + index);
            System.out.println("Nonce: " + nonce);
            System.out.println("Timestamp: " + timestamp);
            System.out.println("Data: " + data);
            System.out.println("Previous hash: " + previousHash);
            System.out.println("Hash: " + hash);
        }
    }

    public Block() {
    }

    /*
     * Método privado para calcular o hash do bloco com base em seus atributos.
     * É utilizado internamente na inicialização do bloco e após a mineração.
     */
    private String calculateHash() {
        return Utils.hash256(index + previousHash + timestamp);
    }

    // Getters e Setters para os atributos da classe Block

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getNonce() {
        return nonce;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "Block{" +
                "index=" + index +
                ", timestamp=" + timestamp +
                ", creator='" + creator + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        return index == block.index &&
                timestamp.equals(block.timestamp) &&
                hash.equals(block.hash) &&
                previousHash.equals(block.previousHash) &&
                creator.equals(block.creator);
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + timestamp.hashCode();
        result = 31 * result + hash.hashCode();
        result = 31 * result + previousHash.hashCode();
        result = 31 * result + creator.hashCode();
        return result;
    }
}
