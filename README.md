# turing-machine

Turing machine made in Clojure, pure functional way:
1. Tape consists of 2 lists that have top elements (head elements) directed toward machine's head. Head of machine is the head of left list
2. Machine state is left tape side, right tape side and machine state put into vector
3. All state changes are cons'ed and head'ed via lazy sequences

## Installation

No installation required

## Usage

    $ java -jar turing-machine-0.1.0-standalone.jar <CA_0_TURING... file>

## Options


Turing machine file format:
First row - initial head position
Second row - initial tape
Third and further rows - rules.
The rule consist of 5 columns:
<State> <Char under head> <Char to write> <Head direction> <Next state>

State is string (i.e. not a single char)
Machine starts with "0" state

When executing file CA_0_TURING_add_bin.tmprog.txt machine ends with exception - that
is expected behaviour, as machine has done it's job (i.e. ended adding numbers) and cannot find rule
for some final (halt) state.

## License

No license
