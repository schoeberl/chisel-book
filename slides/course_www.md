# DTU 02139: Digital Electronics 2

This course is a follow up of Digital Electronics 1 for students of Electrical Engineering at DTU, but can also be taken from other study lines.

### Aims of the course

To provide the participants with the competencies necessary to design digital circuits that perform simple calculations or control tasks using typical simulation and synthesis tools and to implement these circuits using reconfigurable hardware (FPGA technology). 

### List of Topics

 * Calculation of circuit delay and energy consumption of combinatorial circuits using R-C switch models.
 * Macromodels and timing parameters for combinatorial circuits and flip-flops. Calculation of critical path.
 * Hardware description in a hardware description language (Chisel)
 * Metastability and synchronization of asynchronous input signals.
 * FPGA technology: Basics of structure and principles of operation.
 * FSMD-style implementation template for a digital circuit (a so-called data-path controlled by a finite state machine).
 * Elementary HDL: (a) basic language constructs, (b) understanding its semantics based on how a given circuit simulates, (c) code templates for combinatorial and sequential circuits.
 * Lab exercises covering Chisel and related CAD tools for simulation, synthesis, and prototype implementation using FPGA technology (currently GTKWave and Xilinx Vivado). 

### Prerequisites

The course is intended for students enrolled in the bachelor program in electrical engineering or bachelor students from software engineering with an interest in
hardware design. Knowledge on following topics is a prerequisite:

 * Boolean algebra
 * Basic digital structures (e.g., AND gate, register)
 * Computer usage as a developer (text/program editor, file system, command line interface)
 * Basic programming

Depending on your background the prerequisites are covered in one of the following courses:

 * 02138 Digital Electronics 1
 * 02132 Computer Systems
 * 02135 Introduction to Cyber Systems

### Schedule, Format, Place

The course is lectured Thursday afternoons 13:00 - 17:00.
The course is lectured in English.

First part is usually lectures and second part is reserved for project work.
Students will work in groups of two or three on small lab exercises
and as a final project on a vending machine.

The course is lectured B308-A013 and the labs are in B308-IT117, B308-IT127.

### Pensum List

#### Dally

In all Dally sections ignore VHDL code

 * Section 8.1 - 8.6
 * Section 14.1 - 14.5
 * Section 15.1 - 15.6
 * Section 16.1 - 16.3
 * Section 17.1 - 17.2
 * Section 28.1 - 28.4

#### Chisel book (Schoeberl)

 * Chapter 2
 * Section 4.1 - 4.2
 * Chapter 5
 * Section 6.1 - 6.3
 * Chapter 7
 * Chapter 8
 * Chapter 9

#### Slides

 * Timing and RC switching model from L04 slides (in DTU Inside) 

