#!/bin/bash

# Define project paths
SPRING_PROJECTS=(
  "C:/WorkSpace/New folder/CarRental/CarService"
  "C:/WorkSpace/New folder/CarRental/CarListnerService"
  "C:/WorkSpace/New folder/CarRental/CarAdminMonitor"
)
ANGULAR_PROJECT="C:/WorkSpace/New folder/CarRental/CarRentalWebApp"

# Create logs directory if it doesn't exist
mkdir -p logs

# Store PIDs
PIDS=()

# Function to rotate logs
rotate_log() {
  local log_file=$1
  if [ -f "$log_file" ]; then
    local timestamp=$(date +"%Y%m%d_%H%M%S")
    mv "$log_file" "${log_file%.log}_$timestamp.log"
  fi
}

# Function to start Spring Boot apps
start_spring_apps() {
  for project in "${SPRING_PROJECTS[@]}"; do
    app_name=$(basename "$project")
    log_file="logs/${app_name}.log"
    rotate_log "$log_file"
    echo "Starting Spring Boot app: $app_name"
    (cd "$project" && ./mvnw spring-boot:run > "$log_file" 2>&1 &)
    PIDS+=($!)
    sleep 2
  done
}

# Function to start Angular app
start_angular_app() {
  log_file="logs/angular_app.log"
  rotate_log "$log_file"
  echo "Starting Angular app..."
  (cd "$ANGULAR_PROJECT" && npm start > "$log_file" 2>&1 &)
  PIDS+=($!)
  sleep 2
}

# Function to stop all services
cleanup() {
  echo -e "\nStopping all services..."
  for pid in "${PIDS[@]}"; do
    kill "$pid" 2>/dev/null
  done
  echo "All services stopped."
  exit 0
}

# Trap Ctrl+C and call cleanup
trap cleanup SIGINT

# Start services
start_spring_apps
start_angular_app

# Keep script running
echo "All services are running. Press Ctrl+C to stop."
while true; do
  sleep 1
done
