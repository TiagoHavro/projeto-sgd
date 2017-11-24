/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.facade;

import br.com.sistemaM.entidade.Usuario;
import br.com.sistemaM.enums.NivelAcesso;
import java.io.Serializable;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import br.com.sistemaM.persistencia.Transacional;
import br.com.sistemaM.utils.Criptografia;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author tiago
 */
@Transacional
public class UsuarioFacade extends AbstractFacade<Usuario> implements Serializable {

    @Inject
    private EntityManager em;

    public UsuarioFacade() {
        super(Usuario.class);
    }

    @Override
    protected EntityManager getEm() {
        return em;
    }

    public Usuario pesquisaUsuario(String login, String senha) {
        Query q = em.createQuery("FROM Usuario AS u WHERE u.login='" + login + "' AND u.senha='" + Criptografia.md5(senha) + "'");
        if (q.getResultList().size() == 1) {
            return (Usuario) q.getSingleResult();
        }
        return null;
    }

    public Usuario pesquisaUsuarioPorEmail(String email) {
        Query q = em.createQuery("FROM Usuario AS u WHERE u.email='" + email + "'");
        if (q.getResultList().size() == 1) {
            return (Usuario) q.getSingleResult();
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuário não encontrado no sistema", email);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }
    }

    public List<Usuario> listarAlunoProfessor(String login) {
        Query q = em.createQuery("FROM Usuario AS u WHERE u.login='" + login + "'");
        return q.getResultList();
    }

    public List<Usuario> autoCompleteProfessor(Usuario u) {
        if (u.getNivelAcesso().equals(NivelAcesso.MASTER)) {
            Query q = em.createQuery("FROM Usuario AS u WHERE u.nivelAcesso='" + NivelAcesso.PROFESSOR + "'");
            return q.getResultList();
        } else {
            Query q = em.createQuery("FROM Usuario AS u WHERE u.login='" + u.getLogin() + "' AND u.nivelAcesso='" + NivelAcesso.PROFESSOR + "'");
            return q.getResultList();
        }
    }

}
