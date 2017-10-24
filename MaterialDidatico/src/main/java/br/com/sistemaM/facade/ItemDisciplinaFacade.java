/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.facade;

import br.com.sistemaM.entidade.ItemDisciplina;
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
public class ItemDisciplinaFacade extends AbstractFacade<ItemDisciplina> implements Serializable{
    
    @Inject
    private EntityManager em;

    public ItemDisciplinaFacade() {
        super(ItemDisciplina.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }
    
    public List<ItemDisciplina> listarProfessor(String login) {
        Query q = em.createQuery("FROM ItemDisciplina AS id WHERE id.usuario.login = '" + login + "'");
//        Query q = em.createQuery("FROM Disciplina AS d INNER JOIN d.curso AS c WHERE d.usuario.login = '" + login + "'");
        return q.getResultList();
    }
    
    public List<ItemDisciplina> listarAluno(String login) {
        Query q = em.createQuery("FROM ItemDisciplina AS id WHERE id.usuario.login = '" + login + "'");
//        Query q = em.createQuery("FROM Disciplina AS d INNER JOIN d.curso AS c INNER JOIN d.itensDisciplina AS i WHERE i.usuario.login = '" + login + "'");
        return q.getResultList();
    }
      
}
