/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.controle;

import br.com.sistemaM.entidade.Disciplina;
import br.com.sistemaM.entidade.ItemMaterial;
import br.com.sistemaM.entidade.Material;
import br.com.sistemaM.facade.AbstractFacade;
import br.com.sistemaM.facade.ItemMaterialFacade;
import br.com.sistemaM.facade.MaterialFacade;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import java.util.Date;
import javax.faces.context.ExternalContext;

/**
 *
 * @author tiago
 */
@Named
@ViewScoped
public class MaterialControle extends AbstractControle<Material> implements Serializable {

    @Inject
    private MaterialFacade materialFacade;
    private String extensao;
    private String nomeArq;
    private boolean layoutDownload = false;

    public MaterialControle() {
        super(Material.class);
    }

    @Override
    public AbstractFacade<Material> getFacade() {
        return materialFacade;
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

    public boolean isLayoutDownload() {
        return layoutDownload;
    }

    public void setLayoutDownload(boolean layoutDownload) {
        this.layoutDownload = layoutDownload;
    }

    public void abrirDownload() {
        layoutDownload = true;
        super.setLayoutList(false);
    }

    public void baixarMaterial(ItemMaterial item) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String caminho = ec.getRealPath("").replace("\\target\\MaterialDidatico-1.0-SNAPSHOT\\", "\\src\\main\\webapp\\arquivos\\") 
                    + item.getId() + "\\" + item.getNomearq();
            File arquivo = new File(caminho);
            if (!arquivo.exists()) {
                mensagem("Arquivo não encontrado: ", FacesMessage.SEVERITY_FATAL, item.getNomearq());
            }
            ec.responseReset();
            ec.setResponseContentType(item.getFormato());
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + item.getNomearq() + "\"");
            OutputStream output = ec.getResponseOutputStream();
            InputStream stream = new DataInputStream(new FileInputStream(arquivo));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = stream.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }
            output.flush();
            fc.responseComplete();
            mensagem("Sucesso ao fazer download do arquivo: ", FacesMessage.SEVERITY_INFO, item.getNomearq());
        } catch (IOException e) {
            e.printStackTrace();
            mensagem("Erro ao fazer download do arquivo: ", FacesMessage.SEVERITY_FATAL, item.getNomearq());
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            UploadedFile arq = event.getFile();
            InputStream in = new BufferedInputStream(arq.getInputstream());
            String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("").replace("\\target\\MaterialDidatico-1.0-SNAPSHOT\\", "\\src\\main\\webapp\\arquivos\\") + super.getEntidade().getId() + "\\" + arq.getFileName();
            File fileAnexo = new File(caminho);
            fileAnexo.getParentFile().mkdirs();
            extensao = arq.getFileName().substring(arq.getFileName().lastIndexOf("."), arq.getFileName().length()).replace(".", "");
            nomeArq = arq.getFileName();
            try {
                super.getEntidade().addItem(super.getEntidade(), extensao, nomeArq);
                materialFacade.salvar(super.getEntidade());
            } catch (Exception ex) {
                ex.printStackTrace();
                mensagem("Erro ao salvar arquivo: ", FacesMessage.SEVERITY_FATAL, nomeArq);
            }
            FileOutputStream fout = new FileOutputStream(caminho);
            while (in.available() != 0) {
                fout.write(in.read());
            }
            fout.close();
            mensagem("Sucesso ao fazer upload do arquivo: ", FacesMessage.SEVERITY_INFO, nomeArq);
        } catch (IOException e) {
            e.printStackTrace();
            mensagem("Erro ao fazer upload do arquivo: ", FacesMessage.SEVERITY_FATAL, nomeArq);
        }
    }

    public String excluirItem(ItemMaterial item, Material mat) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String caminho = ec.getRealPath("").replace("\\target\\MaterialDidatico-1.0-SNAPSHOT\\", "\\src\\main\\webapp\\arquivos\\") + item.getMaterial().getId() + "\\" + item.getNomearq();
            File arquivo = new File(caminho);
            if (!arquivo.exists()) {
                mensagem("Arquivo não encontrado: ", FacesMessage.SEVERITY_FATAL, item.getNomearq());
                return null;
            }
            mat.removeItem(item);
            materialFacade.salvar(mat);
            arquivo.delete();
            mensagem("Excluido com sucesso ", FacesMessage.SEVERITY_INFO, "");
            return "list?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            mensagem("Erro ao excluir o arquivo: ", FacesMessage.SEVERITY_FATAL, item.getNomearq());
            return null;
        }
    }

    public String excluir(Material material) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        for (ItemMaterial it : material.getItensMaterial()) {
            String caminho = ec.getRealPath("").replace("\\target\\MaterialDidatico-1.0-SNAPSHOT\\", "\\src\\main\\webapp\\arquivos\\") + material.getId() + "\\" + it.getNomearq();
            File arquivo = new File(caminho);
            if (!arquivo.exists()) {
                mensagem("Arquivo não encontrado: ", FacesMessage.SEVERITY_FATAL, it.getNomearq());
                return null;
            }
        }
        try {
            materialFacade.excluir(material);
        } catch (Exception e) {
            e.printStackTrace();
            mensagem("Erro ao excluir o arquivo", FacesMessage.SEVERITY_FATAL, "");
            return null;
        }
        mensagem("Excluido com sucesso ", FacesMessage.SEVERITY_INFO, "");
        return "list?faces-redirect=true";
    }

    @Override
    public String salvar() {
        try {
            Material m = new Material();
            m.setNome(super.getEntidade().getNome());
            m.setDataCadastro(new Date());
            m.setDisciplina(super.getEntidade().getDisciplina());
            materialFacade.salvar(m);
            mensagem("Salvo com sucesso: ", FacesMessage.SEVERITY_INFO, super.getEntidade().getNome());
            voltar();
        } catch (Exception ex) {
            ex.printStackTrace();
            mensagem("Erro ao salvar", FacesMessage.SEVERITY_FATAL, ex.getMessage());
        }
        return null;
    }
}
