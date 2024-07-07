package com.blockchain.blockchain.agent;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/*
 * Classe que representa uma blockchain, que é uma lista encadeada de blocos.
 * Implementa Serializable para permitir a serialização dos objetos.
 */
public class BlockChain implements Serializable {

    private List<Block> blocks = new LinkedList<>(); // Lista de blocos da blockchain
    private Object lock = new Object(); // Objeto de bloqueio para operações sincronizadas

    /*
     * Construtor da classe BlockChain.
     * Inicializa a blockchain com o bloco raiz especificado.
     */
    public BlockChain(Block root) {
        add(root); // Adiciona o bloco raiz à blockchain
    }

    /*
     * Método para adicionar um bloco à blockchain de forma segura.
     * Utiliza sincronização para garantir operações atômicas.
     */
    public void add(Block block) {
        synchronized (lock) {
            blocks.add(block);
        }
    }

    /*
     * Método para adicionar uma lista de blocos à blockchain, evitando duplicatas.
     * Utiliza sincronização para garantir operações atômicas.
     */
    public void add(List<Block> blockList) {
        synchronized (lock) {
            for (Block block : blockList) {
                boolean found = false;
                for (Block old : blocks) {
                    if (old.getHash().equals(block.getHash())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    blocks.add(block);
                }
            }
        }
    }

    /*
     * Verifica se a blockchain está vazia.
     */
    public boolean isEmpty() {
        return blocks.isEmpty();
    }

    /*
     * Obtém o último bloco adicionado à blockchain.
     */
    public Block getLatestBlock() {
        return blocks.get(blocks.size() - 1);
    }

    /*
     * Retorna o tamanho atual da blockchain.
     */
    public int size() {
        return blocks.size();
    }

    /*
     * Retorna a lista de todos os blocos da blockchain.
     */
    public List<Block> getBlocks() {
        return blocks;
    }

    /*
     * Define a lista de blocos da blockchain.
     */
    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    /*
     * Retorna o objeto de bloqueio utilizado para sincronização.
     */
    public Object getLock() {
        return lock;
    }

    /*
     * Define o objeto de bloqueio utilizado para sincronização.
     */
    public void setLock(Object lock) {
        this.lock = lock;
    }
}
