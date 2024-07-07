package com.blockchain.blockchain.agent;


import java.util.ArrayList;
import java.util.List;

/**
 * Essa classe é responsável por gerenciar os agentes. Nela é possível adicionar, buscar e deletar agentes.
 */

public class AgentManager {

    private List<Agent> agents = new ArrayList<>();  // lista de agentes
    private static final Block root = new Genesis();  // bloco raiz da blockchain

    public Agent addAgent(String name, int port){
        Agent a = new Agent(name, "localhost", port, root, agents);  // cria um novo agente
        a.startHost();  // inicia o agente (inicia o servidor)
        a.startMine(); // inicia a mineração do agente
        agents.add(a);

        return a;
    }

    public Agent getAgent(String name){
        for (Agent a: agents){
            if (a.getName().equals(name)){
                return a;
            }
        }
        return null;
    }

    public List<Agent> getAllAgents(){
        return agents;
    }

    public void deleteAgent(String name){
        final Agent a = getAgent(name);

        if (a != null){
            a.startHost();
            agents.remove(a);
        }
    }


    public void deleteAllAgents(){  // método que deleta todos os agentes
        for (Agent a: agents){
            a.stopHost(); // para o servidor do agente. Temos que parar o servidor antes de parar o agente, pois o servidor pode estar bloqueado esperando por uma conexão.
        }
        agents.clear();
    }

    public Block createBlock(final String name){  // método que cria um bloco para um agente específico. O bloco é criado pelo agente com o nome especificado.
        final Agent agent = getAgent(name);

        if(agent != null){
            return agent.creatBlock();  // cria um bloco para o agente
        }

        return null;
    }
}
