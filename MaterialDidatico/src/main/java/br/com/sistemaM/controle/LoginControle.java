/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.controle;

import br.com.sistemaM.memoria.Mensagem;
import br.com.sistemaM.entidade.Usuario;
import br.com.sistemaM.enums.NivelAcesso;
import br.com.sistemaM.facade.UsuarioFacade;
import br.com.sistemaM.utils.EmailUtils;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.mail.EmailException;

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
    private Mensagem mensagem = new Mensagem();
    private String codEmail;
    private String codEmailDigitado;
    private boolean layoutRecuperarSenha = false;
    private boolean CodEmailIgualCodEmailDigitado = false;

    public Mensagem getMensagem() {
        return mensagem;
    }

    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }

    public void enviaEmail() {
        try {
            mensagem.setTitulo("SGD Nova Senha");
            usuario = usuarioFacade.pesquisaUsuarioPorEmail(mensagem.getDestino());
            if (usuario != null) {
                UUID uuid = UUID.randomUUID();
                String myRandom = uuid.toString().substring(0, 9).toUpperCase();
                Date dataAtual = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                codEmail = myRandom.concat(sdf.format(dataAtual));
                mensagem.setMensagem("Foi Gerado uma nova senha para o acesso " + codEmail);
                EmailUtils.enviaEmail(mensagem);
                layoutRecuperarSenha = true;
            }
            limpaMensagem();
        } catch (EmailException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro! Occoreu um erro ao enviar a mensagem.", ""));
        }
    }

    public void limpaMensagem() {
        mensagem = new Mensagem();
    }

    public void limparContato() {
        layoutRecuperarSenha = false;
        CodEmailIgualCodEmailDigitado = false;
    }

    public String contato() {
        return "contato.xhtml";
    }

    public void compararCodEmail() {
        if (codEmail != null && codEmailDigitado != null) {
            if (codEmail.equals(codEmailDigitado)) {
                CodEmailIgualCodEmailDigitado = true;
            }
        }
    }

    public boolean isCodEmailIgualCodEmailDigitado() {
        return CodEmailIgualCodEmailDigitado;
    }

    public void setCodEmailIgualCodEmailDigitado(boolean CodEmailIgualCodEmailDigitado) {
        this.CodEmailIgualCodEmailDigitado = CodEmailIgualCodEmailDigitado;
    }

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

    public String getCodEmail() {
        return codEmail;
    }

    public void setCodEmail(String codEmail) {
        this.codEmail = codEmail;
    }

    public boolean isLayoutRecuperSenha() {
        return layoutRecuperarSenha;
    }

    public void setLayoutRecuperSenha(boolean layoutRecuperSenha) {
        this.layoutRecuperarSenha = layoutRecuperSenha;
    }

    public String getCodEmailDigitado() {
        return codEmailDigitado;
    }

    public void setCodEmailDigitado(String codEmailDigitado) {
        this.codEmailDigitado = codEmailDigitado;
    }

}
