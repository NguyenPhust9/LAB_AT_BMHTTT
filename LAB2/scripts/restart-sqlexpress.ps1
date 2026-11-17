#Requires -RunAsAdministrator
$ErrorActionPreference = 'Stop'
$labRoot = Split-Path $PSScriptRoot -Parent
try {
    Restart-Service -Name 'MSSQL$SQLEXPRESS' -ErrorAction Stop
    (Get-Service -Name 'MSSQL$SQLEXPRESS').WaitForStatus('Running', [TimeSpan]::FromSeconds(30))
    'SQLEXPRESS restarted successfully.' | Set-Content -LiteralPath (Join-Path $labRoot 'tmp/sql-restart.log')
} catch {
    $_.Exception.Message | Set-Content -LiteralPath (Join-Path $labRoot 'tmp/sql-restart.log')
    exit 1
}
