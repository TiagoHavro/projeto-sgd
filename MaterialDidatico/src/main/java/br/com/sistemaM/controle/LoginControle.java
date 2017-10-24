/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.controle;

import br.com.sistemaM.entidade.Usuario;
import br.com.sistemaM.enums.NivelAcesso;
import br.com.sistemaM.facade.UsuarioFacade;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author tiago
 */
@Named(value = "loginControle")
@SessionScoped
public class LoginControle implements Serializable {

    @Inject
    private UsuarioFacade usuarioFacade;
    private Usuario usuario;
    private String login;
    private String senha;
    private Boolean logado = false;

    public String logar() {
        usuario = usuarioFacade.pesquisaUsuario(login, senha);
        if (usuario != null) {
            logado = true;
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bem-Vindo ao Sistema", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "/index";
        } else {
            logado = false;
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuário não encontrado no sistema", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;
    }

    public String logoff() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Saindo do Sistema", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "/login?faces-redirect=true";
    }

    public String cadastroUsuario() {
        usuario = new Usuario();
        usuario.setNivelAcesso(NivelAcesso.ALUNO);
        return "usuario.xhtml";
    }

    public String salvar() {
        try {
            usuarioFacade.salvar(usuario);
            logado = true;
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bem-Vindo ao Sistema", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "/index";
        } catch (Exception ex) {
            ex.printStackTrace();
            logado = false;
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao salvar Usuário no sistema", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;
    }

    public Boolean getLogado() {
        return logado;
    }

    public void setLogado(Boolean logado) {
        this.logado = logado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
