$ErrorActionPreference = 'Stop'
$labRoot = Split-Path $PSScriptRoot -Parent
$labTools = Join-Path $labRoot '.tools'
New-Item -ItemType Directory -Force -Path $labTools | Out-Null
$labZip = Join-Path $labTools 'maven.zip'
Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.11/apache-maven-3.9.11-bin.zip' -OutFile $labZip
Expand-Archive -LiteralPath $labZip -DestinationPath $labTools -Force
Write-Host 'Maven da san sang. Chay .\mvnw.cmd -f demo/pom.xml verify'
