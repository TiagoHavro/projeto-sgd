/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.controle;

import br.com.sistemaM.entidade.Curso;
import br.com.sistemaM.entidade.ItemDisciplina;
import br.com.sistemaM.enums.NivelAcesso;
import br.com.sistemaM.facade.AbstractFacade;
import br.com.sistemaM.facade.CursoFacade;
import br.com.sistemaM.facade.ItemDisciplinaFacade;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author tiago
 */
@Named
@ViewScoped
public class ItemDisciplinaControle extends AbstractControle<ItemDisciplina> implements Serializable {

    @Inject
    private ItemDisciplinaFacade itemDisciplinaFacade;
    private ItemDisciplina itemDisciplina;
    @Inject
    private LoginControle loginControle;

    public ItemDisciplinaControle() {
        super(ItemDisciplina.class);
    }

    @Override
    public AbstractFacade<ItemDisciplina> getFacade() {
        return itemDisciplinaFacade;
    }

    @Override
    public List<ItemDisciplina> getListar() throws Exception {
        try {
            if (loginControle.getUsuario().getNivelAcesso().equals(NivelAcesso.PROFESSOR)) {
                return itemDisciplinaFacade.listarProfessor(loginControle.getUsuario().getLogin());
            } else if (loginControle.getUsuario().getNivelAcesso().equals(NivelAcesso.ALUNO)) {
                return itemDisciplinaFacade.listarAluno(loginControle.getUsuario().getLogin());
            }
            return super.getListar();
        } catch (Exception e) {
            e.printStackTrace();
            mensagem("Erro ao buscar do banco", FacesMessage.SEVERITY_FATAL, "");
            return null;
        }
    }

    public ItemDisciplina getItemDisciplina() {
        return itemDisciplina;
    }

    public void setItemDisciplina(ItemDisciplina itemDisciplina) {
        this.itemDisciplina = itemDisciplina;
    }

}
