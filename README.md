# Monopoly

[![Build](https://github.com/ve1lchen/project-monopoly/actions/workflows/build.yml/badge.svg)](https://github.com/ve1lchen/project-monopoly/actions/workflows/build.yml)
[![Mutation Testing](https://github.com/ve1lchen/project-monopoly/actions/workflows/mutation-testing.yml/badge.svg)](https://github.com/ve1lchen/project-monopoly/actions/workflows/mutation-testing.yml)
[![Coverage Status](https://coveralls.io/repos/github/ve1lchen/project-monopoly/badge.svg?branch=main)](https://coveralls.io/github/ve1lchen/project-monopoly?branch=main)


A university Software Engineering project implementing Monopoly in Scala 3.
The current prototype uses a console interface; a GUI is planned.

## Run

With a JDK and sbt installed:

```sh
sbt run
```

The prototype starts with two players, Alice and Renee.

| Command | Action |
|---|---|
| `1` | Roll dice and move the current player |
| `2` | Buy the current property if it is available and affordable |
| `3` | End the turn after rolling; a double keeps the same player |
| `0` | Exit and display player balances and owned field positions |

Use `0` to exit: EOF handling is not implemented yet.

## Development Status

Rolling, movement, property purchases and turn changes are implemented.
Rent, Start rewards, special-field actions, bankruptcy integration and automatic
game termination remain unfinished.

`DummyBoard` and `DummyField` are temporary placeholders for the board and field
classes being developed by the other team member. The current 11-field board and
its prices are sample data. They will be replaced during integration.

See [ARCHITECTURE.md](ARCHITECTURE.md) for component responsibilities, the turn
flow, integration contracts and known limitations.

## Tests

```sh
sbt test
sbt clean coverage test coverageReport
```

The HTML coverage report is written to
`target/scala-3.9.0/scoverage-report/index.html`.
