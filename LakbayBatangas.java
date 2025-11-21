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

    // Setup mock data: 6 municipalities, each with 2 spots, each spot with 2 questions
    private void setupMockData() {
        // For brevity and clarity: create 12 spots with 2 questions each
        // Municipality thresholds: municipality 0 unlocked, others require progressive points
        municipalities.add(createMunicipality(0, "Balete Bay", 0,
                new Place[] {
                        new Beach("Silangan Beach", "Gentle waves and sunrise views.", createQs(
                                "What color is the typical sunrise at Silangan?",
                                new String[] {"Blue", "Orange", "Purple"}, 1,
                                "Silangan's traditional snack is called?",
                                new String[] {"Kakanin", "Sinigang", "Adobo"}, 0)),
                        new HeritageSite("Balete Shrine", "A shrine with local folklore.", createQs(
                                "What tree is central to Balete folklore?",
                                new String[] {"Oak", "Balete", "Pine"}, 1,
                                "What item do visitors leave at the shrine?",
                                new String[] {"Coins", "Leaves", "Notes"}, 0))
                }));

        municipalities.add(createMunicipality(1, "Lemery", 10,
                new Place[] {
                        new Mountain("Mount Payapa", "A short hike with panoramic views.", createQs(
                                "How many peaks does Mount Payapa have?",
                                new String[] {"One", "Two", "Three"}, 0,
                                "What bird is often seen on the trail?",
                                new String[] {"Eagle", "Sparrow", "Kingfisher"}, 2)),
                        new HeritageSite("Lemery Plaza", "Town center with historical markers.", createQs(
                                "Which era built Lemery Plaza?",
                                new String[] {"Spanish", "American", "Japanese"}, 0,
                                "Which activity is common at the plaza?",
                                new String[] {"Basketball", "Fishing", "Skiing"}, 0))
                }));

        municipalities.add(createMunicipality(2, "Taal", 20,
                new Place[] {
                        new HeritageSite("Old Town Hall", "Colonial architecture and murals.", createQs(
                                "What style is the Old Town Hall?",
                                new String[] {"Modern", "Colonial", "Art Deco"}, 1,
                                "Which mural theme is displayed?",
                                new String[] {"Harvest", "Space", "Technology"}, 0)),
                        new Mountain("Taal Ridge", "Ridge overlooking the lake.", createQs(
                                "What is the lake near the ridge called?",
                                new String[] {"Laguna", "Taal Lake", "Balete Lake"}, 1,
                                "Ridge hiking is best at what time?",
                                new String[] {"Midnight", "Afternoon", "Morning"}, 2))
                }));

        municipalities.add(createMunicipality(3, "Nasugbu", 30,
                new Place[] {
                        new Beach("Calayo Cove", "Secluded cove with clear water.", createQs(
                                "Calayo Cove is famous for which activity?",
                                new String[] {"Surfing", "Snorkeling", "Skiing"}, 1,
                                "What marine life is commonly seen?",
                                new String[] {"Dolphins", "Penguins", "Polar bears"}, 0)),
                        new Mountain("Pico del Sol", "A hill known for sunsets.", createQs(
                                "Pico del Sol is best visited to watch?",
                                new String[] {"Sunrise", "Noon", "Sunset"}, 2,
                                "What feature defines its summit?",
                                new String[] {"A cross", "A lighthouse", "A bench"}, 0))
                }));

        municipalities.add(createMunicipality(4, "Taysan", 40,
                new Place[] {
                        new HeritageSite("Heritage Museum", "Local crafts and stories.", createQs(
                                "Museum exhibits focus on?",
                                new String[] {"Local crafts", "Space travel", "Cars"}, 0,
                                "What do visitors learn about?",
                                new String[] {"Cuisine", "Planetary motion", "Quantum physics"}, 0)),
                        new Beach("Taysan Shores", "Long stretch of golden sand.", createQs(
                                "Taysan Shores sand is:",
                                new String[] {"Black", "Golden", "White"}, 1,
                                "A recommended activity: ",
                                new String[] {"Sledding", "Beach volleyball", "Ice fishing"}, 1))
                }));

        municipalities.add(createMunicipality(5, "Calaca", 50,
                new Place[] {
                        new Mountain("Calaca Peak", "A challenging climb with rewarding views.", createQs(
                                "Calaca Peak elevation is best described as:",
                                new String[] {"Low", "Medium", "High"}, 1,
                                "What should hikers bring?",
                                new String[] {"Sunscreen", "Ski gear", "Wetsuit"}, 0)),
                        new HeritageSite("Old Lighthouse", "Abandoned lighthouse with legends.", createQs(
                                "The lighthouse guided ships during:",
                                new String[] {"Daytime", "Storms", "Solar eclipses"}, 1,
                                "Legends say the light was powered by:",
                                new String[] {"Magic", "Oil lamp", "Gasoline"}, 1))
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

