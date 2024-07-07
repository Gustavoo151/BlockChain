package com.blockchain.blockchain.agent;

/*
 * Classe que representa o bloco inicial (Genesis block) de uma blockchain.
 * Herda da classe Block e define as propriedades específicas do bloco Genesis.
 */
public class Genesis extends Block {

    /*
     * Construtor da classe Genesis.
     * Inicializa as propriedades do bloco Genesis com valores padrão fixos.
     */
    public Genesis() {
        this.index = 0;  // Índice inicial da blockchain
        this.previousHash = "0000000000000000000000000000000000000000000000000000000000000000";  // Hash anterior vazio
        this.creator = "ROOT";  // Criador do bloco Genesis
        this.timestamp = 0L;  // Timestamp inicial
        this.nonce = 0;  // Nonce inicial
        this.hash = Utils.hash256("Genesis");  // Calcula o hash do bloco Genesis com base no seu nome
    }
}
