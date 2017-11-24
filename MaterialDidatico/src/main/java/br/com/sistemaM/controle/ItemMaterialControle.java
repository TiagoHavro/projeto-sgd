/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.controle;

import br.com.sistemaM.entidade.ItemMaterial;
import br.com.sistemaM.entidade.Material;
import br.com.sistemaM.facade.AbstractFacade;
import br.com.sistemaM.facade.ItemMaterialFacade;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author tiago
 */
@Named
@ViewScoped
public class ItemMaterialControle extends AbstractControle<ItemMaterial> implements Serializable {

    @Inject
    private ItemMaterialFacade itemMaterialFacade;
    private ItemMaterial itemMaterial;

    public ItemMaterialControle() {
        super(ItemMaterial.class);
    }

    public List<ItemMaterial> listarItemMaterial(Material mat) {
        return itemMaterialFacade.listarItemMaterial(mat);
    }

    @Override
    public AbstractFacade<ItemMaterial> getFacade() {
        return itemMaterialFacade;
    }

    public ItemMaterial getItemMaterial() {
        return itemMaterial;
    }

    public void setItemMaterial(ItemMaterial itemMaterial) {
        this.itemMaterial = itemMaterial;
    }

}
