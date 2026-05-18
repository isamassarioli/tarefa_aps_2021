<#
Exporta o banco PostgreSQL usando pg_dump para um arquivo .sql
Uso:
  .\export_pg_dump.ps1 -Host localhost -Port 5432 -Database meu_exemplo -User postgres -OutFile db\meu_exemplo_dump.sql
Se não passar OutFile, será usado db\meu_exemplo_dump.sql
#>
param(
    [string]$HostName = "localhost",
    [int]$Port = 5432,
    [string]$Database = "meu_exemplo",
    [string]$User = "postgres",
    [string]$OutFile = "db\\meu_exemplo_dump.sql"
)

# Checa se pg_dump está disponível
if (-not (Get-Command pg_dump -ErrorAction SilentlyContinue)) {
    Write-Error "pg_dump não foi encontrado no PATH. Instale PostgreSQL client ou adicione pg_dump ao PATH."
    exit 1
}

# Cria pasta db se não existir
$dir = Split-Path -Path $OutFile -Parent
if (-not (Test-Path $dir)) {
    New-Item -ItemType Directory -Path $dir | Out-Null
}

# Pede a senha de forma segura
Write-Host "Exportando banco '$Database' em ${HostName}:${Port} como usuário $User"
$securePw = Read-Host -AsSecureString "Senha do usuário $User (será usada temporariamente via PGPASSWORD)"
$ptr = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePw)
try {
    $plainPw = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($ptr)
} finally {
    [System.Runtime.InteropServices.Marshal]::ZeroFreeBSTR($ptr)
}

# Define variável de ambiente temporária
$env:PGPASSWORD = $plainPw

try {
    $cmd = "pg_dump -h $HostName -p $Port -U $User -F p -f `"$OutFile`" $Database"
    Write-Host "Executando: $cmd"
    & pg_dump -h $HostName -p $Port -U $User -F p -f $OutFile $Database
    if ($LASTEXITCODE -ne 0) {
        Write-Error "pg_dump retornou código $LASTEXITCODE"
        exit $LASTEXITCODE
    }
    else {
        Write-Host "Dump gerado com sucesso em: $OutFile"
    }
} finally {
    # Remove a variável de ambiente com a senha
    Remove-Item Env:PGPASSWORD -ErrorAction SilentlyContinue
}
