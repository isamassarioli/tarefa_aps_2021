package cdp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "turma")
public class Turma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "horario")
    private String horario;

    @Column(name = "limite_alunos")
    private int limiteAlunos;

    @Column(name = "fechada")
    private boolean fechada;

    @Column(name = "data_inicio")
    private Date dataInicio;

    @Column(name = "data_fim")
    private Date dataFim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @OneToMany
    private List<Aluno> alunos;

    public Turma() {
        this.alunos = new ArrayList<>();
    }

    public Turma(String horario, int limiteAlunos, boolean fechada, Date dataInicio, Date dataFim) {
        this.dataFim = dataFim;
        this.dataInicio = dataInicio;
        this.fechada = fechada;
        this.horario = horario;
        this.limiteAlunos = 40;
        this.alunos = new ArrayList<>();
    }

    public Turma(String horario, int limiteAlunos, boolean fechada, Date dataInicio, Date dataFim, Curso curso, Professor professor) {
        this.dataFim = dataFim;
        this.dataInicio = dataInicio;
        this.fechada = fechada;
        this.horario = horario;
        this.limiteAlunos = limiteAlunos;
        this.curso = curso;
        this.professor = professor;
        this.alunos = new ArrayList<>();
    }

    public String getHorario() {
        return horario;
    }

    public int getLimiteAlunos() {
        return limiteAlunos;
    }

    public boolean isFechada() {
        return fechada;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public Curso getCurso() {
        return curso;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public void setLimiteAlunos(int limiteAlunos) {
        this.limiteAlunos = limiteAlunos;
    }

    public void setFechada(boolean fechada) {
        this.fechada = fechada;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void adicionarAluno(Aluno aluno) {
        if (alunos.size() < limiteAlunos && !alunos.contains(aluno)) {
            alunos.add(aluno);
        }
    }

    public void removerAluno(Aluno aluno) {
        alunos.remove(aluno);
    }
}
