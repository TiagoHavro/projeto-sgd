/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.facade;

import br.com.sistemaM.entidade.Disciplina;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author tiago
 * @param <T>
 */
public abstract class AbstractFacade<T> implements Serializable {

    private Class<T> classe;

    public AbstractFacade(Class<T> classe) {
        this.classe = classe;
    }

    protected abstract EntityManager getEm();

    public T salvar(T entidade) throws Exception {
        try {
            entidade = getEm().merge(entidade);
            getEm().flush();
            return entidade;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void excluir(T entidade) throws Exception {
        try {
            getEm().remove(getEm().merge(entidade));
            getEm().flush();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public T pesquisarId(Long id) {
        return getEm().find(classe, id);
    }

    public List<T> listar() {
        return getEm().createQuery("FROM " + classe.getSimpleName() + " ORDER BY id").getResultList();
    }

    public List<T> AutoComplete(Long id) {
        Query q = getEm().createQuery("FROM " + classe.getSimpleName() + " AS t WHERE t.id = '" + id + "'");
        return q.getResultList();
    }
    
    public List<T> AutoCompletePorNome(String nome) {
        Query q = getEm().createQuery("FROM " + classe.getSimpleName() + " AS t WHERE t.nome LIKE ('%" + nome + "%')");
        return q.getResultList();
    }
    
    public Disciplina BuscarDisciplinaPeloCodAcesso(String codAcesso) {
        Query q = getEm().createQuery("FROM Disciplina AS d WHERE d.codAcesso = '" + codAcesso + "'");
        return (Disciplina) q.getSingleResult();
    }

}
