package com.blockchain.blockchain.web;


import com.blockchain.blockchain.agent.Agent;
import com.blockchain.blockchain.agent.AgentManager;
import com.blockchain.blockchain.agent.Block;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="agent")
public class AgentController {

    private static AgentManager agentManager = new AgentManager();

    @RequestMapping(method = RequestMethod.GET)
    public Agent getAgent(@RequestParam("name") String name) {
        return agentManager.getAgent(name);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteAgent(@RequestParam("name") String name) {
        agentManager.deleteAgent(name);
    }

    @RequestMapping(method = RequestMethod.POST, params = {"name", "port"})
    public Agent addAgent(@RequestParam("name") String name, @RequestParam("port") int port) {
        return agentManager.addAgent(name, port);
    }

    @RequestMapping(path = "all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllAgents() throws Exception {
        List<Agent> agents =  agentManager.getAllAgents();
        List<Block> blockChain = null;
        if (agents.size() > 0) {
            blockChain = agents.get(0).getBlocks();
        }

        Map<String, Object> results = new HashMap<>();
        results.put("agents", agents);
        results.put("blocks", blockChain);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(results);
    }

    @RequestMapping(path = "all", method = RequestMethod.DELETE)
    public void deleteAllAgents() {
        agentManager.deleteAllAgents();
    }

    @RequestMapping(method = RequestMethod.POST, path = "mine")
    public Block createBlock(@RequestParam(value = "agent") final String name) {
        return agentManager.createBlock(name);
    }
}
