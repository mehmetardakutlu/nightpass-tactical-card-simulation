# Nightpass: Survival Card Game Simulation

Nightpass is a high-performance tactical simulation engine developed for the **CMPE 250 (Data Structures and Algorithms)** course at Boğaziçi University. The project simulates a survival duel against a mysterious entity known as "The Stranger," where strategic card management and efficient data processing are the keys to survival.

## 🛠 Technical Implementation & Constraints

The core challenge of this project was the **strict prohibition of standard Java Collection Framework libraries** (except for `ArrayList`). Every data structure used for the game logic was implemented from scratch to ensure maximum efficiency and demonstrate core computer science principles.

### Custom AVL Tree Architecture
* **Self-Balancing Logic:** Implemented a robust **AVL Tree** to maintain $O(\log n)$ time complexity for insertions, deletions, and search operations.
* **Nested Tree Design:** Utilized an `AttackTree` where each node (representing an attack value) hosts a nested `QueueTree` (representing health values). This enables efficient multi-criteria searching during battle phases.
* **Automatic Balancing:** Features height tracking and rotation mechanisms (left, right, left-right, right-left) to handle dynamic stat updates while maintaining tree equilibrium.

### Battle Priority & Decision Engine
* **Tactical Card Selection:** Implemented a complex 4-tier battle priority system to determine the optimal card for each duel:
    1. Survive and Kill
    2. Survive and Damage
    3. Kill and Die
    4. Max Damage
* **Deterministic Tie-Breaking:** Integrated a custom **FIFO Queue** (`MyQueue`) to resolve conflicts based on the exact chronology of card deck entry.
* **Resource Allocation:** Designed a **Healing and Revival** mechanism that manages a finite "Heal Pool" to restore fallen cards from the discard pile, applying multiplicative permanent stat penalties upon revival.

## 🚀 Performance & Scaling
* **Massive Data Handling:** Successfully processes over **550,000 commands** and **400,000 card entities**.
* **Execution Efficiency:** Optimized to complete large-scale performance benchmarks in **under 6 seconds**, significantly surpassing the 15-second academic limit.
* **Algorithmic Complexity:** Achieves $O(\log n)$ performance for complex queries like `steal_card` and `battle` through efficient tree traversal and nested data management.

## 📚 Demonstrated Skills
* **Advanced Data Structures:** Nested AVL Trees, Custom Priority Queues, and FIFO Queues.
* **Algorithm Optimization:** Time complexity management ($O(n \log n)$ scaling) and large-scale simulation engineering.
* **Object-Oriented Design:** Decoupled architecture using specialized classes for `Game`, `Card`, and node logic to ensure maintainability.

## 🖥 How to Run
### 1. Recommended: Python Test Runner
The easiest way to verify the simulation and benchmark performance is using the automated script from the project root:
# Test all cases
```bash
python3 test_runner.py
```
# Test specific types
```bash
python3 test_runner.py --type type1
```
```bash
python3 test_runner.py --type type2
```
The script automatically compares your outputs with expected results and displays execution times.
### 2. Manual Compilation and Execution
To run individual test cases manually, execute the following from the directory where the *.java files are located:
# Compile source code
```bash
javac *.java
```

# Execute simulation with specific input/output
```bash
java Main <input_file> <output_file>
```
