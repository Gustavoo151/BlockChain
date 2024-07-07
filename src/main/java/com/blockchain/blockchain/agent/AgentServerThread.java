package com.blockchain.blockchain.agent;/*
 * Classe que representa uma thread do servidor para comunicação com agentes na blockchain.
 * Responsável por receber mensagens dos clientes, processá-las e responder conforme o tipo de mensagem recebida.
 */

import com.blockchain.blockchain.agent.Agent;
import com.blockchain.blockchain.agent.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static com.blockchain.blockchain.agent.Message.MESSAGE_TYPE.*;

public class AgentServerThread extends Thread {
    private Socket client; // Socket para comunicação com o cliente
    private final Agent agent; // Agente associado a esta thread

    /*
     * Construtor da classe AgentServerThread.
     * Inicializa a thread com o agente e o socket do cliente.
     */
    AgentServerThread(final Agent agent, final Socket client) {
        super(agent.getName() + System.currentTimeMillis()); // Define um nome único para a thread
        this.agent = agent;
        this.client = client;
    }

    /*
     * Método principal da thread, responsável por executar a lógica de comunicação com o cliente.
     */
    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                final ObjectInputStream in = new ObjectInputStream(client.getInputStream())) {

            // Envia uma mensagem de prontidão ao cliente
            Message message = new Message.MessageBuilder().withSender(agent.getPort()).withType(READY).build();
            out.writeObject(message);

            Object fromClient;
            while ((fromClient = in.readObject()) != null) {
                if (fromClient instanceof Message) {
                    final Message msg = (Message) fromClient;
                    System.out.println(String.format("%d received: %s", agent.getPort(), fromClient.toString()));

                    // Processa a mensagem com base no tipo recebido
                    if (INFO_NEW_BLOCK == msg.type) {
                        // Verifica se a mensagem contém blocos válidos e adiciona ao agente
                        if (msg.blocks.isEmpty() || msg.blocks.size() > 1) {
                            System.err.println("Invalid block received: " + msg.blocks);
                        }
                        synchronized (agent) {
                            agent.addBlock(msg.blocks.get(0));
                        }
                        break; // Finaliza a thread após adicionar o bloco
                    } else if (REQ_ALL_BLOCKS == msg.type) {
                        // Envia todos os blocos do agente em resposta à solicitação
                        out.writeObject(new Message.MessageBuilder()
                                .withSender(agent.getPort())
                                .withType(RSP_ALL_BLOCKS)
                                .withBlocks(agent.getBlocks())
                                .build());
                        break; // Finaliza a thread após enviar os blocos
                    }
                }
            }
            client.close(); // Fecha o socket após a comunicação
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace(); // Trata exceções de leitura, escrita ou classe não encontrada
        }
    }
}
