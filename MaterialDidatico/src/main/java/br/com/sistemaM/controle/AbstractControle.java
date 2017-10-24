/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.controle;

import br.com.sistemaM.converter.ConverterGenerico;
import br.com.sistemaM.facade.AbstractFacade;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author tiago
 * @param <T>
 */
public abstract class AbstractControle<T> implements Serializable {

    private Class<T> classe;
    private List<T> listagem;
    private T entidade;
    private Boolean layoutList = true;
    private Boolean layoutForm = false;
    private Boolean layoutView = false;
    private ConverterGenerico converterGenerico;

    public AbstractControle(Class<T> classe) {
        this.classe = classe;
    }

    public abstract AbstractFacade<T> getFacade();

    public void novo() {
        try {
            entidade = classe.newInstance();
            layoutList = false;
            layoutForm = true;
            layoutView = false;
        } catch (InstantiationException | IllegalAccessException ex) {
            mensagem("Erro ao instanciar ", FacesMessage.SEVERITY_FATAL, ex.getMessage());
        }
    }

    public void alterar() {
        layoutList = false;
        layoutForm = true;
        layoutView = false;
    }
    
    public void voltar(){
        entidade = null;
        layoutList = true;
        layoutForm = false;
        layoutView = false;
    }

    public String salvar() {
        try {
            getFacade().salvar(entidade);
            mensagem("Salvo com sucesso ", FacesMessage.SEVERITY_INFO, "");
            return "list?faces-redirect=true";
        } catch (Exception ex) {
            ex.printStackTrace();
            mensagem("Erro ao salvar", FacesMessage.SEVERITY_FATAL, ex.getMessage());
        }
        return null;
    }

    public String excluir() {
        try {
            getFacade().excluir(entidade);
            mensagem("Excluido com sucesso ", FacesMessage.SEVERITY_INFO, "");
            return "list?faces-redirect=true";
        } catch (Exception ex) {
            ex.printStackTrace();
            mensagem("Erro ao excluir", FacesMessage.SEVERITY_FATAL, ex.getMessage());
        }
        return null;
    }

    public void view() {
        layoutList = false;
        layoutForm = false;
        layoutView = true;
    }

    public List<T> getListar() throws Exception {
        if (listagem == null) {
            return getFacade().listar();
        }
        return listagem;
    }

    public List<T> AutoComplete(Long id) {
        return getFacade().AutoComplete(id);
    }
    
    public List<T> AutoCompletePorNome(String nome) {
        return getFacade().AutoCompletePorNome(nome);
    }

    public ConverterGenerico converter() {
        if (converterGenerico == null) {
            converterGenerico = new ConverterGenerico(getFacade());
        }
        return converterGenerico;
    }

    public void onRowEdit(RowEditEvent event) {
        mensagem("Sucesso!", FacesMessage.SEVERITY_WARN, "Celula Editada");
    }
     
    public void onRowCancel(RowEditEvent event) {
        mensagem("Atenção!", FacesMessage.SEVERITY_WARN, "Edit Cancelado");
    }
     
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         
        if(newValue != null && !newValue.equals(oldValue)) {
            mensagem("Celula Editada", FacesMessage.SEVERITY_INFO, "Old: " + oldValue + ", New:" + newValue);
        }
    }
    
    protected void mensagem(String msg, FacesMessage.Severity tipo, String detail) {
        FacesMessage message = new FacesMessage(tipo, msg, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public T getEntidade() {
        return entidade;
    }

    public void setEntidade(T entidade) {
        this.entidade = entidade;
    }

    public Boolean getLayoutList() {
        return layoutList;
    }

    public void setLayoutList(Boolean layoutList) {
        this.layoutList = layoutList;
    }

    public Boolean getLayoutForm() {
        return layoutForm;
    }

    public void setLayoutForm(Boolean layoutForm) {
        this.layoutForm = layoutForm;
    }

    public Boolean getLayoutView() {
        return layoutView;
    }

    public void setLayoutView(Boolean layoutView) {
        this.layoutView = layoutView;
    }

}
