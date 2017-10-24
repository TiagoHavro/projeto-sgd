/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.controle;

import br.com.sistemaM.entidade.Disciplina;
import br.com.sistemaM.enums.NivelAcesso;
import br.com.sistemaM.facade.AbstractFacade;
import br.com.sistemaM.facade.DisciplinaFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
public class DisciplinaControle extends AbstractControle<Disciplina> implements Serializable {

    @Inject
    private DisciplinaFacade disciplinaFacade;
    private Disciplina disciplina;
    @Inject
    private LoginControle loginControle;
    private String codAcesso;

    public DisciplinaControle() {
        super(Disciplina.class);
    }

    @Override
    public AbstractFacade<Disciplina> getFacade() {
        return disciplinaFacade;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
    
    public void addItem() throws Exception {
        disciplina = disciplinaFacade.BuscarDisciplinaPeloCodAcesso(codAcesso);
        super.setEntidade(disciplina);
        super.getEntidade().addItem(disciplina, loginControle.getUsuario());
    }

    @Override
    public List<Disciplina> getListar() throws Exception {
        try {
            if (loginControle.getUsuario().getNivelAcesso().equals(NivelAcesso.PROFESSOR)) {
                return disciplinaFacade.listarProfessor(loginControle.getUsuario().getLogin());
            } else if (loginControle.getUsuario().getNivelAcesso().equals(NivelAcesso.ALUNO)) {
                return disciplinaFacade.listarAluno(loginControle.getUsuario().getLogin());
            }
            return super.getListar();
        } catch (Exception e) {
            e.printStackTrace();
            mensagem("Erro ao buscar do banco", FacesMessage.SEVERITY_FATAL, "");
            return null;
        }
    }

    public String getCodAcesso() {
        return codAcesso;
    }

    public void setCodAcesso(String codAcesso) {
        this.codAcesso = codAcesso;
    }

    @Override
    public String salvar() {
        UUID uuid = UUID.randomUUID();
        String myRandom = uuid.toString().substring(0, 9).toUpperCase();
        Date dataAtual = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String codAcesso = myRandom.concat(sdf.format(dataAtual));
        super.getEntidade().setCodAcesso(codAcesso);
        return super.salvar();
    }

}
