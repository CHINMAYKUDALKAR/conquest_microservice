#!/bin/bash

# ─────────────────────────────────────────────
# Conquest Microservice — Startup Script
# Starts all services in the correct order
# ─────────────────────────────────────────────

BASE_DIR="$(cd "$(dirname "$0")" && pwd)"

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
RED='\033[0;31m'
NC='\033[0m'

# Logging helpers
log() { echo -e "${CYAN}[Conquest]${NC} $1"; }
success() { echo -e "${GREEN}[✓]${NC} $1"; }
waiting() { echo -e "${YELLOW}[⏳]${NC} $1"; }

# Required ports
PORTS=(9000 9001 9002 9090 8000)

# Free a port by killing whatever is using it
free_port() {
    local port=$1
    local pids=$(lsof -ti tcp:$port 2>/dev/null)
    if [ -n "$pids" ]; then
        echo -e "${RED}[✗]${NC} Port $port is in use (PID: $pids) — killing..."
        echo "$pids" | xargs kill -9 2>/dev/null
        sleep 1
        success "Port $port freed."
    fi
}

# Cleanup — kill all background processes on Ctrl+C
cleanup() {
    echo ""
    log "Shutting down all services..."
    kill $(jobs -p) 2>/dev/null
    wait 2>/dev/null
    success "All services stopped."
    exit 0
}
trap cleanup SIGINT SIGTERM

# ── 0. Free all required ports ──
echo ""
log "Checking and freeing required ports..."
for port in "${PORTS[@]}"; do
    free_port $port
done
success "All ports are available."
echo ""

# ── 1. Eureka Server (must start first) ──
log "Starting Eureka Server (port 9000)..."
cd "$BASE_DIR/eureka_server_app/eureka_server_app"
mvn spring-boot:run -q &
EUREKA_PID=$!

waiting "Waiting 20s for Eureka to be ready..."
sleep 20
success "Eureka Server started (PID: $EUREKA_PID)"

# ── 2. User Service ──
log "Starting User Service (port 9001)..."
cd "$BASE_DIR/user_service/user_service"
mvn spring-boot:run -q &
USER_PID=$!

# ── 3. Ticket Service ──
log "Starting Ticket Service (port 9002)..."
cd "$BASE_DIR/ticket_service/ticket_service"
mvn spring-boot:run -Dspring-boot.run.fork=false -q &
TICKET_PID=$!

waiting "Waiting 15s for User & Ticket services to register with Eureka..."
sleep 15
success "User Service started (PID: $USER_PID)"
success "Ticket Service started (PID: $TICKET_PID)"

# ── 4. API Gateway ──
log "Starting API Gateway (port 9090)..."
cd "$BASE_DIR/api_gateway/api_gateway"
mvn spring-boot:run -q &
GATEWAY_PID=$!

waiting "Waiting 10s for Gateway to be ready..."
sleep 10
success "API Gateway started (PID: $GATEWAY_PID)"

# ── 5. Frontend ──
log "Starting Frontend (port 8000)..."
cd "$BASE_DIR/frontend"
python3 -m http.server 8000 &
FRONTEND_PID=$!
success "Frontend started (PID: $FRONTEND_PID)"

echo ""
echo -e "${GREEN}════════════════════════════════════════════${NC}"
echo -e "${GREEN}  All services are running!${NC}"
echo -e "${GREEN}════════════════════════════════════════════${NC}"
echo ""
echo -e "  Eureka Dashboard:  ${CYAN}http://localhost:9000${NC}"
echo -e "  User Service:      ${CYAN}http://localhost:9001${NC}"
echo -e "  Ticket Service:    ${CYAN}http://localhost:9002${NC}"
echo -e "  API Gateway:       ${CYAN}http://localhost:9090${NC}"
echo -e "  Frontend UI:       ${CYAN}http://localhost:8000${NC}"
echo ""
echo -e "  Press ${YELLOW}Ctrl+C${NC} to stop all services."
echo ""

# Wait for all background processes
wait
