/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.controle;

import br.com.sistemaM.enums.NivelAcesso;
import br.com.sistemaM.entidade.Usuario;
import br.com.sistemaM.facade.AbstractFacade;
import br.com.sistemaM.facade.UsuarioFacade;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author tiago
 */
@Named
@ViewScoped
public class UsuarioControle extends AbstractControle<Usuario> implements Serializable {

    private Usuario usuario;
    @Inject
    private UsuarioFacade usuarioFacade;
    @Inject
    private LoginControle loginControle;

    public UsuarioControle() {
        super(Usuario.class);
    }

    @Override
    public AbstractFacade<Usuario> getFacade() {
        return usuarioFacade;
    }

    @Override
    public List<Usuario> getListar() throws Exception {
        try {
            if (!loginControle.getUsuario().getNivelAcesso().equals(NivelAcesso.MASTER)) {
                return usuarioFacade.listarAlunoProfessor(loginControle.getUsuario().getLogin());
            }
            return super.getListar();
        } catch (Exception e) {
            e.printStackTrace();
            mensagem("Erro ao buscar do banco", FacesMessage.SEVERITY_FATAL, "");
            return null;
        }
    }

    public List<Usuario> autoCompleteProfessor(String nome) {
        return usuarioFacade.autoCompleteProfessor(loginControle.getUsuario());
    }

    public NivelAcesso[] getNiveisAcesso() {
        return NivelAcesso.values();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
