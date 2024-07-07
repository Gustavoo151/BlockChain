package com.blockchain.blockchain.agent;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static com.blockchain.blockchain.agent.Message.MESSAGE_TYPE.*;

/**
 * Classe que representa um agente na rede blockchain.
 * É responsável por manter a conexão com outros agentes e
 * gerenciar a blockchain.
 */
public class Agent {

    private String id;
    private String name;
    private String address;
    private int port;
    private List<Agent> peers;
    private ServerSocket serverSocket;
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
    private boolean listening = true;
    private BlockChain blockChain;

    // Fábrica de threads para sincronização
    private final ThreadFactory factory = new ThreadFactory() {
        private AtomicInteger cnt = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "sync-" + cnt.getAndIncrement());
        }
    };

    // Construtor vazio para uso com Jackson
    public Agent() {
        id = UUID.randomUUID().toString();
    }

    // Construtor principal para inicialização com parâmetros
    Agent(String name, String address, int port, Block root, List<Agent> agents) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.port = port;
        this.peers = agents;
        blockChain = new BlockChain(root);
    }

    /**
     * Cria um novo bloco na blockchain se válido.
     * @return O bloco criado, ou null se não pôde criar.
     */
    Block createBlock() {
        if (blockChain.isEmpty()) {
            return null;
        }
        Block previousBlock = getLatestBlock();
        if (previousBlock == null) {
            return null;
        }
        int index = previousBlock.getIndex() + 1;
        Block block = new Block(index, previousBlock.getHash(), name);
        System.out.println(String.format("%s criou um novo bloco %s", name, block.toString()));
        broadcast(INFO_NEW_BLOCK, block);
        addBlock(block);
        return block;
    }

    /**
     * Adiciona um bloco à blockchain se válido.
     * @param block O bloco a ser adicionado.
     */
    void addBlock(Block block) {
        if (isBlockValid(block)) {
            blockChain.add(block);
        }
    }

    /**
     * Inicia o servidor para aceitar conexões de outros agentes.
     */
    void startHost() {
        executor.execute(() -> {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println(String.format("Servidor %s iniciado", serverSocket.getLocalPort()));
                listening = true;
                while (listening) {
                    AgentServerThread thread = new AgentServerThread(Agent.this, serverSocket.accept());
                    thread.start();
                }
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Não foi possível escutar na porta " + port);
            }
        });
        broadcast(REQ_ALL_BLOCKS, null);
    }

    /**
     * Para o servidor de aceitação de conexões.
     */
    void stopHost() {
        listening = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicia o processo de mineração de novos blocos periodicamente.
     */
    void startMine() {
        executor.execute(() -> {
            try {
                while (true) {
                    createBlock();
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Retorna a lista de blocos da blockchain deste agente.
     * @return Lista de blocos.
     */
    @JsonIgnore
    public List<Block> getBlocks() {
        return blockChain.getBlocks();
    }

    // Métodos privados auxiliares

    private Block getLatestBlock() {
        if (blockChain.isEmpty()) {
            return null;
        }
        return blockChain.getLatestBlock();
    }

    private boolean isBlockValid(Block block) {
        Block latestBlock = getLatestBlock();
        if (latestBlock == null) {
            return false;
        }
        int expected = latestBlock.getIndex() + 1;
        if (block.getIndex() != expected) {
            System.out.println(String.format("Índice inválido. Esperado: %s Atual: %s", expected, block.getIndex()));
            return false;
        }
        if (!Objects.equals(block.getPreviousHash(), latestBlock.getHash())) {
            System.out.println("Código hash não corresponde");
            return false;
        }
        return true;
    }

    private void broadcast(Message.MESSAGE_TYPE type, Block block) {
        peers.forEach(peer -> sendMessage(peer, type, peer.getAddress(), peer.getPort(), block));
    }

    private void sendMessage(Agent agent, Message.MESSAGE_TYPE type, String host, int port, Block... blocks) {
        if (agent.getId().equals(this.id)) {
            return;
        }
        try (Socket peer = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(peer.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(peer.getInputStream())) {

            Object fromPeer;
            while ((fromPeer = in.readObject()) != null) {
                if (fromPeer instanceof Message) {
                    Message msg = (Message) fromPeer;
                    System.out.println(String.format("%d recebeu: %s", this.port, msg.toString()));
                    if (READY == msg.type) {
                        out.writeObject(new Message.MessageBuilder()
                                .withType(type)
                                .withReceiver(port)
                                .withSender(this.port)
                                .withBlocks(Arrays.asList(blocks)).build());
                    } else if (RSP_ALL_BLOCKS == msg.type) {
                        if (!msg.blocks.isEmpty() && this.blockChain.size() == 1) {
                            blockChain.add((Block) msg.blocks);   // Pode causar erro
                        }
                        break;
                    }
                }
            }
        } catch (UnknownHostException e) {
            System.err.println(String.format("Host desconhecido %s %d", host, port));
        } catch (IOException e) {
            System.err.println(String.format("%s não conseguiu I/O para a conexão com %s. Tentando novamente...%n", getPort(), port));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Getters e Setters

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
