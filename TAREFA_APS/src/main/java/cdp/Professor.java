package cdp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;

import java.util.List;
import java.util.Date;

@Entity
@Table(name = "professor")
public class Professor extends Pessoa {

    @Column(name = "titulacao")
    private String titulacao;

    public Professor() {
        // construtor JPA
    }

    @OneToMany(mappedBy = "professor")
    private List<Turma> turmas;

    public List<Turma> getTurmas() {
        return turmas;
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
