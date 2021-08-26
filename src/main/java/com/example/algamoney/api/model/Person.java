package com.example.algamoney.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @NotNull
    private String name;

    @Embedded
    private Adress adress;

    @NotNull
    private Boolean active;

    public Long getCode() {
        return code;
    }

    public void setCode(Long codigo) {
        this.code = codigo;
    }

    public String getName() {
        return name;
    }

    public void setName(String nome) {
        this.name = nome;
    }

    public Adress getAdress() {
        return adress;
    }

    public void setAdress(Adress endereco) {
        this.adress = adress;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean ativo) {
        this.active = ativo;
    }

    @JsonIgnore
    @Transient
    public boolean isInactive(){ return !this.active; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }

}