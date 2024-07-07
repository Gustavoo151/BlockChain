package com.blockchain.blockchain.agent;


import java.util.LinkedList;
import java.util.List;

/*
 * Classe que representa a blockchain. Ela é composta por uma lista de blocos e um objeto de bloqueio para garantir a consistência dos dados.
 * A classe fornece métodos para adicionar blocos à blockchain, verificar se a blockchain está vazia, obter o último bloco da blockchain,
 * obter o tamanho da blockchain, obter a lista de blocos da blockchain e definir a lista de blocos da blockchain.
 */
public class BlockChain {

    private List<Block> blocks = new LinkedList<>();  // lista de blocos da blockchain (LinkedList é uma implementação de lista que é mais eficiente para adicionar e remover elementos no início e no final da lista)

    private Object lock = new Object();  // objeto de bloqueio para garantir a consistência dos dados (o objeto de bloqueio é usado para garantir que as operações na lista de blocos sejam atômicas)

    public BlockChain(Block root)
    {
        add(root);  // adiciona o bloco raiz à blockchain
    }

    public void add(Block block){
        synchronized (lock){  // método que adiciona um bloco à blockchain. O synchronized é usado para garantir que a operação seja atômica.
            blocks.add(block);  // adiciona o bloco à lista de blocos
        }
    }

    public void add(LinkedList<Block> blockList){  // método que adiciona uma lista de blocos à blockchain

        synchronized (lock) {

            for (Block block: blockList) {    // percorre a lista de blocos
                boolean find = false;   // verifica se o bloco já está na blockchain

                for(Block old: blocks){  // verifica se o bloco já está na blockchain comparando o hash

                    if(old.getHash().equals(block.getHash())){  // se o bloco já está na blockchain, sai do loop e não adiciona o bloco
                        find = true;
                        break;
                    }
                }

                if (find){  // se o bloco já está na blockchain, não adiciona o bloco
                    continue;
                }
                blocks.add(block);  // adiciona o bloco à lista de blocos
            }
        }
    }

    public boolean isEmpty(){
        synchronized (lock){  // método que verifica se a blockchain está vazia. O synchronized é usado para garantir que a operação seja atômica.
            return blocks.isEmpty();  // verifica se a lista de blocos está vazia
        }
    }

    public Block getLatestBlock(){
        synchronized (lock){
            return blocks.get(blocks.size() - 1);
        }
    }

    public int size(){
        synchronized (lock){  // método que retorna o tamanho da blockchain. O synchronized é usado para garantir que a operação seja atômica.
            return blocks.size();
        }
    }

    public List<Block> getBlocks(){
        synchronized (lock){
            return blocks;
        }
    }

    public void setBlocks(List<Block> blocks){
        synchronized (lock){
            this.blocks = blocks;
        }
    }

    public Object getLock(){
        return lock;
    }

    public void setLock(Object lock){
        this.lock = lock;
    }
}
