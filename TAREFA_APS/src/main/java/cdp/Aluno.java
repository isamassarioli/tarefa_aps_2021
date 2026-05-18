package cdp;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToMany;
import java.util.Date;

@Entity
@Table(name = "aluno")
public class Aluno extends Pessoa {
    public Aluno() {
        // construtor padrão JPA
    }

    public Aluno(String nome, Date dataNascimento, long CPF) {
        super(nome, dataNascimento, CPF);
    }

    @ManyToMany(mappedBy = "alunos")
    private java.util.List<Turma> turmas;

    public java.util.List<Turma> getTurmas() {
        return turmas;
    }

}
