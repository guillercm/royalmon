@echo off
for /f "tokens=5" %a in ('netstat -aon ^| find "8080"') do taskkill /f /pid %a
pause
