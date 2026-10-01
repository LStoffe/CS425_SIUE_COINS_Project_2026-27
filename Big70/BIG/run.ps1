# Run the entire COINS stack with Docker (no Java/Maven/Node needed).
# Usage: .\run.ps1
# Requires: Docker Desktop

Set-Location $PSScriptRoot

Write-Host "Starting COINS (PostgreSQL + App + pgAdmin)..." -ForegroundColor Cyan
docker compose up -d

if ($LASTEXITCODE -ne 0) {
    Write-Host "If you see 'docker compose' not found, try: docker-compose up -d" -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "App:    http://localhost:8080" -ForegroundColor Green
Write-Host "pgAdmin: http://localhost:5050  (admin@local.test / adminpass)" -ForegroundColor Green
Write-Host ""
Write-Host "Logs:   docker compose logs -f app" -ForegroundColor Gray
Write-Host "Stop:   docker compose down" -ForegroundColor Gray
