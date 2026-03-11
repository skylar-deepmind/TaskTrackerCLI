# TaskTrackerCLI

A minimal Java CLI task tracker with local JSON persistence (no external JSON library).

## Features
- Add, update, delete tasks
- Mark task status: `todo`, `in-progress`, `done`
- List all tasks or filter by status
- Persist tasks to a local `task-tracker.json` file

## Requirements
- JDK 17+
- Maven 3.8+

## Build
```bash
mvn -q -DskipTests package
```

## Run
Use the compiled classes in `target/classes`:
```bash
java -cp target/classes TaskCli add "Write README"
java -cp target/classes TaskCli list
java -cp target/classes TaskCli update 1 "Write README and comments"
java -cp target/classes TaskCli mark-in-progress 1
java -cp target/classes TaskCli mark-done 1
java -cp target/classes TaskCli delete 1
```

## Commands
- `add "description"`
- `update <id> "new description"`
- `delete <id>`
- `mark-in-progress <id>`
- `mark-done <id>`
- `list`
- `list <status>`

## Data File
Tasks are stored in `task-tracker.json` in the current working directory.

Example structure:
```json
[
  {
    "id": 1,
    "description": "Write README",
    "status": "todo",
    "createdAt": "2026-03-11T10:00:00",
    "updatedAt": "2026-03-11T10:00:00"
  }
]
```

## Project Structure
- `src/main/java/Task.java`: Task model
- `src/main/java/TaskCli.java`: CLI entry point
- `src/main/java/TaskRepository.java`: JSON persistence
- `pom.xml`: Maven build config
