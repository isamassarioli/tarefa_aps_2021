package cdp;

import java.util.Date;

public class Turma {
    private String horario;
    private int limiteAlunos;
    private boolean fechada;
    private Date dataInicio;
    private Date dataFim;

    public Turma(String horario, int limiteAlunos, boolean fechada, Date dataInicio, Date dataFim) {
        this.dataFim = dataFim;
        this.dataInicio = dataInicio;
        this.fechada = fechada;
        this.horario = horario;
        this.limiteAlunos = 40;
    }
}
