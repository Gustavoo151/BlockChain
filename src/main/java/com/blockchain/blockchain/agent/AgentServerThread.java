package com.blockchain.blockchain.agent;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static com.blockchain.blockchain.agent.Message.MESSAGE_TYPE.*;

public class AgentServerThread extends Thread{
    private Socket client;
    private final Agent agent;

    AgentServerThread(final  Agent agent, final Socket client){
        super(agent.getName() + System.currentTimeMillis());
        this.agent = agent;
        this.client = client;
    }

    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                final ObjectInputStream in = new ObjectInputStream(client.getInputStream())) {
            Message message = new Message.MessageBuilder().withSender(agent.getPort()).withType(READY).build();
            out.writeObject(message);
            Object fromClient;
            while ((fromClient = in.readObject()) != null) {
                if (fromClient instanceof Message) {
                    final Message msg = (Message) fromClient;
                    System.out.println(String.format("%d received: %s", agent.getPort(), fromClient.toString()));
                    if (INFO_NEW_BLOCK == msg.type) {
                        if (msg.blocks.isEmpty() || msg.blocks.size() > 1) {
                            System.err.println("Invalid block received: " + msg.blocks);
                        }
                        synchronized (agent) {
                            agent.addBlock(msg.blocks.get(0));
                        }
                        break;
                    } else if (REQ_ALL_BLOCKS == msg.type) {
                        out.writeObject(new Message.MessageBuilder()
                                .withSender(agent.getPort())
                                .withType(RSP_ALL_BLOCKS)
                                .withBlocks(agent.getBlocks())
                                .build());
                        break;
                    }
                }
            }
            client.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
