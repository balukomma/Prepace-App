import json
import re
import os

# Paths
ASSETS_PATH = r"c:\Prepace\app\src\main\assets\quiz_data.json"
SQL_FILES = [
    r"c:\Prepace\questions.sql",
    r"c:\Prepace\questions_reasoning.sql",
    r"c:\Prepace\questions_technical.sql"
]

def parse_sql_file(file_path):
    print(f"Parsing {file_path}...")
    questions_data = {} # {Category: {Topic: [Questions]}}
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
        
    # Regex to match values: ('Cat', 'Topic', 'Q', 'Op1', 'Op2', 'Op3', 'Op4', 'Ans')
    # Handling escaped single quotes like \'
    pattern = re.compile(r"\('([^']*(?:\\.[^']*)*)', '([^']*(?:\\.[^']*)*)', '([^']*(?:\\.[^']*)*)', '([^']*(?:\\.[^']*)*)', '([^']*(?:\\.[^']*)*)', '([^']*(?:\\.[^']*)*)', '([^']*(?:\\.[^']*)*)', '([^']*(?:\\.[^']*)*)'\)")
    
    matches = pattern.findall(content)
    print(f"Found {len(matches)} questions in {file_path}")
    
    for match in matches:
        category = match[0].replace(r"\'", "'")
        topic = match[1].replace(r"\'", "'")
        question_text = match[2].replace(r"\'", "'")
        option1 = match[3].replace(r"\'", "'")
        option2 = match[4].replace(r"\'", "'")
        option3 = match[5].replace(r"\'", "'")
        option4 = match[6].replace(r"\'", "'")
        answer = match[7].replace(r"\'", "'")
        
        # Determine correct answer index
        options = [option1, option2, option3, option4]
        try:
            correct_index = options.index(answer)
        except ValueError:
            print(f"Warning: Answer '{answer}' not found in options for question: {question_text}")
            correct_index = 0 # Default to 0 if mismatch
            
        question_obj = {
            "questionText": question_text,
            "optionA": option1,
            "optionB": option2,
            "optionC": option3,
            "optionD": option4,
            "correctAnswerIndex": correct_index,
            "explanation": "" # Empty as per plan
        }
        
        if category not in questions_data:
            questions_data[category] = {}
        if topic not in questions_data[category]:
            questions_data[category][topic] = []
            
        questions_data[category][topic].append(question_obj)
        
    return questions_data

def update_json_data(sql_data):
    if not os.path.exists(ASSETS_PATH):
        print(f"Error: {ASSETS_PATH} not found.")
        return

    with open(ASSETS_PATH, 'r', encoding='utf-8') as f:
        try:
            data = json.load(f)
        except json.JSONDecodeError:
            print("Error decoding existing JSON. Starting fresh or aborting.")
            return

    existing_categories = {cat["name"]: cat for cat in data.get("categories", [])}
    
    total_added = 0
    
    # Iterate over parsed SQL data and update/create structure
    for category_name, topics in sql_data.items():
        if category_name not in existing_categories:
            print(f"Creating new category: {category_name}")
            new_cat = {"name": category_name, "quizzes": []}
            data["categories"].append(new_cat)
            existing_categories[category_name] = new_cat
            
        cat_obj = existing_categories[category_name]
        # We want to replace quizzes for this category or append? Plan said overwrite.
        # But we need to map "Topic" (SQL) to "Quiz Title" (JSON)
        
        # Let's clear existing quizzes for this category to ensure clean state as per "update all"
        # Verify if we should clear. User said "update all questions".
        # I will clear the quizzes list for the target categories and rebuild.
        cat_obj["quizzes"] = []
        
        for topic_name, questions in topics.items():
            quiz_obj = {
                "title": topic_name,
                "difficulty": "Medium",
                "description": f"Practice questions for {topic_name}",
                "time_limit": 15,
                "questions": questions
            }
            cat_obj["quizzes"].append(quiz_obj)
            total_added += len(questions)
            
    print(f"Total questions processed and staged: {total_added}")
    
    with open(ASSETS_PATH, 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2)
    print("Successfully updated quiz_data.json")

def main():
    all_data = {}
    for sql_file in SQL_FILES:
        if os.path.exists(sql_file):
            file_data = parse_sql_file(sql_file)
            # Merge into all_data
            for cat, topics in file_data.items():
                if cat not in all_data:
                    all_data[cat] = {}
                for topic, questions in topics.items():
                    if topic not in all_data[cat]:
                        all_data[cat][topic] = []
                    all_data[cat][topic].extend(questions)
        else:
            print(f"File not found: {sql_file}")

    if all_data:
        update_json_data(all_data)
    else:
        print("No data parsed.")

if __name__ == "__main__":
    main()
