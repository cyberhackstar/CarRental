#!/bin/bash

# Set JAVA_HOME and update PATH
export JAVA_HOME="/c/Program Files/Eclipse Adoptium/jdk-21.0.6.7-hotspot"
export PATH="$JAVA_HOME/bin:$PATH"

# Define project directories
BASE_DIR="/c/WorkSpace/New folder/CarRental"
SPRING_PROJECTS=("CarService" "CarListnerService" "CarAdminMonitor")
ANGULAR_PROJECT="CarRentalWebApp"

# Define ports for health checks
PORTS=(8081 8082 8080)
ANGULAR_PORT=4200

# Create logs directory if it doesn't exist
mkdir -p "$BASE_DIR/Logs"#!/bin/bash

# Set JAVA_HOME and update PATH
export JAVA_HOME="/c/Program Files/Eclipse Adoptium/jdk-21.0.6.7-hotspot"
export PATH="$JAVA_HOME/bin:$PATH"

# Define project directories
BASE_DIR="/c/WorkSpace/New folder/CarRental"
SPRING_PROJECTS=("CarService" "CarListnerService" "CarAdminMonitor")
ANGULAR_PROJECT="CarRentalWebApp"

# Define ports for health checks
PORTS=(8081 8082 8080)
ANGULAR_PORT=4200

# Create logs directory if it doesn't exist
mkdir -p "$BASE_DIR/Logs"

# Store PIDs
PIDS=()

rotate_log() {
  local log_file=$1
  if [ -f "$log_file" ]; then
    local timestamp=$(date +"%Y%m%d_%H%M%S")
    mv "$log_file" "${log_file%.log}_$timestamp.log"
  fi
}

start_activemq() {
  local activemq_path="/c/Users/10827643/Downloads/apache-activemq-6.1.6-bin/apache-activemq-6.1.6/bin"
  echo "Starting ActiveMQ..."
  (cd "$activemq_path" && ./activemq start > "$BASE_DIR/Logs/activemq.log" 2>&1 &)
  PIDS+=($(jobs -p))
  sleep 3
}

start_spring_apps() {
  for i in "${!SPRING_PROJECTS[@]}"; do
    local project="${SPRING_PROJECTS[$i]}"
    local port="${PORTS[$i]}"
    local project_path="$BASE_DIR/$project"
    echo "Starting Spring Boot app: $project on port $port"
    (cd "$project_path" && mvn spring-boot:run &)
    PIDS+=($(jobs -p))
    sleep 3
  done
}

start_angular_app() {
  local project_path="$BASE_DIR/$ANGULAR_PROJECT"
  local log_file="$BASE_DIR/Logs/angular_app.log"
  rotate_log "$log_file"
  echo "Starting Angular app..."
  (cd "$project_path" && npx ng serve --port $ANGULAR_PORT > "$log_file" 2>&1 &)
  PIDS+=($(jobs -p))
  sleep 5
}

cleanup() {
  echo -e "\nStopping all services..."
  for pid in "${PIDS[@]}"; do
    kill "$pid" 2>/dev/null
  done

  local activemq_path="/c/Users/10827643/Downloads/apache-activemq-6.1.6-bin/apache-activemq-6.1.6/bin"
  (cd "$activemq_path" && ./activemq stop > /dev/null 2>&1)

  pkill -f 'mvn spring-boot:run'
  pkill -f 'ng serve'
  taskkill //F //IM java.exe > /dev/null 2>&1
  taskkill //F //IM node.exe > /dev/null 2>&1

  PIDS=()
  echo "All services stopped."
  exit 0
}

trap cleanup SIGINT

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

open_browser() {
  echo "Opening Angular app in browser..."
  start "" "http://localhost:$ANGULAR_PORT"
}

