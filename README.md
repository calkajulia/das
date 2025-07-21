# das - distributed averaging system

This application implements a distributed system that calculates the arithmetic mean of numbers from different processes. The system uses UDP protocol for communication between processes running on the same machine.

## System Architecture

The application operates in a **MASTER-SLAVE** architecture:

* **MASTER** - central process responsible for collecting data from all SLAVE processes and calculating the average
* **SLAVE** - client processes that send their numbers to the MASTER process

## Automatic Mode Selection

When you start the application with a specified port:
* If the port is **available** → process becomes **MASTER** and opens a socket on that port
* If the port is **occupied** → process becomes **SLAVE** and connects to the existing MASTER

## Communication Protocol

The system uses a simple text-based protocol:
* **Numbers other than 0 and -1**: MASTER stores the value and waits for more
* **Value 0**: MASTER calculates the average of all received non-zero numbers (including its initial value) and broadcasts the result to the local network
* **Value -1**: MASTER broadcasts termination signal and shuts down the entire system

## Compilation and Execution

```bash
# Compilation
javac *.java

# Execution
java DAS <port> <number>
```

## Detailed Operation Description

### MASTER (central process)
* Opens UDP socket on the specified port
* Stores its initial number (parameter `<number>`)
* Cyclically listens for messages from SLAVE processes
* Prints all received values to console
* Upon receiving **0**: calculates average, prints result and sends broadcast to local network
* Upon receiving **-1**: sends broadcast with termination signal and shuts down system

### SLAVE (client process)
* Creates UDP socket on a random available port
* Sends its number to the MASTER process running on port `<port>`
* Immediately closes socket and terminates