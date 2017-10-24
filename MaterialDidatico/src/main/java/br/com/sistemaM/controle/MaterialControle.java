/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.controle;

import br.com.sistemaM.entidade.Disciplina;
import br.com.sistemaM.entidade.Material;
import br.com.sistemaM.enums.NivelAcesso;
import br.com.sistemaM.facade.AbstractFacade;
import br.com.sistemaM.facade.DisciplinaFacade;
import br.com.sistemaM.facade.MaterialFacade;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import java.util.Date;
import org.primefaces.model.TreeNode;

/**
 *
 * @author tiago
 */
@Named
@ViewScoped
public class MaterialControle extends AbstractControle<Material> implements Serializable {

    @Inject
    private MaterialFacade materialFacade;
    @Inject
    private LoginControle loginControle;
    private Material material;
    private String extensao;
    private String nomeArq;
    private Date dataCadastro;
    private Disciplina disciplina;
    private String nome;
    private TreeNode root;

    public MaterialControle() {
        super(Material.class);
    }

    @Override
    public AbstractFacade<Material> getFacade() {
        return materialFacade;
    }

    @Override
    public List<Material> getListar() throws Exception {
        if(loginControle.getUsuario().getNivelAcesso().equals(NivelAcesso.PROFESSOR)){
            return materialFacade.listarProfessor(loginControle.getUsuario().getLogin());
        } else if(loginControle.getUsuario().getNivelAcesso().equals(NivelAcesso.ALUNO)){
            return materialFacade.listarAluno(loginControle.getUsuario().getLogin());
        }
        return super.getListar();
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    
    public String getExtensao() {
        return extensao;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }

    public String getNomeArq() {
        return nomeArq;
    }

    public void setNomeArq(String nomeArq) {
        this.nomeArq = nomeArq;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        try {
            UploadedFile arq = event.getFile();
            InputStream in = new BufferedInputStream(arq.getInputstream());
            String caminho = "D:\\trabalho e estudo\\MaterialDidatico\\src\\main\\webapp\\imagens\\Upload\\" + arq.getFileName();
            File fileAnexo = new File(caminho);
            fileAnexo.getParentFile().mkdirs();
            extensao = arq.getFileName().substring(arq.getFileName().lastIndexOf("."), arq.getFileName().length());
            nomeArq = arq.getFileName();
            FileOutputStream fout = new FileOutputStream(caminho);
            while (in.available() != 0) {
                fout.write(in.read());
            }
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String salvar() {
        try {
             
            Material m = new Material();
            m.setNome(nome);
            m.setNomearq(nomeArq);
            m.setFormato(extensao);
            m.setDataCadastro(new Date());
            m.setDisciplina(disciplina);
            materialFacade.salvar(m);
            mensagem("Salvo com sucesso: ", FacesMessage.SEVERITY_INFO, "");
            voltar();

        } catch (Exception ex) {
            ex.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;

    }
}
