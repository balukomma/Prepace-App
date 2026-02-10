package com.simats.prepace.utils;

public class AppKnowledgeBase {

    public static String getResponse(String query) {
        String lowerQuery = query.toLowerCase().trim();

        // --- GREETINGS ---
        if (lowerQuery.matches(".*\\b(hi|hello|hey|greetings|good morning|good afternoon|good evening)\\b.*")) {
            return "Hello! I'm PrepAce AI. I can help you with:\n\n• App Features & Navigation\n• Quiz Rules & Scoring\n• Account Management\n• Troubleshooting\n\nWhat would you like to know?";
        }

        // --- ACCOUNT & PROFILE ---
        if (lowerQuery.contains("profile") || lowerQuery.contains("account") || lowerQuery.contains("my info")) {
            if (lowerQuery.contains("edit") || lowerQuery.contains("change") || lowerQuery.contains("update")) {
                return "To edit your profile:\n\n1. Go to the **Profile** tab (bottom right).\n2. Tap the **Edit Profile** button.\n3. You can update your Name, Email, and Avatar there.";
            }
            if (lowerQuery.contains("delete") || lowerQuery.contains("remove")) {
                return "To delete your account, please contact our support team via **Profile > Help & Support > Contact Us**. Note that this action is irreversible.";
            }
            return "Your Profile section contains your personal details, statistics, and settings. You can access it by tapping the **Profile** icon in the bottom navigation bar.";
        }

        if (lowerQuery.contains("password") || lowerQuery.contains("forgot") || lowerQuery.contains("reset")) {
            return "If you forgot your password:\n\n1. Log out (if logged in).\n2. On the Login screen, tap **Forgot Password?**.\n3. Enter your registered email to receive a reset link.";
        }

        if (lowerQuery.contains("logout") || lowerQuery.contains("sign out") || lowerQuery.contains("log out")) {
            return "To log out:\n\n1. Go to **Profile**.\n2. Scroll to the bottom.\n3. Tap the **Logout** button.";
        }
        
        if (lowerQuery.contains("avatar") || lowerQuery.contains("photo") || lowerQuery.contains("picture")) {
             return "You can change your profile picture in **Profile > Edit Profile**. Tap on the camera icon to select a new image from your gallery.";
        }

        // --- QUIZZES & CONTENT ---
        if (lowerQuery.contains("quiz") || lowerQuery.contains("test") || lowerQuery.contains("exam")) {
            if (lowerQuery.contains("start") || lowerQuery.contains("take") || lowerQuery.contains("play")) {
                return "To start a quiz:\n\n1. Go to the **Home** or **Quizzes** tab.\n2. Select a Category (Aptitude, Reasoning, Technical).\n3. Choose a topic (e.g., Time & Work).\n4. Read the instructions and tap **Start Quiz**.";
            }
            if (lowerQuery.contains("category") || lowerQuery.contains("type")) {
                return "We offer three main categories:\n\n1. **Aptitude**: Math and numerical skills.\n2. **Reasoning**: Logical thinking and puzzles.\n3. **Technical**: Computer Science (DS, Algo, OS, etc.).";
            }
            if (lowerQuery.contains("history") || lowerQuery.contains("past") || lowerQuery.contains("previous")) {
                return "You can view your past quiz results in **Profile > Quiz History** or by tapping the **View Progress** card on the Home screen.";
            }
            return "PrepAce offers a wide range of quizzes to help you prepare for exams. You can browse them by category in the **Quizzes** tab.";
        }

        if (lowerQuery.contains("aptitude")) {
            return "**Aptitude** covers numerical ability topics like:\n• Time & Work\n• Profit & Loss\n• Simple & Compound Interest\n• Permutation & Combination\n• Probability";
        }
        if (lowerQuery.contains("reasoning")) {
            return "**Reasoning** covers logical topics like:\n• Coding-Decoding\n• Blood Relations\n• Direction Sense\n• Seating Arrangements\n• Syllogisms";
        }
        if (lowerQuery.contains("technical")) {
            return "**Technical** covers CS fundamentals:\n• Data Structures & Algorithms\n• Operating Systems\n• DBMS\n• Computer Networks\n• Java/C++ Programming";
        }

        // --- SCORING & PROGRESS ---
        if (lowerQuery.contains("score") || lowerQuery.contains("point") || lowerQuery.contains("mark")) {
            return "Scoring Rules:\n\n• **Correct Answer**: +10 points (base)\n• **Time Bonus**: Answer quickly for extra points!\n• **Incorrect Answer**: No negative marking (unless specified).\n• **Difficulty Multiplier**: Harder questions give more points.";
        }

        if (lowerQuery.contains("negative") && lowerQuery.contains("mark")) {
            return "Currently, there is **no negative marking** in standard practice quizzes. Feel free to guess if you're stuck!";
        }

        if (lowerQuery.contains("badge") || lowerQuery.contains("achievement") || lowerQuery.contains("reward") || lowerQuery.contains("medal")) {
            return "You earn **Badges** by achieving milestones like:\n\n• Taking your first quiz\n• Getting a perfect score\n• Completing 10 quizzes\n\nCheck your collection in **Profile > Achievements**.";
        }
        
        if (lowerQuery.contains("leaderboard") || lowerQuery.contains("rank") || lowerQuery.contains("standing")) {
             return "Check the **Performance** section (search for 'Performance' or find it in stats) to see your global ranking and how you compare with other students.";
        }
        
        if (lowerQuery.contains("time") && (lowerQuery.contains("spent") || lowerQuery.contains("tracking"))) {
            return "We track your total study time! You can see your **Total Time Spent** on the Home screen dashboard. This counts the actual time you spend active in the app.";
        }

        // --- SETTINGS & PREFERENCES ---
        if (lowerQuery.contains("notification") || lowerQuery.contains("alert")) {
            return "You can manage notifications in **Profile > Settings**. You can toggle specific alerts for new quizzes, daily reminders, or achievements.";
        }
        
        if (lowerQuery.contains("sound") || lowerQuery.contains("volume") || lowerQuery.contains("music") || lowerQuery.contains("vibration")) {
            return "Sound and Vibration settings can be toggled in **Profile > Settings**.";
        }
        
        if (lowerQuery.contains("dark mode") || lowerQuery.contains("theme")) {
            return "PrepAce currently follows your system theme or defaults to Light Mode. We are working on a dedicated Dark Mode toggle for future updates!";
        }

        // --- SUPPORT & TROUBLESHOOTING ---
        if (lowerQuery.contains("bug") || lowerQuery.contains("crash") || lowerQuery.contains("error") || lowerQuery.contains("issue")) {
            return "I'm sorry you're facing an issue! Please try:\n\n1. Restarting the app.\n2. Clearing app cache.\n3. Updating to the latest version.\n\nIf it persists, report it via **Profile > Help & Support**.";
        }
        
        if (lowerQuery.contains("contact") || lowerQuery.contains("support") || lowerQuery.contains("email") || lowerQuery.contains("help")) {
            return "You can contact our support team at **support@prepace.com** or use the **Help & Support** feature in your Profile.";
        }
        
        if (lowerQuery.contains("offline") || lowerQuery.contains("internet") || lowerQuery.contains("wifi")) {
             return "PrepAce requires an active internet connection to load new questions and sync your progress. Please ensure you are connected to Wi-Fi or mobile data.";
        }

        // --- ABOUT ---
        if (lowerQuery.contains("about") || lowerQuery.contains("who are you") || lowerQuery.contains("developer") || lowerQuery.contains("version")) {
            return "I am PrepAce AI, your virtual assistant.\n\n**App Version**: 1.0\n**Developer**: Simats Team\n**Mission**: To help you ace your placement exams!";
        }
        
        if (lowerQuery.contains("privacy") || lowerQuery.contains("data") || lowerQuery.contains("policy")) {
            return "We take your privacy seriously. Your data is encrypted and secure. You can read our full **Privacy Policy** in **Profile > Legal**.";
        }

        // --- DEFAULT FALLBACK ---
        return "I'm not sure I understand that. Try asking about:\n\n• **'How to start a quiz?'**\n• **'How is score calculated?'**\n• **'Edit my profile'**\n• **'Aptitude topics'**\n• **'Contact support'**";
    }
}