tail_logs() {
  echo -e "\nTailing logs. Press Ctrl+C to stop and shut down services."
  tail -n 10 -f "$BASE_DIR"/Logs/*.log &
  TAIL_PID=$!

  while kill -0 "$TAIL_PID" 2>/dev/null; do
    sleep 1
  done
}

main_menu() {
  while true; do
    echo -e "\nSelect an option:"
    echo "1. Start all services"
    echo "2. Restart all services"
    echo "3. Stop all services"
    echo "0. Exit"
    read -rp "Enter your choice: " choice

    case $choice in
      1)
        start_activemq
        start_spring_apps
        start_angular_app
        open_browser
        tail_logs
        ;;
      2)
        cleanup
        start_activemq
        start_spring_apps
        start_angular_app
        open_browser
        tail_logs
        ;;
      3)
        cleanup
        ;;
      0)
        cleanup
        echo "Exiting..."
        exit 0
        ;;
      *)
        echo "Invalid choice. Please try again."
        ;;
    esac
  done
}

main_menu


# Store PIDs
PIDS=()

rotate_log() {
  local log_file=$1
  if [ -f "$log_file" ]; then
    local timestamp=$(date +"%Y%m%d_%H%M%S")
    mv "$log_file" "${log_file%.log}_$timestamp.log"
  fi
}

trap cleanup SIGINT

start_activemq() {
  local activemq_path="/c/Users/10827643/Downloads/apache-activemq-6.1.6-bin/apache-activemq-6.1.6/bin"
  echo "Starting ActiveMQ..."
  (cd "$activemq_path" && ./activemq start > "$BASE_DIR/Logs/activemq.log" 2>&1 &)
  PIDS+=($(jobs -p))
  sleep 3
}

start_spring_apps() {
  for i in "${!SPRING_PROJECTS[@]}"; do
    local project="${SPRING_PROJECTS[$i]}"
    local port="${PORTS[$i]}"
    local project_path="$BASE_DIR/$project"
    local log_file="$BASE_DIR/Logs/${project}.log"
    rotate_log "$log_file"
    echo "Starting Spring Boot app: $project on port $port"
    (cd "$project_path" && mvn spring-boot:run &)
    PIDS+=($(jobs -p))
    sleep 3
  done
}

start_angular_app() {
  local project_path="$BASE_DIR/$ANGULAR_PROJECT"
  local log_file="$BASE_DIR/Logs/angular_app.log"
  rotate_log "$log_file"
  echo "Starting Angular app..."
  (cd "$project_path" && npx ng serve --port $ANGULAR_PORT > "$log_file" 2>&1 &)
  PIDS+=($(jobs -p))
  sleep 5
}

cleanup() {
  echo -e "\nStopping all services..."
  for pid in "${PIDS[@]}"; do
    kill "$pid" 2>/dev/null
  done

  # Try to stop ActiveMQ gracefully
  local activemq_path="/c/Users/10827643/Downloads/apache-activemq-6.1.6-bin/apache-activemq-6.1.6/bin"
  (cd "$activemq_path" && ./activemq stop > /dev/null 2>&1)

  # Fallback: kill Java and Node processes
  pkill -f 'mvn spring-boot:run'
  pkill -f 'ng serve'
  taskkill //F //IM java.exe > /dev/null 2>&1
  taskkill //F //IM node.exe > /dev/null 2>&1
  

  PIDS=()
  echo "All services stopped."
  
}


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

open_browser() {
  echo "Opening Angular app in browser..."
  start "" "http://localhost:$ANGULAR_PORT"
}

tail_logs() {
  echo -e "\nTailing logs. Press Ctrl+C to return to menu."
  tail -n 10 -f "$BASE_DIR"/Logs/*.log &
  TAIL_PID=$!
  trap "kill $TAIL_PID 2>/dev/null" SIGINT
  wait $TAIL_PID
}


main_menu() {
  while true; do
    echo -e "\nSelect an option:"
    echo "1. Start all services"
    echo "2. Restart all services"
    echo "3. Stop all services"
    echo "0. Exit"
    read -rp "Enter your choice: " choice

    case $choice in
	  1)
		start_activemq
		start_spring_apps
		start_angular_app
		open_browser
		tail_logs
		;;
	  2)
		cleanup
		start_activemq
		start_spring_apps
		start_angular_app
		open_browser
		tail_logs
		;;

      3)
        cleanup
        ;;
      0)
        cleanup
        echo "Exiting..."
        exit 0
        ;;
      *)
        echo "Invalid choice. Please try again."
        ;;
    esac
  done
}

main_menu
