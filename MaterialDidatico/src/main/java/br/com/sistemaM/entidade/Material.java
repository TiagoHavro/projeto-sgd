/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistemaM.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.hibernate.envers.Audited;

/**
 *
 * @author tiago
 */
@Entity
@Audited
@Table(name = "material")
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mat_id", nullable = false)
    private Long id;
    @Column(name = "mat_nome", nullable = false)
    private String nome;
    @Column(name = "mat_data_cadastro", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataCadastro;
    @ManyToOne
    @JoinColumn(name = "dis_id", nullable = false)
    private Disciplina disciplina;
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "material",
            orphanRemoval = true)
    private Set<ItemMaterial> itensMaterial = new HashSet<>();

    @Transient
    private ItemMaterial itemMaterial = new ItemMaterial();

    public void addItem(Material m, String extensao, String nomeArq) throws Exception {
        boolean contem = false;
        itemMaterial = new ItemMaterial();
        itemMaterial.setFormato(extensao);
        itemMaterial.setNomearq(nomeArq);
        itemMaterial.setDataCadastro(new Date());
        itemMaterial.setMaterial(m);
        for (ItemMaterial i : itensMaterial) {
            if (i.getMaterial().equals(itemMaterial.getMaterial()) && i.getNomearq().equals(itemMaterial.getNomearq())) {
                contem = true;
                throw new Exception("O Arquivo já está adicionado");
            }
        }
        if (!contem) {
            itensMaterial.add(itemMaterial);
        }
    }

    public void removeItem(ItemMaterial item) {
        itensMaterial.remove(item);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Set<ItemMaterial> getItensMaterial() {
        return itensMaterial;
    }

    public void setItensMaterial(Set<ItemMaterial> itensMaterial) {
        this.itensMaterial = itensMaterial;
    }

    public ItemMaterial getItemMaterial() {
        return itemMaterial;
    }

    public void setItemMaterial(ItemMaterial itemMaterial) {
        this.itemMaterial = itemMaterial;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Material)) {
            return false;
        }
        Material other = (Material) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id.toString();
    }

}
