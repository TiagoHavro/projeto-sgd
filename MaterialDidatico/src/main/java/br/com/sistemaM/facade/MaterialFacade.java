/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.facade;

import br.com.sistemaM.entidade.ItemMaterial;
import br.com.sistemaM.entidade.Material;
import br.com.sistemaM.persistencia.Transacional;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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
    @Inject
    private ItemMaterialFacade itemMaterialFacade;

    public MaterialFacade() {
        super(Material.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }

    @Override
    public void excluir(Material entidade) throws Exception {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        try {
            for (ItemMaterial it : entidade.getItensMaterial()) {
                String caminho = ec.getRealPath("").replace("\\target\\MaterialDidatico-1.0-SNAPSHOT\\", "\\src\\main\\webapp\\arquivos\\") + entidade.getId() + "\\" + it.getNomearq();
                File arquivo = new File(caminho);
                arquivo.delete();
            }
            getEm().remove(getEm().merge(entidade));
            getEm().flush();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<Material> listarProfessor(String login) {
        try {
            Query q = em.createQuery("SELECT DISTINCT (m) FROM Material AS m WHERE m.disciplina.usuario.login = '" + login + "'");
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
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
