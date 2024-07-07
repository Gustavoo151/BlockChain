package com.blockchain.blockchain.agent;

import java.util.ArrayList;
import java.util.List;

/**
 * A classe AgentManager é responsável por gerenciar os agentes na rede blockchain.
 * Permite adicionar, buscar, deletar agentes e criar blocos para agentes específicos.
 */
public class AgentManager {

    private List<Agent> agents = new ArrayList<>();  // Lista de agentes na rede
    private static final Block root = new Genesis();  // Bloco raiz da blockchain

    /**
     * Adiciona um novo agente à rede.
     *
     * @param name Nome do agente
     * @param port Porta na qual o agente será executado
     * @return O agente criado
     */
    public Agent addAgent(String name, int port) {
        Agent agent = new Agent(name, "localhost", port, root, agents);  // Cria um novo agente
        agent.startHost();  // Inicia o servidor do agente
        agent.startMine();  // Inicia a mineração do agente
        agents.add(agent);  // Adiciona o agente à lista de agentes
        return agent;
    }

    /**
     * Retorna um agente específico pelo nome.
     *
     * @param name Nome do agente a ser buscado
     * @return O agente encontrado, ou null se não encontrado
     */
    public Agent getAgent(String name) {
        for (Agent agent : agents) {
            if (agent.getName().equals(name)) {
                return agent;
            }
        }
        return null;
    }

    /**
     * Retorna todos os agentes na rede.
     *
     * @return Lista contendo todos os agentes na rede
     */
    public List<Agent> getAllAgents() {
        return agents;
    }

    /**
     * Deleta um agente da rede pelo nome.
     *
     * @param name Nome do agente a ser deletado
     */
    public void deleteAgent(String name) {
        Agent agent = getAgent(name);
        if (agent != null) {
            agent.stopHost();  // Para o servidor do agente
            agents.remove(agent);  // Remove o agente da lista
        }
    }

    /**
     * Deleta todos os agentes da rede.
     * Este método para todos os servidores antes de remover os agentes da lista.
     */
    public void deleteAllAgents() {
        for (Agent agent : agents) {
            agent.stopHost();  // Para o servidor do agente
        }
        agents.clear();  // Limpa a lista de agentes
    }

    /**
     * Cria um novo bloco para um agente específico na rede.
     *
     * @param name Nome do agente que criará o bloco
     * @return O bloco criado pelo agente, ou null se o agente não foi encontrado
     */
    public Block createBlock(String name) {
        Agent agent = getAgent(name);
        if (agent != null) {
            return agent.createBlock();  // Cria um bloco usando o agente especificado
        }
        return null;
    }
}
