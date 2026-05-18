package cdp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "professor")
public class Professor extends Pessoa {

    @Column(name = "titulacao")
    private String titulacao;

    public Professor() {
        // construtor JPA
    }

    public Professor(String nome, Date dataNascimento, long CPF, String titulacao) {
        super(nome, dataNascimento, CPF);
        this.titulacao = titulacao;
    }

    public String getTitulacao() {
        return titulacao;
    }

    public void setTitulacao(String titulacao) {
        this.titulacao = titulacao;
    }
}
