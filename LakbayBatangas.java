import java.util.*;

public class LakbayBatangas {
    /* ---------------------------
     * Inner class: Player
     * Encapsulation: private fields, getters/setters, behavior methods.
     * --------------------------- */
    public class Player {
        private String name;
        private int hearts;
        private int points;

        public Player(String name) {
            this.name = name;
            this.hearts = 2; // Start with 2 hearts
            this.points = 0;
        }
        public String getName() {
            return name;
        }
        public int getHearts() {
            return hearts;
        }
        public int getPoints() {
            return points;
        }
        public void addPoints(int p) {
            if (p > 0) {
                points += p;
            }
        }
        public void loseHeart() {
            if (hearts > 0) hearts--;
        }
        public boolean isAlive() {
            return hearts > 0;
        }
        public void restoreHeart() {
            hearts++;
        }
        @Override
        public String toString() {
            return String.format("%s ❤️ x%d  ✨ %d pts", name, hearts, points);
        }
    }
    /* ---------------------------
     * Abstract superclass: Place, Demonstrates abstraction and will be extended by concrete subclasses., Protected fields illustrate protected access in inheritance hierarchy.
     * --------------------------- */
    public abstract class Place {
        protected String name;
        protected String description;
        protected Question[] questions; // Use array for fixed number of spot questions

        public Place(String name, String description, Question[] questions) {
            this.name = name;
            this.description = description;
            this.questions = questions;
        }
        // Abstract method to be overridden by subclasses (polymorphism)
        public abstract void explore(Player player, Scanner scanner);
        // Shared quiz conduction logic used by subclasses
        protected void conductQuiz(Player player, Scanner scanner) {
            System.out.println("----------------------------------------");
            System.out.println(" Quiz: " + name + " (2 questions)");
            System.out.println("----------------------------------------");
            for (int i = 0; i < questions.length; i++) {
                Question q = questions[i];
                try {
                    boolean correct = q.ask(scanner);
                    if (correct) {
                        System.out.println("✅ Correct! +5 points\n");
                        player.addPoints(5);
                    } else {
                        System.out.println("❌ Wrong. -1 heart\n");
                        player.loseHeart();
                        if (!player.isAlive()) {
                            System.out.println("💔 You've lost all hearts!");
                            return;
                        }
                    }
                } catch (InvalidChoiceException ice) {
                    System.out.println("⚠️ " + ice.getMessage() + " Counting as wrong answer.");
                    player.loseHeart();
                    if (!player.isAlive()) {
                        System.out.println("💔 You've lost all hearts!");
                        return;
                    }
                }
            }
            System.out.println("Spot complete! Current points: " + player.getPoints() + " | Hearts: " + player.getHearts());
            System.out.println("----------------------------------------\n");
        }
    }

    /* ---------------------------
     * Subclass: Beach
     * --------------------------- */
    public class Beach extends Place {
        public Beach(String name, String description, Question[] questions) {
            super(name, description, questions); // use of super()
        }

        @Override
        public void explore(Player player, Scanner scanner) {
            System.out.println("🏖️  Welcome to " + name + " — a beautiful beach spot!");
            System.out.println(description);
            conductQuiz(player, scanner);
        }
    }

    /* ---------------------------
     * Subclass: Mountain
     * --------------------------- */
    public class Mountain extends Place {
        public Mountain(String name, String description, Question[] questions) {
            super(name, description, questions);
        }

        @Override
        public void explore(Player player, Scanner scanner) {
            System.out.println("🏔️  You're at " + name + " — a scenic mountain trail!");
            System.out.println(description);
            conductQuiz(player, scanner);
        }
    }

    /* ---------------------------
     * Subclass: HeritageSite
     * --------------------------- */
    public class HeritageSite extends Place {
        public HeritageSite(String name, String description, Question[] questions) {
            super(name, description, questions);
        }

        @Override
        public void explore(Player player, Scanner scanner) {
            System.out.println("🏛️  Visiting " + name + " — a cultural heritage site.");
            System.out.println(description);
            conductQuiz(player, scanner);
        }
    }

