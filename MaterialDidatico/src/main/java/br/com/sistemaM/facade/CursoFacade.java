/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.facade;

import br.com.sistemaM.entidade.Curso;
import br.com.sistemaM.persistencia.Transacional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author tiago
 */
@Transacional
public class CursoFacade extends AbstractFacade<Curso> implements Serializable {

    @Inject
    private EntityManager em;

    public CursoFacade() {
        super(Curso.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }

    public List<Curso> listarProfessor(String login) {
        try {
            Query q = em.createQuery("SELECT DISTINCT (d.curso) FROM Disciplina AS d WHERE d.usuario.login = '" + login + "'");
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
