/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.facade;

import br.com.sistemaM.entidade.Disciplina;
import br.com.sistemaM.persistencia.Transacional;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author tiago
 */
@Transacional
public class DisciplinaFacade extends AbstractFacade<Disciplina> implements Serializable {

    @Inject
    private EntityManager em;

    public DisciplinaFacade() {
        super(Disciplina.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }

    public List<Disciplina> listarProfessor(String login) {
        try {
            Query q = em.createQuery("SELECT DISTINCT (d) FROM Disciplina AS d WHERE d.usuario.login = '" + login + "'");
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Disciplina> listarAluno(String login) {
        try {
            Query q = em.createQuery("SELECT DISTINCT (id.disciplina) FROM ItemDisciplina AS id WHERE id.usuario.login = '" + login + "'");
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
