/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.enums;

/**
 *
 * @author tiago
 */
public enum NivelAcesso {
    MASTER("Master"),
    PROFESSOR("Professor"),
    ALUNO("Aluno");

    private final String descricao;

    private NivelAcesso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