    /* ---------------------------
     * Inner class: Question
     * Represents a multiple-choice question. */
    public class Question {
        private String prompt;
        private String[] options;
        private int correctIndex; // 0-based

        public Question(String prompt, String[] options, int correctIndex) {
            this.prompt = prompt;
            this.options = options;
            this.correctIndex = correctIndex;
        }

        /**
         * Asks the question via console. Returns true if correct, false otherwise.
         * Throws InvalidChoiceException for invalid numeric choices.
         */
        public boolean ask(Scanner scanner) throws InvalidChoiceException {
            System.out.println("\n" + prompt);
            for (int i = 0; i < options.length; i++) {
                System.out.printf("  %d. %s%n", i + 1, options[i]);
            }
            System.out.print("Your answer (enter number): ");
            String line = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(line);
                if (choice < 1 || choice > options.length) {
                    throw new InvalidChoiceException("Choice out of range.");
                }
                return (choice - 1) == correctIndex;
            } catch (NumberFormatException nfe) {
                throw new InvalidChoiceException("Invalid input; expected a number.");
            }
        }
    }

    /* ---------------------------
     * Inner class: Municipality
     * Holds exactly 2 tourist spots (array) and has unlocking metadata.
     * --------------------------- */
    public class Municipality {
        private String name;
        private Place[] spots; // exactly 2 spots per requirement
        private int index; // identifier order
        private boolean unlocked;
        private int unlockThreshold; // points required

        public Municipality(String name, int index, int unlockThreshold, Place[] spots) {
            this.name = name;
            this.index = index;
            this.unlockThreshold = unlockThreshold;
            this.spots = spots;
            // By default, index 0 is unlocked
            this.unlocked = index == 0 || unlockThreshold <= 0;
        }

        public String getName() {
            return name;
        }

        public boolean isUnlocked() {
            return unlocked;
        }

        public void tryUnlock(Player player) {
            if (!unlocked && player.getPoints() >= unlockThreshold) {
                unlocked = true;
                System.out.println("🔓 Municipality unlocked: " + name + " (requires " + unlockThreshold + " pts).");
            }
        }

        public Place[] getSpots() {
            return spots;
        }

        public int getIndex() {
            return index;
        }

        public int getUnlockThreshold() {
            return unlockThreshold;
        }

        @Override
        public String toString() {
            return String.format("%s %s (Req: %d pts)", name, (unlocked ? "🔓" : "🔒"), unlockThreshold);
        }
    }

    /* ---------------------------
     * Inner class: UnlockSystem
     * Manages which municipalities unlock at thresholds.
     * --------------------------- */
    public class UnlockSystem {
        private List<Municipality> municipalities;

        public UnlockSystem(List<Municipality> municipalities) {
            this.municipalities = municipalities;
        }

        // Check all municipalities and unlock what the player has earned
        public void refreshUnlocks(Player player) {
            for (Municipality m : municipalities) {
                m.tryUnlock(player);
            }
        }
    }

    /* ---------------------------
     * Inner class: Leaderboard
     * Stores multiple players in runtime with sorted insertion.
     * --------------------------- */
    public class Leaderboard {
        // Simple holder for name + points
        private class Entry {
            String name;
            int points;
            Entry(String name, int points) {
                this.name = name;
                this.points = points;
            }
        }

        private List<Entry> entries;

        public Leaderboard() {
            entries = new ArrayList<>();
        }

        // Sorted insert (descending by points)
        public void add(String playerName, int points) {
            Entry newEntry = new Entry(playerName, points);
            int idx = 0;
            while (idx < entries.size() && entries.get(idx).points >= points) {
                idx++;
            }
            entries.add(idx, newEntry);
        }

        public void display() {
            System.out.println("\n========================================");
            System.out.println("🏆 Leaderboard — Lakbay Batangas");
            System.out.println("========================================");
            if (entries.isEmpty()) {
                System.out.println("(No records yet.)");
            } else {
                int rank = 1;
                for (Entry e : entries) {
                    System.out.printf(" %2d. %s — %d pts%n", rank++, e.name, e.points);
                }
            }
            System.out.println("========================================\n");
        }
    }

    /* ---------------------------
     * Inner class: InvalidChoiceException
     * Custom exception for invalid menu/question choices.
     * --------------------------- */
    public class InvalidChoiceException extends Exception {
        public InvalidChoiceException(String message) {
            super(message);
        }
    }

    /* ---------------------------
     * Main game flow
     * --------------------------- */
    private Scanner scanner;
    private Leaderboard leaderboard;
    private UnlockSystem unlockSystem;
    private List<Municipality> municipalities;

    public LakbayBatangas() {
        scanner = new Scanner(System.in);
        leaderboard = new Leaderboard();
        municipalities = new ArrayList<>();
        setupMockData();
        unlockSystem = new UnlockSystem(municipalities);
    }

    // Setup mock data: 6 municipalities, each with 2 spots, each spot with 1 question
    private void setupMockData() {
        // For brevity and clarity: create 12 spots with 1 question each
        // Municipality thresholds: municipality 0 unlocked, others require progressive points
        municipalities.add(createMunicipality(0, "Taal", 0,
                new Place[] {
                        new Mountain("Taal Volcano", "One of the popular attractions in this municipality is the Taal Volcano.", createQs(
                                "It is known for what unique geographical features?",
                                new String[] {"A. Volcano within a lake, and a lake within a volcano", "B. Volcano between two mountains", "C. Volcano in the middle of the ocean, and a lake within a volcano"}, 1,),
                        new HeritageSite("Taal Basilica", "Officially the Minor Basilica of Saint Martin of Tours,  the architecture blends Baroque and Neoclassical styles, featuring a grand altar, stained glass windows, and a dome ceiling. It was built between 1856 and 1878, was designated a national historical landmark in 1974, and has survived earthquakes in 2017 and the 2020 Taal Volcano eruption.", createQs(
                                " It is famous for its title as the?",
                                new String[] {"A. The oldest church in the Philippines", "B. The largest church in the Philippines and Asia", "C. The most preserved church in Philippines and Asia"}, 1,)
                }));

        municipalities.add(createMunicipality(1, "Lemery", 10,
                new Place[] {
                        new HeritageSite("Fantasy World", "A medieval-themed amusement park. The main attraction is the large, colorful castle, but it also features other structures like a throne room, fountains, and a treehouse.", createQs(
                                "What fact about the this place is?",
                                new String[] {"A. Was never completed due to financial issues", "B. One of the most popular fantasy world in Philippines", "C. Restricted to public due to risk of collapsing due to unfinished construction"}, 0,),
                        new Beach("Lakeshore Area", "This quiet lakeshore area offers serene views of Taal Lake and is visited mostly by locals rather than tourists.", createQs(
                                "It is known for?",
                                new String[] {"A. Having a hidden natural hot spring that feeds into the lake", "B. Being a peaceful spot ideal for sunrise viewing and lakeside picnics", "C. Hosting the largest fish market in Batangas"}, 0,)
                }));

        municipalities.add(createMunicipality(2, "Mabini", 20,
                new Place[] {
                        new Mountain("Mount Gulugod Baboy", "Also known as Mount Gulbab, is a popular hiking destination in Mabini, Batangas, Philippines, known for its rolling hills that resemble a pig's spine. With stunning views of the surrounding mountains, neighboring islands, and coastline at the top, the hike is rated as easy to moderate and appropriate for novices.", createQs(
                                "The highest peak, Pinagbanderahan meaning?",
                                new String[] {"A. Where the flag was hoisted", "B. Endless assault", "C. Pig's spine"}, 1,),
                        new Beach("Camp Netanya Resort and Spa", "A popular resort, known for its Greek-style architecture, stunning ocean views, and access to the rich marine sanctuary. Guests can enjoy activities such as snorkeling and kayaking, diving, and boat tours.", createQs(
                                "Which one is true?",
                                new String[] {"A. It is known for being a  Santorini-inspired beach resort", "B. It is known for being an Italy-inspired beach resort", "C. It is known for being a  Rome-inspired beach resort"}, 1,)
                }));

        municipalities.add(createMunicipality(3, "Laurel", 30,
                new Place[] {
                        new Beach("Simbahan Bato", "There are many mythical stories about simbahang bato most commonly known as Kapilya ni San Gabriel Archangel in Laurel, Batangas. According to the elderly people who lived there, this place was already sacred even before it was turned into a church. Some elders said that they could hear beautiful and unique music coming from the cave.", createQs(
                                "There is also a story that says that this cave is?",
                                new String[] {"A. That this cave serve as a refuge for the animals back in the day", "B. served as a hiding place for people during Spanish and American war", "C. Ancient people use to do sacred rituals inside the cave"}, 1),
                        new Beach("Ambon-Ambon Falls", " A tall, multi-tiered waterfall near Taal Lake, estimated to be around 60 meters high, with a name that means drizzle due to the light spray it creates.", createQs(
                                "The falls are located in Laurel and are accessed via — from the Las Haciendas Estates jump-off point.",
                                new String[] {"A. It can be reached by a paved road that leads directly to the waterfall", "B. It requires trekking through a forested trail and river pathways before reaching the falls", "C. It is an artificial waterfall created as part of a resort development"}, 2,)
                }));

        municipalities.add(createMunicipality(4, "Batangas", 40,
                new Place[] {
                        new Mountain("Mt. Banoy", "A popular hiking destination located in Batangas City, Batangas. It is known for being a beginner-friendly mountain with well-established trails, scenic views of Batangas Bay, and a vantage point overlooking the city. Many hikers visit Mt. Banoy for day hikes because of its accessibility and relatively moderate difficulty.", createQs(
                                "What is true about Mt. Banoy?",
                                new String[] {"A. It is a volcanic mountain located at the boundary of Batangas and Laguna", "B. It is a mountain in Batangas City popular for day hikes and views of Batangas Bay", "C. It is the tallest mountain in Luzon, surpassing Mt. Pulag"}, 0,),
                        new Mountain("Nacpan Point", "A quiet viewpoint area with a cliffside overlooking Batangas Bay.", createQs(
                                "Nacpan Point is visited mainly for:",
                                new String[] {"A. Extreme rock climbing", "B. Sunset viewing and panoramic photos of the coastline", "C. Its century-old lighthouse"}, 1,)
                }));

        municipalities.add(createMunicipality(5, "Cuenca", 50,
                new Place[] {
                        new Mountain("Lumampao", "Known for the Lumampao Viewdeck, a tourist spot offering scenic views of rolling hills and valleys, especially famous for its sunset panoramas. Cuenca itself is a municipality recognized for its rich culture, natural beauty.", createQs(
                                "Sitio Ilaya View Deck is mostly visited for:",
                                new String[] {"A. Its strong winds perfect for paragliding", "B. Its elevated spot ideal for overlooking Taal Lake", "C. Its underground tunnels"}, 1,),
                        new Mountain("Mt. Maculot", "a popular, dormant stratovolcano in Cuenca, Batangas, Philippines, it features a main destination called — . The mountain is a significant tourist attraction and a popular destination for day hikes, especially for beginners.", createQs(
                                "What is the main destination called?",
                                new String[] {"A. Grotto Viewpoint", "B. Summit", "C. Rockies Viewpoint"}, 1,)
                }));
    }

    // helper to create municipality with index and two places
    private Municipality createMunicipality(int index, String name, int threshold, Place[] spots) {
        return new Municipality(name, index, threshold, spots);
    }

    // helper to create two questions array for a spot
    private Question[] createQs(String p1, String[] opts1, int c1, String p2, String[] opts2, int c2) {
        return new Question[] { new Question(p1, opts1, c1), new Question(p2, opts2, c2) };
    }

    // Entry point for playing the game
    public void play() {
        printBanner();
        System.out.print("Enter your name, traveler: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = "Wanderer";
        Player player = new Player(name);

        System.out.println("\nWelcome, " + player.getName() + "! 🗺️  Ready to explore Lakbay Batangas.");
        boolean exit = false;

        // Initially refresh unlocks (first municipality unlocked)
        unlockSystem.refreshUnlocks(player);

        while (!exit && player.isAlive()) {
            try {
                showStatus(player);
                System.out.println("Choose a municipality to visit (or 0 to quit):");
                for (int i = 0; i < municipalities.size(); i++) {
                    Municipality m = municipalities.get(i);
                    System.out.printf("  %d. %s %s%n", i + 1, m.getName(), (m.isUnlocked() ? "🔓" : "🔒"));
                }
                System.out.print("Selection: ");
                String line = scanner.nextLine().trim();
                int choice;
                try {
                    choice = Integer.parseInt(line);
                } catch (NumberFormatException nfe) {
                    throw new InvalidChoiceException("Please enter a valid number for municipality.");
                }
                if (choice == 0) {
                    System.out.println("Thanks for visiting Lakbay Batangas! Safe travels.");
                    break;
                }
                if (choice < 1 || choice > municipalities.size()) {
                    throw new InvalidChoiceException("Municipality choice out of range.");
                }
                Municipality selected = municipalities.get(choice - 1);
                if (!selected.isUnlocked()) {
                    System.out.println("🚫 This municipality is still locked. Earn more points to unlock it! Required: "
                            + selected.getUnlockThreshold() + " pts.");
                    // show hint: how to earn more points
                    System.out.println("Tip: Complete other spots and answer questions correctly (+5 pts each).");
                    continue;
                }

                // Choose a spot within municipality
                chooseSpot(selected, player);

                // After exploring a spot, update unlocks and check game over
                unlockSystem.refreshUnlocks(player);

                if (!player.isAlive()) {
                    System.out.println("\nGAME OVER 💀 — " + player.getName() + " has no hearts left.");
                    break;
                }

                // Offer to continue or quit
                System.out.print("Continue exploring? (y/n): ");
                String cont = scanner.nextLine().trim().toLowerCase();
                if (!cont.equals("y") && !cont.equals("yes")) {
                    exit = true;
                }

            } catch (InvalidChoiceException ice) {
                System.out.println("⚠️ " + ice.getMessage());
            } catch (Exception e) {
                System.out.println("⚠️ Unexpected error: " + e.getMessage());
            }
        }

        // Save to leaderboard and show results
        leaderboard.add(player.getName(), player.getPoints());
        System.out.println("\nFinal Score: " + player.getPoints() + " pts");
        leaderboard.display();
        System.out.println("Thank you for playing Lakbay Batangas! 🌴");
    }

    // Show player's current status with visual elements
    private void showStatus(Player player) {
        System.out.println("\n========================================");
        System.out.println("Player: " + player.getName() + " | Hearts: " + player.getHearts() + " | Points: " + player.getPoints());
        System.out.println("========================================");
    }

    // Choose a spot within a municipality and explore via polymorphic Place objects
    private void chooseSpot(Municipality m, Player player) throws InvalidChoiceException {
        System.out.println("\nYou arrived at: " + m.getName() + " — choose a tourist spot:");
        Place[] spots = m.getSpots();
        for (int i = 0; i < spots.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, spots[i].name);
        }
        System.out.print("Selection (1-2): ");
        String line = scanner.nextLine().trim();
        int sc;
        try {
            sc = Integer.parseInt(line);
        } catch (NumberFormatException nfe) {
            throw new InvalidChoiceException("Invalid spot selection; expected a number.");
        }
        if (sc < 1 || sc > spots.length) {
            throw new InvalidChoiceException("Spot selection out of range.");
        }
        Place chosenSpot = spots[sc - 1];
        // Polymorphism: explore() will call subclass override
        chosenSpot.explore(player, scanner);
    }

    private void printBanner() {
        System.out.println("========================================");
        System.out.println("⭐ Welcome to LAKBAY BATANGAS ⭐");
        System.out.println("Cultural Exploration Game — Answer quizzes, earn points, unlock places!");
        System.out.println("Hearts: 2 | Wrong answer = -1 heart | Correct = +5 pts");
        System.out.println("========================================\n");
    }

    /* ---------------------------
     * main method
     * --------------------------- */
    public static void main(String[] args) {
        LakbayBatangas game = new LakbayBatangas();
        game.play();
    }
}


