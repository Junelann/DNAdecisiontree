# Automated Sequence Generator

## Description

**Automated Sequence Generator**  is a Java program designed to automatically generate tree-encoding DNA sequences according to specific rules. The program takes a decision tree (represented by an adjacency matrix) as input and generates DNA sequences of tree-encoding molecules as output.

## System Requirements

### Software Dependencies

- **Java Development Kit (JDK):** Version 8 or above (JDK 11+ recommended)
- **Operating System:**
  - Windows 10/11
  - macOS 11.0 or above
  - Linux (Ubuntu 18.04+)
- **Third-party Libraries:**
  - No external dependencies; only standard Java API is used.

### Tested Versions

- **JDK:** Tested on JDK 8, JDK 11, JDK 17
- **Operating Systems:**
  - Windows 10
  - Ubuntu 20.04
  - macOS Ventura 13.0

### Hardware Requirements

- **Standard desktop or laptop computer**
  - Recommended:
    - RAM ≥ 2GB
    - CPU ≥ 2 cores
    - No GPU or special hardware required

## Installation Guide

### Instructions

1. **Download the source code**
    - Clone this repository or download the ZIP package and extract it.
2. **Prepare input data**
    - Prepare the adjacency matrix text file as described in the [Demo](#demo) section below.
3. **Compile the code**
    - Use the command line or an IDE (e.g., IntelliJ IDEA, Eclipse) to compile all Java files.
    - Command line example (assuming source files are in `src/`):
      ```bash
      javac src/*.java
      ```
4. **Run the program**
    - Navigate to the directory containing the compiled class files and run the main program:
      ```bash
      java ProgramEntryPoint
      ```

### Typical Install Time

- **Download and compilation:**  
  On a standard desktop computer, downloading and compiling the entire project typically takes less than 1 minute.

## Demo

### Instructions to Run on Data

1. **Prepare the input file**

- This is an example binary tree with 6 nodes (0–5).  The tree structure is shown below:
```
    0
   / \
  1   2
 / \    \
3   4    5
```

- The default input file name is `input.txt`, and its content should be an **adjacency matrix**, with each row separated by spaces.  

#### Adjacency Matrix Example
| From\To | 0 | 1 | 2 | 3 | 4 | 5 |
|---------|---|---|---|---|---|---|
| 0       | 0 | 1 | 1 | 0 | 0 | 0 |
| 1       | 0 | 0 | 0 | 1 | 1 | 0 |
| 2       | 0 | 0 | 0 | 0 | 0 | 1 |
| 3       | 0 | 0 | 0 | 0 | 0 | 0 |
| 4       | 0 | 0 | 0 | 0 | 0 | 0 |
| 5       | 0 | 0 | 0 | 0 | 0 | 0 |

#### Notes
- `1` indicates an edge from the row node to the column node (parent → child).  
- `0` indicates no edge.  

2. **Run the main program**
    - Execute the following command:
      ```bash
      java ProgramEntryPoint
      ```
    - The program will automatically read `input.txt`, generate DNA sequences, and output them to `output.txt`.

### Expected Output

- **Output file `output.txt`:**
    - Each line follows the format:
      ```
      selfIndex:0   childIndex:1  sequence:ATG...GTC,CGT...TGA,...
      ```
      - `selfIndex`: Current node index
      - `childIndex`: Child node index
      - `sequence`: Processed DNA sequences (comma-separated; types: top, btm1, btm2, input)

### Expected Run Time

- **Small graphs (≤10 nodes):**  
  Sequence generation and output: ~1-2 seconds
- **Medium graphs (100 nodes):**  
  ~10-20 seconds
- **Large graphs (1000 nodes):**  
  Depending on hardware and tree complexity, ~1-2 minutes

## Instructions for Use

1. **Prepare the adjacency matrix file**
    - You can customize the file name, but you need to modify the path in `ProgramEntryPoint` accordingly.
2. **Modify main program parameters (if needed)**
    - Default input file is `input.txt`, output file is `output.txt`. You can change these in `ProgramEntryPoint`.
3. **Run the program**
    - Follow the steps described in the [Demo](#demo) section.
4. **Check the output**
    - Find all generated DNA sequences in the output file.