## Recommended Textbooks

 * Dally: [Digital Design Using VHDL a Systems Approach](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=3&cad=rja&uact=8&ved=2ahUKEwiijsD_76vmAhW4AxAIHci4Ai8QFjACegQIAhAB&url=https%3A%2F%2Fwww.cambridge.org%2Fcore%2Fbooks%2Fdigital-design-using-vhdl%2F0649D96BCF9E14D1AC4D192D629A3E5A&usg=AOvVaw3f4XcAOJdrcFfY6QdnGvRm)
 * Schoeberl: [Digital Design with Chisel](http://www.imm.dtu.dk/%7Emasca/chisel-book.html)

The Dally book is available from
[Polyteknisk Boghandel](http://www.polyteknisk.dk/)
in Building 101A.
The Schoeberl book is open access and freely available.

## Resources

 * [Lab Material](https://github.com/schoeberl/chisel-lab)
 * [The Chisel Book](http://www.imm.dtu.dk/%7Emasca/chisel-book.html)
 * [Chisel](https://www.chisel-lang.org/)


## Instructor

[Martin Schoeberl](http://www.imm.dtu.dk/%7Emasca/)

## Lecture Plan

*This lecture plan may change during the semester, adapting to the current teaching situation.*

### Week 1 Introduction

**Topics:** Introduction and motivation, languages for hardware design, testing (see 2.1.4), tools and setup, a first round in Chisel

**Slides:** [01_intro.pdf](01_intro.pdf)

**Reading:** Chisel: 1, 2, Dally: 2.1.4 

**Optional reading:** Dally: 1, 2

**Lab:** [Chisel “Hello World”](https://github.com/schoeberl/chisel-lab)


### Week 2 Basic Digital Circuits in Chisel

**Topics:** Recap combinational and synchronous circuits with Chisel code (including comparison with VHDL)

**Slides:** [02_basic.pdf](02_basic.pdf)

**Reading:** Dally 8, 14.1, 14.2 and Chisel 5

**Lab:** [Combinational circuits in Chisel](https://github.com/schoeberl/chisel-lab)


### Week 3 Components and Sequential Circuits

**Topics:** Composing circuits out of components, sequential building blocks, using CLI

**Slides:** [03_sequential.pdf](03_sequential.pdf)

**Reading:** Dally: 16.1 and 16.2 Chisel: 3.1, 3.2, 4 and 6

**Lab:** [Components and sequential circuits](https://github.com/schoeberl/chisel-lab)


### Week 4 Timing (Jens Sparsoe)

**Topics:** Delay and Power, Timing, and Metastability

**Slides:** in DTU Learn

**Reading:** Dally: 15.1-15.6, and 28.1-28.4

**Optional reading:** (Slides in DTU Learn)

**Lab:** 15.1, 15.10, 15.11, 15.12, 15.19, 15.20, 15.21


### Week 5 Testing and Verification

**Topics:** Sequential building blocks, testing and verification in Chisel,
waveform viewing

**Slides:** [05_testing.pdf](05_testing.pdf)

**Reading:** Dally: 2.1.4 and 20, Chisel: 3

**Lab:** [A Simple Tester](https://github.com/schoeberl/chisel-lab)


### Week 6 State Machines

**Topics:** State machine (repetition) and the coding in Chisel

**Slides:** [06_fsm.pdf](06_fsm.pdf)

**Reading:** Dally: 14.3-14.5, Chisel: 8

**Lab:** [Hexadecimal to 7-segment decoder](https://github.com/schoeberl/chisel-lab)


### Week 7 State Machine and Datapath

**Topics:** State machine and datapath (FSMD)

**Slides:** [07_fsmd.pdf](07_fsmd.pdf)

**Reading:** Dally: 16.3, 29.1-29.3, Chisel: 9.2

**Lab:** Dally 14.3, 14.5 (Sketch a Chisel implementation on paper), 14.28, Sketch a Chisel implementation on paper for 14.28


### Week 8 State Machines and Refactoring

**Topics:** Refactoring of state machines, input processing, reset

**Slides:** [08_refactor.pdf](08_refactor.pdf)

**Reading:** Dally: 17, Chisel: 9.1, 7

**Lab:** [Multiplexed Seven-Segment Display](https://github.com/schoeberl/chisel-lab)


### Week 9 Communications State Machines

**Topics:** Ready/valid interface, and hardware generators

**Slides:** [09_commfsm.pdf](09_commfsm.pdf)

**Reading:** Dally: 22, Chisel: 9.3

**Lab:** [Using a UART Component](https://github.com/schoeberl/chisel-lab)


### Week 10 Interfacing and Memory, Vending Machine Specification

**Topics:** Interfaces, memory, and serial interface

**Slides:** [11_interface.pdf](11_interface.pdf)

**Reading:** Dally: 24 Chisel: 6.4

**Slides:** [10_vending.pdf](10_vending.pdf)

**Lab:** [Testing a Vending Machine](https://github.com/schoeberl/chisel-lab)


### Easter Week


### Week 11 Full day lab

**Lab:** [The Vending Machine](https://github.com/schoeberl/chisel-lab)


### Week 12 Outro, A Simple Processor

**Topics:** Lipsi as a simple processor, Discussion of course, and followup courses

**Slides:** [13_outro.pdf](13_outro.pdf)

**Lab:** [The Vending Machine](https://github.com/schoeberl/chisel-lab)


### Week 13 Guest Lecture on Verification and Verilog and VHDL


**Slides:** [13a_verilog.pdf](13a_verilog.pdf)

**Lab:** Finish vending machine and report
