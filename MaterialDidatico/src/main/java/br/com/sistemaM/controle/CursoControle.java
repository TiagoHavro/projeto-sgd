/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.controle;

import br.com.sistemaM.entidade.Curso;
import br.com.sistemaM.enums.NivelAcesso;
import br.com.sistemaM.facade.AbstractFacade;
import br.com.sistemaM.facade.CursoFacade;
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
public class CursoControle extends AbstractControle<Curso> implements Serializable {

    @Inject
    private CursoFacade cursoFacade;
    private Curso curso;
    @Inject
    private LoginControle loginControle;

    public CursoControle() {
        super(Curso.class);
    }

    @Override
    public AbstractFacade<Curso> getFacade() {
        return cursoFacade;
    }

    @Override
    public String salvar() {
        try {
            cursoFacade.salvar(getEntidade());
            mensagem("Salvo com sucesso ", FacesMessage.SEVERITY_INFO, "");
            return "/app/disciplina/list.xhtml";
        } catch (Exception ex) {
            ex.printStackTrace();
            mensagem("Erro ao salvar", FacesMessage.SEVERITY_FATAL, ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Curso> getListar() throws Exception {
        try {
            if (loginControle.getUsuario().getNivelAcesso().equals(NivelAcesso.PROFESSOR)) {
                return cursoFacade.listarProfessor(loginControle.getUsuario().getLogin());
            }
            return super.getListar();
        } catch (Exception e) {
            e.printStackTrace();
            mensagem("Erro ao buscar do banco", FacesMessage.SEVERITY_FATAL, "");
            return null;
        }
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
