package com.blockchain.blockchain.agent;

/*
 * Classe que representa uma mensagem serializável trocada entre agentes em uma blockchain.
 * A mensagem pode conter informações como remetente, destinatário, tipo e blocos associados.
 */
import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    int sender;  // Remetente da mensagem
    int receiver;  // Destinatário da mensagem
    MESSAGE_TYPE type;  // Tipo da mensagem (enumeração)
    List<Block> blocks;  // Lista de blocos associados à mensagem

    /*
     * Enumeração que define os tipos de mensagens suportados.
     */
    public enum MESSAGE_TYPE {
        NEW_NODE,  // Nova conexão (nó)
        READY,  // Pronto para comunicação
        INFO_NEW_BLOCK,  // Informação sobre novo bloco adicionado
        REQ_ALL_BLOCKS,  // Requisição de todos os blocos
        RSP_ALL_BLOCKS  // Resposta com todos os blocos
    }

    /*
     * Sobrescrita do método toString para exibir informações detalhadas sobre a mensagem.
     */
    @Override
    public String toString() {
        return String.format("Message {type=%s, sender=%d, receiver=%d, blocks=%s}", type, sender, receiver, blocks);
    }

    /*
     * Classe interna estática MessageBuilder para construção de objetos Message de forma fluente.
     */
    static class MessageBuilder {
        private final Message message = new Message();  // Objeto Message sendo construído

        /*
         * Define o remetente da mensagem.
         */
        MessageBuilder withSender(final int sender) {
            message.sender = sender;
            return this;
        }

        /*
         * Define o destinatário da mensagem.
         */
        MessageBuilder withReceiver(final int receiver) {
            message.receiver = receiver;
            return this;
        }

        /*
         * Define o tipo da mensagem.
         */
        MessageBuilder withType(final MESSAGE_TYPE type) {
            message.type = type;
            return this;
        }

        /*
         * Define os blocos associados à mensagem.
         */
        MessageBuilder withBlocks(final List<Block> blocks) {
            message.blocks = blocks;
            return this;
        }

        /*
         * Constrói e retorna o objeto Message configurado.
         */
        Message build() {
            return message;
        }
    }
}
