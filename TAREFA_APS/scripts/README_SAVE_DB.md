Fluxo para salvar o dump do PostgreSQL em um branch Git

1. Gere o dump do banco (exemplo):

   powershell.exe -File .\scripts\export_pg_dump.ps1 -HostName localhost -Port 5432 -Database meu_exemplo -User postgres -OutFile db\meu_exemplo_dump.sql

2. Crie um branch e salve o dump no Git:

   powershell.exe -File .\scripts\save_db_to_branch.ps1 -BranchName "db-backup-20260518" -DumpPath "db\meu_exemplo_dump.sql" -Message "backup 2026-05-18"

3. Envie para o remoto (opcional):

   git push -u origin db-backup-20260518

Observações:
- Os scripts usam `pg_dump` e `git` instalados e disponíveis no PATH.
- Não armazene senhas em texto no repositório. O script `export_pg_dump.ps1` solicita a senha interativamente e a passa temporariamente via variável de ambiente.
