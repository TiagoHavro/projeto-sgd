/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.facade;

import br.com.sistemaM.entidade.ItemMaterial;
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
public class ItemMaterialFacade extends AbstractFacade<ItemMaterial> implements Serializable {

    @Inject
    private EntityManager em;

    public ItemMaterialFacade() {
        super(ItemMaterial.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }

    public List<ItemMaterial> listarItemMaterial(Material mat) {
        Query q = em.createQuery("FROM ItemMaterial AS im WHERE im.material.id = '" + mat.getId() + "'");
        return q.getResultList();
    }

}
