import json
import sys

# Read the quiz data
with open(r'C:\Prepace\app\src\main\assets\quiz_data.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

# Define difficulty mappings for each quiz title
difficulty_map = {
    # Aptitude - Easy topics
    "Percentage": "Easy",
    "Simple Interest": "Easy",
    "Algebra": "Easy",
    "Quantitative Aptitude": "Easy",
    
    # Aptitude - Medium topics
    "Profit & Loss": "Medium",
    "Ages": "Medium",
    "Ratio & Proportion": "Medium",
    "Averages": "Medium",
    "Number System": "Medium",
    
    # Aptitude - Hard topics
    "Time & Work": "Hard",
    "Time & Distance": "Hard",
    "Compound Interest": "Hard",
    "Permutations & Combinations": "Hard",
    "Probability": "Hard",
    
    # Reasoning - Easy topics
    "Logical Reasoning": "Easy",
    "Series Completion": "Easy",
    "Coding-Decoding": "Easy",
    "Blood Relations": "Easy",
    
    # Reasoning - Medium topics
    "Verbal Reasoning": "Medium",
    "Analogies": "Medium",
    "Classification": "Medium",
    "Direction Sense": "Medium",
    
    # Reasoning - Hard topics
    "Analytical Reasoning": "Hard",
    "Syllogism": "Hard",
    "Data Sufficiency": "Hard",
    "Puzzles": "Hard",
    
    # Technical - Easy topics
    "Programming Basics": "Easy",
    "HTML & CSS": "Easy",
    "Database Basics": "Easy",
    "Computer Fundamentals": "Easy",
    
    # Technical - Medium topics
    "Java Programming": "Medium",
    "Python Programming": "Medium",
    "SQL": "Medium",
    "Operating Systems": "Medium",
    "Computer Networks": "Medium",
    
    # Technical - Hard topics
    "Data Structures": "Hard",
    "Algorithms": "Hard",
    "System Design": "Hard",
    "Advanced Programming": "Hard",
}

# Update difficulties
updated_count = 0
for category in data['categories']:
    for quiz in category['quizzes']:
        title = quiz['title']
        if title in difficulty_map:
            old_difficulty = quiz['difficulty']
            quiz['difficulty'] = difficulty_map[title]
            if old_difficulty != quiz['difficulty']:
                updated_count += 1
                print(f"Updated '{title}': {old_difficulty} -> {quiz['difficulty']}")

# Write back to file
with open(r'C:\Prepace\app\src\main\assets\quiz_data.json', 'w', encoding='utf-8') as f:
    json.dump(data, f, indent=2, ensure_ascii=False)

print(f"\nTotal quizzes updated: {updated_count}")
print("Quiz difficulty levels have been successfully varied!")
