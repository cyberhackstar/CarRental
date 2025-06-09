#!/bin/bash

BASE_DIR="/c/WorkSpace/New folder/CarRental"
PORTS=(8080 8081 8082)
ANGULAR_PORT=4200

# Function to check health of services
check_health() {
  echo -e "\nHealth Check:"
  for port in "${PORTS[@]}"; do
    if curl -s "http://localhost:$port/actuator/health" | grep -q '\"status\":\"UP\"'; then
      echo "Service on port $port is UP"
    else
      echo "Service on port $port is DOWN or not responding"
    fi
  done
}

# Function to tail logs
tail_logs() {
  echo -e "\nTailing logs. Press Ctrl+C to stop."
  tail -n 10 -f "$BASE_DIR"/Logs/*.log
}

# Function to open Angular app
open_browser() {
  echo "Opening Angular app in browser..."
  start "" "http://localhost:$ANGULAR_PORT"
}

# Function to stop running services
stop_services() {
  echo -e "\nStopping running services..."
  pkill -f 'mvn spring-boot:run'
  pkill -f 'ng serve'
  taskkill //F //IM java.exe > /dev/null 2>&1
  taskkill //F //IM node.exe > /dev/null 2>&1
  echo "All detected services have been stopped."
}

# Menu
while true; do
  echo -e "\nSelect an option:"
  echo "1. Check health of services"
  echo "2. Tail logs"
  echo "3. Open Angular app in browser"
  echo "4. Stop running services"
  echo "0. Exit"
  read -rp "Enter your choice: " choice

  case $choice in
    1) check_health ;;
    2) tail_logs ;;
    3) open_browser ;;
    4) stop_services ;;
    0) echo "Exiting..."; exit 0 ;;
    *) echo "Invalid choice. Try again." ;;
  esac
done
