# DTU 02139: Digital Electronics 2

This course is a follow up of Digital Electronics 1 for students of Electrical Engineering at DTU, but can also be taken from other study lines.

*This course is being restructured and therefore this web page is under constant change.*

### Aims of the course

### Tentative List of Topics

### Prerequisites

The course is intended for students enrolled in the bachelor program in electrical engineering or bachelor students from software engineering with an interest in
hardware design. Knowledge on following topics is a prerequisite:

 * Boolean algebra
 * Basic digital structures (e.g., AND gate, register)
 * Computer usage as a developer (text/program editor, file system, command line interface)
 * Basic programming

Depending on your background the prerequisites are covered in one of following courses:

 * 02138 Digital Electronics 1
 * 02132 Computer Systems
 * 02135 Introduction to Cyber Systems

### Schedule, Format, Place

The course is lectured Thursday afternoons 13:00 - 17:00.

First part is usually lectures and second part for project work.
Students will work in groups of two or three on small lab exercises
and as a final project on a vending machine.

The course is lectured in building TBD, room TBD.

### Pensum List

TBD 

## Recommended Textbook

 * Dally: [Digital Design Using VHDL a Systems Approach](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=3&cad=rja&uact=8&ved=2ahUKEwiijsD_76vmAhW4AxAIHci4Ai8QFjACegQIAhAB&url=https%3A%2F%2Fwww.cambridge.org%2Fcore%2Fbooks%2Fdigital-design-using-vhdl%2F0649D96BCF9E14D1AC4D192D629A3E5A&usg=AOvVaw3f4XcAOJdrcFfY6QdnGvRm)
 * Schoeberl: [Digital Design with Chisel](http://www.imm.dtu.dk/%7Emasca/chisel-book.html)

The Dally book is available from
[Polyteknisk Boghandel](http://www.polyteknisk.dk/)
in Building 101A.

## Resources

 * [Lab Material](https://github.com/schoeberl/chisel-lab)
 * [The Chisel Book](http://www.imm.dtu.dk/%7Emasca/chisel-book.html)
 * [Chisel](https://www.chisel-lang.org/)

## Instructor

[Martin Schoeberl](http://www.imm.dtu.dk/%7Emasca/)

## Lecture Plan (2020)

### Week 1 Introduction

**Topics:** Introduction and motivation, languages for hardware design, testing (see 2.1.4), tools and setup, a first round in Chisel

**Lab:** Chisel “Hello World”

**Reading:** Chisel: 1, 2, Dally: 2.1.4 

**Optional reading:** Dally: 1, 2


### Week 2 Basic Digital Circuits in Chisel

**Topics:** Recap combinational and synchronous circuits with Chisel code (including comparison with VHDL)

**Lab:** Basic circuits in Chisel, given testers, diagram to code and code to diagram exercises

**Reading:** Dally 8 and Chisel x


### Week 3 Timing

**Topics:** Timing, Metastability, and Power

**Lab:** Book exercises

**Reading:** Dally: 5, 15, and 28


### Week 4 State Machines in Chisel

**Topics:** Some more Chisel, state machine (repetition)

**Lab:** 7 segment decoder

**Reading:** Dally: 14, Chisel: 7 (stuff that is in Dally 7) (FIXME: check how this relates to week 2, maybe this is 2 material)

 * There should be a simple FSM lab with Chisel

### Week 5 Testing and Verification

**Topics:** Testing and verification in Chisel, using CLI, unit tests, waveform viewing

**Lab:** Tester for 7 segment decoder, counter for 7 segment display

**Reading:** Dally: 20, Chisel: 3, maybe some text on test driven development


### Week 6 State Machine and Datapath

**Topics:** State machine and datapath (FSMD) (1 hour)

**Lab:** Full display

**Reading:** Dally: 16, Chisel: 8.2


### Week 7

**Topics:** State Machines and Refactoring (1 hour)

**Lab:** Full display

**Reading:** Dally: 17


### Week 8

**Topics:** Communicating FSMs and ready/valid interface

**Lab:**

**Reading:** Dally: x, Chisel: 8.1


### Week 9

**Topics:** maybe lab only

**Lab:** work on vending machine


### Week 10

**Topics:** maybe lab only

**Lab:** work on vending machine


### Week 11 Microcoded State Machine

**Topics:** Microcoded FSMs

**Lab:** 18.1 and 18.2

**Reading:** Dally 18

 * This is all a little bit late when we want to cover interface, pipelining, and interconnect, memory


### Week 12 A Simple Processor

**Topics:** Leros as a simple processor

**Lab:** Work on VM

**Reading:** Dally x, Chisel: 11


### Week 13

**Topics:** Outro

**Lab:** Finish VM
