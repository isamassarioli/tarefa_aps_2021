<#
Cria um branch git, adiciona o arquivo de dump e comita.
Uso:
  .\save_db_to_branch.ps1 -BranchName "db-backup-YYYYMMDD" -DumpPath "db\meu_exemplo_dump.sql" -Message "backup"
#>
param(
    [string]$BranchName = $("db-backup-$(Get-Date -Format yyyyMMdd)"),
    [string]$DumpPath = "db\meu_exemplo_dump.sql",
    [string]$Message = "Backup DB"
)

# Verifica git
if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
    Write-Error "git não encontrado no PATH. Instale Git ou rode este script em uma máquina com git."
    exit 1
}

# Verifica se o dump existe
if (-not (Test-Path $DumpPath)) {
    Write-Error "Arquivo de dump não encontrado: $DumpPath"
    exit 1
}

# Cria branch e commita
Write-Host "Criando branch '$BranchName' e adicionando $DumpPath"

# Stash changes to avoid losing work
git add $DumpPath
try {
    git checkout -b $BranchName
} catch {
    # Se o branch já existir, apenas faz checkout
    git checkout $BranchName
}

git add $DumpPath
git commit -m $Message
Write-Host "Dump salvo no branch $BranchName"
# Mostrar instrução para push
Write-Host "Para enviar ao remoto: git push -u origin $BranchName"
