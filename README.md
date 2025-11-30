1. Project Title
  -Lakbay Batangas: A Console-Based Cultural Exploration Game

2. Description / Overview
  -Lakbay Batangas is a fun and interactive Java console game that helps players understand the interesting tourist spots
in Batangas through their playing experience. The player travels around different towns, responds to quiz questions, 
collects points, and opens new places. The game reenacts the experience of exploration, making choices, and advancing —
all the while incorporating solid Object-Oriented Programming (OOP) principles that are part of the 2nd-year IT/CS curriculum.

The game demonstrates:
  ✔ OOP Principles (Encapsulation, Inheritance, Abstraction, Polymorphism)
  ✔ Custom Exceptions
  ✔ Arrays and ArrayList usage
  ✔ Input handling & validation
  ✔ Console-based UI with game logic

3. OOP Concepts Applied
3.1 Encapsulation
  -All sensitive fields (e.g., Player.name, hearts, points) are declared private.
  -Accessed through getters (getHearts()), setters or modifying methods (loseHeart(), addPoints()).
  -Prevents accidental modification and maintains internal consistency.
3.2 Inheritance
Abstract superclass Place is extended by:
  -Beach
  -Mountain
  -HeritageSite
  -Each subclass inherits shared fields (name, description, questions) and behavior.

3.3 Polymorphism
  -Place declares an abstract method explore(Player, Scanner).
  -Subclasses (Beach, Mountain, HeritageSite) override explore() with unique descriptions.
  -Dynamic runtime binding occurs in:
 # chosenSpot.explore(player, scanner);

 3.4 Abstraction
  -Place is an abstract class, hiding quiz mechanics inside conductQuiz().
  -InvalidChoiceException abstracts error handling for invalid inputs.

3.5 Exception Handling
-Uses custom InvalidChoiceException for invalid numeric choices.
Try-catch blocks handle:
  -Invalid quiz choices
  -Invalid menu inputs
  -Non-numeric inputs
  -Prevents crashes and maintains smooth gameplay.

3.6 Arrays & Collections
  -Question[] → fixed-size array for spot quizzes
  -ArrayList<Municipality> → dynamic list of all municipalities
ArrayList used in:
  -Leaderboard entries
  -Unlock system
  -Player notifications

4. Program Structure
  -Main Class
  -LakbayBatangas
  -Handles game loop, menus, initialization, and core gameplay.
| Class                               | Role                                                       |
| ----------------------------------- | ---------------------------------------------------------- |
| **Player**                          | Stores player stats, hearts, points, and status.           |
| **Place (abstract)**                | Parent class for all tourist spots. Defines quiz behavior. |
| **Mountain / Beach / HeritageSite** | Subclasses, each implementing `explore()` polymorphically. |
| **Question**                        | Handles multiple-choice questions & validation.            |
| **Municipality**                    | Contains 2 tourist spots and unlock requirements.          |
| **UnlockSystem**                    | Handles unlocking municipalities based on points.          |
| **Leaderboard**                     | Stores and displays sorted player scores.                  |
| **InvalidChoiceException**          | Custom exception for invalid user choices.                 |

 LakbayBatangas
 ├── Player
 ├── Place (abstract)
 │     ├── Mountain
 │     ├── Beach
 │     └── HeritageSite
 ├── Municipality
 ├── UnlockSystem
 ├── Leaderboard
 │      └── Entry (inner)
 ├── Question
 └── InvalidChoiceException

5. How to Run the Program
  Step 1 — Compile
    -Open terminal inside the folder containing LakbayBatangas.java:
    # javac LakbayBatangas.java
  Step 2 — Run
    # java LakbayBatangas

6. Sample Output
========================================
✴ Welcome to LAKBAY BATANGAS ✴
Cultural Exploration Game — Answer quizzes, earn points, unlock places!
Hearts: 2 | Wrong answer = -1 heart | Correct = +5 pts
========================================

Enter your name, traveler: Juan

Welcome, Juan! 🗺️  Ready to explore Lakbay Batangas.

Player: Juan | Hearts: 2 | Points: 0
Choose a municipality to visit:
  1. Taal 🗝
  2. Lemery ✉
  3. Mabini ✉
  4. Laurel ✉
  5. Batangas ✉
  6. Cuenca ✉
Selection: 1

You arrived at: Taal — choose a tourist spot:
  1. Taal Volcano
  2. Basilica of St. Martin de Tours
Selection: 1

🏔️  You're at Taal Volcano...
Quiz: Taal Volcano (2 questions)
✓ Correct! +5 points
✘ Wrong. -1 heart

Spot complete! Current points: 5 | Hearts: 1

7. Author & Acknowledgements

Author:
LIM, LAWRENCE ALJERUH B.
MAGO, CHARIZ MAE C.
LOREGAS, JOHNMARK
MARASIGAN, CLARK
BSIT – 2nd Year

Acknowledgments:
  -Sir Jayson Abratique
  -Resources on Batangas tourism information

8. Optional Sections
Future Enhancements
  -Save/load game progress (file handling)
  -More municipalities & spots
  -Sound effects / GUI version
  -Timer-based answering
  -Story mode / achievements

References
-Official Batangas tourism pages
-Course modules on OOP principles
