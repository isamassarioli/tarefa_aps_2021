package tools;

import cgt.AplGerenciarPessoas;
import cdp.Aluno;
import java.util.List;

/**
 * Small smoke test to verify JPA/DAO connectivity and list returned Aluno objects.
 */
public class TestSmoke {
    public static void main(String[] args) {
        System.out.println("Starting smoke test: listarAlunos()...");
        try {
            AplGerenciarPessoas apl = new AplGerenciarPessoas();
            List<Aluno> alunos = apl.listarAlunos();
            if (alunos == null || alunos.isEmpty()) {
                System.out.println("No alunos returned (list is empty or null).");
            } else {
                System.out.println("Found " + alunos.size() + " alunos:");
                for (Aluno a : alunos) {
                    System.out.println("- " + a.getNome() + " | CPF=" + a.getCPF() + " | nascimento=" + a.getDataNascimento());
                }
            }
        } catch (Exception ex) {
            System.err.println("Exception while running smoke test:");
            ex.printStackTrace();
            System.exit(2);
        }
        System.out.println("Smoke test finished.");
    }
}
