/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.facade;

import br.com.sistemaM.entidade.Material;
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
public class MaterialFacade extends AbstractFacade<Material> implements Serializable {

    @Inject
    private EntityManager em;

    public MaterialFacade() {
        super(Material.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }

    public List<Material> listarProfessor(String login) {
        try {
            Query q = em.createQuery("SELECT DISTINCT (m) FROM Material AS m WHERE m.disciplina.usuario.login = '" + login + "'");
            System.out.println("lista: " + q.getResultList().toString());
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Material> listarAluno(String login) {
        try {
            Query q = em.createQuery("SELECT DISTINCT (m) FROM Material AS m, ItemDisciplina AS it "
                    + "WHERE it.disciplina.id = m.disciplina.id AND it.usuario.login = '" + login + "'");
            System.out.println("lista: " + q.getResultList().toString());
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
