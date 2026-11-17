@echo off
setlocal
set "LAB2_MAVEN=%~dp0.tools\apache-maven-3.9.11\bin\mvn.cmd"
if exist "%LAB2_MAVEN%" (
    call "%LAB2_MAVEN%" "-Dmaven.repo.local=%~dp0.tools\m2" %*
    goto done
)
where mvn.cmd >nul 2>nul
if errorlevel 1 (
    echo Maven chua co. Chay: powershell -File scripts\setup-maven.ps1
    exit /b 1
)
call mvn.cmd %*
:done
exit /b %ERRORLEVEL%
